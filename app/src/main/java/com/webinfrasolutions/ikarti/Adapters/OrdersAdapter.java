package com.webinfrasolutions.ikarti.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.webinfrasolutions.ikarti.API.AcceptOrderApi;
import com.webinfrasolutions.ikarti.API.DeclineOrderApi;
import com.webinfrasolutions.ikarti.API.DisptchOrderApi;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;
import com.webinfrasolutions.ikarti.Pojo.OrderItem;
import com.webinfrasolutions.ikarti.R;

import java.util.List;

import customfonts.MyEditText;
import customfonts.MyTextView;
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

public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Context context;

    List<OrderItem> beans;

    public OrdersAdapter(Context contexts, List<OrderItem> record) {
        this.context = contexts;
        this.beans = record;
    }

    public class OrderDeclined extends RecyclerView.ViewHolder {
        ImageView image;
        TextView orderday;
        TextView ordername;
        TextView qt,declined;
        TextView idnumber;
StateProgressBar progressBar;
        public OrderDeclined(View convertView) {
            super(convertView);


            image = (ImageView)convertView.findViewById(R.id.image);
            orderday = (TextView)convertView.findViewById(R.id.orderday);
            ordername = (TextView)convertView.findViewById(R.id.ordername);
           qt = (TextView)convertView.findViewById(R.id.qt);
           idnumber = (TextView)convertView.findViewById(R.id.idnumber);
           progressBar = convertView.findViewById(R.id.your_state_progress_bar_id);
           declined = convertView.findViewById(R.id.declined);



            setIsRecyclable(false);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView ;

                itemView  = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_order, parent, false);
                return new OrderDeclined(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        String[] descriptionData = {"Placed", "Confirmed",
                "Dispatched", "Delivered"};


        OrderItem bean = (OrderItem) beans.get(position);
        Glide.with(context).
                load(context.getResources().getString(R.string.base_url)+"deals/images/"+bean.getPics().get(0).getPicPath())
                .thumbnail(Glide.with(context).load(R.drawable.loading))
                .crossFade()
                .into(((OrderDeclined)viewHolder).image);

        //  viewHolder.image.setImageResource(bean.getPics().get(0).getPicPath());
        ((OrderDeclined)viewHolder).orderday.setText(bean.getCreatedData());
        ((OrderDeclined)viewHolder).ordername.setText(bean.getProduct().get(0).getProductName());
        ((OrderDeclined)viewHolder).qt.setText(bean.getQuantity());
        ((OrderDeclined)viewHolder).idnumber.setText(bean.getOrderId());
        ((OrderDeclined)viewHolder).progressBar.setStateDescriptionData(descriptionData);

        switch (bean.getOrderStatus()){

            case "order_placed":{
                ((OrderDeclined)viewHolder).progressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                ((OrderDeclined)viewHolder).progressBar.setForegroundColor(context.getResources().getColor(R.color.yellow));
                ((OrderDeclined)viewHolder).progressBar.setCurrentStateDescriptionColor(context.getResources().getColor(R.color.yellow));
                break;
            }case "order_accpted":{
                ((OrderDeclined)viewHolder).progressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                ((OrderDeclined)viewHolder).progressBar.setForegroundColor(context.getResources().getColor(R.color.colorbutton));
                ((OrderDeclined)viewHolder).progressBar.setCurrentStateDescriptionColor(context.getResources().getColor(R.color.colorbutton));
                break;
            }case "order_dispatched":{
                ((OrderDeclined)viewHolder).progressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                ((OrderDeclined)viewHolder).progressBar.setForegroundColor(context.getResources().getColor(R.color.light_blue));
                ((OrderDeclined)viewHolder).progressBar.setCurrentStateDescriptionColor(context.getResources().getColor(R.color.light_blue));

                break;
            }case "order_delivered":{
                ((OrderDeclined)viewHolder).progressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                ((OrderDeclined)viewHolder).progressBar.setForegroundColor(context.getResources().getColor(R.color.light_green));
                ((OrderDeclined)viewHolder).progressBar.setCurrentStateDescriptionColor(context.getResources().getColor(R.color.light_green));
                break;
            }
            case "order_declined":{
                ((OrderDeclined)viewHolder).progressBar.setVisibility(View.GONE);
                ((OrderDeclined)viewHolder).declined.setVisibility(View.VISIBLE);
                ((OrderDeclined)viewHolder).declined.setText("Order Declined By StoreKeeper\nReason : My Reason");

                break;
            }



        }





    }




    @Override
    public int getItemCount() {
        return beans.size();
    }

   /* @Override
    public int getItemViewType(int position) {

        switch (beans.get(position).getOrderStatus()) {
            case "order_placed":
                return 0;
            case "order_accpted":
                return 1;
            default:
                return 2;
        }


    }
*/



}
