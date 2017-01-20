package com.example.android.planesmonitor.util;

import android.content.Context;
import android.graphics.Color;
import android.widget.FrameLayout;

import com.example.android.planesmonitor.PlanesMonitorApp;
import com.example.android.planesmonitor.domain.PlaneActivityTime;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Date;
import java.util.List;



public class GraphGenerator {

    public static void populateGraphWithAllPlanes(Context context, FrameLayout frameLayout) {
        populateGraph(context, frameLayout, PlanesMonitorApp.getInstance().getDataStoreManager().getAllPlaneActivitiesTimes());
    }

    public static void populateGraphWithOnePlane(Context context, FrameLayout frameLayout, String planeName) {
        populateGraph(context, frameLayout, PlanesMonitorApp.getInstance().getDataStoreManager().getPlaneActivitiesTimesByName(planeName));
    }

    private static void populateGraph(Context context, FrameLayout frameLayout, List<PlaneActivityTime> planeActivityTimes) {
        TimeSeries timeSeries = new TimeSeries("Minutes practicing plane");
        long max = 0;
        for (PlaneActivityTime planeActivityTime : planeActivityTimes) {
            timeSeries.add(new Date(planeActivityTime.getDay().getTimeInMillis()), (double) planeActivityTime.getTotalMinutes());
            if (planeActivityTime.getTotalMinutes() > max) {
                max = planeActivityTime.getTotalMinutes();
            }
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(timeSeries);

        XYSeriesRenderer timesRenderer = new XYSeriesRenderer();
        timesRenderer.setColor(Color.BLUE);
        timesRenderer.setPointStyle(PointStyle.CIRCLE);
        timesRenderer.setFillPoints(true);
        timesRenderer.setLineWidth(4);
        timesRenderer.setDisplayChartValues(true);
        timesRenderer.setChartValuesTextSize(30);

        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();

        multiRenderer.setChartTitle("Plane Times Chart");
        multiRenderer.setXTitle("Days");
        multiRenderer.setYTitle("Minutes");
        multiRenderer.setMarginsColor(Color.argb(0, 128, 0, 0)); // transparent margins
//        multiRenderer.setZoomButtonsVisible(true);
        multiRenderer.setDisplayValues(true);
        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setYAxisMax(max + max / 20);
        multiRenderer.setYAxisMin(0);
        multiRenderer.setShowGrid(true); // we show the grid
        multiRenderer.setChartTitleTextSize(40);
        multiRenderer.setLabelsTextSize(30);
        multiRenderer.setLegendTextSize(30);
        multiRenderer.setAxisTitleTextSize(30);

        // Adding visitsRenderer and viewsRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
        // should be same
        multiRenderer.addSeriesRenderer(timesRenderer);

        GraphicalView chartView = (GraphicalView) ChartFactory.getTimeChartView(context, dataset, multiRenderer,"dd-MMM-yyyy");

        frameLayout.addView(chartView);
    }
}
