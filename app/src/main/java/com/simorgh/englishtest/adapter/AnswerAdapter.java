package com.simorgh.englishtest.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simorgh.database.model.Answer;
import com.simorgh.englishtest.R;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class AnswerAdapter extends ListAdapter<Answer, AnswerAdapter.AnswerHolder> {
    public AnswerAdapter(@NonNull DiffUtil.ItemCallback<Answer> diffCallback) {
        super(diffCallback);
    }

    protected AnswerAdapter(@NonNull AsyncDifferConfig<Answer> config) {
        super(config);
    }

    @NonNull
    @Override
    public AnswerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item, parent, false);
        ViewCompat.setLayoutDirection(v, ViewCompat.LAYOUT_DIRECTION_LTR);
        return new AnswerHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerHolder holder, final int position) {
        Answer answer = getItem(position);
        if (answer != null) {
            TextView correctAnswer = holder.itemView.findViewById(R.id.tv_correct_answer);
            TextView yourAnswer = holder.itemView.findViewById(R.id.tv_your_answer);
            TextView questionNumber = holder.itemView.findViewById(R.id.tv_question_num);

            correctAnswer.setText(String.valueOf(answer.getTrueAnswer()));
            yourAnswer.setText(String.valueOf(answer.getAnswer()));
            questionNumber.setText(String.valueOf(answer.getQuestionNumber()));

            if (answer.isCorrect()) {
                yourAnswer.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.answer_number_bkg_corect));
            } else {
                yourAnswer.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.answer_number_bkg_wrong));

            }
        }
    }

    public class AnswerHolder extends RecyclerView.ViewHolder {
        AnswerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class ItemDiffCallBack extends DiffUtil.ItemCallback<Answer> {

        @Override
        public boolean areItemsTheSame(@NonNull Answer Answer, @NonNull Answer newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Answer oldItem, @NonNull Answer newItem) {
            return false;
        }
    }
}
