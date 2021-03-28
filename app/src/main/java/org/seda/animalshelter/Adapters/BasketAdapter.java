package org.seda.animalshelter.Adapters;

import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.seda.animalshelter.Activity.DeliveryActivity;
import org.seda.animalshelter.Activity.MainMenuActivity;
import org.seda.animalshelter.Activity.PaymentActivity;
import org.seda.animalshelter.Activity.ServiceDetailsActivity;
import org.seda.animalshelter.DB.DBQuery;
import org.seda.animalshelter.Models.Basket;
import org.seda.animalshelter.R;

import java.util.List;

public class BasketAdapter extends RecyclerView.Adapter {

    private List<Basket> basketModelList;
    private int lastPosition = -1;
    private TextView basketTotalAmount;
    private boolean showDeleteBtn;
    public  TextView productQuantity;

    public BasketAdapter(List<Basket> basketModelList,TextView basketTotalAmount,boolean showDeleteBtn) {
        this.basketModelList = basketModelList;
        this.basketTotalAmount=basketTotalAmount;
        this.showDeleteBtn = showDeleteBtn;
    }

    @Override
    public int getItemViewType(int position) {
        switch (basketModelList.get(position).getType()){
            case 0:
               return Basket.BASKET;
            case 1:
                return Basket.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType){
            case Basket.BASKET:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_basket,parent,false);
                return  new BasketItemViewHolder(view);
            case Basket.TOTAL_AMOUNT:
                 View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_total_amount,parent,false);
                return  new BasketTotalAmountViewHolder(view1);
            default:
                return null;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (basketModelList.get(position).getType()){
            case Basket.BASKET:
                String productId = basketModelList.get(position).getProductId();
                String resource = basketModelList.get(position).getProductImage();
                String name = basketModelList.get(position).getProductName();
                String price = basketModelList.get(position).getProductPrice();
                Long productQuantity = basketModelList.get(position).getProductAmount();

                ((BasketItemViewHolder)holder).setItemDetails(productId,resource,name,price,position,String.valueOf(productQuantity));
                break;
            case Basket.TOTAL_AMOUNT:

                int totalItems = 0;
                int totalItemPrice = 0;
                String deliveryPrice;
                int totalAmount;
                int savedAmount=0;

                for (int i = 0;i<basketModelList.size();i++){
                    if(basketModelList.get(i).getType() == Basket.BASKET){
                        totalItems++;
                        totalItemPrice = totalItemPrice+Integer.parseInt(basketModelList.get(i).getProductPrice());
                    }
                }

                if(totalItemPrice>5){
                    deliveryPrice = "Бесплатно";
                    totalAmount=totalItemPrice;
                }else{
                    deliveryPrice = "Бесплатно";
                    totalAmount=totalItemPrice;
                }


                basketModelList.get(position).setTotalItems(totalItems);
                basketModelList.get(position).setTotalItemsPrice(totalItemPrice);
                basketModelList.get(position).setTotalAmount(totalAmount);


                //      String totalItemPrice = basketModelList.get(position).getTotalItemPrice();
          //      String deliveryPrice = basketModelList.get(position).getDeliveryPrice();
          //      String totalAmount = basketModelList.get(position).getTotalAmount();
          //      String savedAmount = basketModelList.get(position).getSaveAmount();

                ((BasketTotalAmountViewHolder)holder).setTotalAmount(totalItems,totalItemPrice,deliveryPrice,totalAmount);

                break;
            default:
                return;
        }

        if(lastPosition<position)
        {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition=position;
        }
    }

    @Override
    public int getItemCount() {
        return basketModelList.size();
    }

    class BasketItemViewHolder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private TextView productName;
        private TextView productPrice;

        private LinearLayout deleteBtn;


        public BasketItemViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.imageView_item_basket_img);
            productName = itemView.findViewById(R.id.textView_item_basket_name);
            productPrice = itemView.findViewById(R.id.textView_item_basket_price);
            productQuantity = itemView.findViewById(R.id.textView_item_basket_amount);
            deleteBtn = itemView.findViewById(R.id.linearLayout_item_basket_remove);

        }

        private void setItemDetails(final String productId, String resource, String name, String price, final int index,final String quantity){

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.home_hor_icon)).into(productImage);
            productName.setText(name);
            productPrice.setText(price + " KZT");
        //    productAmount.setText(amount);

            productQuantity.setText("Кол-во: " + quantity);

            productQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog amountDialog = new Dialog(itemView.getContext());
                    amountDialog.setContentView(R.layout.amount_dialog);
                    amountDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    amountDialog.setCancelable(false);
                    final EditText amountNo = amountDialog.findViewById(R.id.editTextNumber_amount_dialog_no);
                    Button amountOk = amountDialog.findViewById(R.id.button_amount_dialog_ok);
                    Button amountCancel = amountDialog.findViewById(R.id.button_amount_dialog_cancel);
                   // amountNo.setHint("");

                    amountCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            amountDialog.dismiss();
                        }
                    });

                    amountOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!TextUtils.isEmpty(amountNo.getText())) {
                                if (Long.valueOf(amountNo.getText().toString()) != 0) {
                                    if(itemView.getContext() instanceof MainMenuActivity) {

                                        DBQuery.basketListModel.get(index).setProductAmount(Long.valueOf(amountNo.getText().toString()));

                                    }else{
                                        if(PaymentActivity.fromCart) {
                                            DBQuery.basketListModel.get(index).setProductAmount(Long.valueOf(amountNo.getText().toString()));
                                        }
                                    }


//                                    else{
//                                        DeliveryActivity.basketModelList.get(index).setProductAmount(Long.valueOf(amountNo.getText().toString()));
//                                    }
                                    productQuantity.setText("Кол-во: " + amountNo.getText());
                              //      amountDialog.dismiss();
                                }
                            }
                            //    Toast.makeText(itemView.getContext(), "", Toast.LENGTH_SHORT).show();
                                amountDialog.dismiss();
                        }
                    });
                    amountDialog.show();
                }
            });
            if(showDeleteBtn){
                deleteBtn.setVisibility(View.VISIBLE);
            }else{
                deleteBtn.setVisibility(View.GONE);
            }
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!ServiceDetailsActivity.running_basket_query){
                        ServiceDetailsActivity.running_basket_query=true;
                        DBQuery.removeFragmentBasket(index,itemView.getContext(),basketTotalAmount);
                    }
                }
            });
        }
    }

    class BasketTotalAmountViewHolder extends RecyclerView.ViewHolder{

        private TextView totalItems;
        private TextView totalItemPrice;
        private TextView deliveryPrice;
        private TextView totalAmount;
        private TextView savedAmount;


        public BasketTotalAmountViewHolder(@NonNull View itemView) {
            super(itemView);


            totalItems = itemView.findViewById(R.id.textView_basket_total_amount_kolvo_price);
            totalItemPrice = itemView.findViewById(R.id.textView_basket_total_amount_price);
            deliveryPrice = itemView.findViewById(R.id.textView_basket_total_amount_delevery_free);
            totalAmount = itemView.findViewById(R.id.textView_basket_total_amount_total_prices);
           // savedAmount = itemView.findViewById(R.id.textView_basket_total_amount_price);

        }

        private void setTotalAmount(int totalItemsText,int totalItemPriceText,String deliveryPriceText,int totalAmountText){
            totalItems.setText("Цена(" + totalItemsText + ")");
            totalItemPrice.setText(totalItemPriceText + " KZT");
            if(deliveryPrice.equals("Бесплатно")){
                deliveryPrice.setText(deliveryPriceText);
            }else{
                deliveryPrice.setText(deliveryPriceText);
            }

            String temp = productQuantity.getText().toString().substring(8);
            Integer check = Integer.parseInt(temp);
            Integer res = check * totalItemPriceText;


            totalAmount.setText(res + " KZT");
            basketTotalAmount.setText(res + " KZT");

            LinearLayout parent = (LinearLayout) basketTotalAmount.getParent().getParent();

            if(totalItemPriceText==0){
                DBQuery.basketListModel.remove(DBQuery.basketListModel.size()-1);
                parent.setVisibility(View.GONE);
            }else{
                parent.setVisibility(View.VISIBLE);
            }
       //     savedAmount.setText(savedAmountText);
        }
    }
}
