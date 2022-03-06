package com.example.sassydesign;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private String id;
    private String password;
    private String name;
    private AlertDialog dialog;
    private boolean validate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        final EditText idText = (EditText) findViewById(R.id.editTextTextPersonName);
        final EditText passwordText = (EditText) findViewById(R.id.editTextTextPersonName2);
        final EditText nameText = (EditText) findViewById(R.id.editTextTextPersonName3);
        final Button validateButton = (Button) findViewById(R.id.button);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = idText.getText().toString();
                if(validate) {
                    return;
                }
                if(id.equals("")) {
                    Dialog dialog = new Dialog(SignupActivity.this);
                    dialog.setContentView(R.layout.dialog_not_context);
                    TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                    dialogTitle.setText("아이디는 빈 칸 일 수 없습니다.");
                    Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                    return;
                }
                String url = "";

                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success)
                            {
                                Dialog dialog = new Dialog(SignupActivity.this);
                                dialog.setContentView(R.layout.dialog_not_context);
                                TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                                dialogTitle.setText("사용할 수 있는 아이디입니다.");
                                Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();

                                idText.setEnabled(false);
                                validate = true;
                            }
                            else {
                                Dialog dialog = new Dialog(SignupActivity.this);
                                dialog.setContentView(R.layout.dialog_not_context);
                                TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                                dialogTitle.setText("사용할 수 없는 아이디입니다.");
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
                        parameters.put("id",id);

                        return parameters;
                    }
                };
                request.setShouldCache(false);
                Volley.newRequestQueue(SignupActivity.this).add(request);

            }
        });

        final Button registerButton = (Button) findViewById(R.id.signupButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = idText.getText().toString();
                final String password = passwordText.getText().toString();
                final String name = nameText.getText().toString();



                if(!validate)
                {
                    Dialog dialog = new Dialog(SignupActivity.this);
                    dialog.setContentView(R.layout.dialog_not_context);
                    TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                    dialogTitle.setText("먼저 중복 체크를 해주세요.");
                    Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                    return;
                }

                if(id.equals("") || password.equals("") ||name.equals(""))
                {
                    Dialog dialog = new Dialog(SignupActivity.this);
                    dialog.setContentView(R.layout.dialog_not_context);
                    TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                    dialogTitle.setText("빈 칸 없이 입력해주세요.");
                    Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return;
                }

                if(!Pattern.matches("((?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9가-힣]).{8,})", password))
                {
                    Dialog dialog = new Dialog(SignupActivity.this);
                    dialog.setContentView(R.layout.dialog_not_context);
                    TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                    dialogTitle.setText("올바른 PW가 아닙니다. 영문 소문자, 숫자, 특수문자를 모두 포함하여 8자 이상 써주세요.");
                    Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return;
                }


                String url = "";

                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Dialog dialog = new Dialog(SignupActivity.this);
                                dialog.setContentView(R.layout.dialog_not_context);
                                TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                                dialogTitle.setText("회원 등록에 성공했습니다.");
                                Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();

                                finish();
                            } else {
                                Dialog dialog = new Dialog(SignupActivity.this);
                                dialog.setContentView(R.layout.dialog_not_context);
                                TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                                dialogTitle.setText("회원 등록에 실패했습니다.");
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
                        parameters.put("id",id);
                        parameters.put("password", password);
                        parameters.put("name", name);

                        return parameters;
                    }

                };
                request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(

                        20000 ,

                        com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                        com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                request.setShouldCache(false);
                Volley.newRequestQueue(SignupActivity.this).add(request);
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