package com.example.pascal.bierlijst20;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Statistics extends AppCompatActivity {
    private Database helper = new Database();
    private int peoplesCount;
    public final int[] COLORFUL_COLORS = {
            Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31), Color.rgb(179, 100, 53), Color.rgb(56, 160, 234),
            Color.rgb(203, 35, 219), Color.rgb(124, 123, 59), Color.rgb(28, 201, 91),
            Color.rgb(112, 25, 10), Color.rgb(124, 16, 47), Color.rgb(147, 143, 144),
            Color.rgb(13, 2, 137), Color.rgb(100, 28, 201), Color.rgb(15, 195, 226),
            Color.rgb(106, 150, 31), Color.rgb(193, 21, 98), Color.rgb(112, 147, 204), Color.rgb(44, 44, 45)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_statistics);
        helper.FileAvailability();

        peoplesCount = helper.numberOfUsers();
        makeBoughtBeerPie();
        makeDrankMostChart();

    }

    public void makeDrankMostChart(){
        //region Reading and sorting data from the History file
        peoplesCount = helper.numberOfUsers();
        ArrayList<PieEntry> entries = new ArrayList<>();
        String[] userNames = new String[peoplesCount];
        int[] usersDrankSoMuch = new int[peoplesCount];
        for(int i = 0; i < peoplesCount;i++) {
            userNames[i] = helper.getName(i);
        }
        String directory = Environment.getExternalStorageDirectory()+"/Bierlijst Files/BierHistory.csv";
        String unsplittedData;
        ArrayList<String> rowData = new ArrayList<>();


        try {
            /**Store unsplitted Data in rows**/
            BufferedReader lezer = new BufferedReader(new FileReader(directory));
            while ((unsplittedData = lezer.readLine()) != null) {
                //sorted bij putting the first line in last array position
                rowData.add(unsplittedData);
            }
        } catch (IOException e) {
            System.out.println("Damn you need to make a History File first," +
                    " by clicking on the buttons and start drinking.");
        }
        /**Split data in collumns and add to the table**/
        try{
            for (int sort = 0; sort < rowData.size(); sort++) {

                if (rowData.get(sort).contains("Biertje")) {
                    for(int users = 0; users < peoplesCount; users++) {
                        if (rowData.get(sort).contains("," + userNames[users] + ",")) {
                            usersDrankSoMuch[users] += 1;
                        }
                    }

                }

            }
            for(int i = 0; i < peoplesCount; i++) {
                entries.add(new PieEntry(usersDrankSoMuch[i], helper.getName(i)));

            }
        }
        catch (IndexOutOfBoundsException index) {
            index.printStackTrace();
            System.out.println("----------------Something in the arrays failed.");
        }
        //endregion
        //region Draw Pie Chart
        PieChart pieChart = findViewById(R.id.PieChartOverallDrank);
        PieDataSet pieDataSet= new PieDataSet(entries, "");
        pieDataSet.setColors(COLORFUL_COLORS);
        pieDataSet.setValueTextSize(16f);
        pieDataSet.setValueFormatter(new MyFormatterThingy());
        Legend pieLegend = pieChart.getLegend();
        pieLegend.setTextSize(16f);
        pieLegend.setTextColor(Color.parseColor("#F7C0C0C0"));
        pieLegend.setDrawInside(false);
        pieLegend.setWordWrapEnabled(true);
        //pieLegend.setYOffset(50f);
        pieChart.setData(new PieData(pieDataSet));

        pieChart.setDrawEntryLabels(false);
        pieChart.setCenterText("gedronken Bier");
        pieChart.setCenterTextSize(20f);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1500);
        //endregion
    }

    public void makeBoughtBeerPie(){
        //region Add data into the entries
        peoplesCount = helper.numberOfUsers();
        ArrayList<PieEntry> entries = new ArrayList<>();
        String[] userNames = new String[peoplesCount];
        int[] usersBoughtBeer = new int[peoplesCount];
        for(int i = 0; i < peoplesCount;i++) {
            userNames[i] = helper.getName(i);
        }
        String directory = Environment.getExternalStorageDirectory()+"/Bierlijst Files/BierHistory.csv";
        String unsplittedData;
        ArrayList<String> rowData = new ArrayList<>();

        try {
            /**Store unsplitted Data in rows**/
            BufferedReader lezer = new BufferedReader(new FileReader(directory));
            while ((unsplittedData = lezer.readLine()) != null) {
                //sorted bij putting the first line in last array position
                rowData.add(unsplittedData);
            }
        } catch (IOException e) {
            System.out.println("Damn you need to make a History File first," +
                    " by clicking on the buttons and start drinking.");
        }
        /**Split data in collumns and add to the table**/
        try{
            for (int sort = 0; sort < rowData.size(); sort++) {

                if (rowData.get(sort).contains("Kratje")) {
                    for(int users = 0; users < peoplesCount; users++) {
                        if (rowData.get(sort).contains("," + userNames[users] + ",")) {
                            usersBoughtBeer[users] += 24;
                        }
                    }

                }
                else if (rowData.get(sort).contains("Extra")) {
                    for(int users = 0; users < peoplesCount; users++) {
                        if (rowData.get(sort).contains("," + userNames[users] + ",")) {
                            usersBoughtBeer[users] += Integer.parseInt(rowData.get(sort).split(",")[3].substring(6));
                        }
                    }

                }
            }
            for(int i = 0; i < peoplesCount; i++) {
                entries.add(new PieEntry(usersBoughtBeer[i], helper.getName(i)));
            }
        }
        catch (IndexOutOfBoundsException index) {
            index.printStackTrace();
            System.out.println("----------------Something in the arrays failed.");
        }
        //endregion
        //region Draw Pie Chart
        PieChart pieChart = findViewById(R.id.PieChartOverallBought);
        PieDataSet pieDataSet= new PieDataSet(entries, "");
        pieDataSet.setValueFormatter(new MyFormatterThingy());
        pieDataSet.setColors(COLORFUL_COLORS);
        pieDataSet.setValueTextSize(16f);

        Legend pieLegend = pieChart.getLegend();
        pieLegend.setEnabled(false);

        pieChart.setData(new PieData(pieDataSet));
        pieChart.setDrawEntryLabels(false);
        pieChart.setCenterText("Bier gekocht");
        pieChart.setCenterTextSize(20f);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1500);
        //endregion
    }

}
