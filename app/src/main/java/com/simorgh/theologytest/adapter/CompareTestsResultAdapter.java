package com.simorgh.theologytest.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simorgh.theologytest.R;
import com.simorgh.theologytest.model.Answer2;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class CompareTestsResultAdapter extends ListAdapter<Answer2, CompareTestsResultAdapter.AnswerHolder> {
    public CompareTestsResultAdapter(@NonNull DiffUtil.ItemCallback<Answer2> diffCallback) {
        super(diffCallback);
    }

    protected CompareTestsResultAdapter(@NonNull AsyncDifferConfig<Answer2> config) {
        super(config);
    }

    @NonNull
    @Override
    public AnswerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_compare_result, parent, false);
        ViewCompat.setLayoutDirection(v, ViewCompat.LAYOUT_DIRECTION_LTR);
        return new AnswerHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerHolder holder, final int position) {
        Answer2 answer = getItem(position);
        if (answer != null) {
            TextView correctAnswer = holder.itemView.findViewById(R.id.tv_correct_answer);
            TextView currentAnswer = holder.itemView.findViewById(R.id.tv_current_answer);
            TextView prevAnswer = holder.itemView.findViewById(R.id.tv_prev_answer);
            TextView questionNumber = holder.itemView.findViewById(R.id.tv_question_num);

            correctAnswer.setText(String.valueOf(answer.getTrueAnswer()));
            currentAnswer.setText(String.valueOf(answer.getCurrentAnswer()));
            prevAnswer.setText(String.valueOf(answer.getPrevAnswer()));
            questionNumber.setText(String.valueOf(answer.getQuestionNumber()));

            if (answer.getCurrentAnswer() != 0) {
                if (answer.getCurrentAnswer() == answer.getTrueAnswer()) {
                    currentAnswer.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.answer_number_bkg_corect));
                } else {
                    currentAnswer.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.answer_number_bkg_wrong));
                }
            } else{
                currentAnswer.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.answer_number_bkg_black));
                currentAnswer.setText("");
            }

            if (answer.getPrevAnswer() != 0) {
                if (answer.getPrevAnswer() == answer.getTrueAnswer() && answer.getPrevAnswer() != 0) {
                    prevAnswer.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.answer_number_bkg_corect));
                } else {
                    prevAnswer.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.answer_number_bkg_wrong));
                }
            } else {
                prevAnswer.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.answer_number_bkg_black));
                prevAnswer.setText("");
            }
        }
    }

    public class AnswerHolder extends RecyclerView.ViewHolder {
        AnswerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class ItemDiffCallBack extends DiffUtil.ItemCallback<Answer2> {

        @Override
        public boolean areItemsTheSame(@NonNull Answer2 Answer, @NonNull Answer2 newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Answer2 oldItem, @NonNull Answer2 newItem) {
            return false;
        }
    }
}
