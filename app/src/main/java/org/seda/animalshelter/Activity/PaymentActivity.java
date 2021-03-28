package org.seda.animalshelter.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.braintreepayments.cardform.view.CardForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.seda.animalshelter.DB.DBQuery;
import org.seda.animalshelter.LoginRegistration.RegistrationActivity;
import org.seda.animalshelter.Models.Basket;
import org.seda.animalshelter.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PaymentActivity extends AppCompatActivity {


    CardForm cardForm;
    Button buy;
    public static List<Basket> basketModelList;
    private ConstraintLayout orderConfirmationLayout;
    private ImageButton continueShoppingBtn;
    private int random;
    private static TextView order_id;
    public static String orders_id;
    Random rand = new Random();
    int upperbound = 5000;
    private Dialog loadingDialog;
    private boolean successResponse = false;
    public static boolean fromCart;
    public static Toolbar toolbar;
    private ImageView logo;
    private Window window;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_layout);

        toolbar = findViewById(R.id.toolbar);
  //      logo = findViewById(R.id.imageView_app_bar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        orderConfirmationLayout = findViewById(R.id.order_confirmation_layout);
        continueShoppingBtn = findViewById(R.id.imageView_activity_delivery_shopping_btn);
        order_id = findViewById(R.id.textView_payment_layout_order_id);

        //malenkoe okoshko
        loadingDialog = new Dialog(PaymentActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        //malenkoe okoshko

      //  order_id = Integer.toString(random);

        cardForm = findViewById(R.id.card_form);
        buy = findViewById(R.id.btnBuy_fragment_account);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .setup(PaymentActivity.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        loadingDialog.dismiss();
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardForm.isValid()) {

            successResponse=true;

                            if(MainMenuActivity.mainMenuActivity!=null){
                                MainMenuActivity.mainMenuActivity.finish();
                                MainMenuActivity.mainMenuActivity=null;
                                MainMenuActivity.showBasket=false;
                            }
                            if(ServiceDetailsActivity.productDetailsActivity!=null){
                                ServiceDetailsActivity.productDetailsActivity.finish();
                                ServiceDetailsActivity.productDetailsActivity=null;
                            }

                            if(fromCart){

                                loadingDialog.show();
                                Map<String,Object> updateBasket = new HashMap<>();
                                long basketListSize = 0;
                                final List<Integer> indexList = new ArrayList<>();
                                for (int i =0;i< DBQuery.basketlist.size();i++){

//                                    updateBasket.put("product_id_" + basketListSize,basketModelList.get(i).getProductId());
//                                    basketListSize++;
                                    indexList.add(i);
                                }
                                updateBasket.put("list_size",basketListSize);

                                FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                                        .document("MY_BASKET")
                                        .set(updateBasket).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){

                                            for (int i =0;i< indexList.size();i++){
                                                DBQuery.basketlist.remove(indexList.get(i).intValue());
                                                DBQuery.basketListModel.remove(indexList.get(i).intValue());
                                                DBQuery.basketListModel.remove(DBQuery.basketListModel.size()-1);

                                            }
                                        }else{
                                            String error = task.getException().getMessage();
                                            Toast.makeText(PaymentActivity.this,error, Toast.LENGTH_SHORT).show();
                                        }
                                        loadingDialog.dismiss();
                                    }
                                });
                            }
           //         DeliveryActivity.continueBtn.setEnabled(false);
//                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    random = rand.nextInt(upperbound);
                    orders_id = Integer.toString(random);
                    order_id.setText("Номер заказа: " + orders_id);

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismiss();
                            orderConfirmationLayout.setVisibility(View.VISIBLE);
                        }
                    }, 3000);

                            continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(PaymentActivity.this,MainMenuActivity.class);
                                    startActivity(intent);
                                    Animatoo.animateSwipeLeft(PaymentActivity.this);

                                    finish();
                                }
                            });

                } else {
                    Toast.makeText(PaymentActivity.this, "Пожалуйста заполните поля", Toast.LENGTH_LONG).show();
                }

            }
        });
        loadingDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        if(successResponse){
            finish();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            Animatoo.animateSwipeLeft(PaymentActivity.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

