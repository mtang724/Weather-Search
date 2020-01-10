package info.mingyuet.weathersearch;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotoTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotoTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public final class PhotoTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // These are totally arbitrary, pick sizes that are right for your UI.
    private final int imageWidthPixels = 1024;
    private final int imageHeightPixels = 768;
    // You will need to populate these urls somewhere...

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private final ArrayList<String> links = new ArrayList<String>();

    private OnFragmentInteractionListener mListener;

    public PhotoTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PhotoTab.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoTab newInstance(String cityName) {
        PhotoTab fragment = new PhotoTab();
        Bundle args = new Bundle();
        args.putString("cityName", cityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_photo, container, false);
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
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        final View view1= view;
        String cityName = getArguments().getString("cityName");
        cityName = cityName.split(",")[0];
        super.onViewCreated(view, savedInstanceState);
        final Resources resources = this.getResources();
        ApiCall.getImgs(getActivity(), cityName, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                try {
                    JSONArray responseObject = new JSONArray(response);
                    for (int i = 0; i < 8; i++) {
                        String link = (String) responseObject.get(i);
//                        Log.d("res", row);
//                        newsAdapter.insert(row, newsAdapter.getCount());
                        links.add(link);
                    }
                    Log.d("res", links.toString());
                    RecyclerView recyclerView = (RecyclerView) view1.findViewById(R.id.recycler_view);
                    recyclerView.setHasFixedSize(false);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    DisplayMetrics dm = resources.getDisplayMetrics();
                    int width = dm.widthPixels;
                    DataAdapter adapter = new DataAdapter(getActivity().getApplicationContext(), links, width);
                    recyclerView.setAdapter(adapter);
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