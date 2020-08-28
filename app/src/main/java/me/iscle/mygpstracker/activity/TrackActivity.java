package me.iscle.mygpstracker.activity;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import me.iscle.mygpstracker.Device;
import me.iscle.mygpstracker.databinding.ActivityTrackBinding;
import me.iscle.mygpstracker.network.GPS365Repository;
import me.iscle.mygpstracker.network.callback.PositionCallback;
import me.iscle.mygpstracker.network.response.PositionResponse;

public class TrackActivity extends BaseActivity implements OnMapReadyCallback {
    private static final String TAG = "TrackActivity";

    private MapView mapView;
    private Device device;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityTrackBinding binding = ActivityTrackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mapView = binding.map;

        device = getMyGPSTracker().getCurrentDevice();

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        final UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);

        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        updateLocation();
    }

    private void updateLocation() {
        GPS365Repository.getInstance().imeiLogin(device.getImei(), device.getPassword(), new PositionCallback() {
            @Override
            public void onSuccess(PositionResponse response) {
                String[] coords = response.google.split(",");
                LatLng latLng = new LatLng(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(response.name));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
            }

            @Override
            public void onError(LoginError error) {
                Toast.makeText(TrackActivity.this, "Something went wrong while updating the location.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        mapView.onStart();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

}