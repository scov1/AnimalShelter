package org.seda.animalshelter.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.seda.animalshelter.Models.Wishlist;
import org.seda.animalshelter.R;
import org.seda.animalshelter.interfaces.WishlistInterface;
import org.seda.animalshelter.viewholders.WishViewHolder;

import java.util.List;

public class WishAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static boolean fromSearch;
    private List<Wishlist> wishlistModel;
    private Boolean wishlist;
    private WishlistInterface wishlistInterface;
    private Context context;

    public boolean isFromSearch() {
        return fromSearch;
    }

    public void setFromSearch(boolean fromSearch) {
        this.fromSearch = fromSearch;
    }

    public WishAdapter(Context context, List<Wishlist> wishlistModel, Boolean wishlist, WishlistInterface wishlistInterface) {
        this.wishlistModel = wishlistModel;
        this.wishlist = wishlist;
        this.wishlistInterface = wishlistInterface;
        this.context = context;
    }

    public WishAdapter(List<Wishlist> wishlistModel, Boolean wishlist) {
        this.wishlistModel = wishlistModel;
        this.wishlist = wishlist;
    }

    public List<Wishlist> getWishlistModel() {
        return wishlistModel;
    }

    public void setWishlistModel(List<Wishlist> wishlistModel) {
        this.wishlistModel = wishlistModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(
                R.layout.wishlist_item,
                parent,
                false);

        return new WishViewHolder(view, wishlistInterface);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((WishViewHolder)holder).bind(wishlistModel.get(position), wishlist);
    }

    @Override
    public int getItemCount() {
        return wishlistModel.size();
    }
}
