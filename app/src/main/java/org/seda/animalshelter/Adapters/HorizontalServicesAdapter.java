package org.seda.animalshelter.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.seda.animalshelter.Activity.ServiceDetailsActivity;
import org.seda.animalshelter.Models.HorizontalProduct;
import org.seda.animalshelter.R;

import java.util.List;

public class HorizontalServicesAdapter extends RecyclerView.Adapter<HorizontalServicesAdapter.ViewHolder> {

    private List<HorizontalProduct> productList;

    public HorizontalServicesAdapter(List<HorizontalProduct> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public HorizontalServicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item_service,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalServicesAdapter.ViewHolder holder, int position) {
        String resource = productList.get(position).getProductImage();
        String name = productList.get(position).getProductName();
        String price = productList.get(position).getProductPrice();
        String productId = productList.get(position).getProductId();

        holder.setData(productId,resource,name,price);
     //   holder.setProductName(name);
   //     holder.setProductPrice(price);
    }

    @Override
    public int getItemCount() {
        if(productList.size()>8){
            return 8;
        }else{
            return productList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productName;
        private TextView productPrice;
        private TextView productDescription;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.imageView_food_horizontal_item_services);
            productName = itemView.findViewById(R.id.textView_food_horizontal_item_services);
      //      productPrice = itemView.findViewById(R.id.textView_price_service_images_layout);


        }

        private void setData(final String productId, String resource, String name, String price){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.home_hor_icon)).into(productImage);
            productName.setText(name);
      //      productPrice.setText(price + "KZT");

            if(!name.equals("")) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(itemView.getContext(), ServiceDetailsActivity.class);
                        intent.putExtra("product_id",productId);
                        itemView.getContext().startActivity(intent);
                        Animatoo.animateSwipeLeft(itemView.getContext());
                    }
                });
            }
        }

//        private void setProductImage(String resource){
//            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.home_hor_icon)).into(productImage);
//        }
//
//        private void setProductName(String name){
//            productName.setText(name);
//        }
//
//        private void setProductPrice(String price){
//            productName.setText(price + "KZT");
//        }
    }
}
