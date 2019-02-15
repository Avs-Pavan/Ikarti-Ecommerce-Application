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
import com.webinfrasolutions.ikarti.Pojo.OrderItem;
import com.webinfrasolutions.ikarti.Pojo.Record;
import com.webinfrasolutions.ikarti.Pojo.WorkerOrder;
import com.webinfrasolutions.ikarti.R;

import java.util.ArrayList;
import java.util.List;

import customfonts.MyTextView;

/**
 * Created by kevin on 12/8/17.
 */

public class WorkerOrderListAdapter extends RecyclerView.Adapter<WorkerOrderListAdapter.MyViewHolder> {


    Context  context;
    List<WorkerOrder> records=new ArrayList<WorkerOrder>();
    public WorkerOrderListAdapter(Context contexts, List<WorkerOrder> records) {
this.context=contexts;
this.records=records;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyTextView title;

        ImageView image;
        TextView orderday;
        TextView qt;
        // TextView date;
        TextView idnumber;
        public MyViewHolder(View convertView) {
            super(convertView);

            title =  convertView.findViewById(R.id.title);

            image = (ImageView)convertView.findViewById(R.id.image);
            orderday = (TextView)convertView.findViewById(R.id.orderday);
             qt = (TextView)convertView.findViewById(R.id.qt);
            //   date = (TextView)convertView.findViewById(R.id.date);
            idnumber = (TextView)convertView.findViewById(R.id.idnumber);

            setIsRecyclable(false);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.worker_order_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final WorkerOrder bean = records.get(position);

        holder.title.setText(records.get(position).getProduct().get(0).getProductName());
        Glide.with(context).
                load(context.getResources().getString(R.string.base_url) + "deals/images/" + bean.getPics().get(0).getPicPath())
                .thumbnail(Glide.with(context).load(R.drawable.loading))
                .crossFade()
                .into(holder.image);
        holder.orderday.setText(bean.getCreatedData());
        holder.idnumber.setText(bean.getOrderId());
        holder.qt.setText(bean.getQuantity());

    }

    @Override
    public int getItemCount() {
        return records.size();
    }


}
