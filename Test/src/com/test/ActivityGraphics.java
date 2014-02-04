package com.test;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.androidplot.util.PlotStatistics;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

public class ActivityGraphics extends Activity implements SensorEventListener {
	
	 private class APRIndexFormat extends Format {
	        @Override
	        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
	            Number num = (Number) obj;
	 
	            // using num.intValue() will floor the value, so we add 0.5 to round instead:
	            int roundNum = (int) (num.floatValue() + 0.5f);
	            switch(roundNum) {
	                case 0:
	                    toAppendTo.append("North/South/East/West");
	                    break;
	                case 1:
	                    toAppendTo.append("Pitch");
	                    break;
	                case 2:
	                    toAppendTo.append("Roll");
	                    break;
	                default:
	                    toAppendTo.append("Unknown");
	            }
	            return toAppendTo;
	        }
	 
	        @Override
	        public Object parseObject(String source, ParsePosition pos) {
	            return null;  // We don't use this so just return null for now.
	        }
	    }
	 
	    private static final int HISTORY_SIZE = 1000;            // number of points to plot in history
	    private SensorManager sensorMgr = null;
	    private Sensor orSensor = null;
	 
	    private XYPlot aprLevelsPlot = null;
	    private XYPlot aprHistoryPlot = null;
	 
	    private CheckBox hwAcceleratedCb;
	    private CheckBox showFpsCb;
	    private SimpleXYSeries aprLevelsSeries = null;
	    private SimpleXYSeries azimuthHistorySeries = null;
	    private SimpleXYSeries pitchHistorySeries = null;
	    private SimpleXYSeries rollHistorySeries = null;
	 
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.act_graphics_sensor);
	 
	        // setup the APR Levels plot:
	        aprLevelsPlot = (XYPlot) findViewById(R.id.aprLevelsPlot);
	 
	        aprLevelsSeries = new SimpleXYSeries("APR Levels");
	        aprLevelsSeries.useImplicitXVals();
	        aprLevelsPlot.addSeries(aprLevelsSeries,
	                new BarFormatter(Color.argb(100, 0, 200, 0), Color.rgb(0, 80, 0)));
	        aprLevelsPlot.setDomainStepValue(3);
	        aprLevelsPlot.setTicksPerRangeLabel(2);
	 
	        // per the android documentation, the minimum and maximum readings we can get from
	        // any of the orientation sensors is -180 and 359 respectively so we will fix our plot's
	        // boundaries to those values.  If we did not do this, the plot would auto-range which
	        // can be visually confusing in the case of dynamic plots.
	        aprLevelsPlot.setRangeBoundaries(-180, 359, BoundaryMode.FIXED);
	 
	        // use our custom domain value formatter:
	        aprLevelsPlot.setDomainValueFormat(new APRIndexFormat());
	 
	        // update our domain and range axis labels:
	        aprLevelsPlot.setDomainLabel("Axis");
	        aprLevelsPlot.getDomainLabelWidget().pack();
	        aprLevelsPlot.setRangeLabel("Angle (Degs)");
	        aprLevelsPlot.getRangeLabelWidget().pack();
	        aprLevelsPlot.setGridPadding(15, 0, 15, 0);
	 
	        // setup the APR History plot:
	        aprHistoryPlot = (XYPlot) findViewById(R.id.aprHistoryPlot);
	 
	        azimuthHistorySeries = new SimpleXYSeries("Azimuth");
	        azimuthHistorySeries.useImplicitXVals();
	        pitchHistorySeries = new SimpleXYSeries("Pitch");
	        pitchHistorySeries.useImplicitXVals();
	        rollHistorySeries = new SimpleXYSeries("Roll");
	        rollHistorySeries.useImplicitXVals();
	 
	        aprHistoryPlot.setRangeBoundaries(-180, 359, BoundaryMode.FIXED);
	        aprHistoryPlot.setDomainBoundaries(0, 1000, BoundaryMode.FIXED);
	        aprHistoryPlot.addSeries(azimuthHistorySeries, new LineAndPointFormatter(Color.rgb(100, 100, 200), Color.rgb(100, 100, 200), null,null));
	        aprHistoryPlot.addSeries(pitchHistorySeries, new LineAndPointFormatter(Color.rgb(100, 200, 100), Color.rgb(100, 200, 100), null,null));
	        aprHistoryPlot.addSeries(rollHistorySeries, new LineAndPointFormatter(Color.rgb(200, 100, 100),Color.rgb(200, 100, 100), null,null));
	        aprHistoryPlot.setDomainStepValue(11); ///Divisones de la grafica en X
	        aprHistoryPlot.setTicksPerRangeLabel(5);
	        aprHistoryPlot.setDomainLabel("Sample Index");
	        aprHistoryPlot.getDomainLabelWidget().pack();
	        aprHistoryPlot.setRangeLabel("Angle (Degs)");
	        aprHistoryPlot.getRangeLabelWidget().pack();
	 
	        // setup checkboxes:
	        hwAcceleratedCb = (CheckBox) findViewById(R.id.hwAccelerationCb);
	        final PlotStatistics levelStats = new PlotStatistics(1000, false);
	        final PlotStatistics histStats = new PlotStatistics(1000, false);
	 
	        aprLevelsPlot.addListener(levelStats);
	        aprHistoryPlot.addListener(histStats);
	        hwAcceleratedCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	            @Override
	            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
	                if(b) {
	                    aprLevelsPlot.setLayerType(View.LAYER_TYPE_NONE, null);
	                    aprHistoryPlot.setLayerType(View.LAYER_TYPE_NONE, null);
	                } else {
	                    aprLevelsPlot.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	                    aprHistoryPlot.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	                }
	            }
	        });
	 
	        showFpsCb = (CheckBox) findViewById(R.id.showFpsCb);
	        showFpsCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	            @Override
	            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
	                levelStats.setAnnotatePlotEnabled(b);
	                histStats.setAnnotatePlotEnabled(b);
	            }
	        });
	 
	        // get a ref to the BarRenderer so we can make some changes to it:
	        BarRenderer barRenderer = (BarRenderer) aprLevelsPlot.getRenderer(BarRenderer.class);
	        if(barRenderer != null) {
	            // make our bars a little thicker than the default so they can be seen better:
	            barRenderer.setBarWidth(25);
	        }
	 
	        // register for orientation sensor events:
	        sensorMgr = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
	        for (Sensor sensor : sensorMgr.getSensorList(Sensor.TYPE_ORIENTATION)) {
	            if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
	                orSensor = sensor;
	            }
	        }
	 
	        // if we can't access the orientation sensor then exit:
	        if (orSensor == null) {
	            System.out.println("Failed to attach to orSensor.");
	            cleanup();
	        }
	 
	        sensorMgr.registerListener(this, orSensor, SensorManager.SENSOR_DELAY_UI);
	 
	    }
	 
	    private void cleanup() {
	        // aunregister with the orientation sensor before exiting:
	        sensorMgr.unregisterListener(this);
	        finish();
	    }
	 
	    // Called whenever a new orSensor reading is taken.
	    @Override
	    public synchronized void onSensorChanged(SensorEvent sensorEvent) {
	 
	        // update instantaneous data:
	        Number[] series1Numbers = {sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]};
	        aprLevelsSeries.setModel(Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);
	 
	        // get rid the oldest sample in history:
	        if (rollHistorySeries.size() > HISTORY_SIZE) {
	            rollHistorySeries.removeFirst();
	            pitchHistorySeries.removeFirst();
	            azimuthHistorySeries.removeFirst();
	        }
	 
	        // add the latest history sample:
	        azimuthHistorySeries.addLast(null, sensorEvent.values[0]);
	        pitchHistorySeries.addLast(null, sensorEvent.values[1]);
	        rollHistorySeries.addLast(null, sensorEvent.values[2]);
	 
	        // redraw the Plots:
	        aprLevelsPlot.redraw();
	        aprHistoryPlot.redraw();
	    }
	 
	    @Override
	    public void onAccuracyChanged(Sensor sensor, int i) {
	        // Not interested in this event
	    }
	 
	/*
	private XYPlot plot;
	 
	    @Override
	    public void onCreate(Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	 
	        // fun little snippet that prevents users from taking screenshots
	        // on ICS+ devices :-)
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
	                                 WindowManager.LayoutParams.FLAG_SECURE);
	 
	        setContentView(R.layout.act_graphics);
	 
	        // initialize our XYPlot reference:
	        plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
	 
	        // Create a couple arrays of y-values to plot:
	        Number[] series1Numbers = {1, 8, 5, 2, 7, 4};
	        Number[] series2Numbers = {4, 6, 3, 8, 2, 10};
	 
	        // Turn the above arrays into XYSeries':
	        XYSeries series1 = new SimpleXYSeries(
	                Arrays.asList(series1Numbers),          // SimpleXYSeries takes a List so turn our array into a List
	                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
	                "Series1");                             // Set the display title of the series
	 
	        // same as above
	        XYSeries series2 = new SimpleXYSeries(Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2");
	 
	        // Create a formatter to use for drawing a series using LineAndPointRenderer
	        // and configure it from xml:
	        LineAndPointFormatter series1Format = new LineAndPointFormatter();
	        series1Format.setPointLabelFormatter(new PointLabelFormatter());
	        series1Format.configure(getApplicationContext(),
	                R.layout.function1_format);
	 
	        // add a new series' to the xyplot:
	        plot.addSeries(series1, series1Format);
	 
	        // same as above:
	        LineAndPointFormatter series2Format = new LineAndPointFormatter();
	        series2Format.setPointLabelFormatter(new PointLabelFormatter());
	        series2Format.configure(getApplicationContext(),
	                R.layout.function2_format);
	        plot.addSeries(series2, series2Format);
	        
	        ///We can replace the code before to this one:
	        //LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.RED, Color.GREEN, Color.
	 
	        // reduce the number of range labels
	        plot.setTicksPerRangeLabel(3);
	        plot.getGraphWidget().setDomainLabelOrientation(-45);
	 
	    }
	    
	    */

}
