package me.iscle.mygpstracker.activity;

import androidx.appcompat.app.AppCompatActivity;

import me.iscle.mygpstracker.MyGPSTracker;

public class BaseActivity extends AppCompatActivity {
    public MyGPSTracker getMyGPSTracker() {
        return (MyGPSTracker) getApplication();
    }
}
