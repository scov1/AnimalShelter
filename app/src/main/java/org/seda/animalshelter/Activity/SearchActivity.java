package org.seda.animalshelter.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.seda.animalshelter.Adapters.WishAdapter;
import org.seda.animalshelter.Models.Animals;
import org.seda.animalshelter.Models.Wishlist;
import org.seda.animalshelter.R;
import org.seda.animalshelter.interfaces.WishlistInterface;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private TextView textView;
    private RecyclerView recyclerView;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.toolbar);
        //      logo = findViewById(R.id.imageView_app_bar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView = findViewById(R.id.searchView_activity_search);
        textView = findViewById(R.id.textView_activity_search_not_found);
        recyclerView = findViewById(R.id.recyclerView_activity_search_animals);

        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);


        final List<Wishlist> list = new ArrayList<>();
        final List<String> ids = new ArrayList<>();

        final Adapter adapter = new Adapter(list,false);
        adapter.setFromSearch(true);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {

                list.clear();
                ids.clear();


                final String[] tags = s.toLowerCase().split(" ");
                for(final String tag : tags){
                    tag.trim();
                    FirebaseFirestore.getInstance().collection("ANIMALS").whereArrayContains("tags",tag)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                    Wishlist model =  new Wishlist(
                                            documentSnapshot.getId(),
                                            documentSnapshot.get("animal_image_1").toString(),
                                            documentSnapshot.get("animal_title").toString(),
                                            documentSnapshot.get("animal_age").toString()
//                                            documentSnapshot.get("animal_gender_").toString(),
//                                            documentSnapshot.get("animal_desc_").toString()
                                    );

                                    model.setTags((ArrayList<String>) documentSnapshot.get("tags"));
                                    if(!ids.contains(model.getProductId())){
                                        list.add(model);
                                        ids.add(model.getProductId());
                                    }
                                }
                                if(tag.equals(tags[tags.length-1])){
                                    if(list.size()==0){
                                        textView.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }else{
                                        textView.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                    }
                                    adapter.getFilter().filter(s);
                                }
                            }else{
                                String error = task.getException().getMessage();
                                Toast.makeText(SearchActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    class Adapter extends WishAdapter implements Filterable{


        private List<Wishlist> originalList;

        public Adapter(List<Wishlist> wishlistModel, Boolean wishlist) {
            super(wishlistModel, wishlist);

            originalList = wishlistModel;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    FilterResults results = new FilterResults();
                    List<Wishlist> filteredList = new ArrayList<>();

                    final String[] tags = charSequence.toString().toLowerCase().split(" ");

                    for(Wishlist model: originalList){
                        ArrayList<String> presentTags = new ArrayList<>();
                        for(String tag : tags){
                            if(model.getTags().contains(tag)){
                                presentTags.add(tag);
                            }
                        }
                        model.setTags(presentTags);

                    }

                    for(int i = tags.length;i>0;i--){
                        for(Wishlist model : originalList){
                            if(model.getTags().size()==i){
                                filteredList.add(model);
                            }
                        }
                    }

                    results.values = filteredList;
                    results.count = filteredList.size();
                    ///filter logic
                    return results;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                    if(filterResults.count>0){
                        setWishlistModel((List<Wishlist>) filterResults.values);
                    }

                    notifyDataSetChanged();
                }
            };
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            Animatoo.animateSwipeLeft(SearchActivity.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}