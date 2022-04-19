package com.noor.mystore99;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryProduct extends AppCompatActivity {

    DatabaseReference ref;
    RecyclerView recyclerView;
    ArrayList<product> list=new ArrayList<>();
    CategoryProductAdapter adapter;
    categoryModel model=new categoryModel();
    String name1;int index;
    TextView header;
    SearchView searchView ;
    Button back;
    static TextView badge;
    static  int bVal;
    ImageView imageView;
    FrameLayout frameLayout1;
    View view1;
    Handler handler=new Handler();


    private Runnable check=new Runnable() {
        @Override
        public void run() {

if(FirebaseAuth.getInstance().getCurrentUser()!=null){
    ref = FirebaseDatabase.getInstance().getReference("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    ref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            bVal=0;
            if (dataSnapshot.exists()) {
                bVal = (int) dataSnapshot.getChildrenCount();

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    // Log.d("badge =",""+bVal);


    if (bVal == 0) {
        badge.setVisibility(View.GONE);
    } else {
        badge.setVisibility(View.VISIBLE);
        badge.setText(String.valueOf(bVal));
    }

}



            handler.postDelayed(this,100);


            //handler.postDelayed(check, 1000);


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_product);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        recyclerView=findViewById(R.id.Category_product);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        name1= getIntent().getStringExtra("name");
        index=getIntent().getIntExtra("index",0);
        //Toast.makeText(CategoryProduct.this,name1+" "+index,Toast.LENGTH_SHORT).show();
        header=(TextView)findViewById(R.id.header);
        header.setText(name1);
        searchView=findViewById(R.id.search);
        searchView.setQueryHint("Search...");
        searchView.setIconifiedByDefault(false);
        searchView.clearFocus();
        onSearchRequested();
        back=findViewById(R.id.back);
        view1= findViewById(R.id.cartTest);
        badge = (TextView) view1.findViewById(R.id.badge_counter);
        imageView=view1.findViewById(R.id.cartId);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CategoryProduct.this,CartProductList.class);
                startActivity(intent);
            }
        });
        check.run();





        ref= FirebaseDatabase.getInstance().getReference("CategoryProducts").child(name1);
        //Toast.makeText(getActivity(),"hii",Toast.LENGTH_SHORT).show();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                    product p=dataSnapshot1.getValue(product.class);
                    list.add(p);
                }

                adapter=new CategoryProductAdapter(list,CategoryProduct.this);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CategoryProduct.this,"Oppps....someThing is wrong", Toast.LENGTH_SHORT).show();

            }
        });

        if(searchView!=null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        int searchCloseButtonId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView) this.searchView.findViewById(searchCloseButtonId);
// Set on click listener
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Manage this event.
                //searchView.onActionViewCollapsed();
                searchView.setQuery("", false);
                searchView.clearFocus();

            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        return super.onOptionsItemSelected(item);
    }
    public  void search(String str){
        ArrayList<product> prr=new ArrayList<>();

        for(product obj:list){
            if(obj.getProducts_name().toLowerCase().contains(str.toLowerCase())){
                prr.add(obj);
            }
           // Toast.makeText(CategoryProduct.this,""+prr, Toast.LENGTH_SHORT).show();
            adapter=new CategoryProductAdapter(prr,CategoryProduct.this);
            recyclerView.setAdapter(adapter);

        }

    }



}
