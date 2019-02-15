package com.webinfrasolutions.ikarti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import customfonts.MyTextView;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        MyTextView tv=findViewById(R.id.toolbar_title);
        tv.setText("Notifications");
    }
    public void goHome(View V){
        super.onBackPressed();

    }
}
