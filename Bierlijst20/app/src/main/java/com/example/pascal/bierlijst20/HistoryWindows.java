package com.example.pascal.bierlijst20;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HistoryWindows extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //region SETUP

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //setTitle("Geschiedenis");
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_history_windows);
        TableLayout table = findViewById(R.id.myTableLayout);
        String directory = Environment.getExternalStorageDirectory()+"/Bierlijst Files/BierHistory.csv";
        String[] rowData;
        String unsplittedData = "", useless = "";
        int row_amount = 0;
        //endregion

        //region Reading file for the amount of Rows
        int counter = 0;
        try {
            BufferedReader r = new BufferedReader(new FileReader(directory));
            while((useless = r.readLine()) != null)
            {
                counter++;
            }
            row_amount = counter;
        }catch(IOException e){
            e.printStackTrace();
        }
        String[] rows_amount = new String[row_amount];
        TextView totalrows = findViewById(R.id.totalRows);
        totalrows.setText("Vermeldingen "+counter);
        //endregion

        //region Draw the Table
            try {
            /**Store unsplitted Data in rows**/
            BufferedReader lezer = new BufferedReader(new FileReader(directory));
            while((unsplittedData = lezer.readLine()) != null && (counter != 0))
            {
                //sorted bij putting the first line in last array position
                rows_amount[counter-1] = unsplittedData;
                counter--;
            }
            /**Split data in collumns and add to the table**/
            for(int sort = 0; sort < row_amount; sort++) {
                rowData = rows_amount[sort].split(",");
                TableRow tableROW = new TableRow(this);

                for(int collumn = 0; collumn < 6; collumn++) {
                    TextView text_total = new TextView(this);
                    text_total.setText(rowData[collumn]);

                    text_total.setTextColor(Color.parseColor(getSharedPreferences("colors",MODE_PRIVATE).getString("current_historyText","#802e1d")));
                    tableROW.addView(text_total, TableRow.LayoutParams.MATCH_PARENT, 60);
                }
                table.addView(tableROW, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            }
        }
            catch(IOException e){System.out.println("Damn you need to make a History File first," +
                                                    " by clicking on the buttons and start drinking."); }

        //endregion

        //region Check Colors
        TextView current = findViewById(R.id.header_Action1);
        current.setTextColor(Color.parseColor(getSharedPreferences("colors",MODE_PRIVATE).getString("current_historyText","#FFC107")));
        current = findViewById(R.id.header_Balance);
        current.setTextColor(Color.parseColor(getSharedPreferences("colors",MODE_PRIVATE).getString("current_historyText","#FFC107")));
        current = findViewById(R.id.header_Date);
        current.setTextColor(Color.parseColor(getSharedPreferences("colors",MODE_PRIVATE).getString("current_historyText","#FFC107")));
        current = findViewById(R.id.header_Person);
        current.setTextColor(Color.parseColor(getSharedPreferences("colors",MODE_PRIVATE).getString("current_historyText","#FFC107")));
        current = findViewById(R.id.header_Time);
        current.setTextColor(Color.parseColor(getSharedPreferences("colors",MODE_PRIVATE).getString("current_historyText","#FFC107")));
        current = findViewById(R.id.header_Total);
        current.setTextColor(Color.parseColor(getSharedPreferences("colors",MODE_PRIVATE).getString("current_historyText","#FFC107")));

        TableLayout tab = (TableLayout) findViewById(R.id.tableLayout);
        tab.setBackgroundColor(Color.parseColor(getSharedPreferences("colors",MODE_PRIVATE).getString("current_background","#414141")));
        ScrollView dataTab = (ScrollView) findViewById(R.id.scrollView2);
        dataTab.setBackgroundColor(Color.parseColor(getSharedPreferences("colors",MODE_PRIVATE).getString("current_background","#414141")));
        //endregion
    }
}
