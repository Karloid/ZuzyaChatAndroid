package com.zuzya.chat.test;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.zuzya.chat.R;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
		setSupportActionBar(toolbar);

		// Now retrieve the DrawerLayout so that we can set the status bar color.
		// This only takes effect on Lollipop, or when using translucentStatusBar
		// on KitKat.
		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
		drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark));

	}
}
