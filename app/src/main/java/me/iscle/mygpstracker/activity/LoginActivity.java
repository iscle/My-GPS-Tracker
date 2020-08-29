package me.iscle.mygpstracker.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import me.iscle.mygpstracker.Device;
import me.iscle.mygpstracker.databinding.ActivityLoginBinding;
import me.iscle.mygpstracker.network.GPS365Repository;
import me.iscle.mygpstracker.network.callback.PositionCallback;
import me.iscle.mygpstracker.network.response.PositionResponse;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String PREFERENCE_IMEI = "imei";
    private static final String PREFERENCE_PASSWORD = "password";
    private static final String PREFERENCE_REMEMBER_CREDENTIALS = "remember_credentials";

    private EditText imei;
    private EditText password;
    private CheckBox rememberCredentials;
    private Button logIn;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imei = binding.imei;
        password = binding.password;
        rememberCredentials = binding.rememberCredentials;
        logIn = binding.logIn;

        sharedPreferences = getSharedPreferences(getClass().getName(), MODE_PRIVATE);

        imei.setText(sharedPreferences.getString(PREFERENCE_IMEI, ""));
        password.setText(sharedPreferences.getString(PREFERENCE_PASSWORD, ""));
        rememberCredentials.setChecked(sharedPreferences.getBoolean(PREFERENCE_REMEMBER_CREDENTIALS, false));

        if (sharedPreferences.getBoolean(PREFERENCE_REMEMBER_CREDENTIALS, false)) {
            onClick(logIn);
        }

        logIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String imei = this.imei.getText().toString();
        final String password = this.password.getText().toString();

        editor.putString(PREFERENCE_IMEI, imei);
        editor.apply();

        GPS365Repository.getInstance().imeiLogin(imei, password, new PositionCallback() {
            @Override
            public void onSuccess(PositionResponse response) {
                if (rememberCredentials.isChecked()) {
                    editor.putString(PREFERENCE_PASSWORD, password);
                    editor.putBoolean(PREFERENCE_REMEMBER_CREDENTIALS, true);
                    editor.apply();
                }

                getMyGPSTracker().setCurrentDevice(new Device(imei, password));

                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finishAffinity();
            }

            @Override
            public void onError(LoginError error) {
                switch (error) {
                    case NETWORK_ERROR:
                        Toast.makeText(LoginActivity.this, "A network error occurred. Please try again.", Toast.LENGTH_LONG).show();
                        break;
                    case SERVER_ERROR:
                        Toast.makeText(LoginActivity.this, "A server error occurred. Please try again.", Toast.LENGTH_LONG).show();
                        break;
                    case PASSWORD_ERROR:
                        Toast.makeText(LoginActivity.this, "Wrong password.", Toast.LENGTH_LONG).show();
                        editor.remove(PREFERENCE_PASSWORD);
                        editor.remove(PREFERENCE_REMEMBER_CREDENTIALS);
                        editor.apply();
                        break;
                }
            }
        });
    }
}