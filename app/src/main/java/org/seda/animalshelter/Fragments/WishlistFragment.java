package org.seda.animalshelter.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.seda.animalshelter.Activity.ServiceDetailsActivity;
import org.seda.animalshelter.Adapters.WishAdapter;
import org.seda.animalshelter.DB.DBQuery;
import org.seda.animalshelter.R;
import org.seda.animalshelter.interfaces.WishlistInterface;


public class WishlistFragment extends Fragment implements WishlistInterface {

    View view;
    private RecyclerView wishlistRecyclerView;
    private Dialog loadingDialog;
    public static WishAdapter wishlistAdapter;

    public WishlistFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        initializeView();
        return view;
    }

    public void initializeView() {

        //malenkoe okoshko
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        //  loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.cats_banner));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        //malenkoe okoshko

        wishlistRecyclerView = view.findViewById(R.id.recyclerView_fragment_wishlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        wishlistRecyclerView.setLayoutManager(linearLayoutManager);

        if (DBQuery.wishListModel.size() == 0) {
            DBQuery.wishlist.clear();
            DBQuery.loadWishlist(getContext(), loadingDialog, true);
        } else {
            loadingDialog.dismiss();
        }

        wishlistAdapter = new WishAdapter(requireContext(), DBQuery.wishListModel, true, this);
        wishlistRecyclerView.setAdapter(wishlistAdapter);
        wishlistAdapter.notifyDataSetChanged();
    }

    @Override
    public void initiateServiceClicked(View view, int position) {
        switch (view.getId()){
            case R.id.imageView_wishlist_item_delete: {
                if(!ServiceDetailsActivity.running_wishlist_query) {
                    ServiceDetailsActivity.running_wishlist_query = true;
                    DBQuery.removeFragmentWishlist(position, requireContext());
                }
            }
            default: {
                Intent productDetailsIntent = new Intent(requireContext(), ServiceDetailsActivity.class);
                productDetailsIntent.putExtra("product_id", DBQuery.wishListModel.get(position).getProductId());
                startActivity(productDetailsIntent);
            }
        }
    }
}