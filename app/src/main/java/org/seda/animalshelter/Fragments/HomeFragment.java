package org.seda.animalshelter.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.seda.animalshelter.Activity.MainMenuActivity;
import org.seda.animalshelter.Adapters.CategoryAdapter;
import org.seda.animalshelter.Adapters.GridListAnimalsAdapter;
import org.seda.animalshelter.Adapters.HomePageAdapter;
import org.seda.animalshelter.Adapters.HorizontalServicesAdapter;
import org.seda.animalshelter.Models.Animals;
import org.seda.animalshelter.Models.Category;
import org.seda.animalshelter.Models.HomePage;
import org.seda.animalshelter.Models.HorizontalProduct;
import org.seda.animalshelter.R;

import java.util.ArrayList;
import java.util.List;

import static org.seda.animalshelter.DB.DBQuery.categoryList;
import static org.seda.animalshelter.DB.DBQuery.firebaseFirestore;
import static org.seda.animalshelter.DB.DBQuery.homePageList;
import static org.seda.animalshelter.DB.DBQuery.lists;
import static org.seda.animalshelter.DB.DBQuery.loadCategory;
import static org.seda.animalshelter.DB.DBQuery.loadFragmentData;
import static org.seda.animalshelter.DB.DBQuery.loadedCategoriesItem;


public class HomeFragment extends Fragment {

    View view;
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private RecyclerView homePageRecyclerView;
    private HomePageAdapter adapter;
    private ImageView noInternet;

 //   private FirebaseFirestore firebaseFirestore;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        noInternet = view.findViewById(R.id.imageView_content_main_no_internet);

        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()==true){
            MainMenuActivity.drawer.setDrawerLockMode(0);
//            categoryRecyclerView.setVisibility(View.VISIBLE);
   //         homePageRecyclerView.setVisibility(View.VISIBLE);
            noInternet.setVisibility(View.GONE);
            init();
        }else{
            MainMenuActivity.drawer.setDrawerLockMode(1);
//            categoryRecyclerView.setVisibility(View.GONE);
  //          homePageRecyclerView.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Нет подключения к интернету!", Toast.LENGTH_SHORT).show();
            Glide.with(this).load(R.drawable.no_internet).into(noInternet);
            noInternet.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void init(){

        ///Category

        categoryRecyclerView = view.findViewById(R.id.recyclerView_category_fragment_home);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(linearLayoutManager);



        categoryAdapter = new CategoryAdapter(categoryList);
        categoryRecyclerView.setAdapter(categoryAdapter);

        if(categoryList.size() == 0){
            loadCategory(categoryAdapter,getContext());
        }else{
            categoryAdapter.notifyDataSetChanged();
        }

        ///Category
//
//        List<Animals> animalsList = new ArrayList<>();
//        animalsList.add(new Animals(R.drawable.cat2, "Кета", "1 год", "Девочка"));
//        animalsList.add(new Animals(R.drawable.cat2, "Кета", "2 год", "Девочка"));
//        animalsList.add(new Animals(R.drawable.cat2, "Кета", "3 год", "Девочка"));
//        animalsList.add(new Animals(R.drawable.cat2, "Кета", "1 год", "Девочка"));
//        animalsList.add(new Animals(R.drawable.cat2, "Кета", "1 год", "Девочка"));
//        animalsList.add(new Animals(R.drawable.cat2, "Кета", "1 год", "Девочка"));
//        animalsList.add(new Animals(R.drawable.cat2, "Кета", "1 год", "Девочка"));
//        animalsList.add(new Animals(R.drawable.cat2, "Кета", "1 год", "Девочка"));
//        animalsList.add(new Animals(R.drawable.cat2, "Кета", "1 год", "Девочка"));
//        animalsList.add(new Animals(R.drawable.cat2, "Кета", "1 год", "Девочка"));
//        animalsList.add(new Animals(R.drawable.cat2, "Кета", "1 год", "Девочка"));


//        List<HorizontalProduct> horizontalProducts = new ArrayList<>();
//        horizontalProducts.add(new HorizontalProduct(R.drawable.food_icon,"Кормить","2500","Туалет"));
//        horizontalProducts.add(new HorizontalProduct(R.drawable.uhod_icon,"Ухаживать","2500","Туалет"));
//        horizontalProducts.add(new HorizontalProduct(R.drawable.vetirinar_icon,"Лечить","2500","Туалет"));
//        horizontalProducts.add(new HorizontalProduct(R.drawable.kostochka_icon,"Лакомства,Игрушки","2500","Туалет"));
//        horizontalProducts.add(new HorizontalProduct(R.drawable.home_hor_icon,"Забрать домой","2500","Туалет"));
//        horizontalProducts.add(new HorizontalProduct(R.drawable.home_hor_icon,"Забрать домой","2500","Туалет"));
//        horizontalProducts.add(new HorizontalProduct(R.drawable.home_hor_icon,"Забрать домой","2500","Туалет"));
//        horizontalProducts.add(new HorizontalProduct(R.drawable.home_hor_icon,"Забрать домой","2500","Туалет"));
//        horizontalProducts.add(new HorizontalProduct(R.drawable.home_hor_icon,"Забрать домой","2500","Туалет"));
//        horizontalProducts.add(new HorizontalProduct(R.drawable.home_hor_icon,"Забрать домой","2500","Туалет"));
//

        homePageRecyclerView = view.findViewById(R.id.recyclerView_fragment_home);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homePageRecyclerView.setLayoutManager(layoutManager);



        if(lists.size() == 0){
            loadedCategoriesItem.add("Главная");
            lists.add(new ArrayList<HomePage>());
            adapter = new HomePageAdapter(lists.get(0));
            loadFragmentData(adapter,getContext(),0,"Home");
        }else{
            adapter = new HomePageAdapter(lists.get(0));
            adapter.notifyDataSetChanged();
        }

        homePageRecyclerView.setAdapter(adapter);
    }
}