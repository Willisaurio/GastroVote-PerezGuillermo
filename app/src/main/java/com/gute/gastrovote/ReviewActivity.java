package com.gute.gastrovote;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;



public class ReviewActivity extends AppCompatActivity {


    private int barNumber = 1;
    private Bar bar;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.review_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Bundle datos = getIntent().getExtras();
        if (datos == null) {
            finish();
            return;
        }
        barNumber = datos.getInt("barNumber");
        //noinspection deprecation
        this.bar = (Bar) datos.get("selectedBar");
    }

    private void confirmClearingDialog() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.custom_alert_dialog);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(getString(R.string.clear_confirm_dialog))
                .setTitle(getString(R.string.clear_confirm_dialog_title));

        // Add the buttons
        builder.setPositiveButton(getString(R.string.clear_lwc), (dialog, id) -> {
            // User clicked the "Delete" button
            clearForm();
        });

        builder.setNegativeButton(getString(R.string.cancel_lwc), (dialog, id) -> {
            // User canceled the dialog
            dialog.dismiss();
        });

        // 3. Get the AlertDialog from create() and show it
        AlertDialog dialog = builder.create();
        dialog.show();
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

        TextView tv = findViewById(R.id.tvMeGusta);
        SwitchMaterial s = findViewById(R.id.swMeGusta);
        if (s.isChecked()){
            tv.setText(R.string.i_liked);
        } else {
            tv.setText(R.string.did_u_like);

        }
    }




    public void checkStudentId(View V){
        CheckBox c = findViewById(R.id.cbEstudiante);
        if(c.isChecked()){
            Toast.makeText(this, R.string.carne, Toast.LENGTH_LONG).show();
        }
    }



//    public int countDoneBars(){
//        int count = 0;
//        for (Bar bar : bars.values()) {
//            if (bar.isDone()) {
//                count++;
//            }
//        }
//        return count;
//    }

    public void calcular(View view){
        if (this.bar == null){
            Utils.sayShort(this, "#01DEBUG: NULL bar");
        }
        RatingBar rb = findViewById(R.id.ratingBar); // calificacion
        if (rb.getRating() == 0) {Utils.sayLong(this, getResources().getString(R.string.not_rated_2));return;} // check if rating is greater than 0
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
            price = this.bar.getPrecio() * multi;
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
    }
}
