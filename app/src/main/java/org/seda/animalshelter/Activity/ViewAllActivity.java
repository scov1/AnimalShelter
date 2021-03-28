package org.seda.animalshelter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.seda.animalshelter.Activity.ServiceDetailsActivity;
import org.seda.animalshelter.Adapters.GridListAnimalsAdapter;
import org.seda.animalshelter.Adapters.WishAdapter;
import org.seda.animalshelter.DB.DBQuery;
import org.seda.animalshelter.Models.Animals;
import org.seda.animalshelter.Models.HorizontalProduct;
import org.seda.animalshelter.Models.Wishlist;
import org.seda.animalshelter.R;
import org.seda.animalshelter.interfaces.WishlistInterface;

import java.util.List;

public class ViewAllActivity extends AppCompatActivity implements WishlistInterface {

    private Toolbar toolbar;

    private RecyclerView recyclerView;

    private GridView gridView;

    public static List<Animals> animalsList;

    public static List<Wishlist> wishList;

    public static List<HorizontalProduct> horizontalProductsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        initializeViews();
        initializeToolbar();
        initiateChecklayoutType();
    }

    public void initiateChecklayoutType(){
        switch (getIntent().getIntExtra("layout_code",-1)){
            case 0: {
                initiateDisplayRecyclerView();
                initializeAnimalServiceLayoutManager();
                initializeAnimalServiceAdapter();
                break;
            }
            case 1: {
                initiateDisplayGridView();
                initializeGridViewAdapter();
                break;
            }
        }
    }

    public void initializeToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
    }

    public void initializeViews(){
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView_activity_view_all);
        gridView = findViewById(R.id.gridView_activity_view_all);
    }

    public void initializeAnimalServiceAdapter(){
        WishAdapter wishlistAdapter = new WishAdapter(this,
                wishList, false, this);
        recyclerView.setAdapter(wishlistAdapter);
    }

    public void initializeAnimalServiceLayoutManager(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void initiateDisplayRecyclerView(){
        gridView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void initiateDisplayGridView(){
        recyclerView.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
    }

    public void initializeGridViewAdapter(){
        GridListAnimalsAdapter gridListAnimalsAdapter = new GridListAnimalsAdapter(animalsList);
        gridView.setAdapter(gridListAnimalsAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initiateServiceClicked(View view, int position) {
        switch (view.getId()){
            case R.id.imageView_wishlist_item_delete: {
                if(!ServiceDetailsActivity.running_wishlist_query) {
                    ServiceDetailsActivity.running_wishlist_query = true;
                    DBQuery.removeFragmentWishlist(position, this);
                }
            }
            default: {
                Intent productDetailsIntent = new Intent(this, ServiceDetailsActivity.class);
                productDetailsIntent.putExtra("product_id", wishList.get(position).getProductId());
                startActivity(productDetailsIntent);
            }
        }
    }
}