package com.example.sassydesign;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BetHomeScreen extends Fragment {

    Button betGroupAddButton;
    Button betGroupInviteButton;
    RecyclerView groupList;

    GroupAdapter groupAdapter;

    int replay = 0;
    int replay2 = 0;

    String url = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.bet_group_list, container, false);

        groupAdapter = new GroupAdapter();

        betGroupAddButton = rootView.findViewById(R.id.betGroupAddButton);
        betGroupAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroupAddActivity.class);
                startActivity(intent);
            }
        });

        groupList = rootView.findViewById(R.id.betGroupListRecyclerView);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        groupList.setLayoutManager(layoutManager);

        betGroupInviteButton = rootView.findViewById(R.id.betGroupInviteButton);
        betGroupInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        if(replay2 == 0){
            setItemObject();
            replay2 = 1;
        }

        return rootView;
    }

    private void showAlertDialog(){
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_invite, null);
        final EditText inviteCode = (EditText)dialogView.findViewById(R.id.inviteCode);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        builder.setPositiveButton("입장하기", new DialogInterface.OnClickListener() {
            String value = "";
            @Override
            public void onClick(DialogInterface dialog, int which) {

                value = inviteCode.getText().toString();
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String result) {
                        try {
                            JSONObject object=new JSONObject(result);

                            if(object.isNull("groupname")){

                                Dialog dialog = new Dialog(getContext());
                                dialog.setContentView(R.layout.dialog_common);
                                TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                                TextView dialogMessage = (TextView)dialog.findViewById(R.id.dialog_message);
                                Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);

                                dialogTitle.setText("가계부가 중복되었습니다.");
                                dialogMessage.setText("이미 초대된 내기가계부입니다.");
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }
                            else if(object.getString("groupname").equals("noinvitecode")) {
                                Dialog dialog = new Dialog(getContext());
                                dialog.setContentView(R.layout.dialog_common);
                                TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                                TextView dialogMessage = (TextView)dialog.findViewById(R.id.dialog_message);
                                Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);

                                dialogTitle.setText("가계부가 존재하지 않습니다.");
                                dialogMessage.setText("입력된 초대코드에 해당하는 내기가계부가 없습니다.");
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }
                            else {

                                ArrayList<String> idList=new ArrayList<String>();
                                ArrayList<String> idid=new ArrayList<String>();
                                ArrayList<String> intentgroup=new ArrayList<String>();

                                //JSONObject object = jsonArray.getJSONObject(0);
                                String groupname=object.getString("groupname");
                                String goal = object.getString("goal");
                                JSONArray id=object.getJSONArray("id");

                                for(int i=0; i<id.length(); i++){
                                    String str=id.get(i).toString();
                                    idList.add(str);

                                }
                                idList.add(MainActivity.ID);

                                int goalMoney=object.getInt("goalprice");
                                String reward=object.getString("reward");
                                String penalty=object.getString("penalty");
                                String startDay=object.getString("startDay");
                                String endDay=object.getString("endDay");
                                String category= object.getString("category");
                                String randomcode= object.getString("inviteCode");

                                int peopleNum = idList.size();
                                String[] IDlist = idList.toArray(new String[idList.size()]);

                                intentgroup.add(groupname);
                                intentgroup.add(goal);
                                intentgroup.add(String.valueOf(peopleNum));
                                intentgroup.add(String.valueOf(goalMoney));
                                intentgroup.add(reward);
                                intentgroup.add(penalty);
                                intentgroup.add(startDay);
                                intentgroup.add(endDay);
                                intentgroup.add(category);
                                intentgroup.add(randomcode);
                                intentgroup.add(Arrays.toString(IDlist));

                                Intent intent = new Intent(getActivity(), GroupInviteActivity.class);
                                intent.putExtra("newGroup", intentgroup);
                                intent.putExtra("idList",idList);
                                startActivity(intent);
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
                        parameters.put("id",MainActivity.ID);
                        parameters.put("inviteCode", value);
                        return parameters;
                    }

                };
                request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                        20000 ,
                        com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                request.setShouldCache(false);
                Volley.newRequestQueue(getContext()).add(request);
                Toast.makeText(getContext(), value, Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#719aff"));
            }
        });
        alertDialog.show();
    }

    public void setItemObject(){

        String randomCode = "";
        String groupName = "";
        String goal = "";
        int peopleNum = 1;
        int goalMoney = 0;
        String reward = "";
        String penalty = "";
        String startDay = "";
        String endDay = "";
        String category = "";
        String url = ""+MainActivity.ID;
        int replay = 0;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {

                    JSONArray jsonArray=new JSONArray(result);
                    ArrayList<String> groupNameList = new ArrayList<String>();
                    ArrayList<String> goalList=new ArrayList<String>();
                    ArrayList<Integer> goalMoneyList=new ArrayList<Integer>();
                    ArrayList<String> rewardList=new ArrayList<String>();
                    ArrayList<String> penaltyList=new ArrayList<String>();
                    ArrayList<String> startDayList=new ArrayList<String>();
                    ArrayList<String> endDayList=new ArrayList<String>();
                    ArrayList<String> categoryList=new ArrayList<String>();
                    ArrayList<String> randomCodeList=new ArrayList<String>();
                    ArrayList<ArrayList<String>> ids = new ArrayList<ArrayList<String>>();


                    int count = 0;
                    while(count < jsonArray.length())  //title리스트에 title 담기
                    {    ArrayList<String> idList=new ArrayList<String>();
                        JSONObject object = jsonArray.getJSONObject(count);
                        String groupname=object.getString("groupname");
                        String goal = object.getString("goal");
                        JSONArray id=object.getJSONArray("id");

                        for(int i=0; i<id.length(); i++){
                            String str=id.get(i).toString();
                            idList.add(str);
                        }

                        int goalMoney=object.getInt("goalprice");
                        String reward=object.getString("reward");
                        String penalty=object.getString("penalty");
                        String startDay=object.getString("startDay");
                        String endDay=object.getString("endDay");
                        String category= object.getString("category");
                        String randomcode= object.getString("inviteCode");
                        groupNameList.add(groupname);
                        goalList.add(goal);
                        goalMoneyList.add(goalMoney);
                        rewardList.add(reward);
                        penaltyList.add(penalty);
                        startDayList.add(startDay);
                        endDayList.add(endDay);
                        categoryList.add(category);
                        randomCodeList.add(randomcode);
                        ids.add(idList);

                        count++;
                    }
                    int cursor=groupNameList.size();
                    for(int i = 0 ; i<cursor; i++){
                        String groupName1 = groupNameList.get(i);
                        String goal1 = goalList.get(i);
                        int goalMoney1 = goalMoneyList.get(i);
                        String reward1 = rewardList.get(i);
                        String penalty1 = penaltyList.get(i);
                        String startDay1 = startDayList.get(i);
                        String endDay1 = endDayList.get(i);
                        String category1 =categoryList.get(i);
                        String randomCode1 = randomCodeList.get(i);
                        ArrayList<String> idexample = ids.get(i);
                        int peopleNum = idexample.size();

                        Group newGroup = new Group(groupName1, goal1, peopleNum, goalMoney1, reward1, penalty1,
                                startDay1, endDay1, category1, randomCode1, idexample);

                        groupAdapter.addItem(newGroup);
                        groupList.setAdapter(groupAdapter);
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
        );
        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                20000 ,
                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        Volley.newRequestQueue(getContext()).add(request);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (replay != 0){
            groupAdapter.removeItem();
            setItemObject();
        }
        replay = 1;
    }
}
