package info.mingyuet.weathersearch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
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
    FloatingActionButton fab;
    String weatherDetail;
    String locationDetail;
    PlansPagerAdapter adapter;
    List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_dynamic_tabs);
        Set<String> set = new HashSet<>();
        SharedPreferences sharedPreferences= getSharedPreferences("location_data", Context.MODE_PRIVATE);
        String locations_json = sharedPreferences.getString("location_json5", "[]");
        try {
            JSONArray jsonArray = new JSONArray(locations_json);
            list = new ArrayList<String>();
            for (int i = 0; i < jsonArray.length(); i++) {
                Log.d("tag",list.toString());
                list.add(jsonArray.getString(i));
            }
            TabLayout tab = (TabLayout) findViewById(R.id.tabs);
            for (int k = 0; k < list.size() + 1; k++) {
                tab.addTab(tab.newTab());
            }

            ViewPager viewPager = (ViewPager) findViewById(R.id.frameLayout);
            adapter = new PlansPagerAdapter
                    (getSupportFragmentManager(), tab.getTabCount(), list);
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(10);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
//Bonus Code : If your tab layout has more than 2 tabs then tab will scroll other wise they will take whole width of the screen
//            if (tab.getTabCount() == 2) {
//                tab.setTabMode(TabLayout.MODE_FIXED);
//            } else {
            tab.setTabMode(TabLayout.MODE_SCROLLABLE);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_area, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
        searchAutoComplete = (androidx.appcompat.widget.SearchView.SearchAutoComplete) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        // Create a new ArrayAdapter and add data to search auto complete object.
//        String dataArr[] = {"Apple" , "Amazon" , "Amd", "Microsoft", "Microwave", "MicroNews", "Intel", "Intelligence"};
        lst = new ArrayList<String>();
//        newsAdapter = new burtuAdapteris(this, android.R.layout.simple_dropdown_item_1line, lst);
//        newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, lst);
////        newsAdapter.setNotifyOnChange(true);
//        searchAutoComplete.setAdapter(newsAdapter);
//        searchAutoComplete.setThreshold(1);
        // Listen to search view item on click event.
        searchAutoComplete.setDropDownBackgroundResource(R.color.white);
        searchAutoComplete.setLinkTextColor(getResources().getColor(R.color.colorBlack));
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText("" + queryString);
//                Toast.makeText(ActionBarSearchActivity.this, "you clicked " + queryString, Toast.LENGTH_LONG).show();
            }
        });
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // Perform final Search
//                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//                        alertDialog.setMessage("Search keyword is " + query);
//                        alertDialog.show();
                        locationDetail = query;
                        String city;
                        String state;
                        String[] queries = query.split(",");
                        if (queries.length > 1) {
                            city = queries[0];
                            state = queries[1];
                        }else{
                            city = queries[0];
                            state = "";
                        }
                        Intent intent = new Intent(MainActivity.this, Searchable.class);
                        intent.putExtra("city", city);
                        intent.putExtra("state", state);
                        intent.putExtra("location", locationDetail);
                        startActivity(intent);
                        return false;

                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        // Text has changed
                        Log.d("res", newText);
                        makeApiCall(newText);
                        newsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, lst);
                        searchAutoComplete.setAdapter(newsAdapter);
                        searchAutoComplete.setThreshold(1);
                        Log.d("res", lst.toString());
//                        searchAutoComplete.showDropDown();
                        return true;
                    }
                }
        );
        return super.onCreateOptionsMenu(menu);
    }
    private void makeApiCall(String text) {
        Log.d("res",text);
        ApiCall.makeAutoComplete(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                try {
                    lst.clear();
                    JSONArray responseObject = new JSONArray(response);
                    for (int i = 0; i < 5; i++) {
                        String row = (String) responseObject.get(i);
//                        Log.d("res", row);
//                        newsAdapter.insert(row, newsAdapter.getCount());
                        newsAdapter.add(row);
                    }
                    newsAdapter.getFilter().filter(searchAutoComplete.getText(), null);
                    newsAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

}
