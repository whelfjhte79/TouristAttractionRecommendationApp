package com.example.touristattraction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchPage extends Activity {
    Button btn_search_start, btn_prev;
    EditText search_plain_text;
    private static String IP_ADDRESS = "192.168.0.16/";
    private static String TAG = "search page";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        btn_search_start = findViewById(R.id.search_start_button);
        btn_prev = findViewById(R.id.search_page_prev_button);
        search_plain_text = findViewById(R.id.search_plain_text);
        btn_search_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search_data= search_plain_text.getText().toString();

                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/search.php", search_data);
                Log.d("qq",search_data);
                Intent intent = new Intent(getApplicationContext(),SearchResultPage.class);
                intent.putExtra("search",search_data);
                startActivity(intent);
            }
        });
        btn_prev.setOnClickListener(new View.OnClickListener(){
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

            progressDialog = ProgressDialog.show(SearchPage.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            String result2 = result.toString();


        }

        @Override
        protected String doInBackground(String... params) {
            String search_data = (String) params[1];
            String serverURL = (String) params[0];
            serverURL = serverURL + "?" + "search_data=" + search_data;
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
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
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
}