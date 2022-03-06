package com.example.sassydesign;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupDetailActivity extends AppCompatActivity {

    ArrayList<Group> items = new ArrayList<Group>();
    TextView betDetailGroupName;
    TextView betDetailGoal;
    TextView betDetailGoalMoney;
    TextView betDetailCategory;
    TextView betDetailTime;
    TextView betDetailReward;
    TextView betDetailPenalty;
    int betGoalMoney;
    int peopleNum;
    String randomCode;
    String category;

    Button betDetailInviteButton;
    ArrayList <String> idList=new ArrayList<String>();

    GroupUserAdapter groupUserAdapter = new GroupUserAdapter();

    String url2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bet_group_detail);

        Intent intent = getIntent();

        items = (ArrayList) intent.getSerializableExtra("items");
        int position = intent.getIntExtra("position", 0);
        System.out.println("GroupDetailActivity - position: " + position);
        //그룹명
        betDetailGroupName = findViewById(R.id.betDetailGroupName);
        betDetailGroupName.setText(items.get(position).getGroupName());
        //목표
        betDetailGoal = findViewById(R.id.betDetailGoal);
        betDetailGoal.setText(items.get(position).getGoal());
        //목표금액
        betDetailGoalMoney = findViewById(R.id.betDetailGoalMoney);
        betDetailGoalMoney.setText(items.get(position).getGoalMoney()+"");
        betGoalMoney = items.get(position).getGoalMoney();
        //목표기간 - 시작 & 끝나는 날짜
        betDetailTime = findViewById(R.id.betDetailTime);
        betDetailTime.setText(items.get(position).getStartDay() + " ~ " + items.get(position).getEndDay());
        //카테고리
        betDetailCategory = findViewById(R.id.betDetailCategory);
        betDetailCategory.setText(items.get(position).getCategory());
        //보상
        betDetailReward = findViewById(R.id.betDetailReward);
        betDetailReward.setText(items.get(position).getReward());
        //벌칙
        betDetailPenalty = findViewById(R.id.betDetailPenalty);
        betDetailPenalty.setText(items.get(position).getPenalty());

        //친구초대코드
        randomCode = items.get(position).getRandomCode();
        //카테고리
        category = items.get(position).getCategory();
        idList=items.get(position).getIdList();
        peopleNum = idList.size();


        RecyclerView betDetailUsers = findViewById(R.id.betDetailUsers);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        betDetailUsers.setLayoutManager(layoutManager);


        StringRequest request1 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    Map<String, String> map1 = new HashMap<>();
                    for(int i=0; i<jsonResponse.length()-1; i++) {
                        JSONObject object = jsonResponse.getJSONObject(i);
                        String id = object.getString("id");
                        String money = object.getString("money");

                        if (!id.equals("---")) {
                            map1.put(id, money);
                        }
                    }

                    ArrayList <String> moneyList=new ArrayList<String>();
                    ArrayList <String> nicknameList=new ArrayList<String>();
                    ArrayList <String> budgetList=new ArrayList<String>();
                    ArrayList <String> idList2=new ArrayList<String>();
                    ArrayList <String> photoList=new ArrayList<String>();
                    int count=0;
                    JSONArray jar=jsonResponse.getJSONArray(jsonResponse.length()-1);
                    while(count<jar.length()){
                        JSONArray jar2=jar.getJSONArray(count);
                        for(int i=0; i<jar2.length(); i++){
                            JSONObject object =jar2.getJSONObject(i);

                            String nickname=object.getString("name");
                            String budget=object.getString("budget");
                            String id=object.getString("id");
                            String photo = object.getString("photoname");
                            nicknameList.add(nickname);
                            budgetList.add(budget);
                            idList2.add(id);
                            photoList.add(photo);
                        }


                        count++;
                    }
                    for(int i=0; i<idList2.size(); i++){
                        if(map1.containsKey(idList2.get(i))){
                            moneyList.add(map1.get(idList2.get(i)));
                        }
                        else{
                            moneyList.add("0");
                        }
                    }


                    for(int i = 0; i < peopleNum; i++){
                        String id = idList2.get(i);
                        String budget=budgetList.get(i);
                        String detailProfile = photoList.get(i);
                        String detailUserName =nicknameList.get(i);
                        //선택한 카테고리에 해당되는 지출합 받아오기
                        int categorytotal =Integer.valueOf(moneyList.get(i));
                        //퍼센트 계산하기, 안 건들여도 됨
                        double percent = 0;
                        if (categorytotal != 0){
                            percent = ((double)categorytotal / (double)betGoalMoney) * 100.0;
                        }
                        //아이디, 프로필, 사용자 이름, 지출합, 퍼센트 순서대로 넣어줌
                        groupUserAdapter.addItem(new GroupUser(id, detailProfile, detailUserName,
                                Integer.toString(categorytotal), String.format("%.1f", percent)));
                    }
                    betDetailUsers.setAdapter(groupUserAdapter);


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
                for(int i=0; i<idList.size(); i++){
                    parameters.put("IdArray["+i+"]",idList.get(i));
                    parameters.put("num",String.valueOf(idList.size()));
                }

                parameters.put("startDay",items.get(position).getStartDay());
                parameters.put("endDay",items.get(position).getEndDay());
                parameters.put("category",category);

                return parameters;
            }

        };
        request1.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                20000 ,
                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request1.setShouldCache(false);
        Volley.newRequestQueue(GroupDetailActivity.this).add(request1);









        //친구초대코드버튼 누르면 얼럿 띄우기
        betDetailInviteButton = findViewById(R.id.betDetailInviteButton);
        betDetailInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                View dialogView = getLayoutInflater().inflate(R.layout.common_dialog, null);
//                AlertDialog.Builder builder = new AlertDialog.Builder(GroupDetailActivity.this);
//                AlertDialog inviteDialog = builder.create();
//                builder.setView(dialogView);
//
//                TextView dialogTitle = (TextView)dialogView.findViewById(R.id.dialog_title);
//                TextView dialogMessage = (TextView)dialogView.findViewById(R.id.dialog_message);
//                dialogTitle.setText("친구 초대 코드");
//                dialogMessage.setText(randomCode);
//                Button dialogButton = (Button)dialogView.findViewById(R.id.dialog_button);
//                inviteDialog.show();
//
//                dialogButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        inviteDialog.dismiss();
//                    }
//                });

                Dialog dialog = new Dialog(GroupDetailActivity.this);
                dialog.setContentView(R.layout.dialog_common);
                TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                TextView dialogMessage = (TextView)dialog.findViewById(R.id.dialog_message);
                dialogTitle.setText("친구 초대 코드");
                dialogMessage.setText(randomCode);
                Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();


//                        .setPositiveButton("확인", null)
//                        .create();
//                inviteDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                    @Override
//                    public void onShow(DialogInterface dialog) {
//                        inviteDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
//                    }
//                });

            }
        });

        //그룹참여중인 사용자 recyclerView 설정
        //  RecyclerView betDetailUsers = findViewById(R.id.betDetailUsers);
        //   GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        //   betDetailUsers.setLayoutManager(layoutManager);

        //인원수만큼 반복
        //  for(int i = 0; i < peopleNum; i++){
        //   System.out.println(i+"번째값: "+moneyList.get(i));
        //아이디, 프로필, 사용자 이름 받아오기
        //    String id = idList.get(i);
        //  Bitmap detailProfile = null;
        //  String detailUserName = "";
        //선택한 카테고리에 해당되는 지출합 받아오기
        //  int categorytotal = 0;
        //퍼센트 계산하기, 안 건들여도 됨
        //  int percent = 0;
        // if (categorytotal != 0){
        //      percent = (categorytotal / betDetilGoalMoney) * 100;
        //   }


        //아이디, 프로필, 사용자 이름, 지출합, 퍼센트 순서대로 넣어줌
        //  groupUserAdapter.addItem(new GroupUser(id, detailProfile, detailUserName,
        //         Integer.toString(categorytotal), Integer.toString(percent)));
        //  }
        // betDetailUsers.setAdapter(groupUserAdapter);
    }
}