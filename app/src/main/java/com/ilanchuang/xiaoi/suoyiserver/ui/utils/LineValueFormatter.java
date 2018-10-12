package com.ilanchuang.xiaoi.suoyiserver.ui.utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Obl on 2017/7/19.
 * com.ilanchuang.xiaoi.utils
 */

public class LineValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;
    private float lastValue;

    public LineValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,###");
        lastValue = -1f;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format((int) value);
    }
}
