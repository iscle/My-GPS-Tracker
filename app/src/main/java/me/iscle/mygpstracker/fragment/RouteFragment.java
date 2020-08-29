package me.iscle.mygpstracker.fragment;

import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.iscle.mygpstracker.Device;
import me.iscle.mygpstracker.R;
import me.iscle.mygpstracker.Utils;
import me.iscle.mygpstracker.databinding.FragmentRouteBinding;
import me.iscle.mygpstracker.model.RoutePoint;
import me.iscle.mygpstracker.network.GPS365Repository;
import me.iscle.mygpstracker.network.callback.PositionCallback;
import me.iscle.mygpstracker.network.callback.RouteCallback;
import me.iscle.mygpstracker.network.response.PositionResponse;

public class RouteFragment extends BaseFragment implements OnMapReadyCallback {
    private static final String TAG = "RouteFragment";

    private MapView mapView;
    private View infoLayout;
    private View settingLayout;
    private EditText startDate;
    private EditText endDate;
    private CheckBox gps;
    private CheckBox wifi;
    private CheckBox lbs;
    private RadioButton slow;
    private RadioButton normal;
    private RadioButton fast;
    private CheckBox icon;
    private CheckBox line;
    private Button search;

    private Device device;
    private GoogleMap googleMap;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final SimpleDateFormat queryDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FragmentRouteBinding binding = FragmentRouteBinding.inflate(inflater, container, false);
        mapView = binding.map;
        infoLayout = binding.infoLayout;
        settingLayout = binding.settingLayout;
        startDate = binding.startDateEt;
        endDate = binding.endDateEt;
        gps = binding.gpsCb;
        wifi = binding.wifiCb;
        lbs = binding.lbsCb;
        slow = binding.slowRb;
        normal = binding.normalRb;
        fast = binding.fastRb;
        icon = binding.iconCb;
        line = binding.lineCb;
        search = binding.searchB;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        device = getMyGPSTracker().getCurrentDevice();

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        final Calendar calendar = Calendar.getInstance();

        endDate.setText(dateFormat.format(calendar.getTime()));
        endDate.setInputType(InputType.TYPE_NULL);
        endDate.setOnClickListener(v -> endDateDialog());
        endDate.setOnFocusChangeListener((v, hasFocus) -> endDateDialog());

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        startDate.setText(dateFormat.format(calendar.getTime()));
        startDate.setInputType(InputType.TYPE_NULL);
        startDate.setOnClickListener(v -> startDateDialog());
        startDate.setOnFocusChangeListener((v, hasFocus) -> startDateDialog());

        slow.setOnClickListener(v -> {
            slow.setChecked(true);
            normal.setChecked(false);
            fast.setChecked(false);
        });

        normal.setOnClickListener(v -> {
            slow.setChecked(false);
            normal.setChecked(true);
            fast.setChecked(false);
        });

        fast.setOnClickListener(v -> {
            slow.setChecked(false);
            normal.setChecked(false);
            fast.setChecked(true);
        });

        search.setOnClickListener(v -> {
            settingLayout.setVisibility(View.GONE);
            infoLayout.setVisibility(View.VISIBLE);

            try {
                final String imei = getMyGPSTracker().getCurrentDevice().getImei();
                final Date startDate = dateFormat.parse(this.startDate.getText().toString());
                final Date endDate = dateFormat.parse(this.endDate.getText().toString());
                GPS365Repository.getInstance().getRoute(imei, queryDateFormat.format(startDate), queryDateFormat.format(endDate), new RouteCallback() {
                    @Override
                    public void onSuccess(RoutePoint[] response) {
                        for (RoutePoint point : response) {
                            switch (Utils.getLocationSource(point.source)) {
                                case GPS:
                                    if (!gps.isChecked()) {
                                        continue;
                                    }
                                    break;
                                case WIFI:
                                    if (!wifi.isChecked()) {
                                        continue;
                                    }
                                    break;
                                case LBS:
                                    if (!lbs.isChecked()) {
                                        continue;
                                    }
                                    break;
                            }

                            String[] coords = point.google.split(",");
                            LatLng latLng = new LatLng(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
                            googleMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(point.updatetime)
                            );
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                        }
                    }

                    @Override
                    public void onError(RouteError error) {

                    }
                });
            } catch (ParseException e) {
                Toast.makeText(getContext(), "There was an error parsing the dates. Check that the format is correct.x", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startDateDialog() {
        DatePickerDialog dateDialog = DatePickerDialog.newInstance((view, year, monthOfYear, dayOfMonth) -> {
            TimePickerDialog timeDialog
                    = TimePickerDialog.newInstance((view1, hourOfDay, minute, second)
                    -> formatAndSetDate(startDate, dayOfMonth, monthOfYear, year, hourOfDay, minute), DateFormat.is24HourFormat(getContext()));

            timeDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
            timeDialog.vibrate(false);
            timeDialog.show(getParentFragmentManager(), "TimePickerDialog");
        });

        dateDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
        dateDialog.vibrate(false);
        dateDialog.show(getParentFragmentManager(), "DatePickerDialog");
    }

    private void endDateDialog() {
        DatePickerDialog dateDialog = DatePickerDialog.newInstance((view, year, monthOfYear, dayOfMonth) -> {
            TimePickerDialog timeDialog
                    = TimePickerDialog.newInstance((view1, hourOfDay, minute, second)
                    -> formatAndSetDate(endDate, dayOfMonth, monthOfYear, year, hourOfDay, minute), DateFormat.is24HourFormat(getContext()));

            timeDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
            timeDialog.vibrate(false);
            timeDialog.show(getParentFragmentManager(), "TimePickerDialog");
        });

        dateDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
        dateDialog.vibrate(false);
        dateDialog.show(getParentFragmentManager(), "DatePickerDialog");
    }

    private void formatAndSetDate(EditText editText, int day, int month, int year, int hour, int minute) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        editText.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        final UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);

        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //updateLocation();
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
            public void onError(PositionError error) {
                Toast.makeText(getContext(), "Something went wrong while updating the location.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}