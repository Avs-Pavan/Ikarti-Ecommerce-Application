package com.webinfrasolutions.ikarti;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.webinfrasolutions.ikarti.Pojo.CartItem;

import java.text.DecimalFormat;

import customfonts.MyTextView;

public class PlaceOrderActivity extends AppCompatActivity {
    ImageView plus,minus;
    TextView cartno;
    MyTextView  total,discount,stotal,shipping,payable;
    CartItem item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
initView();
        loadData();
    }
    public  void loadData(){
        total=findViewById(R.id.total);
        discount=findViewById(R.id.discount);
        stotal=findViewById(R.id.stotal);
        shipping=findViewById(R.id.deliveryfree);
        payable=findViewById(R.id.payable);
       item=(CartItem) getIntent().getSerializableExtra("record");
        total.setText(priceToRupees(Double.parseDouble(item.getProductPrice())));
        stotal.setText(priceToRupees(Double.parseDouble(item.getProductPrice())));
        payable.setText(priceToRupees(Double.parseDouble(item.getProductPrice())));
    }
public  void initView(){

    //********PRODUCT PLUS MINUS*************
    plus = (ImageView)findViewById(R.id.plus);
    minus = (ImageView)findViewById(R.id.minus);
    cartno = (TextView) findViewById(R.id.cartno);

    final int[] number = {1};
    cartno.setText(""+1);

    minus.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (number[0] >1){

                number[0] = number[0] -1;
                cartno.setText(""+ number[0]);
                int ItemCount=Integer.parseInt(cartno.getText().toString());
                float price=Float.parseFloat(item.getProductPrice());
                payable.setText(priceToRupees(Double.parseDouble(ItemCount*price+"")));

               // payable.setText(number[0]*Float.parseFloat(item.getProductPrice())+"");

            }
        }
    });
    plus.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


                number[0] = number[0] + 1;
                cartno.setText("" + number[0]);
            int ItemCount=Integer.parseInt(cartno.getText().toString());
            float price=Float.parseFloat(item.getProductPrice());
            payable.setText(priceToRupees(Double.parseDouble(ItemCount*price+"")));



        }
    });
}



    public void placeOrder(View view) {

      /*  SharedPreferences db=getSharedPreferences("order",MODE_PRIVATE);
        SharedPreferences.Editor editor=db.edit();

        editor.putString("pid",item.getProductId());
        editor.putString("quantity",cartno.getText().toString());
        editor.putString("product_deal_price",stotal.getText().toString());
        editor.putString("product_price",item.getProductPrice());
        editor.putString("save_price","100");
        editor.putString("item_total",payable.getText().toString());
        editor.putString("store_id",item.getStoreId());
        editor.apply();
        Intent i=new Intent(PlaceOrderActivity.this,AddressActivity.class);
        startActivity(i);*/
      showDialog();

    }
    public static String priceToRupees(Double price) {
        DecimalFormat formatter = new DecimalFormat("##,##,###.00");
        return formatter.format(price);
    }
    public void showDialog(){
        MyTextView titletv,pricetv,ok,cancel,piececount,totalprice;
        final Dialog catDialog = new BottomSheetDialog(PlaceOrderActivity.this);
        catDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        catDialog.setCancelable(true);
        catDialog.setContentView(R.layout.place_order_dialog);
        catDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        catDialog.show();
        ok =  catDialog.findViewById(R.id.button_ok);
        cancel =  catDialog.findViewById(R.id.button_cancel);

        titletv =  catDialog.findViewById(R.id.title);
        pricetv =  catDialog.findViewById(R.id.price);
        piececount =  catDialog.findViewById(R.id.piece_count);
        totalprice =  catDialog.findViewById(R.id.total_price);
ImageView imageView=catDialog.findViewById(R.id.image);
        Glide
                .with(PlaceOrderActivity.this)
                .load(getResources().getString(R.string.base_url)+"deals/images/"+item.getPics().get(0).getPicPath())
                .centerCrop()
                .error(R.drawable.cloth)
                .placeholder(R.drawable.loading)
                .into(imageView);
        pricetv.setText(item.getProductPrice());
        titletv.setText(item.getProductName());
        piececount.setText(cartno.getText().toString()+" Pieces");
        //int ItemCount=Integer.parseInt(cartno.getText().toString());
        //float price=Float.parseFloat(item.getProductPrice());
        totalprice.setText(payable.getText().toString());

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete(cartId,position);
                  SharedPreferences db=getSharedPreferences("order",MODE_PRIVATE);
        SharedPreferences.Editor editor=db.edit();

        editor.putString("pid",item.getProductId());
        editor.putString("quantity",cartno.getText().toString());
        editor.putString("product_deal_price",item.getProductPrice());
        editor.putString("product_price",item.getProductPrice());
        editor.putString("save_price","100");
        editor.putString("title",item.getProductName());
        editor.putString("des",item.getProductDescription());
        editor.putString("url",item.getPics().get(0).getPicPath());
        editor.putString("item_total",Float.parseFloat(item.getProductPrice())*Float.parseFloat(cartno.getText().toString())+"");
        editor.putString("store_id",item.getStoreId());
                Log.i("pid",item.getProductId());
                Log.i("quantity",cartno.getText().toString());
                Log.i("product_deal_price",item.getProductPrice());
                Log.i("product_price",item.getProductPrice());
                Log.i("save_price","100");
                Log.i("title",item.getProductName());
                Log.i("des",item.getProductDescription());
                Log.i("url",item.getPics().get(0).getPicPath());
                Log.i("item_total",Float.parseFloat(item.getProductPrice())*Float.parseFloat(cartno.getText().toString())+"");
                Log.i("store_id",item.getStoreId());
        editor.apply();
        Intent i=new Intent(PlaceOrderActivity.this,AddressActivity.class);
        startActivity(i);
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
