package org.seda.animalshelter.Fragments;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.seda.animalshelter.R;


public class WriteUsFragment extends Fragment {

    public static Toolbar toolbar;
    private TextView email;

    public WriteUsFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_write_us, container, false);



        email =view.findViewById(R.id.textView_email_activity_about_us);

        email.setPaintFlags(email.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        return view;
    }
}