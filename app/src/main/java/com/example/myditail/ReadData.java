package com.example.myditail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.myditail.modelclass.FireModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

import adapter.DetailAdapter;

public class ReadData extends AppCompatActivity {


    private RecyclerView rv_view;
    private ImageView back;
    List<FireModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_data);

        rv_view=findViewById(R.id.rv_view);
        back=findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        CallReadData();

    }


    void  CallReadData(){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbr = firebaseDatabase.getReference();

        dbr.child("MyDetail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    String key = dataSnapshot.getKey();
                    String name =dataSnapshot.child("name").getValue().toString();
                    String surname =dataSnapshot.child("surname").getValue().toString();
                    String gender =dataSnapshot.child("gender").getValue().toString();
                    String age =dataSnapshot.child("age").getValue().toString();
                    String contact =dataSnapshot.child("contact").getValue().toString();
                    String email =dataSnapshot.child("email").getValue().toString();
                    String password =dataSnapshot.child("password").getValue().toString();
                    String link = dataSnapshot.child("link").getValue().toString();

                    Log.e("TAG", "onDataChange: ========================================================"+name + " "+surname +" "+gender +" "+age+ " "+contact+ " "+email+" "+password+" "+link);

                    FireModel fireModel = new FireModel(name,surname,gender,age,contact,email,password,link,key);
                    list.add(fireModel);
                }
                MyAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }
    void  MyAdapter(){
        DetailAdapter detailAdapter = new DetailAdapter(ReadData.this,list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_view.setAdapter(detailAdapter);
        rv_view.setLayoutManager(layoutManager);

    }
}