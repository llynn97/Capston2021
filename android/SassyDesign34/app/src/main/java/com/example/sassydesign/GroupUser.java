package com.example.sassydesign;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;

import java.io.Serializable;

public class GroupUser implements Serializable {
    String ID;
    String detailProfile;
    String detailUserName;
    String detailSum;
    String detailPercent;

    public GroupUser(String ID, String detailProfile, String detailUserName, String detailSum, String detailPercent) {
        this.ID = ID;
        this.detailProfile = detailProfile;
        this.detailUserName = detailUserName;
        this.detailSum = detailSum;
        this.detailPercent = detailPercent;
    }

    public String getDetailProfile() {
        return detailProfile;
    }

    public void setDetailProfile(String detailProfile) {
        this.detailProfile = detailProfile;
    }

    public String getDetailUserName() {
        return detailUserName;
    }

    public void setDetailUserName(String detailUserName) {
        this.detailUserName = detailUserName;
    }

    public String getDetailSum() {
        return detailSum;
    }

    public void setDetailSum(String detailSum) {
        this.detailSum = detailSum;
    }

    public String getDetailPercent() {
        return detailPercent;
    }

    public void setDetailPercent(String detailPercent) {
        this.detailPercent = detailPercent;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}