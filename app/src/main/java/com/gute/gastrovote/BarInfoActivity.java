package com.gute.gastrovote;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BarInfoActivity extends AppCompatActivity {

    @SuppressLint("DefaultLocale")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.bar_info_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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

        // Capture Intent's bar and apply its values to UI displays
        Bar bar = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bar = getIntent().getSerializableExtra("bar", Bar.class);
        } else {
            //noinspection deprecation
            bar = (Bar) getIntent().getSerializableExtra("bar");
        }

        if (bar != null) {
            TextView tvTitle = findViewById(R.id.tvTitle), tvSubtitle = findViewById(R.id.tvSubtitle), tvPrice = findViewById(R.id.tvPrice), tvBarAddress = findViewById(R.id.tvBarAddress), tvBarPhone = findViewById(R.id.tvBarPhone);
            ImageView ivFavorite = findViewById(R.id.ivFavorite), ivDone = findViewById(R.id.ivDone);
            RatingBar cardRatingBar = findViewById(R.id.cardRatingBar);
            if (tvTitle != null) {
                tvTitle.setText(bar.getTapaName());
            }
            if (tvSubtitle != null) {
                tvSubtitle.setText(bar.getTapaDesc());
            }
            if (tvPrice != null) {
                tvPrice.setText(String.format("%.2f", bar.getPrecio()));
            }
            if (ivFavorite != null) {
                ivFavorite.setImageResource(bar.isFav() ? R.drawable.ic_heart : R.drawable.ic_heart_grayscale);
            }
            if (ivDone != null) {
                ivDone.setVisibility(bar.isDone() ? View.VISIBLE : View.INVISIBLE);
            }
            if (cardRatingBar != null) {
                cardRatingBar.setRating(bar.getRating());
            }
            if (tvBarAddress != null) {
                tvBarAddress.setText(bar.getBarName());
            }
            if (tvBarPhone != null) {
                tvBarPhone.setText(bar.getTelf());
            }
        }
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult resultado) {

                        if (resultado.getResultCode() == RESULT_OK) {
                            assert resultado.getData() != null;
                            if (resultado.getData().getStringExtra("review") != null) {
                                TextView tv = findViewById(R.id.tvResumen);
                                tv.setText(resultado.getData().getStringExtra("review"));
                            }
                        }
                    }
                });


        // Review Button ActionListener
        findViewById(R.id.btReview).setOnClickListener(v -> {
            Intent intent = new Intent(this, ReviewActivity.class);
            launcher.launch(intent);
        });


    } // END ON CREATE

    private ActivityResultLauncher<Intent> launcher;

    // go back (from utils)
    public void goBack(){
        finish();
    }
}
