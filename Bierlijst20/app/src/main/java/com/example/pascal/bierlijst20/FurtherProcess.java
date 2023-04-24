package com.example.pascal.bierlijst20;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

public class FurtherProcess extends AppCompatActivity {

    //region SETUP
    private int personID,lastAction = 0,whatIsShown = 0, peoplesCount;
    private String userName;
    private MediaPlayer bier, krat, neen;
    private Database helper = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //SETUP between main activity and this
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_further_process);
        Intent intent = getIntent();

        //getting the info which person is selected
        personID = intent.getIntExtra(MainActivity.EXTRA_INT,0)+1;

        helper.FileAvailability();
        peoplesCount = helper.numberOfUsers();
        userName = helper.getName(personID);
        TextView nameShower = findViewById(R.id.nameShow);
        nameShower.setText("Hallo " + userName);


        bier = new MediaPlayer().create(this, R.raw.bier);
        krat = new MediaPlayer().create(this, R.raw.krat);
        neen = new MediaPlayer().create(this, R.raw.tochniet);

        //Check & update everything in advance (Color, Bier, Balance)
        actionAndBalance(5); //5 is alleen show balance
        checkColors();
        helper.popUP(helper.getBalance(peoplesCount),this);
        showBarChart(whatIsShown);
    }

    @Override
    protected void onStop() {
        super.onStop();
        krat.release();
        neen.release();
        bier.release();
        krat = null;
        neen = null;
        bier = null;
    }
    //endregion

    //region  Action Choices
    public void takeBeer(View view) {
        if(helper.getBalance(helper.numberOfUsers())>0){
            bier.seekTo(400);
            bier.start();
            actionAndBalance(-1);
            browserHistory(-1);
            lastAction = -1;
            showBarChart(whatIsShown);
        }
        else{
            helper.popUP("HALT STOP! Er is geen bier meer! Je moet er eerst ff wat bij kopen voordat je verder zuipt.",this);
        }
    }
    public void buyCrate(View view) {
        krat.seekTo(800);
        krat.start();
        actionAndBalance(24);
        browserHistory(24);
        lastAction = 24;
        showBarChart(whatIsShown);
    }
    public void unDo(View view) {
        if(lastAction != 0) {
            neen.seekTo(600);
            neen.start();
            actionAndBalance(-lastAction);
            browserHistory(1);
            lastAction = 0;
            showBarChart(whatIsShown);
        }
        else{
            helper.popUP("Hoo maar!\nJe kan alleen je laatste actie ongedaan maken", this);
        }
    }
//endregion

    public void actionAndBalance(int aktie) {
        if (aktie != 5) {

            /**********Edit Balance, etc.***********/
            helper.saveBalance(personID,aktie);
            /************Show Balance************/
            int NewcurrentBalance = helper.getBalance(personID);
            TextView textView = findViewById(R.id.balance_Box);
            textView.setText("Mijn Bier Balans: " + NewcurrentBalance);

        }
        else {
            /******Show Balance for Beginning******/
            TextView textView = findViewById(R.id.balance_Box);
            textView.setText("Mijn Bier Balans: " + helper.getBalance(personID));
        }
    }

    public void browserHistory(int opslagAktie) {
        //region SETUP for Saving

        String FILENAME = "BierHistory.csv";
        File directory = new File(Environment.getExternalStorageDirectory() +
                                            File.separator + "Bierlijst Files");

        String action;
        if (opslagAktie == -1) {
            action = "Biertje";
        } else if (opslagAktie == 24) {
            action = "Kratje";
        } else {
            action = "Ongedaan";
        }
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String currentTime = dateFormat.format(Calendar.getInstance().getTime());
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        int currentBalance = helper.getBalance(personID);
        int totalBier = helper.getBalance(peoplesCount);
        String newEntry = "";
        //endregion
        //Data assembly
            newEntry =  totalBier+ "," + date + "," + currentTime + "," + action + "," + userName + "," + currentBalance + "\n";

        //region File zoeken & Opslaan
        try {
            File browserHistoryFile = new File(directory, FILENAME);
            FileOutputStream stream = new FileOutputStream(browserHistoryFile, true);
            if (!browserHistoryFile.exists()) {
                directory.mkdirs();
                directory.createNewFile();
            }
            stream.write(newEntry.getBytes());
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //endregion
    }

    public void checkColors() {
        Button current = findViewById(R.id.bierButton);
        current.setBackgroundResource(R.drawable.furtherbuttons);
        GradientDrawable color = (GradientDrawable) current.getBackground();
        color.setColor(Color.parseColor(getSharedPreferences("colors", MODE_PRIVATE).getString("current_buttonsDark", "#ff8800")));
        current.setTextColor(Color.parseColor(getSharedPreferences("colors", MODE_PRIVATE).getString("current_text", "#ffffff")));

        current = findViewById(R.id.kratButton);
        current.setBackgroundResource(R.drawable.furtherbuttons);
        color = (GradientDrawable) current.getBackground();
        color.setColor(Color.parseColor(getSharedPreferences("colors", MODE_PRIVATE).getString("current_buttonsDark", "#ff8800")));
        current.setTextColor(Color.parseColor(getSharedPreferences("colors", MODE_PRIVATE).getString("current_text", "#ffffff")));

        current = findViewById(R.id.undoButton);
        current.setBackgroundResource(R.drawable.furtherbuttons);
        color = (GradientDrawable) current.getBackground();
        color.setColor(Color.parseColor(getSharedPreferences("colors", MODE_PRIVATE).getString("current_buttonsLight", "#FFA13C")));
        current.setTextColor(Color.parseColor(getSharedPreferences("colors", MODE_PRIVATE).getString("current_text", "#ffffff")));


         }

    public void showBarChart(int whichChart){
        String directory = Environment.getExternalStorageDirectory()+"/Bierlijst Files/BierHistory.csv";
        int maximumBars = 31;
        String currentMonth = "", unsplittedData = "";
        float[] monthBalances = new float[maximumBars];
        ArrayList<String> rowData = new ArrayList<>();
        ArrayList<String> personData = new ArrayList<>();

        //region Reading and sorting data from one Person
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
        for (int sort = 0; sort < rowData.size(); sort++) {
            if (rowData.get(sort).contains("," + userName + ",")) {
                personData.add(rowData.get(sort));
            }
        }
        //endregion
        //region Sort Data according to the Viewing option
        if(whichChart == 0) {
            try {
                currentMonth = rowData.get(rowData.size()-1).split(",")[1].substring(0, 8);
                for (int days = 0; days < maximumBars; days++) {
                    for (int historycounter = 0; historycounter <= personData.size() - 1; historycounter++) {
                        if (days < 10) {
                            if (personData.get(historycounter).contains(currentMonth + "0" + days)) {
                                monthBalances[days] = Integer.parseInt(personData.get(historycounter).split(",")[5]);

                            }
                        }
                        else {
                            if (personData.get(historycounter).contains(currentMonth + days)) {
                                monthBalances[days] = Integer.parseInt(personData.get(historycounter).split(",")[5]);

                            }
                        }
                    }
                }
            } catch (IndexOutOfBoundsException index) {
                index.printStackTrace();
                System.out.println("----------------Something in the arrays failed.");
            }
        }
        else{
            try {
                currentMonth = rowData.get(rowData.size()-1).split(",")[1].substring(0, 8);
                for (int days = 0; days < maximumBars; days++) {
                    monthBalances[days] = 0;
                    for (int historycounter = 0; historycounter <= personData.size() - 1; historycounter++) {
                        if (days < 10) {
                            if (personData.get(historycounter).contains(currentMonth + "0" + days)) {
                                if (personData.get(historycounter).contains("Biertje")) {
                                    monthBalances[days] += -1;
                                }
                                else if(personData.get(historycounter).contains("Kratje")) {
                                    monthBalances[days] += 24;
                                }
                                else if(personData.get(historycounter).contains("Extra")){
                                    monthBalances[days] += Integer.parseInt(personData.get(historycounter).split(",")[3].substring(6));
                                }
                            }
                        } else {
                            if (personData.get(historycounter).contains(currentMonth + days)) {
                                if (personData.get(historycounter).contains("Biertje")) {
                                    monthBalances[days] += -1;
                                }
                                else if(personData.get(historycounter).contains("Kratje")) {
                                    monthBalances[days] += 24;
                                }
                                else if(personData.get(historycounter).contains("Extra")){
                                    monthBalances[days] += Integer.parseInt(personData.get(historycounter).split(",")[3].substring(6));
                                }
                            }
                        }
                    }
                }
            } catch (IndexOutOfBoundsException index) {
                index.printStackTrace();
                System.out.println("----------------Something in the arrays failed.");
            }
        }
        //endregion
        //region Draw Bar Chart
        BarChart chart;
        ArrayList<BarEntry> BARENTRY ;

        BarDataSet Bardataset ;
        BarData BARDATA ;
        chart = findViewById(R.id.PieChartOverall);
        BARENTRY = new ArrayList<>();

        for(int counter = 0; counter < maximumBars; counter++){
            BARENTRY.add(new BarEntry(counter, monthBalances[counter]));
        }
        Bardataset = new BarDataSet(BARENTRY, "");
        BARDATA = new BarData(Bardataset);
        chart.setData(BARDATA);
        Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setDrawValueAboveBar(true);
        chart.setMaxVisibleValueCount(31);
        chart.getAxisLeft().setTextColor(Color.parseColor( "#ffffff"));
        chart.getXAxis().setTextColor(Color.parseColor(    "#ffffff"));
        chart.getAxisRight().setTextColor(Color.parseColor("#ffffff"));
        Legend leg = chart.getLegend();
        leg.setEnabled(false);
        if(whichChart == 0) {
            chart.getDescription().setText("Balans");
            chart.getDescription().setTextSize(14f);
            chart.getDescription().setTextColor(Color.parseColor("#ffffff"));
        }
        else{
            chart.getDescription().setText("Activiteit");
            chart.getDescription().setTextSize(14f);
            chart.getDescription().setTextColor(Color.parseColor("#ffffff"));
        }

        chart.animateY(1500);
        //endregion
    }

    public void clickOnBarChart(View view){
        if(whatIsShown == 0) {
            whatIsShown = 1;
        }
        else{
            whatIsShown = 0;
        }
        showBarChart(whatIsShown);
    }
}