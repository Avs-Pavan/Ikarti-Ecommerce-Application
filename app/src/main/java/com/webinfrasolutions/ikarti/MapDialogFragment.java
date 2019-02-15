package com.webinfrasolutions.ikarti;

import android.app.DialogFragment;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by kevin on 22/12/17.
 */


public class MapDialogFragment extends DialogFragment
        implements GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleMap mMap;  GoogleApiClient mGoogleApiClient;
    private static View view;
    public MapDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View rootView = i
        // Inflate the layout for this fragment
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }

        try {
            view = inflater.inflate(R.layout.select_location_dialog, container, false);
          //  view.setBackgroundResource(android.R.color.transparent);
             //view.setBackgroundResource(R.color.light_blue);

        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }


        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
       // mapFragment.getMapAsync(this);
        return view;

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMinZoomPreference(16.0f);
        //   mMap.setMaxZoomPreference(19.0f);

        // mMap.animateCamera( CameraUpdateFactory.zoomTo( 15.0f ) );
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.setMyLocationEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);
        //  mMap.setOnMyLocationClickListener(this);

        //  mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //  mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(20.0f));
        // mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        // Setting a click event handler for the map
        // googleMap.moveCamera(CameraUpdateFactory.newLatLng(myloc));
        // googleMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
       /* googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                googleMap.clear();
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(point.latitude, point.longitude)).title("Your New Location");

                googleMap.addMarker(marker);
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));

                System.out.println(point.latitude+"---"+ point.longitude);
            }
        });*/


    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}