package com.gute.gastrovote;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.Serializable;
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


        if (savedInstanceState != null) {
            // Restore the HashMap from the bundle
            bars = (HashMap<Integer, Bar>) savedInstanceState.getSerializable(STATE_MAP);
            selectedBar = savedInstanceState.getInt(SELECTED_BAR)-1;
        } else {
            bars = new HashMap<>();
            bars.put(1, new Bar(getString(R.string.bar_1_name), getString(R.string.tapa_1_name), getString(R.string.tapa_1_desc), 3.25, true, 5f));
            bars.put(2, new Bar(getString(R.string.bar_2_name), getString(R.string.tapa_2_name), getString(R.string.tapa_2_desc), 4.50, 2.5f));
            bars.put(3, new Bar(getString(R.string.bar_3_name), getString(R.string.tapa_3_name), getString(R.string.tapa_3_desc), 6.40, 4f));
            bars.put(4, new Bar(getString(R.string.bar_4_name), getString(R.string.tapa_4_name), getString(R.string.tapa_4_desc), 4.50, 4.5f));
            bars.put(5, new Bar(getString(R.string.bar_5_name), getString(R.string.tapa_5_name), getString(R.string.tapa_5_desc), 2.80, true, 3.5f));
            bars.put(6, new Bar(getString(R.string.bar_6_name), getString(R.string.tapa_6_name), getString(R.string.tapa_6_desc), 6.00, 3f));
            bars.put(7, new Bar(getString(R.string.bar_7_name), getString(R.string.tapa_7_name), getString(R.string.tapa_7_desc), 8.15));
            bars.put(8, new Bar(getString(R.string.bar_8_name), getString(R.string.tapa_8_name), getString(R.string.tapa_8_desc), 9.50, 4.5f));
            selectedBar = 0;
        }

        // --- --- declaration of actionListeners --- --- //
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
        barForward(null);
        // set an onRatingBarChangeListener to ratingBar to update the rating
        ratingBar.setOnRatingBarChangeListener((rBar, rating, fromUser) -> {
            updateRating(rating);
        });

        // clear form actionListener
        Button clearBtn = findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(v -> confirmClearingDialog());

        // Like Switch actionListener
        SwitchMaterial swMeGusta = findViewById(R.id.swMeGusta);
        swMeGusta.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateLikeTextView();
        });

        // --- --- END declaration of actionListeners --- --- //

        // update tvMeGusta on onCreate to survive defaulting while onPause
        updateLikeTextView();

        // update tvProgress
        updateProgress();


    } // END OF onCreate
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the HashMap (ensure your keys/values implement Serializable)
        outState.putSerializable(STATE_MAP, (HashMap<Integer, Bar>) bars);
        outState.putInt(SELECTED_BAR, selectedBar);
    }
    // --- --- ATRIBUTES --- --- //
    Map<Integer, Bar> bars;
    private static final String STATE_MAP = "state_map";
    private static final String SELECTED_BAR = "selected_bar";
    private int selectedBar;
    private boolean twiceFixer = true;

    // --- --- UTILS --- --- //
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
        try{
            tvBar.setText(Objects.requireNonNull(this.bars.get(this.selectedBar)).getBarName());
            tvTitle.setText(Objects.requireNonNull(this.bars.get(this.selectedBar)).getTapaName());
            tvSubtitle.setText(Objects.requireNonNull(this.bars.get(this.selectedBar)).getTapaDesc());
            tvPrice.setText(Objects.requireNonNull(this.bars.get(this.selectedBar)).getPrecio() + "€");
            cardRatingBar.setRating(Objects.requireNonNull(this.bars.get(this.selectedBar)).getRating());
            // external methods
            updateHeartIc();
            updateDoneIc();
        } catch (NullPointerException e){
            this.setDefaulRating();
        }
    }

    public void swFav(View view){
        try {
            boolean isFavNow = !Objects.requireNonNull(this.bars.get(this.selectedBar)).isFav();
            Objects.requireNonNull(this.bars.get(this.selectedBar)).setFav(isFavNow);
            if(isFavNow) {
                this.sayShort(getResources().getString(R.string.added_to_favs));
            } else {
                this.sayShort(getResources().getString(R.string.removed_from_favs));
            }
            // Update heart ic
            this.updateHeartIc();
        }
        catch (NullPointerException e){
            ImageView ivFavorite = findViewById(R.id.ivFavorite);
            ivFavorite.setImageResource(R.drawable.ic_heart_grayscale);
        }
    }
    public void updateHeartIc(){
        ImageView ivFavorite = findViewById(R.id.ivFavorite);
        if( Objects.requireNonNull(this.bars.get(this.selectedBar)).isFav() ) {
            ivFavorite.setImageResource(R.drawable.ic_heart);
        } else {
            ivFavorite.setImageResource(R.drawable.ic_heart_grayscale);
        }
    }


    public void updateDoneIc(){
        ImageView ivDone = findViewById(R.id.ivDone);
        try {
            if( Objects.requireNonNull(this.bars.get(this.selectedBar)).isDone() ) {
                ivDone.setVisibility(View.VISIBLE);
            } else {
                ivDone.setVisibility(View.INVISIBLE);
            }
        } catch (NullPointerException e) {
            ivDone.setVisibility(View.INVISIBLE);
        }
    }
    public void updateRating(){
        try {
            RatingBar ratingBar = findViewById(R.id.ratingBar);
            updateRating(ratingBar.getRating());
        } catch (NullPointerException e){
            this.setDefaulRating();
        }
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
        if( rating == 0.0f){
            tvRating.setText(R.string.not_rated);
        } else if( rating <= 1.0f){
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

    public void updateProgress(){
        ProgressBar p = findViewById(R.id.progressBar);
        p.setProgress(countDoneBars());
        TextView tv = findViewById(R.id.tvProgress);
        tv.setText(getString(R.string.progress_message, countDoneBars(), bars.size()));
    }

    public void setDefaulRating(){
        TextView tvBar = findViewById(R.id.tvBar),
                tvTitle = findViewById(R.id.tvTitle),
                tvSubtitle = findViewById(R.id.tvSubtitle),
                tvPrice = findViewById(R.id.tvPrice);
        RatingBar cardRatingBar = findViewById(R.id.cardRatingBar);
        ImageView ivFavorite = findViewById(R.id.ivFavorite);
        tvBar.setText(R.string.bar_name);
        tvTitle.setText(R.string.tapa);
        tvSubtitle.setText(R.string.description);
        tvPrice.setText(R.string.precio);
        cardRatingBar.setRating(3.5f);
        ivFavorite.setImageResource(R.drawable.ic_heart_grayscale);
    }

    public void checkStudentId(View V){
        CheckBox c = findViewById(R.id.cbEstudiante);
        if(c.isChecked()){
            Toast.makeText(this, R.string.carne, Toast.LENGTH_LONG).show();
        }
    }


    public int countDoneBars(){
        int count = 0;
        for (Bar bar : bars.values()) {
            if (bar.isDone()) {
                count++;
            }
        }
        return count;
    }

    private void confirmClearingDialog() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.custom_alert_dialog);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(getString(R.string.clear_confirm_dialog))
                .setTitle(getString(R.string.clear_confirm_dialog_title));

        // Add the buttons
        builder.setPositiveButton(getString(R.string.clear_lwc), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button
                clearForm();
            }
        });

        builder.setNegativeButton(getString(R.string.cancel_lwc), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User canceled the dialog
                dialog.dismiss();
            }
        });

        // 3. Get the AlertDialog from create() and show it
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    // --- --- END UTILS --- --- //





    public void calcular(View view){
        Bar bar;
        try {
            bar = Objects.requireNonNull(this.bars.get(this.selectedBar));
        } catch (NullPointerException e){
            sayLong(getResources().getString(R.string.select_a_bar));
            return;
        }

        RatingBar rb = findViewById(R.id.ratingBar); // calificacion
        if (rb.getRating() == 0) {sayLong(getResources().getString(R.string.not_rated_2));return;} // check if rating is greater than 0
        CheckBox ch = findViewById(R.id.cbEstudiante), cbVegan = findViewById(R.id.cbVegan), cbGlutenFree = findViewById(R.id.cbGlutenFree), cbSpicy = findViewById(R.id.cbSpicy); // checkBox estudiante
        SeekBar sb = findViewById(R.id.seekBar); // calidad-precio
        SwitchMaterial sw = findViewById(R.id.swMeGusta); // me gusta
        RadioGroup rg = findViewById(R.id.sizeRadioGroup); // tamaño
        TextView tvRating = findViewById(R.id.tvRating); // valoracion (txt)


        int  rBtId = rg.getCheckedRadioButtonId();
        String size, bool;
        byte multi;
        boolean stDisc = ch.isChecked(), vegan = cbVegan.isChecked(), glutenFree = cbGlutenFree.isChecked(), spicy = cbSpicy.isChecked();
        if (rBtId == R.id.sizeRb2) {
            size = getResources().getString(R.string.size_half_2);
            multi = 2;
        } else if (rBtId == R.id.sizeRb3) {
            size = getResources().getString(R.string.size_ration);
            multi = 3;
        } else {
            // defaults to 1
            size = getResources().getString(R.string.size_tapa);
            multi = 1;
        }
        if ( sw.isChecked() ){
            bool = getResources().getString(R.string.yes);
        } else {
            bool = getResources().getString(R.string.no);
        }

        double price;
        try {
            price = Objects.requireNonNull(this.bars.get(this.selectedBar)).getPrecio() * multi;
        } catch (NullPointerException e){
            price = 0;
        }

        String textRating;
        if (rb.getRating() % 1 == 0){
            textRating = String.valueOf((int) rb.getRating());
        } else {
            textRating = String.valueOf(rb.getRating());
        }

        /*
         * String result layout
         * ${tapa} - ${barName}\n
         * ${size} - ${rating}/5 (${ratingText})\n
         * [${vegan}, ${glutenFree}, ${spicy}]\n
         * Value for money: ${valueForMoney}/10 - ${like}\n
         * Cost: ${cost} x ${size} = ${finalCost} €\n
         * [Discount: ${discount}]\n]
         * Final cost {}
         * Comment:
         * ${comment}
         */
        String result = bar.getTapaName()+" - "+bar.getBarName()+"\n" +
                getResources().getString(R.string.size)+": "+size+" - "+textRating+"/5 ("+tvRating.getText()+")\n";

        if ( vegan ) {
            result += getString(R.string.vegetarian);
            if ( glutenFree || spicy ) {
                result += ", ";
            } else {
                result += "\n";
            }
        }
        if ( glutenFree ) {
            result += getString(R.string.gluten_free);
            if ( spicy ) {
                result += ", ";
            } else {
                result += "\n";
            }
        }
        if ( spicy ) {
            result += getString(R.string.spicy)+"\n";
        }

        result += getResources().getString(R.string.value_for_money)+": "+sb.getProgress()+"/10 - "+getResources().getString(R.string.like)+": "+bool+"\n" +
                getResources().getString(R.string.res_price)+bar.getPrecio()+" x "+multi+" = "+price+" €\n";
        if (stDisc) {
            result += getResources().getString(R.string.res_disc)+"-"+((double) Math.round(price*10)/100.0)+" €\n";
            // aplicar descuento
            price = price * 0.9;
            // round to 2 decimals
            price = Math.round(price * 100.0) / 100.0;
        }
        result += getResources().getString(R.string.res_final_price)+price+" €";


        EditText editTextComment = findViewById(R.id.editTextTextMultiLine);
        String comment = editTextComment.getText().toString();
        if (!comment.isBlank()){
            result += "\n"+getResources().getString(R.string.comment)+":\n"+comment;
        }

        TextView resumen = findViewById(R.id.tvResumen);
        resumen.setText(result);
        if(ch.isChecked()){
            Toast.makeText(this, R.string.calculated, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.not_discount, Toast.LENGTH_LONG).show();
        }

        // completar bar
        bar.setDone(true);
        updateProgress();
        updateDoneIc();
    }


    public void clearForm() {
        RadioButton rb1 = findViewById(R.id.sizeRb1), rb2 = findViewById(R.id.sizeRb2), rb3 = findViewById(R.id.sizeRb3);
        rb1.setChecked(true);
        rb2.setChecked(false);
        rb3.setChecked(false);
        SeekBar sb = findViewById(R.id.seekBar);
        sb.setProgress(5);
        RatingBar rb = findViewById(R.id.ratingBar);
        rb.setRating(0);
        TextView tvRating = findViewById(R.id.tvRating);
        tvRating.setText(R.string.not_rated);
        EditText comment = findViewById(R.id.editTextTextMultiLine);
        comment.setText("");
        TextView tvResumen = findViewById(R.id.tvResumen);
        tvResumen.setText(R.string.final_price);

        CheckBox cbVegan = findViewById(R.id.cbVegan), cbGlutenFree = findViewById(R.id.cbGlutenFree), cbSpicy = findViewById(R.id.cbSpicy);
        cbVegan.setChecked(false);
        cbGlutenFree.setChecked(false);
        cbSpicy.setChecked(false);


        SwitchMaterial swMeGusta = findViewById(R.id.swMeGusta);
        swMeGusta.setChecked(false);

        CheckBox cbEstudiante = findViewById(R.id.cbEstudiante);
        cbEstudiante.setChecked(false);

        Toast.makeText(this, R.string.cleared, Toast.LENGTH_LONG).show();
    }

    public void updateLikeTextView(){
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





















}
// ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   --- //
// ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   CLASE BAR   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   --- //
// ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   --- //
class Bar implements Serializable {
    private String barName;
    private String tapaName;
    private String tapaDesc;
    private double precio;
    private boolean done, fav;
    private float rating;
    private String review = "";

    //Constructors
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
    public String getReview() {
        return review;
    }
    public void setReview(String review) {
        this.review = review;
    }
}
