package info.mingyuet.weathersearch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Detail extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] navIcons = {
            R.drawable.calendar_today,
            R.drawable.trending_up,
            R.drawable.google_photos,
    };
    private int[] navLabels = {
            R.string.today,
            R.string.weekly,
            R.string.photo,
    };
//    // another resouces array for active state for the icon
    private int[] navIconsActive = {
            R.drawable.calendar_today_active,
            R.drawable.trending_up_active,
            R.drawable.google_photos_active,
    };
    String location;
    String temperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent getIntent = getIntent();
        String weatherDetail = getIntent.getStringExtra("weatherDetail");
        final String locationDetail = getIntent.getStringExtra("locationDetail");
        try {
            JSONObject root = new JSONObject(weatherDetail);
            JSONObject currently = root.getJSONObject("currently");
            temperature = currently.getString("temperature");

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            TextView textView = (TextView)toolbar.findViewById(R.id.toolbarTextView);
            textView.setText(locationDetail);

            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    String text = "Check out " + locationDetail + "'s Weather! It is " + temperature + "° F! #CSCI571WeatherSearch";
                    try {
                        String url = URLEncoder.encode(text, "UTF-8");
                        String URL = "https://twitter.com/intent/tweet?text=" + url;
                        Uri uri = Uri.parse(URL);
                        Log.d("res", uri.toString());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), weatherDetail, locationDetail);
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        // loop through all navigation tabs
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            // inflate the Parent LinearLayout Container for the tab
            // from the layout nav_tab.xml file that we created 'R.layout.nav_tab
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.nav_tab, null);

            // get child TextView and ImageView from this layout for the icon and label
            TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
            ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);

            // set the label text by getting the actual string value by its id
            // by getting the actual resource value `getResources().getString(string_id)`
            tab_label.setText(getResources().getString(navLabels[i]));

            // set the home to be active at first
            if(i == 0) {
                tab_label.setTextColor(getResources().getColor(R.color.white));
                tab_icon.setImageResource(navIconsActive[i]);
            } else {
                tab_icon.setImageResource(navIcons[i]);
            }

            // finally publish this custom view to navigation tab
            tabLayout.getTabAt(i).setCustomView(tab);
        }
        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);

                        // 1. get the custom View you've added
                        View tabView = tab.getCustomView();

                        // get inflated children Views the icon and the label by their id
                        TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                        ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);

                        // change the label color, by getting the color resource value
                        tab_label.setTextColor(getResources().getColor(R.color.white));
                        // change the image Resource
                        // i defined all icons in an array ordered in order of tabs appearances
                        // call tab.getPosition() to get active tab index.
                        tab_icon.setImageResource(navIconsActive[tab.getPosition()]);
                    }

                    // do as the above the opposite way to reset tab when state is changed
                    // as it not the active one any more
                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        View tabView = tab.getCustomView();
                        TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                        ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);

                        // back to the black color
                        tab_label.setTextColor(getResources().getColor(R.color.dark_grey));
                        // and the icon resouce to the old black image
                        // also via array that holds the icon resources in order
                        // and get the one of this tab's position
                        tab_icon.setImageResource(navIcons[tab.getPosition()]);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.twitter_menu, menu);
        return true;
    }

}
