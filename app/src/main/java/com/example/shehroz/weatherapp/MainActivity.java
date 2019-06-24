package com.example.shehroz.weatherapp;
// API-KEY=4e1cac5565d5a11cf14069dfaf5f5be6
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    TextView weatherTeleTV;
    EditText cityName;
    public void findWeather(View view){

        InputMethodManager mgr=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(),0);
        try {
            String city=cityName.getText().toString();
            String encodedCityName= URLEncoder.encode(city,"UTF-8");
            DownloadTask task=new DownloadTask();
            task.execute("http://api.openweathermap.org/data/2.5/weather?q="+encodedCityName+"&appid=4e1cac5565d5a11cf14069dfaf5f5be6");

        }catch (Exception e){
               Toast.makeText(getApplicationContext(),"Could not find the Weather",Toast.LENGTH_LONG);
        }
        }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherTeleTV=(TextView)findViewById(R.id.weatherTeleId);
        cityName=(EditText)findViewById(R.id.cityName);
    }
    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection httpURLConnection= null;




            try {
                url=new URL(urls[0]);
                httpURLConnection=(HttpURLConnection) url.openConnection();
                InputStream in=httpURLConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while (data!=-1){

                    char current =(char) data;
                    result+=current;
                    data=reader.read();
                }
                return result;

            } catch (MalformedURLException e) {
                Toast.makeText(getApplicationContext(),"Could not find the Weather",Toast.LENGTH_LONG);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(),"Could not find the Weather",Toast.LENGTH_LONG);

            }

            return null;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                String msg="";

                JSONObject jsonObject=new JSONObject(result);
                String mainTemp=jsonObject.getString("main");
                String weatherInfo=jsonObject.getString("weather");

                JSONArray jsonArray2=new JSONArray(mainTemp);
                for(int i=0;i<jsonArray2.length();++i){

                    JSONObject jsonPart= jsonArray2.getJSONObject(i);

                    String temp="";
                    temp=jsonPart.getString("temp");

                   msg+=temp;
                }
                JSONArray jsonArray=new JSONArray(weatherInfo);
                for(int i=0;i<jsonArray.length();++i){

                    JSONObject jsonPart= jsonArray.getJSONObject(i);
                    String main="";
                    String description="";

                    main=jsonPart.getString("main");
                    description=jsonPart.getString("description");
                    Log.i("description",jsonPart.getString("description"));

                    if(description!=""&&main!=""){

                        msg+=main+" : "+description+mainTemp+"\r\n";

                    }
                }

                if (msg!=""){

                    weatherTeleTV.setText(msg);
                }else {
                    Toast.makeText(getApplicationContext(), "Could not find the Weather", Toast.LENGTH_LONG);
                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(),"Could not find the Weather",Toast.LENGTH_LONG);

            }


        }
    }
}
