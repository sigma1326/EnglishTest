package com.simorgh.englishtest.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simorgh.database.Date;
import com.simorgh.database.model.Answer;
import com.simorgh.database.model.Question;
import com.simorgh.englishtest.R;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import io.github.inflationx.calligraphy3.CalligraphyUtils;

public class QuestionAdapter extends ListAdapter<Question, QuestionAdapter.ViewHolder> {
    private Typeface typeface;
    private List<Answer> answers = new LinkedList<>();
    private Date date = null;
    private OnReadingShownListener onReadingShownListener;

    public List<Answer> getAnswers() {
        return answers;
    }

    public QuestionAdapter(@NonNull DiffUtil.ItemCallback<Question> diffCallback) {
        super(diffCallback);
    }

    public QuestionAdapter(@NonNull DiffUtil.ItemCallback<Question> diffCallback, OnReadingShownListener onReadingShownListener) {
        super(diffCallback);
        this.onReadingShownListener = onReadingShownListener;
    }

    protected QuestionAdapter(@NonNull AsyncDifferConfig<Question> config) {
        super(config);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(parent.getContext().getAssets(), "fonts/tnr.ttf");
        }
        ViewCompat.setLayoutDirection(v, ViewCompat.LAYOUT_DIRECTION_LTR);
        return new ViewHolder(v);
    }

    private Date getDate() {
        if (date == null) {
            date = new Date(Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()));
        }
        return date;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question questionItem = getItem(position);
        if (questionItem != null) {
            if (onReadingShownListener != null) {
                onReadingShownListener.onReadingShown(questionItem);
            }

            TextView tv_question = holder.itemView.findViewById(R.id.tv_question);
            TextView tv_questionNum = holder.itemView.findViewById(R.id.tv_question_num);
            TextView tv_answer1 = holder.itemView.findViewById(R.id.tv_answer1);
            TextView tv_answer2 = holder.itemView.findViewById(R.id.tv_answer2);
            TextView tv_answer3 = holder.itemView.findViewById(R.id.tv_answer3);
            TextView tv_answer4 = holder.itemView.findViewById(R.id.tv_answer4);


            TextView tv_answer1Num = holder.itemView.findViewById(R.id.tv_answer1_num);
            TextView tv_answer2Num = holder.itemView.findViewById(R.id.tv_answer2_num);
            TextView tv_answer3Num = holder.itemView.findViewById(R.id.tv_answer3_num);
            TextView tv_answer4Num = holder.itemView.findViewById(R.id.tv_answer4_num);


            tv_question.setText(questionItem.getQuestion());
            tv_questionNum.setText(String.format("%d - ", questionItem.getQuestionNumber()));

            tv_answer1.setText(questionItem.getAnswer1());
            tv_answer2.setText(questionItem.getAnswer2());
            tv_answer3.setText(questionItem.getAnswer3());
            tv_answer4.setText(questionItem.getAnswer4());

            CalligraphyUtils.applyFontToTextView(tv_question, typeface);
            CalligraphyUtils.applyFontToTextView(tv_questionNum, typeface);
            CalligraphyUtils.applyFontToTextView(tv_answer1, typeface);
            CalligraphyUtils.applyFontToTextView(tv_answer2, typeface);
            CalligraphyUtils.applyFontToTextView(tv_answer3, typeface);
            CalligraphyUtils.applyFontToTextView(tv_answer4, typeface);


            CalligraphyUtils.applyFontToTextView(tv_answer1Num, typeface);
            CalligraphyUtils.applyFontToTextView(tv_answer2Num, typeface);
            CalligraphyUtils.applyFontToTextView(tv_answer3Num, typeface);
            CalligraphyUtils.applyFontToTextView(tv_answer4Num, typeface);


            clearSelectedAnswers(tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num);


            holder.itemView.findViewById(R.id.layout_answer1).setOnClickListener(v -> {
                if (holder.answer == 1) {
                    holder.answer = 0;
                    tv_answer1Num.setTextColor(Color.parseColor("#d96071"));
                    tv_answer1Num.setBackgroundDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_number_bkg));

                    removeAnswer(holder, position);
                } else {
                    clearSelectedAnswer(holder, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num);

                    holder.answer = 1;
                    tv_answer1Num.setBackgroundDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_number_bkg_selected));
                    tv_answer1Num.setTextColor(Color.parseColor("#000000"));

                    addAnswer(holder, position);

                }

            });

            holder.itemView.findViewById(R.id.layout_answer2).setOnClickListener(v -> {
                if (holder.answer == 2) {
                    holder.answer = 0;
                    tv_answer2Num.setTextColor(Color.parseColor("#d96071"));
                    tv_answer2Num.setBackgroundDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_number_bkg));

                    removeAnswer(holder, position);

                } else {
                    clearSelectedAnswer(holder, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num);

                    holder.answer = 2;
                    tv_answer2Num.setBackgroundDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_number_bkg_selected));
                    tv_answer2Num.setTextColor(Color.parseColor("#000000"));

                    addAnswer(holder, position);
                }
            });

            holder.itemView.findViewById(R.id.layout_answer3).setOnClickListener(v -> {
                if (holder.answer == 3) {
                    holder.answer = 0;
                    tv_answer3Num.setTextColor(Color.parseColor("#d96071"));
                    tv_answer3Num.setBackgroundDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_number_bkg));

                    removeAnswer(holder, position);
                } else {
                    clearSelectedAnswer(holder, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num);

                    holder.answer = 3;
                    tv_answer3Num.setBackgroundDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_number_bkg_selected));
                    tv_answer3Num.setTextColor(Color.parseColor("#000000"));

                    addAnswer(holder, position);
                }
            });

            holder.itemView.findViewById(R.id.layout_answer4).setOnClickListener(v -> {
                if (holder.answer == 4) {
                    holder.answer = 0;
                    tv_answer4Num.setTextColor(Color.parseColor("#d96071"));
                    tv_answer4Num.setBackgroundDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_number_bkg));

                    removeAnswer(holder, position);

                } else {
                    clearSelectedAnswer(holder, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num);

                    holder.answer = 4;
                    tv_answer4Num.setBackgroundDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_number_bkg_selected));
                    tv_answer4Num.setTextColor(Color.parseColor("#000000"));

                    addAnswer(holder, position);
                }
            });
        }
    }

    private void removeAnswer(@NonNull ViewHolder holder, int position) {
        Question q = getItem(position);
        Answer answer = new Answer(q.getId(), holder.answer, getDate());
        for (Answer ans : answers) {
            if (answer.getQuestionId() == ans.getQuestionId()) {
                answers.remove(ans);
                break;
            }
        }
    }

    private void addAnswer(@NonNull ViewHolder holder, int position) {
        Question q = getItem(position);
        Answer answer = new Answer(q.getId(), holder.answer, getDate());
        boolean exists = false;
        for (Answer ans : answers) {
            if (answer.getQuestionId() == ans.getQuestionId()) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            answers.add(answer);
        }
    }

    private void clearSelectedAnswer(@NonNull ViewHolder holder, TextView tv_answer1Num, TextView tv_answer2Num, TextView tv_answer3Num, TextView tv_answer4Num) {
        switch (holder.answer) {
            case 1:
                clearSelection(tv_answer1Num);
                break;
            case 2:
                clearSelection(tv_answer2Num);
                break;
            case 3:
                clearSelection(tv_answer3Num);
                break;
            case 4:
                clearSelection(tv_answer4Num);
                break;
        }
    }

    private void clearSelectedAnswers(TextView tv_answer1Num, TextView tv_answer2Num, TextView tv_answer3Num, TextView tv_answer4Num) {
        clearSelection(tv_answer1Num);
        clearSelection(tv_answer2Num);
        clearSelection(tv_answer3Num);
        clearSelection(tv_answer4Num);
    }

    private void clearSelection(final TextView textView) {
        textView.setTextColor(Color.parseColor("#d96071"));
        textView.setBackgroundDrawable(ContextCompat.getDrawable(textView.getContext(), R.drawable.answer_number_bkg));
    }


    public interface OnReadingShownListener {
        void onReadingShown(final Question question);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private int answer = 0;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class ItemDiffCallBack extends DiffUtil.ItemCallback<Question> {

        @Override
        public boolean areItemsTheSame(@NonNull Question oldItem, @NonNull Question newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Question oldItem, @NonNull Question newItem) {
            return false;
        }
    }
}
