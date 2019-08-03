package com.example.connectit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.connectit.Adapter.MessageDisplayAdapter;
import com.example.connectit.Model.Messenger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessagesDisplay extends AppCompatActivity {

    public String reciever,recieverimage,recievername;
    public Long time;
    public EditText message;
    public List<String> id,sent,recieved;
    public TextView send;
    public List<Messenger> msent,mrecieved,mMessages;
    public RecyclerView recyclerView;
    public MessageDisplayAdapter messageDisplayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_display);
        message=findViewById(R.id.message);
        msent=new ArrayList<>();
        id=new ArrayList<>();
        recieved=new ArrayList<>();
        sent=new ArrayList<>();
        mrecieved=new ArrayList<>();
        mMessages=new ArrayList<>();
        recyclerView=findViewById(R.id.recycler_view);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.scrollToPosition(linearLayoutManager.findLastVisibleItemPosition());
        recyclerView.removeAllViews();
        messageDisplayAdapter=new MessageDisplayAdapter(mMessages,getApplicationContext(),id);
        recyclerView.setAdapter(messageDisplayAdapter);
        Intent intent=getIntent();
        reciever=intent.getStringExtra("recieverid");
        recieverimage=intent.getStringExtra("recieverimage");
        recievername=intent.getStringExtra("recievername");
        time=intent.getLongExtra("time",0);
        send=findViewById(R.id.send);
        ImageView imageView=findViewById(R.id.image_profile);
        Glide.with(getApplicationContext()).load(recieverimage).into(imageView);
        TextView username=findViewById(R.id.username);
        username.setText(recievername);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(message.getText().toString().equals("")) {
                    Toast.makeText(MessagesDisplay.this, "Cant send Empty message", Toast.LENGTH_SHORT).show();
                }else
                {
                    sendmessage();
                }
                getmessages();
            }
        });
        getmessages();
        }
    public void sendmessage()
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Sent");
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("message",message.getText().toString());
        hashMap.put("reciever",reciever);
        hashMap.put("sender",FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("time",time);
        reference.push().setValue(hashMap);
        DatabaseReference reference1= FirebaseDatabase.getInstance().getReference("Messages").child(reciever).child("Recieved");
        HashMap<String,Object> hashMap1=new HashMap<>();
        hashMap1.put("message",message.getText().toString());
        hashMap1.put("sender", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap1.put("time",time);
        hashMap1.put("reciever",reciever);
        reference1.push().setValue(hashMap1);
        message.setText("");
    }

    public void getmessages()
    {
        mrecieved.clear();
        mMessages.clear();
        msent.clear();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Sent");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                msent.clear();
                sent.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Messenger messenger=snapshot.getValue(Messenger.class);
                    if(messenger.getReciever().equals(reciever)) {
                        msent.add(messenger);
                        mMessages.clear();
                        sent.add(messenger.getSender());
                        id.clear();
                        sort();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference reference1= FirebaseDatabase.getInstance().getReference("Messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Recieved");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    mrecieved.clear();
                    recieved.clear();
                    Messenger messenger=snapshot.getValue(Messenger.class);
                    if(messenger.getSender().equals(reciever)) {
                        mrecieved.add(messenger);
                        recieved.add(messenger.getSender());
                        id.clear();
                        mMessages.clear();
                        sort();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        messageDisplayAdapter.notifyDataSetChanged();
        }

    public void sort()
    {
        int i,i1,i2;
        int size=msent.size()+mrecieved.size();
        int size1=msent.size();
        int size2=mrecieved.size();
        i1 = 0;
        i2 = 0;
        for(i=0; i < size; i++)
        {
            if(i1 >= size1 || i2 >= size2)
            {
                break;
            }
            if(msent.get(i1).getTime() < mrecieved.get(i2).getTime())
            {
                mMessages.add(msent.get(i1));
                id.add(sent.get(i1));
                i1++;
            }
            else
            {
                mMessages.add(mrecieved.get(i2));
                id.add(recieved.get(i2));
                i2++;
            }
        }

        while(i1 < size1)
        {
            mMessages.add(msent.get(i1));
            id.add(sent.get(i1));
            i++;
            i1++;
        }
        while(i2 < size2)
        {
            mMessages.add(mrecieved.get(i2));
            id.add(recieved.get(i2));
            i++;
            i2++;
        }
    }
}
