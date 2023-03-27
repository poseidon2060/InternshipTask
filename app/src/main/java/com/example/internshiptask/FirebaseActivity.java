package com.example.internshiptask;

import static androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class FirebaseActivity extends AppCompatActivity {

    private final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private String uriData = null;
    final private Integer REQUEST_CODE = 100;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<User, UserHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        recyclerView = findViewById(R.id.recyclerView);

        //loading data
        fetchData();

        //Date Picker
        TextView tvDatePicker = findViewById(R.id.tvDatePicker);
        tvDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        //Image Select
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new PickVisualMedia(), uri -> {
                    if (uri != null) {
                        uriData = uri.toString();
                    } else {
                        Log.d("Photo picker", "No media selected");
                    }
                });

        Button btnChooseImage = findViewById(R.id.btnChooseImage);
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        //Save Button
        EditText etName = findViewById(R.id.etName);
        EditText etPhoneNumber = findViewById(R.id.etPhoneNumber);
        EditText etEmail = findViewById(R.id.etEmail);
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String phone = etPhoneNumber.getText().toString();
                String email = etEmail.getText().toString();
                String date = ((TextView) findViewById(R.id.tvDatePicker)).getText().toString();
                if (uriData != null) {
                    uploadToFirebase(Uri.parse(uriData), new OnUploadFinishedListener() {
                        @Override
                        public void onUploadFinished(String uploadedUri) {
                            String uri = uploadedUri;
                            User user = new User(name, phone, email, date, uri);

                            try {
                                addUser(user);
                            } catch (Exception e) {
                                Log.d("MYTAG", e.getMessage());
                            }

                            etName.setText("");
                            etPhoneNumber.setText("");
                            etEmail.setText("");
                            tvDatePicker.setText("");
                            uriData = "";
                        }
                    });
                } else {
                    String uri = uriData;
                    User user = new User(name, phone, email, date, uri);

                    try {
                        addUser(user);
                    } catch (Exception e) {
                        Log.d("MYTAG", e.getMessage());
                    }

                    etName.setText("");
                    etPhoneNumber.setText("");
                    etEmail.setText("");
                    tvDatePicker.setText("");
                    uriData = "";
                }

            }
        });
    }

    private void addUser(User user) {
        myRef.child(String.valueOf(System.currentTimeMillis())).setValue(user);
    }

    class UserHolder extends RecyclerView.ViewHolder {

        private final TextView tvName;
        private final TextView tvPhone;
        private final TextView tvEmail;
        private final TextView tvDate;
        private final ImageView imageView;
        private final Button btnDelete;
        private final Button btnEdit;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvDate = itemView.findViewById(R.id.tvDate);
            imageView = itemView.findViewById(R.id.imageView);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }

        public void bind(User user) {
            tvName.setText(user.getName());
            tvPhone.setText(user.getPhoneNumber());
            tvEmail.setText(user.getEmailAdd());
            tvDate.setText(user.getBirthDate());
            try {
                Picasso.get().load(user.getImageUri()).into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
            btnDelete.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    adapter.getRef(position).removeValue();
                }
            });
            btnEdit.setOnClickListener(v -> {
                User temp;
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    temp = adapter.getItem(position);
                    adapter.getRef(position).removeValue();

                    EditText etName = findViewById(R.id.etName);
                    EditText etPhoneNumber = findViewById(R.id.etPhoneNumber);
                    EditText etEmail = findViewById(R.id.etEmail);
                    TextView tvDatePicker = findViewById(R.id.tvDatePicker);

                    etName.setText(temp.getName());
                    etEmail.setText(temp.getEmailAdd());
                    etPhoneNumber.setText(temp.getPhoneNumber());
                    tvDatePicker.setText(temp.getBirthDate());
                    uriData = temp.getImageUri();
                }
            });
        }
    }

    private void fetchData() {

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users");

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<User, UserHolder>(options) {
            @NonNull
            @Override
            public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_item, parent, false);

                return new UserHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserHolder holder, int position, @NonNull User model) {
                holder.bind(model);
            }
        };

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(FirebaseActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String date = day + "/" + (month + 1) + "/" + year;
                        ((TextView) findViewById(R.id.tvDatePicker)).setText(date);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void uploadToFirebase(Uri uri, final OnUploadFinishedListener listener) {
        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        uriData = uri.toString();
                        Log.d("URIData", uriData);
                        Toast.makeText(FirebaseActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        listener.onUploadFinished(uriData);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FirebaseActivity.this, "Uploading failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    interface OnUploadFinishedListener {
        void onUploadFinished(String uploadedUri);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}