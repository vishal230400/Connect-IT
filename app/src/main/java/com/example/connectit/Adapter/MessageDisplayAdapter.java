package com.example.connectit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.connectit.Model.Messenger;
import com.example.connectit.Model.User;
import com.example.connectit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MessageDisplayAdapter extends RecyclerView.Adapter<MessageDisplayAdapter.ViewHolder> {


    public List<Messenger> messagesList;
    public Context mContext;
    public FirebaseUser firebaseUser;

    public MessageDisplayAdapter(List<Messenger> messagesList, Context mContext) {
        this.messagesList = messagesList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
        if(viewType==1)
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.messagesdisplay_user, parent, false);
        }
        else
            {
            view = LayoutInflater.from(mContext).inflate(R.layout.messagesdisplay, parent, false);
            }
        return new MessageDisplayAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        holder.message.setText(messagesList.get(position).getMessage());
        holder.setIsRecyclable(false);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");

        if(messagesList.get(position).getSender().equals(firebaseUser.getUid()))
        {

        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(messagesList.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
            return 1;
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_profile;
        public TextView message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_profile=itemView.findViewById(R.id.image_profile);
            message=itemView.findViewById(R.id.message);
        }
    }
}
