package org.seda.animalshelter.Adapters;

import android.content.Intent;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.seda.animalshelter.Activity.AnimalsDetailsActivity;
import org.seda.animalshelter.Activity.MainActivity;
import org.seda.animalshelter.Activity.ServiceDetailsActivity;
import org.seda.animalshelter.Activity.TakeHomePetActivity;
import org.seda.animalshelter.Activity.ViewAllActivity;
import org.seda.animalshelter.Models.Animals;
import org.seda.animalshelter.Models.HomePage;
import org.seda.animalshelter.Models.HorizontalProduct;
import org.seda.animalshelter.Models.Wishlist;
import org.seda.animalshelter.R;
import org.w3c.dom.Text;

import java.util.List;

public class HomePageAdapter extends RecyclerView.Adapter {

    private List<HomePage> homePageList;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public HomePageAdapter(List<HomePage> homePageList) {

        this.homePageList = homePageList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageList.get(position).getType()){
            case 0:
                return HomePage.BANNER;
            case 1:
                return HomePage.HORIZONTAL_PRODUCT;
            case 2:
                return HomePage.GRID_ANIMALS;
            case 3:
                return HomePage.ADOPT_PET;
            default:
                return -1;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case HomePage.BANNER:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_home,parent,false);
                return new BannerViewHolder(view);
            case HomePage.HORIZONTAL_PRODUCT:
                View horizontalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_services,parent,false);
                return new HorizontalProductViewHolder(horizontalView);
            case HomePage.GRID_ANIMALS:
                View gridAnimalsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_animals,parent,false);
                return new GridProductViewHolder(gridAnimalsView);
            case HomePage.ADOPT_PET:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_take_home_pet,parent,false);
                return new AdoptViewHolder(view1);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (homePageList.get(position).getType()){
            case HomePage.BANNER:
                String resource = homePageList.get(position).getResource();
                ((BannerViewHolder)holder).setBanner(resource);
                break;
            case HomePage.HORIZONTAL_PRODUCT:

                String horizontalTitle = homePageList.get(position).getProductName();
                List<Wishlist> viewAllProducts = homePageList.get(position).getViewAllProductList();
                List<HorizontalProduct> horizontalProducts = homePageList.get(position).getProductList();
                ((HorizontalProductViewHolder)holder).setHorizontalProductRecyclerView(horizontalProducts,horizontalTitle,viewAllProducts);
                break;
            case HomePage.GRID_ANIMALS:
                String gridTitle = homePageList.get(position).getProductName();
                List<Animals> gridAnimals = homePageList.get(position).getAnimalsList();
                ((GridProductViewHolder)holder).setGridAnimals(gridAnimals,gridTitle);
                break;
            case HomePage.ADOPT_PET:
                String resourceAdopt = homePageList.get(position).getResource();
                ((AdoptViewHolder)holder).setAdopt(resourceAdopt);
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {

        return homePageList.size();
    }


    public class BannerViewHolder extends RecyclerView.ViewHolder{

        private ImageView banner;
        private ConstraintLayout bannerLayout;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);

            banner = itemView.findViewById(R.id.imageView_cat_banner_home);
            bannerLayout = itemView.findViewById(R.id.constraintLayout_banner_home);
        }
        private void setBanner(String resource)
        {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.home_hor_icon)).into(banner);

        }

    }

    public class AdoptViewHolder extends RecyclerView.ViewHolder{

        private ImageView adoptImgHome;
        private TextView adoptTitle;
        private TextView adoptBlock;
        private Button adoptBtn;
        private ConstraintLayout adoptLayout;

        public AdoptViewHolder(@NonNull View itemView) {
            super(itemView);

            adoptLayout = itemView.findViewById(R.id.constraintLayout_main_take_home_pet);
            adoptTitle = itemView.findViewById(R.id.textView_main_take_home_pet_title);
            adoptBlock = itemView.findViewById(R.id.textView_main_take_home_pet_block);
            adoptBtn = itemView.findViewById(R.id.button_main_take_home_pet_btn);
            adoptImgHome = itemView.findViewById(R.id.imageView_main_take_home_pet_homeImage);

        }
        private void setAdopt(String resource)
        {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.home_hor_icon)).into(adoptImgHome);
//            adoptTitle.setText(title);
//            adoptBlock.setText(block);

            adoptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), TakeHomePetActivity.class);
                    itemView.getContext().startActivity(intent);
                    Animatoo.animateSwipeLeft(itemView.getContext());
                }
            });



        }

    }

    public class HorizontalProductViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout constraintLayout;
        private TextView productTitle;
        private TextView productTitle2;
        private Button productAllBtn;
        private RecyclerView horizontalProductRecyclerView;


        public HorizontalProductViewHolder(@NonNull final View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.constraintLayout_horizontal_services);
            productTitle = itemView.findViewById(R.id.textView_horizontal_services_title);
            productAllBtn = itemView.findViewById(R.id.button_horizontal_services_all_service);
            horizontalProductRecyclerView = itemView.findViewById(R.id.recyclerView_horizontal_services_items);
            horizontalProductRecyclerView.setRecycledViewPool(recycledViewPool);



        }

        private void setHorizontalProductRecyclerView(List<HorizontalProduct> horizontalProducts, final String title, final List<Wishlist> viewAllProductList){

            productTitle.setText(title);

            if(horizontalProducts.size()>3){
                productAllBtn.setVisibility(View.VISIBLE);
                productAllBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewAllActivity.wishList = viewAllProductList;
                        Intent intent = new Intent(itemView.getContext(), ViewAllActivity.class);
                        intent.putExtra("layout_code",0);
                        intent.putExtra("title",title);
                        itemView.getContext().startActivity(intent);
                        Animatoo.animateSwipeLeft(itemView.getContext());

                    }
                });
            }else{
                productAllBtn.setVisibility(View.INVISIBLE);
            }

            HorizontalServicesAdapter horizontalServicesAdapter = new HorizontalServicesAdapter(horizontalProducts);
            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizontalProductRecyclerView.setLayoutManager(linearLayoutManager1);
            horizontalProductRecyclerView.setAdapter(horizontalServicesAdapter);
            horizontalServicesAdapter.notifyDataSetChanged();
        }
    }

    public class GridProductViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout constraintLayout;
        private TextView gridAnimalName;
        private Button gridViewAllAnimals;
      //  private GridView gridView;
        private GridLayout gridProductLayout;

        public GridProductViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.constraintLayout_grid_animals);
            gridAnimalName = itemView.findViewById(R.id.textView_grid_animals_title);
            gridViewAllAnimals = itemView.findViewById(R.id.button_grid_animals_all_animals);
          //   gridView = itemView.findViewById(R.id.gridLayout_grid_animals);
            gridProductLayout = itemView.findViewById(R.id.gridLayout_grid_animals);
        }

        private void setGridAnimals(final List<Animals> animalsList, final String title){

            gridAnimalName.setText(title);


            for (int i = 0;i<4;i++){
                ImageView animalImage = gridProductLayout.getChildAt(i).findViewById(R.id.imageView_animal_item_image_animal);
                TextView animalName = gridProductLayout.getChildAt(i).findViewById(R.id.textView_animal_item_name_animal);
                TextView animalAge = gridProductLayout.getChildAt(i).findViewById(R.id.textView_animal_item_age_animal);
                TextView animalGender = gridProductLayout.getChildAt(i).findViewById(R.id.textView_animal_item_gender_animal);

                Glide.with(itemView.getContext()).load(animalsList.get(i).getAnimalImage()).apply(new RequestOptions().placeholder(R.drawable.home_hor_icon)).into(animalImage);
                animalName.setText(animalsList.get(i).getName());
                animalAge.setText(animalsList.get(i).getAge());
                animalGender.setText(animalsList.get(i).getGender());


                if(!title.equals("")) {
                    final int finalI = i;
                    gridProductLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent productDetailsIntent = new Intent(itemView.getContext(), AnimalsDetailsActivity.class);
                            productDetailsIntent.putExtra("animal_id",animalsList.get(finalI).getAnimalId());
                            itemView.getContext().startActivity(productDetailsIntent);
                            Animatoo.animateSwipeLeft(itemView.getContext());

                        }
                    });
                }
            }

           // gridView.setAdapter(new GridListAnimalsAdapter(animalsList));
            if(animalsList.size()>4){
                gridViewAllAnimals.setVisibility(View.VISIBLE);
                gridViewAllAnimals.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewAllActivity.animalsList = animalsList;
                        Intent intent = new Intent(itemView.getContext(), ViewAllActivity.class);
                        intent.putExtra("layout_code",1);
                        intent.putExtra("title",title);
                        itemView.getContext().startActivity(intent);
                        Animatoo.animateSwipeLeft(itemView.getContext());
                    }
                });
            }else{
                gridViewAllAnimals.setVisibility(View.INVISIBLE);

            }
        }
    }
}
