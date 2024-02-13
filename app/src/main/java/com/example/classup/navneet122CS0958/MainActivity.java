package com.example.classup.navneet122CS0958;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.classup.navneet122CS0958.faculty.UpdateFaculty;
import com.example.classup.navneet122CS0958.notice.DeleteNoticeActivity;
import com.example.classup.navneet122CS0958.notice.UploadNotice;

public class MainActivity extends AppCompatActivity {
    private CardView addNotice;
    private CardView addGalleryImg;
    private CardView addEbook;
    private CardView faculty;
    private CardView deleteNotice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addNotice = findViewById(R.id.addNotice);
        addGalleryImg = findViewById(R.id.addGalleryImage);
        addEbook = findViewById(R.id.addEbook);
        faculty = findViewById(R.id.faculty);
        deleteNotice = findViewById(R.id.deleteNotice);

        addNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent1 = new Intent(MainActivity.this, UploadNotice.class);
                startActivity(intent1);
            }
        });
        addGalleryImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this,UploadImage.class);
                startActivity(intent2);
            }
        });
        addEbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MainActivity.this,UploadPdf.class);
                startActivity(intent3);
            }
        });
        faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(MainActivity.this, UpdateFaculty.class);
                startActivity(intent4);
            }
        });
        deleteNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(MainActivity.this, DeleteNoticeActivity.class);
                startActivity(intent5);
            }
        });

    }
}