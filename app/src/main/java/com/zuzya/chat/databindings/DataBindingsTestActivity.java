package com.zuzya.chat.databindings;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;

import com.zuzya.chat.R;
import com.zuzya.chat.databinding.ActivityDataBindingsBinding;

public class DataBindingsTestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDataBindingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_data_bindings);
        final User user = new User("Test", "User");
        binding.setUser(user);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int x = 0; x < 40; x++) {
                        for (int i = 0; i < 150; i++) {
                            user.setPaddingLeft(user.getPaddingLeft() + 1);
                            Thread.sleep(16);
                        }

                        for (int i = 0; i < 150; i++) {
                            user.setPaddingLeft(user.getPaddingLeft() - 1);
                            Thread.sleep(16);
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
