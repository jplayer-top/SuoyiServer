package com.ilanchuang.xiaoi.suoyiserver.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.ilanchuang.xiaoi.suoyiserver.R;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.SYServer;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.HealthDataBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.model.ServerModel;
import com.ilanchuang.xiaoi.suoyiserver.ui.utils.CustomScatterShapeRenderer;
import com.ilanchuang.xiaoi.suoyiserver.ui.utils.LineValueFormatter;
import com.ilanchuang.xiaoi.suoyiserver.ui.utils.MyValueFormatter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.jplayer.baseprolibrary.net.retrofit.NetCallBackObserver;
import top.jplayer.baseprolibrary.ui.adapter.BaseViewPagerViewAdapter;
import top.jplayer.baseprolibrary.ui.fragment.SuperBaseFragment;
import top.jplayer.baseprolibrary.utils.ScreenUtils;
import top.jplayer.baseprolibrary.utils.StringUtils;

/**
 * Created by Obl on 2017/8/24.
 * com.ilanchuang.xiaoi.health.fragment
 */

public class HealthReportFragment extends SuperBaseFragment {

    @BindView(R.id.chart1)
    BarChart mChart1;
    @BindView(R.id.chart2)
    LineChart mChart2;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.llDit)
    LinearLayout mLlDit;


    @BindView(R.id.scrollView)
    NestedScrollView mScrollView;

    @BindView(R.id.tvWatchOne)
    public TextView mTvWatchOne;
    @BindView(R.id.tvWatchTwo)
    public TextView mTvWatchTwo;
    @BindView(R.id.cardSelTab)
    public CardView mCardSelTab;
    @BindView(R.id.chart5)
    LineChart mChart5;
    private int mHeight;
    private int mStatus;
    private final int WILL_UP = 0;
    private final int WILL_DOWN = 1;
    private final int ING_STATUS = 2;

    private int curFragment = 0;
    private List<HealthDataBean.ReportBean> mHealthReportBeanList;

    @Override
    public int initLayout() {
        return R.layout.fragment_health_report;
    }

    @Override
    protected void initData(View rootView) {
        ButterKnife.bind(this, rootView);
        mChart1.setNoDataText("暂无测量数据");
        mChart2.setNoDataText("暂无测量数据");
        mChart5.setNoDataText("暂无测量数据");
        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }
        new ServerModel(SYServer.class)
                .requestDate(arguments.getString("fid"))
                .subscribe(new NetCallBackObserver<HealthDataBean>() {
                    @Override
                    public void responseSuccess(HealthDataBean response) {
                        if (response.reports != null) {
                            mHealthReportBeanList = response.reports;
                            mCardSelTab.setVisibility(View.VISIBLE);
                            if (response.reports.size() > 0) {
                                mTvWatchOne.setText(StringUtils.init().fixNullStr(response.reports.get(0).rname, ""));
                                if (response.reports.size() > 1) {
                                    mTvWatchTwo.setText(StringUtils.init().fixNullStr(response.reports.get(1).rname, ""));
                                }
                            }
                            mTvWatchTwo.setVisibility(response.reports.size() > 1 ? View.VISIBLE : View.GONE);
                            if (response.reports.size() >= 1) {
                                setReport(response.reports.get(0));
                            }
                            curFragment = 0;
                        }
                    }

                    @Override
                    public void responseFail(HealthDataBean healthDataBean) {

                    }
                });
        mHeight = ScreenUtils.dp2px(64);
        mTvWatchOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvWatchTwo.setEnabled(true);
                mTvWatchOne.setEnabled(false);
                if (curFragment != 0) {
                    curFragment = 0;
                    setReport(mHealthReportBeanList.get(curFragment));
                }
            }
        });
        mTvWatchTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvWatchTwo.setEnabled(false);
                mTvWatchOne.setEnabled(true);
                if (curFragment != 1) {
                    curFragment = 1;
                    setReport(mHealthReportBeanList.get(curFragment));
                }
            }
        });
        mStatus = WILL_UP;
        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY - oldScrollY > 0 && scrollY > mHeight && mStatus == WILL_UP) {
                    ViewAnimator.animate(mCardSelTab).duration(300).translationY(0, -mHeight)
                            .onStart(new AnimationListener.Start() {
                                @Override
                                public void onStart() {
                                    mStatus = ING_STATUS;
                                }
                            })
                            .onStop(new AnimationListener.Stop() {
                                @Override
                                public void onStop() {
                                    mStatus = WILL_DOWN;
                                    mCardSelTab.setVisibility(View.GONE);
                                }
                            }).start();
                }
                if (scrollY - oldScrollY < 0 && scrollY < mHeight && mStatus == WILL_DOWN) {
                    ViewAnimator.animate(mCardSelTab).duration(300).translationY(-mHeight, 0)
                            .onStart(new AnimationListener.Start() {
                                @Override
                                public void onStart() {
                                    mStatus = ING_STATUS;
                                    mCardSelTab.setVisibility(View.VISIBLE);
                                }
                            })
                            .onStop(new AnimationListener.Stop() {
                                @Override
                                public void onStop() {
                                    mStatus = WILL_UP;
                                }
                            }).start();
                }
            }
        });
    }

    public void setReport(HealthDataBean.ReportBean reportsBean) {
        xValue = reportsBean.bloodStatusDataFormatV.cat;
        mSValue = reportsBean.glucoseStatusDataFormatV.get(0).cat;
        initLineChart(mChart2, generateLineData(reportsBean.heartStatusDataFormatV.hr), "近日心率");
        initBarChart(mChart1, generateBarData(reportsBean.bloodStatusDataFormatV.hp,
                reportsBean.bloodStatusDataFormatV.lp));
        initLineChart(mChart5, generateO2LineData(reportsBean.oxygenDataFormatV.bo), "近日血氧");

        mLlDit.removeAllViews();
        mViewPager.setAdapter(new ChartSugarAdapter(reportsBean.glucoseStatusDataFormatV));
        for (int i = 0; i < 7; i++) {
            TextView textView = new TextView(getContext());
            textView.setTag(i);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                    .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i != 0) {
                layoutParams.leftMargin = ScreenUtils.getWidthOfScreen(getContext(), 100, 8);
            }
            textView.setLayoutParams(layoutParams);
            textView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(i == 0 ? R.drawable
                            .shape_dit_big : R.drawable.shape_dit_smill),
                    null,
                    null);
            textView.setText(xValue.get(i));
            textView.setTextSize(8);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.parseColor("#999999"));
            mLlDit.addView(textView);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < 7; i++) {
                    TextView view = (TextView) mLlDit.getChildAt(i);
                    view.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(i == position ? R
                            .drawable.shape_dit_big : R.drawable.shape_dit_smill), null, null);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private List<String> mSValue;
    private List<String> xValue;

    private void initBarChart(BarChart mChart, BarData data) {
        mChart.getDescription().setEnabled(false);
        mChart.setTouchEnabled(false);
        mChart.setDrawValueAboveBar(true);
        YAxis yAxis = mChart.getAxisLeft();
        mChart.getAxisRight().setEnabled(false);
        XAxis xAxis = mChart.getXAxis();
        setXYAxis(yAxis, xAxis);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xValue.get((int) value % xValue.size());
            }
        });
        mChart.animateY(1000);
        Legend l = mChart.getLegend();
        ArrayList<LegendEntry> custom = new ArrayList<>();
        LegendEntry entry1 = new LegendEntry();
        entry1.label = "低压";
        entry1.formColor = BarColors[0];
        custom.add(entry1);
        LegendEntry entry2 = new LegendEntry();
        entry2.label = "高压      近日血压";
        entry2.formColor = BarColors[1];
        custom.add(entry2);
        l.setCustom(custom);
        setLegend(l);
        mChart.setData(data);
        mChart.setFitBars(true);
        mChart.invalidate();
    }


    private void initScatterChart(ScatterChart mChart, ScatterData data) {
        mChart.setDrawGridBackground(false);
        mChart.setTouchEnabled(false);
        mChart.getDescription().setEnabled(false);
        Legend l = mChart.getLegend();
        ArrayList<LegendEntry> custom = new ArrayList<>();
        LegendEntry entry1 = new LegendEntry();
        entry1.label = "今日血糖";
        entry1.formColor = BarColors[0];
        custom.add(entry1);
        l.setCustom(custom);
        setLegend(l);

        YAxis yAxis = mChart.getAxisLeft();
        XAxis xAxis = mChart.getXAxis();
        xAxis.setAxisMinimum(data.getXMin() - 0.5f);
        xAxis.setAxisMaximum(data.getXMax() + 0.5f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mSValue.get((int) value % xValue.size());
            }
        });
        mChart.getAxisRight().setEnabled(false);
        setXYAxis(yAxis, xAxis);
        mChart.animateX(1000);
        mChart.setData(data);
        mChart.invalidate();
    }


    private void initLineChart(LineChart mChart, LineData lineData, String label) {
        mChart.setDrawGridBackground(false);
        mChart.setTouchEnabled(false);
        mChart.getDescription().setEnabled(false);
        Legend l = mChart.getLegend();
        ArrayList<LegendEntry> custom = new ArrayList<>();
        LegendEntry entry1 = new LegendEntry();
        entry1.label = label;
        entry1.formColor = BarColors[0];
        custom.add(entry1);
        l.setCustom(custom);
        setLegend(l);

        YAxis yAxis = mChart.getAxisLeft();
        XAxis xAxis = mChart.getXAxis();
        xAxis.setAxisMinimum(lineData.getXMin() - 0.5f);
        xAxis.setAxisMaximum(lineData.getXMax() + 0.5f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xValue.get((int) value % xValue.size());
            }
        });
        mChart.getAxisRight().setEnabled(false);
        setXYAxis(yAxis, xAxis);
        mChart.animateX(1000);
        mChart.setData(lineData);
        mChart.invalidate();
    }

    private void setXYAxis(YAxis yAxis, XAxis xAxis) {
        yAxis.setDrawAxisLine(true);
        yAxis.setDrawGridLines(false);
        yAxis.setGranularity(1f);
        yAxis.setAxisMinimum(0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
    }

    private void setLegend(Legend l) {

        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);
    }

    private int[] mColors = new int[]{
            Color.parseColor("#21b1f4"),
            Color.parseColor("#eb4f4e")
    };
    private int[] BarColors = new int[]{
            Color.parseColor("#21b1f4"),//蓝
            Color.parseColor("#f0c66b"),//黄
            Color.RED
    };

    private LineData generateLineData(List<HealthDataBean.ReportBean.HeartStatusDataFormatVBean.HrBean> hr) {

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        List<Integer> list = new ArrayList<>();

        ArrayList<Entry> values = new ArrayList<Entry>();
        for (int index = 0; index < hr.size(); index++) {
            if (hr.get(index).status < 1) {
                list.add(mColors[0]);
            } else {
                list.add(mColors[1]);
            }
            values.add(new Entry(index, hr.get(index).data));
        }
        LineDataSet d = new LineDataSet(values, "近日心率 ");
        d.setLineWidth(2.5f);
        d.setCircleRadius(4f);
        d.setColor(Color.parseColor("#fec37d"));
        dataSets.add(d);

        ((LineDataSet) dataSets.get(0)).setCircleColors(list);

        LineData lineData = new LineData(dataSets);
        lineData.setValueFormatter(new LineValueFormatter());
        return lineData;

    }

    private LineData generateO2LineData(List<HealthDataBean.ReportBean.OxygenDataFormatVBean.BoBean> bo) {

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        List<Integer> list = new ArrayList<>();

        ArrayList<Entry> values = new ArrayList<Entry>();
        for (int index = 0; index < bo.size(); index++) {
            if (bo.get(index).status < 1) {
                list.add(mColors[0]);
            } else {
                list.add(mColors[1]);
            }
            values.add(new Entry(index, bo.get(index).data));
        }
        LineDataSet d = new LineDataSet(values, "近日血氧 ");
        d.setLineWidth(2.5f);
        d.setCircleRadius(4f);
        d.setColor(Color.parseColor("#fec37d"));
        dataSets.add(d);

        ((LineDataSet) dataSets.get(0)).setCircleColors(list);

        LineData lineData = new LineData(dataSets);
        lineData.setValueFormatter(new LineValueFormatter());
        return lineData;

    }

    private ScatterData generateScatterData(List<HealthDataBean.ReportBean.GlucoseStatusDataFormatVBean.BgBean>
                                                    hr) {

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        List<Integer> listColor = new ArrayList<>();
        for (int i = 0; i < hr.size(); i++) {
            float val = (float) (hr.get(i).data);
            if (hr.get(i).status == 1) {
                yVals2.add(new Entry(i, val));
            } else {
                yVals1.add(new Entry(i, val));
            }
            listColor.add(hr.get(i).status == 1 ? Color.RED : Color.parseColor("#21b1f4"));
        }
        ArrayList<IScatterDataSet> dataSets = new ArrayList<IScatterDataSet>();
        if (yVals2.size() > 0) {
            ScatterDataSet set2 = new ScatterDataSet(yVals2, "血糖不正常");
            set2.setColor(Color.RED);
            set2.setShapeRenderer(new CustomScatterShapeRenderer(2));
            set2.setValueTextColor(Color.parseColor("#FF0000"));//设置文字的颜色
            set2.setScatterShapeSize(8f);
            set2.setScatterShapeHoleColor(Color.parseColor("#FF0000"));
            dataSets.add(set2);
        }

        ScatterDataSet set1 = new ScatterDataSet(yVals1, "今日血糖");
        set1.setColors(listColor);
        set1.setShapeRenderer(new CustomScatterShapeRenderer(1));
        set1.setScatterShapeSize(8f);
        dataSets.add(set1);
        // add the datasets
        return new ScatterData(dataSets);
    }

    @NonNull
    private BarData generateBarData(List<HealthDataBean.ReportBean.BloodStatusDataFormatVBean.HpBean> hp,
                                    List<HealthDataBean.ReportBean.BloodStatusDataFormatVBean.LpBean> lp) {
        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < hp.size(); i++) {
            float val1 = (float) lp.get(i).data;
            float val2 = (float) hp.get(i).data;
            float val3 = val2 - val1;
            list.add(lp.get(i).status > 0 ? BarColors[2] : BarColors[0]);
            list.add(hp.get(i).status > 0 ? BarColors[2] : BarColors[1]);
            yVals1.add(new BarEntry(i, new float[]{val1, val3}));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "近日血压");
        set1.setDrawIcons(false);
        int[] colors = {Color.parseColor("#21B1F4"), Color.parseColor("#f0c66b")};
        set1.setStackLabels(new String[]{"低压", "高压"});
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        set1.setColors(list);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new MyValueFormatter());
        data.setBarWidth(0.5f);
        data.setValueTextColor(Color.parseColor("#000000"));
        return data;
    }

    @NonNull
    private BarData generateStepBarData(List<Integer> sp) {
        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        for (int i = 0; i < sp.size(); i++) {
//            float val2 = new Random().nextInt(10000);
            float val2 = (float) sp.get(i);
            yVals1.add(new BarEntry(i, new float[]{val2}));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "近日步数");
        set1.setDrawIcons(false);
        set1.setStackLabels(new String[]{"步数"});
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        set1.setColor(getResources().getColor(R.color.colorPrimary));

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new MyValueFormatter());
        data.setBarWidth(0.5f);
        data.setValueTextColor(Color.parseColor("#000000"));
        return data;
    }

    private class ChartSugarAdapter extends BaseViewPagerViewAdapter<HealthDataBean.ReportBean.GlucoseStatusDataFormatVBean> {

        public ChartSugarAdapter(List<HealthDataBean.ReportBean.GlucoseStatusDataFormatVBean> list) {
            super(list, R.layout.item_glucose);
        }

        @Override
        public void bindView(View view, HealthDataBean.ReportBean.GlucoseStatusDataFormatVBean glucoseStatusDataFormatVBean, int position) {
            ScatterChart chartViewPager = (ScatterChart) view.findViewById(R.id.chartViewPager);
            chartViewPager.setNoDataText("暂无测量数据");
            initScatterChart(chartViewPager, generateScatterData(list.get(position).bg));

        }
    }
}
