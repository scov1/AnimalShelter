package org.seda.animalshelter.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.seda.animalshelter.R;


public class PetsFragment extends Fragment {



    public PetsFragment() {
        // Required empty public constructor
    }

    private View view;


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pets, container, false);

        initializeView();


        return view;
    }

    public void initializeView() {

    }

}