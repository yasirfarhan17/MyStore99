package com.noor.mystore99;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {




    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView categoryRecyclerView;
    private categoryAdaptor categoryAdaptor1;
    ArrayList<categoryModel> list1=new ArrayList<>();

    /////////Banner Slider
    private ViewPager bannersliderviewPager;
    ArrayList<sliderModel> sliderModelList=new ArrayList<>();
    private int currentPage=2;
    private Timer timer;
    final private long delayTime=3000;
    final private long periodTime=3000;
    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


    DatabaseReference ref;
    RecyclerView recyclerView;
    ArrayList<product> list;
    ArrayList<String> l=new ArrayList<>();
    ArrayList<product> list3;
    productAdapter adapter;
    int count=1;
    Button btn_ins,btn_dsc;
    TextView CountText,allcat,allPro,updateTime;
    boolean checkRun=false;

    SearchView searchView;
    Handler handler=new Handler();
    android.app.AlertDialog waitingtDialog;
    public static NestedScrollView nestedScrollView;
    public static Toolbar toolbar;







    ////////Banner slider

    ////// strip Ad
    private ImageView stripAdImage;
    private ConstraintLayout stripAdContainer;


    ////Strip Ad

    private Runnable update=new Runnable() {
        @Override
        public void run() {
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            String timeSub=currentTime.substring(0,5);
            String timeReplace=timeSub.replaceAll("[^a-zA-Z0-9]","");
            int timeCon=Integer.parseInt(timeReplace);
            if(timeCon>=800 && timeCon<=859){
                updateTime.setVisibility(View.VISIBLE);
                categoryRecyclerView.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                searchView.setVisibility(View.INVISIBLE);
                bannersliderviewPager.setVisibility(View.INVISIBLE);
                allcat.setVisibility(View.INVISIBLE);
                allPro.setVisibility(View.INVISIBLE);
                checkRun=true;

                ref=FirebaseDatabase.getInstance().getReference("Cart");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            snapshot1.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else {
                updateTime.setVisibility(View.GONE);
                categoryRecyclerView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                bannersliderviewPager.setVisibility(View.VISIBLE);
                allcat.setVisibility(View.VISIBLE);
                allPro.setVisibility(View.VISIBLE);
                checkRun=false;
            }

            handler.postDelayed(this,1000);

        }
    };
    private Runnable page=new Runnable() {
        @Override
        public void run() {
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            String timeSub=currentTime.substring(0,5);
            String timeReplace=timeSub.replaceAll("[^a-zA-Z0-9]","");
            int timeCon=Integer.parseInt(timeReplace);

            if(timeCon>=800 && timeCon<=859){
                updateTime.setVisibility(View.VISIBLE);
                categoryRecyclerView.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                searchView.setVisibility(View.INVISIBLE);
                bannersliderviewPager.setVisibility(View.INVISIBLE);
                allcat.setVisibility(View.INVISIBLE);
                allPro.setVisibility(View.INVISIBLE);
                checkRun=true;
                ref=FirebaseDatabase.getInstance().getReference("Cart");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            snapshot1.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
           else if(recyclerView.getVisibility()== View.INVISIBLE){
                updateTime.setVisibility(View.GONE);
                recyclerView.setAdapter(adapter);
                recyclerView.setVisibility(View.VISIBLE);
                checkRun=false;

            }
           //adapter.notifyDataSetChanged();
            handler.postDelayed(this,1000);

        }
    };

    private Runnable check=new Runnable() {
        @Override
        public void run() {
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            String timeSub=currentTime.substring(0,5);
            String timeReplace=timeSub.replaceAll("[^a-zA-Z0-9]","");
            int timeCon=Integer.parseInt(timeReplace);

            if(timeCon>=800 && timeCon<=859){
                updateTime.setVisibility(View.VISIBLE);
                categoryRecyclerView.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                searchView.setVisibility(View.INVISIBLE);
                bannersliderviewPager.setVisibility(View.INVISIBLE);
                allcat.setVisibility(View.INVISIBLE);
                allPro.setVisibility(View.INVISIBLE);
                checkRun=true;
                ref=FirebaseDatabase.getInstance().getReference("Cart");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            snapshot1.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
          else  if(searchView.getQuery().toString().isEmpty()|| searchView.getQuery().toString().equals("")){
                updateTime.setVisibility(View.GONE);
                categoryRecyclerView.setVisibility(View.VISIBLE);
                bannersliderviewPager.setVisibility(View.VISIBLE);
                allcat.setVisibility(View.VISIBLE);
                allPro.setVisibility(View.VISIBLE);
                checkRun=false;

            }

            handler.postDelayed(this,5000);


            //handler.postDelayed(check, 1000);


        }
    };




    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         final View view=inflater.inflate(R.layout.fragment_home2, container, false);
        //View view1=inflater.inflate(R.layout.cardview, container, false);
        //CountText=(TextView)view1.findViewById(R.id.inc_box);

        categoryRecyclerView=view.findViewById(R.id.category_recyclerView);
        allcat=view.findViewById(R.id.allCategory);

        allPro=view.findViewById(R.id.allproduct);
        nestedScrollView=view.findViewById(R.id.scroll);
        toolbar=view.findViewById(R.id.toolbar);


        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        final GridLayoutManager gridLayoutManager=new GridLayoutManager(view.getContext(),2);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(gridLayoutManager);
        searchView=view.findViewById(R.id.search);
        searchView.setQueryHint("Search...");
        searchView.setIconifiedByDefault(false);
        getActivity().onSearchRequested();
        updateTime=view.findViewById(R.id.updateTime);
        recyclerView=(RecyclerView)view.findViewById(R.id.product_recyclerView1);
        bannersliderviewPager=view.findViewById(R.id.banner_slider_view_pager);

       // Toast.makeText(view.getContext(),""+timeCon,Toast.LENGTH_LONG).show();

        if(checkRun){
            update.run();
        }
        else {
            waitingtDialog= new SpotsDialog.Builder().setContext(getActivity()).build();
            waitingtDialog.show();
            ref= FirebaseDatabase.getInstance().getReference("Category");
            //Toast.makeText(getActivity(),"hii",Toast.LENGTH_SHORT).show();
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list1.clear();

                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                        categoryModel p=dataSnapshot1.getValue(categoryModel.class);
                        list1.add(p);
                    }

                    categoryAdaptor1=new categoryAdaptor(list1,view.getContext());
                    categoryRecyclerView.setAdapter(categoryAdaptor1);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(),"Oppps....someThing is wrong", Toast.LENGTH_SHORT).show();

                }
            });




            /////banner slider


            categoryRecyclerView.setVisibility(View.VISIBLE);
            bannersliderviewPager.setVisibility(View.VISIBLE);

            ref=FirebaseDatabase.getInstance().getReference("Banner");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        int count = (int) dataSnapshot.getChildrenCount();
                        for (int i = 0; i < count + 1; i++) {
                            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                                String val1 = dataSnapshot1.child("img").getValue().toString();
                                String color = dataSnapshot1.child("color").getValue().toString();
                                sliderModel ob = new sliderModel(val1, color);
                                sliderModelList.add(ob);
                            }

                        }

                        sliderAdapter sliderAdapter = new sliderAdapter(sliderModelList);
                        bannersliderviewPager.setAdapter(sliderAdapter);
                        bannersliderviewPager.setClipToPadding(false);
                        bannersliderviewPager.setPageMargin(20);
                        sliderAdapter.notifyDataSetChanged();

                        bannersliderviewPager.setCurrentItem(currentPage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });







            ViewPager.OnPageChangeListener onPageChangeListener=new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPage=position;

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if(state== ViewPager.SCROLL_STATE_IDLE){
                        pageLooper();
                    }

                }
            };
            bannersliderviewPager.addOnPageChangeListener(onPageChangeListener);

            startbannerslideshow();
            bannersliderviewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    pageLooper();
                    stopbannerslidshow();
                    if(motionEvent.getAction()== MotionEvent.ACTION_UP){
                        startbannerslideshow();
                    }
                    return false;
                }
            });

            check.run();

            /////banner slider
            ///// strip Ad

        /*stripAdImage=view.findViewById(R.id.strip_ad_image);
        stripAdContainer=view.findViewById(R.id.strip_ad_container);
        stripAdImage.setImageResource(R.drawable.slider_imgg);
        stripAdContainer.setBackgroundColor(Color.parseColor("#000000"));*/



            /////strip Ad







            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
            //ViewCompat.setNestedScrollingEnabled(recyclerView, false);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(100);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            //update.run();
            list=new ArrayList<product>();

            ref= FirebaseDatabase.getInstance().getReference("CategoryProducts");
            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    //list.clear();
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        product p=dataSnapshot1.getValue(product.class);
                        list.add(p);

                    }
                    Collections.sort(list,(product p,product p1) ->{
                        return p.getProducts_name().compareToIgnoreCase(p1.getProducts_name());
                    });
                    adapter=new productAdapter(list,view.getContext());
                    //Toast.makeText(getActivity(),"Hii"+list,Toast.LENGTH_SHORT).show();
                    //adapter.notifyDataSetChanged();
                    recyclerView.setItemViewCacheSize(10);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                    waitingtDialog.dismiss();





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
            //Toast.makeText(getActivity(),""+list,Toast.LENGTH_LONG).show();


            page.run();



            if(searchView!=null){
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        search(newText,view.getContext());
                        //recreate1();

                        return true;
                    }




                });
                //check.run();
            }
            //searchView.setIconified(true);


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
                    categoryRecyclerView.setVisibility(View.VISIBLE);
                    bannersliderviewPager.setVisibility(View.VISIBLE);
                    allcat.setVisibility(View.VISIBLE);
                    allPro.setVisibility(View.VISIBLE);
                    // searchView.setIconifiedByDefault(false);

                }
            });
        }




       // searchView.clearFocus();














       /* adapter=new productAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new productAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position,int count,TextView box) {
                count++;
                setText(box,count);

            }

            @Override
            public void setText(TextView box, int val) {
                box.setText(String.valueOf(val));

            }

        });*/





        return view;
    }

    @Override
    public void onDestroy() {
        waitingtDialog.dismiss();
        super.onDestroy();
    }

    public  void search(String str, Context context) {
        ArrayList<product> prr=new ArrayList<>();

            for (product obj : list) {
                if (obj.getProducts_name().toLowerCase().contains(str.toLowerCase())) {
                    prr.add(obj);

                }
                adapter = new productAdapter(prr,context);
                recyclerView.setAdapter(adapter);

                //handler.removeCallbacks(check);


        }
        categoryRecyclerView.setVisibility(View.GONE);
        bannersliderviewPager.setVisibility(View.GONE);
        allcat.setVisibility(View.GONE);
        allPro.setVisibility(View.GONE);
        //Thread.sleep(500);
        //handler.removeCallbacks(check);

    }

    public Boolean recreate1(){

        return true;
    }



    /////banner slider

    private void pageLooper(){

        if(currentPage==sliderModelList.size()-2){
            currentPage=2;
            bannersliderviewPager.setCurrentItem(currentPage,false);
        }

        if(currentPage==1){
            currentPage=sliderModelList.size()-3;
            bannersliderviewPager.setCurrentItem(currentPage,false);
        }

    }

    private  void startbannerslideshow(){
        final Handler handler=new Handler();
        final Runnable update=new Runnable() {
            @Override
            public void run() {
                if(currentPage>=sliderModelList.size()){
                    currentPage=1;
                }
                bannersliderviewPager.setCurrentItem(currentPage++,true);
            }
        };
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        },delayTime,periodTime);
    }

    private void stopbannerslidshow(){
        timer.cancel();
    }



    /////banner slider


    /*public  class QuotesChildEventListner implements ChildEventListener {

        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            product p=dataSnapshot.getValue(pr);


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
    }*/

    public static void jump(){
        nestedScrollView.scrollTo(0,0);
    }

}
