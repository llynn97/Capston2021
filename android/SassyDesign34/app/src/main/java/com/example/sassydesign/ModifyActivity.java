package com.example.sassydesign;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lib.kingja.switchbutton.SwitchMultiButton;

public class ModifyActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Button dateButton;
    ItemDetailAdapter adapter;
    public static Activity modifyActivity;
    String newDate = "";
    ArrayList<Item> items = new ArrayList<Item>();
    String title;
    String title11;
    String selectedCategory;
    String cashCard;
    String inOrOut;
    String productQuantity;
    String initDate="";
    RecyclerView dailyReceiptList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hand_add);

        modifyActivity = ModifyActivity.this;

        //인텐트
        Intent intent = getIntent();

        items = (ArrayList) intent.getSerializableExtra("items");
        int position = intent.getIntExtra("position", 0);
        System.out.println("ModifyActivity - position: " + position);
        System.out.println("items :  " + items.get(position).getTitle());

        //리싸이클러뷰 설정
        RecyclerView recyclerView = findViewById(R.id.handAddRecyclerView);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ItemDetailAdapter();

        //수입/지출 switch버튼
        SwitchMultiButton switchInOutButton = findViewById(R.id.switchInOutButton);
        //카드/현금/기타 switch버튼
        SwitchMultiButton switchCCButton = findViewById(R.id.switchCCButton);
        //날짜 버튼
        //누르면 다시 날짜 선택할 수 있게 함
        dateButton = findViewById(R.id.dateButton);

        //제목
        EditText itemDetailTitle = findViewById(R.id.itemDetailTitle);
        //title11 = itemDetailTitle.getText().toString();
        //항목 이름
        EditText productName = findViewById(R.id.productName);
        //항목 가격
        EditText productCost = findViewById(R.id.productCost);
        //항목 수량
        EditText productQuantity = findViewById(R.id.productQuantity);
        //항목 카테고리
        Spinner categorySpinner = findViewById(R.id.categorySpinner);


        //항목 추가버튼
        Button itemDetailAddButton = findViewById(R.id.itemDetailAddButton);
        Button completeButton = findViewById(R.id.completeButton);
        //날짜 확인 - 해결
        newDate = items.get(position).getDate();
        dateButton.setText(newDate);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });

        //수입/지출 확인
        if(items.get(position).getInOrOut().equals("수입")){
            switchInOutButton.setSelectedTab(0);
        }
        else if(items.get(position).getInOrOut().equals("지출")){
            switchInOutButton.setSelectedTab(1);
        }

        //카드/현금/기타 확인 -해결
        if(items.get(position).getCacheOrCard().equals("카드")){
            switchCCButton.setSelectedTab(0);
        }
        else if(items.get(position).getCacheOrCard().equals("현금")){
            switchCCButton.setSelectedTab(1);
        }
        else if(items.get(position).getCacheOrCard().equals("기타")){
            switchCCButton.setSelectedTab(2);
        }

        //제목 확인 -해결
        itemDetailTitle.setText(items.get(position).getTitle());
        title = itemDetailTitle.getText().toString();
        //objid.clear();

        //어댑터 부분
        for(int i=0; i<items.get(position).getItemList().size(); i++) {
            String name = items.get(position).getItemList().get(i);
            String cost = items.get(position).getPriceList().get(i);
            String quantity = items.get(position).getQuantityList().get(i);
            String selectedCategory = items.get(position).getCategoryList().get(i);
            String objectid = items.get(position).getObjectidList().get(i);
            //objid.add(objectid);

            ItemDetail itemDetail = null;;

            if(selectedCategory.equals("식비")){
                itemDetail = new ItemDetail(name, cost, quantity,selectedCategory,objectid);
                itemDetail.setPosition(1);
            }
            else if(selectedCategory.equals("문화생활")){
                itemDetail = new ItemDetail(name, cost, quantity,selectedCategory,objectid);
                itemDetail.setPosition(2);
            }
            else if(selectedCategory.equals("패션/미용")){
                itemDetail = new ItemDetail(name, cost, quantity,selectedCategory,objectid);
                itemDetail.setPosition(3);
            }
            else if(selectedCategory.equals("수입")){
                itemDetail = new ItemDetail(name, cost, quantity,selectedCategory,objectid);
                itemDetail.setPosition(4);
            }
            else if(selectedCategory.equals("교육")){
                itemDetail = new ItemDetail(name, cost, quantity,selectedCategory,objectid);
                itemDetail.setPosition(5);
            }
            else if(selectedCategory.equals("교통/차량")){
                itemDetail = new ItemDetail(name, cost, quantity,selectedCategory,objectid);
                itemDetail.setPosition(6);
            }
            else if(selectedCategory.equals("마트/편의점")){
                itemDetail = new ItemDetail(name, cost, quantity,selectedCategory,objectid);
                itemDetail.setPosition(7);
            }
            else if(selectedCategory.equals("건강")){
                itemDetail = new ItemDetail(name, cost, quantity,selectedCategory,objectid);
                itemDetail.setPosition(8);
            }
            else if(selectedCategory.equals("기타")){
                itemDetail = new ItemDetail(name, cost, quantity,selectedCategory,objectid);
                itemDetail.setPosition(9);
            }
            adapter.addItem(itemDetail);
        }
        recyclerView.setAdapter(adapter);

        itemDetailAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDetail itemDetail = null;
                itemDetail = new ItemDetail("", "", "1", null,"1");

                adapter.addItem(itemDetail);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }
        });


        //완료 버튼 눌렀을때 수정 서버 코드
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean CheckBlank = false;

                String url = "";
                title = itemDetailTitle.getText().toString();

                if (switchInOutButton.getSelectedTab() == 0) {
                    inOrOut = "수입";
                } else if (switchInOutButton.getSelectedTab() == 1) {
                    inOrOut = "지출";
                }

                if (switchCCButton.getSelectedTab() == 0) {
                    cashCard = "카드";
                } else if (switchCCButton.getSelectedTab() == 1) {
                    cashCard = "현금";
                } else if (switchCCButton.getSelectedTab() == 2) {
                    cashCard = "기타";
                }

                //날짜 선택하는 버튼 누르면 선택된 날짜 selectedDate에 받아오기
                if (!((dateButton.getText().toString()).equals("날짜"))) {
                    Date selectedDate = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M/dd");
                    String dateMsg = dateButton.getText().toString();
                    try {
                        selectedDate = simpleDateFormat.parse(dateMsg);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                //년, 월, 일 처리 부분
                String[] seperateDate = dateButton.getText().toString().split("/");
                //여기서 년, 월, 일 나눠줌.
                int year = Integer.parseInt(seperateDate[0]);
                int month = Integer.parseInt(seperateDate[1]);
                int day = Integer.parseInt(seperateDate[2]);
                String wholeDay = year+"-"+month+"-"+day;

                for (int i = 0; i < adapter.getItemCount(); i++) {
                    //상세 품목 객체 하나를 tmp에 받아옴
                    ItemDetail tmp = adapter.getItem(i);

                    //tmp의 get함수로 받아서 데베에 넣기

                    //초기화해줌
                    String objectid = tmp.getObjectId();
                    String productName = tmp.getProductName();
                    String productCost = tmp.getProductCost();
                    Spinner category = tmp.getCategory();
                    String productQuantity = tmp.getProductQuantity();
                    String selectedCategory = tmp.getProductCategory();

                    //빈칸이 있으면 얼럿창 띄워서 완료 못 하게 하기
                    if (title.equals("") || productName.equals("") || productCost.equals("")) {
                        CheckBlank = false;

                        Dialog dialog = new Dialog(ModifyActivity.this);
                        dialog.setContentView(R.layout.dialog_common);
                        TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                        TextView dialogMessage = (TextView)dialog.findViewById(R.id.dialog_message);
                        dialogTitle.setText("빈칸이 있습니다");
                        dialogMessage.setText("빈칸을 다 채워주세요");
                        Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);
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


                        // 영수증
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

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
                                parameters.put("_id", objectid);
                                parameters.put("title", title);
                                parameters.put("itemname", productName);
                                parameters.put("price", productCost);
                                parameters.put("category", selectedCategory);
                                parameters.put("paymethod", cashCard);
                                parameters.put("profit", inOrOut);
                                parameters.put("amount", productQuantity);
                                parameters.put("year", String.valueOf(year));  //String.valueOf(year)
                                parameters.put("month", String.valueOf(month));
                                parameters.put("date", String.valueOf(day));   //String.valueOf(day)
                                parameters.put("id", MainActivity.ID);
                                parameters.put("wholeDay", wholeDay);

                                return parameters;
                            }

                        };
                        request.setRetryPolicy(new DefaultRetryPolicy(
                                20000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        request.setShouldCache(false);
                        Volley.newRequestQueue(getApplicationContext()).add(request);
                    }
                }
                if (CheckBlank == true)
                {

                    ModifyActivity MA = (ModifyActivity) ModifyActivity.modifyActivity;
                    MA.finish();
                }

            }
        });


    }

    public void showDatePicker(View view) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
        newDate = year + "/" + (month + 1) + "/" + day;
        dateButton.setText(newDate);
        //adapter.removeItem();

    }
}