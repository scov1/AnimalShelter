package org.seda.animalshelter.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.seda.animalshelter.Fragments.HomeFragment;
import org.seda.animalshelter.LoginRegistration.LoginActivity;
import org.seda.animalshelter.LoginRegistration.RegistrationActivity;
import org.seda.animalshelter.Models.HomePage;
import org.seda.animalshelter.R;

import java.util.HashMap;
import java.util.Map;

public class TakeHomePetActivity extends AppCompatActivity {

    public static Toolbar toolbar;
    private ImageView logo;
    private Window window;
    private EditText user_name,user_email;
    Button btnOrderCallBack;
    FirebaseAuth firebaseAuth;
    public static Activity takeHome;
    private FirebaseFirestore firebaseFirestore;
    private boolean successResponse = false;
    private Dialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_home_pet);

        toolbar = findViewById(R.id.toolbar);
        logo = findViewById(R.id.imageView_app_bar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     //   window = getWindow();
       // window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        init();
    }



    private void init() {

        user_name = findViewById(R.id.editText_activity_take_home_pet_name);
        user_email = findViewById(R.id.editText_activity_take_home_pet_email);
        btnOrderCallBack = findViewById(R.id.button_activity_take_home_pet_callback);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //malenkoe okoshko
        loadingDialog = new Dialog(TakeHomePetActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
     //   loadingDialog.show();
        //malenkoe okoshko




//        btnOrderCallBack.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//            }

    }

    public void btnNextTakeHomeMain(View view) {
        String name = user_name.getText().toString().trim();
        String email = user_email.getText().toString().trim();


        loadingDialog.show();
        if(TextUtils.isEmpty(name)){
            user_name.setError("Введите имя!");
        }
        else if(TextUtils.isEmpty(email)){
            user_email.setError("Введите email!");
        }

        else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Map<String,Object> users = new HashMap<>();
            users.put("name", user_name.getText().toString());
            users.put("email",user_email.getText().toString());

            firebaseFirestore.collection("TAKE_PET")
                    .document(firebaseAuth.getUid())
                    .set(users)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(TakeHomePetActivity.this, "Мы с вами свяжемся!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(TakeHomePetActivity.this, MainMenuActivity.class);
                                startActivity(intent);
                                Animatoo.animateSwipeLeft(TakeHomePetActivity.this);
                                finish();
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(TakeHomePetActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                            loadingDialog.dismiss();
                        }
                    });
        }else{
            user_email.setError("Email не корректный!");
        }
    }


        public void imageButton_backMainActivityTake(View view) {
        Intent intent = new Intent(TakeHomePetActivity.this, MainMenuActivity.class);
        startActivity(intent);
        Animatoo.animateSwipeLeft(TakeHomePetActivity.this);
        finish();

    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        Intent intent = new Intent(TakeHomePetActivity.this, MainMenuActivity.class);
//        startActivity(intent);
//        Animatoo.animateSwipeLeft(TakeHomePetActivity.this);
//        finish();
//    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            Animatoo.animateSwipeLeft(TakeHomePetActivity.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(successResponse){
            finish();
            return;
        }
        super.onBackPressed();
    }
}