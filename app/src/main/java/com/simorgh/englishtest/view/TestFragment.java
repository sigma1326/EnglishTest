package com.simorgh.englishtest.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.simorgh.database.Date;
import com.simorgh.database.TestRepository;
import com.simorgh.database.model.Answer;
import com.simorgh.database.model.Question;
import com.simorgh.database.model.Reading;
import com.simorgh.englishtest.DialogMaker;
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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
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
    private OnAppTitleChangedListener onAppTitleChangedListener;


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

//        rvQuestions.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                mViewModel.setCanTouch(newState != RecyclerView.SCROLL_STATE_SETTLING);
//            }
//
//        });
//        rvQuestions.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//                if (e.getAction() == MotionEvent.ACTION_DOWN && rv.getScrollState() == RecyclerView.SCROLL_STATE_SETTLING) {
//                    Log.d("debug13", "onInterceptTouchEvent: click performed");
//                    if (mViewModel.isCanTouch()) {
//                        rv.stopScroll();
//                        Objects.requireNonNull(rv.findChildViewUnder(e.getX(), e.getY())).performClick();
//                        return true;
//                    }
//                }
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//                Log.d("debug13", "onTouchEvent: ");
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//        });

        //add snap helper to rv
//        new LinearSnapHelper().attachToRecyclerView(rvQuestions);


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


        });

        stopTestButton.setOnClickListener(v -> {
            showTestResult();
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

    private void showTestResult() {
        List<Answer> answers = ((QuestionAdapter) Objects.requireNonNull(rvQuestions.getAdapter())).getAnswers();
        Date date = ((QuestionAdapter) Objects.requireNonNull(rvQuestions.getAdapter())).getDate();
        if (date != null && mViewModel != null) {
            mViewModel.getTestRepository().updateAnswers(answers);

            Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment)
                    .navigate(TestFragmentDirections.actionTestFragmentToTestResultFragment()
                            .setDate(date.getMilli())
                            .setYear(mViewModel.getYear())
                            .setMajor(mViewModel.getMajor())
                            .setIsTestType(mViewModel.isTestType()));
        }
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
            boolean isTestType;
            year = TestFragmentArgs.fromBundle(getArguments()).getYear();
            major = TestFragmentArgs.fromBundle(getArguments()).getMajor();
            isTestType = TestFragmentArgs.fromBundle(getArguments()).getIsTestType();
            mViewModel = ViewModelProviders.of(this).get(TestViewModel.class);
            mViewModel.init(new TestRepository(Objects.requireNonNull(getActivity()).getApplication()), year, major, isTestType);
            if (onAppTitleChangedListener != null && mViewModel != null) {
                onAppTitleChangedListener.onAppTitleChanged(mViewModel.getFragmentTitle());
            }
            mViewModel.getQuestionLiveData().observe(this, questions -> {
                if (questions != null && rvQuestions != null && rvQuestions.getAdapter() != null) {
                    ((QuestionAdapter) rvQuestions.getAdapter()).submitList(questions);
                }
            });
        }
    }

    public interface OnAppTitleChangedListener {
        void onAppTitleChanged(final String titleText);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onAppTitleChangedListener = (OnAppTitleChangedListener) context;
    }

    @Override
    public void onDetach() {
        onAppTitleChangedListener = null;
        super.onDetach();
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
            if (Objects.requireNonNull(rvQuestions.getAdapter()).getItemCount() == position + 1) {
                DialogMaker.createTestEndDialog(Objects.requireNonNull(getActivity()), v -> {
                    //todo check this
                }, v -> showTestResult());
            } else {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    rvQuestions.smoothScrollToPosition(position + 1);
                }, 200);
            }
        }
    }
}
