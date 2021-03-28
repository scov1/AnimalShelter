package org.seda.animalshelter.LoginRegistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.seda.animalshelter.Activity.MainActivity;
import org.seda.animalshelter.Activity.MainMenuActivity;
import org.seda.animalshelter.Fragments.HomeFragment;
import org.seda.animalshelter.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText user_name,user_email,user_password;
    Button btnReg;
    FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    public static boolean reset_password = false;
    public static boolean disableCloseBtn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            RegistrationActivity.disableCloseBtn=false;
            LoginActivity.disableCloseBtn=false;
            if(reset_password){
                reset_password = false;
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                Animatoo.animateSwipeLeft(RegistrationActivity.this);
                finish();
                return false;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private void init() {

        user_name= findViewById(R.id.editText_activity_take_home_pet_name);
        user_email= findViewById(R.id.editText_activity_take_home_pet_email);
        user_password= findViewById(R.id.editText_reg_pass_activity_registration);
        btnReg = findViewById(R.id.button_activity_take_home_pet_callback);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        btnReg.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String name = user_name.getText().toString().trim();
                String email = user_email.getText().toString().trim();
                String password= user_password.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    user_name.setError("Введите имя!");
                }
                else if(TextUtils.isEmpty(email)){
                    user_email.setError("Введите email!");
                }
                else if(TextUtils.isEmpty(password)){
                    user_password.setError("Введите пароль!");
                }

                else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if(user_password.length()>=8) {
                        firebaseAuth.createUserWithEmailAndPassword(user_email.getText().toString(), user_password.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            Map<String,Object> users = new HashMap<>();
                                            users.put("fullname", user_name.getText().toString());
                                            users.put("email",user_email.getText().toString());
                                            users.put("profile","");

                                            firebaseFirestore.collection("USERS")
                                                    .document(firebaseAuth.getUid())
                                                    .set(users)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                CollectionReference userListData = firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA");

                                                                //MAPS
                                                                Map<String,Object> wishlistMap = new HashMap<>();
                                                                wishlistMap.put("list_size",(long)0);

                                                                Map<String,Object> basketlistMap = new HashMap<>();
                                                                basketlistMap.put("list_size",(long)0);

                                                                final List<String> documentNames = new ArrayList<>();
                                                                documentNames.add("MY_WISHLIST");
                                                                documentNames.add("MY_BASKET");

                                                                ///MAPS

                                                                List<Map<String,Object>> documentFields = new ArrayList<>();
                                                                documentFields.add(wishlistMap);
                                                                documentFields.add(basketlistMap);

                                                                for(int i = 0;i<documentNames.size();i++){
                                                                    final int finalI = i;
                                                                    userListData.document(documentNames.get(i))
                                                                            .set(documentFields.get(i)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){

                                                                                if(finalI == documentNames.size()-1){
                                                                                    Toast.makeText(RegistrationActivity.this, "Пользователь создан успешно!", Toast.LENGTH_SHORT).show();
                                                                                    Intent intent = new Intent(RegistrationActivity.this,  MainMenuActivity.class);
                                                                                    startActivity(intent);
                                                                                    Animatoo.animateSwipeLeft(RegistrationActivity.this);
                                                                                    finish();
                                                                                }
                                                                            }else{
                                                                                String error = task.getException().getMessage();
                                                                                Toast.makeText(RegistrationActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            } else {
                                                                String error = task.getException().getMessage();
                                                                Toast.makeText(RegistrationActivity.this, error, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            String error = task.getException().getMessage();
                                            Toast.makeText(RegistrationActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                    }
                    else{
                        user_password.setError("Пароль не может быть меньше 8 символов");
                    }
                }else{
                    user_email.setError("Email не корректный!");
                }
            }
        });

    }





    public void textView_login(View view) {
        Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
        startActivity(intent);
        Animatoo.animateSwipeLeft(RegistrationActivity.this);
        finish();
    }

    public void imageButton_backMainActivity(View view) {
        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(intent);
        Animatoo.animateSwipeLeft(RegistrationActivity.this);
        finish();
    }
}