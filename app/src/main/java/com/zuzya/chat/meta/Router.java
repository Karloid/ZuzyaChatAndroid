package com.zuzya.chat.meta;

import android.content.Context;

import com.zuzya.chat.R;
import com.zuzya.chat.viewmodels.ChatViewModel;
import com.zuzya.chat.viewmodels.TestViewModel;

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
                for (int i = 0; i < 4; i++){
                    currentScreen.onNext(new Screen(R.layout.v_test, new TestViewModel()));
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                currentScreen.onNext(new Screen(R.layout.v_chat, new ChatViewModel(context)));
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
