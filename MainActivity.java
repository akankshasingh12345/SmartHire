package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Adapters.QuizAdapter;
import com.example.quizapp.Model.Question;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private QuizAdapter adapter;
    private ArrayList<Question> questions;
    private CountDownTimer timer;
    private long timeLeftInMillis;
    private static final long TOTAL_TIME = 600000; // 10 minutes in milliseconds
    private TextView timerTextView;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "quiz_pref";
    private static final String KEY_QUESTION_INDEX = "question_index";
    private static final String KEY_TIME_LEFT = "time_left";
    private int currentQuestionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        timerTextView = findViewById(R.id.timerTextView);
        questions = loadQuestions();
        adapter = new QuizAdapter(questions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        currentQuestionIndex = sharedPreferences.getInt(KEY_QUESTION_INDEX, 0);
        timeLeftInMillis = sharedPreferences.getLong(KEY_TIME_LEFT, TOTAL_TIME);

        startTimer();
        recyclerView.scrollToPosition(currentQuestionIndex);
    }

    private ArrayList<Question> loadQuestions() {
        ArrayList<Question> questions = new ArrayList<>();
        try {
            InputStream is = getResources().openRawResource(R.raw.questions);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String question = obj.getString("question");
                JSONArray optionsArray = obj.getJSONArray("options");
                ArrayList<String> options = new ArrayList<>();
                for (int j = 0; j < optionsArray.length(); j++) {
                    options.add(optionsArray.getString(j));
                }
                String answer = obj.getString("answer");
                questions.add(new Question(question, options, answer));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return questions;
    }

    private void startTimer() {
        timer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                // Handle quiz finish
            }
        }.start();
    }
    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeFormatted);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_QUESTION_INDEX, currentQuestionIndex);
        editor.putLong(KEY_TIME_LEFT, timeLeftInMillis);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentQuestionIndex = sharedPreferences.getInt(KEY_QUESTION_INDEX, 0);
        timeLeftInMillis = sharedPreferences.getLong(KEY_TIME_LEFT, TOTAL_TIME);
        startTimer();
        recyclerView.scrollToPosition(currentQuestionIndex);
    }
    public void updateCurrentQuestionIndex(int index) {
        currentQuestionIndex =index;
}
}
