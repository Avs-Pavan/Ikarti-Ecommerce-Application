package com.webinfrasolutions.ikarti;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webinfrasolutions.ikarti.API.WorkerDispatchApi;
import com.webinfrasolutions.ikarti.Pojo.Address;
import com.webinfrasolutions.ikarti.Pojo.Distance;
import com.webinfrasolutions.ikarti.Pojo.Duration;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;
import com.webinfrasolutions.ikarti.Pojo.WorkerOrder;

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
import retrofit2.http.GET;
import retrofit2.http.Query;

public class DispatchOrder extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    public void goBack(View V) {
        super.onBackPressed();
    }

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker, destin;
    Location mylocation;
    int CALLPERMISSION_REQUEST = 0;
    MyTextView distance, address, sname, oname;
    WorkerOrder record;
    LatLng source, destination;
    LocationRequest mLocationRequest;
    PendingResult<LocationSettingsResult> pendingResult;
    public static final int REQUEST_LOCATION = 001;
    LocationRequest locationRequest;
    MyEditText code;
    LocationSettingsRequest.Builder locationSettingsRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_dispatch_order);
        MyTextView title = findViewById(R.id.toolbar_title);
        title.setText("Dispatch Order");
        code = findViewById(R.id.code_et);
        try {

            record = (WorkerOrder) getIntent().getSerializableExtra("record");

            if (record.getDeliveryType().equals("location")) {
                LinearLayout view = findViewById(R.id.card);
                view.setVisibility(View.VISIBLE);
                double lat = Double.parseDouble(record.getLocation().get(0).getLatitude());
                double lon = Double.parseDouble(record.getLocation().get(0).getLongitude());

                destination = new LatLng(lat, lon);
                Log.i("status", "destination " + destination);

                initLocation();

            } else {

                LinearLayout address_lay = findViewById(R.id.address_lay);
                address_lay.setVisibility(View.VISIBLE);
                initAddress();
            }
        } catch (NullPointerException n) {
            n.printStackTrace();
            Log.i("status", "failed intent data");

        }
        distance = findViewById(R.id.distance);
    }

    private void initAddress() {
        MyTextView fname, lname, addd, city, state, zip, country, phone;

        fname = findViewById(R.id.name3);
        lname = findViewById(R.id.name4);
        addd = findViewById(R.id.addL2);
        city = findViewById(R.id.city2);
        state = findViewById(R.id.state2);
        zip = findViewById(R.id.zip2);
        country = findViewById(R.id.country2);
        phone = findViewById(R.id.phn);


        final Address address = record.getAddress().get(0);

        fname.setText(address.getFirstName());
        lname.setText(address.getLastName());
        addd.setText(address.getAddressLine());
        city.setText(address.getCity());
        state.setText(address.getState());
        zip.setText(address.getZipcode());
        country.setText("India");
        phone.setText(address.getMobileNumber());

    }

    public void navigate(View view) {

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" +
                source.latitude + "," + source.longitude + "&daddr=" + destination.latitude + "," + destination.longitude));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    public void initLocation() {


        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!GpsStatus) {
            mEnableGps();

        } else {
            setmMap();
        }


    }

    public void mEnableGps() {
        mGoogleApiClient = new GoogleApiClient.Builder(DispatchOrder.this)
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

                            status.startResolutionForResult(DispatchOrder.this, REQUEST_LOCATION);
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
                        Toast.makeText(DispatchOrder.this, "Gps enabled", Toast.LENGTH_SHORT).show();
                        setmMap();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(DispatchOrder.this, "Gps Canceled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    public void setmMap() {

        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermissions();

        }


        if (checkAndRequestPermissions()) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);
        } else checkAndRequestPermissions();


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("status", "conneted to  client");

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
        Toast.makeText(DispatchOrder.this, "Connection Suspended", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(DispatchOrder.this, "onConnectionFailed", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i("status", "onLocationChanged");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mylocation = location;
        source = latLng;
        initMap();
        // mSydney = mMap.addMarker(new MarkerOptions()
        //         .position(SYDNEY)
        //         .title("Sydney")
        //       .snippet("Population: 4,627,300")
        //     .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createStoreMarker()));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.i("status", "map ready");
        //initMap();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        //mMap.setOnMapClickListener(this);
    }


    private boolean checkAndRequestPermissions() {
        int permissionCall = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE);
        int locationPermission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionCall != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CALL_PHONE);
        }
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), CALLPERMISSION_REQUEST);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);

                    mapFragment.getMapAsync(this);

                } else {
                    //Toast.makeText( this,"You Have To Enable Permissions To Use Dialing Feature",Toast.LENGTH_SHORT).show();
                    checkAndRequestPermissions();
                    //You did not accept the request can not use the functionality.
                }
                break;
        }
    }

    public void callCustomer(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + record.getUser().get(0).getMobileNumber()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            checkAndRequestPermissions();
        }
        startActivity(intent);

    }

    public void dispatch(View view) {


        String cod = code.getText().toString().trim();
        if (code.length() < 4) {
            code.setError("Enter A Valid Code");

        } else {
            dispatchCall(cod);
        }
    }

    private void dispatchCall(String cod) {
        RequestBody Code = RequestBody.create(MediaType.parse("text/plain"), cod);
        RequestBody UId = RequestBody.create(MediaType.parse("text/plain"), record.getUid());
        RequestBody Sid = RequestBody.create(MediaType.parse("text/plain"), record.getStoreId());
        RequestBody Oid = RequestBody.create(MediaType.parse("text/plain"), record.getOrderId());

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        WorkerDispatchApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(WorkerDispatchApi.class);


        retrofit2.Call<MyPojo> mService = service.dispath(Oid, UId, Sid, Code);
        mService.enqueue(new Callback<MyPojo>() {
            @Override
            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {
                MyTextView message = findViewById(R.id.message);
                if (response.body().getStatus()) {
                    message.setTextColor(getResources().getColor(R.color.light_green));
                    message.setText(response.body().getMessage());
                } else {
                    message.setTextColor(getResources().getColor(R.color.red));
                    message.setText(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<MyPojo> call, Throwable t) {

            }
        });

    }

    public interface MyApiRequestInterface {

        @GET("/maps/api/directions/json")
        public Call<DirectionObject> getJson(@Query("origin") String origin, @Query("destination") String destination
                , @Query("mode") String mode);
    }

    public void initMap() {

        Log.i("status", "inimap");

        String base_url = "http://maps.googleapis.com/";


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        MyApiRequestInterface service = new Retrofit.Builder().baseUrl(base_url).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(MyApiRequestInterface.class);


        if (destin != null) {
            destin.remove();
        }

        // source = new LatLng(16.5175, 81.7253);
        //destination = new LatLng(16.5449, 81.5212);
        // mMap.addMarker(new MarkerOptions().position(source));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(destination);
        markerOptions.title("Customer");
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createUserMarker()));

        // markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView("Store")));
        // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        destin = mMap.addMarker(markerOptions);

        // mCurrLocationMarker = mMap.addMarker(markerOptions);

        // mMap.addMarker(new MarkerOptions().position(destination));

        retrofit2.Call<DirectionObject> mService = service.getJson(source.latitude + "," + source.longitude, destination.latitude + "," + destination.longitude, "driving");


        mService.enqueue(new Callback<DirectionObject>() {
            @Override
            public void onResponse(Call<DirectionObject> call, Response<DirectionObject> response) {

                if (response.code() == 200) {
                    List<LatLng> mDirections = getDirectionPolylines(response.body().getRoutes());
                    drawRouteOnMap(mMap, mDirections);
                    String kdistance = response.body().getRoutes().get(0).getLegs().get(0).getDistance().getText();
                    String time = response.body().getRoutes().get(0).getLegs().get(0).getDuration().getText();
                    // Toast.makeText( this, "Distance "+distance+ "Time "+time, Toast.LENGTH_SHORT).show();
                    distance.setText("Distance " + kdistance + "  | Journey Time " + time);
                } else {
                    Toast.makeText(DispatchOrder.this, "Server Error", Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onFailure(Call<DirectionObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(DispatchOrder.this, "Retrofit Failed", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void drawRouteOnMap(GoogleMap map, List<LatLng> positions) {
        PolylineOptions options = new PolylineOptions().width(8).color(getResources().getColor(R.color.light_blue)).geodesic(true);
        options.addAll(positions);
        Polyline polyline = map.addPolyline(options);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(source);
        builder.include(destination);
        LatLngBounds bounds = builder.build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
        mMap.animateCamera(cu, new GoogleMap.CancelableCallback() {
            public void onCancel() {
            }

            public void onFinish() {
                CameraUpdate zout = CameraUpdateFactory.zoomBy(-2);
                mMap.animateCamera(zout);
            }
        });
        /*CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(positions.get(0).latitude, positions.get(0).longitude))
                .zoom(25)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

    }


    private List<LatLng> getDirectionPolylines(List<RouteObject> routes) {
        List<LatLng> directionList = new ArrayList<LatLng>();
        for (RouteObject route : routes) {
            List<LegsObject> legs = route.getLegs();
            for (LegsObject leg : legs) {
                List<StepsObject> steps = leg.getSteps();
                for (StepsObject step : steps) {
                    PolylineObject polyline = step.getPolyline();
                    String points = polyline.getPoints();
                    List<LatLng> singlePolyline = decodePoly(points);
                    for (LatLng direction : singlePolyline) {
                        directionList.add(direction);
                    }
                }
            }
        }
        return directionList;
    }


    public class DirectionObject {
        private List<RouteObject> routes;
        private String status;

        public DirectionObject(List<RouteObject> routes, String status) {
            this.routes = routes;
            this.status = status;
        }

        public List<RouteObject> getRoutes() {
            return routes;
        }

        public String getStatus() {
            return status;
        }
    }

    public class PolylineObject {
        private String points;

        public PolylineObject(String points) {
            this.points = points;
        }

        public String getPoints() {
            return points;
        }
    }

    public class LegsObject {
        private List<StepsObject> steps;

        public LegsObject(List<StepsObject> steps) {
            this.steps = steps;
        }

        public List<StepsObject> getSteps() {
            return steps;
        }

        @SerializedName("distance")
        @Expose
        private Distance distance;
        @SerializedName("duration")
        @Expose
        private Duration duration;

        /**
         * @return The distance
         */
        public Distance getDistance() {
            return distance;
        }

        /**
         * @param distance The distance
         */
        public void setDistance(Distance distance) {
            this.distance = distance;
        }

        /**
         * @return The duration
         */
        public Duration getDuration() {
            return duration;
        }

        /**
         * @param duration The duration
         */
        public void setDuration(Duration duration) {
            this.duration = duration;
        }

    }

    public class RouteObject {
        private List<LegsObject> legs;

        public RouteObject(List<LegsObject> legs) {
            this.legs = legs;
        }

        public List<LegsObject> getLegs() {
            return legs;
        }
    }

    public class StepsObject {
        private PolylineObject polyline;

        public StepsObject(PolylineObject polyline) {
            this.polyline = polyline;
        }

        public PolylineObject getPolyline() {
            return polyline;
        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        Log.i("status", "building client");

    }


    private Bitmap createStoreMarker() {
        View markerLayout = getLayoutInflater().inflate(R.layout.view_custom_marker, null);

        ImageView markerImage = (ImageView) markerLayout.findViewById(R.id.marker_image);
        TextView markerRating = (TextView) markerLayout.findViewById(R.id.marker_text);
        // markerImage.setImageResource(R.drawable.locationpin);
        // markerRating.setText("You");

        markerLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        markerLayout.layout(0, 0, markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight());

        final Bitmap bitmap = Bitmap.createBitmap(markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        markerLayout.draw(canvas);
        return bitmap;
    }

    private Bitmap createUserMarker() {
        View markerLayout = getLayoutInflater().inflate(R.layout.location_pi_destination_lay, null);

        ImageView markerImage = (ImageView) markerLayout.findViewById(R.id.marker_image);
        TextView markerRating = (TextView) markerLayout.findViewById(R.id.marker_text);
        // markerImage.setImageResource(R.drawable.locationpin);
        markerRating.setText("Customer");

        markerLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        markerLayout.layout(0, 0, markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight());

        final Bitmap bitmap = Bitmap.createBitmap(markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        markerLayout.draw(canvas);
        return bitmap;
    }
}