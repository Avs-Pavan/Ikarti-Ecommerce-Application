package com.webinfrasolutions.ikarti;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.webinfrasolutions.ikarti.API.DeleteCartItemApi;
import com.webinfrasolutions.ikarti.API.FetchMyCartListApi;
import com.webinfrasolutions.ikarti.Pojo.CartItem;
import com.webinfrasolutions.ikarti.Pojo.CartListPojo;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;
import com.webinfrasolutions.ikarti.Pojo.Pic;
import com.webinfrasolutions.ikarti.Pojo.UserData;
import com.webinfrasolutions.ikarti.util.SessionManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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

public class CartActivity extends AppCompatActivity {
    static RecyclerView rv;
    static LinearLayout error;
    String from = "";

    public void goBack(View view) {
        if (from.equals("tq")) {
            Intent i = new Intent(CartActivity.this, Uhome.class);
            startActivity(i);

        } else super.onBackPressed();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        error = findViewById(R.id.emptycart);

        try {
            from = getIntent().getStringExtra("from");
            if ((from.equals("tq"))) from = "tq";
        } catch (NullPointerException n) {
            from = "";
        }
        rv = findViewById(R.id.cart_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        UserData userData = new SessionManager().getUserData(CartActivity.this);
        fetchList(userData.getUid());
    }

    private void fetchList(String uid) {

        RequestBody Uid = RequestBody.create(MediaType.parse("text/plain"), uid);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        FetchMyCartListApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(FetchMyCartListApi.class);


        retrofit2.Call<CartListPojo> mService = service.fetch(Uid);
        mService.enqueue(new Callback<CartListPojo>() {
            @Override
            public void onResponse(Call<CartListPojo> call, Response<CartListPojo> response) {

                if (response.body().getStatus())

                    if (response.body().getCartList().size() < 1) {

                        error.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                    } else
                        rv.setAdapter(new CartAdapter(CartActivity.this, CartActivity.this, response.body().getCartList()));
                // Toast.makeText(CartActivity.this,""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<CartListPojo> call, Throwable t) {
                Toast.makeText(CartActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (from.equals("tq")) {
            Intent intent = new Intent(CartActivity.this, Uhome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else super.onBackPressed();

    }


    /**
     * Created by kevin on 12/8/17.
     */

    public static class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context context;
        Activity activity;
        List<CartItem> records = new ArrayList<CartItem>();

        public CartAdapter(Activity activity, Context contexts, List<CartItem> records) {
            this.context = contexts;
            this.activity = activity;
            this.records = records;
        }

        public class PlaceOrderView extends RecyclerView.ViewHolder {
            public MyTextView titletv, pricetv, placeOrderBtn;
            ImageView deleteCartButton, imageView;

            public PlaceOrderView(View view) {
                super(view);
                titletv = view.findViewById(R.id.title);
                pricetv = view.findViewById(R.id.price);
                deleteCartButton = view.findViewById(R.id.deleteCartButton);
                imageView = view.findViewById(R.id.image);
                placeOrderBtn = view.findViewById(R.id.placeorder_btn);

                setIsRecyclable(false);
            }

        }

        public class StorePickupView extends RecyclerView.ViewHolder {
            public MyTextView titletv, pricetv, storepickup;
            ImageView deleteCartButton, imageView;

            public StorePickupView(View view) {
                super(view);
                titletv = view.findViewById(R.id.title);
                pricetv = view.findViewById(R.id.price);
                deleteCartButton = view.findViewById(R.id.deleteCartButton);
                imageView = view.findViewById(R.id.image);
                storepickup = view.findViewById(R.id.store_pickup_btn);

                setIsRecyclable(false);
            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;

            switch (viewType) {
                case 0:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_place_order_row, parent, false);
                    return new PlaceOrderView(view);
                case 1:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_store_pickup_row, parent, false);
                    return new StorePickupView(view);
            }
            return null;
        }

        @Override
        public int getItemViewType(int position) {

            switch (records.get(position).getDelivery()) {
                case "true":
                    return 0;
                case "false":
                    return 1;

                default:
                    return -1;
            }


        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            switch (records.get(position).getDelivery()) {

                case "true": {

                    ((PlaceOrderView) holder).deleteCartButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //   Toast.makeText(context,"delete",Toast.LENGTH_SHORT).show();
//delete(records.get(position).getCartId(),position);

                            showDialog(records.get(position).getCartId(), position);
                        }
                    });
                    ((PlaceOrderView) holder).placeOrderBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("record", records.get(position));
                            Intent mainIntent = new Intent(context, PlaceOrderActivity.class);
                            //add bundle to intent
                            mainIntent.putExtras(bundle);
                            //start activity
                            context.startActivity(mainIntent);
                        }
                    });
                    ((PlaceOrderView) holder).titletv.setText(records.get(position).getProductName());
                    Pic pic = records.get(position).getPics().get(0);
                    Glide.with(context).
                            load(context.getResources().getString(R.string.base_url) + "deals/images/" + pic.getPicPath())
                            .thumbnail(Glide.with(context).load(R.drawable.loading))
                            .crossFade()
                            .into(((PlaceOrderView) holder).imageView);
                    ((PlaceOrderView) holder).pricetv.setText(priceToRupees(Double.parseDouble(records.get(position).getProductPrice())));
                    break;
                }
                case "false": {

                    ((StorePickupView) holder).deleteCartButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //   Toast.makeText(context,"delete",Toast.LENGTH_SHORT).show();
//delete(records.get(position).getCartId(),position);

                            showDialog(records.get(position).getCartId(), position);
                        }
                    });
                    ((StorePickupView) holder).storepickup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("record", records.get(position));
                            Intent mainIntent = new Intent(context, StorePickup.class);
                            //add bundle to intent
                            mainIntent.putExtras(bundle);
                            //start activity
                            context.startActivity(mainIntent);
                            //Toast.makeText(context,"store_pickup",Toast.LENGTH_SHORT).show();
                        }
                    });
                    ((StorePickupView) holder).titletv.setText(records.get(position).getProductName());
                    Pic pic = records.get(position).getPics().get(0);
                    Glide.with(context).
                            load(context.getResources().getString(R.string.base_url) + "deals/images/" + pic.getPicPath())
                            .thumbnail(Glide.with(context).load(R.drawable.loading))
                            .crossFade()
                            .into(((StorePickupView) holder).imageView);
                    ((StorePickupView) holder).pricetv.setText(priceToRupees(Double.parseDouble(records.get(position).getProductPrice())));

                    break;

                }


            }


        }


        public static String priceToRupees(Double price) {
            DecimalFormat formatter = new DecimalFormat("##,##,###.00");
            return formatter.format(price);
        }

        @Override
        public int getItemCount() {
            return records.size();
        }

        public void delete(String cart_id, final int position) {

            RequestBody CartId = RequestBody.create(MediaType.parse("text/plain"), cart_id);

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            // Change base URL to your upload server URL.
            DeleteCartItemApi service = new Retrofit.Builder().baseUrl(context.getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(DeleteCartItemApi.class);


            retrofit2.Call<MyPojo> mService = service.delete(CartId);
            mService.enqueue(new Callback<MyPojo>() {
                @Override
                public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {

                    if (response.body().getStatus()) {
                        records.remove(position);
                        if (records.size() < 1) {
                            error.setVisibility(View.VISIBLE);
                            rv.setVisibility(View.GONE);

                        }
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

        public void showDialog(final String cartId, final int position) {
            MyTextView titletv, pricetv, ok, cancel;
            ImageView imageView;
            final Dialog catDialog = new BottomSheetDialog(context);
            catDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            catDialog.setCancelable(true);
            catDialog.setContentView(R.layout.deelte_from_cart_dialog);
            catDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
            catDialog.show();
            ok = catDialog.findViewById(R.id.button_ok);
            cancel = catDialog.findViewById(R.id.button_cancel);

            titletv = catDialog.findViewById(R.id.title);
            pricetv = catDialog.findViewById(R.id.price);
            imageView = catDialog.findViewById(R.id.image);
            pricetv.setText(priceToRupees(Double.parseDouble(records.get(position).getProductPrice())));
            titletv.setText(records.get(position).getProductName());
            Pic pic = records.get(position).getPics().get(0);

            Glide.with(context).
                    load(context.getResources().getString(R.string.base_url) + "deals/images/" + pic.getPicPath())
                    .thumbnail(Glide.with(context).load(R.drawable.loading))
                    .crossFade()
                    .into(imageView);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(cartId, position);
                    catDialog.dismiss();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    catDialog.dismiss();
                }
            });

        }

    }


}
