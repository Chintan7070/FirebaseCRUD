package com.example.myditail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myditail.modelclass.FireModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.security.Permission;

public class MainActivity extends AppCompatActivity {

    private ImageView image1;
    private TextView selectimage1;
    private EditText password1, email1, contact1, age1, name1, surname1;
    private RadioGroup radiogroup1;
    private RadioButton male1, female1;
    private Button read1, insert1;
    Uri uri;
    String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image1 = findViewById(R.id.image11);
        name1 = findViewById(R.id.name);
        surname1 = findViewById(R.id.surname);
        radiogroup1 = findViewById(R.id.radiogroup);
        male1 = findViewById(R.id.male);
        female1 = findViewById(R.id.female);
        age1 = findViewById(R.id.age);
        contact1 = findViewById(R.id.contect);
        email1 = findViewById(R.id.email);
        password1 = findViewById(R.id.password);
        selectimage1 = findViewById(R.id.selectimage);
        insert1 = findViewById(R.id.insert);
        read1 = findViewById(R.id.read);


        getpermission(permission);

        insert1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UploadImage(uri);

            }
        });

        selectimage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);
            }
        });

        read1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ReadData.class);
                startActivity(intent);
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            try {
                uri = data.getData();
                image1.setImageURI(uri);
            } catch (Exception e) {
                Log.e("TAG", "Cancel===" + e.getMessage());
            }
        }
    }

    void InsertData(String name, String surname, String gender, String age, String contact, String email, String password, String s) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbr = firebaseDatabase.getReference();

        FireModel fireModel = new FireModel(name, surname, gender, age, contact, email, password,s);
        dbr.child("MyDetail").push().setValue(fireModel);

    }


    void UploadImage(Uri uri) {

        try {
            File file = new File(uri.toString());

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference(file.getName());

            UploadTask uploadTask = storageRef.putFile(uri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri1) {
                            try {
                                String link = uri1.toString();
                                String gender;
                                Log.e("TAG", "onSuccess:+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+link);

                                String name = name1.getText().toString();
                                String surname = surname1.getText().toString();
                                if (radiogroup1.getCheckedRadioButtonId() == R.id.male) {
                                    gender = "male";
                                } else {
                                    gender = "female";
                                }
                                String age = age1.getText().toString();
                                String contact = contact1.getText().toString();
                                String email = email1.getText().toString();
                                String password = password1.getText().toString();
                                String  link1 = link.toString();
                                String gender1 = gender;

                                //UploadImage(uri);

                                if (link1.toString().isEmpty() || name.isEmpty() || surname.isEmpty() || gender1.isEmpty() || age.isEmpty() || contact.isEmpty() || email.isEmpty() || password.isEmpty()) {
                                    Toast.makeText(MainActivity.this, "Please enter all detail", Toast.LENGTH_SHORT).show();
                                } else {
                                    InsertData(name, surname, gender1, age, contact, email, password, link1);
                                    Toast.makeText(MainActivity.this, "Detail Saved Successfully", Toast.LENGTH_SHORT).show();
                                    //Log.e("TAG", "onSuccess:===================== "  +"================"+ link1);
                                }

                            } catch (Exception e) {
                                Log.e("TAG", "onSuccess: " + e.getMessage());
                            }

                        }

                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Image Faile", Toast.LENGTH_SHORT).show();
                }
            });
             } catch (Exception e) {
            Log.e("TAG", "UploadImage: ==========================" + e.getMessage());
        }
    }


    void getpermission(String[] permission){
            if (ContextCompat.checkSelfPermission(MainActivity.this, permission[0]) == PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(MainActivity.this, permission[1]) == PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(MainActivity.this, permission[2])== PackageManager.PERMISSION_DENIED)
            {
                ActivityCompat.requestPermissions(MainActivity.this,permission,100);
            }
            else
            {
                Toast.makeText(MainActivity.this,"Permission Granted",Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED &&grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainActivity.this,"Allow",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this,"Deny",Toast.LENGTH_SHORT).show();
            }
        }
    }



}