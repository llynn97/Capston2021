package com.example.sassydesign;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lib.kingja.switchbutton.SwitchMultiButton;


public class YearlyScreen extends Fragment {

    BarChart chart2;
    int replay = 0;
    YearlyScreen yearlyScreen;

    String url="";

    ArrayList<String> startyear=new ArrayList<String>();
    ArrayList<String> startmonth=new ArrayList<String>();

    Spinner yearChangeSpinner;
    String yearCategory;

    public void setYearCategory(String category){
        yearCategory = category;
    }
    public String getYearCategory(){
        return yearCategory;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.yearly_screen, container, false);
        yearChangeSpinner = rootView.findViewById(R.id.yearChangeSpinner);
        chart2 =(BarChart)rootView.findViewById(R.id.tab1_chart_2);
        SwitchMultiButton mSwitchMultiButton = (SwitchMultiButton) rootView.findViewById(R.id.switchYearlyInOutButton);

        yearlyScreen = this;
        startyear.clear();

        yearCategory = "2021";
        yearChangeSpinner.setSelection(3); //2021로 설정

        //해당하는 연도 설정
        startyear.add("2018");
        startyear.add("2019");
        startyear.add("2020");
        startyear.add("2021");
        startyear.add("2022");
        startyear.add("2023");

        // 해당하는 월 설정
        startmonth.add("1");
        startmonth.add("2");
        startmonth.add("3");
        startmonth.add("4");
        startmonth.add("5");
        startmonth.add("6");
        startmonth.add("7");
        startmonth.add("8");
        startmonth.add("9");
        startmonth.add("10");
        startmonth.add("11");
        startmonth.add("12");

        yearChangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chart2.clearChart();
                yearCategory = (String)yearChangeSpinner.getSelectedItem();
                setYearCategory((String)yearChangeSpinner.getSelectedItem());
                mSwitchMultiButton.setSelectedTab(1); //[수입, 지출 Tab] 맨 처음에 지출버튼이 눌려있는 걸 기본으로 설정해줌.

                mSwitchMultiButton.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
                    @Override
                    public void onSwitch(int position, String tabText) {
                        if(position==0){
                            chart2.clearChart();
                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonResponse = new JSONArray(response);

                                        Map<String, String> map1 = new HashMap<>();
                                        map1.clear();

                                        for(int i=0; i<jsonResponse.length(); i++){
                                            JSONObject object =jsonResponse.getJSONObject(i);
                                            String month=object.getString("month");
                                            String money=object.getString("money");
                                            if(!month.equals("---")){
                                                map1.put(month,money);
                                            }
                                        }
                                        ArrayList<Float> YearlyIncomeList=new ArrayList<Float>();
                                        for(int i=0; i<startmonth.size(); i++){
                                            if(map1.containsKey(startmonth.get(i))){
                                                YearlyIncomeList.add(Float.valueOf(map1.get(startmonth.get(i))));
                                            }
                                            else{
                                                YearlyIncomeList.add((float) 0);
                                            }
                                        }
                                        chart2.addBar(new BarModel("1월", YearlyIncomeList.get(0), 0xFF547FFF));
                                        chart2.addBar(new BarModel("2월", YearlyIncomeList.get(1), 0xFF547FFF));
                                        chart2.addBar(new BarModel("3월", YearlyIncomeList.get(2), 0xFF547FFF));
                                        chart2.addBar(new BarModel("4월", YearlyIncomeList.get(3), 0xFF547FFF));
                                        chart2.addBar(new BarModel("5월", YearlyIncomeList.get(4), 0xFF547FFF));
                                        chart2.addBar(new BarModel("6월", YearlyIncomeList.get(5), 0xFF547FFF));
                                        chart2.addBar(new BarModel("7월", YearlyIncomeList.get(6), 0xFF547FFF));
                                        chart2.addBar(new BarModel("8월", YearlyIncomeList.get(7), 0xFF547FFF));
                                        chart2.addBar(new BarModel("9월", YearlyIncomeList.get(8), 0xFF547FFF));
                                        chart2.addBar(new BarModel("10월", YearlyIncomeList.get(9), 0xFF547FFF));
                                        chart2.addBar(new BarModel("11월", YearlyIncomeList.get(10), 0xFF547FFF));
                                        chart2.addBar(new BarModel("12월", YearlyIncomeList.get(11), 0xFF547FFF));

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
                                    for(int i=0; i<startmonth.size(); i++){
                                        parameters.put("syearArray["+i+"]",startmonth.get(i));
                                    }
                                    parameters.put("id", MainActivity.ID);
                                    parameters.put("num",String.valueOf(startmonth.size()));
                                    parameters.put("profit","수입");
                                    parameters.put("year",yearCategory);
                                    return parameters;
                                }
                            };
                            request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                                    20000 ,
                                    com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            request.setShouldCache(false);
                            Volley.newRequestQueue(getContext()).add(request);

                            chart2.startAnimation();
                        }
                        else if(position == 1){
                            chart2.clearChart();

                            StringRequest request2 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonResponse2 = new JSONArray(response);
                                        Map<String, String> map1 = new HashMap<>();
                                        map1.clear();
                                        for(int i=0; i<jsonResponse2.length(); i++){
                                            JSONObject object =jsonResponse2.getJSONObject(i);
                                            String month=object.getString("month");
                                            String money=object.getString("money");
                                            if(!month.equals("---")){
                                                map1.put(month,money);
                                            }
                                        }
                                        ArrayList<Float> YearlyOutComeList=new ArrayList<Float>();
                                        for(int i=0; i<startmonth.size(); i++){
                                            if(map1.containsKey(startmonth.get(i))){
                                                YearlyOutComeList.add(Float.valueOf(map1.get(startmonth.get(i))));
                                            }
                                            else{
                                                YearlyOutComeList.add((float) 0);
                                            }
                                        }

                                        chart2.addBar(new BarModel("1월", YearlyOutComeList.get(0), 0xFFFA5050));
                                        chart2.addBar(new BarModel("2월", YearlyOutComeList.get(1), 0xFFFA5050));
                                        chart2.addBar(new BarModel("3월", YearlyOutComeList.get(2), 0xFFFA5050));
                                        chart2.addBar(new BarModel("4월", YearlyOutComeList.get(3), 0xFFFA5050));
                                        chart2.addBar(new BarModel("5월", YearlyOutComeList.get(4), 0xFFFA5050));
                                        chart2.addBar(new BarModel("6월", YearlyOutComeList.get(5), 0xFFFA5050));
                                        chart2.addBar(new BarModel("7월", YearlyOutComeList.get(6), 0xFFFA5050));
                                        chart2.addBar(new BarModel("8월", YearlyOutComeList.get(7), 0xFFFA5050));
                                        chart2.addBar(new BarModel("9월", YearlyOutComeList.get(8), 0xFFFA5050));
                                        chart2.addBar(new BarModel("10월", YearlyOutComeList.get(9), 0xFFFA5050));
                                        chart2.addBar(new BarModel("11월", YearlyOutComeList.get(10), 0xFFFA5050));
                                        chart2.addBar(new BarModel("12월", YearlyOutComeList.get(11), 0xFFFA5050));

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
                                    for(int i=0; i<startmonth.size(); i++){
                                        parameters.put("syearArray["+i+"]",startmonth.get(i));
                                    }
                                    parameters.put("id",MainActivity.ID);
                                    parameters.put("num",String.valueOf(startmonth.size()));
                                    parameters.put("profit","지출");
                                    parameters.put("year",yearCategory);

                                    return parameters;
                                }
                            };
                            request2.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                                    20000 ,
                                    com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            request2.setShouldCache(false);
                            Volley.newRequestQueue(getContext()).add(request2);

                            chart2.startAnimation();

                        }
                    }


                });

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                chart2.clearChart();
                yearCategory = (String)yearChangeSpinner.getSelectedItem();
                setYearCategory((String)yearChangeSpinner.getSelectedItem());
                mSwitchMultiButton.setSelectedTab(1); //[수입, 지출 Tab] 맨 처음에 지출버튼이 눌려있는 걸 기본으로 설정해줌.

                mSwitchMultiButton.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
                    @Override
                    public void onSwitch(int position, String tabText) {
                        if(position==0){
                            chart2.clearChart();
                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonResponse = new JSONArray(response);

                                        Map<String, String> map1 = new HashMap<>();
                                        map1.clear();

                                        for(int i=0; i<jsonResponse.length(); i++){
                                            JSONObject object =jsonResponse.getJSONObject(i);
                                            String month=object.getString("month");
                                            String money=object.getString("money");
                                            if(!month.equals("---")){
                                                map1.put(month,money);
                                            }
                                        }
                                        ArrayList<Float> YearlyIncomeList=new ArrayList<Float>();
                                        for(int i=0; i<startmonth.size(); i++){
                                            if(map1.containsKey(startmonth.get(i))){
                                                YearlyIncomeList.add(Float.valueOf(map1.get(startmonth.get(i))));
                                            }
                                            else{
                                                YearlyIncomeList.add((float) 0);
                                            }
                                        }
                                        //#547FFF
                                        chart2.addBar(new BarModel("1월", YearlyIncomeList.get(0), 0xFF547FFF));
                                        chart2.addBar(new BarModel("2월", YearlyIncomeList.get(1), 0xFF547FFF));
                                        chart2.addBar(new BarModel("3월", YearlyIncomeList.get(2), 0xFF547FFF));
                                        chart2.addBar(new BarModel("4월", YearlyIncomeList.get(3), 0xFF547FFF));
                                        chart2.addBar(new BarModel("5월", YearlyIncomeList.get(4), 0xFF547FFF));
                                        chart2.addBar(new BarModel("6월", YearlyIncomeList.get(5), 0xFF547FFF));
                                        chart2.addBar(new BarModel("7월", YearlyIncomeList.get(6), 0xFF547FFF));
                                        chart2.addBar(new BarModel("8월", YearlyIncomeList.get(7), 0xFF547FFF));
                                        chart2.addBar(new BarModel("9월", YearlyIncomeList.get(8), 0xFF547FFF));
                                        chart2.addBar(new BarModel("10월", YearlyIncomeList.get(9), 0xFF547FFF));
                                        chart2.addBar(new BarModel("11월", YearlyIncomeList.get(10), 0xFF547FFF));
                                        chart2.addBar(new BarModel("12월", YearlyIncomeList.get(11), 0xFF547FFF));

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
                                    for(int i=0; i<startmonth.size(); i++){
                                        parameters.put("syearArray["+i+"]",startmonth.get(i));
                                    }
                                    parameters.put("id", MainActivity.ID);
                                    parameters.put("num",String.valueOf(startmonth.size()));
                                    parameters.put("profit","수입");
                                    parameters.put("year",yearCategory);
                                    return parameters;
                                }
                            };
                            request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                                    20000 ,
                                    com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            request.setShouldCache(false);
                            Volley.newRequestQueue(getContext()).add(request);

                            chart2.startAnimation();
                        }
                        else if(position == 1){
                            chart2.clearChart();

                            StringRequest request2 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonResponse2 = new JSONArray(response);
                                        Map<String, String> map1 = new HashMap<>();
                                        map1.clear();
                                        for(int i=0; i<jsonResponse2.length(); i++){
                                            JSONObject object =jsonResponse2.getJSONObject(i);
                                            String month=object.getString("month");
                                            String money=object.getString("money");
                                            if(!month.equals("---")){
                                                map1.put(month,money);
                                            }
                                        }
                                        ArrayList<Float> YearlyOutComeList=new ArrayList<Float>();
                                        for(int i=0; i<startmonth.size(); i++){
                                            if(map1.containsKey(startmonth.get(i))){
                                                YearlyOutComeList.add(Float.valueOf(map1.get(startmonth.get(i))));
                                            }
                                            else{
                                                YearlyOutComeList.add((float) 0);
                                            }
                                        }

                                        chart2.addBar(new BarModel("1월", YearlyOutComeList.get(0), 0xFFFA5050));
                                        chart2.addBar(new BarModel("2월", YearlyOutComeList.get(1), 0xFFFA5050));
                                        chart2.addBar(new BarModel("3월", YearlyOutComeList.get(2), 0xFFFA5050));
                                        chart2.addBar(new BarModel("4월", YearlyOutComeList.get(3), 0xFFFA5050));
                                        chart2.addBar(new BarModel("5월", YearlyOutComeList.get(4), 0xFFFA5050));
                                        chart2.addBar(new BarModel("6월", YearlyOutComeList.get(5), 0xFFFA5050));
                                        chart2.addBar(new BarModel("7월", YearlyOutComeList.get(6), 0xFFFA5050));
                                        chart2.addBar(new BarModel("8월", YearlyOutComeList.get(7), 0xFFFA5050));
                                        chart2.addBar(new BarModel("9월", YearlyOutComeList.get(8), 0xFFFA5050));
                                        chart2.addBar(new BarModel("10월", YearlyOutComeList.get(9), 0xFFFA5050));
                                        chart2.addBar(new BarModel("11월", YearlyOutComeList.get(10), 0xFFFA5050));
                                        chart2.addBar(new BarModel("12월", YearlyOutComeList.get(11), 0xFFFA5050));

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
                                    for(int i=0; i<startmonth.size(); i++){
                                        parameters.put("syearArray["+i+"]",startmonth.get(i));
                                    }
                                    parameters.put("id",MainActivity.ID);
                                    parameters.put("num",String.valueOf(startmonth.size()));
                                    parameters.put("profit","지출");
                                    parameters.put("year",yearCategory);

                                    return parameters;
                                }
                            };
                            request2.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                                    20000 ,
                                    com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            request2.setShouldCache(false);
                            Volley.newRequestQueue(getContext()).add(request2);

                            chart2.startAnimation();

                        }
                    }


                });
            }

        });

        mSwitchMultiButton.setSelectedTab(1); //[수입, 지출 Tab] 맨 처음에 지출버튼이 눌려있는 걸 기본으로 설정해줌.

        mSwitchMultiButton.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                if(position==0){
                    chart2.clearChart();
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonResponse = new JSONArray(response);
                                Map<String, String> map1 = new HashMap<>();
                                map1.clear();

                                for(int i=0; i<jsonResponse.length(); i++){
                                    JSONObject object =jsonResponse.getJSONObject(i);
                                    String month=object.getString("month");
                                    String money=object.getString("money");
                                    if(!month.equals("---")){
                                        map1.put(month,money);
                                    }
                                }
                                ArrayList<Float> YearlyIncomeList=new ArrayList<Float>();
                                for(int i=0; i<startmonth.size(); i++){
                                    if(map1.containsKey(startmonth.get(i))){
                                        YearlyIncomeList.add(Float.valueOf(map1.get(startmonth.get(i))));
                                    }
                                    else{
                                        YearlyIncomeList.add((float) 0);
                                    }
                                }
                                //#547FFF
                                chart2.addBar(new BarModel("1월", YearlyIncomeList.get(0), 0xFF547FFF));
                                chart2.addBar(new BarModel("2월", YearlyIncomeList.get(1), 0xFF547FFF));
                                chart2.addBar(new BarModel("3월", YearlyIncomeList.get(2), 0xFF547FFF));
                                chart2.addBar(new BarModel("4월", YearlyIncomeList.get(3), 0xFF547FFF));
                                chart2.addBar(new BarModel("5월", YearlyIncomeList.get(4), 0xFF547FFF));
                                chart2.addBar(new BarModel("6월", YearlyIncomeList.get(5), 0xFF547FFF));
                                chart2.addBar(new BarModel("7월", YearlyIncomeList.get(6), 0xFF547FFF));
                                chart2.addBar(new BarModel("8월", YearlyIncomeList.get(7), 0xFF547FFF));
                                chart2.addBar(new BarModel("9월", YearlyIncomeList.get(8), 0xFF547FFF));
                                chart2.addBar(new BarModel("10월", YearlyIncomeList.get(9), 0xFF547FFF));
                                chart2.addBar(new BarModel("11월", YearlyIncomeList.get(10), 0xFF547FFF));
                                chart2.addBar(new BarModel("12월", YearlyIncomeList.get(11), 0xFF547FFF));

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
                            for(int i=0; i<startmonth.size(); i++){
                                //-07-24
                                parameters.put("syearArray["+i+"]",startmonth.get(i));
                                //parameters.put("eyearArray["+i+"]","2022");
                                // parameters.put("num",String.valueOf(idexample.size()));
                            }

                            parameters.put("id", MainActivity.ID);
                            parameters.put("num",String.valueOf(startmonth.size()));
                            parameters.put("profit","수입");
                            parameters.put("year",yearCategory);

                            return parameters;
                        }

                    };
                    request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                            20000 ,
                            com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    request.setShouldCache(false);
                    Volley.newRequestQueue(getContext()).add(request);

                    chart2.startAnimation();
                }
                else if(position == 1){
                    chart2.clearChart();

                    StringRequest request2 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonResponse2 = new JSONArray(response);
                                Map<String, String> map1 = new HashMap<>();
                                map1.clear();
                                for(int i=0; i<jsonResponse2.length(); i++){
                                    JSONObject object =jsonResponse2.getJSONObject(i);
                                    String month=object.getString("month");
                                    String money=object.getString("money");
                                    if(!month.equals("---")){
                                        map1.put(month,money);
                                    }
                                }
                                ArrayList<Float> YearlyOutComeList=new ArrayList<Float>();
                                for(int i=0; i<startmonth.size(); i++){
                                    if(map1.containsKey(startmonth.get(i))){
                                        YearlyOutComeList.add(Float.valueOf(map1.get(startmonth.get(i))));
                                    }
                                    else{
                                        YearlyOutComeList.add((float) 0);
                                    }
                                }
                                chart2.addBar(new BarModel("1월", YearlyOutComeList.get(0), 0xFFFA5050));
                                chart2.addBar(new BarModel("2월", YearlyOutComeList.get(1), 0xFFFA5050));
                                chart2.addBar(new BarModel("3월", YearlyOutComeList.get(2), 0xFFFA5050));
                                chart2.addBar(new BarModel("4월", YearlyOutComeList.get(3), 0xFFFA5050));
                                chart2.addBar(new BarModel("5월", YearlyOutComeList.get(4), 0xFFFA5050));
                                chart2.addBar(new BarModel("6월", YearlyOutComeList.get(5), 0xFFFA5050));
                                chart2.addBar(new BarModel("7월", YearlyOutComeList.get(6), 0xFFFA5050));
                                chart2.addBar(new BarModel("8월", YearlyOutComeList.get(7), 0xFFFA5050));
                                chart2.addBar(new BarModel("9월", YearlyOutComeList.get(8), 0xFFFA5050));
                                chart2.addBar(new BarModel("10월", YearlyOutComeList.get(9), 0xFFFA5050));
                                chart2.addBar(new BarModel("11월", YearlyOutComeList.get(10), 0xFFFA5050));
                                chart2.addBar(new BarModel("12월", YearlyOutComeList.get(11), 0xFFFA5050));
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
                            for(int i=0; i<startmonth.size(); i++){
                                parameters.put("syearArray["+i+"]",startmonth.get(i));
                            }

                            parameters.put("id",MainActivity.ID);
                            parameters.put("num",String.valueOf(startmonth.size()));
                            parameters.put("profit","지출");
                            parameters.put("year",yearCategory);

                            return parameters;
                        }
                    };
                    request2.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                            20000 ,
                            com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    request2.setShouldCache(false);
                    Volley.newRequestQueue(getContext()).add(request2);

                    chart2.startAnimation();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (replay != 0){
        }
        replay = 1;
    }
}