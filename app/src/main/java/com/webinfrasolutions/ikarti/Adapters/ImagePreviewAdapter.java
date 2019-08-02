package com.webinfrasolutions.ikarti.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.webinfrasolutions.ikarti.R;

import java.util.List;

/**
 * Created by kevin on 12/8/17.
 */

public class ImagePreviewAdapter extends RecyclerView.Adapter<ImagePreviewAdapter.MyViewHolder> {

    private List<String> urls;
    Context context;
    ImageView deleteimg;

    public ImagePreviewAdapter(Context contexts, List<String> url) {
        this.context = contexts;
        this.urls = url;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public MyViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.preview_img);
            deleteimg = (ImageView) view.findViewById(R.id.deletepic);
            setIsRecyclable(false);
        }


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_preview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        deleteimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urls.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "delete " + position, Toast.LENGTH_SHORT).show();
            }
        });

        Glide.with(context)
                .load(concatinate(urls.get(position)))
                //  .asGif()
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public String concatinate(String str) {

        str = context.getString(R.string.base_url) + "/deals/images/" + str;
        return str;

    }
}
