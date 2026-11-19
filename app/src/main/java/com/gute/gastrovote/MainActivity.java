package com.gute.gastrovote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.MenuProvider;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;



public class MainActivity extends AppCompatActivity {

    @SuppressWarnings({"unchecked", "deprecation"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        updateInternetConnectionStatus();




        // --- --- declaration of actionListeners --- --- //

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addMenuProvider(new MenuProvider() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {

                menuInflater.inflate(R.menu.main_activity_menubar, menu);

                if (menu instanceof MenuBuilder) {
                    MenuBuilder menuBuilder =
                            (MenuBuilder) menu;
                    //noinspection RestrictedApi
                    menuBuilder.setOptionalIconsVisible(true);
                }

            }
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.settings_menu_opt) {
                    gotoSettings();
                    return true;
                } else if (id == R.id.about_menu_opt) {
                    gotoAbout();
                    return true;
                }


                return false;
            }

        }); // End of toolbar actionListeners



        // --- --- END declaration of actionListeners --- --- //




    } // END OF onCreate




    @Override
    protected void onStart() {
        super.onStart();
        updateInternetConnectionStatus();
        registerNetworkCallback();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterNetworkCallback();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PROGRESS_KEY, progress);
    }
    // --- --- ATRIBUTES --- --- //
    private int progress;
    public final String PROGRESS_KEY = "progress";
    private ConnectivityManager.NetworkCallback networkCallback;

    // --- --- UTILS --- --- //
    //post production fix
    private void sayShort(String s){
        Utils.sayShort(this,s);
    }
    private void sayShort(Context c, String s){
        Utils.sayShort(c,s);
    }
    private void sayLong(String s){
        Utils.sayLong(this,s);
    }
    private void sayLong(Context c, String s){
        Utils.sayLong(c,s);
    }


    // -- GOTOs menu actions -- //
    public void gotoSettings(){
        // TODO intent to settings
        this.sayShort("Settings not implemented");
    }
    private void gotoAbout() {
        this.sayShort("About not implemented");
    }
    public void gotoList(View v){
        Intent intent = new Intent(this, BarListActivity.class);
        startActivity(intent);
    }
    public void updateProgress(){
        ProgressBar p = findViewById(R.id.progressBar);
        p.setProgress(0);
        TextView tv = findViewById(R.id.tvProgress);
        tv.setText(getString(R.string.progress_message,0, 0));
    }



    // --- --- END UTILS --- --- //


    // NETWORK UTILITIES
    private void updateInternetConnectionStatus() {

        // Set the text of the greeting to Hello + username with placeholders
        TextView tv = findViewById(R.id.tvTitulo);
        tv.setText(getString(R.string.saludo, PreferencesHandler.getUsername(this)));
        // check if there is connection to the internet and what type is it
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        TextView tvConnInfo = findViewById(R.id.tvConnectionInfo);
        boolean connected = false;
        if (cm != null) {
            Network network = cm.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
                if (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        tvConnInfo.setText(getString(R.string.conn_info_WiFi));
                        connected = true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        tvConnInfo.setText(getString(R.string.conn_info_mobile));
                        connected = true;
                    }
                }
            }
        }
        if (!connected) {
            tvConnInfo.setText(getString(R.string.conn_info_noConn));
        }
    }
    private void registerNetworkCallback() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null && networkCallback == null) {
            networkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NonNull Network network) {
                    runOnUiThread(() -> updateInternetConnectionStatus());
                }

                @Override
                public void onLost(@NonNull Network network) {
                    runOnUiThread(() -> updateInternetConnectionStatus());
                }

                @Override
                public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                    runOnUiThread(() -> updateInternetConnectionStatus());
                }
            };
            cm.registerDefaultNetworkCallback(networkCallback);
        }
    }

    private void unregisterNetworkCallback() {
        if (networkCallback != null) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                cm.unregisterNetworkCallback(networkCallback);
            }
            networkCallback = null;
        }
    }
}

