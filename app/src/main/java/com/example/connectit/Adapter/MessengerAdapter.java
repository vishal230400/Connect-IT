package com.example.connectit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.connectit.MessagesDisplay;
import com.example.connectit.Model.User;
import com.example.connectit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessengerAdapter extends RecyclerView.Adapter<MessengerAdapter.ViewHolder> {

    public List<User> mUsersList;
    public Context mContext;
    public FirebaseUser firebaseUser;

    public MessengerAdapter(List<User> mUsersList, Context mContext) {
        this.mUsersList = mUsersList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.message_item,parent,false);
        return new MessengerAdapter.ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final User user=mUsersList.get(position);
        holder.username.setText(user.getUsername());
        Long time=System.currentTimeMillis();
        Glide.with(mContext).load(user.getImageurl()).into(holder.image_profile);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //Toast.makeText(mContext,"going",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(mContext, MessagesDisplay.class);
                intent.putExtra("recieverid",user.getId());
                intent.putExtra("time",System.currentTimeMillis());
                intent.putExtra("recieverimage",user.getImageurl());
                intent.putExtra("recievername",user.getUsername());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profile;
        public TextView username;
        public RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_profile=itemView.findViewById(R.id.image_profile);
            username=itemView.findViewById(R.id.username);
            relativeLayout=itemView.findViewById(R.id.relativelayout);
        }
    }
}
