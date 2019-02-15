package com.webinfrasolutions.ikarti;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.webinfrasolutions.ikarti.API.FetchcategoriesApi;
import com.webinfrasolutions.ikarti.API.RegisterMerchantApi;
import com.webinfrasolutions.ikarti.Adapters.MultiSelectAdapterCategory;
import com.webinfrasolutions.ikarti.Adapters.SelectesCatAdapter;
import com.webinfrasolutions.ikarti.Pojo.Category;
import com.webinfrasolutions.ikarti.Pojo.CategoryListPojo;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;
import com.yayandroid.locationmanager.LocationBaseActivity;
import com.yayandroid.locationmanager.LocationConfiguration;
import com.yayandroid.locationmanager.LocationManager;
import com.yayandroid.locationmanager.constants.FailType;
import com.yayandroid.locationmanager.constants.LogType;
import com.yayandroid.locationmanager.constants.ProviderType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import customfonts.MyEditText;
import customfonts.MyTextView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterShop extends LocationBaseActivity implements TimePickerDialog.OnTimeSetListener,
        GoogleMap.OnMyLocationButtonClickListener,
 OnMapReadyCallback {
    CircleImageView image;
    private TimePickerDialog tpd;
    int timetype = 0;
    ArrayList<String> cats=new ArrayList<>();
ArrayList<Category> selectedcats=new ArrayList<>();
    MyTextView open, close, lat_tv, long_tv,cat_tv;
    private GoogleMap mMap;
    CheckBox delivery;
RecyclerView selected_rv;
    List<Category> list=new ArrayList<>();
    private static final int  LOCATION_ACCESS = 2;
    Dialog myDialog1;
    String latitude, longitude, openTime, closeTime, category;
    TextInputLayout shopNameLay, ownerNameLay, primaryContactLay, secondaryContactLay, addresssLay;
    EditText shopNameEt, ownerNameEt, primaryContactEt, secondaryContactEt, addresssEt;
    public void register(View view) {

        // registerCall(shopNameEt.getText().toString(), ownerNameEt.getText().toString(), primaryContactEt.getText().toString(), secondaryContactEt.getText().toString(), addresssEt.getText().toString(), latitude, longitude, openTime, closeTime, category);

        if (validateShopNAme()&&validateOwnerNAme()&&validatePrimaryContact()
                &&validateAddress()&&validateCategory()){

            registerCall(shopNameEt.getText().toString(),
                    ownerNameEt.getText().toString(),
                    primaryContactEt.getText().toString(),
                    secondaryContactEt.getText().toString(),
                    addresssEt.getText().toString(),latitude,longitude,openTime,closeTime,category
            );

        }
        else {


            validateCloseTime();
            validateOpenTime();
            validateCategory();
          //  validateSecondaryContact();
            validatePrimaryContact();



            validateOwnerNAme();
            validateShopNAme();

        }

    }


    private void registerCall(String Sshopname, String Sownername, String sprimsary, String ssecondary, String saadress, String slatitude, String slongitude, String sopenTime, String scloseTime, String scategory) {


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        RegisterMerchantApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(RegisterMerchantApi.class);
        boolean bdelivery;
        if (delivery.isChecked())
            bdelivery=true;
        else
            bdelivery=false;


        retrofit2. Call<MyPojo> mService = service.login(Sshopname,Sownername,sprimsary,ssecondary,
                saadress,slatitude,slongitude,sopenTime,scloseTime
                ,scategory,"holidaynote",bdelivery+"",prepareArray(getCat()));
        mService.enqueue(new Callback<MyPojo>() {
            @Override
            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {
                MyPojo obj=response.body();
                if (obj.getStatus())
                {
                    Intent i=new Intent(RegisterShop.this,Login.class);
                    startActivity(i);finish();
                }
                Toast.makeText(RegisterShop.this,""+obj.getMessage(),Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onFailure(Call<MyPojo> call, Throwable t) {
                Toast.makeText(RegisterShop.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public LocationConfiguration getLocationConfiguration() {
        return new LocationConfiguration()
                .keepTracking(false)
                .askForGooglePlayServices(true)
                .setMinAccuracy(200.0f)
                .setWaitPeriod(ProviderType.GOOGLE_PLAY_SERVICES, 5 * 1000)
                .setWaitPeriod(ProviderType.GPS, 10 * 1000)
                .setWaitPeriod(ProviderType.NETWORK, 5 * 1000)
                .setGPSMessage("Please TurnOn Gps?")
                .setRationalMessage("Please Give  the permission To Use The App");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getLocationManager().isWaitingForLocation()
                && !getLocationManager().isAnyDialogShowing()) {
            // displayProgress();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        /// dismissProgress();
    }

    @Override
    public void onLocationFailed(int failType) {
        switch (failType) {
            case FailType.PERMISSION_DENIED: {
                Toast.makeText(RegisterShop.this, "Couldn't get location, because user didn't give permission!", Toast.LENGTH_SHORT).show();
                getLocation();
                break;
            }
            case FailType.GP_SERVICES_NOT_AVAILABLE:
            case FailType.GP_SERVICES_CONNECTION_FAIL: {
                Toast.makeText(RegisterShop.this, "Couldn't get location, because Google Play Services not available!", Toast.LENGTH_SHORT).show();

                getLocation();
                break;
            }
            case FailType.NETWORK_NOT_AVAILABLE: {
                Toast.makeText(RegisterShop.this, "Couldn't get location, because network is not accessible!", Toast.LENGTH_SHORT).show();
                getLocation();

                break;
            }
            case FailType.TIMEOUT: {
                Toast.makeText(RegisterShop.this, "Couldn't get location, and timeout!", Toast.LENGTH_SHORT).show();
                getLocation();

                break;
            }
            case FailType.GP_SERVICES_SETTINGS_DENIED: {
                Toast.makeText(RegisterShop.this, "Couldn't get location, because user didn't activate providers via settingsApi!", Toast.LENGTH_SHORT).show();
                getLocation();

                break;
            }
            case FailType.GP_SERVICES_SETTINGS_DIALOG: {
                Toast.makeText(RegisterShop.this, "Couldn't display settingsApi dialog!", Toast.LENGTH_SHORT).show();

                getLocation();
                break;
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Toast.makeText(RegisterShop.this, "location changed", Toast.LENGTH_SHORT).show();

        try {
            mMap.clear();
            lat_tv.setText(location.getLatitude() + "");
            long_tv.setText(location.getLongitude() + "");
            latitude = location.getLatitude() + "";
            longitude = location.getLongitude() + "";
            LatLng myloc = new LatLng(location.getLatitude(), location.getLongitude());
            MyTextView tv = findViewById(R.id.loc_msg);
            tv.setVisibility(View.VISIBLE);
            mMap.addMarker(new MarkerOptions().position(myloc).title("Your Present Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myloc));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));

        } catch (NullPointerException n) {
            // n.printStackTrace();
        }

    }

    private boolean checkAndRequestPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);


        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), LOCATION_ACCESS);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_ACCESS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Permission Granted Successfully. Write working code here.
                    get_location();
                    initMaps();
                } else {
                    Toast.makeText(RegisterShop.this, "U Should Give Location Access To Use This Application", Toast.LENGTH_SHORT).show();
                    checkAndRequestPermissions();
                    //You did not accept the request can not use the functionality.
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shop);
        Resources res = getResources();
        image = findViewById(R.id.profile_imview);
        open = findViewById(R.id.open_time_tv);
        lat_tv = findViewById(R.id.lat_tv);
        long_tv = findViewById(R.id.long_tv);
        cat_tv = findViewById(R.id.cat_tv);
        selected_rv=findViewById(R.id.selected_rv);
        delivery=findViewById(R.id.home_delivery);
fetchCategories();
        close = findViewById(R.id.close_time_tv);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterShop.this, MapsActivity.class);
                startActivity(i);
            }
        });

        if (Build.VERSION.SDK_INT < 23) {
            //Do not need to check the permission

            get_location();
            initMaps();


        } else {
            if (checkAndRequestPermissions()) {

                get_location();
                initMaps();
                //If you have already permitted the permission
            } else {

                // askpermission();
            }
        }

        initEts();

    }


    public void initMaps() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Getting a reference to the map
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        if (timetype == 1){
            open.setText(hourOfDay + ":" + minute);
        openTime=hourOfDay + ":" + minute;
        if (validateOpenTime())
open.setTextColor(getResources().getColor(R.color.light_green));
        }
        else if (timetype == 2) {
            close.setText((hourOfDay <= 12 ? hourOfDay : hourOfDay - 12) + ":" + minute);
            closeTime=(hourOfDay <= 12 ? hourOfDay : hourOfDay - 12) + ":" + minute;
            if (validateCloseTime())
                close.setTextColor(getResources().getColor(R.color.light_green));


        }
    }

    public void opneTime(View view) {
        timetype = 1;
        Calendar now = Calendar.getInstance();
                /*
                It is recommended to always create a new instance whenever you need to show a Dialog.
                The sample app is reusing them because it is useful when looking for regressions
                during testing
                 */
        if (tpd == null) {
            tpd = TimePickerDialog.newInstance(
                    RegisterShop.this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE), false
            );
        } else {
            tpd.initialize(
                    RegisterShop.this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    now.get(Calendar.SECOND),
                    false);
        }
        // tpd.setThemeDark(modeDarkTime.isChecked());
        tpd.vibrate(true);
        tpd.setCancelColor(getResources().getColor(R.color.grey));
        tpd.dismissOnPause(true);
        tpd.enableSeconds(false);
        tpd.setVersion(false ? TimePickerDialog.Version.VERSION_2 : TimePickerDialog.Version.VERSION_1);
        // tpd.setAccentColor(Color.parseColor("#9C27B0"));
        tpd.setTitle("Opening  Time");
        // tpd.setDisabledTimes(disabledTimes);
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                //Log.d("TimePicker", "Dialog was cancelled");
            }
        });
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }


    public void closeTime(View view) {
        timetype = 2;
        Calendar now = Calendar.getInstance();
                /*
                It is recommended to always create a new instance whenever you need to show a Dialog.
                The sample app is reusing them because it is useful when looking for regressions
                during testing
                 */
        if (tpd == null) {
            tpd = TimePickerDialog.newInstance(
                    RegisterShop.this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE), false
            );
        } else {
            tpd.initialize(
                    RegisterShop.this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    now.get(Calendar.SECOND),
                    false);
        }
        // tpd.setThemeDark(modeDarkTime.isChecked());
        tpd.vibrate(true);
        tpd.setCancelColor(getResources().getColor(R.color.grey));
        tpd.dismissOnPause(true);
        tpd.enableSeconds(false);
        tpd.setVersion(false ? TimePickerDialog.Version.VERSION_2 : TimePickerDialog.Version.VERSION_1);
        // tpd.setAccentColor(Color.parseColor("#9C27B0"));
        tpd.setTitle("Closing Time");
        // tpd.setDisabledTimes(disabledTimes);
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                //Log.d("TimePicker", "Dialog was cancelled");
            }
        });
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        // mMap.setMinZoomPreference(16.0f);
        //   mMap.setMaxZoomPreference(19.0f);

        // mMap.animateCamera( CameraUpdateFactory.zoomTo( 15.0f ) );
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
      //  mMap.setOnMyLocationClickListener(this);

        //  mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
      //  mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
       // mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        // Setting a click event handler for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
            mMap.clear();
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(point.latitude, point.longitude)).title("Your New Location");

                mMap.addMarker(marker);
                latitude = point.latitude+"";
                longitude = point.longitude+"";

                lat_tv.setText(point.latitude+"");
                long_tv.setText(point.longitude+"");
                System.out.println(point.latitude+"---"+ point.longitude);
            }
        });
    }

    public void get_location() {
            LocationManager.setLogType(LogType.GENERAL);
            getLocation();

    }
    public void animate(View btn) {
        Animation rotation = AnimationUtils.loadAnimation(RegisterShop.this, R.anim.rotation);

        btn.startAnimation(rotation);


    }

    @Override
    public boolean onMyLocationButtonClick() {
       // Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
get_location();

        return false;
    }


    public void selectCat(View view) {


        myDialog1 = new BottomSheetDialog(RegisterShop.this);
        myDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog1.setCancelable(true);
        myDialog1.setContentView(R.layout.select_cat_dialog);
        myDialog1.show();
        TextView button = (TextView) myDialog1.findViewById(R.id.button);
        RecyclerView rv=myDialog1.findViewById(R.id.cat_select_rv);
        MultiSelectAdapterCategory adapter = new MultiSelectAdapterCategory(getApplicationContext(), list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
ArrayList<Category> kk=getCat();
                if (kk.size()>0)
                {
                    myDialog1.dismiss();
                  cat_tv.setTextColor(getResources().getColor(R.color.light_green));
                    setSelectedVIewa(kk);

                }else {
                    Toast.makeText(RegisterShop.this,"Please Select A Category",Toast.LENGTH_SHORT).show();
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
                case R.id.shopNameEt:
                {
                    validateShopNAme();
                    break;}
                case R.id.ownerNameEt:
                {
                    validateOwnerNAme();
                    break;}
                case R.id.primaryContactEt:
                {
                    validatePrimaryContact();
                    break;}
                case R.id.secondaryContactEt:
                {
                    validateSecondaryContact();
                    break;}
                case R.id.addresssEt:
                {
                    validateAddress();
                    break;}



            }
        }
    }
    public  void initEts(){

        shopNameEt=(MyEditText)findViewById(R.id.shopNameEt);
        ownerNameEt=(MyEditText)findViewById(R.id.ownerNameEt);
        primaryContactEt=(MyEditText)findViewById(R.id.primaryContactEt);
        secondaryContactEt=(MyEditText)findViewById(R.id.secondaryContactEt);
        addresssEt=(MyEditText)findViewById(R.id.addresssEt);


        shopNameLay=(TextInputLayout)findViewById(R.id.shopNameLay);
        ownerNameLay=(TextInputLayout)findViewById(R.id.ownerNameLay);
        primaryContactLay=(TextInputLayout)findViewById(R.id.primaryContactLay);
        secondaryContactLay=(TextInputLayout)findViewById(R.id.secondaryContactLay);
        addresssLay=(TextInputLayout)findViewById(R.id.addressLay);


        shopNameEt.addTextChangedListener(new MyTextWatcher(shopNameEt));
        ownerNameEt.addTextChangedListener(new MyTextWatcher(ownerNameEt));
        primaryContactEt.addTextChangedListener(new MyTextWatcher(primaryContactEt));
        secondaryContactEt.addTextChangedListener(new MyTextWatcher(secondaryContactEt));
        addresssEt.addTextChangedListener(new MyTextWatcher(addresssEt));


    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private boolean validateShopNAme() {
        if (shopNameEt.getText().toString().trim().isEmpty()) {
            shopNameLay.setError("Field Cannot Be Empty");
            requestFocus(shopNameEt);
            return false;
        } else {
            shopNameLay.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateOwnerNAme() {
        if (ownerNameEt.getText().toString().trim().isEmpty()) {
            ownerNameLay.setError("Field Cannot Be Empty");
            requestFocus(ownerNameEt);
            return false;
        } else {
            ownerNameLay.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validatePrimaryContact() {
        if (primaryContactEt.getText().toString().trim().isEmpty()||primaryContactEt.getText().toString().trim().length()<10) {
            primaryContactLay.setError("Please Enter A Valid 10 Digit Mobile Number");
            requestFocus(primaryContactEt);
            return false;
        } else {
            primaryContactLay.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateSecondaryContact() {
        if (secondaryContactEt.getText().toString().trim().isEmpty()||primaryContactEt.getText().toString().trim().length()<10) {
            secondaryContactLay.setError("Please Enter A Valid 10 Digit Mobile Number");
            requestFocus(secondaryContactEt);
            return false;
        } else {
            secondaryContactLay.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateAddress() {
        if (addresssEt.getText().toString().trim().isEmpty()) {
            addresssLay.setError("Field Cannot Be Empty");
            requestFocus(addresssEt);
            return false;
        } else {
            addresssLay.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateCategory(){
//Toast.makeText(RegisterShop.this,list.size()+"",Toast.LENGTH_SHORT).show();
        if (cats.size()<1)
        {
            cat_tv.setTextColor(getResources().getColor(R.color.red));
            requestFocus(cat_tv);
            return false;
        }

       return  true;
    }
private  boolean validateOpenTime(){

openTime=open.getText().toString();
        if (openTime.equals("")||openTime.equals("Opening Time")) {
            open.setTextColor(getResources().getColor(R.color.red));
            requestFocus(open);
            return false;
        }

        return true;
}
    private  boolean validateCloseTime(){
        closeTime=close.getText().toString();
        if (closeTime.equals("")||closeTime.equals("Closing Time")) {
            close.setTextColor(getResources().getColor(R.color.red));

            requestFocus(close);
            return false;
        }

        return true;
    }

    public  String[] prepareArray(ArrayList<Category> ll){
        String [] kk=new String[ll.size()];
        for (int x=0;x<cats.size();x++)
            kk[x]=cats.get(x);

        return kk;
    }

public  ArrayList<Category> getCat(){
    String text = "";
    for (Category category : list) {
        if (category.isSelected()) {
            if(!selectedcats.contains(category)) {
                selectedcats.add(category);
                cats.add(category.getCatId());
            }
            text += category.getCatName()+"\n";
        }
    }
    Log.d("TAG", "Output : " + text);

    return  selectedcats;
}

    public void goBack(View view) {
        super.onBackPressed();
    }
public  void fetchCategories(){

    RequestBody StoreID = RequestBody.create(MediaType.parse("text/plain"), "1");

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
    // Change base URL to your upload server URL.
    FetchcategoriesApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(FetchcategoriesApi.class);


    retrofit2. Call<CategoryListPojo> mService = service.fetch("fhgjg");
    mService.enqueue(new Callback<CategoryListPojo>() {
        @Override
        public void onResponse(Call<CategoryListPojo> call, Response<CategoryListPojo> response) {

            if (response.body().getStatus())
            // clearFields();
            {
                list=response.body().getCategory();
              }
            //Toast.makeText(RegisterShop.this,""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
        }


        @Override
        public void onFailure(Call<CategoryListPojo> call, Throwable t) {
            Toast.makeText(RegisterShop.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();

        }
    });

}

public  void setSelectedVIewa(ArrayList<Category> cat){


    RecyclerView.LayoutManager LayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
    selected_rv.setLayoutManager(LayoutManager);
    selected_rv.setAdapter(new SelectesCatAdapter(RegisterShop.this,cat));

}

}


