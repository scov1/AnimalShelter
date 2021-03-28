package org.seda.animalshelter.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.seda.animalshelter.Adapters.OrderAdapter;
import org.seda.animalshelter.Models.Order;
import org.seda.animalshelter.R;

import java.util.ArrayList;
import java.util.List;


public class OrdersFragment extends Fragment{

    private View view;

    public OrdersFragment() {
        // Required empty public constructor
    }

    private RecyclerView orderRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_orders, container, false);

        initializeView();

        return view;
    }

    public void initializeView() {

        orderRecyclerView = view.findViewById(R.id.recyclerView_fragment_orders);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        orderRecyclerView.setLayoutManager(linearLayoutManager);

        List<Order> orderList = new ArrayList<>();
        orderList.add(new Order(R.drawable.food_icon,"Viskas","6 февраля 2021г."));
        orderList.add(new Order(R.drawable.vetirinar_icon,"Лишай","Отмена"));
        orderList.add(new Order(R.drawable.uhod_icon,"Стрижка","2 февраля 2021г."));

        OrderAdapter orderAdapter = new OrderAdapter(orderList);
        orderRecyclerView.setAdapter(orderAdapter);
        orderAdapter.notifyDataSetChanged();

    }
}