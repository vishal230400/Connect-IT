package com.example.connectit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.TextUtilsCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

  EditText username,fullname,email,password;
  Button register;
  TextView txt_login;
  FirebaseAuth auth;
  DatabaseReference reference;
  ProgressDialog pd;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);
    username=findViewById(R.id.username);
    fullname=findViewById(R.id.fullname);
    email=findViewById(R.id.email);
    password=findViewById(R.id.password);
    register=findViewById(R.id.register);
    txt_login=findViewById(R.id.txt_login);
    auth=FirebaseAuth.getInstance();
    txt_login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
      }
    });

    register.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        pd=new ProgressDialog(RegisterActivity.this);
        pd.setMessage("Please Wait....");
        pd.show();

        String string_username=username.getText().toString();
        String string_fullname=fullname.getText().toString();
        String string_email=email.getText().toString();
        String string_password=password.getText().toString();

        if(TextUtils.isEmpty(string_username) || TextUtils.isEmpty(string_fullname) || TextUtils.isEmpty(string_email) ||TextUtils.isEmpty(string_password) )
          Toast.makeText(RegisterActivity.this, "All fields are Required", Toast.LENGTH_SHORT).show();
        else if(string_password.length()<6)
          Toast.makeText(RegisterActivity.this,"Password must have atleast 6 characters",Toast.LENGTH_SHORT).show();
        else {
          register(string_username,string_fullname,string_email,string_password);
        }
      }
    });
  }

  private void register(final String username, final String fullname, String email, String password){
    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful())
        {
          FirebaseUser firebaseUser=auth.getCurrentUser();
          String userid=firebaseUser.getUid();
          reference= FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

          HashMap<String,Object> hashMap=new HashMap<>();
          hashMap.put("id",userid);
          hashMap.put("username",username);
          hashMap.put("fullname",fullname);
          hashMap.put("bio","");
          hashMap.put("imageurl","https://firebasestorage.googleapis.com/v0/b/connectit-189ed.appspot.com/o/placeholder.png?alt=media&token=364a3316-a7ef-4da9-b4c2-213d66ebd46a");

          reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful()) {
                pd.dismiss();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
              }
            }
          });
        }else{
          pd.dismiss();
          Toast.makeText(RegisterActivity.this,"You cant register with this email",Toast.LENGTH_SHORT).show();
        }
      }
    });
  }
}