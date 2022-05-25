package com.example.myditail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myditail.modelclass.FireModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class UpatePage extends AppCompatActivity {

    private TextView selectimage;
    private ImageView image1, back, changesave;
    private EditText password, name, surname, age, contact, email;
    RadioGroup ueradiogroup;
    Uri uri;
    String link1;
    String key1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upate_page);

        key1 = getIntent().getStringExtra("key");
        Toast.makeText(UpatePage.this, "key : " + key1, Toast.LENGTH_SHORT).show();

        back = findViewById(R.id.back);
        changesave = findViewById(R.id.changeandsave);
        name = findViewById(R.id.ename);
        surname = findViewById(R.id.esurname);
        ueradiogroup = findViewById(R.id.eradiogroup);
        age = findViewById(R.id.eage);
        contact = findViewById(R.id.econtect);
        email = findViewById(R.id.eemail);
        password = findViewById(R.id.epassword);
        image1 = findViewById(R.id.eimage1);
        selectimage = findViewById(R.id.eselectimage);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbr = firebaseDatabase.getReference();

        dbr.child("MyDetail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equals(key1)) {
                        String name1 = dataSnapshot.child("name").getValue().toString();
                        String surname1 = dataSnapshot.child("surname").getValue().toString();
                        String gender1 = dataSnapshot.child("gender").getValue().toString();
                        String age1 = dataSnapshot.child("age").getValue().toString();
                        String contact1 = dataSnapshot.child("contact").getValue().toString();
                        String email1 = dataSnapshot.child("email").getValue().toString();
                        String password1 = dataSnapshot.child("password").getValue().toString();
                        link1 = dataSnapshot.child("link").getValue().toString();

                        Log.e("TAG", "onDataChange: ====== " + name1 + "  " + surname1 + "  " + gender1 + "  " + age1 + "  " + contact1 + "  " + email1 + "  " + password1 + "  " + link1);

                        name.setText(name1);
                        surname.setText(surname1);
                        if (gender1.equals("male") || gender1.equals("Male")) {
                            ueradiogroup.check(R.id.emale);
                            //Toast.makeText(UpatePage.this,"fmemale",Toast.LENGTH_SHORT).show();

                        } else {
                            //Toast.makeText(UpatePage.this,"male",Toast.LENGTH_SHORT).show();
                            ueradiogroup.check(R.id.efemale);
                        }
                        age.setText(age1);
                        contact.setText(contact1);
                        email.setText(email1);
                        password.setText(password1);
                        Glide.with(UpatePage.this).load(link1).placeholder(R.drawable.default_img).into(image1);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2001);
            }
        });

        changesave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUpload(uri);
                changesave.setBackgroundColor(Color.parseColor("#B1B1B1"));
            }
        });


    }


    void ImageUpload(Uri uri) {

        try {
            File file = new File(uri.toString());
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference(file.getName());

            UploadTask uploadTask = storageReference.putFile(uri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String name1 = name.getText().toString();
                            String surname1 = surname.getText().toString();
                            String gender;
                           /* if (ueradiogroup.getCheckedRadioButtonId() ==  R.id.male) {
                                gender = "male";
                            } else {
                                gender = "female";
                            }*/
                            gender = ((RadioButton)findViewById(ueradiogroup.getCheckedRadioButtonId())).getText().toString();
                            String age1 = age.getText().toString();
                            String contact1 = contact.getText().toString();
                            String email1 = email.getText().toString();
                            String password1 = password.getText().toString();
                            String ulink = uri.toString();

                            Log.e("TAG", "NEW VALUE  ++++++++++++++++++++++++++++: ====== " + name1 + "  " + surname1 + "  " + gender + "  " + age1 + "  " + contact1 + "  " + email1 + "  " + password1 + "  " + ulink);
                            if (name1.isEmpty() || surname1.isEmpty() || gender.isEmpty() || age1.isEmpty() || contact1.isEmpty() || email1.isEmpty() || password1.isEmpty() || ulink.isEmpty()) {
                                Toast.makeText(UpatePage.this, "Please enter all detail", Toast.LENGTH_SHORT).show();
                            } else {
                                updateData(key1, name1, surname1, gender, age1, contact1, email1, password1, ulink);
                            }
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });

        } catch (Exception e) {

            String name1 = name.getText().toString();
            String surname1 = surname.getText().toString();
            String gender1;
            /*if (ueradiogroup.getCheckedRadioButtonId() == R.id.emale) {
                gender1 = "male";
            } else {
                gender1 = "female";
            }*/
            gender1 = ((RadioButton)findViewById(ueradiogroup.getCheckedRadioButtonId()))
                    .getText().toString();
            String age1 = age.getText().toString();
            String contact1 = contact.getText().toString();
            String email1 = email.getText().toString();
            String password1 = password.getText().toString();

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference dbr1 = firebaseDatabase.getReference();

            FireModel fireModel = new FireModel(name1, surname1, gender1, age1, contact1, email1, password1, link1);
            dbr1.child("MyDetail").child(key1).setValue(fireModel);

            /*dbr1.child("Mydetail").child(key11).child("name").setValue(name1);
            dbr1.child("Mydetail").child(key11).child("surname").setValue(surname1);
            dbr1.child("Mydetail").child(key11).child("gender").setValue(gender);
            dbr1.child("Mydetail").child(key11).child("age").setValue(age1);
            dbr1.child("Mydetail").child(key11).child("contact").setValue(contact1);
            dbr1.child("Mydetail").child(key11).child("email").setValue(email1);
            dbr1.child("Mydetail").child(key11).child("password").setValue(password1);*/

            Toast.makeText(UpatePage.this, "Data Updated !imagae", Toast.LENGTH_SHORT).show();
            changesave.setBackgroundColor(Color.parseColor("#007381"));
        }

    }

    void updateData(String key11, String name1, String surname1, String gender, String age1, String contact1, String email1, String password1, String ulink) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbr1 = firebaseDatabase.getReference();

        FireModel fireModel = new FireModel(name1, surname1, gender, age1, contact1, email1, password1, ulink);
        dbr1.child("MyDetail").child(key11).setValue(fireModel);


        Log.e("TAG", "updateData+++++: " + name1 + "  " + surname1 + "  " + gender + "  " + age1 + "  " + contact1 + "  " + email1 + "  " + password1 + " " + ulink);
        Toast.makeText(UpatePage.this, "Data Updated", Toast.LENGTH_SHORT).show();
        changesave.setBackgroundColor(Color.parseColor("#007381"));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 2001) {
                uri = data.getData();
                image1.setImageURI(uri);
            }
        } catch (Exception e) {
            Log.e("TAG", "onActivityResult: " + e.getMessage());
        }


    }
}