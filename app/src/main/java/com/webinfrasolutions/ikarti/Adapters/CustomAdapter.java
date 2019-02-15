package com.webinfrasolutions.ikarti.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.webinfrasolutions.ikarti.Pojo.Pic;
import com.webinfrasolutions.ikarti.Pojo.Product;
import com.webinfrasolutions.ikarti.ProductDetailsActivity;
import com.webinfrasolutions.ikarti.R;
import com.webinfrasolutions.ikarti.SmallBang;
import com.webinfrasolutions.ikarti.SmallBangListener;

import java.util.List;


public class CustomAdapter extends UltimateViewAdapter<CustomAdapter.SimpleAdapterViewHolder> {
    private List<Product> productList;
    private Activity main;

    public CustomAdapter(List<Product> stringList, Activity categoryLIstActivity) {
        this.productList = stringList;
        this.main=categoryLIstActivity;
    }


    @Override
    public void onBindViewHolder(final SimpleAdapterViewHolder holder, int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= productList.size() : position < productList.size()) && (customHeaderView != null ? position > 0 : true)) {

            ((SimpleAdapterViewHolder) holder).title.setText(productList.get(position).getProductName());
            holder.setIsRecyclable(false);

            //  holder.icon.setImageResource(mDataset.get(position).get());
          //  holder.image.setImageResource(urls[position]);
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

            Pic pic=productList.get(position).getPics().get(0);
            Glide.with(main).
                    load(main.getResources().getString(R.string.base_url)+"deals/images/"+pic.getPicPath())
                    .thumbnail(Glide.with(main).load(R.drawable.loading))
                    .crossFade()
                    .into(holder.image);
        }

    }

    @Override
    public int getAdapterItemCount() {
        return productList.size();
    }

    @Override
    public SimpleAdapterViewHolder getViewHolder(View view) {
        return new SimpleAdapterViewHolder(view, false);
    }

    @Override
    public SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cat_list_view, parent, false);
        SimpleAdapterViewHolder vh = new SimpleAdapterViewHolder(v, true);
        return vh;
    }

    public void  showMsg(String msg){

        Toast.makeText(main,msg,Toast.LENGTH_SHORT).show();

    }
    public void insert(Product product, int position) {
        insertInternal(productList, product, position);
    }

    public void remove(int position) {
        removeInternal(productList, position);
       // showMsg("remove");
    }

    public void clear() {
        clearInternal(productList);
    }

    @Override
    public void toggleSelection(int pos) {
        super.toggleSelection(pos);
    }

    @Override
    public void setSelected(int pos) {
        super.setSelected(pos);
    }

    @Override
    public void clearSelection(int pos) {
        super.clearSelection(pos);
    }


    public void swapPositions(int from, int to) {
        swapPositions(productList, from, to);
    }


    @Override
    public long generateHeaderId(int position) {

        if (getItem(position).length() > 0)
            return getItem(position).charAt(0);
        else return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.stick_header_item, viewGroup, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        TextView textView = (TextView) viewHolder.itemView.findViewById(R.id.stick_text);
        textView.setText(String.valueOf(getItem(position).charAt(0)));
        viewHolder.itemView.setBackgroundColor(Color.parseColor("#AAffffff"));


    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        swapPositions(fromPosition, toPosition);
//        notifyItemMoved(fromPosition, toPosition);
        super.onItemMove(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        if (position > 0)
          //remove(position);
        // notifyItemRemoved(position);
//        notifyDataSetChanged();
        super.onItemDismiss(position);
    }


    public void setOnDragStartListener(OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;

    }


    public class SimpleAdapterViewHolder extends UltimateRecyclerviewViewHolder {


        ImageView image;
        TextView title;
        TextView price;
        TextView cutprice;
        TextView discount;
        TextView ratingtex;
        ImageView fav1,fav2;
        RatingBar ratingbar;
        private SmallBang mSmallBang;

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);

            if (isItem) {


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

              //  Log.i(LOG_TAG, "Adding Listener");
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
       Bundle bundle = new Bundle();
                       bundle.putSerializable("record", productList.get(getLayoutPosition()));
                       Intent mainIntent = new Intent(main, ProductDetailsActivity.class);
                        mainIntent.putExtras(bundle);
                        main.startActivity(mainIntent);                    }
                });
            }

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    public String getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < productList.size())
            return productList.get(position).getProductName();
        else return "";
    }

}