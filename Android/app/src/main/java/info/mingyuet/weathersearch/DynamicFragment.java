package info.mingyuet.weathersearch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

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


public class DynamicFragment extends Fragment {
    View view;
//    RequestQueue queue;
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
    FloatingActionButton fab;
    String weatherDetail;
    String locationDetail;
    TabLayout tab;
    List<String> locations;
    ViewPager pageView;
    TextView fetch_text;


    public static DynamicFragment newInstance(int val, String location) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putInt("position", val);
        args.putString("location", location);
        fragment.setArguments(args);
        return fragment;
    }

    int val;
    TextView c;

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main, container, false);
        tab = (TabLayout) getActivity().findViewById(R.id.tabs);
        pageView = (ViewPager) getActivity().findViewById(R.id.frameLayout);
        String location = getArguments().getString("location");
        locationDetail = location;
        int position = getArguments().getInt("position");
        hashMap = new HashMap<>();
        hashMap.put("clear-day", R.drawable.weather_sunny);
        hashMap.put("clear-night", R.drawable.weather_night);
        hashMap.put("rain", R.drawable.weather_rainy);
        hashMap.put("sleet", R.drawable.weather_snowy_rainy);
        hashMap.put("snow", R.drawable.weather_snowy);
        hashMap.put("wind", R.drawable.weather_windy_variant);
        hashMap.put("fog", R.drawable.weather_fog_icon);
        hashMap.put("cloudy", R.drawable.weather_cloudy);
        hashMap.put("partly-cloudy-night", R.drawable.weather_night_partly_cloudy);
        hashMap.put("partly-cloudy-day", R.drawable.weather_partly_cloudy);
        Log.d("res", position + location);
        if (position == 0) {
            view = inflater.inflate(R.layout.activity_main, container, false);
            spinner = (ProgressBar) view.findViewById(R.id.progressBar1);
            spinner.setVisibility(View.VISIBLE);
            fetch_text = (TextView) view.findViewById(R.id.fetching_weather);
            fetch_text.setVisibility(View.VISIBLE);
            cardView1 = (CardView) view.findViewById(R.id.card_view_1);
            cardView1.setVisibility(View.GONE);
            cardView2 = (CardView) view.findViewById(R.id.card_view_2);
            cardView2.setVisibility(View.GONE);
            cardView3 = (CardView) view.findViewById(R.id.card_view_3);
            cardView3.setVisibility(View.GONE);
            cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
                Intent intent = new Intent(getContext(), Detail.class);
                intent.putExtra("weatherDetail", weatherDetail);
                intent.putExtra("locationDetail", locationDetail);
                startActivity(intent);
            }
        });
            hashMap = new HashMap<>();
            hashMap.put("clear-day", R.drawable.weather_sunny);
            hashMap.put("clear-night", R.drawable.weather_night);
            hashMap.put("rain", R.drawable.weather_rainy);
            hashMap.put("sleet", R.drawable.weather_snowy_rainy);
            hashMap.put("snow", R.drawable.weather_snowy);
            hashMap.put("wind", R.drawable.weather_windy_variant);
            hashMap.put("fog", R.drawable.weather_fog_icon);
            hashMap.put("cloudy", R.drawable.weather_cloudy);
            hashMap.put("partly-cloudy-night", R.drawable.weather_night_partly_cloudy);
            hashMap.put("partly-cloudy-day", R.drawable.weather_partly_cloudy);
            String url = "http://www.ip-api.com/json";
            final RequestQueue queue = Volley.newRequestQueue(getActivity());
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            location_text = (TextView) view.findViewById(R.id.location_text);
                            JSONObject root = null;
                            try {
                                root = new JSONObject(response);
                                String city = root.getString("city");
                                String state = root.getString("region");
                                String country = root.getString("countryCode");
                                String location = city + ", " + state + ", " + country;
                                locationDetail = location;
                                location_text.setText(location);
                                String lat = root.getString("lat");
                                String lon = root.getString("lon");
                                StringRequest weatherRequest = weatherRequestFunc(lat, lon);
                                queue.add(weatherRequest);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("response", "not working" + error);
                }
            });
            queue.add(stringRequest);
            return view;
        }else{
                fab = (FloatingActionButton) view.findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       try {

                           SharedPreferences sharedPreferences = getActivity().getSharedPreferences("location_data", Context.MODE_PRIVATE);
                           String locations_json = sharedPreferences.getString("location_json5", "[]");
                           //步骤2： 实例化SharedPreferences.Editor对象
                           SharedPreferences.Editor editor = sharedPreferences.edit();
                           JSONArray jsonArray = new JSONArray(locations_json);
                           List<String> list = new ArrayList<String>();
                           for (int i = 0; i < jsonArray.length(); i++) {
                               list.add("\"" + jsonArray.getString(i) + "\"");
                           }
                           //步骤3：将获取过来的值放入文件
                           String location_str = "\"" + locationDetail + "\"";
                           int index = list.indexOf(location_str);
                           list.remove(location_str);
                           Log.d("tag", "remove" + index);
                           Log.d("tag", "remove" + location_str);
                           editor.putString("location_json5", list.toString());
                           //步骤4：提交
                           editor.commit();
                           SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("location_data", Context.MODE_PRIVATE);
                           String locations_json2 = sharedPreferences2.getString("location_json5", "[]");
                           //步骤2： 实例化SharedPreferences.Editor对象
                           JSONArray jsonArray2 = new JSONArray(locations_json2);
                           List<String> list2 = new ArrayList<String>();
                           for (int i = 0; i < jsonArray2.length(); i++) {
                               list2.add(jsonArray2.getString(i));
                           }
                           Log.d("tag", "List2" + list2.toString());
                           Toast.makeText(getActivity(), locationDetail + " was removed from favorites",
                                   Toast.LENGTH_LONG).show();
                           fab.setImageResource(R.drawable.map_marker_plus);
                           tab.removeTabAt(index+1);
                           PlansPagerAdapter adapter = new PlansPagerAdapter(getActivity().getSupportFragmentManager(), tab.getTabCount(), list2);
                           adapter.notifyDataSetChanged();
                           pageView.setAdapter(adapter);

                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
                });
                fetch_text = (TextView) view.findViewById(R.id.fetching_weather);
                fetch_text.setVisibility(View.VISIBLE);
                spinner = (ProgressBar) view.findViewById(R.id.progressBar1);
                spinner.setVisibility(View.VISIBLE);
                cardView1 = (CardView) view.findViewById(R.id.card_view_1);
                cardView1.setVisibility(View.GONE);
                cardView2 = (CardView) view.findViewById(R.id.card_view_2);
                cardView2.setVisibility(View.GONE);
                cardView3 = (CardView) view.findViewById(R.id.card_view_3);
                cardView3.setVisibility(View.GONE);
            cardView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
                    Intent intent = new Intent(getContext(), Detail.class);
                    intent.putExtra("weatherDetail", weatherDetail);
                    intent.putExtra("locationDetail", locationDetail);
                    startActivity(intent);
                }
            });
                fab = view.findViewById(R.id.fab);
                fab.setImageResource(R.drawable.map_marker_minus);
                fab.setVisibility(View.VISIBLE);
                String city;
                String state;
                String[] queries = location.split(",");
                if (queries.length > 1) {
                    city = queries[0];
                    state = queries[1];
                }else{
                    city = queries[0];
                    state = "";
                }
                location_text = (TextView) view.findViewById(R.id.location_text);
                location_text.setText(location);
                ApiCall.getGeo(getActivity(), city, state, new Response.Listener<String>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onResponse(String response) {
                        try {
                            RequestQueue queue = Volley.newRequestQueue(getActivity());
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
                            fab.setVisibility(View.VISIBLE);
                            spinner.setVisibility(View.GONE);
                            fetch_text.setVisibility(View.GONE);

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
            return view;
        }
    }

    StringRequest weatherRequestFunc(String lat, String lon){
        String url = "http://weather-nodejs-app.us-east-2.elasticbeanstalk.com/api/weather?lat=" + lat + "&lng=" + lon;
        StringRequest weatherRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        weatherDetail = response;
                        JSONObject root = null;
                        summary_icon = (ImageView) view.findViewById(R.id.summary_icon);
                        temperature = (TextView) view.findViewById(R.id.temperature);
                        summary_text = (TextView) view.findViewById(R.id.summary);
                        humidity_text = (TextView) view.findViewById(R.id.info_humidity);
                        windSpeed_text = (TextView) view.findViewById(R.id.info_windSpeed);
                        visibility_text = (TextView) view.findViewById(R.id.info_visibility);
                        pressure_text = (TextView) view.findViewById(R.id.info_pressure);
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
                            humidity_text.setText(humidity_percentage + "%");
                            windSpeed_text.setText(windSpeed + " mph");
                            visibility_text.setText(visibility + " km");
                            pressure_text.setText(pressure + " mb");
                            JSONObject daily = root.getJSONObject("daily");
                            JSONArray daily_data = daily.getJSONArray("data");
                            Log.d("res", "middle");
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
                                    TextView date1 = (TextView) view.findViewById(R.id.date1);
                                    date1.setText(dateStr);
                                    ImageView icon1 = (ImageView) view.findViewById(R.id.icon1);
                                    int daily_icon_res = hashMap.get(daily_icon);
                                    icon1.setImageResource(daily_icon_res);
                                    TextView lowTemp1 = (TextView) view.findViewById(R.id.lowTemp1);
                                    TextView highTemp1 = (TextView) view.findViewById(R.id.highTemp1);
                                    lowTemp1.setText(tempLowInt + "");
                                    highTemp1.setText(tempHighInt + "");
                                }
                                if(i==1){
                                    TextView date1 = (TextView) view.findViewById(R.id.date2);
                                    date1.setText(dateStr);
                                    ImageView icon1 = (ImageView) view.findViewById(R.id.icon2);
                                    int daily_icon_res = hashMap.get(daily_icon);
                                    icon1.setImageResource(daily_icon_res);
                                    TextView lowTemp1 = (TextView) view.findViewById(R.id.lowTemp2);
                                    TextView highTemp1 = (TextView) view.findViewById(R.id.highTemp2);
                                    lowTemp1.setText(tempLowInt + "");
                                    highTemp1.setText(tempHighInt + "");
                                }
                                if(i==2){
                                    TextView date1 = (TextView) view.findViewById(R.id.date3);
                                    date1.setText(dateStr);
                                    ImageView icon1 = (ImageView) view.findViewById(R.id.icon3);
                                    int daily_icon_res = hashMap.get(daily_icon);
                                    icon1.setImageResource(daily_icon_res);
                                    TextView lowTemp1 = (TextView) view.findViewById(R.id.lowTemp3);
                                    TextView highTemp1 = (TextView) view.findViewById(R.id.highTemp3);
                                    lowTemp1.setText(tempLowInt + "");
                                    highTemp1.setText(tempHighInt + "");
                                }
                                if(i==3){
                                    TextView date1 = (TextView) view.findViewById(R.id.date4);
                                    date1.setText(dateStr);
                                    ImageView icon1 = (ImageView) view.findViewById(R.id.icon4);
                                    int daily_icon_res = hashMap.get(daily_icon);
                                    icon1.setImageResource(daily_icon_res);
                                    TextView lowTemp1 = (TextView) view.findViewById(R.id.lowTemp4);
                                    TextView highTemp1 = (TextView) view.findViewById(R.id.highTemp4);
                                    lowTemp1.setText(tempLowInt + "");
                                    highTemp1.setText(tempHighInt + "");
                                }
                                if(i==4){
                                    TextView date1 = (TextView) view.findViewById(R.id.date5);
                                    date1.setText(dateStr);
                                    ImageView icon1 = (ImageView) view.findViewById(R.id.icon5);
                                    int daily_icon_res = hashMap.get(daily_icon);
                                    icon1.setImageResource(daily_icon_res);
                                    TextView lowTemp1 = (TextView) view.findViewById(R.id.lowTemp5);
                                    TextView highTemp1 = (TextView) view.findViewById(R.id.highTemp5);
                                    lowTemp1.setText(tempLowInt + "");
                                    highTemp1.setText(tempHighInt + "");
                                }
                                if(i==5){
                                    TextView date1 = (TextView) view.findViewById(R.id.date6);
                                    date1.setText(dateStr);
                                    ImageView icon1 = (ImageView) view.findViewById(R.id.icon6);
                                    int daily_icon_res = hashMap.get(daily_icon);
                                    icon1.setImageResource(daily_icon_res);
                                    TextView lowTemp1 = (TextView) view.findViewById(R.id.lowTemp6);
                                    TextView highTemp1 = (TextView) view.findViewById(R.id.highTemp6);
                                    lowTemp1.setText(tempLowInt + "");
                                    highTemp1.setText(tempHighInt + "");
                                }
                                if(i==6){
                                    TextView date1 = (TextView) view.findViewById(R.id.date7);
                                    date1.setText(dateStr);
                                    ImageView icon1 = (ImageView) view.findViewById(R.id.icon7);
                                    int daily_icon_res = hashMap.get(daily_icon);
                                    icon1.setImageResource(daily_icon_res);
                                    TextView lowTemp1 = (TextView) view.findViewById(R.id.lowTemp7);
                                    TextView highTemp1 = (TextView) view.findViewById(R.id.highTemp7);
                                    lowTemp1.setText(tempLowInt + "");
                                    highTemp1.setText(tempHighInt + "");
                                }
                                if(i==7){
                                    TextView date1 = (TextView) view.findViewById(R.id.date8);
                                    date1.setText(dateStr);
                                    ImageView icon1 = (ImageView) view.findViewById(R.id.icon8);
                                    int daily_icon_res = hashMap.get(daily_icon);
                                    icon1.setImageResource(daily_icon_res);
                                    TextView lowTemp1 = (TextView) view.findViewById(R.id.lowTemp8);
                                    TextView highTemp1 = (TextView) view.findViewById(R.id.highTemp8);
                                    lowTemp1.setText(tempLowInt + "");
                                    highTemp1.setText(tempHighInt + "");
                                }

                            }
                            spinner.setVisibility(View.GONE);
                            fetch_text.setVisibility(View.GONE);
                            cardView1.setVisibility(View.VISIBLE);
                            cardView2.setVisibility(View.VISIBLE);
                            cardView3.setVisibility(View.VISIBLE);
                            tab.setVisibility(View.VISIBLE);
                            Log.d("res", "final");
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
