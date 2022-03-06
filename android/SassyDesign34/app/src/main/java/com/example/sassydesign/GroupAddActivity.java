package com.example.sassydesign;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class GroupAddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Button betCreateCompleteButton;
    public static Activity groupAddActivity;
    String url="";
    String url2 = "";
    String groupName;
    String goal;
    String goalMoney1;
    String reward;
    String penalty;
    String startDay;
    String endDay;
    final CountDownLatch countDownLatch = new CountDownLatch(1);

    String category;
    private boolean validate = false;


    private String randomCode;
    private String random = "";

    private boolean check1=false; //randomcode가 데베에 이미 있는건지 확인

    EditText betCreateGroupName;
    EditText betCreateGroupGoal;
    EditText betCreateGroupMoney;
    EditText betCreateGroupReward;
    EditText betCreateGroupPenalty;
    Button betCreateFromDate;
    Button betCreateToDate;
    Spinner betCreateSharedSpinner;
    Button createInviteCode;
    Date selectedDate;

    String fromDate = "";
    String toDate = "";
    String a = "";

    long now = System.currentTimeMillis();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bet_create_group);

        groupAddActivity = GroupAddActivity.this;

        betCreateGroupName = findViewById(R.id.betCreateGroupName);
        betCreateGroupGoal = findViewById(R.id.betCreateGroupGoal);
        betCreateGroupMoney = findViewById(R.id.betCreateGroupMoney);
        betCreateGroupReward = findViewById(R.id.betCreateGroupReward);
        betCreateGroupPenalty = findViewById(R.id.betCreateGroupPenalty);
        betCreateFromDate = findViewById(R.id.betCreateFromDate);
        betCreateToDate = findViewById(R.id.betCreateToDate);
        betCreateSharedSpinner = findViewById(R.id.betCreateSharedSpinner);

        selectedDate = new Date(now);

        betCreateFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker1(v);
            }
        });

        betCreateToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker2(v);
            }
        });

        goalMoney1 = betCreateGroupMoney.getText().toString();
        createInviteCode = findViewById(R.id.createInviteCode);
        createInviteCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rnd = new Random();
                String random1 = "";
                String random2 = "";
                String random3 = "";
                String random4 = "";
                random1 = String.valueOf((char) ((int) (rnd.nextInt(26))+65));
                random2 = String.valueOf((char) ((int) (rnd.nextInt(26))+65));
                random3 = String.valueOf((char) ((int) (rnd.nextInt(26))+65));
                random4 = String.valueOf((char) ((int) (rnd.nextInt(26))+65));


                //완성된 랜덤 코드 : random
                String newrandom = "";
                newrandom = String.valueOf(random1+random2+random3+random4);

                String finalNewrandom = newrandom;
                String finalNewrandom1 = newrandom;
                StringRequest request2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success)
                            {
                                random = finalNewrandom1;

                                Dialog dialog = new Dialog(GroupAddActivity.this);
                                dialog.setContentView(R.layout.dialog_common);
                                TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                                TextView dialogMessage = (TextView)dialog.findViewById(R.id.dialog_message);
                                Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);

                                dialogTitle.setText("사용할 수 있는 랜덤코드입니다.");
                                dialogMessage.setText(random);
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();

                                createInviteCode.setEnabled(false);
                                createInviteCode.setBackground(getResources().getDrawable(R.drawable.button_not_enabled));

                                validate = true;
                            }
                            else {
                                Dialog dialog = new Dialog(GroupAddActivity.this);
                                dialog.setContentView(R.layout.dialog_common);
                                TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                                TextView dialogMessage = (TextView)dialog.findViewById(R.id.dialog_message);
                                Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);

                                dialogTitle.setText("사용할 수 없는 랜덤코드입니다.");
                                dialogMessage.setText(random);
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
                        parameters.put("inviteCode", finalNewrandom);

                        return parameters;
                    }
                };
                request2.setShouldCache(false);
                Volley.newRequestQueue(GroupAddActivity.this).add(request2);

            }
        });

        betCreateCompleteButton = findViewById(R.id.betCreateCompleteButton);
        betCreateCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean CheckBlank = false;
                boolean CheckBlank2 = false;

                //데이터베이스에 넘겨줄 변수들
                //그룹이름
                groupName = betCreateGroupName.getText().toString();
                //목표
                goal = betCreateGroupGoal.getText().toString();
                //목표금액
                goalMoney1 = betCreateGroupMoney.getText().toString();
                int goalMoney2 = 0;
                try {
                    goalMoney2 = Integer.parseInt(goalMoney1);
                } catch (NumberFormatException e){

                } catch (Exception e) {

                }


                //보상
                reward = betCreateGroupReward.getText().toString();
                //벌칙
                penalty = betCreateGroupPenalty.getText().toString();
                //시작일자
                startDay = betCreateFromDate.getText().toString();
                //끝일자
                endDay = betCreateToDate.getText().toString();
                //공유 카테고리
                category = betCreateSharedSpinner.getSelectedItem().toString();

                //시작 일자 년, 원, 일로 받아오기
                String[] startSeperateDate = startDay.split("/");
                int sYear = 0;
                int sMonth = 0;
                int sDay = 0;
                try{
                    sYear = Integer.parseInt(startSeperateDate[0]);
                    sMonth = Integer.parseInt(startSeperateDate[1]);
                    sDay = Integer.parseInt(startSeperateDate[2]);
                } catch (NumberFormatException e){

                } catch (Exception e){

                }
                String startDay2 = sYear+"-"+sMonth+"-"+sDay;

                String[] endSeperateDate = endDay.split("/");
                int eYear = 0;
                int eMonth = 0;
                int eDay = 0;
                try{
                    eYear = Integer.parseInt(endSeperateDate[0]);
                    eMonth = Integer.parseInt(endSeperateDate[1]);
                    eDay = Integer.parseInt(endSeperateDate[2]);
                } catch (NumberFormatException e){

                } catch (Exception e){

                }
                String endDay2 = eYear+"-"+eMonth+"-"+eDay;

                if (sYear>eYear) {
                    CheckBlank2 = false;
                }
                else{
                    if (sMonth>eMonth){
                        CheckBlank2 = false;
                    }
                    else{
                        if(sDay>eDay){
                            CheckBlank2 = false;
                        }
                        else{
                            CheckBlank2 = true;
                        }
                    }
                }

                if (groupName.equals("") || goal.equals("") ||
                        goalMoney1.equals("") ||
                        reward.equals("") || penalty.equals("") ||
                        startDay.equals("") || endDay.equals("") ||
                        category.equals("카테고리")){
                    CheckBlank = false;

                    Dialog dialog = new Dialog(GroupAddActivity.this);
                    dialog.setContentView(R.layout.dialog_common);
                    TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                    TextView dialogMessage = (TextView)dialog.findViewById(R.id.dialog_message);
                    Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);

                    dialogTitle.setText("빈칸이 있습니다.");
                    dialogMessage.setText("빈칸을 다 채워주세요.");
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else if (CheckBlank2 == false){
                    Dialog dialog = new Dialog(GroupAddActivity.this);
                    dialog.setContentView(R.layout.dialog_common);
                    TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                    TextView dialogMessage = (TextView)dialog.findViewById(R.id.dialog_message);
                    Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);

                    dialogTitle.setText("날짜를 확인해주세요.");
                    dialogMessage.setText("종료 날짜가 시작 날짜보다 빠릅니다.");
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else if (validate == false){
                    Dialog dialog = new Dialog(GroupAddActivity.this);
                    dialog.setContentView(R.layout.dialog_common);
                    TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                    TextView dialogMessage = (TextView)dialog.findViewById(R.id.dialog_message);
                    Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);

                    dialogTitle.setText("초대코드가 없습니다.");
                    dialogMessage.setText("초대코드를 생성해주세요.");
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else {
                    CheckBlank = true;

                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                System.out.println("들어왔니>>>>"+random);
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
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
                            parameters.put("groupname", groupName);
                            parameters.put("goal", goal);
                            parameters.put("goalprice", goalMoney1);
                            parameters.put("reward", reward);
                            parameters.put("penalty", penalty);
                            parameters.put("startDay", startDay2);
                            parameters.put("endDay", endDay2);  //String.valueOf(year)
                            parameters.put("inviteCode",random);
                            parameters.put("category",category);   //String.valueOf(day)
                            parameters.put("id", MainActivity.ID);

                            return parameters;
                        }

                    };
                    request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(

                            20000 ,

                            com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                            com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    request.setShouldCache(false);
                    Volley.newRequestQueue(GroupAddActivity.this).add(request);

                }

                if (CheckBlank == true && CheckBlank2 == true && validate == true){
                    GroupAddActivity GA = (GroupAddActivity) GroupAddActivity.groupAddActivity;
                    GA.finish();
                }
            }
        });

    }

    public void showDatePicker1(View view) {
        a = "시작 일자";
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme,  this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void showDatePicker2(View view) {
        a = "종료 일자";
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) { // 선택 후 뜨는거
        if(a.equals("시작 일자")){
            fromDate = year + "/" + (month + 1) + "/" + day;
            betCreateFromDate.setText(fromDate);
        }
        else if(a.equals("종료 일자")){
            toDate = year + "/" + (month + 1) + "/" + day;
            betCreateToDate.setText(toDate);
        }

    }
}