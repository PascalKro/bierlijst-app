package com.example.pascal.bierlijst20;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import java.util.ArrayList;


/******************************TO DO's****************************************\
 *  new Features:
 *  new sounds for button presses
 *  put every string in resources file & check dutch Language in App & English in code
 *  check with other devices if constraints are good
 *
 *  Long Term:
 *  Setup Names in the beginning (maybe)
 *  little Tutorial in the beginning with a option to dismiss and don't remind
 *  Language Packs
 *  different colors for each button (personalized)
\*****************************************************************************/

public class MainActivity extends AppCompatActivity {

    public static String EXTRA_INT = "totalBier";
    private int permission, peoplesCount;
    private boolean mute = false; // has to be global for it to stay muted or unmuted
    private SharedPreferences namesList;
    private Database helper = new Database();
    private ArrayList <Button> nameButtons = new ArrayList<>();

    //region SETUP Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
           Manifest.permission.READ_EXTERNAL_STORAGE,
           Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public  void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED){

                // We don't have permission so prompt the user

                ActivityCompat.requestPermissions(
                        activity,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );

        }
    }



    //endregion
    //region SETUP Layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("BIERLIJST");

        //region Storage Permission stuff
        verifyStoragePermissions(this);
        Boolean isAnswered = true;

        while(isAnswered == true){

            if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                isAnswered = false;
                System.out.println("Finally I can get out");
            }
            else{}
            System.out.println("In the Loop waiting");
        }
        try {
            synchronized (this) {
                this.wait(100);
            }
        }
        catch(InterruptedException e){
            System.out.println("Something with the Permissions and the time fucked up.");
        }
        //endregion

        // Check & update everything in advance (Color, Bier, Names)
        checkEverything();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //When coming back from a other view check & update again
        checkEverything();
    }
    //endregion
    //region SETUP Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.Statistics_menu)
        {
            Intent intent = new Intent(this, Statistics.class);
            startActivity(intent);
            return true;
        }
        //region History Menu
        else if (id == R.id.History_menu) {
            Intent intent = new Intent(this, HistoryWindows.class);
            startActivity(intent);
            return true;
        }
        //endregion
        //region Settings
        else if (id == R.id.Settings_menu) {
            Intent intent = new Intent(this, Settings.class);
            startActivityForResult(intent,1);
            return true;
        }
        //endregion
        //region Mute Menu
        else if (id == R.id.Mute_menu) {
            namesList = getApplicationContext().getSharedPreferences("strings",MODE_PRIVATE);
            AudioManager audio = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

            if(mute == false){
                audio.setStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_MUTE, 0);
                mute = true;
                item.setTitle(namesList.getString("UnMute_action","UnMute"));
            }
            else{

                audio.setStreamVolume(AudioManager.STREAM_MUSIC,8, 0);
                mute = false;
                item.setTitle(namesList.getString("Mute_action","Mute"));
            }
            return true;
        }
        //endregion

        return super.onOptionsItemSelected(item);
    }
    //endregion

    public void buttonPress(View view){
        /*Start-Up next Windows*/
        Intent intent = new Intent(this, FurtherProcess.class);
        int whichPerson = view.getId();
        whichPerson--;
        intent.putExtra(EXTRA_INT, whichPerson);
        startActivityForResult(intent,0);

    }

    public void checkEverything(){
        helper.FileAvailability();
        peoplesCount = helper.numberOfUsers();
        helper.popUP(helper.getBalance(peoplesCount), this);
        checkButtons();
        checkColors();
        checkNames();
        checkBierAmount();
    }

    public void checkBierAmount() {
        TextView PilsDisplay = findViewById(R.id.pilsVoorraad);
        PilsDisplay.setText("Pils Voorraad: " + helper.getBalance(peoplesCount));
        for(int counter = 0; counter < peoplesCount; counter++) {
            PilsDisplay = findViewById(40 + counter);
            PilsDisplay.setText(helper.getBalance(counter) + " biertjes");
        }
    }

    public void checkColors() {
        //New Method of coloring without losing Background
        Button current;
        LinearLayout row;
        TextView colorofBierText = findViewById(R.id.pilsVoorraad);
        colorofBierText.setTextColor(Color.parseColor(getSharedPreferences("colors",MODE_PRIVATE).getString("current_title","#ffffff")));
        for(int counterID = 0; counterID < peoplesCount; counterID++){
            //BUTTON
            current = findViewById(counterID);
            current.setBackgroundResource(R.drawable.mainbuttons2);
            GradientDrawable color =  (GradientDrawable) current.getBackground();
            color.setColor(Color.parseColor(getSharedPreferences("colors",MODE_PRIVATE).getString("current_buttonsLight","#ff8800")));
            current.setTextColor(Color.parseColor(getSharedPreferences("colors",MODE_PRIVATE).getString("current_text","#ffffff")));
            //ROW
            row = findViewById(20+counterID);
            row.setBackground(getDrawable(R.drawable.mainbuttons));
            GradientDrawable rowColor =  (GradientDrawable) row.getBackground();
            rowColor.setColor(Color.parseColor(getSharedPreferences("colors",MODE_PRIVATE).getString("current_buttonsDark","#FFA13C")));

            //BierText
            colorofBierText = findViewById(40+counterID);
            colorofBierText.setTextColor(Color.parseColor(getSharedPreferences("colors",MODE_PRIVATE).getString("current_text","#ffffff")));
        }
       }

    public void checkNames(){
        TextView namesUpdate;
        for(int counting = 0; counting < peoplesCount; counting++){
            namesUpdate = findViewById(counting);
            namesUpdate.setText(helper.getName(counting));
        }
    }

    public void checkButtons() {
        LinearLayout layout = findViewById(R.id.ButtonsContainer);
        layout.removeAllViews();
        for (int checkingCounter = 0; checkingCounter < helper.numberOfUsers(); checkingCounter++) {
            Button checkings = findViewById(checkingCounter);
            if (checkings == null) {
                setUpButton(checkingCounter);
            }

        }
    }

    public void setUpButton(final int countedID){
        //region Preparation

        /************************************\
         * Button ID = 0  + counter
         * Row ID    = 20 + counter
         * BierText  = 40 + counter
        \************************************/
        int heightDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,90, getResources().getDisplayMetrics());
        int betweenSpaceDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20, getResources().getDisplayMetrics());
       LinearLayout.LayoutParams lowWeight = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1
        );

        LinearLayout.LayoutParams highWeight = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                7
        );
        Button newButton = new Button(this);
        TextView bierText = new TextView(this);
        LinearLayout row = new LinearLayout(this);
        LinearLayout Parent = findViewById(R.id.ButtonsContainer);
        Parent.setOrientation(LinearLayout.VERTICAL);
        //endregion

        //Row ATTRIBUTES
        row.setId(20+countedID);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setBackground(getDrawable(R.drawable.mainbuttons));
        GradientDrawable rowcolor =  (GradientDrawable) row.getBackground();
        rowcolor.setColor(Color.parseColor(getSharedPreferences("colors",MODE_PRIVATE).getString("current_buttons","#ff8800")));
        bierText.setOnClickListener(new View.OnClickListener()
        {   @Override
            public void onClick(View whichButton) {
                    Button clickButton = findViewById(countedID);
                    clickButton.callOnClick();
                }});
        //Button TEXT
        newButton.setId(countedID);
        newButton.setText(helper.getName(countedID));
        newButton.setTextColor(Color.parseColor(getSharedPreferences("colors",MODE_PRIVATE).getString("current_text","#ffffff")));
        newButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
        //Button LOOK
        newButton.setLayoutParams(highWeight);
        newButton.setBackground(getDrawable(R.drawable.mainbuttons2));
        GradientDrawable buttonColor =  (GradientDrawable) newButton.getBackground();
        buttonColor.setColor(Color.parseColor(getSharedPreferences("colors",MODE_PRIVATE).getString("current_buttons2","#FFA13C")));
        newButton.setHeight(heightDP);
        //Button FUNCTION
        newButton.setOnClickListener(new View.OnClickListener()
        {   @Override
            public void onClick(View whichButton) {
            buttonPress(whichButton);}});
        row.addView(newButton);

        //Bier Text
        bierText.setId(40+countedID);
        bierText.setLayoutParams(lowWeight);
        bierText.setTextColor(Color.parseColor(getSharedPreferences("colors",MODE_PRIVATE).getString("current_text","#ffffff")));
        bierText.setPadding(50,0,0,0);
        bierText.setText(helper.getBalance(countedID)+" biertjes");

        row.addView(bierText);

        Space betweenSpace = new Space(this);
        betweenSpace.setLayoutParams(new LinearLayout.LayoutParams(0,betweenSpaceDP));
        betweenSpace.setId(60+countedID);

        Parent.addView(row);
        Parent.addView(betweenSpace);
    }
}
