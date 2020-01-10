package info.mingyuet.weathersearch;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeeklyTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeeklyTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeeklyTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    String weatherDetail;
    HashMap<String, Integer> hashMap;
    JSONArray weekly_data;
    int maxAxis;

    private OnFragmentInteractionListener mListener;

    public WeeklyTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WeeklyTab.
     */
    // TODO: Rename and change types and number of parameters
    public static WeeklyTab newInstance(String weatherDetail) {
        WeeklyTab fragment = new WeeklyTab();
        Bundle args = new Bundle();
        args.putString("weatherDetail", weatherDetail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherDetail = getArguments().getString("weatherDetail");
        hashMap = new HashMap<>();
        hashMap.put("clear-day",R.drawable.weather_sunny);
        hashMap.put("clear-night", R.drawable.weather_night);
        hashMap.put("rain", R.drawable.weather_rainy);
        hashMap.put("sleet", R.drawable.weather_snowy_rainy);
        hashMap.put("snow", R.drawable.weather_snowy);
        hashMap.put("wind", R.drawable.weather_windy_variant);
        hashMap.put("fog", R.drawable.weather_fog_icon);
        hashMap.put("cloudy", R.drawable.weather_cloudy);
        hashMap.put("partly-cloudy-night", R.drawable.weather_night_partly_cloudy);
        hashMap.put("partly-cloudy-day", R.drawable.weather_partly_cloudy);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_weekly_tab, container, false);
        TextView summary_text = (TextView) result.findViewById(R.id.weeklySummary);
        ImageView icon_img = (ImageView) result.findViewById(R.id.weeklyIcon);
        LineChart chart = (LineChart) result.findViewById(R.id.chart);
        try {
            JSONObject root = new JSONObject(weatherDetail);
            JSONObject daily = root.getJSONObject("daily");
            String summary = daily.getString("summary");
            String icon = daily.getString("icon");
            weekly_data = daily.getJSONArray("data");
            summary_text.setText(summary);
            icon_img.setImageResource(hashMap.get(icon));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<Entry> highList= new ArrayList<Entry>();
        float maxTemp = 0;
        for (int i= 0; i<8; i++){
            try {
                JSONObject weekly_detail = weekly_data.getJSONObject(i);
                String temperatureHigh = weekly_detail.getString("temperatureHigh");
                float tempHigh = (float) Double.parseDouble(temperatureHigh);
                if (tempHigh > maxTemp){
                    maxTemp = tempHigh;
                }
                highList.add(new Entry(i, (float) Double.parseDouble(temperatureHigh)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        maxAxis = (int) Math.round(maxTemp / 10) * 10;
        ArrayList<Entry> lowList= new ArrayList<Entry>();
        for (int i= 0; i<8; i++){
            try {
                JSONObject weekly_detail = weekly_data.getJSONObject(i);
                String temperatureLow = weekly_detail.getString("temperatureLow");
                lowList.add(new Entry(i, (float) Double.parseDouble(temperatureLow)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);//设置x轴位置
        xAxis.setDrawLabels(true);
        // the labels that should be drawn on the XAxis
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.GRAY);
//        LimitLine yLimitLine = new LimitLine(80f);
        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(true);
        leftAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(true);
        rightAxis.setDrawGridLines(false);
        leftAxis.setTextColor(Color.GRAY);
        rightAxis.setTextColor(Color.GRAY);
//        leftAxis.addLimitLine(yLimitLine);
        LineDataSet dataSet1= new LineDataSet(highList, "Maximum Temperature");
        dataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet1.setAxisDependency(YAxis.AxisDependency.RIGHT);
        LineDataSet dataSet2= new LineDataSet(lowList, "Minimum Temperature");
        dataSet2.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        dataSet1.setColor(Color.rgb(250,162,25));
        dataSet2.setColor(Color.rgb(172,143,236));
        LineData data = new LineData(dataSet2,dataSet1);
        Legend legend = chart.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        Log.d("res",leftAxis.getAxisMinimum() + "<-");
        LimitLine yLimitLine = new LimitLine(maxAxis);
        yLimitLine.setLineColor(Color.GRAY);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setGranularity(10f);
        rightAxis.setGranularityEnabled(true);
        rightAxis.setGranularity(10f);
        leftAxis.addLimitLine(yLimitLine);
        LimitLine yLimitLine2 = new LimitLine(maxAxis-10);
        yLimitLine2.setLineColor(Color.GRAY);
        leftAxis.addLimitLine(yLimitLine2);
        legend.setEnabled(true);
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(15);
        chart.setData(data);
        return result;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
