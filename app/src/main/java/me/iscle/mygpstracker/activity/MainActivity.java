package me.iscle.mygpstracker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import me.iscle.mygpstracker.R;
import me.iscle.mygpstracker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}