package org.seda.animalshelter.Adapters;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.seda.animalshelter.Activity.OrderDetailsActivity;
import org.seda.animalshelter.Models.Order;
import org.seda.animalshelter.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Viewholder> {

    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);
        return new Viewholder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.Viewholder holder, int position) {
        int resource = orderList.get(position).getProductImage();
        String title = orderList.get(position).getProductTitle();
        String delivery = orderList.get(position).getProductDelivery();
        holder.setData(resource,title,delivery);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private TextView productTitle;
        private TextView productDelivery;
        private ImageView productDeliveryIndicator;


        public Viewholder(@NonNull final View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.imageView_order_item_img);
            productTitle = itemView.findViewById(R.id.textView_order_item_title);
            productDelivery = itemView.findViewById(R.id.textView_order_item_delevery_text);
            productDeliveryIndicator = itemView.findViewById(R.id.imageView_order_item_indicator);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), OrderDetailsActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setData(int resource, String title, String deliveryDate){
            productImage.setImageResource(resource);
            productTitle.setText(title);
            if(deliveryDate.equals("Отмена")){
                productDeliveryIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorRed)));

            }else{
                productDeliveryIndicator.setImageTintList( ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorSuccess)));

            }
            productDelivery.setText(deliveryDate);

        }
    }
}
