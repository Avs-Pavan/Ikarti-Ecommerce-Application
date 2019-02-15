package com.webinfrasolutions.ikarti;

import android.app.NotificationManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.webinfrasolutions.ikarti.Pojo.OrderItem;

public class StoreOrderNotificationActivity extends AppCompatActivity {
    ImageView image;
    TextView orderday;
    TextView ordername;
    TextView qt;
    TextView idnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order_notification);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Dismiss Notification
        notificationmanager.cancel(0);

        // Retrive the data from MainActivity.java
        Intent i = getIntent();

        image = (ImageView)findViewById(R.id.image);
        orderday = (TextView)findViewById(R.id.orderday);
        ordername = (TextView)findViewById(R.id.ordername);
        qt = (TextView)findViewById(R.id.qt);
        //date = (TextView)findViewById(R.id.date);
        idnumber = (TextView)findViewById(R.id.idnumber);


        OrderItem bean =(OrderItem) i.getSerializableExtra("record");

        Glide.with(StoreOrderNotificationActivity.this).
                load(getResources().getString(R.string.base_url)+"deals/images/"+bean.getPics().get(0).getPicPath())
                .thumbnail(Glide.with(StoreOrderNotificationActivity.this).load(R.drawable.loading))
                .crossFade()
                .into(image);

        //  viewHolder.image.setImageResource(bean.getPics().get(0).getPicPath());
        orderday.setText(bean.getCreatedData());
        ordername.setText(bean.getProduct().get(0).getProductName());
        qt.setText(bean.getQuantity());
      //  date.setText(bean.getCreatedData());
        idnumber.setText(bean.getOrderId());


    }
}
