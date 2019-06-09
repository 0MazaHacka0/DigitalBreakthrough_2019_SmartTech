package com.smarttech.doingtogether.ui.passenger;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.view.ClusterRenderer;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.smarttech.doingtogether.R;
import com.smarttech.doingtogether.data.network.ApiHelper;
import com.smarttech.doingtogether.data.network.AppApiHelper;
import com.smarttech.doingtogether.data.network.model.DriverWaypoint;

import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.smarttech.doingtogether.data.network.model.LoginRequest;
import com.smarttech.doingtogether.data.network.model.LoginResponse;
import com.smarttech.doingtogether.ui.driver_screen.DriverScreenActivity;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PassengerActivity extends FragmentActivity implements OnMapReadyCallback {

    private ApiHelper mApi;

    private GoogleMap mMap;

    private String mKey;

    private ArrayList<DriverWaypoint> mDrivers;

    private LatLng mCurrentGeoPos = new LatLng(47.238224, 39.712125);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Api
        mApi = AppApiHelper.getRetrofitAdapter();

        mKey = "Bearer " + getSharedPreferences("settings", Context.MODE_PRIVATE)
                .getString("key", "lol");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(mCurrentGeoPos).title("My geo"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentGeoPos, mMap.getMaxZoomLevel()));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                startActivity(new Intent(PassengerActivity.this, DriverScreenActivity.class));
            }
        });

        loadStops();
    }

    protected void loadStops() {
        mDrivers = new ArrayList<>();

        mApi.getDrivers(mKey, mCurrentGeoPos.latitude, mCurrentGeoPos.longitude, 0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ArrayList<DriverWaypoint>>() {
                    @Override
                    public void accept(ArrayList<DriverWaypoint> driverWaypoint) throws Exception {
                        if (driverWaypoint != null) {
                            mDrivers = driverWaypoint;
                            setUpClusterer();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        int a = 1;
                    }
                });
    }

    private class MyItem implements ClusterItem {
        private final LatLng mPosition;
        private String mTitle;
        private String mSnippet;
        private BitmapDescriptor icon;

        public MyItem(LatLng pos) {
            mPosition = pos;
        }

        public MyItem(double lat, double lng) {
            mPosition = new LatLng(lat, lng);
            Bitmap ic = BitmapFactory.decodeResource(getResources(), R.drawable.car_dot);
            ic = Bitmap.createScaledBitmap(ic, 25, 25, true);
            icon = BitmapDescriptorFactory.fromBitmap(ic);
        }

        public MyItem(double lat, double lng, String title, String snippet) {
            mPosition = new LatLng(lat, lng);
            mTitle = title;
            mSnippet = snippet;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public String getTitle() {
            return mTitle;
        }

        @Override
        public String getSnippet() {
            return mSnippet;
        }

        public BitmapDescriptor getIcon() {
            return icon;
        }
    }

    // Declare a variable for the cluster manager.
    private ClusterManager<MyItem> mClusterManager;

    private void setUpClusterer() {
        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentGeoPos, 20));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyItem>(this, mMap);

        ClusterRenderer clusterRenderer = new ClusterRenderer(this, mMap, mClusterManager);
//        mClusterManager.setRenderer(clusterRenderer);
        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {

        // Draw markers
        for (int i = 0; i < mDrivers.size(); ++i) {
            MyItem offsetItem = new MyItem(mDrivers.get(i).getLat(), mDrivers.get(i).getLng());
            mClusterManager.addItem(offsetItem);
        }
    }

    private class ClusterRenderer extends DefaultClusterRenderer<MyItem> {

        public ClusterRenderer(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
            clusterManager.setRenderer(this);
        }


        @Override
        protected void onBeforeClusterItemRendered(MyItem markerItem, MarkerOptions markerOptions) {

            if (markerItem.getIcon() != null) {
                markerOptions.icon(markerItem.getIcon()); //Here you retrieve BitmapDescriptor from ClusterItem and set it as marker icon
            }
            markerOptions.visible(true);
        }
    }
}
