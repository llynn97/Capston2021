package com.example.sassydesign;

import android.app.Activity;
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

public class GroupInviteActivity extends AppCompatActivity {
    TextView betDetailGroupName;
    TextView betDetailGoal;
    TextView betDetailGoalMoney;
    TextView betDetailCategory;
    TextView betDetailTime;
    TextView betDetailReward;
    TextView betDetailPenalty;
    Button betDetailInviteButton;
    int betGoalMoney;

    ArrayList<Group> groups = new ArrayList<Group>();
    GroupAdapter groupAdapter = new GroupAdapter();
    ArrayList <String> moneyList=new ArrayList<String>();
    ArrayList <String> nicknameList=new ArrayList<String>();
    ArrayList <String> budgetList=new ArrayList<String>();
    ArrayList <String> idList2=new ArrayList<String>();
    ArrayList <String> photonameList=new ArrayList<String>();

    public static Activity groupInviteActivity;

    Intent intent;
    Group invitedGroup;
    GroupUserAdapter groupUserAdapter = new GroupUserAdapter();

    String url2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bet_group_detail);

        groupInviteActivity = GroupInviteActivity.this;

        betDetailGroupName = findViewById(R.id.betDetailGroupName);
        betDetailGoal = findViewById(R.id.betDetailGoal);
        betDetailGoalMoney = findViewById(R.id.betDetailGoalMoney);
        betDetailCategory = findViewById(R.id.betDetailCategory);
        betDetailTime = findViewById(R.id.betDetailTime);
        betDetailReward = findViewById(R.id.betDetailReward);
        betDetailPenalty = findViewById(R.id.betDetailPenalty);
        betDetailInviteButton = findViewById(R.id.betDetailInviteButton);

        intent = getIntent();
        ArrayList<String> code = intent.getStringArrayListExtra("newGroup");
        ArrayList<String> idlist = intent.getStringArrayListExtra("idList");
        String randomCode = "";
        String groupName = "";
        String goal = "";
        //int peopleNum = 1;
        int goalMoney = 0;
        String reward = "";
        String penalty = "";
        String startDay = "";
        String endDay = "";

        int betDetilGoalMoney = Integer.parseInt(code.get(3));

        int peopleNum=Integer.parseInt(code.get(2));
        int goalmoney = Integer.parseInt(code.get(3));
        String startDay2 = code.get(6);
        String endDay2 = code.get(7);
        String category = code.get(8);

        //ArrayList<String> idlist = null;
        invitedGroup = new Group(code.get(0), code.get(1), Integer.parseInt(code.get(2)), Integer.parseInt(code.get(3)), code.get(4), code.get(5),
                code.get(6), code.get(7), code.get(8), code.get(9), idlist);

        betDetailGroupName.setText(invitedGroup.getGroupName());

        betDetailGoal.setText(invitedGroup.getGoal());
        //목표금액
        betDetailGoalMoney.setText(invitedGroup.getGoalMoney()+"");
        betGoalMoney = invitedGroup.getGoalMoney();
        //목표기간 - 시작 & 끝나는 날짜
        betDetailTime.setText(invitedGroup.getStartDay() + " ~ " + invitedGroup.getEndDay());
        //카테고리
        betDetailCategory.setText(invitedGroup.getCategory());
        //보상
        betDetailReward.setText(invitedGroup.getReward());
        //벌칙
        betDetailPenalty.setText(invitedGroup.getPenalty());

        betDetailInviteButton = findViewById(R.id.betDetailInviteButton);
        betDetailInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(GroupInviteActivity.this);
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
            }
        });

        // 사용자 설정하자ㅏ
        RecyclerView betDetailUsers = findViewById(R.id.betDetailUsers);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        betDetailUsers.setLayoutManager(layoutManager);



        StringRequest request1 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonResponse = new JSONArray(response);
                    Map<String, String> map1 = new HashMap<>();
                    for(int i=0; i<jsonResponse.length()-1; i++){
                        JSONObject object =jsonResponse.getJSONObject(i);
                        String id=object.getString("id");
                        String money=object.getString("money");
                        if(!id.equals("---")){
                            map1.put(id,money);
                        }
                    }

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
                            photonameList.add(photo);

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

                    for(int i = 0; i < idlist.size(); i++){
                        String id = idList2.get(i);
                        String budget=budgetList.get(i);
                        String detailProfile = photonameList.get(i);
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
                for(int i=0; i<idlist.size(); i++){
                    parameters.put("IdArray["+i+"]",idlist.get(i));
                    parameters.put("num",String.valueOf(idlist.size()));
                }

                parameters.put("startDay",startDay2);
                parameters.put("endDay",endDay2);
                parameters.put("category",category);

                return parameters;
            }

        };
        request1.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                20000 ,
                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request1.setShouldCache(false);
        Volley.newRequestQueue(GroupInviteActivity.this).add(request1);


    }

}
