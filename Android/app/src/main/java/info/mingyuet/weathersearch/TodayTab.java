package info.mingyuet.weathersearch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TodayTab extends Fragment {

    int position;
    private TextView textView;
    String weatherDetail;
    HashMap<String, Integer> hashMap;

    public static Fragment getInstance(int position, String weatherDetail) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        bundle.putString("weatherDetail", weatherDetail);
        TodayTab tabFragment = new TodayTab();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");
        weatherDetail = getArguments().getString("weatherDetail");
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
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DecimalFormat df = new DecimalFormat("0.00");
        try {
            JSONObject root = new JSONObject(weatherDetail);
            JSONObject currently = root.getJSONObject("currently");
            String temperature = currently.getString("temperature");
            int temp = (int) Math.round(Double.parseDouble(temperature));
            String windSpeed = df.format(Double.parseDouble(currently.getString("windSpeed")));
            String pressure = df.format(Double.parseDouble(currently.getString("pressure")));
            String precipIntensity = df.format(Double.parseDouble(currently.getString("precipIntensity")));
            String humidity = currently.getString("humidity");
            int humidity_int = (int) Math.round(Double.parseDouble(humidity) * 100);
            String visibility = df.format(Double.parseDouble(currently.getString("visibility")));
            String cloudCover = currently.getString("cloudCover");
            int cloudCover_int = (int) Math.round(Double.parseDouble(cloudCover) * 100);
            String ozone = df.format(Double.parseDouble(currently.getString("ozone")));
            String icon = currently.getString("icon");
            String summary = currently.getString("summary");
            TextView temperature_text = (TextView) view.findViewById(R.id.daily_temp);
            temperature_text.setText(temp+"°F");
            TextView windSpeed_text = (TextView) view.findViewById(R.id.detail_windSpeed);
            windSpeed_text.setText(windSpeed + " mph");
            TextView pressure_text = (TextView) view.findViewById(R.id.detail_pressure);
            pressure_text.setText(pressure + " mb");
            TextView precipIntensity_text = (TextView) view.findViewById(R.id.daily_precipitation);
            precipIntensity_text.setText(precipIntensity);
            TextView humidity_text = (TextView) view.findViewById(R.id.daily_humidity);
            humidity_text.setText(humidity_int + "%");
            TextView visibility_text = (TextView) view.findViewById(R.id.daily_visibility);
            visibility_text.setText(visibility + " km");
            TextView cloudCover_text = (TextView) view.findViewById(R.id.daily_cloud);
            cloudCover_text.setText(cloudCover_int + "%");
            TextView ozone_text = (TextView) view.findViewById(R.id.daily_ozone);
            ozone_text.setText(ozone + " DU");
            TextView summary_text = (TextView) view.findViewById(R.id.daily_summary);
            summary_text.setText(summary);
            ImageView summary_icon = (ImageView) view.findViewById(R.id.daily_summary_icon);
            summary_icon.setImageResource(hashMap.get(icon));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        textView = (TextView) view.findViewById(R.id.textView);
//
//        textView.setText("Fragment " + (position + 1));

    }
}