package org.seda.animalshelter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.seda.animalshelter.Adapters.CategoryAdapter;
import org.seda.animalshelter.Adapters.HomePageAdapter;
import org.seda.animalshelter.Models.Category;
import org.seda.animalshelter.Models.HomePage;
import org.seda.animalshelter.Models.HorizontalProduct;
import org.seda.animalshelter.R;

import java.util.ArrayList;
import java.util.List;

import static org.seda.animalshelter.DB.DBQuery.homePageList;
import static org.seda.animalshelter.DB.DBQuery.lists;
import static org.seda.animalshelter.DB.DBQuery.loadFragmentData;
import static org.seda.animalshelter.DB.DBQuery.loadedCategoriesItem;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;
    private HomePageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String title = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categoryRecyclerView = findViewById(R.id.recyclerView_activity_category);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(layoutManager);


        int listPosition = 0;
        for (int i = 0;i<loadedCategoriesItem.size();i++){
            if(loadedCategoriesItem.get(i).equals(title.toUpperCase())){
                listPosition=i;

            }
        }

        if(listPosition == 0){
            loadedCategoriesItem.add(title.toUpperCase());
            lists.add(new ArrayList<HomePage>());
            adapter = new HomePageAdapter(lists.get(loadedCategoriesItem.size()-1));
            loadFragmentData(adapter,this,loadedCategoriesItem.size()-1,title);
        }else{
            adapter = new HomePageAdapter(lists.get(listPosition));
        }


        categoryRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.item_search_main_menu){
            Intent intent = new Intent(CategoryActivity.this,SearchActivity.class);
            startActivity(intent);
            return true;
        }else if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}