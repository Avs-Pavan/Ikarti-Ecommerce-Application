package com.webinfrasolutions.ikarti.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.webinfrasolutions.ikarti.API.AddFavouriteApi;
import com.webinfrasolutions.ikarti.API.DeleteFavouriteApi;
import com.webinfrasolutions.ikarti.Pojo.Category;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;
import com.webinfrasolutions.ikarti.ProductDetailsActivity;
import com.webinfrasolutions.ikarti.R;
import com.webinfrasolutions.ikarti.SmallBang;
import com.webinfrasolutions.ikarti.SmallBangListener;

import java.util.List;

import customfonts.MyTextView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kevin on 12/8/17.
 */

public class CatDialogGridAdapter extends RecyclerView.Adapter<CatDialogGridAdapter.MyViewHolder> {

Activity context;
    List<Category> categories;

    public CatDialogGridAdapter(Activity activity, List<Category> records) {
this.context=activity;
        this.categories =records;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
MyTextView textView;
       ImageView catImg;
        public MyViewHolder(View view) {
            super(view);
textView=view.findViewById(R.id.cat_title);
catImg=view.findViewById(R.id.cat_img);
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cat_grid_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

holder.textView.setText(categories.get(position).getCatName());

switch (position)
{
    case 0:
        holder.catImg.setImageResource(R.drawable.electronics);
break;
    case 1:
        holder.catImg.setImageResource(R.drawable.fashion);
break;
    case 2:
        holder.catImg.setImageResource(R.drawable.food);
        break;


}




    }



    @Override
    public int getItemCount() {
        return categories.size();
    }

}
