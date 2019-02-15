package com.webinfrasolutions.ikarti.Adapters;

/**
 * Created by kevin on 12/8/17.
 */


import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.webinfrasolutions.ikarti.OfferDetailsActivity;
import com.webinfrasolutions.ikarti.Pojo.Pic;
import com.webinfrasolutions.ikarti.R;

import java.util.List;


public class SlidingImage_Adapter extends PagerAdapter {


 //   private ArrayList<String> IMAGES,name;
   private LayoutInflater inflater;
   private Context context;
private  List<Pic> urls;

    public SlidingImage_Adapter(Context context, List<Pic> urls
                                //Context context, ArrayList<String> IMAGES, ArrayList<String> name
    ) {
       this.context = context;
       // this.IMAGES=IMAGES;
       // this.name=name;
        this.urls=urls;
       inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return urls.size();

    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.image_slider_view, view, false);

       /* imageLayout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //this will log the page number that was click
                Intent i=new Intent(context, OfferDetailsActivity.class);
                context.startActivity(i);
                Toast.makeText(context,"clicked  "+(position+1),Toast.LENGTH_SHORT).show();
                Log.i("TAG", "This page was clicked: " + position);
            }
        });*/


        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.slider_img);
        Glide.with(context).
                load(context.getResources().getString(R.string.base_url)+"deals/images/"+urls.get(position).getPicPath())
                .thumbnail(Glide.with(context).load(R.drawable.loading))
                .crossFade()
                .into(imageView);
     // Glide.with(context).load(IMAGES.get(position)).into(imageView);
        //imageView.setImageResource(IMAGES.get(position));

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}