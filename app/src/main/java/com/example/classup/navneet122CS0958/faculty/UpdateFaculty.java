package com.example.classup.navneet122CS0958.faculty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classup.navneet122CS0958.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateFaculty extends AppCompatActivity {
    FloatingActionButton fab;
    private RecyclerView cs, me, ece, ce, mi, bt, fpe, pe, ch, ma;
    private LinearLayout csNoData, meNoData, eceNoData, ceNoData, miNoData, btNoData, fpeNoData, peNoData, chNoData, maNoData;
    private List<TeachersData> list1, list2, list3, list4, list5, list6, list7, list8, list9, list10;
    private DatabaseReference reference,dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);

        fab = findViewById(R.id.fab);
        cs = findViewById(R.id.cs);
        me = findViewById(R.id.me);
        ece = findViewById(R.id.ece);
        ce = findViewById(R.id.ce);
        mi = findViewById(R.id.mi);
        bt = findViewById(R.id.bt);
        fpe = findViewById(R.id.fpe);
        pe = findViewById(R.id.pe);
        ch = findViewById(R.id.ch);
        ma = findViewById(R.id.ma);

        csNoData = findViewById(R.id.csNoData);
        meNoData = findViewById(R.id.meNoData);
        eceNoData = findViewById(R.id.eceNoData);
        ceNoData = findViewById(R.id.ceNoData);
        miNoData = findViewById(R.id.miNoData);
        btNoData = findViewById(R.id.btNoData);
        fpeNoData = findViewById(R.id.fpeNoData);
        peNoData = findViewById(R.id.peNoData);
        chNoData = findViewById(R.id.chNoData);
        maNoData = findViewById(R.id.maNoData);
 reference = FirebaseDatabase.getInstance().getReference();


        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        list4 = new ArrayList<>();
        list5 = new ArrayList<>();
        list6 = new ArrayList<>();
        list7 = new ArrayList<>();
        list8 = new ArrayList<>();
        list9 = new ArrayList<>();
        list10 = new ArrayList<>();

        cs();
        me();
        ece();
        ce();
        mi();
        bt();
        fpe();
        pe();
        ch();
        ma();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateFaculty.this, AddTeachers.class));
            }
        });
    }

    private void cs() {
        dbRef = reference.child("Computer Science Engineering");
        setRecyclerView(cs, list1, csNoData, "Computer Science Engineering");
    }

    private void me() {
        dbRef = reference.child("Mechanical Engineering");
        setRecyclerView(me, list2, meNoData, "Mechanical Engineering");
    }

    private void ece() {
        dbRef = reference.child("Electronics and Communication Engineering");
        setRecyclerView(ece, list3, eceNoData, "Electronics and Communication Engineering");
    }

    private void ce() {
        dbRef = reference.child("Civil Engineering");
        setRecyclerView(ce, list4, ceNoData, "Civil Engineering");
    }

    private void mi() {
        dbRef = reference.child("Mining Engineering");
        setRecyclerView(mi, list5, miNoData, "Mining Engineering");
    }

    private void bt() {
        dbRef = reference.child("Biotechnology Engineering");
        setRecyclerView(bt, list6, btNoData, "Biotechnology Engineering");
    }

    private void fpe() {
        dbRef = reference.child("Food Processing Engineering");
        setRecyclerView(fpe, list7, fpeNoData, "Food Processing Engineering");
    }

    private void pe() {
        dbRef = reference.child("Physics Engineering");
        setRecyclerView(pe, list8, peNoData, "Physics Engineering");
    }

    private void ch() {
        dbRef = reference.child("Chemistry");
        setRecyclerView(ch, list9, chNoData, "Chemistry");
    }

    private void ma() {
        dbRef = reference.child("Maths");
        setRecyclerView(ma, list10, maNoData, "Maths");
    }

    private void setRecyclerView(RecyclerView recyclerView, List<TeachersData> dataList, LinearLayout noDataLayout, String category) {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                if (!snapshot.exists()) {
                    noDataLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        TeachersData data = snapshot1.getValue(TeachersData.class);
                        dataList.add(data);
                    }
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    TeacherAdapter adapter = new TeacherAdapter(dataList, UpdateFaculty.this, category);
                    recyclerView.setAdapter(adapter);
                    noDataLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
