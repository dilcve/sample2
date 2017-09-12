package br.com.rf.dropchallenge.model;

import android.text.TextUtils;

import java.io.Serializable;

public class Amount implements Serializable {

    public double value;
    public String unit;

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return !TextUtils.isEmpty(unit) ? unit : "";
    }
}
