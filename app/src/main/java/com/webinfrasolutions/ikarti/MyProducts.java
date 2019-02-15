package com.webinfrasolutions.ikarti;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.webinfrasolutions.ikarti.API.DeleteProductApi;
import com.webinfrasolutions.ikarti.API.MyProductListApi;
import com.webinfrasolutions.ikarti.Adapters.OrdersAdapter;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;
import com.webinfrasolutions.ikarti.Pojo.MyproductsListPojo;
import com.webinfrasolutions.ikarti.Pojo.Product;
import com.webinfrasolutions.ikarti.Pojo.Storekeeper;
import com.webinfrasolutions.ikarti.util.SessionManager;

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

public class MyProducts extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);
        MyTextView title=(MyTextView)findViewById(R.id.toolbar_title);
        title.setText("My Product's");
        title.setTextColor(getResources().getColor(R.color.light_blue));
        fetchList();
         recyclerView    = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    public void goBack(View view) {
        super.onBackPressed();
    }
    private class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder>  {
        private static final int UNSELECTED = -1;

        private RecyclerView recyclerView;
        private int selectedItem = UNSELECTED;

        public SimpleAdapter(RecyclerView recyclerView, List<Product> myproductlist) {
            this.recyclerView = recyclerView;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.expandable_row, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return myproductlist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener {
            private ExpandableLayout expandableLayout;
            private TextView expandButton;

            private int position;final ImageView image,preview_img;
            LinearLayout edit,delete,preview;

            public ViewHolder(View itemView) {
                super(itemView);

                expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandable_layout);
                expandableLayout.setInterpolator(new OvershootInterpolator());
                expandableLayout.setOnExpansionUpdateListener(this);
                expandButton = (TextView) itemView.findViewById(R.id.expand_button);
edit=(LinearLayout) itemView.findViewById(R.id.edit);
delete=(LinearLayout)itemView.findViewById(R.id.delete);
preview_img=(ImageView) itemView.findViewById(R.id.image);
preview=(LinearLayout)itemView.findViewById(R.id.preview);
                image = (ImageView) itemView.findViewById(R.id.edit_img);

                expandButton.setOnClickListener(this);
            }

            public void bind(final int position) {
             //   Resources res = getResources();
             //   final int newColor = res.getColor(R.color.light_blue);
             //   image.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                expandButton.setText(myproductlist.get(position).getProductName());
                Context context=MyProducts.this;
                Glide.with(context).
                        load(context.getResources().getString(R.string.base_url)+"deals/images/"+myproductlist.get(0).getPics().get(0).getPicPath())
                        .thumbnail(Glide.with(context).load(R.drawable.loading))
                        .crossFade()
                        .into(preview_img);

                this.position = position;
                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(MyProducts.this,"edit  "+position,Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(MyProducts.this,Addproduct.class);
                            i.putExtra("task","edit");
i.putExtra("data",myproductlist.get(position));
                            startActivity(i);
                        }
                    });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Toast.makeText(MyProducts.this,"delete  "+position,Toast.LENGTH_SHORT).show();

                        RequestBody Product_Id = RequestBody.create(MediaType.parse("text/plain"), myproductlist.get(position).getProductId());

                        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
                        // Change base URL to your upload server URL.
                        DeleteProductApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(DeleteProductApi.class);


                        retrofit2. Call<MyPojo> mService = service.delete(Product_Id);
                        mService.enqueue(new Callback<MyPojo>() {
                            @Override
                            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {

                                if (response.body().getStatus()) {
                                    myproductlist.remove(position);
                                    notifyDataSetChanged();
                                }
                                    // clearFields();
                                Toast.makeText(MyProducts.this,""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
                            }


                            @Override
                            public void onFailure(Call<MyPojo> call, Throwable t) {
                                Toast.makeText(MyProducts.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });



                    }
                });
                preview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MyProducts.this,"preview  " +position+
                                "",Toast.LENGTH_SHORT).show();


                    }
                });
             //   expandButton.setText("Product Number "+position);

                expandButton.setSelected(false);
                expandableLayout.collapse(false);
            }

            @Override
            public void onClick(View view) {
                ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
                if (holder != null) {
                    holder.expandButton.setSelected(false);
                    holder.expandableLayout.collapse();
                }

                if (position == selectedItem) {
                    selectedItem = UNSELECTED;
                } else {
                    expandButton.setSelected(true);
                    expandableLayout.expand();
                    selectedItem = position;
                }
            }

            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                Log.d("ExpandableLayout", "State: " + state);
                if (state != ExpandableLayout.State.COLLAPSED) {
                    recyclerView.smoothScrollToPosition(getAdapterPosition());
                }
            }
        }
    }
    List<Product> myproductlist=new ArrayList<>();

    public void fetchList(){
        Storekeeper storekeeper=new SessionManager().getStoreKeeperData(MyProducts.this);
        RequestBody StoreID = RequestBody.create(MediaType.parse("text/plain"), storekeeper.getStoreId());

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        MyProductListApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(MyProductListApi.class);


        retrofit2. Call<MyproductsListPojo> mService = service.fetch(StoreID);
        mService.enqueue(new Callback<MyproductsListPojo>() {
            @Override
            public void onResponse(Call<MyproductsListPojo> call, Response<MyproductsListPojo> response) {

                if (response.body().getStatus())
                    // clearFields();
                myproductlist=response.body().getProduct();
                recyclerView.setAdapter(new SimpleAdapter(recyclerView,myproductlist));

                //Toast.makeText(MyProducts.this,""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<MyproductsListPojo> call, Throwable t) {
                Toast.makeText(MyProducts.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }
    }


