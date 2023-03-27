package com.example.internshiptask;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private final ArrayList<User> userList;
    private final OnClickDelete onClickDelete;
    private final OnClickEdit onClickEdit;

    public UserAdapter(ArrayList<User> userList, OnClickDelete onClickDelete, OnClickEdit onClickEdit) {
        this.userList = userList;
        this.onClickDelete = onClickDelete;
        this.onClickEdit = onClickEdit;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(userList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public interface OnClickDelete {
        void onDelete(int index);
    }

    public interface OnClickEdit {
        void onEdit(int index);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvPhone;
        private final TextView tvEmail;
        private final TextView tvDate;
        private final ImageView imageView;
        private final Button btnDelete;
        private final Button btnEdit;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvDate = itemView.findViewById(R.id.tvDate);
            imageView = itemView.findViewById(R.id.imageView);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }

        public void bind(User user, final int index) {
            tvName.setText(user.getName());
            tvPhone.setText(user.getPhoneNumber());
            tvEmail.setText(user.getEmailAdd());
            tvDate.setText(user.getBirthDate());
            try {
                imageView.setImageURI(Uri.parse(user.getImageUri()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            btnDelete.setOnClickListener(v -> onClickDelete.onDelete(index));
            btnEdit.setOnClickListener(v -> onClickEdit.onEdit(index));
        }
    }
}

