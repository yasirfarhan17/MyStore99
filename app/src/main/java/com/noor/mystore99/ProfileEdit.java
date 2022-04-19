package com.noor.mystore99;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfileEdit extends AppCompatActivity {
    CircleImageView image;
    Button edit,update,back;
    TextView name, phone, address, landmark;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    DatabaseReference ref;
    String val1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilr_edit);
        edit = findViewById(R.id.editPhoto);
        image = findViewById(R.id.profile_image);
        name=findViewById(R.id.EditName);
        phone=findViewById(R.id.EditPhone);
        address=findViewById(R.id.EditAddress);
        landmark=findViewById(R.id.EditLandMark);
        update=findViewById(R.id.update);
        phone.setEnabled(false);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final Bitmap[] bitmap = new Bitmap[1];


        ref= FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("photo");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String val=dataSnapshot.getValue().toString();

                    byte[] decodedString = Base64.decode(val, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    image.setImageBitmap(decodedByte);

                    val1=val;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ref= FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("phone");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    phone.setText(dataSnapshot.getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref= FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    name.setText(dataSnapshot.getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref= FirebaseDatabase.getInstance().getReference("Myorder").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("add");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    address.setText(dataSnapshot.getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().isEmpty()||phone.getText().toString().isEmpty()||address.getText().toString().isEmpty()){
                    Toast.makeText(ProfileEdit.this,"Please Enter Correct detail!!",Toast.LENGTH_SHORT).show();
                }
                else {
                    ref= FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ref.child("name").setValue(name.getText().toString());

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap bitmap=null ;

                    if(mImageUri!=null) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(ProfileEdit.this.getContentResolver(), mImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {

                            bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();

                    }


                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteFormat = stream.toByteArray();
                    String encodedImage = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

                    ref= FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ref.child("photo").setValue(encodedImage);

                    ref= FirebaseDatabase.getInstance().getReference("Myorder").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("add");
                    ref.setValue(address.getText().toString()+" "+landmark.getText().toString());

                    Toast.makeText(ProfileEdit.this,"Detail SuccessFully Update ",Toast.LENGTH_SHORT).show();
                }
            }
        });





    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(image);
        }
    }
}


/*

byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
myimageview.setImageBitmap(decodedByte);

BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(myStream, false);
Bitmap region = decoder.decodeRegion(new Rect(10, 10, 50, 50), null);



 */

