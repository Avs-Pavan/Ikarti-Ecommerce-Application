package com.webinfrasolutions.ikarti;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.webinfrasolutions.ikarti.API.AddOrderApi;
import com.webinfrasolutions.ikarti.API.AddProductApi;
import com.webinfrasolutions.ikarti.Pojo.Category;
import com.webinfrasolutions.ikarti.Pojo.GetOtpPojo;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;
import com.webinfrasolutions.ikarti.Pojo.Storekeeper;
import com.webinfrasolutions.ikarti.Pojo.UserData;
import com.webinfrasolutions.ikarti.util.SessionManager;

import customfonts.MyTextView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ThankUActivity extends AppCompatActivity {
String address_id;ImageView imageView;
    MyTextView title,price,des,total_price,shipping,itemcount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_u);
       // Toast.makeText(this,address_id,Toast.LENGTH_SHORT).show();
        title=findViewById(R.id.title);
        price=findViewById(R.id.price);
        des=findViewById(R.id.des);
        itemcount=findViewById(R.id.itemcount);
        total_price=findViewById(R.id.total_price);
        imageView=findViewById(R.id.imageView);
        shipping=findViewById(R.id.shipping_time);
        loadData();
    }

    private void loadData() {
        final SharedPreferences db=getSharedPreferences("order",MODE_PRIVATE);

        price.setText(db.getString("product_price",null));
                    price.setText(db.getString("product_price",null));
        total_price.setText(db.getString("item_total",null));
                    des.setText(db.getString("des",null));
                    itemcount.setText(db.getString("quantity",null)+" X ");
                    title.setText(db.getString("title",null));

        Glide.with(ThankUActivity.this).load(ThankUActivity.this.getResources().getString(R.string.base_url)+"deals/images/"+db.getString("url",null) )
                            .thumbnail(Glide.with(ThankUActivity.this).load(R.drawable.loading))
                            .crossFade()
                            .into(imageView);
    }


    public void goBack(View view) {
        Intent i=new Intent(ThankUActivity.this,CartActivity.class);
        i.putExtra("from","tq");
        startActivity(i);
    }
@Override
    public void onBackPressed(){

    Intent i=new Intent(ThankUActivity.this,CartActivity.class);
    i.putExtra("from","tq");
    startActivity(i);
}
}
