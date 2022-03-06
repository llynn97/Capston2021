package com.example.sassydesign;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

//최상단바 액티비티(날짜별, 카테고리별, 현금/카드별)
//main_top_bar.xml
public class MainTopBar extends Fragment {

    DailyScreen dailyScreen;
    MonthlyScreen monthlyScreen;
    YearlyScreen yearlyScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_top_bar, container, false);

        TabLayout tabs = rootView.findViewById(R.id.mainTop);
        tabs.addTab(tabs.newTab().setText("일일"));
        tabs.addTab(tabs.newTab().setText("한달"));
        tabs.addTab(tabs.newTab().setText("연간"));
        tabs.setSelectedTabIndicatorColor(Color.parseColor("#FF547FFF"));

        dailyScreen = new DailyScreen();
        monthlyScreen = new MonthlyScreen();
        yearlyScreen = new YearlyScreen();

        getFragmentManager().beginTransaction().replace(R.id.mainTopBarContainer, dailyScreen).commit();

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = null;

                if(position == 0){
                    selected = dailyScreen;
                }
                else if(position == 1){
                    selected = monthlyScreen;
                }
                else if(position == 2){
                    selected = yearlyScreen;
                }

                getFragmentManager().beginTransaction().replace(R.id.mainTopBarContainer, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return rootView;
    }

}