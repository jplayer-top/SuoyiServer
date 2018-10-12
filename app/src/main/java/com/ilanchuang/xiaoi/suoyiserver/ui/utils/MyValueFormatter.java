package com.ilanchuang.xiaoi.suoyiserver.ui.utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Obl on 2017/7/17.
 * com.ilanchuang.xiaoi.utils
 */

public class MyValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;
    private float lastValue;

    public MyValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,###");
        lastValue = -1f;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if (lastValue == entry.getX()) {
            return (int) entry.getY() + "";
        } else {
            lastValue = entry.getX();
            return mFormat.format((int)value);
        }
    }
}
