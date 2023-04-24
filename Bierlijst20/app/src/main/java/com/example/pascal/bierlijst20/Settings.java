package com.example.pascal.bierlijst20;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Settings extends AppCompatActivity {

    private Spinner colorSelection, personSelection, numberSelection,ChangeNameSelection, removeNameSelection;
    private EditText editNameField;
    private String lastPackageChosen;
    private SharedPreferences namesList;
    private Database helper = new Database();
    private int peoplesCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_settings);
        colorSelection = findViewById(R.id.spinnerColor);
        personSelection = findViewById(R.id.spinnerPerson);
        numberSelection = findViewById(R.id.spinnerNumber);
        ChangeNameSelection = findViewById(R.id.spinnerChangeFrom);
        removeNameSelection = findViewById(R.id.spinnerRemove);
        editNameField = findViewById(R.id.editName);
        helper.FileAvailability();
        peoplesCount = helper.numberOfUsers();
        updateSpinner();
        //set the current setted color package
        checkCurrentPackage();
    }

    /*********************Different Settings Options*********************/

    public void ConfirmColor(View view){
        lastPackageChosen = String.valueOf(colorSelection.getSelectedItem());

        SharedPreferences sharedPref = getSharedPreferences("colors", MODE_PRIVATE);
        SharedPreferences.Editor edi = sharedPref.edit();
        if(lastPackageChosen.compareTo("Oranje!") == 0){
            edi.putString("current_background", "#414141").apply();
            edi.putString("current_buttonsDark",    "#ff8800").apply();
            edi.putString("current_buttonsLight",   "#FFA13C").apply();
            edi.putString("current_text",       "#ffffff").apply();
            edi.putString("current_historyText","#ff8800").apply();
            edi.putString("current_title",      "#ffffff").apply();
        }
        else if(lastPackageChosen.compareTo("Ludica Geel") == 0){
            edi.putString("current_background", "#414141").apply();
            edi.putString("current_buttonsDark",    "#B8B100").apply();
            edi.putString("current_buttonsLight",   "#E7DD10").apply();
            edi.putString("current_text",       "#010d5e").apply();
            edi.putString("current_historyText","#d9d335").apply();
            edi.putString("current_title",      "#d9d335").apply();
        }
        else if(lastPackageChosen.compareTo("Saxion Groen") == 0){
            edi.putString("current_background", "#414141").apply();
            edi.putString("current_buttonsDark",    "#0A8740").apply();
            edi.putString("current_buttonsLight",   "#12b558").apply();
            edi.putString("current_text",       "#ffffff").apply();
            edi.putString("current_historyText","#12b558").apply();
            edi.putString("current_title",      "#12b558").apply();

        }
        else if(lastPackageChosen.compareTo("Aqua Blauw") == 0){
            edi.putString("current_background", "#414141").apply();
            edi.putString("current_buttonsDark",    "#0294A5").apply();
            edi.putString("current_buttonsLight",   "#06B6CA").apply();
            edi.putString("current_text",       "#ffffff").apply();
            edi.putString("current_historyText","#06B6CA").apply();
            edi.putString("current_title",      "#ffffff").apply();
        }
        else if(lastPackageChosen.compareTo("Vuur Rood") == 0){
            edi.putString("current_background", "#414141").apply();
            edi.putString("current_buttonsDark",    "#B12910").apply();
            edi.putString("current_buttonsLight",   "#F22F08").apply();
            edi.putString("current_text",       "#ffffff").apply();
            edi.putString("current_historyText","#ffffff").apply();
            edi.putString("current_title",      "#ffffff").apply();
        }
        else if(lastPackageChosen.compareTo("Militair Groen") == 0){
            edi.putString("current_background", "#414141").apply();
            edi.putString("current_buttonsDark",    "#79791C").apply();
            edi.putString("current_buttonsLight",   "#979735").apply();
            edi.putString("current_text",       "#ffffff").apply();
            edi.putString("current_historyText","#a3b254").apply();
            edi.putString("current_title",      "#ffffff").apply();
        }
        else if(lastPackageChosen.compareTo("Oreo") == 0) {
            edi.putString("current_background", "#414141").apply();
            edi.putString("current_buttonsDark",    "#d9d9d9").apply();
            edi.putString("current_buttonsLight",   "#ffffff").apply();
            edi.putString("current_text",       "#000000").apply();
            edi.putString("current_historyText","#ffffff").apply();
            edi.putString("current_title","#ffffff").apply();
        }
        edi.apply();
        namesList = getApplicationContext().getSharedPreferences("strings",MODE_PRIVATE);
        SharedPreferences.Editor choiceEditor = namesList.edit();
        choiceEditor.putString("settings_currentColorPackage", lastPackageChosen);
        choiceEditor.apply();
        this.finish();
    }

    public void checkCurrentPackage(){
        namesList = getApplicationContext().getSharedPreferences("strings",MODE_PRIVATE);
        lastPackageChosen = namesList.getString("settings_currentColorPackage", "Oranje!");

        if(lastPackageChosen.compareTo("Oranje!") == 0){
            colorSelection.setSelection(0);
        }
        else if(lastPackageChosen.compareTo("Ludica Geel") == 0){
            colorSelection.setSelection(1);
        }
        else if(lastPackageChosen.compareTo("Saxion Groen") == 0){
            colorSelection.setSelection(2);

        }
        else if(lastPackageChosen.compareTo("Aqua Blauw") == 0){
            colorSelection.setSelection(3);
        }
        else if(lastPackageChosen.compareTo("Vuur Rood") == 0){
            colorSelection.setSelection(4);
        }
        else if(lastPackageChosen.compareTo("Militair Groen") == 0){
            colorSelection.setSelection(5);
        }
        else if(lastPackageChosen.compareTo("Oreo") == 0) {
            colorSelection.setSelection(6);
        }

    }

    public void ConfirmExtra(View view) {
        int chosenPerson = personSelection.getSelectedItemPosition();
        int chosenNumber    = Integer.valueOf(numberSelection.getSelectedItemPosition())+1;
        helper.saveBalance(chosenPerson,chosenNumber);

        helper.popUP("Lekker Man!\n"+chosenNumber+" extra biertjes van "+helper.getName(chosenPerson)+" erbij.", this);

        browserHistory(chosenPerson, chosenNumber, "");
    }

    public void ConfirmNameChange(View view){
        int spinnerPersonChoice = ChangeNameSelection.getSelectedItemPosition();
        String ChangeToName = String.valueOf(editNameField.getText());
        String fromName = String.valueOf(ChangeNameSelection.getSelectedItem());
        if(ChangeToName.length() <= 14 && ChangeToName.length() >= 2) {
            editNameField.setText("");
            helper.saveName(spinnerPersonChoice, ChangeToName);
            updateSpinner();
            browserHistory(spinnerPersonChoice, 25, fromName);//pop up with confirmation
            helper.popUP(" Gefeliciteerd je hebt de naam successvol gewijzid.\n Nu kan je lekker verder zuipen!",this);
        }
        else if(ChangeToName.length() > 14){
            helper.popUP(" Wow wat een lange naam zeg.\n Kiez is een Bijnaam of zo.\n Die's veel te lang!",this);
        }
        else{
            helper.popUP(" Je moet me wel minstens 2 letters geven.\n Probeer't opnieuw!", this);
        }
    }

    public void ConfirmReset(View view){
        AlertDialog.Builder popUP_lowBier = new AlertDialog.Builder(this);
        String popUpMessage = "";
        File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "Bierlijst Files");
        File browserHistoryFile = new File(directory, "BierHistory.csv");
        File usernameFile = new File(directory,"Userfiles.csv");
        if ((browserHistoryFile.length()==0) || usernameFile.length() == 0) {
            popUpMessage = "Alles is al gereset.";
            popUP_lowBier.setMessage(popUpMessage)
                    .setPositiveButton("Ah wat jammer zeg!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); }}).create().show();
        }
        else{
            popUpMessage = "Weet je't echt zeker? Je kan het niet ongedaan maken, hè!?";
            popUP_lowBier.setMessage(popUpMessage)
                    .setPositiveButton("Jawohl!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //region Rename the History File for Archive
                            try {
                                File userDataFile = new File(new File(Environment.getExternalStorageDirectory() +
                                    File.separator + "Bierlijst Files"), "Userfiles.csv");
                                userDataFile.delete();
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.UK);
                                String newFileName = "BierHistoryUntil" + formatter.format(new Date()) + ".csv";

                                File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "Bierlijst Files");
                                File browserHistoryFile = new File(directory, "BierHistory.csv");
                                File browserHistoryFileOld = new File(directory, newFileName);
                                if (!browserHistoryFile.exists()) {

                                    directory.mkdirs();
                                    directory.createNewFile();
                                }
                                browserHistoryFile.renameTo(browserHistoryFileOld);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //endregion

                            dialog.dismiss();
                            finish();
                        }
                    }).setNegativeButton("Nee, liever niet.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();}}).create().show();
        }
    }

    public void ConfirmAddedPerson(View view){
        peoplesCount = helper.numberOfUsers();
        int newCount = peoplesCount + 2;
        File specialDirectory = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Bierlijst Files");
        String fileName = "Userfiles.csv";
        String directory = Environment.getExternalStorageDirectory()+"/Bierlijst Files/";
        ArrayList <String>dataPeople = new ArrayList<String>(newCount);
        EditText addedName = findViewById(R.id.addPersonTextField);
        String newName = addedName.getText()+"";
        int available = 0;

        //region check availablity
        for(int i = 0; i < peoplesCount; i++){
            if(newName == helper.getName(i)){
                available = 1;
            }
            else {
                if (addedName.getText().length() < 2) {
                    available = 2;
                }
                else if(addedName.getText().length() > 13){
                    available = 3;
                }
            }
        }
        //endregion

        if(available == 0 && peoplesCount < 19) {
            try {
                /**Store unsplitted Data in rows**/
                BufferedReader reader = new BufferedReader(new FileReader(directory+fileName));
                for (int i = 0; i <= newCount; i++) {
                    if(i < peoplesCount) {
                        dataPeople.add(reader.readLine()+"\n");
                    }
                    else if(i == peoplesCount){
                        String newPerson = newName + ",0\n";
                        dataPeople.add(newPerson);
                    }
                    else{dataPeople.add(reader.readLine()+"\n");}
                }
            }
            catch (IOException e) {
                System.out.println("Damn you need to make person by going to the settings and start renaming");
            }

            //region File zoeken & Opslaan
            try {
                File userDataFile = new File(specialDirectory, fileName);
                FileOutputStream stream = new FileOutputStream(userDataFile, false);
                if (!userDataFile.exists()) {
                    specialDirectory.mkdirs();
                    specialDirectory.createNewFile();
                }
                for (int counter = 0; counter < newCount; counter++) {
                    stream.write(dataPeople.get(counter).getBytes());//person 1
                }
                stream.flush();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //endregion
            peoplesCount = helper.numberOfUsers();
            updateSpinner();
            helper.popUP("Welkom "+newName+" bij Bierlijst! Je vrienden zijn al goed bezig.\nDus, waar wacht je nog op? Gooooo!",this);

        }
        else if(peoplesCount >= 20){
            helper.popUP("Hoo maar!\nMaximaal aantal accounts (19) is al bereikt.\nProbeer eerst accounts te verwijderen of van naam te veranderen.", this);
        }
        else if(available == 1){
            helper.popUP("Oops!\nDeze naam is al vergeven.\nProbeer't opnieuw!",this);
        }
        else if(available == 2){
            helper.popUP("Oops!\nDe naam moet minimaal 2 letters bevatten.\nProbeer't opnieuw!",this);
        }
        else if(available == 3){
            helper.popUP("Oops!\nDe naam mag maximaal 14 letters bevatten.\nProbeer't opnieuw!",this);
        }
    }

    public void ConfirmDeletePerson(View view){
        peoplesCount = helper.numberOfUsers();
        int newTotalCount = peoplesCount;
        File specialDirectory = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Bierlijst Files");
        String fileName = "Userfiles.csv";
        String directory = Environment.getExternalStorageDirectory()+"/Bierlijst Files/";
        ArrayList <String>dataPeople = new ArrayList<String>(newTotalCount);
        int whichRemove = removeNameSelection.getSelectedItemPosition();
        if(helper.getBalance(whichRemove)== 0 && peoplesCount >= 3) {
            try {
                /**Store unsplitted Data in rows**/
                BufferedReader reader = new BufferedReader(new FileReader(directory + fileName));
                for (int i = 0; i <= newTotalCount; i++) {
                    if (i != whichRemove) {
                        dataPeople.add(reader.readLine() + "\n");
                        System.out.println("------------------------------------- if:"+i);
                    }
                    else{
                        reader.readLine();
                        System.out.println("------------------------------------- else:"+i);
                    }
                }
            } catch (IOException e) {
                System.out.println("Damn you need to make person by going to the settings and start renaming");
            }

            //region File zoeken & Opslaan
            try {
                File userDataFile = new File(specialDirectory, fileName);
                FileOutputStream stream = new FileOutputStream(userDataFile, false);
                if (!userDataFile.exists()) {
                    specialDirectory.mkdirs();
                    specialDirectory.createNewFile();
                }
                for (int counter = 0; counter < newTotalCount; counter++) {
                    stream.write(dataPeople.get(counter).getBytes());//person 1
                }
                stream.flush();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //endregion
            peoplesCount = helper.numberOfUsers();
            updateSpinner();
            helper.popUP("Okey, dat vind ik nou echt jammer, maar okey.\nVeel succes zonder Bierlijst.", this);
        }
        else if(peoplesCount < 3){
            helper.popUP("Hoo maar! Het minimaal (2) aantal accounts is al bereikt.\nJe wilt toch niet in je ééntje zuipen, hè!", this);
        }
        else{
            helper.popUP("Hoo maar! Je moet eerst jouw balans op Null krijgen voor dat je Bierlijst verlaat. Zie je zo weer!", this);
        }
    }

    public void browserHistory(int IDperson, int ExtraNumber, String fromName) {
        //region SETUP for Saving

        String showname = helper.getName(IDperson);
        String action = "";
        String FILENAME = "BierHistory.csv";
        File directory = new File(Environment.getExternalStorageDirectory()+
                File.separator+"Bierlijst Files");
        if(ExtraNumber < 20) {
            action = "Extra +" + ExtraNumber;
        }
        else{
            action = fromName+" naar";
        }
        Date timeDate= Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String currentTime=dateFormat.format(timeDate);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        int totalBier = helper.getBalance(peoplesCount);
        int currentBalance = helper.getBalance(IDperson);
        String newEntry;
        //endregion

        //region Data assembly
        if(ExtraNumber < 20) {
            newEntry = totalBier + "," +
                    date + "," +
                    currentTime + "," +
                    action + "," +
                    showname + "," +
                    currentBalance + "\n";
        }
        else{
            newEntry = totalBier + "," +
                    date + "," +
                    currentTime + "," +
                    action + "," +
                    showname + "," +
                    currentBalance + "\n";
        }
        //endregion

        //region File search & Safe
        try {
            File browserHistoryFile = new File(directory,FILENAME);
            FileOutputStream stream = new FileOutputStream(browserHistoryFile, true);

            if(!browserHistoryFile.exists()) {

                directory.mkdirs();
                directory.createNewFile();
            }

            stream.write(newEntry.getBytes());
            stream.flush();
            stream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //endregion
    }

    public void updateSpinner() {

        ArrayList<String> gettingArray = new ArrayList<>(peoplesCount);
        for (int i = 0; i < peoplesCount; i++) {

            gettingArray.add(helper.getName(i));
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, gettingArray);
        ChangeNameSelection.setAdapter(spinnerArrayAdapter);
        personSelection.setAdapter(spinnerArrayAdapter);
        removeNameSelection.setAdapter(spinnerArrayAdapter);

    }
}
