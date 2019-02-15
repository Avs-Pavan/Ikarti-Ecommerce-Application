package com.webinfrasolutions.ikarti.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webinfrasolutions.ikarti.Pojo.Category;
import com.webinfrasolutions.ikarti.R;

import java.util.List;

/**
 * Created by pawan on 12-05-2017.
 */

public class MultiSelectAdapterCategory extends RecyclerView.Adapter<MultiSelectAdapterCategory.MyViewHolder> {

    private List<Category> catList;

    private Context context;
    public MultiSelectAdapterCategory(Context context, List<Category> list )

    {
      catList =list;
        this.context=context;

       }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView textView;

private LinearLayout lay;
        private ImageView tick;
        private MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textView = (TextView) itemView.findViewById(R.id.name);
            tick=(ImageView)itemView.findViewById(R.id.tick);
            lay=(LinearLayout) itemView.findViewById(R.id.lay_view);
        }


    }

    @Override
    public MultiSelectAdapterCategory.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_multiselect_row, parent, false);

        return new MultiSelectAdapterCategory.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        final Category model = catList.get(position);
        holder.textView.setText(model.getCatName());
        holder.tick.setBackgroundColor(model.isSelected() ? context.getResources().getColor(R.color.light_green) :  context.getResources().getColor(R.color.lightGrey) );

        holder.lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setSelected(!model.isSelected());

            // Toast.makeText(context,"hi",Toast.LENGTH_SHORT).show();
                holder.tick.setBackgroundColor(model.isSelected() ? context.getResources().getColor(R.color.light_green) :  context.getResources().getColor(R.color.lightGrey) );

            }
        });
    }

    @Override
    public int getItemCount() {
        return catList == null ? 0 : catList.size();
                //urls.length;
    }

}
