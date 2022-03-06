package com.example.sassydesign;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import lib.kingja.switchbutton.SwitchMultiButton;

public class ReceiptAddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    String Url = "";
    Button receiptDateButton;
    Date selectedDate;
    String initDate="";
    Button receiptItemDetailAddButton;
    SwitchMultiButton switchInOutButton;
    SwitchMultiButton switchCCButton;
    String inOrOut = "지출";
    String cashCard = "카드";
    String title = "";
    EditText receiptItemDetailTitle;
    ItemDetailAdapter adapter;
    long now = System.currentTimeMillis();
    public static Activity receiptAddActivity;
    String firebaseurl="";
    String getJson = "";
    ArrayList<String> ocrList = new ArrayList<String>();
    int receiptFormStart; //상품명, 품명, 제품명, 메뉴명...
    int startPosition;
    int endPosition;

    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_add);
        receiptFormStart = 0;
        startPosition = 0;
        endPosition = 0;

        receiptAddActivity = ReceiptAddActivity.this;

        Intent fromGalleryIntent = getIntent();
        Url = fromGalleryIntent.getStringExtra("Url");

        firebaseurl=fromGalleryIntent.getStringExtra("firebaseurl");

        //리싸이클러뷰 선언, 초기화, 어댑터 초기화
        RecyclerView receiptAddRecyclerView = findViewById(R.id.receiptAddRecyclerView);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        receiptAddRecyclerView.setLayoutManager(layoutManager);
        adapter = new ItemDetailAdapter();
        receiptAddRecyclerView.setAdapter(adapter);

        receiptDateButton = findViewById(R.id.receiptDateButton);
        selectedDate = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M/dd", Locale.ENGLISH);
        initDate = simpleDateFormat.format(selectedDate);
        receiptDateButton.setText(simpleDateFormat.format(selectedDate));


        //수입/지출 - 처음에 지출로 설정
        switchInOutButton = findViewById(R.id.switchInOutButton);
        switchInOutButton.setSelectedTab(1);

        //카드/현금/기타 - 처음에 카드로 설정
        switchCCButton = findViewById(R.id.switchCCButton);
        switchCCButton.setSelectedTab(0);

        //날짜버튼 누르면 날짜선택
        receiptDateButton = findViewById(R.id.receiptDateButton);
        receiptDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });

        //+버튼 누르면 상세 품목 작성 칸 추가
        receiptItemDetailAddButton = findViewById(R.id.receiptItemDetailAddButton);
        receiptItemDetailAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDetail itemDetail = null;
                //기본 수량을 1로 설정
                itemDetail = new ItemDetail("", "", "1", null, "1");

                adapter.addItem(itemDetail);
                receiptAddRecyclerView.setAdapter(adapter);
            }
        });

        switchCCButton = findViewById(R.id.switchCCButton);
        switchCCButton.setSelectedTab(0);

        //스레드 먼저 끝내고 MainThread돌리기
        NaverThread naverThread = new NaverThread();
        naverThread.start();
        try{
            naverThread.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        ocrList = jsonParsing(getJson);

        //제목 생략
        for(int i=0; i<ocrList.size(); i++) {
            if(ocrList.get(i).equals("품명") || ocrList.get(i).equals("품 명") || ocrList.get(i).equals("상품명")
                    || ocrList.get(i).contains("상품")
                    || ocrList.get(i).equals("제품명") || ocrList.get(i).equals("메뉴")
                    || ocrList.get(i).equals("메 뉴") || ocrList.get(i).equals("메뉴명") || ocrList.get(i).equals("상 품 명")){
                receiptFormStart = i;
                endPosition = ocrList.size();
                //다음이 단가, 단 가 일때
                if(ocrList.get(i + 1).equals("단가") || ocrList.get(i+1).equals("단 가")) {
                    //다음이 수량, 수 량일때
                    if (ocrList.get(i + 2).equals("수량") || ocrList.get(i + 2).equals("수 량")) {
                        if (ocrList.get(i + 3).equals("금액") || ocrList.get(i + 3).equals("금 액")) {
                            startPosition = i + 3;
                            DanFirst();
                        }
                        else if (ocrList.get(i + 3).equals("금")) {
                            if (ocrList.get(i + 4).equals("액")) {
                                startPosition = i + 4;
                                DanFirst();
                            }
                        }
                    }
                    else if(ocrList.get(i + 2).equals("수")) {
                        if(ocrList.get(i + 3).equals("량")) {
                            if (ocrList.get(i + 4).equals("금액") || ocrList.get(i + 4).equals("금 액")) {
                                startPosition = i + 4;
                                DanFirst();
                            }
                            else if (ocrList.get(i + 4).equals("금")) {
                                if (ocrList.get(i + 5).equals("액")) {
                                    startPosition = i + 5;
                                    DanFirst();
                                }
                            }
                        }
                    }
                }
                //단 다음에 가 일때
                else if (ocrList.get(i + 1).equals("단")) {
                    if(ocrList.get(i+2).equals("가")) {
                        if (ocrList.get(i + 3).equals("수량") || ocrList.get(i + 3).equals("수 량")) {
                            if (ocrList.get(i + 4).equals("금액") || ocrList.get(i + 4).equals("금 액")) {
                                startPosition = i + 4;
                                DanFirst();
                            }
                            else if (ocrList.get(i + 4).equals("금")) {
                                if (ocrList.get(i + 5).equals("액")) {
                                    startPosition = i + 5;
                                    DanFirst();
                                }
                            }
                        }
                        else if(ocrList.get(i + 3).equals("수")) {
                            if(ocrList.get(i + 4).equals("량")) {
                                if (ocrList.get(i + 5).equals("금액") || ocrList.get(i + 5).equals("금 액")) {
                                    startPosition = i + 5;
                                    DanFirst();
                                }
                                else if (ocrList.get(i + 5).equals("금")) {
                                    if (ocrList.get(i + 6).equals("액")) {
                                        startPosition = i + 6;
                                        DanFirst();
                                    }
                                }
                            }
                        }
                    }
                }
                //수량이 먼저일때
                if(ocrList.get(i + 1).equals("수량") || ocrList.get(i+1).equals("수 량")) {
                    if (ocrList.get(i + 2).equals("단가") || ocrList.get(i + 2).equals("단 가")) {
                        if (ocrList.get(i + 3).equals("금액") || ocrList.get(i + 3).equals("금 액")) {
                            startPosition = i + 3;
                            SuFirst();
                        }
                        else if (ocrList.get(i + 3).equals("금")) {
                            if (ocrList.get(i + 4).equals("액")) {
                                startPosition = i + 4;
                                SuFirst();
                            }
                        }
                    }
                    else if(ocrList.get(i + 2).equals("단")) {
                        if(ocrList.get(i + 3).equals("가")) {
                            if (ocrList.get(i + 4).equals("금액") || ocrList.get(i + 4).equals("금 액")) {
                                startPosition = i + 4;
                                SuFirst();
                            }
                            else if (ocrList.get(i + 4).equals("금")) {
                                if (ocrList.get(i + 5).equals("액")) {
                                    startPosition = i + 5;
                                    SuFirst();
                                }
                            }
                        }
                    }
                    else if(ocrList.get(i+2).equals("금액") || ocrList.get(i+2).equals("금 액")){
                        startPosition = i+2;
                        ThreeForm();
                    }
                    else if(ocrList.get(i+2).equals("금")){
                        if (ocrList.get(i+3).equals("액")){
                            startPosition = i+3;
                            ThreeForm();
                        }
                    }
                }
                //수 다음에 량 일때
                else if (ocrList.get(i + 1).equals("수")) {
                    if(ocrList.get(i+2).equals("량")) {
                        if (ocrList.get(i + 3).equals("단가") || ocrList.get(i + 3).equals("단 가")) {
                            if (ocrList.get(i + 4).equals("금액") || ocrList.get(i + 4).equals("금 액")) {
                                startPosition = i + 4;
                                SuFirst();
                            }
                            else if (ocrList.get(i + 4).equals("금")) {
                                if (ocrList.get(i + 5).equals("액")) {
                                    startPosition = i + 5;
                                    SuFirst();
                                }
                            }
                        }
                        else if(ocrList.get(i + 3).equals("단")) {
                            if(ocrList.get(i + 4).equals("가")) {
                                if (ocrList.get(i + 5).equals("금액") || ocrList.get(i + 5).equals("금 액")) {
                                    startPosition = i + 5;
                                    SuFirst();
                                }
                                else if (ocrList.get(i + 5).equals("금")) {
                                    if (ocrList.get(i + 6).equals("액")) {
                                        startPosition = i + 6;
                                        SuFirst();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        receiptAddRecyclerView.setAdapter(adapter);

        Button completeButton = findViewById(R.id.receiptCompleteButton);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean CheckBlank = false;

                String url = "";
                receiptItemDetailTitle = findViewById(R.id.receiptItemDetailTitle);
                title = receiptItemDetailTitle.getText().toString();

                //사용자가 선택한 수입/지출 inOrOut으로 받아오기
                if (switchInOutButton.getSelectedTab() == 0) {
                    inOrOut = "수입";
                } else if (switchInOutButton.getSelectedTab() == 1) {
                    inOrOut = "지출";
                }

                //사용자가 선택한 현금/카드 cashCard로 받아오기
                if (switchCCButton.getSelectedTab() == 0) {
                    cashCard = "카드";
                } else if (switchCCButton.getSelectedTab() == 1) {
                    cashCard = "현금";
                } else if (switchCCButton.getSelectedTab() == 2) {
                    cashCard = "기타";
                }

                //날짜 선택하는 버튼 누르면 선택된 날짜 selectedDate에 받아오기
                if (!((receiptDateButton.getText().toString()).equals("날짜"))) {
                    Date selectedDate = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M/dd");
                    String dateMsg = receiptDateButton.getText().toString();
                    try {
                        selectedDate = simpleDateFormat.parse(dateMsg);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                //년, 월, 일 처리 부분
                String[] seperateDate = receiptDateButton.getText().toString().split("/");
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
                    String productName = tmp.getProductName();
                    String productCost = tmp.getProductCost();
                    Spinner category = tmp.getCategory();
                    String productQuantity = tmp.getProductQuantity();
                    String selectedCategory = tmp.getProductCategory();
                    //데베에 넣어졌는지 확인하는 토스트 메시지
                    //삭제해도 되는데 이거 사용해서 확인해도 됨

                    Toast.makeText(getApplicationContext(), "제목: " + title + " 수입/지출: " + inOrOut + " 카드/현금:" + cashCard, Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "상품명:" + productName + " 가격:" + productCost + " 수량:" + productQuantity + "카테고리:" + selectedCategory, Toast.LENGTH_LONG).show();

                    //빈칸이 있으면 얼럿창 띄워서 완료 못 하게 하기
                    if (title.equals("") || productName.equals("") || productCost.equals("") || selectedCategory == null) {
                        CheckBlank = false;
                    } else {
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
                                parameters.put("title", title);
                                parameters.put("itemname", productName);
                                parameters.put("price", productCost);
                                parameters.put("category", selectedCategory);
                                parameters.put("paymethod", cashCard);
                                parameters.put("profit", inOrOut);
                                parameters.put("amount", productQuantity);
                                parameters.put("wholeDay",wholeDay);
                                parameters.put("year", String.valueOf(year));  //String.valueOf(year)
                                parameters.put("month", String.valueOf(month));
                                parameters.put("date", String.valueOf(day));   //String.valueOf(day)
                                parameters.put("id", MainActivity.ID);

                                return parameters;
                            }

                        };
                        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                                20000,
                                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        request.setShouldCache(false);
                        Volley.newRequestQueue(ReceiptAddActivity.this).add(request);
                    }
                }

                if (CheckBlank == false || adapter.getItemCount() == 0){
                    Dialog dialog = new Dialog(ReceiptAddActivity.this);
                    dialog.setContentView(R.layout.dialog_common);
                    TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                    TextView dialogMessage = (TextView)dialog.findViewById(R.id.dialog_message);
                    dialogTitle.setText("빈칸이 있습니다.");
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
                if (CheckBlank == true) {
                    ReceiptAddActivity RA = (ReceiptAddActivity) ReceiptAddActivity.receiptAddActivity;
                    RA.finish();
                }
            }
        });

    }

    private String getNaverHtml (String Url){

        String requestBody = "";
        String apiURL = "";
        String secretKey = "";
        StringBuffer response = new StringBuffer();

        File NewFile1 = null;
        Bitmap bitmap = null;

        try {
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setRequestProperty("X-OCR-SECRET", secretKey);

            JSONObject json = new JSONObject();
            json.put("version", "V2");
            json.put("requestId", UUID.randomUUID().toString());
            json.put("timestamp", System.currentTimeMillis());
            JSONObject image = new JSONObject();
            image.put("format", "jpg");
            image.put("url",firebaseurl); // image should be public, otherwise, should use data
            image.put("name", "demo");
            JSONArray images = new JSONArray();
            images.put(image);
            json.put("images", images);
            String postParams = json.toString();

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
        } catch (Exception e) {
        }
        getJson = response.toString();
        return response.toString();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            String naverHtml = bun.getString("NAVER_HTML");
            getJson = naverHtml;
        }
    };

    public void showDatePicker (View view){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(receiptAddActivity, R.style.DialogTheme, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
        initDate = year + "/" + (month+1) + "/" + day;
        receiptDateButton.setText(initDate);
    }

    public void processDatePickerResult ( int year, int month, int day){
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (year_string + "/" + month_string + "/" + day_string);
        receiptDateButton.setText(dateMessage);
    }

    private ArrayList jsonParsing(String json) {
        ArrayList<String> inferTextArray = new ArrayList<String>();
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray imagesArray = jsonObject.getJSONArray("images");
            for(int i=0; i<imagesArray.length(); i++) {
                JSONObject imagesObject = imagesArray.getJSONObject(i);
                String imagesString = imagesObject.getString("fields");
                JSONArray fieldsArray = imagesObject.getJSONArray("fields");

                for(int j=0; j<fieldsArray.length(); j++) {
                    JSONObject fieldsObject = fieldsArray.getJSONObject(j);
                    String fieldsString = fieldsObject.getString("inferText");
                    inferTextArray.add(fieldsString);
                }
            }
        }catch(JSONException e) {
            e.printStackTrace();
        }
        return inferTextArray;
    }

    //특수문자 제거
    public static String StringReplace(String str){
        String match = "[*=S#,]";
        str =str.replaceAll(match, "");
        return str;
    }


    public class NaverThread extends Thread{
        public void run() {
            Intent fromGalleryIntent = getIntent();
            //선택한 이미지 절대경로 = Url
            Url = fromGalleryIntent.getStringExtra("Url");

            String naverHtml = getNaverHtml(Url);
            Bundle bun = new Bundle();
            bun.putString("NAVER_HTML", naverHtml);
            Message msg = handler.obtainMessage();
            msg.setData(bun);
            handler.sendMessage(msg);
        }
    }

    private void DanFirst() {
        for(int i=startPosition + 1; i<endPosition;) {
            String productName = ocrList.get(i);
            String productQuantity = ocrList.get(i+2);
            String productCost = ocrList.get(i+3);

            if(productName.equals("행사할인")) {
                productName = ocrList.get(i+2);
                productQuantity = ocrList.get(i+4);
                productCost = ocrList.get(i+5);
                i = i + 2;
            }

            try {
                Integer.parseInt(productName);
                i = i + 1;
                continue;
            }catch (NumberFormatException e){
                productName = StringReplace(productName);
                try{
                    String productOneCost = ocrList.get(i + 1);
                    productOneCost = StringReplace(productOneCost);
                    Integer.parseInt(productOneCost);
                    productQuantity = StringReplace(productQuantity);
                    productCost = StringReplace(productCost);
                    adapter.addItem(new ItemDetail(productName, productCost, productQuantity, null, ""));
                    i = i + 4;
                }catch (NumberFormatException ex) {
                    try {
                        String productOneCost = ocrList.get(i + 2);
                        productOneCost = StringReplace(productOneCost);
                        Integer.parseInt(productOneCost);
                        productName = ocrList.get(i) + ocrList.get(i+1);
                        productQuantity = ocrList.get(i+3);
                        productCost = ocrList.get(i+4);
                        productQuantity = StringReplace(productQuantity);
                        productCost = StringReplace(productCost);
                        adapter.addItem(new ItemDetail(productName, productCost, productQuantity, null, ""));
                        i = i + 5;
                    }catch (NumberFormatException exc){
                        try {
                            String productOneCost = ocrList.get(i + 3);
                            productOneCost = StringReplace(productOneCost);
                            Integer.parseInt(productOneCost);
                            productName = ocrList.get(i) + ocrList.get(i+1) + ocrList.get(i+2);
                            productQuantity = ocrList.get(i+4);
                            productCost = ocrList.get(i+5);
                            productQuantity = StringReplace(productQuantity);
                            productCost = StringReplace(productCost);
                            adapter.addItem(new ItemDetail(productName, productCost, productQuantity, null, ""));
                            i = i + 6;
                        }catch (NumberFormatException exception){
                            productName = ocrList.get(i) + ocrList.get(i+1) + ocrList.get(i+2) + ocrList.get(i+3);
                            productQuantity = ocrList.get(i+5);
                            productCost = ocrList.get(i+6);
                            productQuantity = StringReplace(productQuantity);
                            productCost = StringReplace(productCost);
                            adapter.addItem(new ItemDetail(productName, productCost, productQuantity, null, ""));
                            i = i + 7;
                        }
                    }
                }
            }

        }
    }

    private void SuFirst() {
        for(int i=startPosition + 1; i<endPosition;) {
            String productName = ocrList.get(i);
            String productQuantity = ocrList.get(i+1);
            String productCost = ocrList.get(i+3);

            if(productName.equals("행사할인")) {
                productName = ocrList.get(i+2);
                productQuantity = ocrList.get(i+3);
                productCost = ocrList.get(i+5);
                i = i + 2;
            }
            productName = StringReplace(productName);
            try {
                Integer.parseInt(productName);
                i = i + 1;
                continue;
            } catch (NumberFormatException e){
                try{
                    Integer.parseInt(ocrList.get(i+1));
                    productQuantity = StringReplace(productQuantity);
                    productCost = StringReplace(productCost);
                    adapter.addItem(new ItemDetail(productName, productCost, productQuantity, null, ""));
                    i = i + 4;
                }catch (NumberFormatException ex) {
                    try {
                        Integer.parseInt(ocrList.get(i+2));
                        productName = ocrList.get(i) + ocrList.get(i+1);
                        productQuantity = ocrList.get(i+2);
                        productCost = ocrList.get(i+4);
                        productQuantity = StringReplace(productQuantity);
                        productCost = StringReplace(productCost);
                        adapter.addItem(new ItemDetail(productName, productCost, productQuantity, null, ""));
                        i = i + 5;
                    }catch (NumberFormatException exc){
                        try {
                            String productOneCost = ocrList.get(i+3);
                            productOneCost = StringReplace(productOneCost);
                            Integer.parseInt(productOneCost);
                            productName = ocrList.get(i) + ocrList.get(i+1) + ocrList.get(i+2);
                            productQuantity = ocrList.get(i+3);
                            productCost = ocrList.get(i+5);
                            productQuantity = StringReplace(productQuantity);
                            productCost = StringReplace(productCost);
                            adapter.addItem(new ItemDetail(productName, productCost, productQuantity, null, ""));
                            i = i + 6;
                        }catch (NumberFormatException exception){
                            productName = ocrList.get(i) + ocrList.get(i+1) + ocrList.get(i+2) + ocrList.get(i+3);
                            productQuantity = ocrList.get(i+4);
                            productCost = ocrList.get(i+6);
                            productQuantity = StringReplace(productQuantity);
                            productCost = StringReplace(productCost);
                            adapter.addItem(new ItemDetail(productName, productCost, productQuantity, null, ""));
                            i = i + 7;
                        }
                    }
                }
            }
        }
    }

    public void ThreeForm(){
        for(int i=startPosition + 1; i<endPosition;) {
            String productName = ocrList.get(i);
            String productQuantity = ocrList.get(i+1);
            String productCost = "";

            if (productName.contains("배달")){
                productCost = ocrList.get(i+1);
            }
            else {
                productCost = ocrList.get(i+2);
            }
//            String productCost = ocrList.get(i+2);

            if(i+3<=endPosition && (ocrList.get(i + 3).contains("할인") || ocrList.get(i).contains("할인"))) {
                i = i + 1;
                continue;
            }
            try {
                Integer.parseInt(productName);
                i = i + 1;
                continue;
            }catch (NumberFormatException e){
                try{
                    productName = StringReplace(productName);
                    productQuantity = StringReplace(productQuantity);
                    productCost = StringReplace(productCost);
                    Integer.parseInt(productQuantity);
                    if (productName.contains("배달")){
                        productQuantity = "1";
                        adapter.addItem(new ItemDetail(productName, productCost, productQuantity, null, ""));
                        i = i + 2;
                    }
                    else {
                        adapter.addItem(new ItemDetail(productName, productCost, productQuantity, null, ""));
                        i = i + 3;
                    }
                }catch (NumberFormatException ex) {
                    try{
                        productName = ocrList.get(i) + ocrList.get(i+1);
                        productQuantity = ocrList.get(i+2);
                        Integer.parseInt(productQuantity);

                        if (productName.contains("배달")){
                            productQuantity = "1";
                            productCost = ocrList.get(i+2);
                            productName = StringReplace(productName);
                            productQuantity = StringReplace(productQuantity);
                            productCost = StringReplace(productCost);
                            adapter.addItem(new ItemDetail(productName, productCost, productQuantity, null, ""));
                            i = i + 3;
                        }
                        else {
                            productCost = ocrList.get(i+3);
                            productName = StringReplace(productName);
                            productQuantity = StringReplace(productQuantity);
                            productCost = StringReplace(productCost);
                            adapter.addItem(new ItemDetail(productName, productCost, productQuantity, null, ""));
                            i = i + 4;
                        }
                    }catch (NumberFormatException exc){
                        try {
                            productName = ocrList.get(i) + ocrList.get(i+1) + ocrList.get(i+2);
                            productQuantity = ocrList.get(i+3);
                            Integer.parseInt(productQuantity);

                            if (productName.contains("배달")){
                                productQuantity = "1";
                                productCost = ocrList.get(i+3);
                                productName = StringReplace(productName);
                                productQuantity = StringReplace(productQuantity);
                                productCost = StringReplace(productCost);
                                adapter.addItem(new ItemDetail(productName, productCost, productQuantity, null, ""));
                                i = i + 4;
                            }
                            else {
                                productCost = ocrList.get(i+4);
                                productName = StringReplace(productName);
                                productQuantity = StringReplace(productQuantity);
                                productCost = StringReplace(productCost);
                                adapter.addItem(new ItemDetail(productName, productCost, productQuantity, null, ""));
                                i = i + 5;
                            }
                        }catch (NumberFormatException exception){
                            productName = ocrList.get(i) + ocrList.get(i+1) + ocrList.get(i+2) + ocrList.get(i+3);
                            productQuantity = ocrList.get(i+4);

                            if (productName.contains("배달")){
                                productQuantity = "1";
                                productCost = ocrList.get(i+4);
                                productName = StringReplace(productName);
                                productQuantity = StringReplace(productQuantity);
                                productCost = StringReplace(productCost);
                                adapter.addItem(new ItemDetail(productName, productCost, productQuantity, null, ""));
                                i = i + 5;
                            }
                            else {
                                productCost = ocrList.get(i+5);
                                productName = StringReplace(productName);
                                productQuantity = StringReplace(productQuantity);
                                productCost = StringReplace(productCost);
                                adapter.addItem(new ItemDetail(productName, productCost, productQuantity, null, ""));
                                i = i + 6;
                            }
                        }
                    }
                }
            }
        }
    }
}