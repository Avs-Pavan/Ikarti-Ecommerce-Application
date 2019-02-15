package com.webinfrasolutions.ikarti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.webinfrasolutions.ikarti.util.SessionManager;


public class WHome extends AppCompatActivity {

    String[] list={"Orders To Deliver","Logout"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whome);
        ListView lv = (ListView) findViewById(R.id.worker_options_lv);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.shop_options_row, list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){

                    case  0:
                    {
                        Intent i=new Intent(WHome.this,WorkerDispathOrderList.class);

                        startActivity(i);
                    }
                    case  1:
                    {
                        Intent i=new Intent(WHome.this,Login.class);
                        new SessionManager().logout(WHome.this);

                        startActivity(i);finish();
                    }

                }
            }
        });

    }

    public void showToast(String msg){
        Toast.makeText(WHome.this,msg,Toast.LENGTH_SHORT).show();
    }

}
