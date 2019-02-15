package com.webinfrasolutions.ikarti;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.webinfrasolutions.ikarti.API.AddAddressApi;
import com.webinfrasolutions.ikarti.API.AddOrderApi;
import com.webinfrasolutions.ikarti.API.DeleteAddressApi;
import com.webinfrasolutions.ikarti.API.FetchAddressApi;
import com.webinfrasolutions.ikarti.Pojo.Address;
import com.webinfrasolutions.ikarti.Pojo.AddressListPojo;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;
import com.webinfrasolutions.ikarti.Pojo.Pic;
import com.webinfrasolutions.ikarti.Pojo.UserData;
import com.webinfrasolutions.ikarti.util.SessionManager;
import java.util.ArrayList;
import java.util.List;
import customfonts.MyEditText;
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

public class AddressActivity  extends  AppCompatActivity  implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    List<Address> list = new ArrayList<>();RecyclerView rv;
    ScrollView addresslay;
    private GoogleMap mMap;
    UserData userData;
    Location mylocation;
    FloatingActionButton fab ;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    PendingResult<LocationSettingsResult> pendingResult;
    public static final int REQUEST_LOCATION=001;
    LocationRequest locationRequest;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    public MyEditText fname,lname,addd,city,state,zip,country,phone;
    LocationSettingsRequest.Builder locationSettingsRequest;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        userData=new SessionManager().getUserData(AddressActivity.this);
        addresslay=findViewById(R.id.adress_lay);
        rv =findViewById(R.id.addres_rv);
        fab=findViewById(R.id.add_address_fab);
        rv.setLayoutManager(new LinearLayoutManager(this));

        fetchAddresses();
        initAddressLay();
    }

    private void initAddressLay() {
        fname =  findViewById(R.id.name3);
        lname =  findViewById(R.id.name4);
        addd =  findViewById(R.id.addL2);
        city =  findViewById(R.id.city2);
        state = findViewById(R.id.state2);
        zip =  findViewById(R.id.zip2);
        country =findViewById(R.id.country2);
        phone =  findViewById(R.id.phn);
        country.setText("Indian");
        city.setText("Bhimavaram");state.setText("Andhra Pradesh");


    }

    public void gotoplace(View view) {
        showDialog();
    }

    public  void fetchAddresses(){
        // RequestBody StoreID = RequestBody.create(MediaType.parse("text/plain"), "1");
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        FetchAddressApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(FetchAddressApi.class);


        retrofit2. Call<AddressListPojo> mService = service.fetch(userData.getUid());
        mService.enqueue(new Callback<AddressListPojo>() {
            @Override
            public void onResponse(Call<AddressListPojo> call, Response<AddressListPojo> response) {

                if (response.body().getStatus())
                // clearFields();
                {
                    list=response.body().getAddressList();
                    if (list.size()==0){
                        showLay();
                    }
                    rv.setAdapter(new AddressAdapter(AddressActivity.this,list));

                }
                //Toast.makeText(AddressActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<AddressListPojo> call, Throwable t) {
                Toast.makeText(AddressActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }
    public  void showLay(){
        rv.setVisibility(View.GONE);
        addresslay.setVisibility(View.VISIBLE);
        fab.setVisibility(View.INVISIBLE);
    }

    public void addAddress(View view) {
        if (validateFname()&&validateLname()&&validateAddd()&&validateCity()&&validateState()&&validateState()&&validateZip()&&validateCountry()&&validatePhone()){
           // Toast.makeText(AddressActivity.this,"Add Address",Toast.LENGTH_SHORT).show();
addAddressCall();
        }else {
            validatePhone();
            validateCountry();
            validateZip();
            validateState();validateCity();validateAddd();validateLname();validateFname();
        }
    }

    public void showAddressLay(View view) {
        showLay();
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private boolean validateFname() {
        if (fname.getText().toString().trim().isEmpty()) {
            fname.setError("Empty FirstName");
            requestFocus(fname);
            return false;
        }

        return true;
    }
    private boolean validateLname() {
        if (lname.getText().toString().trim().isEmpty()) {
            lname.setError("Empty LastName");
            requestFocus(lname);
            return false;
        }

        return true;
    }

    private boolean validateAddd() {
        if (addd.getText().toString().trim().isEmpty()) {
            addd.setError("Empty Addresss");
            requestFocus(addd);
            return false;
        }

        return true;
    }
    private boolean validateCity() {
        if (city.getText().toString().trim().isEmpty()) {
            city.setError("Empty City");
            requestFocus(city);
            return false;
        }

        return true;
    }
    private boolean validateState() {
        if (state.getText().toString().trim().isEmpty()) {
            state.setError("Empty State");
            requestFocus(state);
            return false;
        }

        return true;
    }
    private boolean validateZip() {
        if (zip.getText().toString().trim().isEmpty()&&zip.getText().toString().trim().length()<6) {
            zip.setError("Empty Zipcode");
            requestFocus(zip);
            return false;
        }

        return true;
    }
    private boolean validateCountry() {
        if (country.getText().toString().trim().isEmpty()) {
            country.setError("Empty country");
            requestFocus(country);
            return false;
        }

        return true;
    }
    private boolean validatePhone() {
        if (phone.getText().toString().trim().isEmpty()&&phone.getText().toString().trim().length()<10) {
            phone.setError("Enter A Valid 10 Digit MobileNumber");
            requestFocus(phone);
            return false;
        }

        return true;
    }
    private void addAddressCall() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.

        AddAddressApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(AddAddressApi.class);
           retrofit2. Call<MyPojo> mService = service.add(userData.getUid(),fname.getText().toString().trim(),lname.getText().toString().trim(),
                   city.getText().toString().trim(),
                   state.getText().toString().trim(),
                   zip.getText().toString().trim(),
                   phone.getText().toString().trim(),
                   addd.getText().toString().trim());
        mService.enqueue(new Callback<MyPojo>() {
            @Override
            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {
                MyPojo obj=response.body();

                if (obj.getStatus()) {
                    rv.setVisibility(View.VISIBLE);
                    addresslay.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                fetchAddresses();
                    Toast.makeText(AddressActivity.this, obj.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onFailure(Call<MyPojo> call, Throwable t) {
                Toast.makeText(AddressActivity.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    public void showDialog(){
        //MapDialogFragment fragment= new MapDialogFragment();
     //  fragment .show(getFragmentManager(), null);
        MyTextView ok,cancel;

        final Dialog catDialog = new BottomSheetDialog(AddressActivity.this);
        catDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        catDialog.setCancelable(false);
        catDialog.setContentView(R.layout.select_location_dialog);
       catDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        catDialog.show();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        /* final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        catDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
             Toast.makeText(AddressActivity.this,"clear",Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().remove(mapFragment);
        });
*/
        ok =  catDialog.findViewById(R.id.button_ok);
        cancel =  catDialog.findViewById(R.id.button_cancel);

        final MapView mMapView;
        MapsInitializer.initialize(AddressActivity.this);
        mMapView = (MapView) catDialog.findViewById(R.id.mapview);
        mMapView.onCreate(catDialog.onSaveInstanceState());
        mMapView.onResume();// needed to get the map to display immediately
        mMapView.getMapAsync(AddressActivity.this);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete(cartId,position);
Toast.makeText(AddressActivity.this,mylocation.toString(),Toast.LENGTH_SHORT).show();
                addOrder();
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

    public void initLocation(){




        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        boolean GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!GpsStatus){
            mEnableGps();

        }else {
        }


    }

    public void mEnableGps() {
        mGoogleApiClient = new GoogleApiClient.Builder(AddressActivity.this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        mLocationSetting();
    }
    public void mLocationSetting() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1 * 1000);
        locationRequest.setFastestInterval(1 * 1000);

        locationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        mResult();

    }

    public void mResult() {
        pendingResult = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSettingsRequest.build());
        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();


                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {

                            status.startResolutionForResult(AddressActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.


                        break;
                }
            }

        });
    }

    //callback method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        //Toast.makeText(AddressActivity.this, "Gps enabled", Toast.LENGTH_SHORT).show();
                        //setmMap();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(AddressActivity.this, "Gps Canceled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
         googleMap.setMinZoomPreference(15.0f);
          mMap.setMaxZoomPreference(22.0f);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);



        //  mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //  mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    //    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
     googleMap.animateCamera(CameraUpdateFactory.zoomTo(20.0f));
        // Setting a click event handler for the map
       // googleMap.moveCamera(CameraUpdateFactory.newLatLng(myloc));
       // googleMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));

    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mylocation=location;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }



    public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

        Context context;
        List<Address> records=new ArrayList<>();
        public AddressAdapter(Context contexts, List<Address> records) {
            this.context=contexts;
            this.records=records;

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public MyTextView fname,lname,addd,city,state,zip,country,phone;
            ImageView deleteAddressBtn;LinearLayout lay;
            public MyViewHolder(View view) {
                super(view);
                fname =  view.findViewById(R.id.name3);
                lname =  view.findViewById(R.id.name4);
                addd =  view.findViewById(R.id.addL2);
                city =  view.findViewById(R.id.city2);
                state =  view.findViewById(R.id.state2);
                zip =  view.findViewById(R.id.zip2);
                country =  view.findViewById(R.id.country2);
                phone =  view.findViewById(R.id.phn);
                deleteAddressBtn=view.findViewById(R.id.deleteAddressBtn);
                lay=view.findViewById(R.id.linear1);
                setIsRecyclable(false);
            }

        }

        @Override
        public AddressAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.address_row, parent, false);

            return new AddressAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(AddressAdapter.MyViewHolder holder, final int position) {
            final Address address=records.get(position);

            holder.fname.setText(address.getFirstName());
            holder.lname.setText(address.getLastName());
            holder.addd.setText(address.getAddressLine());
            holder.city.setText(address.getCity());
            holder.state.setText(address.getState());
            holder.zip.setText(address.getZipcode());
            holder.country.setText("India");
            holder.phone.setText(address.getMobileNumber());
            holder.deleteAddressBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteDialog(address.getId(),position);            }
            });
            holder.lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addOrder(records.get(position).getId());
                   // Bundle bundle = new Bundle();
                   // bundle.putSerializable("address_id",records.get(position).getId());
                   // bundle.putString("dtype","address");
                  // Intent mainIntent = new Intent(context, ThankUActivity.class);
                    //add bundle to intent
                  //  mainIntent.putExtras(bundle);
                    //start activity
                  // context.startActivity(mainIntent);
                }
            });

        }


        @Override
        public int getItemCount() {
            return records.size();
        }
        public  void showDeleteDialog(final String Aid, final int position){
            MyTextView ok,cancel;
            ImageView imageView;
            final Dialog catDialog = new BottomSheetDialog(context);
            catDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            catDialog.setCancelable(true);
            catDialog.setContentView(R.layout.delete_address_bottom_lay);
            catDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
            catDialog.show();
            ok =  catDialog.findViewById(R.id.button_ok);
            cancel =  catDialog.findViewById(R.id.button_cancel);

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(Aid,position);
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
        public void delete(String aid, final int position){

            RequestBody AddressId = RequestBody.create(MediaType.parse("text/plain"), aid);

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            // Change base URL to your upload server URL.
            DeleteAddressApi service = new Retrofit.Builder().baseUrl(context.getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(DeleteAddressApi.class);
            retrofit2.Call<MyPojo> mService = service.delete(AddressId);
            mService.enqueue(new Callback<MyPojo>() {
                @Override
                public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {

                    if (response.body().getStatus()) {
                        records.remove(position);
                        notifyDataSetChanged();
                    }
                    // clearFields();
                    Toast.makeText(context,  response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<MyPojo> call, Throwable t) {
                    Toast.makeText(context,t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });


        }

    }


    private void addOrder(String address_id) {

        final SharedPreferences db=getSharedPreferences("order",MODE_PRIVATE);
        String latitude="",longitude="",address="",delivery_type="address";


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        UserData userData=new SessionManager().getUserData(AddressActivity.this);

        AddOrderApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(AddOrderApi.class);

        retrofit2. Call<MyPojo> mService = service.add(
                userData.getUid(),
                db.getString("pid",null),
                db.getString("quantity",null),
                db.getString("product_deal_price",null),
                db.getString("product_price",null),
                db.getString("save_price",null),
                db.getString("item_total",null),
                db.getString("store_id",null),
                address_id,delivery_type,
                address,latitude,longitude

        );
        mService.enqueue(new Callback<MyPojo>() {
            @Override
            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {

                Toast.makeText(AddressActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                if (response.body().getStatus()){
Intent i=new Intent(AddressActivity.this,ThankUActivity.class);startActivity(i);
                /*    price.setText(db.getString("product_price",null));
                    total_price.setText(db.getString("product_price",null));
                    price.setText(db.getString("item_total",null));
                    des.setText(db.getString("des",null));
                    itemcount.setText(db.getString("quantity",null)+" X ");
                    title.setText(db.getString("title",null));

                    AddressActivity                            load(ThankUActivity.this.getResources().getString(R.string.base_url)+"deals/images/"+db.getString("url",null) )
                            .thumbnail(Glide.with(ThankUActivity.this).load(R.drawable.loading))
                            .crossFade()
                            .into(imageView);*/

                }

            }


            @Override
            public void onFailure(Call<MyPojo> call, Throwable t) {
                Toast.makeText(AddressActivity.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void addOrder() {

        final SharedPreferences db=getSharedPreferences("order",MODE_PRIVATE);
        String latitude=mylocation.getLatitude()+"",
                longitude=mylocation.getLongitude()+"",
                address="",delivery_type="location",address_id="";
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        UserData userData=new SessionManager().getUserData(AddressActivity.this);

        AddOrderApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(AddOrderApi.class);

        retrofit2. Call<MyPojo> mService = service.add(
                userData.getUid(),
                db.getString("pid",null),
                db.getString("quantity",null),
                db.getString("product_deal_price",null),
                db.getString("product_price",null),
                db.getString("save_price",null),
                db.getString("item_total",null),
                db.getString("store_id",null),
                address_id,delivery_type,
                address,latitude,longitude

        );
        mService.enqueue(new Callback<MyPojo>() {
            @Override
            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {

                Toast.makeText(AddressActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                if (response.body().getStatus()){
                    Intent i=new Intent(AddressActivity.this,ThankUActivity.class);startActivity(i);

                /*    price.setText(db.getString("product_price",null));
                    total_price.setText(db.getString("product_price",null));
                    price.setText(db.getString("item_total",null));
                    des.setText(db.getString("des",null));
                    itemcount.setText(db.getString("quantity",null)+" X ");
                    title.setText(db.getString("title",null));

                    AddressActivity                            load(ThankUActivity.this.getResources().getString(R.string.base_url)+"deals/images/"+db.getString("url",null) )
                            .thumbnail(Glide.with(ThankUActivity.this).load(R.drawable.loading))
                            .crossFade()
                            .into(imageView);*/

                }

            }


            @Override
            public void onFailure(Call<MyPojo> call, Throwable t) {
                Toast.makeText(AddressActivity.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }


}
