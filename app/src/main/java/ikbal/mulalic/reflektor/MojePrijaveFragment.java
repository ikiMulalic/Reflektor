package ikbal.mulalic.reflektor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import ikbal.mulalic.reflektor.model.Report;

public class MojePrijaveFragment extends Fragment {

    private ArrayList<Report> listOfReports;
    private View view;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Report report;
    private RecyclerView recyclerView;
    private RecyclerViewMyReportsAdapter recyclerViewMyReportsAdapter;


    public MojePrijaveFragment() {

    }

    public static MojePrijaveFragment newInstance(String param1, String param2) {
        MojePrijaveFragment fragment = new MojePrijaveFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("Ceha","Ceha majstor");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_moje_prijave, container, false);

        sharedPreferences = getActivity().getSharedPreferences("ikbal.mukakic.reflektor", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        listOfReports = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerViewMyReportsAdapter = new RecyclerViewMyReportsAdapter(getContext(),listOfReports);
        recyclerView.setAdapter(recyclerViewMyReportsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        checkSharedReferences();





        /*System.out.println(sharedPreferences.getString("0",""));
        System.out.println(sharedPreferences.getString("1",""));*/
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_upute, menu);
    }

    public void checkSharedReferences()
    {


        int i = 0;
        JSONObject jsonObject;
        JSONParser jsonParser = new JSONParser();
        while (true)
        {
            String x = sharedPreferences.getString(String.valueOf(i),"");
            System.out.println("Rifat" + x);
            if(x == null || x.equals(""))
            {
                System.out.println("Nema nista u sharedpreferences");
                break;
            }
            try {
                System.out.println("Semira");
                jsonObject = (JSONObject) jsonParser.parse(x);
                System.out.println("selma" + jsonObject);
                report = new Report();
                report.setCategoryOfReport(jsonObject.get("category").toString());
                report.setLocationOfReport(jsonObject.get("location").toString());
                report.setDescriptionOfReport(jsonObject.get("description").toString());
                report.setPhotoPathReport(jsonObject.get("photoPath").toString());
                if (jsonObject.get("videoPath").toString() != null) {
                    report.setVideoPathReport(jsonObject.get("videoPath").toString());
                }
                if (jsonObject.get("realLocation").toString() != null) {
                    report.setRealLocationOfReport(jsonObject.get("realLocation").toString());
                }
                listOfReports.add(report);
                recyclerViewMyReportsAdapter.notifyDataSetChanged();
                System.out.println(report.getCategoryOfReport());
                System.out.println(report.getLocationOfReport());
                System.out.println(report.getDescriptionOfReport());
                System.out.println(report.getPhotoPathReport());
                //System.out.println(report.getVideoPathReport());
                System.out.println(report.getRealLocationOfReport());

                i++;

            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.prijava_knjiga) {
            Toast.makeText(getActivity(), "Clicked on " + item.getTitle(), Toast.LENGTH_SHORT)
                    .show();
        }
        return true;
    }

}