package com.example.sassydesign;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

//날짜별의 상단바(일일, 한달, 연간)
//main_second_bar.xml
public class MainSecondBar extends Fragment {

    DailyScreen dailyScreen;
    MonthlyScreen monthlyScreen;
    YearlyScreen yearlyScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_second_bar, container, false);

        TabLayout secondTabs = rootView.findViewById(R.id.mainSecondTop);
        secondTabs.addTab(secondTabs.newTab().setText("일일"));
        secondTabs.addTab(secondTabs.newTab().setText("한달"));
        secondTabs.addTab(secondTabs.newTab().setText("연간"));

        dailyScreen = new DailyScreen();
        monthlyScreen = new MonthlyScreen();
        yearlyScreen = new YearlyScreen();

        getFragmentManager().beginTransaction().replace(R.id.mainSecondBarContainer, dailyScreen).commit();

        secondTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

                getFragmentManager().beginTransaction().replace(R.id.mainSecondBarContainer, selected).commit();
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