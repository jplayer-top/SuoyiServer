package com.ilanchuang.xiaoi.suoyiserver.ui.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.renderer.scatter.IShapeRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Custom shape renderer that draws a single line.
 * Created by philipp on 26/06/16.
 */
public class CustomScatterShapeRenderer implements IShapeRenderer {
    private int curState;

    public CustomScatterShapeRenderer(int i) {
        curState = i;
    }

    @Override
    public void renderShape(Canvas c, IScatterDataSet dataSet, ViewPortHandler viewPortHandler,
                            float posX, float posY, Paint renderPaint) {

        renderPaint.setStyle(Paint.Style.STROKE);
        renderPaint.setColor(curState == 1 ? Color.parseColor("#21b1f4") : Color.RED);
        renderPaint.setStrokeWidth(5f);
        c.drawCircle(posX, posY, 10, renderPaint);
    }
}
