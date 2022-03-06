package com.example.sassydesign;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    public static Activity activity;

    EditText LoginID;
    EditText LoginPW;
    private AlertDialog dialog;
    InputMethodManager imm;  //화면에 나오는 키보드 보이기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        Button registerButton =  findViewById(R.id.SignUpButton);
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, SignupActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        final EditText idText = (EditText) findViewById(R.id.LoginID);
        final EditText passwordText = (EditText) findViewById(R.id.LoginPW);
        final Button loginButton = (Button) findViewById(R.id.LoginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "";
                final String ID=idText.getText().toString();
                final String PW=passwordText.getText().toString();
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonResponse = new JSONObject(response);
                            Boolean success=jsonResponse.getBoolean("success");

                            if(success){
                                Dialog dialog = new Dialog(LoginActivity.this);
                                dialog.setContentView(R.layout.dialog_not_context);
                                TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                                dialogTitle.setText("로그인에 성공했습니다.");
                                Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("id", ID);
                                LoginActivity.this.startActivity(intent);
                                finish();
                            }
                            else{
                                Dialog dialog = new Dialog(LoginActivity.this);
                                dialog.setContentView(R.layout.dialog_not_context);
                                TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                                dialogTitle.setText("계정을 다시 확인하세요.");
                                Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> parameters = new HashMap<>();
                        parameters = new HashMap<>();
                        parameters.put("id",ID);
                        parameters.put("password", PW);

                        return parameters;
                    }

                };
                request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                        20000 ,
                        com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                request.setShouldCache(false);
                Volley.newRequestQueue(LoginActivity.this).add(request);

            }
        });

    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if(dialog != null)
        {
            dialog.dismiss();
            dialog = null;
        }
    }

}
