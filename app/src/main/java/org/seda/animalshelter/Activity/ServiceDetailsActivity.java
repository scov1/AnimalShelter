package org.seda.animalshelter.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.seda.animalshelter.Adapters.ServiceImagesAdapter;
import org.seda.animalshelter.DB.DBQuery;
import org.seda.animalshelter.Fragments.BasketFragment;
import org.seda.animalshelter.Models.Basket;
import org.seda.animalshelter.Models.Wishlist;
import org.seda.animalshelter.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import static org.seda.animalshelter.DB.DBQuery.firebaseCurrentUser;

public class ServiceDetailsActivity extends AppCompatActivity {


    private ViewPager serviceImagesViewPager;
    private TabLayout viewPagerIndicator;
    public static FloatingActionButton addToFavoriteList;
    public static boolean ADD_TO_FAVORITE_LIST = false;
    public static boolean ADD_TO_BASKET_LIST = false;
    public static boolean running_wishlist_query = false;
    public static Activity productDetailsActivity;
    private Button btnBuyProduct;
    private LinearLayout addToCartBtn;
    public static MenuItem basketItem;
    private FirebaseFirestore firebaseFirestore;
    private TextView serviceTitle;
    private TextView servicePrice;
    private ImageView serviceIndicator;
    private TextView serviceIndicatorTxt;
    private TextView serviceDesc;
    private Dialog signIn;
    private FirebaseUser currentUser;
    public static String productId;
    private Dialog loadingDialog;
    private DocumentSnapshot documentSnapshot;
    public static boolean running_basket_query = false;
    private TextView badgeCount;
    public static Boolean showBasket = false;
    public static boolean fromSearch=false;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        serviceImagesViewPager = findViewById(R.id.viewPager_animals_details_images_layout);
        viewPagerIndicator = findViewById(R.id.tabLayout_indicator_animals_images_layout);
        addToFavoriteList = findViewById(R.id.floatingActionButton_addFavoriteList_service_images_layout);
        btnBuyProduct = findViewById(R.id.button_activity_service_details_buyNow);
        serviceTitle = findViewById(R.id.textView_title_animals_images_layout);
        servicePrice = findViewById(R.id.textView_animals_images_layout_age);
        serviceIndicator = findViewById(R.id.imageView_indicator_available_service_images_layout);
        serviceIndicatorTxt = findViewById(R.id.textView_animals_details_images_layout_gender);
        serviceDesc = findViewById(R.id.textView_desc_animals_description_layout);
        addToCartBtn = findViewById(R.id.textView_activity_service_details_addCart_btn);


        //malenkoe okoshko
        loadingDialog = new Dialog(ServiceDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        //  loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.cats_banner));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        //malenkoe okoshko

        firebaseFirestore = FirebaseFirestore.getInstance();
        final List<String> serviceImages = new ArrayList<>();
        productId = getIntent().getStringExtra("product_id");
        firebaseFirestore.collection("PRODUCTS")
                .document(productId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();
                    for (long i = 1; i <= 2; i++) {
                        serviceImages.add(documentSnapshot.get("product_image_" + i).toString());
                    }
                    ServiceImagesAdapter serviceImagesAdapter = new ServiceImagesAdapter(serviceImages);
                    serviceImagesViewPager.setAdapter(serviceImagesAdapter);

                    serviceTitle.setText(documentSnapshot.get("product_title").toString());
                    servicePrice.setText(documentSnapshot.get("product_price").toString() + " KZT");
                    serviceDesc.setText(documentSnapshot.get("product_desc").toString());
                    //          serviceIndicator.setVisibility(View.VISIBLE);
//                    serviceIndicatorTxt.setText(View.VISIBLE);

                    if (currentUser != null) {
                        if (DBQuery.wishlist.size() == 0) {
                            DBQuery.loadWishlist(ServiceDetailsActivity.this, loadingDialog, false);
                        }
                        if (DBQuery.basketlist.size() == 0) {
                            DBQuery.loadBasketList(ServiceDetailsActivity.this, loadingDialog, false,badgeCount,new TextView(ServiceDetailsActivity.this));

                        } else {
                            loadingDialog.dismiss();
                        }
                    } else {
                        loadingDialog.dismiss();
                    }


                    if (DBQuery.wishlist.contains(productId)) {
                        ADD_TO_FAVORITE_LIST = true;
                        addToFavoriteList.setSupportImageTintList(getResources().getColorStateList(R.color.colorRed));
                    } else {
                        ADD_TO_FAVORITE_LIST = false;
                    }
                    if (DBQuery.basketlist.contains(productId)) {
                        ADD_TO_BASKET_LIST = true;

                    } else {
                        ADD_TO_BASKET_LIST = false;
                    }

                } else {
                    loadingDialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(ServiceDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewPagerIndicator.setupWithViewPager(serviceImagesViewPager, true);

        addToFavoriteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser == null) {
                    signIn.show();
                } else {
                    //addToFavoriteList.setEnabled(false);
                    if (!running_wishlist_query) {
                        running_wishlist_query = true;
                        if (ADD_TO_FAVORITE_LIST) {
                            int index = DBQuery.wishlist.indexOf(productId);
                            DBQuery.removeFragmentWishlist(index, ServiceDetailsActivity.this);
                            addToFavoriteList.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                        } else {
                            addToFavoriteList.setSupportImageTintList(getResources().getColorStateList(R.color.colorRed));
                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_id_" + String.valueOf(DBQuery.wishlist.size()), productId);
                            addProduct.put("list_size", (long) DBQuery.wishlist.size() + 1);
                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

//                                    Map<String, Object> updateListSize = new HashMap<>();
//                                    updateListSize.put("list_size", (long) DBQuery.wishlist.size() + 1);

//                                    firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
//                                            .update(updateListSize).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {

                                        if (DBQuery.wishListModel.size() != 0) {
                                            DBQuery.wishListModel.add(new Wishlist(productId,
                                                    documentSnapshot.get("product_image_").toString(),
                                                    documentSnapshot.get("product_title_").toString(),
                                                    documentSnapshot.get("product_price_").toString()
                                            ));
                                        }
                                        ADD_TO_FAVORITE_LIST = true;
                                        addToFavoriteList.setSupportImageTintList(getResources().getColorStateList(R.color.colorRed));
                                        DBQuery.wishlist.add(productId);
                                        Toast.makeText(ServiceDetailsActivity.this, "Товар добавлен в избранное!", Toast.LENGTH_SHORT).show();

                                    } else {
                                        addToFavoriteList.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ServiceDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    running_wishlist_query=false;
                                }
                            });
                        }
                    }
                }
            }
        });

        btnBuyProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (currentUser == null) {
                    signIn.show();

                } else {
                //    loadingDialog.dismiss();
//                    DeliveryActivity.basketModelList = new ArrayList<>();
////DeliveryActivity.basketModelList.clear();
//
//                    DeliveryActivity.basketModelList.add(new Basket(Basket.BASKET, productId,
//
//                            documentSnapshot.get("product_image_1").toString(),
//                            documentSnapshot.get("product_name").toString(),
//                            documentSnapshot.get("product_price").toString(),
//                            (long) 1
//                    ));
//                    DeliveryActivity.basketModelList.add(new Basket(Basket.TOTAL_AMOUNT));
//
//                    if(DBQuery.basketListModel.size()==0){
//                        DBQuery.loadBasketList(ServiceDetailsActivity.this,loadingDialog,true,new TextView(ServiceDetailsActivity.this),badgeCount);
//                    }else{
//
//                    }
                    PaymentActivity.fromCart =false;
                    loadingDialog.show();
                    productDetailsActivity = ServiceDetailsActivity.this;
                    loadingDialog.dismiss();
                    Intent intent = new Intent(ServiceDetailsActivity.this, PaymentActivity.class);
                    startActivity(intent);
                }
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser == null) {
                    signIn.show();
                } else {
                    if (!running_basket_query) {
                        running_basket_query = true;
                        if (ADD_TO_BASKET_LIST) {
                            running_basket_query = false;
                            Toast.makeText(ServiceDetailsActivity.this, "Товар уже в корзине", Toast.LENGTH_SHORT).show();
                        } else {
                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_id_" + String.valueOf(DBQuery.basketlist.size()), productId);
                            addProduct.put("list_size", (long) DBQuery.basketlist.size() + 1);
                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_BASKET")
                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                            if (DBQuery.basketListModel.size() != 0) {
                                                DBQuery.basketListModel.add(0,new Basket(Basket.BASKET, productId,
                                                        documentSnapshot.get("product_image_1").toString(),
                                                        documentSnapshot.get("product_name").toString(),
                                                        documentSnapshot.get("product_price").toString(),
                                                        (long) 1
                                                ));
                                            }
                                            ADD_TO_BASKET_LIST = true;
                                            DBQuery.basketlist.add(productId);
                                            Toast.makeText(ServiceDetailsActivity.this, "Товар добавлен в корзину!", Toast.LENGTH_SHORT).show();
                                            invalidateOptionsMenu();
                                            running_basket_query = false;
                                    } else {
                                        running_basket_query = false;
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ServiceDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
//        Button signInBtn = signIn.findViewById(R.id.button_sign_in_dialog_sign);
//      Button signUpBtn = signIn.findViewById(R.id.button_sign_up_dialog_sign_up);
//        final Intent registerIntent = new Intent(ServiceDetailsActivity.this, RegistrationActivity.class);
//        final Intent loginIntent = new Intent(ServiceDetailsActivity.this, LoginActivity.class);

//        signInBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LoginActivity.disableCloseBtn=true;
//                signIn.dismiss();
//                setSignUp = false;
//                startActivity(loginIntent);
//            }
//        });

//        signUpBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                RegistrationActivity.disableCloseBtn=true;
//                signIn.dismiss();
//                setSignUp = true;
//                startActivity(registerIntent);
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            if(DBQuery.wishlist.size() == 0){
                DBQuery.loadWishlist(ServiceDetailsActivity.this,loadingDialog,false);
            }
//            if(DBQuery.basketlist.size() == 0){
//                DBQuery.loadBasketList(ServiceDetailsActivity.this,loadingDialog,false,badgeCount,new TextView(ServiceDetailsActivity.this));
//            }
            else{
                loadingDialog.dismiss();
            }
        }else{
            loadingDialog.dismiss();
        }


        if(DBQuery.wishlist.contains(productId)){
            ADD_TO_FAVORITE_LIST = true;
            addToFavoriteList.setSupportImageTintList(getResources().getColorStateList(R.color.colorRed));
        }else{
            addToFavoriteList.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            ADD_TO_FAVORITE_LIST = false;
        }
        if(DBQuery.basketlist.contains(productId)){
            ADD_TO_BASKET_LIST = true;

        }else{
            ADD_TO_BASKET_LIST = false;
        }
        invalidateOptionsMenu();
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
                DBQuery.loadBasketList(ServiceDetailsActivity.this,loadingDialog,false,badgeCount,new TextView(ServiceDetailsActivity.this));
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
                        Intent intent = new Intent(ServiceDetailsActivity.this, MainMenuActivity.class);
                        showBasket = true;
                        startActivity(intent);
                        Animatoo.animateSwipeLeft(ServiceDetailsActivity.this);
                        finish();
                    }
                }
            });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            productDetailsActivity = null;
            finish();
            return true;
        } else if (id == R.id.item_search_main_menu) {
            if(fromSearch){
                finish();
            }
            else{
                Intent intent = new Intent(ServiceDetailsActivity.this,SearchActivity.class);
                startActivity(intent);
                Animatoo.animateSwipeLeft(ServiceDetailsActivity.this);
            }

            return true;
        }
        else if (id == R.id.item_cart_main_menu) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        productDetailsActivity = null;
        super.onBackPressed();
    }
}

