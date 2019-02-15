package com.webinfrasolutions.ikarti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.webinfrasolutions.ikarti.Pojo.Storekeeper;
import com.webinfrasolutions.ikarti.util.SessionManager;

import customfonts.MyTextView;

public class Shome extends AppCompatActivity {

    String[] list={
            "Orders",
            "New Product"
            ,"My Products",
           // ,"New Offer"
          //  ,"My Offers",
            "Store Workers",
            "Logout"

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shome);

        ListView  lv= (ListView)findViewById(R.id.shop_options_lv);
        ArrayAdapter adapter=new ArrayAdapter(this,R.layout.shop_options_row,list);
        lv.setAdapter(adapter);
        MyTextView tv=findViewById(R.id.shop_name);

        Storekeeper storekeeper=(Storekeeper) new SessionManager().getStoreKeeperData(Shome.this);
        tv.setText(storekeeper.getName());
lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //showToast(list[position]);
    switch (position){
        case 0:{
            Intent i=new Intent(Shome.this,StoreOrders.class);
            startActivity(i);
            break;
        }
        case 1:{
            Intent i=new Intent(Shome.this,Addproduct.class);
            startActivity(i);
            break;
        } case 10:{
            Intent i=new Intent(Shome.this,AddOffer.class);
            startActivity(i);
            break;
        }
        case 2:{
            Intent i=new Intent(Shome.this,MyProducts.class);
            startActivity(i);
            break;
        }
        case 11:{
            Intent i=new Intent(Shome.this,MyOffers.class);
            startActivity(i);
            break;
        }
        case 4:{
            Intent i=new Intent(Shome.this,Login.class);
            new SessionManager().logout(Shome.this);
            startActivity(i);finish();
            break;
        }
        case 3:{
            Intent i=new Intent(Shome.this,StoreWorkers.class);
            startActivity(i);
            break;
        }
    }

    }
});
    }
    public void showToast(String msg){
        Toast.makeText(Shome.this,msg,Toast.LENGTH_SHORT).show();
    }



}
