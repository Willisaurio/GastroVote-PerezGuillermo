package com.gute.gastrovote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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




        // --- --- declaration of actionListeners --- --- //

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addMenuProvider(new MenuProvider() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {

                menuInflater.inflate(R.menu.main_activity_menubar, menu);

                if (menu instanceof androidx.appcompat.view.menu.MenuBuilder) {
                    androidx.appcompat.view.menu.MenuBuilder menuBuilder =
                            (androidx.appcompat.view.menu.MenuBuilder) menu;
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

        });
        // --- --- END declaration of actionListeners --- --- //




    } // END OF onCreate




    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PROGRESS_KEY, progress);
        }
    // --- --- ATRIBUTES --- --- //
    private int progress;
    public final String PROGRESS_KEY = "progress";

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

    public void updateProgress(){
        ProgressBar p = findViewById(R.id.progressBar);
        p.setProgress(0);
        TextView tv = findViewById(R.id.tvProgress);
        tv.setText(getString(R.string.progress_message,0, 0));
    }



    // --- --- END UTILS --- --- //
    public void gotoList(View v){
        Intent intent = new Intent(this, BarListActivity.class);
        startActivity(intent);
    }
}

