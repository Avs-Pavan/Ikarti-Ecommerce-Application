package com.webinfrasolutions.ikarti.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.webinfrasolutions.ikarti.API.AcceptOrderApi;
import com.webinfrasolutions.ikarti.API.DeclineOrderApi;
import com.webinfrasolutions.ikarti.API.DeleteCartItemApi;
import com.webinfrasolutions.ikarti.API.DisptchOrderApi;
import com.webinfrasolutions.ikarti.CartActivity;
import com.webinfrasolutions.ikarti.CategoryLIstActivity;
import com.webinfrasolutions.ikarti.Pojo.Category;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;
import com.webinfrasolutions.ikarti.Pojo.OrderItem;
import com.webinfrasolutions.ikarti.Pojo.Pic;
import com.webinfrasolutions.ikarti.Pojo.Record;
import com.webinfrasolutions.ikarti.Pojo.Worker;
import com.webinfrasolutions.ikarti.R;
import com.webinfrasolutions.ikarti.RegisterShop;
import com.webinfrasolutions.ikarti.util.SessionManager;

import java.util.ArrayList;
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

public class StoreOrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;

    List<OrderItem> beans;

    public StoreOrdersAdapter(Context contexts, List<OrderItem> record) {
        this.context = contexts;
        this.beans = record;
    }

    public class OrderAccpted extends RecyclerView.ViewHolder {
        ImageView image;
        TextView orderday;
        TextView ordername;
        TextView qt,dispatch;
       // TextView date;

        TextView idnumber;
        public OrderAccpted(View convertView) {
            super(convertView);

            image = (ImageView)convertView.findViewById(R.id.image);
          orderday = (TextView)convertView.findViewById(R.id.orderday);
          dispatch = (TextView)convertView.findViewById(R.id.dispatch);
           ordername = (TextView)convertView.findViewById(R.id.ordername);
           qt = (TextView)convertView.findViewById(R.id.qt);
         //   date = (TextView)convertView.findViewById(R.id.date);
            idnumber = (TextView)convertView.findViewById(R.id.idnumber);
            setIsRecyclable(false);
        }

    }
    public class OrderPlaced extends RecyclerView.ViewHolder {
        ImageView image;
        TextView orderday;
        TextView ordername;
        TextView qt;
        // TextView date;
        MyTextView ok,cancel;
        TextView idnumber;
        public OrderPlaced(View convertView) {
            super(convertView);



            image = (ImageView)convertView.findViewById(R.id.image);
            orderday = (TextView)convertView.findViewById(R.id.orderday);
            ok = convertView.findViewById(R.id.button_ok);
            cancel = convertView.findViewById(R.id.button_cancel);
            ordername = (TextView)convertView.findViewById(R.id.ordername);
            qt = (TextView)convertView.findViewById(R.id.qt);
            //   date = (TextView)convertView.findViewById(R.id.date);
            idnumber = (TextView)convertView.findViewById(R.id.idnumber);
            setIsRecyclable(false);
        }

    }
    public class OrderDefault extends RecyclerView.ViewHolder {
        ImageView image;
        TextView orderday;
        TextView ordername;
        TextView qt,status;
        // TextView date;
        MyTextView ok,cancel;
        TextView idnumber;
        public OrderDefault(View convertView) {
            super(convertView);



            image = (ImageView)convertView.findViewById(R.id.image);
            orderday = (TextView)convertView.findViewById(R.id.orderday);
            status = (TextView)convertView.findViewById(R.id.status);
            ok = convertView.findViewById(R.id.button_ok);
            cancel = convertView.findViewById(R.id.button_cancel);
            ordername = (TextView)convertView.findViewById(R.id.ordername);
            qt = (TextView)convertView.findViewById(R.id.qt);
            //   date = (TextView)convertView.findViewById(R.id.date);
            idnumber = (TextView)convertView.findViewById(R.id.idnumber);
            setIsRecyclable(false);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView ;
        switch (viewType) {
            case 0:
                itemView  = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.store_order_row, parent, false);
                return new OrderPlaced(itemView);
            case 1:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_order_accepted_row, parent, false);
                return new OrderAccpted(itemView);
default:
    itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_order_default, parent, false);
    return new OrderDefault(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        if (beans.get(position).getOrderStatus().equals("order_placed"))
            {
                final OrderItem bean = beans.get(position);
                Glide.with(context).
                        load(context.getResources().getString(R.string.base_url) + "deals/images/" + bean.getPics().get(0).getPicPath())
                        .thumbnail(Glide.with(context).load(R.drawable.loading))
                        .crossFade()
                        .into(((OrderPlaced)viewHolder).image);

                //  viewHolder.image.setImageResource(bean.getPics().get(0).getPicPath());
                ((OrderPlaced)viewHolder).orderday.setText(bean.getCreatedData());
                ((OrderPlaced)viewHolder).ordername.setText(bean.getProduct().get(0).getProductName());
                ((OrderPlaced)viewHolder).qt.setText(bean.getQuantity());
                // viewHolder.date.setText(bean.getCreatedData());
                ((OrderPlaced)viewHolder).idnumber.setText(bean.getOrderId());

                ((OrderPlaced)viewHolder).ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        acceptOrder(bean);
                    }
                });

                ((OrderPlaced)viewHolder).cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCancelDialog(bean);
                    }
                });

            }
            else if (beans.get(position).getOrderStatus().equals("order_accpted")){
            final OrderItem bean = beans.get(position);
            Glide.with(context).
                    load(context.getResources().getString(R.string.base_url) + "deals/images/" + bean.getPics().get(0).getPicPath())
                    .thumbnail(Glide.with(context).load(R.drawable.loading))
                    .crossFade()
                    .into(((OrderAccpted) viewHolder).image);

            //  viewHolder.image.setImageResource(bean.getPics().get(0).getPicPath());
            ((OrderAccpted) viewHolder).orderday.setText(bean.getCreatedData());
            ((OrderAccpted) viewHolder).ordername.setText(bean.getProduct().get(0).getProductName());
            ((OrderAccpted) viewHolder).qt.setText(bean.getQuantity());
            // viewHolder.date.setText(bean.getCreatedData());
            ((OrderAccpted) viewHolder).idnumber.setText(bean.getOrderId());
            ((OrderAccpted) viewHolder).dispatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Dispatch",Toast.LENGTH_SHORT).show();
                    selectWorkere(bean);
                    //dispatchOrder(bean);
                }
            });
           // ((OrderAccpted) viewHolder).status.setText(bean.getOrderStatus());


        }else {
            final OrderItem bean = beans.get(position);
            Glide.with(context).
                    load(context.getResources().getString(R.string.base_url) + "deals/images/" + bean.getPics().get(0).getPicPath())
                    .thumbnail(Glide.with(context).load(R.drawable.loading))
                    .crossFade()
                    .into(((OrderDefault) viewHolder).image);

            //  viewHolder.image.setImageResource(bean.getPics().get(0).getPicPath());
            ((OrderDefault) viewHolder).orderday.setText(bean.getCreatedData());
            ((OrderDefault) viewHolder).ordername.setText(bean.getProduct().get(0).getProductName());
            ((OrderDefault) viewHolder).qt.setText(bean.getQuantity());
            // viewHolder.date.setText(bean.getCreatedData());
            ((OrderDefault) viewHolder).idnumber.setText(bean.getOrderId());
            switch (bean.getOrderStatus()){
                case "order_delivered":{
                    ((OrderDefault) viewHolder).status.setTextColor(context.getResources().getColor(R.color.light_green));
                    ((OrderDefault) viewHolder).status.setText("Order Delivered");
                    break;
                }
                case "order_dispatched":{
                    ((OrderDefault) viewHolder).status.setTextColor(context.getResources().getColor(R.color.light_blue));
                    ((OrderDefault) viewHolder).status.setText("Order Dispatched");
                    break;
                }case "order_declined":{
                    ((OrderDefault) viewHolder).status.setText("Order Declined");
                    break;
                }
              //  default:        ((OrderDefault) viewHolder).status.setText("Order Declined");

            }

        }

        }

    private void cancelOrder(final OrderItem bean, String s) {
        RequestBody OrderId = RequestBody.create(MediaType.parse("text/plain"), bean.getOrderId());
        RequestBody UId = RequestBody.create(MediaType.parse("text/plain"), bean.getUid());
        RequestBody Msg = RequestBody.create(MediaType.parse("text/plain"),s);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        DeclineOrderApi service = new Retrofit.Builder().baseUrl(context.getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(DeclineOrderApi.class);


        retrofit2.Call<MyPojo> mService = service.decline(OrderId,UId,Msg);
        mService.enqueue(new Callback<MyPojo>() {
            @Override
            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {

                if (response.body().getStatus()) {
                    bean.setOrderStatus("order_declined");
                    notifyDataSetChanged();
                }
                // clearFields();
                Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<MyPojo> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });    }

    private void showCancelDialog(final OrderItem bean) {
        MyTextView cancel;
        final MyEditText msg;
        final Dialog catDialog = new BottomSheetDialog(context);
        catDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        catDialog.setCancelable(true);
        catDialog.setContentView(R.layout.decline_order_msg_row);
        catDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        catDialog.show();
        cancel =  catDialog.findViewById(R.id.button_cancel_order);

msg=catDialog.findViewById(R.id.msg);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msg.getText().toString().trim().length()>1){
                    cancelOrder(bean,msg.getText().toString());
                    catDialog.dismiss();

                }else {

msg.setError("PLease Enter A Valid Reason For Order Cancellation");
                }

            }
        });
    }

    private void acceptOrder(final OrderItem bean) {
        RequestBody OrderId = RequestBody.create(MediaType.parse("text/plain"), bean.getOrderId());
        RequestBody UId = RequestBody.create(MediaType.parse("text/plain"), bean.getUid());

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        AcceptOrderApi service = new Retrofit.Builder().baseUrl(context.getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(AcceptOrderApi.class);


        retrofit2.Call<MyPojo> mService = service.accept(OrderId,UId);
        mService.enqueue(new Callback<MyPojo>() {
            @Override
            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {

                if (response.body().getStatus()) {
                    bean.setOrderStatus("order_accpted");
                    notifyDataSetChanged();
                }
                // clearFields();
                Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<MyPojo> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    @Override
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


    private void dispatchOrder(final OrderItem bean, Object o) {
        RequestBody OrderId = RequestBody.create(MediaType.parse("text/plain"), bean.getOrderId());
        RequestBody UId = RequestBody.create(MediaType.parse("text/plain"), bean.getUid());
        Worker worker=(Worker)o;
        RequestBody WId = RequestBody.create(MediaType.parse("text/plain"),worker.getId());

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        DisptchOrderApi service = new Retrofit.Builder().baseUrl(context.getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(DisptchOrderApi.class);


        retrofit2.Call<MyPojo> mService = service.dispath(OrderId,UId,WId);
        mService.enqueue(new Callback<MyPojo>() {
            @Override
            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {

                if (response.body().getStatus()) {
                    bean.setOrderStatus("order_dispatched");
                    notifyDataSetChanged();
                }
                // clearFields();
                Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<MyPojo> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    public void selectWorkere(final OrderItem bean) {
       final Dialog myDialog1;
        myDialog1 = new BottomSheetDialog(context);
        myDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog1.setCancelable(true);
        myDialog1.setContentView(R.layout.select_worker_layout);
        myDialog1.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);

        myDialog1.show();
        RecyclerView recyclerView=myDialog1.findViewById(R.id.worker_rv);
       recyclerView.setLayoutManager(new LinearLayoutManager(context));
       final ArrayList workes= new SessionManager().getWorkerList(context);
       recyclerView.setAdapter(new SelectWorkersAdapter(context,workes));
        recyclerView.addOnItemTouchListener(new RecyclerItemListener(context,recyclerView,new
                RecyclerItemListener.RecyclerTouchListener(){
                    @Override
                    public void onClickItem(View v, int position) {
                       dispatchOrder(bean,workes.get(position));
                        myDialog1.dismiss();
                    }

                    @Override
                    public void onLongClickItem(View v, int position) {

                    }
                }));

    }


}
