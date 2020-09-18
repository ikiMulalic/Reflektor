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


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import ikbal.mulalic.reflektor.model.Report;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MojePrijaveFragment extends Fragment implements RecyclerViewMyReportsAdapter.OnItemClick {

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
        Log.d("Naser","Abdelilah");
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
        recyclerViewMyReportsAdapter = new RecyclerViewMyReportsAdapter(getContext(),listOfReports,this);
        recyclerView.setAdapter(recyclerViewMyReportsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        checkSharedReferences();

        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_upute, menu);
    }

    public void checkSharedReferences()
    {


        int i = 0;
        JSONParser jsonParser = new JSONParser();
        while (true)
        {
            String x = sharedPreferences.getString(String.valueOf(i),"");
            if(x == null || x.equals(""))
            {
                break;
            }
            try {

                JSONObject jsonObject = (JSONObject) jsonParser.parse(x);
                report = new Report();
                report.setSent((Boolean) jsonObject.get("isSent"));
                report.setCreatedAt((String) jsonObject.get("createdAt"));
                report.setCategoryOfReport((String) jsonObject.get("category"));
                report.setLocationOfReport((String) jsonObject.get("location"));
                report.setDescriptionOfReport((String) jsonObject.get("description"));
                report.setPhotoPathReport((String) jsonObject.get("imageName"));

                if (jsonObject.get("videoPath") != null) {
                    report.setVideoPathReport((String) jsonObject.get("videoName"));
                }
                if (jsonObject.get("realLocation") != null) {
                    report.setRealLocationOfReport((String) jsonObject.get("realLocation"));
                }
                report.setPosition(i);
                report.setString(x);

                listOfReports.add(report);
                i++;

            } catch (ParseException e) {
                e.printStackTrace();
            }


        }

        recyclerViewMyReportsAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.prijava_knjiga) {
            Intent secondIntent = new Intent(getActivity(),InstructionsActivity.class);
            startActivity(secondIntent);
        }
        return true;
    }

    @Override
    public void sendAgain(final int position) {

        final OkHttpClient client = new OkHttpClient();
        final JSONObject jsonObject = (JSONObject) JSONValue.parse(listOfReports.get(position).getString());
        final String url = "https://reflektor.live/reports/create";
        final MediaType JSON
                = MediaType.parse("image/png");


        new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    //Your code goes here
                    MultipartBody.Builder multiPartBody = new MultipartBody.Builder()
                            .addFormDataPart("report", jsonObject.toJSONString());
                    if( jsonObject.get("imageName")!= null && !((String)jsonObject.get("imageName")).equals("null"))
                    {
                        multiPartBody.addFormDataPart("Content-Type", "image/png")
                                .addFormDataPart("image", UUID.randomUUID().toString() + ".png",
                                        RequestBody.create(JSON, new File((String) jsonObject.get("imageName"))));
                    }
                    if ( jsonObject.get("videoName")!= null && !((String)jsonObject.get("videoName")).equals("null"))

                    {
                        multiPartBody.addFormDataPart("Content-Type", "video/mp4")
                                .addFormDataPart("video",UUID.randomUUID().toString() + ".mp4",RequestBody.create(JSON, new File((String) jsonObject.get("videoName"))));
                    }

                    RequestBody requestBody = multiPartBody.build();

                    Request request = new Request.Builder()
                            .url(url)
                            .header("Content-Type", "multipart/form-data;")
                            .post(requestBody)
                            .build();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Slanje u toku...", Toast.LENGTH_SHORT).show();

                        }
                    });
                    try  {
                        Response response = client.newCall(request).execute();
                        if (!response.isSuccessful()) {
                            jsonObject.put("isSent",false);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Doslo je do greske, prijava je sacuvana u \"Moje prijave\"", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                        {
                            jsonObject.put("isSent",true);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Uspjesno kreirana prijava", Toast.LENGTH_SHORT).show();
                                    editor.putString(String.valueOf(listOfReports.get(position).getPosition()), jsonObject.toJSONString());
                                    editor.apply();
                                    listOfReports.get(position).setSent(true);
                                    recyclerViewMyReportsAdapter.notifyDataSetChanged();
                                }
                            });
                        }

                    }
                    catch (IOException e)
                    {
                        jsonObject.put("isSent",false);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Doslo je do greske, prijava je sacuvana u \"Moje prijave\"", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }
}