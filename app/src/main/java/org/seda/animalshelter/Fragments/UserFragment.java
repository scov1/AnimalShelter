package org.seda.animalshelter.Fragments;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import org.seda.animalshelter.Activity.MainActivity;
import org.seda.animalshelter.Activity.MainMenuActivity;
import org.seda.animalshelter.Activity.UpdateUserInfoActivity;
import org.seda.animalshelter.DB.DBQuery;
import org.seda.animalshelter.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserFragment extends Fragment{

    private View view;


    public UserFragment() {
        // Required empty public constructor
    }

    private CircleImageView profileView;
    private TextView name,email,welcome;
    private Button signOutBtn;
    private FloatingActionButton settingsBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);

        initializeView();

        return view;
    }

    public void initializeView() {

        profileView = view.findViewById(R.id.circleImageView_profile_image);
        name = view.findViewById(R.id.textView_profile_layout_name);
        email = view.findViewById(R.id.textView_profile_layout_email);
        signOutBtn = view.findViewById(R.id.btnBuy_fragment_account);
        welcome = view.findViewById(R.id.textView_welcome_fragment_account);
        settingsBtn = view.findViewById(R.id.floatingActionButton_profile_layout);




        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                DBQuery.clearData();
                Intent registerIntent = new Intent(getContext(), MainActivity.class);
                startActivity(registerIntent);
                getActivity().finish();
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateUser = new Intent(getContext(), UpdateUserInfoActivity.class);
                updateUser.putExtra("Name",name.getText());
                updateUser.putExtra("Email",email.getText());
                updateUser.putExtra("Photo",DBQuery.profile);

                startActivity(updateUser);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        name.setText(DBQuery.fullname);
        email.setText(DBQuery.email);
        welcome.setText("Добро пожаловать!");
        if(!DBQuery.profile.equals("")){
            Glide.with(getActivity()).load(DBQuery.profile).apply(new RequestOptions().placeholder(R.mipmap.ic_img_profile)).into(profileView);
        }else{
            profileView.setImageResource(R.mipmap.ic_img_profile);
        }
    }
}