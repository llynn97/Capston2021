package com.example.sassydesign;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lib.kingja.switchbutton.SwitchMultiButton;
public class HandAddActivity extends AppCompatActivity {

    Button dateButton;
    Date selectedDate;
    Date selectedDate2;
    Button itemDetailAddButton;
    SwitchMultiButton switchInOutButton;
    SwitchMultiButton switchCCButton;
    String inOrOut="지출";
    String cashCard ="카드";
    String title="";
    EditText itemDetailTitle;
    //  Button completeButton;
    ItemDetailAdapter adapter;
    Spinner category;

    public Spinner getCategory() {
        return this.category;
    }

    long now = System.currentTimeMillis();

    public static Activity handAddActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hand_add);

        handAddActivity = HandAddActivity.this;

        //오늘 날짜를 기본으로 설정해놓음.
        dateButton = findViewById(R.id.dateButton);
        selectedDate = new Date(now);
        selectedDate2 = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M/dd", Locale.ENGLISH);
        dateButton.setText(simpleDateFormat.format(selectedDate));

        switchInOutButton = findViewById(R.id.switchInOutButton);
        switchInOutButton.setSelectedTab(1);

        switchCCButton = findViewById(R.id.switchCCButton);
        switchCCButton.setSelectedTab(0);

        //리싸이클러뷰 선언, 초기화, 어댑터 초기화
        RecyclerView recyclerView = findViewById(R.id.handAddRecyclerView);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ItemDetailAdapter();

        category = findViewById(R.id.categorySpinner);
        //예시로 넣은 상세 품목들
        adapter.addItem(new ItemDetail("", "", "1", null,"1"));


        recyclerView.setAdapter(adapter);

        //+버튼 누르면 상세 품목 작성 칸 추가
        itemDetailAddButton = findViewById(R.id.itemDetailAddButton);
        itemDetailAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDetail itemDetail = null;
                //기본 수량을 1로 설정
                itemDetail = new ItemDetail("", "", "1", null,"1");

                adapter.addItem(itemDetail);
                recyclerView.setAdapter(adapter);
            }
        });

        //날짜버튼 누르면 날짜선택
        dateButton = findViewById(R.id.dateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });
        Button completeButton=findViewById(R.id.completeButton);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean CheckBlank = false;
                String dateMsg="";
                String url = "";
                //사용자가 입력한 제목 title로 받아오기
                itemDetailTitle = findViewById(R.id.itemDetailTitle);
                title = itemDetailTitle.getText().toString();

                //사용자가 선택한 수입/지출 inOrOut으로 받아오기
                if(switchInOutButton.getSelectedTab() == 0){
                    inOrOut = "수입";
                }
                else if (switchInOutButton.getSelectedTab() == 1){
                    inOrOut = "지출";
                }

                //사용자가 선택한 현금/카드 cashCard로 받아오기
                if(switchCCButton.getSelectedTab() == 0){
                    cashCard = "카드";
                }
                else if (switchCCButton.getSelectedTab() == 1){
                    cashCard = "현금";
                }
                else if (switchCCButton.getSelectedTab() == 2) {
                    cashCard = "기타";
                }

                //날짜 선택하는 버튼 누르면 선택된 날짜 selectedDate에 받아오기
                if (!((dateButton.getText().toString()).equals("날짜"))) {
                    Date selectedDate = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M/dd");
                    dateMsg = dateButton.getText().toString();
                    try {
                        selectedDate = simpleDateFormat.parse(dateMsg);
                        selectedDate2 = simpleDateFormat.parse(dateMsg);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                //년, 월, 일 처리 부분
                String [] seperateDate = dateButton.getText().toString().split("/");
                //여기서 년, 월, 일 나눠줌.
                int  year = Integer.parseInt(seperateDate[0]);
                int month = Integer.parseInt(seperateDate[1]);
                int day = Integer.parseInt(seperateDate[2]);
                String wholeDay = year+"-"+month+"-"+day;


                for(int i=0; i< adapter.getItemCount(); i++) {
                    ItemDetail tmp = adapter.getItem(i);

                    String productName = tmp.getProductName();
                    String productCost = tmp.getProductCost();
                    Spinner category = tmp.getCategory();
                    String productQuantity = tmp.getProductQuantity();
                    String selectedCategory =  tmp.getProductCategory();

                    Toast.makeText(getApplicationContext(), "제목: " + title + " 수입/지출: " + inOrOut + " 카드/현금:" + cashCard, Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "상품명:" + productName + " 가격:" + productCost + " 수량:" + productQuantity + "카테고리:" + selectedCategory , Toast.LENGTH_LONG).show();

                    if (title.equals("") || productName.equals("") || productCost.equals("")){
                        CheckBlank = false;

                        Dialog dialog = new Dialog(HandAddActivity.this);
                        dialog.setContentView(R.layout.dialog_common);
                        TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                        TextView dialogMessage = (TextView)dialog.findViewById(R.id.dialog_message);
                        dialogTitle.setText("빈칸이 있습니다.");
                        dialogMessage.setText("빈칸을 다 채워주세요.");
                        Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                    else{
                        CheckBlank = true;

                        // 영수증
                        String finalDateMsg = dateMsg;
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
                                parameters.put("title", title);
                                parameters.put("itemname", productName);
                                parameters.put("price", productCost);
                                parameters.put("category", selectedCategory);
                                parameters.put("paymethod", cashCard);
                                parameters.put("profit", inOrOut);
                                parameters.put("amount", productQuantity);

                                parameters.put("wholeDay", wholeDay);
                                parameters.put("year", String.valueOf(year));  //String.valueOf(year)
                                parameters.put("month",String.valueOf(month));
                                parameters.put("date",String.valueOf(day));   //String.valueOf(day)
                                parameters.put("id", MainActivity.ID);

                                return parameters;
                            }

                        };
                        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                                20000 ,
                                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        request.setShouldCache(false);
                        Volley.newRequestQueue(HandAddActivity.this).add(request);
                    }
                }

                //액티비티 종료(건들면 안 됨)
                if (CheckBlank == true) {
                    HandAddActivity HA = (HandAddActivity) HandAddActivity.handAddActivity;
                    HA.finish();
                }
            }
        });
    }

    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (year_string + "/" + month_string+"/"+day_string);
        dateButton.setText(dateMessage);
    }


    public void checkSwitch(int position) {
        if (position == 0) {
            inOrOut = "수입";
        }
        else{
            inOrOut = "지출";
        }
    }

    public HandAddActivity getHandAddActivity(){
        return (HandAddActivity) handAddActivity;
    }
}