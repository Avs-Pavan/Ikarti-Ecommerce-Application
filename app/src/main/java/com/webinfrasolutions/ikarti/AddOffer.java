package com.webinfrasolutions.ikarti;

import android.app.Dialog;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.webinfrasolutions.ikarti.API.AddOffferApi;
import com.webinfrasolutions.ikarti.API.MyProductListApi;
import com.webinfrasolutions.ikarti.API.UpdateOfferApi;
import com.webinfrasolutions.ikarti.Adapters.MultiSelectProductAdapter;
import com.webinfrasolutions.ikarti.Pojo.GetOtpPojo;
import com.webinfrasolutions.ikarti.Pojo.MyproductsListPojo;
import com.webinfrasolutions.ikarti.Pojo.Offer;
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

public class AddOffer extends AppCompatActivity {
TextInputLayout offerPercentageLay;
    EditText offer_et;
    String offer;boolean task=false;
    String offer_id,store_id;
List<Product> list;
    ArrayList<Product> selectedProducts=new ArrayList<>();

    ArrayList<String> pros=new ArrayList<>();

    Dialog myDialog1;
MyTextView select_products_button;
    public void goBack(View view) {
        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);
        MyTextView titl=findViewById(R.id.toolbar_title);
        titl.setText("New Offer");

        select_products_button=findViewById(R.id.select_products_button);
        offerPercentageLay=(TextInputLayout)findViewById(R.id.offer_lay);
        offer_et=findViewById(R.id.offer_et);
        offer_et.addTextChangedListener(new MyTextWatcher(offer_et));
        fetchList();
        try {
            String data=getIntent().getStringExtra("task");
            if (data.equals("edit"))
                task=true;
            // showError(task);
            Offer offer = (Offer) getIntent().getSerializableExtra("data");

            offer_id=offer.getId();
            store_id=offer.getStoreId();
            loadData(offer);
            //showError(product.getProductName());
        }catch (NullPointerException ne){
            ne.printStackTrace();
        }
    }

    private void loadData(Offer offer) {



        offer_et.setText(offer.getOfferPercentage());


    }


    public void addOffer(View view) {
        if (task) {
            if (validateOffer()&&validateCategory()) {
                RequestBody Id = RequestBody.create(MediaType.parse("text/plain"), offer_id);
                RequestBody Offer = RequestBody.create(MediaType.parse("text/plain"), offer_et.getText().toString());
                RequestBody StoreID = RequestBody.create(MediaType.parse("text/plain"), "1");
                RequestBody Url = RequestBody.create(MediaType.parse("text/plain"), "url");


                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
                // Change base URL to your upload server URL.
                UpdateOfferApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(UpdateOfferApi.class);

                retrofit2.Call<GetOtpPojo> mService = service.update(Offer, StoreID, Url,Id);
                mService.enqueue(new Callback<GetOtpPojo>() {
                    @Override
                    public void onResponse(Call<GetOtpPojo> call, Response<GetOtpPojo> response) {
                        GetOtpPojo obj = response.body();
                        if (obj.getStatus())
                            // clearFields();
                            Toast.makeText(AddOffer.this, "" + obj.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void onFailure(Call<GetOtpPojo> call, Throwable t) {
                        Toast.makeText(AddOffer.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
            else {
                validateCategory();

                validateOffer();
            }

        } else {
            if (validateOffer()&&validateCategory()) {

                RequestBody Offer = RequestBody.create(MediaType.parse("text/plain"), offer_et.getText().toString());
                RequestBody StoreID = RequestBody.create(MediaType.parse("text/plain"), "1");
                RequestBody Url = RequestBody.create(MediaType.parse("text/plain"), "url");


                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
                // Change base URL to your upload server URL.
                AddOffferApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(AddOffferApi.class);


                retrofit2.Call<GetOtpPojo> mService = service.add(Offer, StoreID, Url);
                mService.enqueue(new Callback<GetOtpPojo>() {
                    @Override
                    public void onResponse(Call<GetOtpPojo> call, Response<GetOtpPojo> response) {
                        GetOtpPojo obj = response.body();
                        if (obj.getStatus())
                            // clearFields();
                            Toast.makeText(AddOffer.this, "" + obj.getMessage(), Toast.LENGTH_SHORT).show();

                    }


                    @Override
                    public void onFailure(Call<GetOtpPojo> call, Throwable t) {
                        Toast.makeText(AddOffer.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }else {
                validateCategory();

                validateOffer();
            }

        }
    }

    public void selectItems(View view) {


        myDialog1 = new Dialog(AddOffer.this);
        myDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog1.setCancelable(true);
        myDialog1.setContentView(R.layout.select_cat_dialog);
        myDialog1.show();
        TextView button = (TextView) myDialog1.findViewById(R.id.button);
        RecyclerView rv=myDialog1.findViewById(R.id.cat_select_rv);
        MultiSelectProductAdapter adapter = new MultiSelectProductAdapter(getApplicationContext(), list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Product> kk=getSelectedProducts();
                if (kk.size()>0)
                {
                    myDialog1.dismiss();
                   // cat_tv.setTextColor(getResources().getColor(R.color.light_green));
                  //  setSelectedVIewa(kk);

                }else {
                    Toast.makeText(AddOffer.this,"Please Select A Category",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.title_et:
                {
                    validateOffer();
                    break;}


            }
        }
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateOffer() {
        if (offer_et.getText().toString().trim().isEmpty()) {
            offerPercentageLay.setError("Field Cannot Be Empty");
            requestFocus(offer_et);
            return false;
        } else {
            offerPercentageLay.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateCategory(){
//Toast.makeText(RegisterShop.this,list.size()+"",Toast.LENGTH_SHORT).show();
        if (pros.size()<1)
        {
            select_products_button.setTextColor(getResources().getColor(R.color.red));
            requestFocus(select_products_button);
            return false;
        }

        return  true;
    }

    public void fetchList(){
        Storekeeper storekeeper=(Storekeeper)new SessionManager().getStoreKeeperData(AddOffer.this);

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
                    list=response.body().getProduct();


                Toast.makeText(AddOffer.this,""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<MyproductsListPojo> call, Throwable t) {
                Toast.makeText(AddOffer.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }
    public  ArrayList<Product> getSelectedProducts(){
        String text = "";
        for (Product product : list) {
            if (product.isSelected()) {
                if(!selectedProducts.contains(product)) {
                    selectedProducts.add(product);
                    pros.add(product.getCatId());
                }
                text += product.getProductName()+"\n";
            }
        }
        Log.d("TAG", "Output : " + text);

        return  selectedProducts;
    }


}
