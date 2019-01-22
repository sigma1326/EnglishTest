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
import java.util.Objects;
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
    private OnAnswerListener onAnswerListener;
    private boolean isTestType = false;
    private boolean showAnswer = false;
    private boolean state = false;


    public List<Answer> getAnswers() {
        return answers;
    }

    public QuestionAdapter(@NonNull DiffUtil.ItemCallback<Question> diffCallback) {
        super(diffCallback);
    }

    public QuestionAdapter(@NonNull DiffUtil.ItemCallback<Question> diffCallback, OnReadingShownListener onReadingShownListener, OnAnswerListener onAnswerListener, boolean isTestType) {
        super(diffCallback);
        this.onReadingShownListener = onReadingShownListener;
        this.onAnswerListener = onAnswerListener;
        this.isTestType = isTestType;
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
        showAnswer = false;
        state = false;
        ViewCompat.setLayoutDirection(v, ViewCompat.LAYOUT_DIRECTION_LTR);
        return new ViewHolder(v);
    }

    public Date getDate() {
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
            holder.question = questionItem;
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

            setTypeFace(tv_question, tv_questionNum, tv_answer1, tv_answer2, tv_answer3
                    , tv_answer4, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num);


            clearSelectedAnswers(tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num);


            restoreSelectedAnswer(holder, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num);

            if (!isTestType && state) {
                toggleShowTrueAnswer(holder, questionItem, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num);
            }

            initClickListeners(holder, position, tv_answer1, tv_answer2, tv_answer3, tv_answer4
                    , tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num);
        }
    }

    private void restoreSelectedAnswer(@NonNull ViewHolder holder, TextView tv_answer1Num, TextView tv_answer2Num, TextView tv_answer3Num, TextView tv_answer4Num) {
        if (holder.question != null) {
            if (isAnswerForQuestionExists(holder.question)) {
                holder.answer = Objects.requireNonNull(getAnswer(holder.question)).getAnswer();
                switch (holder.answer) {
                    case 1:
                        selectAnswer(tv_answer1Num);
                        break;
                    case 2:
                        selectAnswer(tv_answer2Num);
                        break;
                    case 3:
                        selectAnswer(tv_answer3Num);
                        break;
                    case 4:
                        selectAnswer(tv_answer4Num);
                        break;
                }
            }
        }
    }

    private void setTypeFace(TextView tv_question, TextView tv_questionNum, TextView tv_answer1, TextView tv_answer2, TextView tv_answer3, TextView tv_answer4, TextView tv_answer1Num, TextView tv_answer2Num, TextView tv_answer3Num, TextView tv_answer4Num) {
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
    }

    private void initClickListeners(@NonNull ViewHolder holder, int position, TextView tv_answer1, TextView tv_answer2, TextView tv_answer3, TextView tv_answer4, TextView tv_answer1Num, TextView tv_answer2Num, TextView tv_answer3Num, TextView tv_answer4Num) {
        holder.itemView.findViewById(R.id.layout_answer1).setOnClickListener(v -> {
            clickAnswer1(holder, position, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num, v);
        });

        tv_answer1.setOnClickListener(v -> {
            clickAnswer1(holder, position, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num, v);
        });

        tv_answer1Num.setOnClickListener(v -> {
            clickAnswer1(holder, position, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num, v);
        });


        holder.itemView.findViewById(R.id.layout_answer2).setOnClickListener(v -> {
            clickAnswer2(holder, position, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num, v);
        });

        tv_answer2.setOnClickListener(v -> {
            clickAnswer2(holder, position, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num, v);
        });

        tv_answer2Num.setOnClickListener(v -> {
            clickAnswer2(holder, position, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num, v);
        });


        holder.itemView.findViewById(R.id.layout_answer3).setOnClickListener(v -> {
            clickAnswer3(holder, position, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num, v);
        });

        tv_answer3.setOnClickListener(v -> {
            clickAnswer3(holder, position, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num, v);
        });

        tv_answer3Num.setOnClickListener(v -> {
            clickAnswer3(holder, position, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num, v);
        });

        holder.itemView.findViewById(R.id.layout_answer4).setOnClickListener(v -> {
            clickAnswer4(holder, position, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num, v);
        });

        tv_answer4.setOnClickListener(v -> {
            clickAnswer4(holder, position, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num, v);
        });

        tv_answer4Num.setOnClickListener(v -> {
            clickAnswer4(holder, position, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num, v);
        });
    }

    private void clickAnswer4(@NonNull ViewHolder holder, int position, TextView tv_answer1Num, TextView tv_answer2Num, TextView tv_answer3Num, TextView tv_answer4Num, View v) {
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
    }

    private void clickAnswer3(@NonNull ViewHolder holder, int position, TextView tv_answer1Num, TextView tv_answer2Num, TextView tv_answer3Num, TextView tv_answer4Num, View v) {
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
    }

    private void clickAnswer2(@NonNull ViewHolder holder, int position, TextView tv_answer1Num, TextView tv_answer2Num, TextView tv_answer3Num, TextView tv_answer4Num, View v) {
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
    }

    private void clickAnswer1(@NonNull ViewHolder holder, int position, TextView tv_answer1Num, TextView tv_answer2Num, TextView tv_answer3Num, TextView tv_answer4Num, View v) {
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
    }

    private void toggleShowTrueAnswer(@NonNull ViewHolder holder, Question questionItem, TextView tv_answer1Num, TextView tv_answer2Num, TextView tv_answer3Num, TextView tv_answer4Num) {
        if (showAnswer) {
            if (holder.answer == questionItem.getTrueAnswer()) {
                switch (holder.answer) {
                    case 1:
                        trueAnswer(tv_answer1Num);
                        break;
                    case 2:
                        trueAnswer(tv_answer2Num);
                        break;
                    case 3:
                        trueAnswer(tv_answer3Num);
                        break;
                    case 4:
                        trueAnswer(tv_answer4Num);
                        break;
                }
            } else {
                switch (holder.answer) {
                    case 1:
                        falseAnswer(tv_answer1Num);
                        break;
                    case 2:
                        falseAnswer(tv_answer2Num);
                        break;
                    case 3:
                        falseAnswer(tv_answer3Num);
                        break;
                    case 4:
                        falseAnswer(tv_answer4Num);
                        break;
                }

                switch (questionItem.getTrueAnswer()) {
                    case 1:
                        trueAnswer(tv_answer1Num);
                        break;
                    case 2:
                        trueAnswer(tv_answer2Num);
                        break;
                    case 3:
                        trueAnswer(tv_answer3Num);
                        break;
                    case 4:
                        trueAnswer(tv_answer4Num);
                        break;
                }

            }
        } else {
            clearSelectedAnswers(tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num);
            if (holder.answer != 0) {
                switch (holder.answer) {
                    case 1:
                        selectAnswer(tv_answer1Num);
                        break;
                    case 2:
                        selectAnswer(tv_answer2Num);
                        break;
                    case 3:
                        selectAnswer(tv_answer3Num);
                        break;
                    case 4:
                        selectAnswer(tv_answer4Num);
                        break;
                }
            }
        }
        state = false;
    }

    private void removeAnswer(@NonNull ViewHolder holder, int position) {
        Question q = getItem(position);
        Answer answer = new Answer(q.getId(), holder.answer, q.getTrueAnswer(), q.getQuestionNumber(), getDate());
        for (Answer ans : answers) {
            if (answer.getQuestionId() == ans.getQuestionId()) {
                answers.remove(ans);
                break;
            }
        }
        showAnswer = false;
    }

    private void addAnswer(@NonNull ViewHolder holder, int position) {
        Question q = getItem(position);
        Answer answer = new Answer(q.getId(), holder.answer, q.getTrueAnswer(), q.getQuestionNumber(), getDate());
        boolean exists;
        exists = isAnswerExists(answer);
        if (!exists) {
            answers.add(answer);
        }
        if (onAnswerListener != null && isTestType) {
            onAnswerListener.onQuestionAnswered(q, position);
        }
        showAnswer = false;
    }

    private boolean isAnswerExists(Answer answer) {
        boolean exists = false;
        for (Answer ans : answers) {
            if (answer.getQuestionId() == ans.getQuestionId()) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    private boolean isAnswerForQuestionExists(final Question question) {
        boolean exists = false;
        for (Answer ans : answers) {
            if (question.getId() == ans.getQuestionId()) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    private Answer getAnswer(final Question question) {
        for (Answer ans : answers) {
            if (question.getId() == ans.getQuestionId()) {
                return ans;
            }
        }
        return null;
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

    private void selectAnswer(final TextView textView) {
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setBackgroundDrawable(ContextCompat.getDrawable(textView.getContext(), R.drawable.answer_number_bkg_selected));
    }

    private void trueAnswer(final TextView textView) {
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setBackgroundDrawable(ContextCompat.getDrawable(textView.getContext(), R.drawable.answer_number_bkg_corect));
    }

    private void falseAnswer(final TextView textView) {
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setBackgroundDrawable(ContextCompat.getDrawable(textView.getContext(), R.drawable.answer_number_bkg_wrong));
    }

    public void toggleShowAnswer(final int lastPosition) {
        if (lastPosition != -1 && lastPosition < getItemCount()) {
            showAnswer = !showAnswer;
            state = true;
            notifyDataSetChanged();
        }
    }


    public interface OnReadingShownListener {
        void onReadingShown(final Question question);
    }

    public interface OnAnswerListener {
        void onQuestionAnswered(final Question question, final int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Question question;
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
