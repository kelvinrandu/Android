package com.example.silasonyango.ewe;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class LogoutDialog extends DialogFragment {
    SharedPreferences pref;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getTag());
        builder.setPositiveButton(R.string.logout, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), SplashScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SessionManager sesion = new SessionManager(getContext());
                getContext();
                pref = getContext().getSharedPreferences("UserInfo",
                        Context.MODE_PRIVATE);
                pref.edit().clear().apply();

                sesion.setLogin(false);
                sesion.setSignedIn(false);
                startActivity(intent);

            }
        });
        builder.setNegativeButton("Cancel", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}
