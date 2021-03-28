package org.seda.animalshelter.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.seda.animalshelter.Activity.AnimalsDetailsActivity;
import org.seda.animalshelter.Activity.MainMenuActivity;
import org.seda.animalshelter.Activity.PaymentActivity;
import org.seda.animalshelter.Activity.ServiceDetailsActivity;
import org.seda.animalshelter.Models.Animals;
import org.seda.animalshelter.Models.HorizontalProduct;
import org.seda.animalshelter.R;

import java.util.List;

public class GridListAnimalsAdapter extends BaseAdapter {

    public static List<Animals> animalsList;

    public GridListAnimalsAdapter(List<Animals> animalsList) {
        this.animalsList = animalsList;
    }

    @Override
    public int getCount() {
        return animalsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        View view1;

        if(view == null){
            view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.animal_item,null);

            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(viewGroup.getContext(), AnimalsDetailsActivity.class);
                    intent.putExtra("animal_id",animalsList.get(i).getAnimalId());
                    viewGroup.getContext().startActivity(intent);
                    Animatoo.animateSwipeLeft(viewGroup.getContext());

                }
            });

            ImageView animalImage = view1.findViewById(R.id.imageView_animal_item_image_animal);
            TextView animalName = view1.findViewById(R.id.textView_animal_item_name_animal);
            TextView animalAge = view1.findViewById(R.id.textView_animal_item_age_animal);
            TextView animalGender = view1.findViewById(R.id.textView_animal_item_gender_animal);
          //  TextView animalDesc = view1.findViewById(R.id.desc);


            Glide.with(viewGroup.getContext()).load(animalsList.get(i).getAnimalImage()).apply(new RequestOptions().placeholder(R.drawable.home_hor_icon)).into(animalImage);
            animalName.setText(animalsList.get(i).getName());
            animalAge.setText(animalsList.get(i).getAge());
            animalGender.setText(animalsList.get(i).getGender());
        }else{
            view1 = view;
        }

        return view1;
    }


}
