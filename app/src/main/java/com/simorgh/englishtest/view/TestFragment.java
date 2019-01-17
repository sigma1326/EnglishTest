package com.simorgh.englishtest.view;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.simorgh.database.TestRepository;
import com.simorgh.database.model.Answer;
import com.simorgh.database.model.Question;
import com.simorgh.database.model.Reading;
import com.simorgh.englishtest.R;
import com.simorgh.englishtest.adapter.QuestionAdapter;
import com.simorgh.englishtest.viewModel.TestViewModel;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import io.github.inflationx.calligraphy3.CalligraphyUtils;

public class TestFragment extends Fragment implements QuestionAdapter.OnReadingShownListener, QuestionAdapter.OnAnswerListener {

    private TestViewModel mViewModel;
    private RecyclerView rvQuestions;
    private ImageButton fab;
    private ImageButton pauseTestButton;
    private ImageButton stopTestButton;
    private TextView tvReadingTitle;
    private HtmlTextView tvReadingContent;
    private Typeface typeface;
    private MotionLayout motionLayout;
    private TestRepository testRepository;


    public static TestFragment newInstance() {
        return new TestFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (testRepository == null) {
            testRepository = new TestRepository(Objects.requireNonNull(getActivity()).getApplication());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test_fragment, container, false);
    }

    int i = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setLayoutDirection(view, ViewCompat.LAYOUT_DIRECTION_LTR);

        if (typeface == null) {
            typeface = Typeface.createFromAsset(Objects.requireNonNull(getContext()).getAssets(), "fonts/tnr.ttf");
        }

        rvQuestions = view.findViewById(R.id.rv_questions);
        fab = view.findViewById(R.id.fab_test);
        pauseTestButton = view.findViewById(R.id.img_pause);
        stopTestButton = view.findViewById(R.id.img_stop);
        motionLayout = (MotionLayout) view;
        tvReadingTitle = view.findViewById(R.id.tv_reading_title);
        tvReadingContent = view.findViewById(R.id.tv_reading_content);

        CalligraphyUtils.applyFontToTextView(tvReadingTitle, typeface);
        CalligraphyUtils.applyFontToTextView(tvReadingContent, typeface);

        tvReadingContent.setHtml(getString(R.string.lorem));


        rvQuestions.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true);
        rvQuestions.setLayoutManager(linearLayoutManager);
        rvQuestions.setNestedScrollingEnabled(false);
        rvQuestions.setAdapter(new QuestionAdapter(new QuestionAdapter.ItemDiffCallBack(), this, this));
        rvQuestions.setHasFixedSize(true);

        //disable scrolling in recyclerView
        rvQuestions.setOnTouchListener((v, event) -> true);

        //add snap helper to rv
        new LinearSnapHelper().attachToRecyclerView(rvQuestions);


        fab.setOnClickListener(v -> {
            switch (motionLayout.getCurrentState()) {
                case R.id.show_reading_show_snackbar:
                    motionLayout.setTransition(motionLayout.getCurrentState(), R.id.show_reading_hide_snackbar);
                    motionLayout.transitionToEnd();
                    break;
                case R.id.show_reading_hide_snackbar:
                    motionLayout.setTransition(motionLayout.getCurrentState(), R.id.show_reading_show_snackbar);
                    motionLayout.transitionToEnd();
                    break;
                case R.id.hide_reading_show_snackbar:
                    motionLayout.setTransition(motionLayout.getCurrentState(), R.id.hide_reading_hide_snackbar);
                    motionLayout.transitionToEnd();
                    break;
                case R.id.hide_reading_hide_snackbar:
                    motionLayout.setTransition(motionLayout.getCurrentState(), R.id.hide_reading_show_snackbar);
                    motionLayout.transitionToEnd();
                    break;
            }
        });


        pauseTestButton.setOnClickListener(v -> {
            rvQuestions.smoothScrollToPosition(++i);


        });

        stopTestButton.setOnClickListener(v -> {
            List<Answer> answers = ((QuestionAdapter) Objects.requireNonNull(rvQuestions.getAdapter())).getAnswers();
            Log.d("debug", "onViewCreated: " + answers.size());
        });


        motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int state, int i1) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int state, int i1, float v) {
            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, final int state) {
                if (state == R.id.hide_reading_show_snackbar || state == R.id.show_reading_show_snackbar) {
                    mViewModel.toggleClosed(true);
                } else {
                    mViewModel.toggleClosed(false);
                }
                if (mViewModel.isClosed()) {
                    fab.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_cross));
                } else {
                    fab.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.pause_two_lines));
                }

            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int state, boolean b, float v) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rvQuestions = null;
        pauseTestButton = null;
        stopTestButton = null;
        fab = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            int year, major;
            year = TestFragmentArgs.fromBundle(getArguments()).getYear();
            major = TestFragmentArgs.fromBundle(getArguments()).getMajor();
            mViewModel = ViewModelProviders.of(this).get(TestViewModel.class);
            mViewModel.init(new TestRepository(Objects.requireNonNull(getActivity()).getApplication()), year, major);
            mViewModel.getQuestionLiveData().observe(this, questions -> {
                if (questions != null && rvQuestions != null && rvQuestions.getAdapter() != null) {
                    ((QuestionAdapter) rvQuestions.getAdapter()).submitList(questions);
                }
            });
        }
    }


    @Override
    public void onReadingShown(final Question question) {
        if (question.getReadingID() != -1) {
            Reading reading = testRepository.getReading(question.getReadingID());
            tvReadingContent.setHtml(reading.getPassage());
            if (motionLayout.getCurrentState() != R.id.show_reading_hide_snackbar && motionLayout.getCurrentState() != R.id.show_reading_show_snackbar) {
                motionLayout.setTransition(motionLayout.getCurrentState(), R.id.show_reading_hide_snackbar);
                motionLayout.transitionToEnd();
            }
        } else {
            if (motionLayout.getCurrentState() != R.id.hide_reading_hide_snackbar && motionLayout.getCurrentState() != R.id.hide_reading_show_snackbar) {
                motionLayout.setTransition(motionLayout.getCurrentState(), R.id.hide_reading_hide_snackbar);
                motionLayout.transitionToEnd();
            }
        }
    }

    @Override
    public void onQuestionAnswered(final Question question, final int position) {
        if (rvQuestions != null) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                rvQuestions.smoothScrollToPosition(position + 1);
            }, 200);
        }
    }
}
