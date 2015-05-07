package com.zuzya.chat.test.viewmodels;

import android.graphics.Color;

import java.util.Random;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class TestViewModel implements ViewModel {
    private BehaviorSubject<String> text;
    private BehaviorSubject<Integer> color;

    public TestViewModel() {       //TODO memory management

        Random rnd = new Random();
        int colorInt = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        color = BehaviorSubject.create(colorInt);

        text = BehaviorSubject.create("LOL");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    text.onNext(text.getValue() + ".");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        });
        thread.start();
    }

    public Observable<String> getText() {
        return text;
    }

    public Observable<Integer> getColor() {
        return color;
    }

}
