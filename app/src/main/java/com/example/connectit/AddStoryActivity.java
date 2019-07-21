package com.example.connectit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class AddStoryActivity extends AppCompatActivity {

    public Uri myImageUri;
    String url="";
    public StorageReference storageReference;
    public StorageTask storageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);
        storageReference= FirebaseStorage.getInstance().getReference("story");
        CropImage.activity().setAspectRatio(9,16).start(AddStoryActivity.this);

    }

    public String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void publishStory()
    {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Posting");
        pd.show();
        if(myImageUri!=null)
        {
            final StorageReference imageReference=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(myImageUri));
            storageTask=imageReference.putFile(myImageUri);
            storageTask.continueWithTask(new Continuation() {
                @Override
                public Task<Uri> then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return imageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloaduri=task.getResult();
                        url=downloaduri.toString();
                        String myid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Story").child(myid);
                        String storyid=reference.push().getKey();
                        long timeend=System.currentTimeMillis()+86400000;
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("imageurl",url);
                        hashMap.put("timestart", ServerValue.TIMESTAMP);
                        hashMap.put("timeend",timeend);
                        hashMap.put("storyid",storyid);
                        hashMap.put("userid",FirebaseAuth.getInstance().getCurrentUser().getUid());

                        reference.child(storyid).setValue(hashMap);
                        pd.dismiss();
                        finish();
                    }
                    else {
                        Toast.makeText(AddStoryActivity.this,"Failed!",Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddStoryActivity.this,"No Image Selected",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            myImageUri=result.getUri();
            publishStory();
        }
        else {
            Toast.makeText(AddStoryActivity.this,"Something gone wrong",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddStoryActivity.this,MainActivity.class));
            finish();
        }
    }
}
