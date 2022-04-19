package com.noor.mystore99;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class AddressPage extends AppCompatActivity {


    TextView textView1,textView2;
    EditText et1,et3,et4;
    Button submit,back;
    String str,str1,str3,str4,str5;
    DatabaseReference ref;
    SharedPreferences preferences;
    String key,val,ad;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_page);

       // radioButton2=(RadioButton)findViewById(R.id.radioButton2);
        textView1=(TextView)findViewById(R.id.saveAdd);
        textView2=(TextView)findViewById(R.id.TvAddress);
        et1=(EditText) findViewById(R.id.EdAdd1);
        et3=(EditText) findViewById(R.id.EdAdd3);
        et4=(EditText) findViewById(R.id.EdPin);

        //et4=(EditText) findViewById(R.id.EdAdd4);
        str1=et1.getText().toString();
        str3=et3.getText().toString();
        submit=(Button)findViewById(R.id.lastBtn);
        ad=getIntent().getStringExtra("ad");
        back=findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
            key = (FirebaseAuth.getInstance().getCurrentUser()).getUid();

        //ref= FirebaseDatabase.getInstance().getReference("User").child(key).child("add");
        ref = FirebaseDatabase.getInstance().getReference("Myorder").child(key);


        str=str1+" "+str3+" "+str4+" "+str5;


        if(ad==null || ad=="") {

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((ad == null || ad.equals("")) && et1.getText().toString().isEmpty()  && et3.getText().toString().isEmpty() && et4.getText().toString().isEmpty()) {
                        Toast.makeText(AddressPage.this, "Please Enter Details!!", Toast.LENGTH_SHORT).show();
                    } else if (et1.getText().toString().isEmpty() || et3.getText().toString().isEmpty() || et4.getText().toString().isEmpty()) {
                        Toast.makeText(AddressPage.this, "Please Enter Details!!", Toast.LENGTH_SHORT).show();
                    } else if (!et1.getText().toString().isEmpty()  && !et3.getText().toString().isEmpty() && !et4.getText().toString().isEmpty() && ad == null) {


                        ref.child("add").setValue(et1.getText().toString().trim() +  ", " + et3.getText().toString().trim() + ",pincode:" + et4.getText().toString().trim());
                        ref.child("pincode").setValue(et4.getText().toString().trim());
                        Toast.makeText(AddressPage.this, "Address Successfully added", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AddressPage.this, Address1.class);
                        startActivity(i);
                        finish();
                    } else if (!et1.getText().toString().isEmpty() &&  !et3.getText().toString().isEmpty() && !et4.getText().toString().isEmpty() && ad != null) {
                        Toast.makeText(AddressPage.this, "Please choose either map or enter your address", Toast.LENGTH_SHORT).show();
                    }
                }
            });



        }


    }
}
