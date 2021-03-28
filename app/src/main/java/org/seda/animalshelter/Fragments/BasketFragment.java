package org.seda.animalshelter.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import org.seda.animalshelter.Activity.DeliveryActivity;
import org.seda.animalshelter.Activity.PaymentActivity;
import org.seda.animalshelter.Adapters.BasketAdapter;
import org.seda.animalshelter.DB.DBQuery;
import org.seda.animalshelter.Models.Basket;
import org.seda.animalshelter.R;

import java.util.ArrayList;

import static org.seda.animalshelter.DB.DBQuery.basketListModel;


public class BasketFragment extends Fragment{

    private View view;
    private RecyclerView basketItemRecyclerView;
    private Button btnContinue;
    public static BasketAdapter basketAdapter;
    private Dialog loadingDialog;
    private TextView totalAmount;


    public BasketFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_basket, container, false);
        initializeView();

        return view;
    }

    public void initializeView() {

        //malenkoe okoshko
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        //  loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.cats_banner));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //loadingDialog.show();

        //malenkoe okoshko

        basketItemRecyclerView = view.findViewById(R.id.recyclerView_fragment_basket_cart_items);
        btnContinue = view.findViewById(R.id.button_fragment_basket_continue);
        totalAmount = view.findViewById(R.id.textView_fragment_basket_price);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        basketItemRecyclerView.setLayoutManager(layoutManager);




        basketAdapter = new BasketAdapter(basketListModel,totalAmount,true);
        basketItemRecyclerView.setAdapter(basketAdapter);
        basketAdapter.notifyDataSetChanged();

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DeliveryActivity.basketModelList = new ArrayList<>();
                PaymentActivity.fromCart = true;

                for(int i=0;i< DBQuery.basketListModel.size();i++){

                    Basket basket = DBQuery.basketListModel.get(i);
                    DeliveryActivity.basketModelList.add(basket);
                }

        //        DeliveryActivity.basketModelList.add(new Basket(Basket.TOTAL_AMOUNT));

            //    loadingDialog.show();


                Intent intent = new Intent(getContext(), DeliveryActivity.class);
                startActivity(intent);
                Animatoo.animateSwipeLeft(getContext());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        basketAdapter.notifyDataSetChanged();

        if(basketListModel.size()==0){
            DBQuery.basketlist.clear();
            DBQuery.loadBasketList(getContext(),loadingDialog,true,new TextView(getContext()),totalAmount);
        }
        else{
            if(DBQuery.basketListModel.get(DBQuery.basketListModel.size()-1).getType() == Basket.TOTAL_AMOUNT){
                LinearLayout parent = (LinearLayout) totalAmount.getParent().getParent();
                parent.setVisibility(View.VISIBLE);


            }
            loadingDialog.dismiss();
        }
    }
}