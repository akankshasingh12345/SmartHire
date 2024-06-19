package com.example.quizapp.Model;

import java.util.ArrayList;


public class Question {
    private String question;
    private ArrayList<String> options;
    private String answer;

    public Question(String question, ArrayList<String> options, String answer){
        this.question = question;
        this.options = options;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public String getAnswer() {
        return answer;

    }
}
