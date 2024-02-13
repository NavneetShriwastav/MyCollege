package com.example.classup.navneet122CS0958.faculty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTeachers extends AppCompatActivity {
    private ImageView addTeacherImage;
    private DatabaseReference dbRef;
    private Bitmap bitmap = null;
    private ProgressDialog pd;
    private EditText addTeacherName;
    private EditText addTeacherEmail;
    private EditText addTeacherPost;
    private Spinner addTeacherCategory;
    private Button addTeacherBtn;
    private final int REQ = 1;
    private String category;
    private StorageReference storageReference;
    private String name, email, post, downloadUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teachers);
        addTeacherImage = findViewById(R.id.addTeacherImage);
        addTeacherName = findViewById(R.id.addTeacherName);
        addTeacherEmail = findViewById(R.id.addTeacherEmail);
        addTeacherPost = findViewById(R.id.addTeacherPost);
        addTeacherCategory = findViewById(R.id.addTeacherCategory);
        addTeacherBtn = findViewById(R.id.addTeacherBtn);
        storageReference = FirebaseStorage.getInstance().getReference();
        pd = new ProgressDialog(this);

        String[] items = new String[]{"Select Category", "Computer Science Engineering", "Mechanical Engineering", "Electronics and Communication Engineering", "Civil Engineering", "Mining Engineering", "Biotechnology Engineering", "Food Processing Engineering", "Physics Engineering", "Chemistry", "Maths"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        addTeacherCategory.setAdapter(adapter);

        addTeacherCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = addTeacherCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });

        addTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
    }

    private void checkValidation() {
        name = addTeacherName.getText().toString();
        email = addTeacherEmail.getText().toString();
        post = addTeacherPost.getText().toString();
        if (name.isEmpty()) {
            addTeacherName.setError("Empty");
            addTeacherName.requestFocus();
        } else if (email.isEmpty()) {
            addTeacherEmail.setError("Empty");
            addTeacherEmail.requestFocus();
        } else if (post.isEmpty()) {
            addTeacherPost.setError("Empty");
            addTeacherPost.requestFocus();
        } else if (category.equals("Select Category")) {
            Toast.makeText(this, "Please provide teacher category", Toast.LENGTH_SHORT).show();
        } else if (bitmap == null) {
            insertData();
        } else {
            uploadImage();
        }
    }

    private void insertData() {
        dbRef = FirebaseDatabase.getInstance().getReference().child(category);
        final String uniqueKey = dbRef.push().getKey();
        TeachersData teachersData = new TeachersData(name, email, post, downloadUrl, uniqueKey);

        dbRef.child(uniqueKey).setValue(teachersData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(AddTeachers.this, "Teacher Added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddTeachers.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImage() {
        pd.setMessage("Uploading...");
        pd.show(); // Show the ProgressDialog

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalImage = baos.toByteArray();
        final StorageReference filePath = storageReference.child("Teachers").child(category).child(finalImage + ".jpg");

        UploadTask uploadTask = filePath.putBytes(finalImage);

        uploadTask.addOnCompleteListener(AddTeachers.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    insertData();
                                    pd.dismiss(); // Dismiss the ProgressDialog on success
                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss(); // Dismiss the ProgressDialog in case of failure
                    Toast.makeText(AddTeachers.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
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
        if (requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            addTeacherImage.setImageBitmap(bitmap);
        }
    }
}
