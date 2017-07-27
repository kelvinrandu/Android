package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by hp on 2/18/2016.
 */
public class SplashScreen extends AppCompatActivity implements View.OnClickListener {
    ViewPager pager;

  com.example.silasonyango.ewe.PagerAdapter adapter;
    int i[] = {R.drawable.splashbackground, R.drawable.splash};
    private String[] title = {"The Platform that allows people to share their ideas ","Get Funding to realise your Dreams"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (new SessionManager(getBaseContext()).isLoggedIn()) {
            Intent in = new Intent(getBaseContext(), Landing.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
            finish();
        }
        setContentView(R.layout.splash_screen);
        setuppager();
        findViewById(R.id.btn_signin).setOnClickListener(this);
        findViewById(R.id.btn_signup).setOnClickListener(this);
        findViewById(R.id.tv_asGuest).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signin:
                Intent intent = new Intent(getBaseContext(), SignIn.class);
                startActivity(intent);
                break;
            case R.id.btn_signup:
                Intent in = new Intent(getBaseContext(), SignUp.class);
                startActivity(in);
                break;
            case R.id.tv_asGuest:
                Intent main_intent = new Intent(getBaseContext(), MainActivity.class);
                main_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(main_intent);
                break;
        }

    }

    private void setuppager() {
        pager = (ViewPager) findViewById(R.id.splash_pager);
        //indicator = (CircleIndicator) findViewById(R.id.splash_indicator);
        adapter = new com.example.silasonyango.ewe.PagerAdapter(getSupportFragmentManager());
        for (int j = 0; j < i.length; j++) {
            Bundle bundle = new Bundle();
            bundle.putInt("Resid", i[j]);
            //bundle.putInt("Resid_land", i_land[j]);
            bundle.putString("Title", title[j]);
            LoginFragment frag = new LoginFragment();
            frag.setArguments(bundle);
            adapter.addFragment(frag);
        }
        // change();
        pager.setAdapter(adapter);
        pager.setPageTransformer(true, new PageTransform());
       // indicator.setViewPager(pager);

        // Slow scroll
        try {
            Interpolator sInterpolator = new DecelerateInterpolator();
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedScroller scroller = new FixedScroller(pager.getContext(),
                    sInterpolator, 500);
            // scroller.setFixedDuration(5000);
            mScroller.set(pager, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {
        }

    }

    class PageTransform implements ViewPager.PageTransformer {

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            TextView title = (TextView) view.findViewById(R.id.tv_login_splash);
//            TextView message = (TextView) view
//                    .findViewById(R.id.tv_login_splashmessage);
            if (position <= -1.0F || position >= 1.0F) {
                view.setTranslationX(view.getWidth() * position);
                title.setTranslationX(position * (pageWidth));
                //message.setTranslationX(position * (pageWidth));
                view.setAlpha(0.0F);

            } else if (position == 0.0F) {
                view.setTranslationX(view.getWidth() * position);
                title.setTranslationX(position * (pageWidth));
                // message.setTranslationX(position * (pageWidth));
                view.setAlpha(1.0F);
            } else {
                // position is between -1.0F & 0.0F OR 0.0F & 1.0F
                view.setTranslationX(view.getWidth() * -position);
                title.setTranslationX(position * (pageWidth));
                // message.setTranslationX(position * (pageWidth));
                view.setAlpha(1.0F - Math.abs(position));
            }

        }
    }
}