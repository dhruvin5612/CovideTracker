package com.example.covidecasetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidecasetracker.api.ApiInterface;
import com.example.covidecasetracker.api.ApiUtilities;
import com.example.covidecasetracker.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView totalConform,totalRecovered,totalActive,totalDeath,totalTest, date;
    private TextView todayConform, todayActive, todayDeath,todayTest, todayRecovered, cname;


    private PieChart pieChart;

    private List<CountryData> list;

    String country = "India";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
        if(getIntent().getStringExtra("country") != null){
            country = getIntent().getStringExtra("country");
        }

        init();


   cname = findViewById(R.id.cname);
   cname.setText(country);
        cname.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CountryActive.class)));

//





        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                        assert response.body() != null;
                        list.addAll(response.body());

                        for(int i =0; i<list.size(); i++){
                            if(list.get(i).getCountry().equals(country)){
                                int conform = Integer.parseInt(list.get(i).getCases());
                                int active = Integer.parseInt(list.get(i).getActive());
                                int recovered = Integer.parseInt(list.get(i).getRecovered());
                                int death = Integer.parseInt(list.get(i).getDeaths());

                                totalActive.setText(NumberFormat.getInstance().format(active));
                                totalConform.setText(NumberFormat.getInstance().format(conform));
                                totalDeath.setText(NumberFormat.getInstance().format(death));
                                totalRecovered.setText(NumberFormat.getInstance().format(recovered));

                                todayDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                                todayConform.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                                todayRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                                totalTest.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));

                                setText(list.get(i).getUpdated());

                                pieChart.addPieSlice(new PieModel("Conform", conform, getResources().getColor(R.color.yellow)));
                                pieChart.addPieSlice(new PieModel("Active", active, getResources().getColor(R.color.blue_pie)));
                                pieChart.addPieSlice(new PieModel("Recovered", recovered, getResources().getColor(R.color.green_pie)));
                                pieChart.addPieSlice(new PieModel("Death", death, getResources().getColor(R.color.red_pie)));

                                pieChart.startAnimation();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }




    //
    @SuppressLint("SetTextI18n")
    private void setText(String updated) {
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("MMM dd, yyyy");

        long milisecond  = Long.parseLong(updated);

        Calendar calnedar = Calendar.getInstance();
        calnedar.setTimeInMillis(milisecond);

        date.setText("Updated at "+format.format(calnedar.getTime()));
    }

    private void init(){
        /////////////       Total   ////////////
        totalConform = findViewById(R.id.totalConform);
        totalRecovered = findViewById(R.id.totalRecovered);
        totalActive = findViewById(R.id.totalActive);
        totalDeath = findViewById(R.id.totalDeath);
        totalTest = findViewById(R.id.totalTest);

        ////////////        Today     ////////////////
        todayConform = findViewById(R.id.todayConform);
        todayActive = findViewById(R.id.todayActive);
        todayDeath = findViewById(R.id.todayDeath);
        todayTest = findViewById(R.id.todayTest);
        todayRecovered = findViewById(R.id.todayRecovered);


        pieChart = findViewById(R.id.pieChart);
        date = findViewById(R.id.date);

    }
}