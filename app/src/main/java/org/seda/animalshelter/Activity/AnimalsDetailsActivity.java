package org.seda.animalshelter.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.seda.animalshelter.Adapters.ServiceImagesAdapter;
import org.seda.animalshelter.DB.DBQuery;
import org.seda.animalshelter.Fragments.BasketFragment;
import org.seda.animalshelter.LoginRegistration.LoginActivity;
import org.seda.animalshelter.LoginRegistration.RegistrationActivity;
import org.seda.animalshelter.Models.Wishlist;
import org.seda.animalshelter.R;

import java.util.ArrayList;
import java.util.List;

import static org.seda.animalshelter.Activity.MainActivity.setSignUp;


public class AnimalsDetailsActivity extends AppCompatActivity {

    private ViewPager animalsImagesViewPager;
    private TabLayout viewPagerIndicator;
    private Button btnBuyProduct;
    private LinearLayout addToCartBtn;
    public static boolean fromSearch=false;
    private FirebaseFirestore firebaseFirestore;
    private TextView animalTitle;
    private TextView animalAge;
    private TextView animalGender;
    private TextView animalDesc;
    private Dialog signIn;
    private FirebaseUser currentUser;
    public static MenuItem basketItem;
    private TextView badgeCount;
    private Dialog loadingDialog;
    public static Boolean showBasket = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        animalsImagesViewPager = findViewById(R.id.viewPager_animals_details_images_layout);
        viewPagerIndicator = findViewById(R.id.tabLayout_indicator_animals_images_layout);
        btnBuyProduct = findViewById(R.id.button_main_take_home_pet_btn);
        animalTitle = findViewById(R.id.textView_title_animals_images_layout);
        animalAge = findViewById(R.id.textView_animals_images_layout_age);
        animalGender = findViewById(R.id.textView_animals_details_images_layout_gender);
        animalDesc = findViewById(R.id.textView_desc_animals_description_layout);
        addToCartBtn = findViewById(R.id.textView_activity_animals_details_addCart_btn);

        firebaseFirestore = FirebaseFirestore.getInstance();
        final List<String> animalImages = new ArrayList<>();
        firebaseFirestore.collection("ANIMALS")
                .document(getIntent().getStringExtra("animal_id"))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    for (long i=1;i<=2;i++){
                        animalImages.add(documentSnapshot.get("animal_image_" + i).toString());
                    }
                    ServiceImagesAdapter animalImagesAdapter = new ServiceImagesAdapter(animalImages);
                    animalsImagesViewPager.setAdapter(animalImagesAdapter);

                    animalTitle.setText(documentSnapshot.get("animal_title").toString());
                    animalAge.setText(documentSnapshot.get("animal_age").toString());
                    animalGender.setText(documentSnapshot.get("animal_gender").toString());
                    animalDesc.setText(documentSnapshot.get("animal_desc").toString());


                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(AnimalsDetailsActivity.this,error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //malenkoe okoshko
        loadingDialog = new Dialog(AnimalsDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        //  loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.cats_banner));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      //  loadingDialog.show();

        //malenkoe okoshko

        viewPagerIndicator.setupWithViewPager(animalsImagesViewPager,true);

        final List<Wishlist> viewAllProductList = new ArrayList<>();

        btnBuyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentUser==null){
                    signIn.show();

                }else {
                    Intent intent = new Intent(AnimalsDetailsActivity.this, ViewAllActivity.class);
                    intent.putExtra("layout_code",0);
                    startActivity(intent);
                }
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentUser==null){
                    signIn.show();

                }else {
                    ///todo add to cart
                }
            }
        });

        signIn = new Dialog(AnimalsDetailsActivity.this);
        signIn.setContentView(R.layout.sign_in_dialog);
        signIn.setCancelable(true);
        signIn.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        Button signInBtn = signIn.findViewById(R.id.button_sign_in_dialog_sign);
        Button signUpBtn = signIn.findViewById(R.id.button_sign_up_dialog_sign_up);
        final Intent registerIntent = new Intent(AnimalsDetailsActivity.this, RegistrationActivity.class);
        final Intent loginIntent = new Intent(AnimalsDetailsActivity.this, LoginActivity.class);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.disableCloseBtn=true;
                signIn.dismiss();
                setSignUp = false;
                startActivity(loginIntent);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrationActivity.disableCloseBtn=true;
                signIn.dismiss();
                setSignUp = true;
                startActivity(registerIntent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        basketItem = menu.findItem(R.id.item_cart_main_menu);
        basketItem.setActionView(R.layout.badge_layout);
        ImageView badgeIcon = basketItem.getActionView().findViewById(R.id.imageView_count_basket_items_icon);
        badgeIcon.setImageResource(R.drawable.icon_cart);
        badgeCount = basketItem.getActionView().findViewById(R.id.textView_count_basket_items_count);

        if(currentUser !=null){
            if(DBQuery.basketlist.size() == 0){
                badgeCount.setVisibility(View.GONE);
                DBQuery.loadBasketList(AnimalsDetailsActivity.this,loadingDialog,false,badgeCount,new TextView(AnimalsDetailsActivity.this));
            }else{
                badgeCount.setVisibility(View.VISIBLE);
                if(DBQuery.basketlist.size()<99) {
                    badgeCount.setText(String.valueOf(DBQuery.basketlist.size()));
                }else{
                    badgeCount.setText("99");
                }

            }
        }
        basketItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentUser==null){
                    signIn.show();
                }else{
                    Intent intent = new Intent(AnimalsDetailsActivity.this, MainMenuActivity.class);
                    showBasket = true;
                    startActivity(intent);
                    finish();
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            Animatoo.animateSwipeLeft(AnimalsDetailsActivity.this);
            return true;
        }

       if(id == R.id.item_search_main_menu){
            if(fromSearch){
                finish();
            }
            else{
                Intent intent = new Intent(AnimalsDetailsActivity.this,SearchActivity.class);
                startActivity(intent);
            }

            return true;
        }
        else if(id == R.id.items_cart_main_menu){
            Intent intent = new Intent(AnimalsDetailsActivity.this,BasketFragment.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        fromSearch = false;
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}