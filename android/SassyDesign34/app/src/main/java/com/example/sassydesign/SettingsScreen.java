package com.example.sassydesign;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SettingsScreen extends Fragment {
    int replay = 0;
    TextView userName;
    EditText budgetText;
    ImageView profileImage;
    TextView userID;
    String personName = "";
    String photoNAME = "";
    String budget = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.settings, container, false);

        userName = rootView.findViewById(R.id.userName);
        budgetText = rootView.findViewById(R.id.budget);
        profileImage = rootView.findViewById(R.id.profileImage);
        userID = rootView.findViewById(R.id.userID);

        setProfile();

        Button logOutButton = rootView.findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_two_button);
                TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                TextView dialogMessage = (TextView)dialog.findViewById(R.id.dialog_message);
                dialogTitle.setText("로그아웃");
                dialogMessage.setText("로그아웃 하시겠습니까?");

                //취소 버튼
                Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //확인 버튼
                Button dialogButton2 = (Button)dialog.findViewById(R.id.dialog_button2);
                dialogButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        });

        Button withdrawalButton = rootView.findViewById(R.id.withdrawalButton);
        withdrawalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_two_button);
                TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                TextView dialogMessage = (TextView)dialog.findViewById(R.id.dialog_message);
                dialogTitle.setText("탈퇴");
                dialogMessage.setText("정말 탈퇴하시겠습니까?");

                //취소 버튼
                Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //확인 버튼
                Button dialogButton2 = (Button)dialog.findViewById(R.id.dialog_button2);
                dialogButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "";

                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONObject jsonResponse = new JSONObject(response);
                                    Boolean success=jsonResponse.getBoolean("success");

                                    if(success){
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                    else{

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
                                parameters.put("id", MainActivity.ID);

                                return parameters;
                            }

                        };
                        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                                20000 ,
                                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        request.setShouldCache(false);
                        Volley.newRequestQueue(getContext()).add(request);
                    }
                });
                dialog.show();
            }
        });
        Button saveButton = rootView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "";
                String budget = budgetText.getText().toString();

                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            Toast.makeText(getContext(), "예산이 저장되었습니다", Toast.LENGTH_SHORT).show();

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
                        parameters.put("id", MainActivity.ID);
                        parameters.put("budget", budget);
                        return parameters;
                    }
                };
                request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                        20000 ,
                        com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                request.setShouldCache(false);
                Volley.newRequestQueue(getContext()).add(request);
            }
        });

        Button profileModifyButton = rootView.findViewById(R.id.profileModifyButton);
        profileModifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileModifyIntent = new Intent(getActivity(), ProfileModify.class);
                profileModifyIntent.putExtra("name", personName);
                profileModifyIntent.putExtra("photolink", photoNAME);
                startActivity(profileModifyIntent);
            }
        });

        return rootView;
    }

    public void setProfile(){
        String url = "";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONArray jsonArray=new JSONArray(result);
                    ArrayList<String> NameList = new ArrayList<String>();
                    ArrayList<String> IdList = new ArrayList<String>();
                    ArrayList<String> budgetList = new ArrayList<String>();
                    ArrayList<String> photonameList = new ArrayList<String>();
                    String name = "";
                    String Id ="";
                    String photoname = "";
                    String budget3 = "";

                    int count = 0;
                    JSONObject object = jsonArray.getJSONObject(0);

                    name=object.getString("name");
                    Id= object.getString("id");
                    photoname = object.getString("photoname");
                    budget3 = object.getString("budget");
                    personName = name;
                    userName.setText(personName);
                    userID.setText(MainActivity.ID);

                    if (photoname.isEmpty()||photoname.equals("")) {
                        profileImage.setImageResource(R.drawable.default_profile_image);
                    } else {
                        Picasso.with(getContext())
                                .load(photoname)
                                .into(profileImage);
                    }

                    budget = budget3;
                    photoNAME = photoname;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                setText();
            }
        }, new Response.ErrorListener() {
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
                parameters.put("id", MainActivity.ID);

                return parameters;
            }

        };

        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                20000 ,
                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        Volley.newRequestQueue(getContext()).add(request);
    }

    public void setText(){
        userName.setText(personName);
        budgetText.setText(budget);
        userID.setText(MainActivity.ID);
        if (photoNAME.isEmpty()||photoNAME.equals("")) {
            profileImage.setImageResource(R.drawable.default_profile_image);
        } else{
            Picasso.with(getContext())
                    .load(photoNAME)
                    .into(profileImage);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (replay != 0){
            setProfile();
        }
        replay = 1;
    }
}