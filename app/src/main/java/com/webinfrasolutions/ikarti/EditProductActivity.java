package com.webinfrasolutions.ikarti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class EditProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
    }

    public void goBack(View view) {
        super.onBackPressed();
    }
}
