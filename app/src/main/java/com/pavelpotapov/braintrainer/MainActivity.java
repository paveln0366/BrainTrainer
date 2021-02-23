package com.pavelpotapov.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textViewQuestion;
    private TextView textViewTimer;
    private TextView textViewScore;
    private TextView textViewOpinion0;
    private TextView textViewOpinion1;
    private TextView textViewOpinion2;
    private TextView textViewOpinion3;

    private ArrayList<TextView> opinions = new ArrayList<>();

    private String question;
    private int rightAnswer;
    private int rightAnswerPosition;
    private boolean isPositive;
    private int min = 5;
    private int max = 30;
    private int countOfQuestions;
    private int countOfRightAnswers;
    private boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewScore = findViewById(R.id.textViewScore);
        textViewOpinion0 = findViewById(R.id.textViewOpinion0);
        textViewOpinion1 = findViewById(R.id.textViewOpinion1);
        textViewOpinion2 = findViewById(R.id.textViewOpinion2);
        textViewOpinion3 = findViewById(R.id.textViewOpinion3);

        opinions.add(textViewOpinion0);
        opinions.add(textViewOpinion1);
        opinions.add(textViewOpinion2);
        opinions.add(textViewOpinion3);

        playNext();

        CountDownTimer timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTimer.setText(getTime(millisUntilFinished));

                if (millisUntilFinished < 20000) {
                    textViewTimer.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
                }

                if (millisUntilFinished < 10000) {
                    textViewTimer.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }

            @Override
            public void onFinish() {
                gameOver = true;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int max = preferences.getInt("max", 0);
                if (countOfRightAnswers >= max) {
                    preferences.edit().putInt("max", countOfRightAnswers).apply();
                }
                Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                intent.putExtra("result", countOfRightAnswers);
                startActivity(intent);
            }
        };
        timer.start();
    }

    private void playNext() {
        generateQuestion();
        for (int i = 0; i < opinions.size(); i++) {
            if (i == rightAnswerPosition) {
                opinions.get(i).setText(Integer.toString(rightAnswer));
            } else {
                opinions.get(i).setText(Integer.toString(generateWrongAnswer()));
            }
        }
        String score = String.format("%s / %s", countOfRightAnswers, countOfQuestions);
        textViewScore.setText(score);
    }

    private void generateQuestion() {
        int a = (int) (Math.random() * (max - min + 1) + min); // 5..30
        int b = (int) (Math.random() * (max - min + 1) + min);
        int mark = (int) (Math.random() * 2); // 0 or 1
        isPositive = mark == 1;
        if (isPositive) {
            rightAnswer = a + b;
            question = String.format("%s + %s", a, b);
        } else {
            rightAnswer = a - b;
            question = String.format("%s - %s", a, b);
        }
        textViewQuestion.setText(question);
        rightAnswerPosition = (int) (Math.random() * 4); // 0..3
    }

    private int generateWrongAnswer() {
        int result;

        do {
             result = (int) (Math.random() * max * 2 + 1) - (max - min); // -25..35
        } while (result == rightAnswer);
        return result;
    }

    private String getTime(long millis) {
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    public void onClickOpinion(View view) {
        if (!gameOver) {
            TextView textView = (TextView) view;
            String answer = textView.getText().toString();
            int chosenAnswer = Integer.parseInt(answer);
            if (chosenAnswer == rightAnswer) {
                countOfRightAnswers++;
                Toast.makeText(this, getString(R.string.answer_right), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.answer_wrong), Toast.LENGTH_SHORT).show();
            }
            countOfQuestions++;
            playNext();
        }
    }
}