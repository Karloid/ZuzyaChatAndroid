package com.zuzya.chat.meta;

import android.view.View;

import com.zuzya.chat.viewmodels.HasViewModel;
import com.zuzya.chat.viewmodels.ViewModel;

public class Screen {
    private int layoutId;
    private ViewModel viewModel;

    public Screen(int layoutId, ViewModel viewModel) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void bind(View view) {
        ((HasViewModel) view).setViewModel(viewModel);
    }

    public ViewModel getViewModel() {
        return viewModel;
    }
}
