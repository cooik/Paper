package com.example.sony.client;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sony.client.models.Paper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment implements View.OnClickListener {
    private Button btn_user;
    private Button btn_rearch;
    private FloatingActionButton fab;
    private EditText et_rearch;
    private RecyclerView recyclerView;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private PaperAdapter mAdapter;
    private Context context;
    private Boolean isRearch = false;
    private String key;
    List<Paper> data;
    public ContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_content,container,false);
        context = getActivity().getApplicationContext();
        initView(view);
        new AsyncLoadData().execute();
        return view;
    }


    private void initView(View view) {
        btn_user = (Button) view.findViewById(R.id.tv_user);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        btn_rearch = (Button) view.findViewById(R.id.bt_rearch);
        et_rearch = (EditText) view.findViewById(R.id.et_rearch);
        btn_rearch.setOnClickListener(this);
        btn_user.setOnClickListener(this);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_user:
                Fragment fragment = new ProfileFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_frame,fragment);
                ft.commit();
                break;
            case R.id.bt_rearch:
                key = et_rearch.getText().toString();
                if(key!=""){
                    isRearch = true;
                    new AsyncLoadData().execute(key);
                }else{
                    isRearch = false;
                    Toast.makeText(context,"Empty!",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fab:
                uploadPaper();


        }
    }

    private void uploadPaper() {
        Intent intent = new Intent(context, UpPaperActivity.class);
        startActivity(intent);
    }

    private class AsyncLoadData extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String urlstr;
           if(isRearch){
                urlstr = Constants.BASE_URL+"/server/search.php?search="+key;
                isRearch = false;
            }else{
               urlstr= Constants.BASE_URL+"/server";
           }

            try {
                url = new URL(urlstr);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                try {
                    conn = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
             data = new ArrayList<>();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    Paper paper = new Paper();
                    paper.setAuthor(json_data.getString("author"));
                    paper.setTitle(json_data.getString("title"));
                    paper.setDescription(json_data.getString("description"));
                    paper.setCreated_at(json_data.getString("created_at"));
                    paper.setFile_name(json_data.getString("file_name"));
                    paper.setFile_type(json_data.getString("file_type"));
                    data.add(paper);
                }

                // Setup and Handover data to recyclerview
                mAdapter = new PaperAdapter(context,data);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                mAdapter.SetOnItemClickListener(new PaperAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent=new Intent(context,PaperDetailActivity.class);
                        intent.putExtra("paper",new Gson().toJson(data.get(position)));
                        startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
