package com.webinfrasolutions.ikarti.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.webinfrasolutions.ikarti.R;
import com.webinfrasolutions.ikarti.SmallBang;
import com.webinfrasolutions.ikarti.SmallBangListener;

/**
 * Created by kevin on 12/11/17.
 */
//mNoOfColumns = Utility.calculateNoOfColumns(getApplicationContext());
    //    rv.setLayoutManager(new GridLayoutManager(this, mNoOfColumns));

public class CatListAdapter  extends RecyclerView
        .Adapter<CatListAdapter.DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    // private ArrayList<BeanlistGrooming> bean;
    Activity main;
    int [] urls;
    private static CatListAdapter.MyClickListener myClickListener;
    private Context context;

    public CatListAdapter()

    {

    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {



        ImageView image;
        TextView title;
        TextView price;
        TextView cutprice;
        TextView discount;
        TextView ratingtex;
        ImageView fav1,fav2;
        RatingBar ratingbar;
        private SmallBang mSmallBang;


        public DataObjectHolder(View itemView) {
            super(itemView);


            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            price = (TextView) itemView.findViewById(R.id.price);
            cutprice = (TextView) itemView.findViewById(R.id.cutprice);
            discount = (TextView) itemView.findViewById(R.id.discount);
            ratingtex = (TextView) itemView.findViewById(R.id.ratingtext);








            cutprice.setPaintFlags(cutprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);



//        ***********ratingBar**********
            ratingbar = (RatingBar) itemView.findViewById(R.id.ratingbar);
            LayerDrawable stars = (LayerDrawable) ratingbar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.parseColor("#f8d64e"), PorterDuff.Mode.SRC_ATOP);

            fav1 = (ImageView)itemView.findViewById(R.id.fav1);
            fav2 = (ImageView)itemView.findViewById(R.id.fav2);




            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            //       myClickListener.onItemClick(getAdapterPosition(), v);
        }

    }

    public void setOnItemClickListener(CatListAdapter.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public CatListAdapter(Activity activity, Context context,int [] images ) {

        this.main = activity;
        this.context = context;
        this.urls = images;
    }



    @Override
    public CatListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cat_list_view, parent, false);

        CatListAdapter.DataObjectHolder dataObjectHolder = new CatListAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final CatListAdapter.DataObjectHolder holder, int position) {


        holder.setIsRecyclable(false);

        //  holder.icon.setImageResource(mDataset.get(position).get());
        holder.image.setImageResource(urls[position]);
      /*   holder.title.setText(bean.get(position).getTitle());
        holder.price.setText(bean.get(position).getPrice());
        holder.cutprice.setText(bean.get(position).getCutprice());
        holder.discount.setText(bean.get(position).getDiscount());
        holder.ratingtex.setText(bean.get(position).getRatingtext());

*/
        holder.mSmallBang = SmallBang.attach2Window(main);

        holder.fav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                holder.fav2.setVisibility(View.VISIBLE);
                holder.fav1.setVisibility(View.GONE);
                like(v);

            }

            public void like(View view){
                holder.fav2.setImageResource(R.drawable.f4);
                holder.mSmallBang.bang(view);
                holder.mSmallBang.setmListener(new SmallBangListener() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {

                    }


                });
            }

        });

        holder.fav2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                holder.fav2.setVisibility(View.GONE);
                holder.fav1.setVisibility(View.VISIBLE);


            }
        });



    }





    public void deleteItem(int index) {
        //  bean.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return
                urls.length;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);

    }






}
