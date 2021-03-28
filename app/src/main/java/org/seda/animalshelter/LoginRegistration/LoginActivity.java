package org.seda.animalshelter.LoginRegistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.seda.animalshelter.Activity.MainActivity;
import org.seda.animalshelter.Activity.MainMenuActivity;
import org.seda.animalshelter.Fragments.HomeFragment;
import org.seda.animalshelter.R;

import static org.seda.animalshelter.LoginRegistration.RegistrationActivity.reset_password;

public class LoginActivity extends AppCompatActivity {


    String user_id;
    private FirebaseAuth firebaseAuth;
    public static boolean disableCloseBtn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        init();
    }

    private void init() {
        final EditText userEmail= findViewById(R.id.editText_log_email_activity_login);
        final EditText userPass= findViewById(R.id.editText_log_pass_activity_login);
        Button btnLogin = findViewById(R.id.button_log_activity_login);


//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String user_email = userEmail.getText().toString().trim();
//                String user_pass= userPass.getText().toString().trim();
//
//                if(TextUtils.isEmpty(user_email)){
//                    userEmail.setError("Поле Email не может быть пустым!");
//                }
//                else if(TextUtils.isEmpty(user_pass)){
//                    userPass.setError("Поле Пароля не может быть пустым!");
//                }
//                else if(Patterns.EMAIL_ADDRESS.matcher(user_email).matches()){
//                    if(userPass.length()>=8){
//                        firebaseAuth.signInWithEmailAndPassword(userEmail.getText().toString(),userPass.getText().toString())
//                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<AuthResult> task) {
//                                        if(task.isSuccessful()){
//                                            Toast.makeText(LoginActivity.this, "Добро пожаловать!", Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(LoginActivity.this,  MainMenuActivity.class);
//                                            startActivity(intent);
//                                            Animatoo.animateSwipeLeft(LoginActivity.this);
//                                            finish();
//                                        }else{
//                                            String error = task.getException().getMessage();
//                                            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                    }else{
//                        Toast.makeText(LoginActivity.this, "Пароль не может быть меньше 8 символов", Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Toast.makeText(LoginActivity.this, "Ошибка,попробуйте заново!", Toast.LENGTH_SHORT).show();
//                }
////                else{
////                    final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
////                    dialog.setTitle("Авторизация");
////                    dialog.setMessage("Пожалуйста ожидайте,мы проверяем ваши данные");
////                    dialog.show();
////                    dialog.setCanceledOnTouchOutside(false);
////
////                    Call<Users> usersCall = apiClient.getEmailLogin(user_email,user_pass);
////                    usersCall.enqueue(new Callback<Users>() {
////                        @Override
////                        public void onResponse(Call<Users> call, Response<Users> response) {
////
////                            if(response.body().getResponse().equals("ok")){
////
////                                user_id = response.body().getUserId();
////      //                          sessionManager.createSession(user_id);
////                                Intent intent = new Intent(LoginActivity.this,MainMenuActivity.class);
////                                startActivity(intent);
////                                Animatoo.animateSwipeLeft(LoginActivity.this);
////                                finish();
////
//////                                Toast.makeText(LoginActivity.this,user_id, Toast.LENGTH_SHORT).show();
//////                                Toast.makeText(LoginActivity.this, "Добро пожаловать!", Toast.LENGTH_SHORT).show();
////                                dialog.dismiss();
////                            }
////                             if(response.body().getResponse().equals("no_account")){
////                                Toast.makeText(LoginActivity.this, "Ошибка!Пользователь не найден", Toast.LENGTH_SHORT).show();
////                                dialog.dismiss();
////                            }
////                        }
////
////                        @Override
////                        public void onFailure(Call<Users> call, Throwable t) {
////
////                        }
////                    });
////                }
//            }
//        });
    }

    public void btnLogin(View view) {

        final EditText userEmail= findViewById(R.id.editText_log_email_activity_login);
        final EditText userPass= findViewById(R.id.editText_log_pass_activity_login);
    //    Button btnLogin = findViewById(R.id.button_log_activity_login);

        String user_email = userEmail.getText().toString().trim();
        String user_pass= userPass.getText().toString().trim();

        if(TextUtils.isEmpty(user_email)){
            userEmail.setError("Поле Email не может быть пустым!");
        }
        else if(TextUtils.isEmpty(user_pass)){
            userPass.setError("Поле Пароля не может быть пустым!");
        }
        else if(Patterns.EMAIL_ADDRESS.matcher(user_email).matches()){
            if(userPass.length()>=8){
                firebaseAuth.signInWithEmailAndPassword(userEmail.getText().toString(),userPass.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "Добро пожаловать!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this,  MainMenuActivity.class);
                                    startActivity(intent);
                                    Animatoo.animateSwipeLeft(LoginActivity.this);
                                    finish();
                                }else{
                                    String error = task.getException().getMessage();
                                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else{
                Toast.makeText(LoginActivity.this, "Пароль не может быть меньше 8 символов", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(LoginActivity.this, "Ошибка,попробуйте заново!", Toast.LENGTH_SHORT).show();
        }
//                else{
//                    final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
//                    dialog.setTitle("Авторизация");
//                    dialog.setMessage("Пожалуйста ожидайте,мы проверяем ваши данные");
//                    dialog.show();
//                    dialog.setCanceledOnTouchOutside(false);
//
//                    Call<Users> usersCall = apiClient.getEmailLogin(user_email,user_pass);
//                    usersCall.enqueue(new Callback<Users>() {
//                        @Override
//                        public void onResponse(Call<Users> call, Response<Users> response) {
//
//                            if(response.body().getResponse().equals("ok")){
//
//                                user_id = response.body().getUserId();
//      //                          sessionManager.createSession(user_id);
//                                Intent intent = new Intent(LoginActivity.this,MainMenuActivity.class);
//                                startActivity(intent);
//                                Animatoo.animateSwipeLeft(LoginActivity.this);
//                                finish();
//
////                                Toast.makeText(LoginActivity.this,user_id, Toast.LENGTH_SHORT).show();
////                                Toast.makeText(LoginActivity.this, "Добро пожаловать!", Toast.LENGTH_SHORT).show();
//                                dialog.dismiss();
//                            }
//                             if(response.body().getResponse().equals("no_account")){
//                                Toast.makeText(LoginActivity.this, "Ошибка!Пользователь не найден", Toast.LENGTH_SHORT).show();
//                                dialog.dismiss();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Users> call, Throwable t) {
//
//                        }
//                    });
//                }
    }


    public void imageButton_backMainActivity(View view) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        Animatoo.animateSwipeLeft(LoginActivity.this);
        finish();
    }

    public void btn_go_to_menu(View view) {
        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
        startActivity(intent);
        Animatoo.animateSwipeLeft(LoginActivity.this);
        finish();
    }

    public void textView_forgot_psw_activity_login(View view) {
        reset_password = true;
        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
        Animatoo.animateSwipeLeft(LoginActivity.this);
        finish();
    }
}