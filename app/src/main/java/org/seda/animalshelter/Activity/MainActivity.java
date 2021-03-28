package org.seda.animalshelter.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import org.seda.animalshelter.LoginRegistration.LoginActivity;
import org.seda.animalshelter.LoginRegistration.RegistrationActivity;
import org.seda.animalshelter.LoginRegistration.ResetPasswordActivity;
import org.seda.animalshelter.R;

public class MainActivity extends AppCompatActivity {


   public static boolean setSignUp = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//
//        if(setSignUp){
//            setSignUp = false;
//            Intent intent = new Intent(MainActivity.this,RegistrationActivity.class);
//            startActivity(intent);
//            Animatoo.animateSwipeLeft(MainActivity.this);
//            finish();
//        }
//        else{
//            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//            startActivity(intent);
//            Animatoo.animateSwipeLeft(MainActivity.this);
//            finish();
//        }


    }

    public void btnLogin(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        Animatoo.animateSwipeLeft(MainActivity.this);
        finish();
    }

    public void btnReg(View view) {
        Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
        Animatoo.animateSwipeLeft(MainActivity.this);
        startActivity(intent);
        finish();
    }

    public void textView_forgot_psw(View view) {
        Intent intent = new Intent(MainActivity.this, ResetPasswordActivity.class);
        Animatoo.animateSwipeLeft(MainActivity.this);
        startActivity(intent);
        finish();
    }
}