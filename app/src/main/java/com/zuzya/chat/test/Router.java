package com.zuzya.chat.test;

import android.content.Context;

import com.zuzya.chat.R;
import com.zuzya.chat.test.viewmodels.TestViewModel;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class Router {
    private static Router singleton;

    private final Context context;

    private BehaviorSubject<Screen> currentScreen;

    Router(Context applicationContext) {
        context = applicationContext;
        currentScreen = BehaviorSubject.create();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    currentScreen.onNext(new Screen(R.layout.test_layout, new TestViewModel()));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public synchronized static Router getSingleton(Context applicationContext) {
        if (singleton == null) {
            singleton = new Router(applicationContext);
        }
        return singleton;
    }

    public Observable<Screen> getCurrentScreen() {
        return currentScreen;
    }
}
