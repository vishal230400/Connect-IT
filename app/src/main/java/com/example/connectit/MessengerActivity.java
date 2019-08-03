package com.example.connectit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.connectit.Adapter.MessengerAdapter;
import com.example.connectit.Adapter.UserAdapter;
import com.example.connectit.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessengerActivity extends AppCompatActivity {

  public RecyclerView recyclerView;
  public EditText search_bar;
  public List<User> mUsers;
  public MessengerAdapter messengerAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_messenger);
    search_bar=findViewById(R.id.search_bar);
    mUsers=new ArrayList<>();
    recyclerView=findViewById(R.id.recycler_view);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    messengerAdapter=new MessengerAdapter(mUsers,getApplicationContext());
    recyclerView.setAdapter(messengerAdapter);

    readUsers();


    search_bar.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        searchUser(s.toString().toLowerCase());
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
  }

  public void searchUser(String s) {
    Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").startAt(s).endAt(s + "\uf8ff");
    query.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        recyclerView.removeAllViews();
        mUsers.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
          User user = snapshot.getValue(User.class);
          mUsers.add(user);
        }
        messengerAdapter = new MessengerAdapter(mUsers, getApplicationContext());
        recyclerView.setAdapter(messengerAdapter);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });

  }

  private void readUsers( ) {
    mUsers.clear();
    recyclerView.removeAllViews();
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
    reference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(search_bar.getText().toString().equals(""))
        {
          mUsers.clear();
          for (DataSnapshot snapshot:dataSnapshot.getChildren())
          {
            User user=snapshot.getValue(User.class);
            if(!user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
              mUsers.add(user);
          }
          messengerAdapter=new MessengerAdapter(mUsers,getApplicationContext());
          recyclerView.setAdapter(messengerAdapter);
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }
}