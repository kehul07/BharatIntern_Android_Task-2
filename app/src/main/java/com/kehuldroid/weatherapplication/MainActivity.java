package com.kehuldroid.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView weatherDetails ;
    EditText cityName;
    Button btn;

    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherDetails = findViewById(R.id.weatherDetails);
        cityName = findViewById(R.id.cityName);
        btn = findViewById(R.id.btn);

        final String[] temp = {""};

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = cityName.getText().toString();
                try{
                    if(city!=null){
                        url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=40d2a185ac454e9892712b6388cc89cb";
                    }else{
                        Toast.makeText(MainActivity.this, "Enter City!!", Toast.LENGTH_SHORT).show();
                    }
                    fetchWeatherData(url);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    private void fetchWeatherData(String api) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, api, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject main = response.getJSONObject("main");
                            double temp = main.getDouble("temp");
                            double tempMin = main.getDouble("temp_min");
                            double tempMax = main.getDouble("temp_max");
                            int pressure = main.getInt("pressure");
                            int humidity = main.getInt("humidity");

                            // Convert temperatures from Kelvin to Celsius
                            temp = temp - 273.15;
                            tempMin = tempMin - 273.15;
                            tempMax = tempMax - 273.15;

                            StringBuilder sb = new StringBuilder();
                            sb.append("Weather Details");
                            sb.append("\n--------------------------");
                            sb.append("\nTemp : "+String.format("%.2f", temp)+"°C");
                            sb.append("\nMax_Temp : "+String.format("%.2f", tempMax)+"°C");
                            sb.append("\nMin_Temp : "+String.format("%.2f", tempMin)+"°C");
                            sb.append("\nHumidity : "+humidity+"%");
                            sb.append("\nPressure : "+pressure);

                            weatherDetails.setVisibility(View.VISIBLE);
                            weatherDetails.setText(sb.toString());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        queue.add(jsonObjectRequest);
    }

}