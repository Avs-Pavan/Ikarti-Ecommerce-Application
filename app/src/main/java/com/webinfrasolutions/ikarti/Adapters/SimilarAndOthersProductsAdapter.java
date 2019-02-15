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

import com.webinfrasolutions.ikarti.CategoryLIstActivity;
import com.webinfrasolutions.ikarti.Pojo.Record;
import com.webinfrasolutions.ikarti.R;

import java.util.ArrayList;
import java.util.List;

import customfonts.MyTextView;

/**
 * Created by kevin on 12/8/17.
 */

public class SimilarAndOthersProductsAdapter extends RecyclerView.Adapter<SimilarAndOthersProductsAdapter.MyViewHolder> {

    String uid;
Context  context;
    Activity activity;
    List<Record> records=new ArrayList<Record>();
    public SimilarAndOthersProductsAdapter(Activity activity, Context contexts, List<Record> records, String uid) {
this.context=contexts;
        this.activity=activity;
       this.records=records;this.uid=uid;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyTextView cat_name;
RecyclerView rv;

        public MyViewHolder(View view) {
            super(view);
            cat_name =  view.findViewById(R.id.cat_title);

            rv =  view.findViewById(R.id.cat_rv);

            setIsRecyclable(false);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dash_row_2, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


if (records.get(position).getProducts().size()>0){
    holder.cat_name.setText(records.get(position).getName());
    RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
    holder.rv.setLayoutManager(layoutManager1);
    holder.rv.setAdapter(new DashAddsAdapter(activity,records.get(position).getProducts(),uid));

}

    }

    @Override
    public int getItemCount() {
        return records.size();
    }

}
