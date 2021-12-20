package com.example.touristattraction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;
public class SignUpPage extends Activity {
    private static String IP_ADDRESS = "192.168.0.16/";
    private static String TAG = "tourist attraction";

    private EditText mTextName;
    private EditText mTextID;
    private EditText mTextPassword;
    static int mTextUserCode = 10;
    private TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);

        mTextName = (EditText)findViewById(R.id.sign_up_text_person_name);
        mTextID = (EditText)findViewById(R.id.sign_up_text_person_id);
        mTextPassword = (EditText)findViewById(R.id.sign_up_text_person_password);
        mTextViewResult = (TextView)findViewById(R.id.textview_result);

        Button signUpButton = (Button)findViewById(R.id.sign_up_page_sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String userName = mTextName.getText().toString();
                String userId = mTextID.getText().toString();
                String userPassword = mTextPassword.getText().toString();
                String userCode = Integer.toString(mTextUserCode++);

                try {
                    InsertData task = new InsertData();
                    task.execute("http://" + IP_ADDRESS + "insert.php", userName, userId, userPassword, userCode);

                    Intent intent = new Intent(getApplicationContext(),InitScreen.class);
                    startActivity(intent);
                }
                catch (Exception e){
                    Log.d(TAG, "insert.php에서 삽입안됨 ", e);
                }
                mTextName.setText("");
                mTextID.setText("");
                mTextPassword.setText("");

            }   
        });
        Button cancelButton = (Button)findViewById(R.id.sign_up_page_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    class InsertData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SignUpPage.this,
                    "Please Wait", null, true, true);

            Log.d(TAG, "onPreExecute response  - ");
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);
        }
        @Override
        protected String doInBackground(String... params) {
            String userName = (String)params[1];
            String userID = (String)params[2];
            String userPassword = (String)params[3];
            String userCode = (String)params[4];
            String serverURL = (String)params[0];
            String postParameters = "user_name=" + userName + "&user_id=" + userID + "&user_password=" + userPassword + "&user_code=" + userCode;

            try{
                URL url = new URL(serverURL);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);

                httpURLConnection.setConnectTimeout(5000);

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));

                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

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
                String line = "오류";

                while((line = bufferedReader.readLine()) != null){
                    Log.d(TAG,"버퍼리더동작" + line);
                    sb.append(line);
                }


                bufferedReader.close();
                return sb.toString();
            }
            catch (Exception e){
                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
}
