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
import com.simorgh.database.model.Answer;
import com.simorgh.database.model.Question;
import com.simorgh.database.model.YearMajorData;
import com.simorgh.englishtest.BaseFragment;
import com.simorgh.englishtest.R;
import com.simorgh.englishtest.adapter.QuestionAdapter;
import com.simorgh.englishtest.model.AppManager;
import com.simorgh.englishtest.util.DialogMaker;
import com.simorgh.englishtest.viewModel.TestViewModel;
import com.simorgh.fluidslider.FluidSlider;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.inflationx.calligraphy3.CalligraphyUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TestFragment extends BaseFragment implements QuestionAdapter.OnReadingShownListener, QuestionAdapter.OnAnswerListener {

    private TestViewModel mViewModel;
    private RecyclerView rvQuestions;
    private ImageButton fab;
    private ImageButton pauseTestButton;
    private ImageButton stopTestButton;
    private ImageButton btnRight;
    private ImageButton btnLeft;
    private TextView tvReadingTitle;
    private HtmlTextView tvReadingContent;
    private Typeface typeface;
    private MotionLayout motionLayout;
    private OnAppTitleChangedListener onAppTitleChangedListener;
    private int lastViewPosition = 0;
    private FluidSlider fluidSlider;
    private TimerListener timerListener;
    private TextView pauseText;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            int year, major;
            boolean isTestType;
            year = TestFragmentArgs.fromBundle(getArguments()).getYear();
            major = TestFragmentArgs.fromBundle(getArguments()).getMajor();
            isTestType = TestFragmentArgs.fromBundle(getArguments()).getIsTestType();
            mViewModel = ViewModelProviders.of(this).get(TestViewModel.class);
            mViewModel.init(AppManager.getRepository(), year, major, isTestType);
            if (onAppTitleChangedListener != null && mViewModel != null) {
                onAppTitleChangedListener.onAppTitleChanged(mViewModel.getFragmentTitle());
            }
            if (mViewModel.getQuestionLiveData() != null) {
                mViewModel.getQuestionLiveData().observe(this, questions -> {
                    if (questions != null && rvQuestions != null && rvQuestions.getAdapter() != null) {
                        ((QuestionAdapter) rvQuestions.getAdapter()).submitList(questions);
                        fluidSlider.setCurrentPosition(0);
                        fluidSlider.setMinMax(new Pair<>(1, questions.size()));
                    }
                });
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            if (TestFragmentArgs.fromBundle(getArguments()).getIsTestType()) {
                return inflater.inflate(R.layout.test_fragment, container, false);
            } else {
                return inflater.inflate(R.layout.fragment_practice, container, false);
            }
        }
        return null;
    }


    @SuppressLint({"ClickableViewAccessibility", "CheckResult"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setLayoutDirection(view, ViewCompat.LAYOUT_DIRECTION_LTR);

        if (timerListener != null && mViewModel.isTestType()) {
            timerListener.initTimer(YearMajorData.getMajorTime(mViewModel.getMajor()) * 60 * 1000, this::showTestResult);
            timerListener.reset();
        }

        if (typeface == null) {
            typeface = Typeface.createFromAsset(Objects.requireNonNull(getContext()).getAssets(), "fonts/tnr.ttf");
        }

        rvQuestions = view.findViewById(R.id.rv_questions);
        fab = view.findViewById(R.id.fab_test);

        motionLayout = (MotionLayout) view;
        tvReadingTitle = view.findViewById(R.id.tv_reading_title);
        tvReadingContent = view.findViewById(R.id.tv_reading_content);

        btnRight = view.findViewById(R.id.btnRight);
        btnLeft = view.findViewById(R.id.btnLeft);
        fluidSlider = view.findViewById(R.id.fluidSlider);
        fluidSlider.setCurrentPosition(0);
        fluidSlider.setMinMax(new Pair<>(1, 25));

        CalligraphyUtils.applyFontToTextView(tvReadingTitle, typeface);
        CalligraphyUtils.applyFontToTextView(tvReadingContent, typeface);

        boolean isTestType = false;
        if (getArguments() != null) {
            isTestType = TestFragmentArgs.fromBundle(getArguments()).getIsTestType();
        }

        rvQuestions.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true);
        rvQuestions.setLayoutManager(linearLayoutManager);
        rvQuestions.setNestedScrollingEnabled(false);

        boolean finalIsTestType = isTestType;
        AppManager.getRepository().getUserSingle()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(user -> user != null)
                .subscribe(user -> {
                    rvQuestions.setAdapter(new QuestionAdapter(new QuestionAdapter.ItemDiffCallBack()
                            , this, this, finalIsTestType, () -> !mViewModel.isPaused()
                            , user.getFontSize()));
                    tvReadingContent.setTextSize(user.getFontSize());

                });

        rvQuestions.setHasFixedSize(true);

        //disable scrolling in recyclerView
        rvQuestions.setOnTouchListener((v, event) -> true);

        rvQuestions.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                //While the ViewPager is scrolling, disable the
//                boolean isScrolling = newState != ViewPager.SCROLL_STATE_IDLE;
//                rvQuestions.requestDisallowInterceptTouchEvent(isScrolling);
            }

        });

        //add snap helper to rv
//        new LinearSnapHelper().attachToRecyclerView(rvQuestions);


        initFAB();


        btnLeft.setOnClickListener(v -> {
            if (lastViewPosition + 1 < Objects.requireNonNull(rvQuestions.getAdapter()).getItemCount()) {
                rvQuestions.smoothScrollToPosition(++lastViewPosition);
                fluidSlider.setCurrentPosition(lastViewPosition);
            }
        });

        btnRight.setOnClickListener(v -> {
            if (lastViewPosition - 1 >= 0) {
                rvQuestions.smoothScrollToPosition(--lastViewPosition);
                fluidSlider.setCurrentPosition(lastViewPosition);
            }
        });


        fluidSlider.setFluidSliderListener(new FluidSlider.FluidSliderListener() {
            @Override
            public void invokePosition(final int position, boolean fromUser) {
                if (rvQuestions != null) {
                    if (fromUser) {
                        rvQuestions.smoothScrollToPosition(position - 1);
                        lastViewPosition = position - 1;
                    }
                }
            }

            @Override
            public void invokeBeginTracking() {

            }

            @Override
            public void invokeEndTracking() {

            }
        });

        if (mViewModel != null && mViewModel.isTestType()) {
            pauseTestButton = view.findViewById(R.id.img_pause);
            stopTestButton = view.findViewById(R.id.img_stop);
            pauseText = view.findViewById(R.id.tv_pause);

            pauseTestButton.setOnClickListener(v -> {
                if (!mViewModel.isPaused()) {
                    timerListener.pause();
                    mViewModel.setPaused(true);
                    pauseTestButton.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_play_arrow_black_24dp));
                    fab.performClick();
                    pauseText.setText("ادامه آزمون");
                } else {
                    timerListener.resume();
                    mViewModel.setPaused(false);
                    pauseTestButton.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.pause_two_lines));
                    fab.performClick();
                    pauseText.setText("توقف آزمون");
                }
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
                    fluidSlider.setEnabled(!mViewModel.isClosed());
                    btnLeft.setEnabled(!mViewModel.isClosed());
                    btnRight.setEnabled(!mViewModel.isClosed());
                    if (mViewModel.isClosed()) {
                        fab.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_cross));
                    } else {
                        if (mViewModel.isPaused()) {
                            fab.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_play_arrow_black_24dp));
                        } else {
                            fab.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.pause_two_lines));
                        }
                    }

                }

                @Override
                public void onTransitionTrigger(MotionLayout motionLayout, int state, boolean b, float v) {

                }
            });
        }
    }

    private void initFAB() {
        fab.setOnClickListener(v -> {
            if (mViewModel != null && mViewModel.isTestType()) {
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
            } else {
                ((QuestionAdapter) Objects.requireNonNull(rvQuestions.getAdapter())).toggleShowAnswer(lastViewPosition);
            }
        });
    }

    private void showTestResult() {
        List<Answer> answers = ((QuestionAdapter) Objects.requireNonNull(rvQuestions.getAdapter())).getAnswers();
        Date date = ((QuestionAdapter) Objects.requireNonNull(rvQuestions.getAdapter())).getDate();
        if (date != null && mViewModel != null) {
            if (!answers.isEmpty()) {
                mViewModel.getRepository().updateAnswers(answers);

                try {
                    Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment)
                            .navigate(TestFragmentDirections.actionTestFragmentToTestResultFragment()
                                    .setDate(date.getDateLong())
                                    .setYear(mViewModel.getYear())
                                    .setMajor(mViewModel.getMajor())
                                    .setIsTestType(mViewModel.isTestType()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment).navigateUp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (timerListener != null) {
            timerListener.reset();
            timerListener.stop();
        }
        rvQuestions = null;
        pauseTestButton = null;
        stopTestButton = null;
        fab = null;
        fluidSlider = null;
        btnRight = null;
        btnLeft = null;
        tvReadingTitle = null;
        motionLayout = null;
        tvReadingContent = null;
        pauseText = null;
        super.onDestroyView();
    }

    public interface OnAppTitleChangedListener {
        void onAppTitleChanged(final String titleText);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onAppTitleChangedListener = (OnAppTitleChangedListener) context;
        timerListener = (TimerListener) context;
    }

    @Override
    public void onDetach() {
        onAppTitleChangedListener = null;
        timerListener.stop();
        timerListener = null;
        super.onDetach();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onReadingShown(final Question question) {
        if (question.getReadingID() != -1) {
            AppManager.getRepository().getReading(question.getReadingID())
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(reading -> reading != null)
                    .subscribe(reading -> {
                        tvReadingContent.setHtml(reading.getPassage());
                        if (motionLayout.getCurrentState() != R.id.show_reading_hide_snackbar && motionLayout.getCurrentState() != R.id.show_reading_show_snackbar) {
                            motionLayout.setTransition(motionLayout.getCurrentState(), R.id.show_reading_hide_snackbar);
                            motionLayout.transitionToEnd();
                        }
                    });
        } else {
            if (motionLayout.getCurrentState() != R.id.hide_reading_hide_snackbar
                    && motionLayout.getCurrentState() != R.id.hide_reading_show_snackbar) {
                motionLayout.setTransition(motionLayout.getCurrentState(), R.id.hide_reading_hide_snackbar);
                motionLayout.transitionToEnd();
            }
        }
    }

    @Override
    public void onQuestionAnswered(final Question question, final int position) {
        if (rvQuestions != null) {
            lastViewPosition = position;
            if (Objects.requireNonNull(rvQuestions.getAdapter()).getItemCount() == position + 1) {
                if (mViewModel.isTestType()) {
                    DialogMaker.createTestEndDialog(Objects.requireNonNull(getActivity()), v -> {
                    }, v -> showTestResult());
                }
            } else {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    rvQuestions.smoothScrollToPosition(position + 1);
                    fluidSlider.setCurrentPosition(position + 1);
                    lastViewPosition = position + 1;
                }, 200);
            }
        }
    }

    public interface TimerListener {
        void initTimer(long time, FinishedListener finishedListener);

        void reset();

        void pause();

        void stop();

        void resume();

    }

    public interface FinishedListener {
        void finished();
    }
}
