package com.simorgh.englishtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class TestOrPracticeDialog {
    public static void createDialog(@NonNull final Context context, @NonNull final String title, final int questionCount, final int time
    , final View.OnClickListener onTestClickListener,final View.OnClickListener onPracticeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.test_or_practice_dialog, null);
        builder.setView(view);

        String count = String.format("تعداد سوالات: %s", String.valueOf(questionCount));
        String t = String.format("زمان پاسخگویی: %s دقیقه", String.valueOf(time));
        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((TextView) view.findViewById(R.id.tv_question_count)).setText(count);
        ((TextView) view.findViewById(R.id.tv_time)).setText(t);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


        view.findViewById(R.id.view_test).setOnClickListener(v -> {
            alertDialog.dismiss();
            onTestClickListener.onClick(v);
        });

        view.findViewById(R.id.view_practice).setOnClickListener(v -> {
            alertDialog.dismiss();
            onPracticeClickListener.onClick(v);
        });
    }
}
