package com.gute.gastrovote;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

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
        bars = new HashMap<>();
        bars.put(1, new Bar("Bar Paco", "Tortilla de patatas", "Un clásico de la cocina española", 3.25, true, 5f));
        bars.put(2, new Bar("Bar Stop", "Pulpo", "Una única rodaja de pulpo", 4.50, 2.5f));
        bars.put(3, new Bar("3 Espadas", "calamares a la romana", "Calamares con un rebozado crujiente", 6.40, 4f));
        bars.put(4, new Bar("Bar Cobatillas", "rollito de salmón","Rollitos de salmón con queso fresco y aguacate", 4.50, 4.5f));
        bars.put(5, new Bar("El Guapo", "Croquetas de jamón","croquetas rellenas de tacos de jamón serrano", 2.80, true, 3.5f));
        bars.put(6, new Bar("Rosarito", "Alcachofas con jamón", "inaudito", 6.00, 3f));
        bars.put(7, new Bar("Bar Santomera", "Jamón serrano y parmesano","tacos o cortes de jamón con queso parmesano rallado" ,8.15));
        bars.put(8, new Bar("Bar Alquerías", "Almejas a la plancha", "Una esquisitez digna de las playas", 9.50, 4.5f));

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the TextView with the current progress value
                TextView tvScore = findViewById(R.id.tvScore);
                tvScore.setText(progress + "/10");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        selectedBar = 0;
        barForward(null);
        // set an onRatingBarChangeListener to ratingBar to update the rating
        ratingBar.setOnRatingBarChangeListener((rBar, rating, fromUser) -> {
            updateRating(rating);
        });


    } // END OF onCreate
    Map<Integer, Bar> bars;
    private int selectedBar;
    private boolean twiceFixer = true;
    @SuppressLint("SetTextI18n")
    public void changeBar(boolean forward){
        if(forward){
            selectedBar++;
            if(selectedBar > bars.size()){
                selectedBar = 1;
            }
        } else {
            selectedBar--;
            if(selectedBar < 1){
                selectedBar = bars.size();
            }
        }
        TextView tvBar = findViewById(R.id.tvBar),
                tvTitle = findViewById(R.id.tvTitle),
                tvSubtitle = findViewById(R.id.tvSubtitle),
                tvPrice = findViewById(R.id.tvPrice);
        RatingBar cardRatingBar = findViewById(R.id.cardRatingBar);
        ImageView ivFavorite = findViewById(R.id.ivFavorite);
        try{
            tvBar.setText(this.bars.get(this.selectedBar).getBarName());
            tvTitle.setText(this.bars.get(this.selectedBar).getTapaName());
            tvSubtitle.setText(this.bars.get(this.selectedBar).getTapaDesc());
            tvPrice.setText(this.bars.get(this.selectedBar).getPrecio() + "€");
            cardRatingBar.setRating(this.bars.get(this.selectedBar).getRating());
            if( this.bars.get(this.selectedBar).isFav() ) {
                ivFavorite.setImageResource(R.drawable.ic_heart);
            } else {
                ivFavorite.setImageResource(R.drawable.ic_heart_grayscale);
            }
        } catch (NullPointerException e){
            tvBar.setText(R.string.bar_name);
            tvTitle.setText(R.string.tapa);
            tvSubtitle.setText(R.string.description);
            tvPrice.setText(R.string.precio);
            cardRatingBar.setRating(0.0f);
            ivFavorite.setVisibility(View.INVISIBLE);
        }
    }
    //TODO add actionListener to heart_ic

    public void sayLong(String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
    public void sayShort(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    public void barForward(View view){
        this.changeBar(true);
    }
    public void barBackward(View view){
        this.changeBar(false);
    }

    public void updateRating(float rating){
        if (selectedBar == 0) {
            RatingBar ratingBar = findViewById(R.id.ratingBar);
            ratingBar.setRating(3.5f);
            TextView tvRating = findViewById(R.id.tvRating);
            tvRating.setText(R.string.review_3);
            // for some reason this is running twice, so i'm fixing it my way
            if (this.twiceFixer) { // fix it visually
                sayShort(getResources().getString(R.string.select_a_bar));
            }
            this.twiceFixer = !this.twiceFixer;
            return;
        }
        TextView tvRating = findViewById(R.id.tvRating);
        if( rating <= 1.0f){
            tvRating.setText(R.string.review_1);
        } else if( rating <= 2.0f){
            tvRating.setText(R.string.review_2);
        } else if( rating <= 3.0f){
            tvRating.setText(R.string.review_3);
        } else if( rating <= 4.0f){
            tvRating.setText(R.string.review_4);
        } else {
            tvRating.setText(R.string.review_5);
        }
    }





    public void calcular(View view){
        if(selectedBar == 0){
            return;
        }

        CheckBox ch = findViewById(R.id.cbEstudiante);
        RatingBar rb = findViewById(R.id.ratingBar);
        updateRating(rb.getRating());
        double price;
        try {
            price = this.bars.get(this.selectedBar).getPrecio();
        } catch (NumberFormatException e){
            Toast.makeText(this, R.string.bad_price, Toast.LENGTH_LONG).show();
            return;
        } catch (NullPointerException e){
            price = 0;
        }
        if(ch.isChecked()){
            //aplicar descuento
            price = price *0.9;
        }
        TextView resumen = findViewById(R.id.tvResumen);
        //TODO resumen
        if(ch.isChecked()){
            Toast.makeText(this, R.string.calculated, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.not_discount, Toast.LENGTH_LONG).show();
        }
    }




    public void clear(View view) {

        TextView tvResumen = findViewById(R.id.tvResumen);
        tvResumen.setText(R.string.final_price);
        SwitchMaterial swMeGusta = findViewById(R.id.swMeGusta);
        swMeGusta.setChecked(false);
        CheckBox cbEstudiante = findViewById(R.id.cbEstudiante);
        cbEstudiante.setChecked(false);

        Toast.makeText(this, R.string.cleared, Toast.LENGTH_LONG).show();
    }

    public void like(View v){
        if (selectedBar == 0){
            SwitchMaterial s = findViewById(R.id.swMeGusta);
            s.setChecked(false);
            return;
        }
        TextView tv = findViewById(R.id.tvMeGusta);
        SwitchMaterial s = findViewById(R.id.swMeGusta);
        if (s.isChecked()){
            tv.setText(R.string.i_liked);
        } else {
            tv.setText(R.string.did_u_like);

        }
    }
    public void st(View V){
        CheckBox c = findViewById(R.id.cbEstudiante);
        if(c.isChecked()){
            Toast.makeText(this, R.string.carne, Toast.LENGTH_LONG).show();
        }
    }
}

class Bar {
    private String barName;
    private String tapaName;
    private String tapaDesc;
    private double precio;
    private boolean done, fav;
    private float rating;


    public Bar (String barName, String tapa, double precio){
        this.barName = barName;
        this.tapaName = tapa;
        this.tapaDesc = "";
        this.precio = precio;
        this.done = false;
        this.fav = false;
        this.rating = 0.0f;
    }
    public Bar (String barName, String tapa, double precio, float rating){
        this.barName = barName;
        this.tapaName = tapa;
        this.tapaDesc = "";
        this.precio = precio;
        this.done = false;
        this.fav = false;
        this.rating = rating;
    }
    public Bar (String barName, String tapa, double precio, boolean fav, float rating){
        this.barName = barName;
        this.tapaName = tapa;
        this.tapaDesc = "";
        this.precio = precio;
        this.done = false;
        this.fav = fav;
        this.rating = rating;
    }
    public Bar (String barName, String tapa, String desc, double precio){
        this.barName = barName;
        this.tapaName = tapa;
        this.precio = precio;
        this.tapaDesc = desc;
        this.done = false;
        this.fav = false;
        this.rating = 0.0f;
    }
    public Bar (String barName, String tapa, String desc, double precio, float rating){
        this.barName = barName;
        this.tapaName = tapa;
        this.precio = precio;
        this.tapaDesc = desc;
        this.done = false;
        this.fav = false;
        this.rating = rating;
    }
    public Bar (String barName, String tapa, String desc, double precio, boolean fav, float rating){
        this.barName = barName;
        this.tapaName = tapa;
        this.tapaDesc = desc;
        this.precio = precio;
        this.done = false;
        this.fav = fav;
        this.rating = rating;
    }

    // Getters y setters
    public String getBarName() {
        return barName;
    }
    public void setBarName(String barName) {
        this.barName = barName;
    }
    public String getTapaName() {
        return tapaName;
    }
    public void setTapaName(String tapaName) {
        this.tapaName = tapaName;
    }
    public String getTapaDesc() {
        return tapaDesc;
    }
    public void setTapaDesc(String tapaDesc) {
        this.tapaDesc = tapaDesc;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public boolean isDone() {
        return done;
    }
    public void setDone(boolean done) {
        this.done = done;
    }
    public boolean isFav() {
        return fav;
    }
    public void setFav(boolean fav) {
        this.fav = fav;
    }
    public float getRating() {
        return rating;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }
}
