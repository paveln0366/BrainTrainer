package com.pavelpotapov.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        textViewResult = findViewById(R.id.textViewResult);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("result")) {
            int result = intent.getIntExtra("result", 0);
            String score = String.format("Your result: %s", result);
            textViewResult.setText(score);
        }
    }

    public void onClickStartNewGame(View view) {

    }
}