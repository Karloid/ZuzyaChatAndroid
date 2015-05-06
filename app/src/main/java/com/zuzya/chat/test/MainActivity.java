package com.zuzya.chat.test;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.zuzya.chat.R;

import java.util.zip.Inflater;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbarAndDrawer();
        container = (LinearLayout) findViewById(R.id.main_container);
        setupRX();
    }

    private void setupRX() {
        Router router = Router.getSingleton(this.getApplicationContext());
        router.getCurrentScreen().subscribe(new Action1<Screen>() {
            @Override
            public void call(final Screen screen) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final View oldChild = container.getChildAt(1);
                        if (oldChild != null) {

                            oldChild.animate()     //TODO rework
                                    .translationY(1000)
                                    .alpha(0.0f)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            container.removeView(oldChild);
                                            if (screen == null) return;
                                            int newLayoutId = screen.getLayoutId();
                                            View newChild = getLayoutInflater().inflate(newLayoutId, container);
                                            //TODO bind ViewModel
                                        }
                                    });
                        } else {  //TODO rework
                            if (screen == null) return;
                            int newLayoutId = screen.getLayoutId();
                            View newChild = getLayoutInflater().inflate(newLayoutId, container);
                            //TODO bind ViewModel
                        }

                    }
                });
            }

        });
    }

    private void setupToolbarAndDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        // Now retrieve the DrawerLayout so that we can set the status bar color.
        // This only takes effect on Lollipop, or when using translucentStatusBar
        // on KitKat.
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark));
    }
}
