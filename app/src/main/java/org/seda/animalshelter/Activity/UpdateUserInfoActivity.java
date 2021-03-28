package org.seda.animalshelter.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.tabs.TabLayout;

import org.seda.animalshelter.Fragments.UpdatePasswordUserFragment;
import org.seda.animalshelter.Fragments.UpdateUserFragment;
import org.seda.animalshelter.R;

public class UpdateUserInfoActivity extends AppCompatActivity {

    public static Toolbar toolbar;
    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private UpdateUserFragment updateUserFragment;
    private UpdatePasswordUserFragment updatePasswordUserFragment;
    private String name,email,photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = findViewById(R.id.tabLayout_activity_update_user_info);
        frameLayout = findViewById(R.id.frameLayout_activity_update_user_info);

        updateUserFragment = new UpdateUserFragment();
        updatePasswordUserFragment = new UpdatePasswordUserFragment();

         name = getIntent().getStringExtra("Name");
         email = getIntent().getStringExtra("Email");
         photo = getIntent().getStringExtra("Photo");

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){

                    setFragment(updateUserFragment,true);
                }
                if(tab.getPosition()==1){
                    setFragment(updatePasswordUserFragment,false);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.getTabAt(0).select();
        setFragment(updateUserFragment,true);
    }

    private void setFragment(Fragment fragment,boolean setBundle){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(setBundle){
            Bundle bundle = new Bundle();
            bundle.putString("Name",name);
            bundle.putString("Email",email);
            bundle.putString("Photo",photo);
            fragment.setArguments(bundle);
        }else {
            Bundle bundle = new Bundle();
            bundle.putString("Email",email);
            fragment.setArguments(bundle);
        }

        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
              finish();
             Animatoo.animateSwipeLeft(UpdateUserInfoActivity.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}