package com.webinfrasolutions.ikarti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.webinfrasolutions.ikarti.API.FetchFavouriteListApi;
import com.webinfrasolutions.ikarti.API.MyProductListApi;
import com.webinfrasolutions.ikarti.Adapters.DashAddsAdapter;
import com.webinfrasolutions.ikarti.Adapters.Utility;
import com.webinfrasolutions.ikarti.Pojo.FavouriteProductsListPojo;
import com.webinfrasolutions.ikarti.Pojo.MyproductsListPojo;
import com.webinfrasolutions.ikarti.Pojo.Product;
import com.webinfrasolutions.ikarti.Pojo.UserData;
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

public class FavouriteProductsActivity extends AppCompatActivity {
RecyclerView rv;    List<Product> myproductlist=new ArrayList<>();
    static LinearLayout error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_adds);
        MyTextView tv=findViewById(R.id.toolbar_title);
        tv.setText("Favourite Products");
        error=findViewById(R.id.emptycart);

        initList();
    }
    public void goBack(View V){
        super.onBackPressed();

    }
    private void initList() {
        rv=(RecyclerView)findViewById(R.id.cat_rv);
        rv.setHasFixedSize(true);

        int mNoOfColumns = Utility.calculateNoOfColumns(getApplicationContext());
    rv.setLayoutManager(new GridLayoutManager(this, mNoOfColumns));

       // rv.setLayoutManager(new LinearLayoutManager(FavouriteProductsActivity.this));
        fetchList();
    }
    public void fetchList(){
        final UserData userData=new SessionManager().getUserData(FavouriteProductsActivity.this);
        RequestBody StoreID = RequestBody.create(MediaType.parse("text/plain"), userData.getUid());

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        FetchFavouriteListApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(FetchFavouriteListApi.class);


        retrofit2. Call<FavouriteProductsListPojo> mService = service.fetch(StoreID);
        mService.enqueue(new Callback<FavouriteProductsListPojo>() {
            @Override
            public void onResponse(Call<FavouriteProductsListPojo> call, Response<FavouriteProductsListPojo> response) {

                if (response.body().getStatus()){
                    if (response.body().getProducts().size()>0){


                    // clearFields();
                    //   Toast.makeText(FavouriteProductsActivity.this,response.body().getProducts()+"",Toast.LENGTH_SHORT).show();
                    myproductlist=response.body().getProducts();
                    rv.setAdapter(new DashAddsAdapter(FavouriteProductsActivity.this,myproductlist,userData.getUid()));
                    //  rv.setAdapter(new CatListAdapter(OfferDetailsActivity.this,OfferDetailsActivity.this,food));

                    //Toast.makeText(FavouriteProductsActivity.this,""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    else {
                        error.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                    }
                }
                     }


            @Override
            public void onFailure(Call<FavouriteProductsListPojo> call, Throwable t) {
                Toast.makeText(FavouriteProductsActivity.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

}
