package com.noor.mystore99;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private AppBarConfiguration mAppBarConfiguration;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CircleImageView image;
    View header ;
    private FrameLayout frameLayout;
    TextView fullName,email;
    DatabaseReference ref;
    String str,key;static  int bVal;
    //SessionManager session;
   // EditText search;
    productAdapter adapter;
    ArrayList<product> p=new ArrayList<>();
    SharedPreferences preferences ;
    String key1;
    FirebaseAuth auth;
    static TextView badge;
    Handler handler=new Handler();
    static MenuItem menuItem;
    // badge text view
    static TextView badgeCounter;
    // change the number to see badge in action
   static int pendingNotifications = 0;
   ImageView imageView;
   FrameLayout frameLayout1;
   View view1;


    private Runnable update=new Runnable() {
        @Override
        public void run() {


           /* if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                ref = FirebaseDatabase.getInstance().getReference("Cart").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        bVal=0;
                        if (dataSnapshot.exists()) {
                            bVal = (int) dataSnapshot.getChildrenCount();

                        }
                        if (bVal != 0) {
                            badge.setVisibility(View.VISIBLE);
                            badge.setText(String.valueOf(bVal));
                        }
                        else{
                            badge.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                // Log.d("badge =",""+bVal);




            }*/
            invalidateOptionsMenu();






            handler.postDelayed(this,100);


            //handler.postDelayed(check, 1000);


        }
    };


    private Runnable check=new Runnable() {
        @Override
        public void run() {


            if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                ref = FirebaseDatabase.getInstance().getReference("Cart").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        bVal=0;
                        if (dataSnapshot.exists()) {
                            bVal = (int) dataSnapshot.getChildrenCount();

                        }
                        if (bVal != 0) {
                            badge.setVisibility(View.VISIBLE);
                            badge.setText(String.valueOf(bVal));
                        }
                        else{
                            badge.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                // Log.d("badge =",""+bVal);




            }
            invalidateOptionsMenu();





            handler.postDelayed(this,100);


            //handler.postDelayed(check, 1000);


        }
    };



    @Override
    public void onBackPressed() {
        DrawerLayout layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START);
        }
        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    MainActivity.this);

            // set title
            alertDialogBuilder.setTitle("Exit");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Do you really want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                            Intent a = new Intent(Intent.ACTION_MAIN);
                            a.addCategory(Intent.CATEGORY_HOME);
                            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(a);
                            //show_Notification("LogOut","You Log Out Your account at "+ currentTime);
                            //EmployeeHome.this.finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        (getSupportActionBar()).setDisplayShowTitleEnabled(false);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
       // sessionManager = new SessionManager(getApplicationContext());
        auth=FirebaseAuth.getInstance();
        str=getIntent().getStringExtra("phone");
       // key= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        //badge=findViewById(R.id.badge);


        //session = new SessionManager(getApplicationContext());
        //search=findViewById(R.id.serach);











        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        header=navigationView.getHeaderView(0);
        fullName=(TextView)header.findViewById(R.id.main_fullname);
        image=header.findViewById(R.id.profile_image);
        email=(TextView)header.findViewById(R.id.main_email);
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String nextDate = format.format(dt);

        Log.d("getTime2",""+nextDate);


        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);

        toggle.syncState();
        navigationView.getMenu().getItem(0).setChecked(true);
        frameLayout=findViewById(R.id.main_framelayout);
        setFragment(new HomeFragment());


        if(checkInternet() && FirebaseAuth.getInstance().getCurrentUser()!=null){
            int versionCode = BuildConfig.VERSION_CODE;
            final int[] codee = new int[1];

            ref=FirebaseDatabase.getInstance().getReference("version").child("version");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("hooo","working");
                    if(snapshot.exists()){
                        codee[0] =Integer.parseInt(snapshot.getValue().toString());
                    }

                    if(versionCode<codee[0]){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                MainActivity.this);

                        // set title
                        alertDialogBuilder.setTitle("SabziTaza Update");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Please update SabziTaza to the latest version.")
                                .setCancelable(false)
                                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.noor.mystore99"));
                                        startActivity(intent);
                                        //show_Notification("LogOut","You Log Out Your account at "+ currentTime);
                                        //EmployeeHome.this.finish();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        dialog.cancel();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



            view1= findViewById(R.id.cartTest);
            badge = (TextView) view1.findViewById(R.id.badge_counter);
            imageView=view1.findViewById(R.id.cartId);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MainActivity.this,CartProductList.class);
                    startActivity(intent);
                }
            });

           // final android.app.AlertDialog waitingtDialog= new SpotsDialog.Builder().setContext(MainActivity.this).build();
            //waitingtDialog.show();
            //update.run();
            check.run();
            //setupBadge(bVal);



            ref= FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("photo");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String val= (dataSnapshot.getValue()).toString();

                        byte[] decodedString = Base64.decode(val, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        image.setImageBitmap(decodedByte);
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
                        String name = (dataSnapshot.child("name").getValue()).toString();
                        String email1=(dataSnapshot.child("phone").getValue()).toString();
                        fullName.setText(name);
                        email.setText(email1);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Toast.makeText(getActivity(),"Oppps....someThing is wrong",Toast.LENGTH_SHORT).show();

                }
            });

            p=new ArrayList<product>();

            ref= FirebaseDatabase.getInstance().getReference("CategoryProducts");
            ref= FirebaseDatabase.getInstance().getReference("CategoryProducts");
            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    p.clear();
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        product p1=dataSnapshot1.getValue(product.class);
                        p.add(p1);
                    }
                    adapter=new productAdapter(p,MainActivity.this);
                    adapter.notifyDataSetChanged();
                    //waitingtDialog.dismiss();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //Toast.makeText(MainActivity.this,""+p,Toast.LENGTH_SHORT).show();
            if(pendingNotifications!=0){

                // set the pending notifications value
            }

           invalidateOptionsMenu();


        }

        else{
            Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }

    }

    /*FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser == null) {
                Intent intent = new Intent(MainActivity.this, first.class);
                startActivity(intent);
            }
        }
    };
    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
    }*/

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id=menuItem.getItemId();

        if(id==R.id.nav_my_mall){
            DrawerLayout layout = (DrawerLayout)findViewById(R.id.drawer_layout);
            layout.closeDrawer(GravityCompat.START);

            HomeFragment.jump();

        }
        if(id==R.id.nav_singout){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, page1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //

        }
        if(id==R.id.nav_my_orders){
            //
            Intent intent=new Intent(MainActivity.this,OrderPage.class);
            startActivity(intent);
            //Toast.makeText(MainActivity.this,"Order",Toast.LENGTH_SHORT).show();
        }

        if(id==R.id.nav_my_account){
            //
            Intent intent=new Intent(MainActivity.this,ProfilePage.class);
            startActivity(intent);
        }
        if(id==R.id.nav_my_instruction){
            //
            Intent intent=new Intent(MainActivity.this,InstructionPage.class);
            startActivity(intent);
        }
        if(id==R.id.nav_my_about){
            //
            Intent intent=new Intent(MainActivity.this,AboutPage.class);
            startActivity(intent);
        }

        return false;
    }



    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }


    public static  void update_counter(int value){

        try{
              if(value!=0){
                  badge.setVisibility(View.VISIBLE);
                  badge.setText(String.valueOf(value));
              }
              else{
                  badge.setVisibility(View.INVISIBLE);
              }

        }
        catch (Exception ex){
            Log.d("Exception","Exception of type"+ex.getMessage());
        }
    }

    public boolean checkInternet(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        return connected;

    }

}
