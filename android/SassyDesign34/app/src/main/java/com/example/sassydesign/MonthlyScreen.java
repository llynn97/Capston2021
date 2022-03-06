package com.example.sassydesign;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MonthlyScreen extends Fragment{

    MaterialCalendarView materialCalendarView; //캘린더 보여주는거

    MonthlyScreen monthlyScreen; //이 화면

    TextView month_income;
    TextView month_outcome;
    TextView MonthlyBudgetText;

    TextView percent_budget;
    TextView montlyBudgetGapText;

    //사용자가 설정해놓은 예산
    int UserBudget = 0;


    public void MonthTotalOutCome(int year, int month, ViewGroup rootView){

        String url = "";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String monthmoneyminus=jsonResponse.getString("월지출");//MonthTotalOutCome()-월에 해당하는 총지출
                    String budget=jsonResponse.getString("예산");

                    int money=Integer.valueOf(monthmoneyminus);
                    UserBudget=Integer.valueOf(budget);

                    percent_budget = rootView.findViewById(R.id.percent_budget);
                    montlyBudgetGapText = rootView.findViewById(R.id.montlyBudgetGapText);
                    MonthlyBudgetText = rootView.findViewById(R.id.MonthlyBudgetText);
                    int budget_gap = UserBudget - money; //예산에서 월간 지출빼줌.
                    if(budget_gap == 0){ //
                        String strGap = Integer.toString(budget_gap);
                        percent_budget.setText(strGap);//예산과의 갭을 화면에 띄워줌
                        MonthlyBudgetText.setText("이번 달 예산이 ");
                        montlyBudgetGapText.setText("원이 남았습니다.");
                    }
                    else if(budget_gap > 0){ //
                        String strGap = Integer.toString(budget_gap);
                        MonthlyBudgetText.setText("이번 달 예산이 ");
                        percent_budget.setText(strGap);//예산과의 갭을 화면에 띄워줌
                        montlyBudgetGapText.setText("원이 남았습니다.");
                    }
                    else{ //예산을 더 많이 사용했을 때
                        budget_gap *= -1; //음수->양수로 바꿔주기
                        String strGap = Integer.toString(budget_gap);
                        percent_budget.setText(strGap);//예산과의 갭을 화면에 띄워줌
                        montlyBudgetGapText.setText("원을 더 사용했습니다.");
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
                parameters.put("year",String.valueOf(year));
                parameters.put("month",String.valueOf(month));
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.monthly_screen, container, false);
        monthlyScreen = this;
        materialCalendarView = rootView.findViewById(R.id.MonthShow);

        materialCalendarView.state().edit().setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2018,1,1))
                .setMaximumDate(CalendarDay.from(2023,12,31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.setSelectedDate(CalendarDay.today());
        materialCalendarView.addDecorators(new MonthlySundayDecorator(), new MonthlySaturdayDecorator());

        month_income = rootView.findViewById(R.id.month_income);
        month_outcome = rootView.findViewById(R.id.month_outcome);

        MonthTotalOutCome(CalendarDay.today().getYear(),CalendarDay.today().getMonth()+1, rootView);

        String url = "";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String moneyminus=jsonResponse.getString("지출");
                    String moneyplus=jsonResponse.getString("수입");

                    month_income.setText(moneyplus); //그 날에 해당하는 수입
                    month_outcome.setText(moneyminus); //그 날에 해당하는 지출
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
                parameters.put("year",String.valueOf(CalendarDay.today().getYear()));
                parameters.put("month",String.valueOf(CalendarDay.today().getMonth()+1));
                parameters.put("day",String.valueOf(CalendarDay.today().getDay()));
                return parameters;
            }
        };
        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                20000,
                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        Volley.newRequestQueue(getContext()).add(request);

        String url4 = "";

        StringRequest request2 = new StringRequest(Request.Method.POST, url4, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);

                    ArrayList<String> plusarraylist=new ArrayList<>(); //그 월에 해당하는 일일별 수입
                    ArrayList<String> minusarraylist=new ArrayList<>(); //그 월에 해당하는 일일별 지출

                    JSONArray jsonminus=jsonResponse.getJSONArray(0);
                    JSONArray jsonplus=jsonResponse.getJSONArray(1);

                    for(int i=0; i<jsonminus.length(); i++){
                        plusarraylist.add(jsonplus.getString(i));
                        minusarraylist.add(jsonminus.getString(i));
                    }
                    for(int i=0; i<jsonminus.length(); i++) {
                        if((Integer.parseInt(plusarraylist.get(i)) != 0) && (Integer.parseInt(minusarraylist.get(i)) != 0)){
                            materialCalendarView.addDecorator(new MonthlyEventDecorator(Color.YELLOW,
                                    Collections.singleton(CalendarDay.from(CalendarDay.today().getYear(), CalendarDay.today().getMonth(), i) )));
                            continue;
                        }
                        else if((Integer.parseInt(plusarraylist.get(i)) != 0)){
                            materialCalendarView.addDecorator(new MonthlyEventDecorator(Color.BLUE,
                                    Collections.singleton(CalendarDay.from(CalendarDay.today().getYear(), CalendarDay.today().getMonth(), i) )));
                            continue;
                        }
                        else if((Integer.parseInt(minusarraylist.get(i)) != 0)){
                            materialCalendarView.addDecorator(new MonthlyEventDecorator(Color.RED,
                                    Collections.singleton(CalendarDay.from(CalendarDay.today().getYear(), CalendarDay.today().getMonth(), i) )));
                            continue;
                        }
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
                parameters.put("year", String.valueOf(CalendarDay.today().getYear()));
                parameters.put("month", String.valueOf(CalendarDay.today().getMonth()+1)); //**************
                return parameters;
            }
        };
        request2.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                50000 ,
                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request2.setShouldCache(false);
        Volley.newRequestQueue(getContext()).add(request2);

        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {

            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                int changedYear = date.getYear();
                int changedMonth = date.getMonth();

                MonthTotalOutCome(changedYear, changedMonth+1, rootView);

                String url5="";
                StringRequest request3 = new StringRequest(Request.Method.POST, url5, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonResponse = new JSONArray(response);

                            ArrayList<String> plusarraylist=new ArrayList<>(); //그 월에 해당하는 일일별 수입
                            ArrayList<String> minusarraylist=new ArrayList<>(); //그 월에 해당하는 일일별 지출

                            JSONArray jsonminus=jsonResponse.getJSONArray(0);
                            JSONArray jsonplus=jsonResponse.getJSONArray(1);

                            for(int i=0; i<jsonminus.length(); i++) {
                                plusarraylist.add(jsonplus.getString(i));
                                minusarraylist.add(jsonminus.getString(i));
                            }

                            for(int i=0; i<jsonminus.length(); i++) {
                                if((Integer.parseInt(plusarraylist.get(i)) != 0) && (Integer.parseInt(minusarraylist.get(i)) != 0)){
                                    materialCalendarView.addDecorator(new MonthlyEventDecorator(Color.YELLOW,
                                            Collections.singleton(CalendarDay.from(changedYear, changedMonth, i) )));
                                    continue;
                                }
                                else if((Integer.parseInt(plusarraylist.get(i)) != 0)){
                                    materialCalendarView.addDecorator(new MonthlyEventDecorator(Color.BLUE,
                                            Collections.singleton(CalendarDay.from(changedYear, changedMonth, i) )));
                                    continue;
                                }
                                else if((Integer.parseInt(minusarraylist.get(i)) != 0)){
                                    materialCalendarView.addDecorator(new MonthlyEventDecorator(Color.RED,
                                            Collections.singleton(CalendarDay.from(changedYear, changedMonth, i) )));
                                    continue;
                                }
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
                        parameters.put("year",String.valueOf(changedYear));
                        parameters.put("month",String.valueOf(changedMonth+1));
                        return parameters;
                    }
                };
                request3.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                        20000,
                        com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                request3.setShouldCache(false);
                Volley.newRequestQueue(getContext()).add(request3);
            }
        });
        //---------------------------------------------------------------------------------

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget,
                                       @NonNull CalendarDay date, boolean selected) {
                //사용자가 선택한 날짜 받아오기
                int year = date.getYear();
                int month = date.getMonth()+1;
                int day = date.getDay();

                MonthTotalOutCome(year, month, rootView); //클릭한 날에 해당하는 월별총지출 뽑아줌.

                String inCome = null;
                String outCome = null;

                String url = "";

                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            String moneyminus=jsonResponse.getString("지출");
                            String moneyplus=jsonResponse.getString("수입");


                            month_income.setText(moneyplus); //그 날에 해당하는 수입
                            month_outcome.setText(moneyminus); //그 날에 해당하는 지출

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
                        parameters.put("year",String.valueOf(year));
                        parameters.put("month",String.valueOf(month));
                        parameters.put("day",String.valueOf(day));

                        return parameters;
                    }

                };
                request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                        50000,
                        com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                request.setShouldCache(false);
                Volley.newRequestQueue(getContext()).add(request);
            }
        });

        return rootView;
    }

    class MonthlySundayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public MonthlySundayDecorator(){

        }

        @Override
        public boolean shouldDecorate(CalendarDay day){
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view){
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }

    class MonthlySelectorDecorator implements DayViewDecorator {
        private final Drawable drawable;

        public MonthlySelectorDecorator(Fragment context){
            drawable = context.getResources().getDrawable(R.drawable.my_selector);

        }

        @Override
        public boolean shouldDecorate(CalendarDay day){
            return true;
        }

        @Override
        public void decorate(DayViewFacade view){
            view.setSelectionDrawable(drawable);
        }
    }

    class MonthlySaturdayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public MonthlySaturdayDecorator(){
        }
        @Override
        public boolean shouldDecorate(CalendarDay day){
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view){
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }

    class MonthlyEventDecorator implements DayViewDecorator {
        private final int color;
        private final HashSet<CalendarDay> dates;
        private final Calendar calendar = Calendar.getInstance();

        public MonthlyEventDecorator(int color, Collection<CalendarDay> dates){
            this.color = color;
            this.dates = new HashSet<>(dates);
        }
        @Override
        public boolean shouldDecorate(CalendarDay day){
            day.copyTo(calendar);
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view){
            view.addSpan(new DotSpan(8, color));
        }
    }

}