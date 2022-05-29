package com.niken.assignment.ui.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.niken.assignment.R;
import com.niken.assignment.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserRecyclerAdapter extends  RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder>{
    private Context context;
    private ArrayList<User> userList;

    //view
    private CircleImageView profileImageView;
    private TextView userNameView;
    private TextView emailView;
    private TextView mobileNumberView;
    private TextView createdDateView;
    private TextView deviceTypeView;

    public UserRecyclerAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User currentUser = userList.get(position);
        userNameView.setText(currentUser.getName());
        emailView.setText(currentUser.getEmail());
        mobileNumberView.setText(currentUser.getPhoneNumber());
        SimpleDateFormat dateFormat =new SimpleDateFormat("MMMM dd,yyyy", Locale.getDefault());
        String createdDate = dateFormat.format(currentUser.getCreatedDate());
        Glide.with(context).load(currentUser.getProfileImage()).into(profileImageView);
        createdDateView.setText(createdDate);
        deviceTypeView.setText(currentUser.getTypeOfDevice());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profile_image_view);
            userNameView = itemView.findViewById(R.id.username_view);
            emailView = itemView.findViewById(R.id.email_address_view);
            mobileNumberView = itemView.findViewById(R.id.mobile_number_view);
            createdDateView = itemView.findViewById(R.id.created_date_view);
            deviceTypeView = itemView.findViewById(R.id.device_type_view);
        }
    }
}
