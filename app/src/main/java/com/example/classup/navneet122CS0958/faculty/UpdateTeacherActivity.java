// UpdateTeacherActivity.java
package com.example.classup.navneet122CS0958.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.classup.navneet122CS0958.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateTeacherActivity extends AppCompatActivity {
    private ImageView updateTeacherImage;
    private EditText updateTeacherName, updateTeacherEmail, updateTeacherPost;
    private Button updateTeacherBtn, deleteTeacherBtn;
    private String name, email, image, post;
    private final int REQ = 1;
    private Bitmap bitmap = null;
    private StorageReference storageReference;
    private DatabaseReference reference;
    private String downloadUrl, uniqueKey, category;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading...");

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        image = getIntent().getStringExtra("image");
        post = getIntent().getStringExtra("post");

        uniqueKey = getIntent().getStringExtra("key");
        category = getIntent().getStringExtra("category");

        updateTeacherImage = findViewById(R.id.updateTeacherImage);
        updateTeacherName = findViewById(R.id.updateTeacherName);
        updateTeacherPost = findViewById(R.id.updateTeacherPost);
        updateTeacherEmail = findViewById(R.id.updateTeacherEmail);
        updateTeacherBtn = findViewById(R.id.updateTeacherBtn);
        deleteTeacherBtn = findViewById(R.id.deleteTeacherBtn);

        reference = FirebaseDatabase.getInstance().getReference().child(category).child(uniqueKey);

        storageReference = FirebaseStorage.getInstance().getReference();

        try {
            Picasso.get().load(image).into(updateTeacherImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateTeacherEmail.setText(email);
        updateTeacherName.setText(name);
        updateTeacherPost.setText(post);
        updateTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        updateTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = updateTeacherName.getText().toString();
                email = updateTeacherEmail.getText().toString();
                post = updateTeacherPost.getText().toString();
                checkValidation();
            }
        });
        deleteTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteData();
            }
        });
    }

    private void deleteData() { Log.d("UpdateTeacherActivity", "Deleting data...");
        reference.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("UpdateTeacherActivity", "Teacher Deleted Successfully");
                            Toast.makeText(UpdateTeacherActivity.this, "Teacher Deleted Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdateTeacherActivity.this, UpdateFaculty.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Log.d("UpdateTeacherActivity", "Failed to delete teacher");
                            Toast.makeText(UpdateTeacherActivity.this, "Failed to delete teacher", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkValidation() {
        if (name.isEmpty()) {
            updateTeacherName.setError("Empty");
            updateTeacherName.requestFocus();
        } else if (post.isEmpty()) {
            updateTeacherPost.setError("Empty");
            updateTeacherPost.requestFocus();
        } else if (email.isEmpty()) {
            updateTeacherEmail.setError("Empty");
            updateTeacherEmail.requestFocus();
        } else if (bitmap == null) {
            updateData("");
        } else {
            uploadImage();
        }
    }

    private void uploadImage() {
        pd.show();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalImage = baos.toByteArray();
        final StorageReference filePath = storageReference.child("Teachers").child(category).child(uniqueKey + ".jpg");

        UploadTask uploadTask = filePath.putBytes(finalImage);

        uploadTask.addOnCompleteListener(UpdateTeacherActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadUrl = String.valueOf(uri);
                            updateData(downloadUrl);
                            pd.dismiss();
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(UpdateTeacherActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateData(String s) {
        HashMap hp = new HashMap<>();
        hp.put("name", name);
        hp.put("email", email);
        hp.put("post", post);
        hp.put("image",s);


        reference.updateChildren(hp)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(UpdateTeacherActivity.this, "Teacher Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateTeacherActivity.this, UpdateFaculty.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateTeacherActivity.this, "Failed to update teacher", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openGallery() {
        Intent picImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picImage, REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                updateTeacherImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
