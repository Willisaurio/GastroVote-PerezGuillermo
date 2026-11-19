package com.gute.gastrovote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;

public class BarListActivity extends AppCompatActivity {

    private int barNumber;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.bar_list_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState != null) {
            // Restore the HashMap from the bundle
            //noinspection deprecation,unchecked
            bars = (HashMap<Integer, Bar>) savedInstanceState.getSerializable(STATE_MAP);
        } else {
            bars = new HashMap<>();
            bars.put(1, new Bar("Bar Paco", "Tortilla de patatas", "Un clásico de la cocina española", 3.25, true, 5f));
            bars.put(2, new Bar("Bar Stop", "Pulpo", "Una única rodaja de pulpo", 4.50, 2.5f));
            bars.put(3, new Bar("3 Espadas", "Calamares a la romana", "Calamares con un rebozado crujiente", 6.40, 4f));
            bars.put(4, new Bar("Bar Cobatillas", "Rollito de salmón", "Rollitos de salmón con queso fresco y aguacate", 4.50, 4.5f));
            bars.put(5, new Bar("Bar El Guapo", "Croquetas de jamón", "croquetas rellenas de tacos de jamón serrano", 2.80, true, 3.5f));
            bars.put(6, new Bar("Rosarito", "Alcachofas con jamón", "inaudito", 6.00, 3f));
            bars.put(7, new Bar("Bar Santomera", "Jamón serrano y parmesano", "tacos o cortes de jamón con queso parmesano rallado", 8.15));
            bars.put(8, new Bar("Bar Alquerías", "Almejas a la plancha", "Una esquisitez digna de las playas", 9.50, 4.5f));

        }
        assert bars != null;
        barNumber = bars.size();

        // INFLATER
        LinearLayout linearLayoutBars = findViewById(R.id.linearLayoutBars);
        if (linearLayoutBars != null) {
            for (Bar bar : bars.values()) {
                View cardView = getLayoutInflater().inflate(R.layout.bar_list_card, linearLayoutBars, false);

                TextView tvTitle = cardView.findViewById(R.id.tvTitle);
                ImageView ivFavorite = cardView.findViewById(R.id.ivFavorite);
                ImageView ivDone = cardView.findViewById(R.id.ivDone);
                TextView tvSubtitle = cardView.findViewById(R.id.tvSubtitle);

                if (tvTitle != null) {
                    tvTitle.setText(bar.getBarName());
                }
                if (tvSubtitle != null) {
                    tvSubtitle.setText(bar.getTapaName());
                }
                if (ivFavorite != null) {
                    ivFavorite.setImageResource(bar.isFav() ? R.drawable.ic_heart : R.drawable.ic_heart_grayscale);
                }
                if (ivDone != null) {
                    ivDone.setVisibility(bar.isDone() ? View.VISIBLE : View.INVISIBLE);
                }
                // add an actionlistener to the whole card
                cardView.setOnClickListener(v -> gotoReview(bar));
                linearLayoutBars.addView(cardView);
            }
        }

        // handle navigation on back pressed or navigation icon pressed (toolbar)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> goBack());

        // register callback to go back to previous activity
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                goBack();
            }
        });


    }// END ON CREATE


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_MAP, bars);
    }

    private HashMap<Integer, Bar> bars;
    public final String STATE_MAP = "bar_map";

    public void gotoReview(Bar bar){
        // TODO intent to BarInfoActivity
        Utils.sayShort(this, "Review not implemented");
    }
    public void goBack(){
        Intent infoCarrier = new Intent();
        infoCarrier.putExtra("barNumber", barNumber);
        setResult(RESULT_OK, infoCarrier);
        finish();

    }
}
