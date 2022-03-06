package com.example.sassydesign;

import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.io.Serializable;

import java.sql.Array;
import java.util.ArrayList;

public class Group implements Serializable {
    String groupName;
    String goal;
    int peopleNum;

    int goalMoney;
    String reward;
    String penalty;
    String startDay;
    String endDay;
    String category;

    String randomCode;

    ArrayList<GroupUser> users;
    ArrayList<String> idList;
    public Group(String groupName, String goal, int peopleNum, int goalMoney, String reward, String penalty,
                 String startDay, String endDay, String category, String randomCode, ArrayList<String> idList){
        this.groupName = groupName;
        this.goal = goal;
        this.peopleNum = peopleNum;
        this.goalMoney = goalMoney;
        this.reward = reward;
        this.penalty = penalty;
        this.startDay = startDay;
        this.endDay = endDay;
        this.category = category;
        this.randomCode = randomCode;
        this.users = new ArrayList<GroupUser>();
        this.idList = idList;


    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }

    public int getGoalMoney() {
        return goalMoney;
    }

    public void setGoalMoney(int goalMoney) {
        this.goalMoney = goalMoney;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRandomCode() {
        return randomCode;
    }

    public void setRandomCode(String randomCode) {
        this.randomCode = randomCode;
    }

    public ArrayList<String> getIdArray() { return idList; }
    public ArrayList<String> getIdList() {
        return this.idList = idList;
    }

    public void setIdList(ArrayList<String> idList) {
        this.idList = idList;
    }

    public ArrayList<GroupUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<GroupUser> users) {
        this.users = users;
    }
}
