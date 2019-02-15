package com.webinfrasolutions.ikarti.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.webinfrasolutions.ikarti.CategoryLIstActivity;
import com.webinfrasolutions.ikarti.Pojo.Record;
import com.webinfrasolutions.ikarti.R;
import com.webinfrasolutions.ikarti.Uhome;

import java.util.ArrayList;
import java.util.List;

import customfonts.MyTextView;

/**
 * Created by kevin on 12/8/17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    String uid;
Context  context;
    Activity activity;
    List<Record> records=new ArrayList<Record>();
    public MyAdapter(Activity activity,Context contexts, List<Record> records,String uid) {
this.context=contexts;
        this.activity=activity;
       this.records=records;this.uid=uid;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyTextView cat_name,showMoreBtn;
RecyclerView rv;
        LinearLayout layout;

        public MyViewHolder(View view) {
            super(view);
            cat_name =  view.findViewById(R.id.cat_title);
            showMoreBtn =  view.findViewById(R.id.show_more_btn);
layout=view.findViewById(R.id.layout);
            rv =  view.findViewById(R.id.cat_rv);

            setIsRecyclable(false);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dash_product_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


if (records.get(position).getProducts().size()>0){
    holder.layout.setVisibility(View.VISIBLE);
    holder.cat_name.setText(records.get(position).getName());
    RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
    holder.rv.setLayoutManager(layoutManager1);
    holder.rv.setAdapter(new DashAddsAdapter(activity,records.get(position).getProducts(),uid));
    holder.showMoreBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Intent x=new Intent(Uhome.this,CategoryLIstActivity.class);
           // x.putExtra("cat",pojo.getCategory().get(position).getCatName());
          //  x.putExtra("catId",pojo.getCategory().get(position).getCatId());

            //startActivity(x);
            Bundle bundle = new Bundle();
            bundle.putString("cat",records.get(position).getName());
            bundle.putString("catId",records.get(position).getCatId());

            Intent mainIntent = new Intent(context, CategoryLIstActivity.class);
           // add bundle to intent
            mainIntent.putExtras(bundle);
         //   start activity
            context.startActivity(mainIntent);
        }
    });
}


    }

    @Override
    public int getItemCount() {
        return records.size();
    }


}
