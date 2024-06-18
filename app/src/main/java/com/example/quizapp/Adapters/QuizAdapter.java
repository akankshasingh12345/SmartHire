package com.example.quizapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.MainActivity;
import com.example.quizapp.Model.Question;
import com.example.quizapp.R;

import java.util.ArrayList;


public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {
    private ArrayList<Question> questions;
    private MainActivity mainActivity;

    public QuizAdapter(ArrayList<Question>
          questions, MainActivity mainActivity) {
        this.questions = questions;
        this.mainActivity = mainActivity;

    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_item, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.bind(question, position);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        RadioGroup optionsGroup;

        QuizViewHolder(View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            optionsGroup = itemView.findViewById(R.id.optionsGroup);
        }

        void bind(final Question question, final int position) {
            questionTextView.setText(question.getQuestion());
            optionsGroup.removeAllViews();
            for (String option : question.getOptions()) {
                RadioButton radioButton = new RadioButton(itemView.getContext());
                radioButton.setText(option);
                optionsGroup.addView(radioButton);
            }
            optionsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    mainActivity.updateCurrentQuestionIndex(position + 1);
                }
            });
        }
    }
}