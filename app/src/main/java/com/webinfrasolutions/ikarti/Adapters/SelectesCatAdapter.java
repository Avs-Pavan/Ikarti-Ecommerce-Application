package com.webinfrasolutions.ikarti.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webinfrasolutions.ikarti.Pojo.Category;
import com.webinfrasolutions.ikarti.R;

import java.util.ArrayList;

/**
 * Created by pawan on 12-05-2017.
 */

public class SelectesCatAdapter extends RecyclerView.Adapter<SelectesCatAdapter.MyViewHolder> {

    private ArrayList<Category> catList;

    private Context context;
    public SelectesCatAdapter(Context context,ArrayList<Category>list )

    {
      catList =list;
        this.context=context;

       }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView textView;

        private MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textView = (TextView) itemView.findViewById(R.id.name);

        }


    }

    @Override
    public SelectesCatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selected_cat_rv_row, parent, false);

        return new SelectesCatAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.textView.setText(catList.get(position).getCatName());

    }

    @Override
    public int getItemCount() {
        return catList == null ? 0 : catList.size();
                //urls.length;
    }

}
