package com.webinfrasolutions.ikarti.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webinfrasolutions.ikarti.Pojo.Product;
import com.webinfrasolutions.ikarti.R;

import java.util.List;

/**
 * Created by pawan on 12-05-2017.
 */

public class MultiSelectProductAdapter extends RecyclerView.Adapter<MultiSelectProductAdapter.MyViewHolder> {

    private List<Product> productList;

    private Context context;
    public MultiSelectProductAdapter(Context context, List<Product> list )

    {
      productList =list;
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
    public MultiSelectProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_multiselect_row, parent, false);

        return new MultiSelectProductAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        final Product model = productList.get(position);
        holder.textView.setText(model.getProductName());
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
        return productList == null ? 0 : productList.size();
                //urls.length;
    }

}
