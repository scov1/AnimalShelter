package org.seda.animalshelter.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.seda.animalshelter.DB.DBQuery;
import org.seda.animalshelter.Fragments.AccountFragment;
import org.seda.animalshelter.Fragments.BasketFragment;
import org.seda.animalshelter.Fragments.HomeFragment;
import org.seda.animalshelter.Fragments.PetsFragment;
import org.seda.animalshelter.Fragments.OrdersFragment;
import org.seda.animalshelter.Fragments.UserFragment;
import org.seda.animalshelter.Fragments.WishlistFragment;
import org.seda.animalshelter.Fragments.WriteUsFragment;
import org.seda.animalshelter.R;

import de.hdodenhof.circleimageview.CircleImageView;
//import static org.seda.animalshelter.DB.DBQuery.firebaseCurrentUser;

public class MainMenuActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener{

    public static Toolbar toolbar;
    private FirebaseUser currentUser;
    private TextView badgeCount;
    public static Boolean showBasket = false;
    private ConstraintLayout constraintLayout;
    private static final int HOME_FRAGMENT = 0;
    private static final int BASKET_FRAGMENT = 1;
    private static final int ORDER_FRAGMENT = 2;
    private static final int WISHLIST_FRAGMENT = 3;
    private static final int ACCOUNT_FRAGMENT = 4;
    private static final int WRITEUS_FRAGMENT = 5;
    private static int currentFragment = -1;
    NavigationView navigationView;
    private ImageView logo;
    private Dialog signIn;
    private int scrollFlags;
    private AppBarLayout.LayoutParams params;
    public static DrawerLayout drawer;
    private Window window;
    public static Activity mainMenuActivity;
    public static boolean resetMainActivity = false;
    private CircleImageView profileView;
    private TextView fullname,email;
    private ImageView addProfileIcon;

 //   @SuppressLint("WrongConstant")
   // @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        toolbar = findViewById(R.id.toolbar);
        logo = findViewById(R.id.imageView_app_bar_main);
        setSupportActionBar(toolbar);
        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        scrollFlags = params.getScrollFlags();

        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view_main_menu);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        constraintLayout = findViewById(R.id.constraintLayout_content_main);

        profileView = navigationView.getHeaderView(0).findViewById(R.id.imageView_img_profile_nav_header_main);
        fullname = navigationView.getHeaderView(0).findViewById(R.id.textView_fullname_nav_header_main);
        email = navigationView.getHeaderView(0).findViewById(R.id.textView_email_nav_header_main);
        addProfileIcon = navigationView.getHeaderView(0).findViewById(R.id.add_img_profile_icon_nav_header_main);

        if (showBasket) {
            mainMenuActivity = this;
          //  drawer.setDrawerLockMode(1);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getFragments("Моя корзина",new BasketFragment(),-2);
        }else{
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            setFragment(new HomeFragment(),HOME_FRAGMENT);
        }


//
//        signIn = new Dialog(MainMenuActivity.this);
//        signIn.setContentView(R.layout.sign_in_dialog);
//        signIn.setCancelable(true);
//        signIn.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        Button signInBtn = signIn.findViewById(R.id.button_sign_in_dialog_sign);
//        Button signUpBtn = signIn.findViewById(R.id.button_sign_up_dialog_sign_up);
//        final Intent registerIntent = new Intent(MainMenuActivity.this, RegistrationActivity.class);
//        final Intent loginIntent = new Intent(MainMenuActivity.this, LoginActivity.class);
//
//        signInBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LoginActivity.disableCloseBtn=true;
//                signIn.dismiss();
//                setSignUp = false;
//                startActivity(loginIntent);
//            }
//        });
//
//        signUpBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                RegistrationActivity.disableCloseBtn = true;
//                signIn.dismiss();
//                setSignUp = true;
//                startActivity(registerIntent);
//            }
//        });

      //  BottomNavigationView bottomNavigateMenu = findViewById(R.id.bottomNavigateMenu);
     //   bottomNavigateMenu.setOnNavigationItemSelectedListener(navigate);

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser==null){
            navigationView.getMenu().getItem(navigationView.getMenu().size()-1).setEnabled(false);
        }else{
            if(DBQuery.email==null) {
                FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DBQuery.fullname = task.getResult().getString("fullname");
                            DBQuery.email = task.getResult().getString("email");
                            DBQuery.profile = task.getResult().getString("profile");

                            fullname.setText(DBQuery.fullname);
                            email.setText(DBQuery.email);
                            if (DBQuery.profile.equals("")) {
                                addProfileIcon.setVisibility(View.VISIBLE);
                            }
                            else {
                                addProfileIcon.setVisibility(View.INVISIBLE);
                                Glide.with(MainMenuActivity.this).load(DBQuery.profile).apply(new RequestOptions().placeholder(R.mipmap.ic_img_profile)).into(profileView);
                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(MainMenuActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                fullname.setText(DBQuery.fullname);
                email.setText(DBQuery.email);
                if (DBQuery.profile.equals("")) {
                    profileView.setImageResource(R.mipmap.ic_img_profile);
                    addProfileIcon.setVisibility(View.VISIBLE);
                } else {
                    addProfileIcon.setVisibility(View.INVISIBLE);
                    Glide.with(MainMenuActivity.this).load(DBQuery.profile).apply(new RequestOptions().placeholder(R.mipmap.ic_img_profile)).into(profileView);
                }
            }
            navigationView.getMenu().getItem(navigationView.getMenu().size()-1).setEnabled(true);
        }
        if(resetMainActivity){
            resetMainActivity=false;
            setFragment(new HomeFragment(),HOME_FRAGMENT);
        }
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer((GravityCompat.START));
        }else{
            if(currentFragment==HOME_FRAGMENT){
                currentFragment = -1;
                super.onBackPressed();
            }else{
                if(showBasket){
                    mainMenuActivity = null;
                    showBasket=false;
                    finish();
                }else{
//                    logo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new HomeFragment(),HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(currentFragment == HOME_FRAGMENT){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main_menu, menu);

            MenuItem basketItem = menu.findItem(R.id.item_cart_main_menu);

            basketItem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon = basketItem.getActionView().findViewById(R.id.imageView_count_basket_items_icon);
            badgeIcon.setImageResource(R.drawable.icon_cart);
            badgeCount = basketItem.getActionView().findViewById(R.id.textView_count_basket_items_count);


                if(currentUser !=null){
                    if(DBQuery.basketlist.size() == 0){
                     //   badgeCount.setVisibility(View.GONE);
                        DBQuery.loadBasketList(MainMenuActivity.this,new Dialog(MainMenuActivity.this),false,badgeCount,new TextView(MainMenuActivity.this));
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
                            getFragments("Моя корзина",new BasketFragment(),BASKET_FRAGMENT);
                        }
                    }
                });
        }
        return true;
    }



    //    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }


   //HOME MENU

//    logo.setVisibility(View.VISIBLE);
//    invalidateOptionsMenu();
//    setFragment(new HomeFragment(),HOME_FRAGMENT);

    ///////MENU BOKOVOE

    MenuItem menuItem;
    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        menuItem=item;

        if(currentUser!=null) {
            drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                @Override
                public void onDrawerClosed(@NonNull View drawerView) {
                    super.onDrawerClosed(drawerView);


                    int id = menuItem.getItemId();
                    if (id == R.id.nav_my_home_main) {
//                        logo.setVisibility(View.VISIBLE);
                        invalidateOptionsMenu();
                        setFragment(new HomeFragment(),HOME_FRAGMENT);
                      //  getFragments("Главная", new HomeFragment(), HOME_FRAGMENT);
                    } else if (id == R.id.nav_my_account_main) {
                        getFragments("Личный кабинет", new UserFragment(), ACCOUNT_FRAGMENT);
                    } else if (id == R.id.nav_my_basket_main) {
                        getFragments("Моя корзина", new BasketFragment(), BASKET_FRAGMENT);
                    } else if (id == R.id.nav_my_favorite_main) {
                        getFragments("Избранное", new WishlistFragment(), WISHLIST_FRAGMENT);
                    }
//                    else if (id == R.id.nav_orders_main) {
//                        getFragments("Мои покупки", new OrdersFragment(), ORDER_FRAGMENT);
//                    }
//                    else if (id == R.id.nav_about_us_main) {
//
//                    }
                    else if (id == R.id.nav_feedback_main) {
                        getFragments("", new WriteUsFragment(), WRITEUS_FRAGMENT);

                    } else if (id == R.id.nav_logout_main) {
                        FirebaseAuth.getInstance().signOut();
                        DBQuery.clearData();
                        Intent registerIntent = new Intent(MainMenuActivity.this,MainActivity.class);
                        startActivity(registerIntent);
                        finish();
                    }
                    drawer.removeDrawerListener(this);
                }
            });

            drawer.closeDrawer(GravityCompat.START);
            return true;
        }else{
            drawer.closeDrawer(GravityCompat.START);
         //   signIn.show();
            return false;
        }
    }

    ////// FRAGMENTS
//    private BottomNavigationView.OnNavigationItemSelectedListener navigate= new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//            Fragment choiceFragment = null;
//
//            switch (item.getItemId()){
//
//                case R.id.item_navigate_pets:
//                    choiceFragment = new PetsFragment();
//                    break;
//                case R.id.item_navigate_store:
//                    choiceFragment = new OrdersFragment();
//                    break;
//                case R.id.item_navigate_basket:
//             //       choiceFragment = new BasketFragment();
//
//               //     getFragments("Моя корзина",new BasketFragment(),BASKET_FRAGMENT);
//                    break;
//                case R.id.item_navigate_user:
//                    choiceFragment = new UserFragment();
//                    break;
//            }
//        //    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_for_fragment_activity_menu,choiceFragment).commit();
//            return true;
//        }
//    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

       // Fragment choiceFragment = null;

        if(id == R.id.item_search_main_menu){

            Intent intent = new Intent(MainMenuActivity.this,SearchActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.item_cart_main_menu){
            if(currentUser==null){
                signIn.show();
            }else{
                getFragments("Моя корзина",new BasketFragment(),BASKET_FRAGMENT);
            }
            return true;
        }else if(id==android.R.id.home){
            if(showBasket){
                mainMenuActivity = null;
                showBasket = false;
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void getFragments(String title,Fragment fragment,int fragmentNo) {
    //    logo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();
        setFragment(fragment,fragmentNo);
        if(fragmentNo == BASKET_FRAGMENT || showBasket){
            navigationView.getMenu().getItem(3).setChecked(true);
            params.setScrollFlags(0);
        }else{
            params.setScrollFlags(scrollFlags);
        }

    }

    private void setFragment(Fragment fragment,int fragmentNo){
        if(fragmentNo!=currentFragment){
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
            fragmentTransaction.replace(constraintLayout.getId(),fragment);
            fragmentTransaction.commit();
        }
    }
}