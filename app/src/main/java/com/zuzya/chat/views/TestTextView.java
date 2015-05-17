package com.zuzya.chat.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zuzya.chat.viewmodels.HasViewModel;
import com.zuzya.chat.viewmodels.TestViewModel;
import com.zuzya.chat.viewmodels.ViewModel;

import rx.functions.Action1;

public class TestTextView extends TextView implements HasViewModel {

    private TestViewModel viewModel;

    public TestTextView(Context context) {
        super(context);
    }

    public TestTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TestTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setViewModel(ViewModel viewModel) {
        this.viewModel = (TestViewModel) viewModel;
        subscribeViewModel();
    }

    private void subscribeViewModel() {   //TODO memory management
        viewModel.getText().subscribe(new Action1<String>() {
            @Override
            public void call(final String s) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        setText(s);
                    }
                });
            }
        });

        viewModel.getColor().subscribe(new Action1<Integer>() {
            @Override
            public void call(final Integer color) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        setBackgroundColor(color);
                    }
                });
            }
        });
    }
}
