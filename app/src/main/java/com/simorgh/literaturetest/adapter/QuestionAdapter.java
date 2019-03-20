package com.simorgh.literaturetest.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simorgh.database.Date;
import com.simorgh.database.model.Answer;
import com.simorgh.database.model.Question;
import com.simorgh.literaturetest.R;

import java.util.ArrayList;
import java.util.Calendar;
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
    private final List<Answer> answers = new ArrayList<>();
    private Date date = null;
    private OnAnswerListener onAnswerListener;
    private boolean isTestType = false;
    private boolean showAnswer = false;
    private boolean state = false;
    private AnswerControllerListener answerControllerListener;
    private int fontSize = 14;


    public interface AnswerControllerListener {
        boolean canAnswer();
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public QuestionAdapter(@NonNull DiffUtil.ItemCallback<Question> diffCallback) {
        super(diffCallback);
    }

    public QuestionAdapter(@NonNull DiffUtil.ItemCallback<Question> diffCallback
            , OnAnswerListener onAnswerListener, boolean isTestType
            , @NonNull AnswerControllerListener answerControllerListener, final int fontSize) {
        super(diffCallback);
        this.onAnswerListener = onAnswerListener;
        this.isTestType = isTestType;
        this.answerControllerListener = answerControllerListener;
        this.fontSize = fontSize;
    }

    protected QuestionAdapter(@NonNull AsyncDifferConfig<Question> config) {
        super(config);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
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
            holder.position = position;

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
            tv_questionNum.setText(String.format(" -%d", questionItem.getQuestionNumber()));

            tv_answer1.setText(questionItem.getAnswer1());
            tv_answer2.setText(questionItem.getAnswer2());
            tv_answer3.setText(questionItem.getAnswer3());
            tv_answer4.setText(questionItem.getAnswer4());

            tv_question.setTextSize(fontSize);
            tv_questionNum.setTextSize(fontSize);
            tv_answer1.setTextSize(fontSize);
            tv_answer2.setTextSize(fontSize);
            tv_answer3.setTextSize(fontSize);
            tv_answer4.setTextSize(fontSize);

            setTypeFace(tv_question, tv_questionNum, tv_answer1, tv_answer2, tv_answer3
                    , tv_answer4, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num);


            clearSelectedAnswers(tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num);


            restoreSelectedAnswer(holder, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num);

            if (!isTestType && state) {
                toggleShowTrueAnswer(holder, questionItem, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num);
            }

            initClickListeners(holder, tv_answer1, tv_answer2, tv_answer3, tv_answer4
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
//        CalligraphyUtils.applyFontToTextView(tv_question, typeface);
        CalligraphyUtils.applyFontToTextView(tv_questionNum, typeface);
//        CalligraphyUtils.applyFontToTextView(tv_answer1, typeface);
//        CalligraphyUtils.applyFontToTextView(tv_answer2, typeface);
//        CalligraphyUtils.applyFontToTextView(tv_answer3, typeface);
//        CalligraphyUtils.applyFontToTextView(tv_answer4, typeface);


        CalligraphyUtils.applyFontToTextView(tv_answer1Num, typeface);
        CalligraphyUtils.applyFontToTextView(tv_answer2Num, typeface);
        CalligraphyUtils.applyFontToTextView(tv_answer3Num, typeface);
        CalligraphyUtils.applyFontToTextView(tv_answer4Num, typeface);
    }

    private void initClickListeners(@NonNull ViewHolder holder, TextView tv_answer1, TextView tv_answer2, TextView tv_answer3, TextView tv_answer4, TextView tv_answer1Num, TextView tv_answer2Num, TextView tv_answer3Num, TextView tv_answer4Num) {
        ((RelativeLayout) holder.itemView.findViewById(R.id.layout_answer1)).requestDisallowInterceptTouchEvent(false);
        ((RelativeLayout) holder.itemView.findViewById(R.id.layout_answer2)).requestDisallowInterceptTouchEvent(false);
        ((RelativeLayout) holder.itemView.findViewById(R.id.layout_answer3)).requestDisallowInterceptTouchEvent(false);
        ((RelativeLayout) holder.itemView.findViewById(R.id.layout_answer4)).requestDisallowInterceptTouchEvent(false);


        holder.itemView.findViewById(R.id.layout_answer1).setOnClickListener(v -> {
            tv_answer1Num.performClick();
        });

        tv_answer1.setOnClickListener(v -> {
            tv_answer1Num.performClick();
        });

        tv_answer1Num.setOnClickListener(v -> {
            clickAnswer1(holder, holder.position, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num, v);
        });


        holder.itemView.findViewById(R.id.layout_answer2).setOnClickListener(v -> {
            tv_answer2Num.performClick();
        });

        tv_answer2.setOnClickListener(v -> {
            tv_answer2Num.performClick();
        });

        tv_answer2Num.setOnClickListener(v -> {
            clickAnswer2(holder, holder.position, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num, v);
        });


        holder.itemView.findViewById(R.id.layout_answer3).setOnClickListener(v -> {
            tv_answer3Num.performClick();
        });

        tv_answer3.setOnClickListener(v -> {
            tv_answer3Num.performClick();
        });

        tv_answer3Num.setOnClickListener(v -> {
            clickAnswer3(holder, holder.position, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num, v);
        });

        holder.itemView.findViewById(R.id.layout_answer4).setOnClickListener(v -> {
            tv_answer4Num.performClick();
        });

        tv_answer4.setOnClickListener(v -> {
            tv_answer4Num.performClick();
        });

        tv_answer4Num.setOnClickListener(v -> {
            clickAnswer4(holder, holder.position, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num, v);
        });
    }

    private void clickAnswer4(@NonNull ViewHolder holder, int position, TextView tv_answer1Num, TextView tv_answer2Num, TextView tv_answer3Num, TextView tv_answer4Num, View v) {
        if (answerControllerListener != null && answerControllerListener.canAnswer()) {
            if (holder.answer == 4) {
                clearSelection(tv_answer4Num);
                removeAnswer(holder, position);
            } else {
                clearSelectedAnswer(holder, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num);

                holder.answer = 4;
                selectAnswer(tv_answer4Num);
                addAnswer(holder, position);
            }
        }
    }

    private void clickAnswer3(@NonNull ViewHolder holder, int position, TextView tv_answer1Num, TextView tv_answer2Num, TextView tv_answer3Num, TextView tv_answer4Num, View v) {
        if (answerControllerListener != null && answerControllerListener.canAnswer()) {
            if (holder.answer == 3) {
                clearSelection(tv_answer3Num);
                removeAnswer(holder, position);
            } else {
                clearSelectedAnswer(holder, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num);

                holder.answer = 3;
                selectAnswer(tv_answer3Num);
                addAnswer(holder, position);
            }
        }
    }

    private void clickAnswer2(@NonNull ViewHolder holder, int position, TextView tv_answer1Num, TextView tv_answer2Num, TextView tv_answer3Num, TextView tv_answer4Num, View v) {
        if (answerControllerListener != null && answerControllerListener.canAnswer()) {
            if (holder.answer == 2) {
                clearSelection(tv_answer2Num);
                removeAnswer(holder, position);
            } else {
                clearSelectedAnswer(holder, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num);

                holder.answer = 2;
                selectAnswer(tv_answer2Num);
                addAnswer(holder, position);
            }
        }
    }

    private void clickAnswer1(@NonNull ViewHolder holder, int position, TextView tv_answer1Num, TextView tv_answer2Num, TextView tv_answer3Num, TextView tv_answer4Num, View v) {
        if (answerControllerListener != null && answerControllerListener.canAnswer()) {
            if (holder.answer == 1) {
                clearSelection(tv_answer1Num);
                removeAnswer(holder, position);
            } else {
                clearSelectedAnswer(holder, tv_answer1Num, tv_answer2Num, tv_answer3Num, tv_answer4Num);

                holder.answer = 1;
                selectAnswer(tv_answer1Num);
                addAnswer(holder, position);
            }
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
        holder.answer = 0;
        Question q = getItem(position);
        for (Answer ans : answers) {
            if (ans.getQuestionId() == q.getId()) {
                answers.remove(ans);
                break;
            }
        }
        state = false;
    }

    private void addAnswer(@NonNull ViewHolder holder, int position) {
        Question q = getItem(position);
        Answer answer = new Answer(q.getId(), holder.answer, q.getTrueAnswer(), q.getQuestionNumber(), getDate());
        checkAndAddAnswer(answer);
        if (onAnswerListener != null && isTestType) {
            onAnswerListener.onQuestionAnswered(q, position);
        }
        showAnswer = false;
    }

    private void checkAndAddAnswer(Answer answer) {
        boolean exists = false;
        for (Answer ans : answers) {
            if (answer.getQuestionId() == ans.getQuestionId()) {
                exists = true;
                ans.setAnswer(answer.getAnswer());
                break;
            }
        }
        if (!exists) {
            answers.add(answer);
        }
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


    public interface OnAnswerListener {
        void onQuestionAnswered(final Question question, final int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Question question;
        private int answer = 0;
        private int position = 0;

        ViewHolder(@NonNull View itemView) {
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
