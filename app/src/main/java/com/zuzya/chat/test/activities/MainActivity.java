package com.zuzya.chat.test.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.zuzya.chat.R;
import com.zuzya.chat.test.Router;
import com.zuzya.chat.test.Screen;

import java.util.zip.Inflater;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbarAndDrawer();
        container = (FrameLayout) findViewById(R.id.main_container);
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
                        handleNewScreen(screen);
                    }
                });
            }
        });
    }

    private void handleNewScreen(final Screen screen) {
        int childcount = container.getChildCount();
        for (int i = 0; i < childcount; i++) {
            final View oldChild = container.getChildAt(i);
            if (oldChild != null) {
                oldChild.animate()     //TODO rework
                        .translationY(1000)
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                container.removeView(oldChild);
                            }
                        });
            }
        }
        if (screen == null) return;
        int newLayoutId = screen.getLayoutId();
        View newView = getLayoutInflater().inflate(newLayoutId, container, false);
        screen.bind(newView);
        container.addView(newView);
        newView.setAlpha(0);
        newView.setTranslationX(-300);
        newView.setTranslationY(-300);
        newView.setScaleX(0.1f);
        newView.setScaleY(0.1f);
        newView.animate().alpha(1)
                .translationX(0)
                .translationY(0)
                .scaleX(1)
                .scaleY(1)
                .start();
        //TODO bind ViewModel
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
