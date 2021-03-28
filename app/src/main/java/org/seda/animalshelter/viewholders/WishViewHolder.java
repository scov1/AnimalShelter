package org.seda.animalshelter.viewholders;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.seda.animalshelter.Activity.AnimalsDetailsActivity;
import org.seda.animalshelter.Adapters.WishAdapter;
import org.seda.animalshelter.Models.Wishlist;
import org.seda.animalshelter.R;
import org.seda.animalshelter.interfaces.WishlistInterface;

public class WishViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private ImageView productImage;
    private TextView productTitle;
    private TextView productPrice;
    private ImageView productDelete;
    private ConstraintLayout constraintLayout;
    private WishlistInterface wishlistInterface;

    public WishViewHolder(@NonNull final View itemView, WishlistInterface wishlistInterface) {
        super(itemView);
        initializeViews();
        initialzieInterface(wishlistInterface);
        initializeListeners();

    }

    public void initialzieInterface(WishlistInterface wishlistInterface){
        this.wishlistInterface = wishlistInterface;
    }

    public void initializeViews(){
        productImage = itemView.findViewById(R.id.imageView_wishlist_item_image);
        productTitle = itemView.findViewById(R.id.textView_wishlist_item_title);
        productPrice = itemView.findViewById(R.id.textView_wishlist_item_price);
        productDelete = itemView.findViewById(R.id.imageView_wishlist_item_delete);
        constraintLayout = itemView.findViewById(R.id.constraintLayout_wishlist_item);
    }

    public void initializeListeners(){
        constraintLayout.setOnClickListener(this);
        productDelete.setOnClickListener(this);
    }

    public void bind(Wishlist wish, Boolean wishlist){
        Glide.with(itemView.getContext()).load(wish.getProductImage()).apply(new RequestOptions().placeholder(R.drawable.home_hor_icon)).into(productImage);
        productTitle.setText(wish.getProductTitle());
        productPrice.setText(wish.getProductPrice());
        if(wishlist) productDelete.setVisibility(View.VISIBLE);
        else productDelete.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if(WishAdapter.fromSearch){
            AnimalsDetailsActivity.fromSearch= true;

        }
       wishlistInterface.initiateServiceClicked(view, getAdapterPosition());
    }
}
