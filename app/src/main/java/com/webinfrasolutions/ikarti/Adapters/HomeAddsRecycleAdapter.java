package com.webinfrasolutions.ikarti.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.webinfrasolutions.ikarti.Pojo.Record;
import com.webinfrasolutions.ikarti.ProductDetailsActivity;
import com.webinfrasolutions.ikarti.R;
import com.webinfrasolutions.ikarti.SmallBang;
import com.webinfrasolutions.ikarti.SmallBangListener;

import java.io.Serializable;
import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by kevin on 11/11/17.
 */

public class HomeAddsRecycleAdapter extends RecyclerView
        .Adapter<HomeAddsRecycleAdapter.DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
   static Activity main;
    private static Record datarecord;
    private  static  Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {


        ImageView image;
        TextView title;
        TextView price;
        TextView cutprice;
        TextView discount;
        TextView ratingtex;
        ImageView fav1, fav2;
        RatingBar ratingbar;
        private SmallBang mSmallBang;
        LinearLayout lay;


        public DataObjectHolder(View itemView) {
            super(itemView);


            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            price = (TextView) itemView.findViewById(R.id.price);
            cutprice = (TextView) itemView.findViewById(R.id.cutprice);
            discount = (TextView) itemView.findViewById(R.id.discount);
            ratingtex = (TextView) itemView.findViewById(R.id.ratingtext);

            lay = (LinearLayout) itemView.findViewById(R.id.lay);


            cutprice.setPaintFlags(cutprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

//        ***********ratingBar**********
            ratingbar = (RatingBar) itemView.findViewById(R.id.ratingbar);
            LayerDrawable stars = (LayerDrawable) ratingbar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.parseColor("#f8d64e"), PorterDuff.Mode.SRC_ATOP);

            fav1 = (ImageView) itemView.findViewById(R.id.fav1);
            fav2 = (ImageView) itemView.findViewById(R.id.fav2);

            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("record",datarecord.getProducts().get(getLayoutPosition()));
             Intent mainIntent = new Intent(context, ProductDetailsActivity.class);
            //add bundle to intent
            mainIntent.putExtras(bundle);
            //start activity
            context.startActivity(mainIntent);
        }
    }

    public HomeAddsRecycleAdapter(Activity activity, Context context, Record record) {

        this.main = activity;
        this.context = context;
        this.datarecord=record;

    }



    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.addview, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {


        holder.setIsRecyclable(false);
        holder.title.setText(datarecord.getProducts().get(position).getProductName());
        holder.price.setText(datarecord.getProducts().get(position).getProductPrice());


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




    @Override
    public int getItemCount() {
        return
               datarecord. getProducts().size();
    }





}
