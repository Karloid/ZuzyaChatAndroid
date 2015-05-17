package com.zuzya.chat.test.viewmodels;

import com.zuzya.chat.viewmodels.ViewModel;

import rx.Observable;

public interface ActionBarProviderViewModel extends ViewModel {
    Observable<String> getActionBarTitle();
}
