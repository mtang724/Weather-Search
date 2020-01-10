package info.mingyuet.weathersearch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Searchable extends AppCompatActivity {
    RequestQueue queue;
    ArrayAdapter<String> newsAdapter;
    ArrayList<String> lst;
    SearchView.SearchAutoComplete searchAutoComplete;
    private ProgressBar spinner;
    CardView cardView1;
    CardView cardView2;
    CardView cardView3;
    ImageView summary_icon;
    TextView temperature;
    TextView summary_text;
    TextView humidity_text;
    TextView windSpeed_text;
    TextView visibility_text;
    TextView pressure_text;
    HashMap<String,Integer> hashMap;
    TextView location_text;
    String weatherDetail;
    String locationDetail;
    FloatingActionButton fab;
    TextView fetch_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        queue = Volley.newRequestQueue(this);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        fetch_text = (TextView) findViewById(R.id.fetching_weather);
        fetch_text.setVisibility(View.VISIBLE);
        cardView1 = (CardView) findViewById(R.id.card_view_1);
        cardView1.setVisibility(View.GONE);
        cardView2 = (CardView) findViewById(R.id.card_view_2);
        cardView2.setVisibility(View.GONE);
        cardView3 = (CardView) findViewById(R.id.card_view_3);
        cardView3.setVisibility(View.GONE);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
                Intent intent = new Intent(Searchable.this, Detail.class);
                intent.putExtra("weatherDetail", weatherDetail);
                intent.putExtra("locationDetail", locationDetail);
                startActivity(intent);
            }
        });
        fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = new HashSet<>();
                SharedPreferences sharedPreferences = getSharedPreferences("location_data", Context.MODE_PRIVATE);
                String locations_json = sharedPreferences.getString("location_json5", "[]");
                //步骤2： 实例化SharedPreferences.Editor对象
                SharedPreferences.Editor editor = sharedPreferences.edit();
                try {
                    JSONArray jsonArray = new JSONArray(locations_json);
                    List<String> list = new ArrayList<String>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        list.add("\""+ jsonArray.getString(i) + "\"");
                    }
                    //步骤3：将获取过来的值放入文件
                    String location_str = "\""+ locationDetail + "\"";
                    if (list.contains(location_str)) {
//                        list.remove(locationDetail.replace(", ","#"));
                        list.remove(location_str);
                        Log.d("tag","remove" + list.toString());
                        editor.putString("location_json5", list.toString());
                        //步骤4：提交
                        editor.commit();
                        Toast.makeText(getApplicationContext(), locationDetail + " was removed from favorites",
                                Toast.LENGTH_LONG).show();
                        fab.setImageResource(R.drawable.map_marker_plus);
                    } else {
//                        list.add(locationDetail.replace(", ","#"));
                        list.add(location_str);
                        Log.d("tag","add" + list.toString());
                        editor.putString("location_json5", list.toString());
                        //步骤4：提交
                        editor.commit();
                        Toast.makeText(getApplicationContext(), locationDetail + " was added to favorites",
                                Toast.LENGTH_LONG).show();
                        fab.setImageResource(R.drawable.map_marker_minus);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("res", sharedPreferences.getString("location_json5", "[]"));
            }
        });
        Intent getIntent = getIntent();
        String city = getIntent.getStringExtra("city");
        String state = getIntent.getStringExtra("state");
        String location = getIntent.getStringExtra("location");
        TextView toolbar_text = (TextView) findViewById(R.id.toolbarTextView);
        TextView location_text = (TextView) findViewById(R.id.location_text);
        toolbar_text.setText(location);
        location_text.setText(location);
        locationDetail = location;
        toolbar_text.setTextColor(Color.WHITE);
        toolbar_text.setTextSize(20);
        ApiCall.getGeo(Searchable.this, city, state, new Response.Listener<String>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(String response) {
                try {
                    //parsing logic, please change it as per your requirement
                    JSONObject root = new JSONObject(response);
                    JSONArray results = root.getJSONArray("results");
                    JSONObject result = results.getJSONObject(0);
                    Log.d("res", result.toString());
                    JSONObject geometry = result.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    String lat = location.getString("lat");
                    String lng = location.getString("lng");
                    Log.d("res", lat);
                    StringRequest weatherRequest = weatherRequestFunc(lat, lng);
                    queue.add(weatherRequest);
                    TextView search_result = (TextView) findViewById(R.id.search_result);
                    search_result.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);
                    fetch_text.setVisibility(View.GONE);
                    SharedPreferences sharedPreferences= getSharedPreferences("location_data", Context.MODE_PRIVATE);
                    String locations_json = sharedPreferences.getString("location_json5", "[]");
                    try {
                        JSONArray jsonArray = new JSONArray(locations_json);
                        List<String> list = new ArrayList<String>();
                        for (int i=0; i<jsonArray.length(); i++) {
                            list.add("\""+ jsonArray.getString(i) + "\"");
                        }
                        String location_str = "\""+ locationDetail + "\"";
                        if (list.contains(location_str)) {
                            fab.setImageResource(R.drawable.map_marker_minus);
                        } else {
                            fab.setImageResource(R.drawable.map_marker_plus);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }
    StringRequest weatherRequestFunc(String lat, String lon){
        String url = "http://weather-nodejs-app.us-east-2.elasticbeanstalk.com/api/weather?lat=" + lat + "&lng=" + lon;
        StringRequest weatherRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        weatherDetail = response;
                        JSONObject root = null;
                        summary_icon = (ImageView) findViewById(R.id.summary_icon);
                        temperature = (TextView) findViewById(R.id.temperature);
                        summary_text = (TextView) findViewById(R.id.summary);
                        humidity_text = (TextView) findViewById(R.id.info_humidity);
                        windSpeed_text = (TextView) findViewById(R.id.info_windSpeed);
                        visibility_text = (TextView) findViewById(R.id.info_visibility);
                        pressure_text = (TextView) findViewById(R.id.info_pressure);
                        try {
                            DecimalFormat df = new DecimalFormat("0.00");
                            root = new JSONObject(response);
                            JSONObject currently = root.getJSONObject("currently");
                            String icon = currently.getString("icon");
                            String temp = currently.getString("temperature");
                            String summary = currently.getString("summary");
                            double humidity = Double.parseDouble(currently.getString("humidity"));
                            String windSpeed = df.format(Double.parseDouble(currently.getString("windSpeed")));
                            String visibility = df.format(Double.parseDouble(currently.getString("visibility")));
                            String pressure = df.format(Double.parseDouble(currently.getString("pressure")));
                            int humidity_percentage = (int) Math.round(humidity * 100);
                            int tempInt = (int) Math.round(Double.parseDouble(temp));
                            Log.d("res", icon);
                            int icon_res = hashMap.get(icon);
                            summary_icon.setImageResource(icon_res);
                            temperature.setText(tempInt + "°F");
                            summary_text.setText(summary);
                            humidity_text.setText(String.valueOf(humidity_percentage) + "%");
                            windSpeed_text.setText(String.valueOf(windSpeed) + " mph");
                            visibility_text.setText(String.valueOf(visibility) + " km");
                            pressure_text.setText(String.valueOf(pressure) + " mb");
                            JSONObject daily = root.getJSONObject("daily");
                            JSONArray daily_data = daily.getJSONArray("data");
                            for (int i = 0; i < daily_data.length(); i ++){
//                                                    Log.d("res", daily_data.get(i).toString());
                                JSONObject day = (JSONObject) daily_data.get(i);
                                String time = day.getString("time");
                                String daily_icon = day.getString("icon");
                                String temperatureLow = day.getString("temperatureLow");
                                String temperatureHigh = day.getString("temperatureHigh");
                                int tempLowInt = (int) Math.round(Double.parseDouble(temperatureLow));
                                int tempHighInt = (int) Math.round(Double.parseDouble(temperatureHigh));
                                Timestamp stamp = new Timestamp((long) Double.parseDouble(time) * 1000);
                                Date date = new Date(stamp.getTime());
                                String dateStr = new java.text.SimpleDateFormat("MM/dd/yyyy").format(date);
                                if(i==0){
                                    TextView date1 = (TextView) findViewById(R.id.date1);
                                    date1.setText(dateStr);
                                    ImageView icon1 = (ImageView) findViewById(R.id.icon1);
                                    int daily_icon_res = hashMap.get(daily_icon);
                                    icon1.setImageResource(daily_icon_res);
                                    TextView lowTemp1 = (TextView) findViewById(R.id.lowTemp1);
                                    TextView highTemp1 = (TextView) findViewById(R.id.highTemp1);
                                    lowTemp1.setText(tempLowInt + "");
                                    highTemp1.setText(tempHighInt + "");
                                }
                                if(i==1){
                                    TextView date1 = (TextView) findViewById(R.id.date2);
                                    date1.setText(dateStr);
                                    ImageView icon1 = (ImageView) findViewById(R.id.icon2);
                                    int daily_icon_res = hashMap.get(daily_icon);
                                    icon1.setImageResource(daily_icon_res);
                                    TextView lowTemp1 = (TextView) findViewById(R.id.lowTemp2);
                                    TextView highTemp1 = (TextView) findViewById(R.id.highTemp2);
                                    lowTemp1.setText(tempLowInt + "");
                                    highTemp1.setText(tempHighInt + "");
                                }
                                if(i==2){
                                    TextView date1 = (TextView) findViewById(R.id.date3);
                                    date1.setText(dateStr);
                                    ImageView icon1 = (ImageView) findViewById(R.id.icon3);
                                    int daily_icon_res = hashMap.get(daily_icon);
                                    icon1.setImageResource(daily_icon_res);
                                    TextView lowTemp1 = (TextView) findViewById(R.id.lowTemp3);
                                    TextView highTemp1 = (TextView) findViewById(R.id.highTemp3);
                                    lowTemp1.setText(tempLowInt + "");
                                    highTemp1.setText(tempHighInt + "");
                                }
                                if(i==3){
                                    TextView date1 = (TextView) findViewById(R.id.date4);
                                    date1.setText(dateStr);
                                    ImageView icon1 = (ImageView) findViewById(R.id.icon4);
                                    int daily_icon_res = hashMap.get(daily_icon);
                                    icon1.setImageResource(daily_icon_res);
                                    TextView lowTemp1 = (TextView) findViewById(R.id.lowTemp4);
                                    TextView highTemp1 = (TextView) findViewById(R.id.highTemp4);
                                    lowTemp1.setText(tempLowInt + "");
                                    highTemp1.setText(tempHighInt + "");
                                }
                                if(i==4){
                                    TextView date1 = (TextView) findViewById(R.id.date5);
                                    date1.setText(dateStr);
                                    ImageView icon1 = (ImageView) findViewById(R.id.icon5);
                                    int daily_icon_res = hashMap.get(daily_icon);
                                    icon1.setImageResource(daily_icon_res);
                                    TextView lowTemp1 = (TextView) findViewById(R.id.lowTemp5);
                                    TextView highTemp1 = (TextView) findViewById(R.id.highTemp5);
                                    lowTemp1.setText(tempLowInt + "");
                                    highTemp1.setText(tempHighInt + "");
                                }
                                if(i==5){
                                    TextView date1 = (TextView) findViewById(R.id.date6);
                                    date1.setText(dateStr);
                                    ImageView icon1 = (ImageView) findViewById(R.id.icon6);
                                    int daily_icon_res = hashMap.get(daily_icon);
                                    icon1.setImageResource(daily_icon_res);
                                    TextView lowTemp1 = (TextView) findViewById(R.id.lowTemp6);
                                    TextView highTemp1 = (TextView) findViewById(R.id.highTemp6);
                                    lowTemp1.setText(tempLowInt + "");
                                    highTemp1.setText(tempHighInt + "");
                                }
                                if(i==6){
                                    TextView date1 = (TextView) findViewById(R.id.date7);
                                    date1.setText(dateStr);
                                    ImageView icon1 = (ImageView) findViewById(R.id.icon7);
                                    int daily_icon_res = hashMap.get(daily_icon);
                                    icon1.setImageResource(daily_icon_res);
                                    TextView lowTemp1 = (TextView) findViewById(R.id.lowTemp7);
                                    TextView highTemp1 = (TextView) findViewById(R.id.highTemp7);
                                    lowTemp1.setText(tempLowInt + "");
                                    highTemp1.setText(tempHighInt + "");
                                }
                                if(i==7){
                                    TextView date1 = (TextView) findViewById(R.id.date8);
                                    date1.setText(dateStr);
                                    ImageView icon1 = (ImageView) findViewById(R.id.icon8);
                                    int daily_icon_res = hashMap.get(daily_icon);
                                    icon1.setImageResource(daily_icon_res);
                                    TextView lowTemp1 = (TextView) findViewById(R.id.lowTemp8);
                                    TextView highTemp1 = (TextView) findViewById(R.id.highTemp8);
                                    lowTemp1.setText(tempLowInt + "");
                                    highTemp1.setText(tempHighInt + "");
                                }

                            }
                            spinner.setVisibility(View.GONE);
                            fetch_text.setVisibility(View.GONE);
                            cardView1.setVisibility(View.VISIBLE);
                            cardView2.setVisibility(View.VISIBLE);
                            cardView3.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("response","not working" + error);
            }
        });
        return weatherRequest;
    }

}
