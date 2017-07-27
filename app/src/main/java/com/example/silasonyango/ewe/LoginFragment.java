package com.example.silasonyango.ewe;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginFragment extends Fragment {
    ImageView background;
    ViewPager pager;
    TextView title;
    int resid, resid_land;
    Bundle b;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_frag, container, false);
        background = (ImageView) v.findViewById(R.id.img_login_background);
        title = (TextView) v.findViewById(R.id.tv_login_splash);

        b = this.getArguments();
        resid = b.getInt("Resid");
        resid_land = b.getInt("Resid_land");
        title.setText(b.getString("Title"));

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            background.setBackgroundResource(resid_land);

        } else {
            background.setBackgroundResource(resid);

        }

        return v;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // background.setImageDrawable(ContextCompat.getDrawable(getContext(),
            // resid));
            background.setBackgroundResource(resid);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // background.setImageDrawable(ContextCompat.getDrawable(getContext(),
            // resid_land));
            background.setBackgroundResource(resid_land);
        }
    }
}
