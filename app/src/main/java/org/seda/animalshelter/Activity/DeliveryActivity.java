package org.seda.animalshelter.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.seda.animalshelter.Adapters.BasketAdapter;
import org.seda.animalshelter.DB.DBQuery;
import org.seda.animalshelter.Fragments.BasketFragment;
import org.seda.animalshelter.Models.Basket;
import org.seda.animalshelter.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DeliveryActivity extends AppCompatActivity {


    public static List<Basket> basketModelList;
    private View view;
    private RecyclerView deliveryRecyclerView;
    private Button btnAddAddress;
    private TextView totalAmount;
    public static Button  continueBtn;
    private Dialog loadingDialog;
    private FirebaseFirestore firebaseFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Доставка");


        deliveryRecyclerView = findViewById(R.id.recyclewView_activity_delivery);
        totalAmount = findViewById(R.id.textView_fragment_basket_price);
        continueBtn = findViewById(R.id.button_fragment_basket_continue);
   //     btnAddAddress = findViewById(R.id.button_shipping_details_layout_add_address);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerView.setLayoutManager(layoutManager);

        //malenkoe okoshko
        loadingDialog = new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        //malenkoe okoshko

        firebaseFirestore = FirebaseFirestore.getInstance();

        BasketAdapter basketAdapter = new BasketAdapter(basketModelList,totalAmount,false);
        deliveryRecyclerView.setAdapter(basketAdapter);
        basketAdapter.notifyDataSetChanged();

//        btnAddAddress.setVisibility(View.GONE);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadingDialog.show();

                Intent intent = new Intent(DeliveryActivity.this,PaymentActivity.class);
                startActivity(intent);
                Animatoo.animateSwipeLeft(DeliveryActivity.this);
                finish();
//                if (ContextCompat.checkSelfPermission(DeliveryActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(DeliveryActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
//                }
//                final String M_id = "YlutwW84809962366406";
//                final String customer_id = FirebaseAuth.getInstance().getUid();
//                final String order_id = UUID.randomUUID().toString().substring(0,28);
//                String url = "https://animalshelterkz.000webhostapp.com/paytm/sample.php";
//                final String callBack = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
//
//                RequestQueue requestQueue = Volley.newRequestQueue(DeliveryActivity.this);
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            if(jsonObject.has("CHECKSUMHASH")){
//                                String CHECKSUMHASH = jsonObject.getString("CHECKSUMHASH");
//
//                                PaytmPGService paytmPGService = PaytmPGService.getStagingService("");
//                                HashMap<String, String> paramMap = new HashMap<String,String>();
//                                paramMap.put( "MID" , M_id);
//                                paramMap.put( "ORDER_ID" , order_id);
//                                paramMap.put( "CUST_ID" , customer_id);
//                                //      paramMap.put( "MOBILE_NO" , "7777777777");
//                                //      paramMap.put( "EMAIL" , "username@emailprovider.com");
//                                paramMap.put( "CHANNEL_ID" , "WAP");
//                                paramMap.put( "TXN_AMOUNT" ,totalAmount.getText().toString().substring(0,totalAmount.getText().length()-4));
//                                paramMap.put( "WEBSITE" , "WEBSTAGING");
//                                paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
//                                paramMap.put( "CALLBACK_URL" , callBack);
//                                paramMap.put("CHECKSUMHASH",CHECKSUMHASH);
//
//                                PaytmOrder order = new PaytmOrder(paramMap);
//                                paytmPGService.initialize(order,null);
//                                paytmPGService.startPaymentTransaction(DeliveryActivity.this,true,true,new PaytmPaymentTransactionCallback(){
//
//                                    @Override
//                                    public void onTransactionResponse(Bundle inResponse) {
//                                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
//
//                                    }
//
//                                    @Override
//                                    public void networkNotAvailable() {
//                                        Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
//
//                                    }
//
//                                    @Override
//                                    public void clientAuthenticationFailed(String inErrorMessage) {
//                                        Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
//
//                                    }
//
//                                    @Override
//                                    public void someUIErrorOccurred(String inErrorMessage) {
//                                        Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage , Toast.LENGTH_LONG).show();
//
//                                    }
//
//                                    @Override
//                                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
//                                        Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
//
//                                    }
//
//                                    @Override
//                                    public void onBackPressedCancelTransaction() {
//                                        Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();
//
//                                    }
//
//                                    @Override
//                                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
//                                        Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();
//
//                                    }
//                                });
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        loadingDialog.dismiss();
//                        Toast.makeText(DeliveryActivity.this, "Что-то пошло не так :(", Toast.LENGTH_SHORT).show();
//                    }
//                }){
//                    @Nullable
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> paramMap = new HashMap<String,String>();
//                        paramMap.put( "MID" , M_id);
//                        paramMap.put( "ORDER_ID" , order_id);
//                        paramMap.put( "CUST_ID" , customer_id);
//                  //      paramMap.put( "MOBILE_NO" , "7777777777");
//                  //      paramMap.put( "EMAIL" , "username@emailprovider.com");
//                        paramMap.put( "CHANNEL_ID" , "WAP");
//                        paramMap.put( "TXN_AMOUNT" ,totalAmount.getText().toString().substring(0,totalAmount.getText().length()-4));
//                        paramMap.put( "WEBSITE" , "WEBSTAGING");
//                        paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
//
//                        return paramMap;
//                    }
//                };
//
//                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingDialog.dismiss();
    }
}