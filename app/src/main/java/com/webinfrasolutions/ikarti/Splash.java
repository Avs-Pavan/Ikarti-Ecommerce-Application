package com.webinfrasolutions.ikarti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.webinfrasolutions.ikarti.util.SessionManager;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.i("login status",new SessionManager().isLoggedIn(this)+"");
        //System.out.print();

if (new SessionManager().isLoggedIn(this)){
    switch (new SessionManager().getUserType(this)){

        case 0:
        {
            Intent i=new Intent(this,Shome.class);
            Log.i("state","store");

            startActivity(i);finish();break;
        }
        case 1:
        {   Intent i=new Intent(this,WHome.class);
            Log.i("state","worker");

            startActivity(i);finish();break;
        }
        case 2:
        {
            Intent i=new Intent(this,Uhome.class);
            Log.i("state","user");

            startActivity(i);finish();break;
        }

    }//
}
else {
    Intent i=new Intent(this,Login.class);
    startActivity(i);finish();

}
    }
}
