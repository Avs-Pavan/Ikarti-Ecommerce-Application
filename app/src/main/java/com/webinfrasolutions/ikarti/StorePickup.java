package com.webinfrasolutions.ikarti;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webinfrasolutions.ikarti.API.FetchStoreKeeperDataApi;
import com.webinfrasolutions.ikarti.Pojo.CartItem;
import com.webinfrasolutions.ikarti.Pojo.Distance;
import com.webinfrasolutions.ikarti.Pojo.Duration;
import com.webinfrasolutions.ikarti.Pojo.FetchStoreDataPojo;
import com.webinfrasolutions.ikarti.Pojo.Storekeeper;
import java.util.ArrayList;
import java.util.List;
import customfonts.MyTextView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class StorePickup extends AppCompatActivity
        implements OnMapReadyCallback,
                GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    Storekeeper storekeeper;
    private static final int LOCATION_REQUEST = 1,
            CALL_REQUEST=2;

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker,destin;
    Location mylocation;
    MyTextView distance, address, sname, oname;
    CartItem record;
    LatLng source, destination;
    LocationRequest mLocationRequest;

    public void goBack(View V) {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_pickup);
        MyTextView title = findViewById(R.id.toolbar_title);
        title.setText("Store Pickup");
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermissions();

        }
        address = findViewById(R.id.address);
        sname = findViewById(R.id.storename);
        oname = findViewById(R.id.ownername);

        try {

            record = (CartItem) getIntent().getSerializableExtra("record");


        } catch (NullPointerException n) {
            n.printStackTrace();
        }
        distance = findViewById(R.id.distance);
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermissions();

        }
        if (checkAndRequestPermissions())
        {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);
        }else checkAndRequestPermissions();



    }

    public void fetchStore() {


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        FetchStoreKeeperDataApi service = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(FetchStoreKeeperDataApi.class);

        retrofit2.Call<FetchStoreDataPojo> mService = service.fetch("1");
        mService.enqueue(new Callback<FetchStoreDataPojo>() {
            @Override
            public void onResponse(Call<FetchStoreDataPojo> call, Response<FetchStoreDataPojo> response) {
                if (response.body().getStatus()) {

                    destination = new LatLng(Float.parseFloat(response.body().getStoreKeeper().getLatitude()),
                            Float.parseFloat(response.body().getStoreKeeper().getLongitude()));
                    storekeeper = response.body().getStoreKeeper();
                    sname.setText(storekeeper.getName());
                    oname.setText(storekeeper.getOwnerName());
                    address.setText(storekeeper.getAddress().trim());
                    initMap();
                }
                Toast.makeText(StorePickup.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<FetchStoreDataPojo> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private boolean checkAndRequestPermissions() {
        int permissionCall = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE);
        int locationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionCall != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CALL_PHONE);
        }
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),LOCATION_REQUEST);
            return false;
        }

        return true;
    }
    @Override    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);

                    mapFragment.getMapAsync(this);

                } else {
                   // Toast.makeText(StorePickup.this,"You Have To Enable Permissions To Use Dialing Feature",Toast.LENGTH_SHORT).show();
                    //checkAndRequestPermissions();
                    //You did not accept the request can not use the functionality.
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //initMap();
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
        //mMap.setOnMapClickListener(this);
    }

    public void callStore(View view) {

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + storekeeper.getContactNumber1()));
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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
        Toast.makeText(StorePickup.this,"Connection Suspended",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
Toast.makeText(StorePickup.this,"Connection Failed",Toast.LENGTH_SHORT).show();
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
        source=latLng;
        fetchStore();
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
       markerRating.setText(storekeeper.getName());

        markerLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        markerLayout.layout(0, 0, markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight());

        final Bitmap bitmap = Bitmap.createBitmap(markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        markerLayout.draw(canvas);
        return bitmap;
    }

    public void navigate(View view) {

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+
                source.latitude+","+source.longitude+"&daddr="+destination.latitude+","+destination.longitude));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    public interface MyApiRequestInterface {

        @GET("/maps/api/directions/json")
        public Call<DirectionObject> getJson(@Query("origin") String origin, @Query("destination") String destination
        , @Query("mode") String mode);
    }
    public void initMap() {
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
        markerOptions.title(storekeeper.getName());
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createUserMarker()));

        // markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView("Store")));
       // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
         destin=  mMap.addMarker(markerOptions);

        // mCurrLocationMarker = mMap.addMarker(markerOptions);

       // mMap.addMarker(new MarkerOptions().position(destination));

        retrofit2.Call<DirectionObject> mService = service.getJson(source.latitude + "," + source.longitude, destination.latitude + "," + destination.longitude,"driving");


        mService.enqueue(new Callback<DirectionObject>() {
            @Override
            public void onResponse(Call<DirectionObject> call, Response<DirectionObject> response) {

                if(response.code()==200){
                    List<LatLng> mDirections = getDirectionPolylines(response.body().getRoutes());
                    drawRouteOnMap(mMap, mDirections);
                 String kdistance = response.body().getRoutes().get(0).getLegs().get(0).getDistance().getText();
                  String time = response.body().getRoutes().get(0).getLegs().get(0).getDuration().getText();
                   // Toast.makeText(StorePickup.this, "Distance "+distance+ "Time "+time, Toast.LENGTH_SHORT).show();
distance.setText("Distance "+kdistance+ "  | Journey Time "+time);
                }else{
                    Toast.makeText(StorePickup.this, "Server Error", Toast.LENGTH_SHORT).show();
                }

                }


            @Override
            public void onFailure(Call<DirectionObject> call, Throwable t) {
t.printStackTrace();
                Toast.makeText(StorePickup.this,"Retrofit Failed",Toast.LENGTH_SHORT).show();

            }
        });


    }

    private Bitmap getMarkerBitmapFromView(String txt) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        //MyTextView marker_title = (MyTextView) customMarkerView.findViewById(R.id.profile_txt);
        //marker_title.setText(txt);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    private void drawRouteOnMap(GoogleMap map, List<LatLng> positions){
        PolylineOptions options = new PolylineOptions().width(8).color(getResources().getColor(R.color.light_blue)).geodesic(true);
        options.addAll(positions);
        Polyline polyline = map.addPolyline(options);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(positions.get(0).latitude, positions.get(0).longitude))
                .zoom(13)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    private List<LatLng> getDirectionPolylines(List<RouteObject> routes){
        List<LatLng> directionList = new ArrayList<LatLng>();
        for(RouteObject route : routes){
            List<LegsObject> legs = route.getLegs();
            for(LegsObject leg : legs){
                List<StepsObject> steps = leg.getSteps();
                for(StepsObject step : steps){
                    PolylineObject polyline = step.getPolyline();
                    String points = polyline.getPoints();
                    List<LatLng> singlePolyline = decodePoly(points);
                    for (LatLng direction : singlePolyline){
                        directionList.add(direction);
                    }
                }
            }
        }
        return directionList;
    }

    public static class RouteDecode {

        public static ArrayList<LatLng> decodePoly(String encoded) {
            ArrayList<LatLng> poly = new ArrayList<LatLng>();
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

                LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
                poly.add(position);
            }
            return poly;
        }
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
    }    public class PolylineObject {
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
         *
         * @return
         * The distance
         */
        public Distance getDistance() {
            return distance;
        }

        /**
         *
         * @param distance
         * The distance
         */
        public void setDistance(Distance distance) {
            this.distance = distance;
        }

        /**
         *
         * @return
         * The duration
         */
        public Duration getDuration() {
            return duration;
        }

        /**
         *
         * @param duration
         * The duration
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
    }
}
