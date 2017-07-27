package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//import info.androidhive.loginandregistration.R;
//import info.androidhive.loginandregistration.app.AppConfig;
//import info.androidhive.loginandregistration.app.AppController;
//import info.androidhive.loginandregistration.helper.SQLiteHandler;
//import info.androidhive.loginandregistration.helper.SessionManager;

public class SignUp extends Activity {
    DatabaseHelper myDb;
    String UserName,PhoneNo,UserPassword,Address;
    private static final String TAG = SignUp.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword,etAddress;
    private ProgressDialog pDialog;
    private SessionManager session;
    //private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        myDb = new DatabaseHelper(this);

        //etAddress = (EditText) findViewById(R.id.et_Address);
        inputFullName = (EditText) findViewById(R.id.et_first_name);
        inputEmail = (EditText) findViewById(R.id.et_email_signUp);
        inputPassword = (EditText) findViewById(R.id.et_passwordSignup);
        btnRegister = (Button) findViewById(R.id.btn_submitsignup);
        btnLinkToLogin = (Button) findViewById(R.id.btn_cancel_signup);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        //db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(SignUp.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

               // Address = etAddress.getText().toString().trim();
                UserName = inputFullName.getText().toString().trim();

                PhoneNo = inputEmail.getText().toString().trim();
                Log.d("dudu",PhoneNo);
                UserPassword = inputPassword.getText().toString().trim();

                if (!UserName.isEmpty() && !PhoneNo.isEmpty() && !UserPassword.isEmpty()) {

                    //validatePhoneNo(PhoneNo);

                    registerUser(UserName, PhoneNo,UserPassword);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        SignIn.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String name, final String email,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                Config.signup_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                showLogMessage("Sign up","User signed up");

                try {
                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");
                   // showMessage("ID", String.valueOf(error));

                    if (error==false) {
                        String id = jObj.getString("id");
                        String name = jObj.getString("name");
                        String email = jObj.getString("email");
                        //String address = jObj.getString("Address");
                        String Key="User";
                        myDb.insertData(id,name,email,Key);
                        //prepareProfPic(id);
                        Intent intent = new Intent(SignUp.this,SignIn.class);
                        startActivity(intent);
                        finish();

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);


                return params;
            }

        };

        // Adding request to request queue
        Mimi.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showLogMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        //builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}

    public void validatePhoneNo(String phoneNumber) {
        //String phoneNumber = etPhoneNo.getText().toString();

        Random r = new Random();
        int code = r.nextInt(98764 - 13469) + 13469;

        String sentCode=String.valueOf(code);
        try {
            SmsManager.getDefault().sendTextMessage(phoneNumber, null, sentCode, null, null);
        } catch (Exception e) {
            AlertDialog.Builder alertDialogBuilder = new

                    AlertDialog.Builder(this);

            AlertDialog dialog = alertDialogBuilder.create();

            dialog.setMessage(e.getMessage());

            dialog.show();

        }

        validateCodeDialog(sentCode);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void validateCodeDialog(final String code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_validate_code, null);
        builder.setView(v);
        builder.setCancelable(true);
       // builder.setTitle("Submit verification code");

        //editText.setText("test label");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


        final EditText editText = (EditText) v.findViewById(R.id.etMarks);
        Button btSubmitCode=(Button)v.findViewById(R.id.btDialog);
        btSubmitCode.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean submitted;

                        submitted=checkEmpty((EditText) editText);

                        if(submitted==false)
                        {
                            //Do nothing and wait
                        }else{



                            String one = editText.getText().toString().trim();
                            boolean correct = code.equals(one);

                            if(correct==true){


                                registerUser(UserName, PhoneNo,UserPassword);
                            }else {
                                showMessage("Validation error","The verification code is not what was sent to "+PhoneNo+". Check your text message and try to re-enter the code or try signing up afresh ");
                            }
                        }


                           // returnCAT();
                    }
                }
        );

        // builder.setMessage(message);
        // builder.show();}


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}

    private boolean checkEmpty(EditText et) {
        String email = "";
        email = et.getText().toString();

        et.setError(null);

        if (email.isEmpty()) {
            et.setError(getString(R.string.error_email_required));
            et.requestFocus();
            return false;
        } else


        return true;
        }


    private void prepareProfPic(final String id){



            StringRequest request = new StringRequest(Request.Method.POST,Config.prep_prof_pic_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {

                    Toast.makeText(SignUp.this, "ProfPic environment ready", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("EROR", volleyError.toString());
                    Toast.makeText(getBaseContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> stringMap = new HashMap<>();
                    stringMap.put("id",id);

                    return stringMap;
                }
            };
            Volley.newRequestQueue(getBaseContext()).add(request);
         }

    public void viewSpecificData() {

                        Cursor res = myDb.getAllData();

                        if (res.getCount() == 0) {
                            //Show message
                            showMessage("Error", "No data found Silas");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("dbID : " + res.getString(0) + "\n");
                            buffer.append("id : " + res.getString(1) + "\n");
                            buffer.append("Name : " + res.getString(2) + "\n");
                            buffer.append("Email : " + res.getString(3) + "\n\n");
                        }

                        //Show all data

                        showMessage("Data", buffer.toString());


    }
    }



