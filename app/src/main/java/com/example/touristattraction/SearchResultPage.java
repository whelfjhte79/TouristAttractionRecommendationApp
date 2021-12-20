package com.example.touristattraction;

import android.app.Activity;
import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SearchResultPage extends Activity {
    Button btn_home, btn_prev;
    TextView text_attraction_name, text_address, text_phone_number;
    TextView text_attraction_name2, text_address2, text_phone_number2;
    ArrayList<TourInfo> tour_infos = new ArrayList<TourInfo>();
    private static String IP_ADDRESS = "192.168.0.16";
    private static String TAG = "SearchResult";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_page);

        btn_home = findViewById(R.id.home_button);
        btn_prev = findViewById(R.id.search_result_prev_button);
        text_attraction_name = findViewById(R.id.output_attraction_name_view);
        text_address = findViewById(R.id.output_address_view);
        text_phone_number = findViewById(R.id.output_phone_no_view);
        text_attraction_name2 = findViewById(R.id.output_attraction_name_view2);
        text_address2 = findViewById(R.id.output_address_view2);
        text_phone_number2 = findViewById(R.id.output_phone_no_view2);
        Intent intent = getIntent();
        String keyword = intent.getStringExtra("search");

        InsertData task = new InsertData();
        task.execute("http://" + IP_ADDRESS + "/search.php", keyword);



        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainScreen.class);
                startActivity(intent);
            }
        });
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SearchResultPage.this,
                    "Please Wait", null, true, true);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            String tour_name,tour_address,tour_phone_number;
            Log.d("이유:",result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("search");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject item = jsonArray.getJSONObject(i);

                    tour_name = item.getString("tour_name");
                    tour_address = item.getString("tour_address");
                    tour_phone_number = item.getString("tour_phone_number");

                    TourInfo tour_info = new TourInfo(tour_name,tour_address,tour_phone_number);
                    tour_infos.add(tour_info);
                }
            } catch (JSONException e) {

                Log.d(TAG, "showResult : ", e);
            }
            text_attraction_name.setText(tour_infos.get(0).getData(0));
            text_address.setText(tour_infos.get(0).getData(1));
            text_phone_number.setText(tour_infos.get(0).getData(2));
            text_attraction_name2.setText(tour_infos.get(1).getData(0));
            text_address2.setText(tour_infos.get(1).getData(1));
            text_phone_number2.setText(tour_infos.get(1).getData(2));
        }
        @Override
        protected String doInBackground(String... params) {
            String search_data = (String)params[1];

            String serverURL = (String)params[0];
            serverURL = serverURL+"?" + "search_data=" + search_data;
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "GET response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }
    public class TourInfo {
        private String[] data;
        public TourInfo(String[] data) {
            data = data;
        }
        public TourInfo(String attraction_name, String address, String phone_number) {
            data = new String[3];
            data[0] = attraction_name;
            data[1] = address;
            data[2] = phone_number;
        }
        public String[] getData() {
            return data;
        }
        public String getData(int index) {
            return data[index];
        }
        public void setData(String[] data) {
            this.data = data;
        }
    }
}