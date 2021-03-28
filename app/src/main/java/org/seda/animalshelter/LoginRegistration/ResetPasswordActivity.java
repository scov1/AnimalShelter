package org.seda.animalshelter.LoginRegistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.seda.animalshelter.Activity.MainActivity;
import org.seda.animalshelter.R;

public class ResetPasswordActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText email;
    TextView sendMail;
  //  ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        init();
    }

    public void imageButton_backMainActivity(View view) {
        Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void init(){
        email = findViewById(R.id.editText_email_reset_password_activity);
        Button resetPass = findViewById(R.id.button_reset_password_activity);
        sendMail = findViewById(R.id.textView_mail_send_success_acitivity_reset_password);
        firebaseAuth = FirebaseAuth.getInstance();

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {

        String resetEmail = email.getText().toString().trim();

        if(resetEmail.isEmpty()){
            email.setError("Поле Email обязательное");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(resetEmail).matches()){
            email.setError("Поле Email не корректное");
            email.requestFocus();
            return;
        }

        firebaseAuth.sendPasswordResetEmail(resetEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    sendMail.setVisibility(View.VISIBLE);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                            Animatoo.animateSwipeLeft(ResetPasswordActivity.this);
                        }
                    }, 2000);

                }else{
                    Toast.makeText(ResetPasswordActivity.this, "Ошибка! Попробуйте заново", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}