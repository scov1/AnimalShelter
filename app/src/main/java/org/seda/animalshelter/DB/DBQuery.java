package org.seda.animalshelter.DB;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.seda.animalshelter.Activity.ServiceDetailsActivity;
import org.seda.animalshelter.Adapters.CategoryAdapter;
import org.seda.animalshelter.Adapters.HomePageAdapter;
import org.seda.animalshelter.Fragments.BasketFragment;
import org.seda.animalshelter.Fragments.WishlistFragment;
import org.seda.animalshelter.Models.Animals;
import org.seda.animalshelter.Models.Basket;
import org.seda.animalshelter.Models.Category;
import org.seda.animalshelter.Models.HomePage;
import org.seda.animalshelter.Models.HorizontalProduct;
import org.seda.animalshelter.Models.Wishlist;
import org.seda.animalshelter.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.seda.animalshelter.Activity.ServiceDetailsActivity.productId;

public class DBQuery {

 //   public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
 //   public static FirebaseUser firebaseCurrentUser = firebaseAuth.getCurrentUser();

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static String email,fullname,profile;
    public static List<Category> categoryList = new ArrayList<>();
    public static List<HomePage> homePageList = new ArrayList<>();
    public static List<List<HomePage>> lists = new ArrayList<>();
    public static List<String> loadedCategoriesItem = new ArrayList<>();
    public static List<String> wishlist = new ArrayList<>();
    public static List<Wishlist> wishListModel = new ArrayList<>();
    public static List<String> basketlist = new ArrayList<>();
    public static List<Basket> basketListModel = new ArrayList<>();

    public static void loadCategory(final CategoryAdapter categoryAdapter, final Context context){
        categoryList.clear();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                categoryList.add(new Category(documentSnapshot.get("icon").toString(),documentSnapshot.get("categoryName").toString()));
                            }
                            categoryAdapter.notifyDataSetChanged();
                        }else{
                            String error = task.getException().getMessage();
                            Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public static void loadFragmentData(final HomePageAdapter adapter, final Context context,final int index,String categoryItem){
        firebaseFirestore.collection("CATEGORIES")
                .document(categoryItem.toUpperCase())
                .collection("HOME_PAGE").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){

                                if((long)documentSnapshot.get("view_type") == 0 ){

                                    lists.get(index).add(new HomePage(0,documentSnapshot.get("banner").toString()));

                                }else if((long)documentSnapshot.get("view_type") ==2){
                                    List<HorizontalProduct> horizontalProducts = new ArrayList<>();
                                    List<Wishlist> viewAllproduct = new ArrayList<>();
                                    long no_of_products = (long)documentSnapshot.get("no_of_products");
                                    for (long i =2;i<=no_of_products;i++){
                                        horizontalProducts.add(new HorizontalProduct(
                                                documentSnapshot.get("product_id_" + i).toString(),
                                                documentSnapshot.get("product_image_" + i).toString(),
                                                documentSnapshot.get("product_title_" + i).toString(),
                                                documentSnapshot.get("product_price_" + i).toString(),
                                                documentSnapshot.get("product_desc_" + i).toString()

                                        ));
                                        viewAllproduct.add(new Wishlist(
                                                documentSnapshot.get("product_id_" + i).toString(),
                                                documentSnapshot.get("product_image_" + i).toString(),
                                                documentSnapshot.get("product_title_" + i).toString(),
                                                documentSnapshot.get("product_price_" + i).toString()

                                        ));
                                    }
                                    lists.get(index).add(new HomePage(1,documentSnapshot.get("layout_title").toString(),horizontalProducts,viewAllproduct));
                                }else if((long)documentSnapshot.get("view_type") ==1){
                                    List<Animals> animalsList = new ArrayList<>();
                                    long no_of_animals = (long)documentSnapshot.get("no_of_animals");
                                    for (long i =1;i<no_of_animals;i++){
                                        animalsList.add(new Animals(
                                                documentSnapshot.get("animal_id_" + i).toString(),
                                                documentSnapshot.get("animal_image_" + i).toString(),
                                                documentSnapshot.get("animal_title_" + i).toString(),
                                                documentSnapshot.get("animal_age_" + i).toString(),
                                                documentSnapshot.get("animal_gender_" + i).toString(),
                                                documentSnapshot.get("animal_desc_" + i).toString()
                                        ));
                                    }
                                    lists.get(index).add(new HomePage(2,documentSnapshot.get("layout_title").toString(),"Поддержите их",animalsList));
                                }
                                else if((long)documentSnapshot.get("view_type") == 3){

                                    lists.get(index).add(new HomePage(3,documentSnapshot.get("adopt_title").toString()));


                                    //  lists.get(index).add(new HomePage(2,documentSnapshot.get("layout_title").toString(),"Поддержите их",animalsList));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }else{
                            String error = task.getException().getMessage();
                            Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void loadWishlist(final Context context, final Dialog dialog,final boolean loadProductData){
        wishlist.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                .document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    for(int i=0;i<(long)task.getResult().get("list_size");i++){
                        wishlist.add(task.getResult().get("product_id_" + i).toString());

                        if(DBQuery.wishlist.contains(productId)){
                            ServiceDetailsActivity.ADD_TO_FAVORITE_LIST = true;
                            if(ServiceDetailsActivity.addToFavoriteList!=null) {
                                ServiceDetailsActivity.addToFavoriteList.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorRed));
                            }
                        }else{
                            if(ServiceDetailsActivity.addToFavoriteList!=null) {
                                ServiceDetailsActivity.addToFavoriteList.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));

                            }
                            ServiceDetailsActivity.ADD_TO_FAVORITE_LIST = false;
                        }

                        if(loadProductData) {
                            wishListModel.clear();
                            final String productId = task.getResult().get("product_id_" + i).toString();
                            firebaseFirestore.collection("PRODUCTS").document(productId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        wishListModel.add(new Wishlist(
                                                productId,
                                                task.getResult().get("product_image_1").toString(),
                                                task.getResult().get("product_title").toString(),
                                                task.getResult().get("product_price").toString()
                                        ));
                                        WishlistFragment.wishlistAdapter.notifyDataSetChanged();
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    public static void removeFragmentWishlist(final int index, final Context context){
        final String removeProductId = wishlist.get(index);
        wishlist.remove(index);
        Map<String,Object> updateWishlist = new HashMap<>();

        for (int i =0;i<wishlist.size();i++){
            updateWishlist.put("product_id_" + i,wishlist.get(i));
        }
        updateWishlist.put("list_size",(long)wishlist.size());
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                .document("MY_WISHLIST")
                .set(updateWishlist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    if(wishListModel.size()!=0){
                        wishListModel.remove(index);
                        WishlistFragment.wishlistAdapter.notifyDataSetChanged();
                    }
                    ServiceDetailsActivity.ADD_TO_FAVORITE_LIST = false;
                    Toast.makeText(context, "Товар удален!", Toast.LENGTH_SHORT).show();
                }else{
                    if(ServiceDetailsActivity.addToFavoriteList !=null) {
                        ServiceDetailsActivity.addToFavoriteList.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorRed));
                    }
                    wishlist.add(index,removeProductId);
                    String error = task.getException().getMessage();
                    Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
                }
//                if(ServiceDetailsActivity.addToFavoriteList!=null) {
//                    ServiceDetailsActivity.addToFavoriteList.setEnabled(true);
//                }
                ServiceDetailsActivity.running_wishlist_query=false;
            }
        });
    }

    public static void removeFragmentBasket(final int index, final Context context,final TextView basketTotalAmount){
        final String removeProductsId = basketlist.get(index);
        basketlist.remove(index);
        Map<String,Object> updateBasket = new HashMap<>();

        for (int i =0;i<basketlist.size();i++){
            updateBasket.put("product_id_" + i,basketlist.get(i));
        }
        updateBasket.put("list_size",(long)basketlist.size());
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                .document("MY_BASKET")
                .set(updateBasket).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    if(basketListModel.size()!=0){
                        basketListModel.remove(index);
                        BasketFragment.basketAdapter.notifyDataSetChanged();
                    }
             //       ServiceDetailsActivity.ADD_TO_BASKET_LIST = false;
                    if(basketlist.size()==0){
                        LinearLayout parent = (LinearLayout) basketTotalAmount.getParent().getParent();
                        parent.setVisibility(View.GONE);
                        basketListModel.clear();
                    }

                    Toast.makeText(context, "Товар удален!", Toast.LENGTH_SHORT).show();
                }else{

                    basketlist.add(index,removeProductsId);
                    String error = task.getException().getMessage();
                    Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
                }
                ServiceDetailsActivity.running_basket_query=false;
            }
        });
    }

    public static void loadBasketList(final Context context, final Dialog dialog, final boolean loadProductData,final TextView badgeCount,final TextView basketTotalAmoount){
        basketlist.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                .document("MY_BASKET")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    for(int i=0;i<(long)task.getResult().get("list_size");i++){
                        basketlist.add(task.getResult().get("product_id_" + i).toString());

                        if(DBQuery.basketlist.contains(ServiceDetailsActivity.productId)){
                            ServiceDetailsActivity.ADD_TO_BASKET_LIST = true;
                        }else{
                            ServiceDetailsActivity.ADD_TO_BASKET_LIST = false;
                        }
                        if(loadProductData) {
                            basketListModel.clear();
                            final String productId = task.getResult().get("product_id_"+i).toString();
                            firebaseFirestore.collection("PRODUCTS").document(productId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        int index = 0;
                                        if(basketlist.size()>=2){
                                            index  = basketlist.size()-2;
                                        }
                                        basketListModel.add(index,new Basket(Basket.BASKET,productId,
                                                task.getResult().get("product_image_1").toString(),
                                                task.getResult().get("product_title").toString(),
                                                task.getResult().get("product_price").toString(),
                                                (long)1
                                         ));

                                        if(basketlist.size()==1){
                                            basketListModel.add(new Basket(Basket.TOTAL_AMOUNT));
                                            LinearLayout parent = (LinearLayout)basketTotalAmoount.getParent().getParent();
                                            parent.setVisibility(View.VISIBLE);
                                        }
                                        if(basketlist.size()==0){
                                            basketListModel.clear();
                                        }
                                        BasketFragment.basketAdapter.notifyDataSetChanged();
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                    if(basketlist.size()!=0){
                        badgeCount.setVisibility(View.VISIBLE);
                     }else{
                       badgeCount.setVisibility(View.INVISIBLE);
                    }
                    if(DBQuery.basketlist.size()<99) {
                        badgeCount.setText(String.valueOf(DBQuery.basketlist.size()));
                    }else{
                        badgeCount.setText("99");
                    }

                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    public static void clearData(){
        categoryList.clear();
        lists.clear();
        loadedCategoriesItem.clear();
        wishlist.clear();
        wishListModel.clear();
        basketlist.clear();
        basketListModel.clear();

    }
}
