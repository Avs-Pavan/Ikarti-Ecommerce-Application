package com.webinfrasolutions.ikarti;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.webinfrasolutions.ikarti.Pojo.Category;
import com.webinfrasolutions.ikarti.Pojo.CategoryListPojo;

import java.util.ArrayList;
import java.util.List;

import customfonts.MyTextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UhomeNavigationFragment extends Fragment {
    CategoryListPojo pojo;
    public UhomeNavigationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_uhome_navigation, container, false);
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
           pojo=(CategoryListPojo) getArguments().getSerializable("pojo");
            String kk = getArguments().getString("params");
        }

     final DrawerLayout   drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawerlayout);
         final FrameLayout    navList = (FrameLayout)getActivity(). findViewById(R.id.nav_holder);

       // ListView lv=(ListView)getActivity().findViewById(R.id.uhome_nav_lv);

        MyTextView orders=getActivity().findViewById(R.id.orders);
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),Orders.class);
                startActivity(i);
            }
        });

    }
    public  String[] prepareArray(List<Category> list){
        String [] li=new String[list.size()];
        for (int x=0;x<list.size();x++) {
            li[x]=list.get(x).getCatName();

        }

        return  li;
    }



}
