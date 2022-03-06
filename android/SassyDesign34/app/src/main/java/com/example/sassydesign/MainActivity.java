package com.example.sassydesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

//메인 액티비티
//activity_main.xml
public class MainActivity extends AppCompatActivity {
    public static String ID;
    public static Activity mainActivity;
    BetHomeScreen betHomeScreen;
    SettingsScreen settingsScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //로그인 화면 종료
        LoginActivity LA = (LoginActivity) LoginActivity.activity;
        //LA.finish();

        //ID, PW 잘 넘어가는지 토스트 메시지로 확인
        Intent intent = getIntent();
        ID = intent.getStringExtra("id");
        String PW = intent.getStringExtra("password");

        Toast temp = Toast.makeText(this.getApplicationContext(), "id =" + ID + "password = " + PW, Toast.LENGTH_LONG);
        temp.show();

        //최상단바 보이기
        MainTopBar mainTopBar = new MainTopBar();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainTopBar).commit();

        //내기가계부 홈, 설정 선택
        betHomeScreen = new BetHomeScreen();
        settingsScreen = new SettingsScreen();
        BottomNavigationView homeTabs = findViewById(R.id.homeTabs);
        homeTabs.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.goToBetReceipt:
                                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, betHomeScreen).commit();
                                return true;

                            case R.id.goToHome:
                                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainTopBar).commit();
                                return true;
                            case R.id.goToSettings:
                                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, settingsScreen).commit();
                                return true;

                        }
                        return false;
                    }
                }
        );
    }
}