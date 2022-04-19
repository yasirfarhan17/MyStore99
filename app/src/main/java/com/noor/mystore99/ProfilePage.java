package com.noor.mystore99;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilePage extends AppCompatActivity {

    ImageView profileImage;
    TextView name,phone,add;
    DatabaseReference ref;
    Button back,edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        profileImage=findViewById(R.id.profile_image);
        name=findViewById(R.id.profileName);
        phone=findViewById(R.id.profilePhone);
       add=findViewById(R.id.profileAdd);
       back=findViewById(R.id.back);
       edit=findViewById(R.id.edit);

       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               onBackPressed();
           }
       });

       edit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(ProfilePage.this,ProfileEdit.class);
               startActivity(intent);
           }
       });

        ref= FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("photo");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String val=dataSnapshot.getValue().toString();

                    byte[] decodedString = Base64.decode(val, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    profileImage.setImageBitmap(decodedByte);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ref = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(CartProductList.this,"hii",Toast.LENGTH_SHORT).show();
                if (dataSnapshot.exists()) {
                    String name1 = dataSnapshot.child("name").getValue().toString();
                    String phone1=dataSnapshot.child("phone").getValue().toString();

                    name.setText(String.valueOf(name1));
                    phone.setText(String.valueOf(phone1));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getActivity(),"Oppps....someThing is wrong",Toast.LENGTH_SHORT).show();

            }
        });

        ref = FirebaseDatabase.getInstance().getReference("Myorder").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("add");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String address=dataSnapshot.getValue().toString();
                    add.setText(String.valueOf(address));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
