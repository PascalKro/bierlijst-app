package com.example.pascal.bierlijst20;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;


public class Database {
    private String fileName = "Userfiles.csv";
    private String directory = Environment.getExternalStorageDirectory()+"/Bierlijst Files/";
    private File special = new File(Environment.getExternalStorageDirectory() +
    File.separator + "Bierlijst Files");
    private String directoryAndFile = directory + fileName;
    private int totalCount = 0, peopleCount = 0;

    public Database(){


    }

    public void FileAvailability(){
        try {
            File userDataFile = new File(special, fileName);

            if(userDataFile.length() ==0 || !userDataFile.exists())
            {
                if (!userDataFile.exists()) {

                    special.mkdirs();
                    userDataFile.createNewFile();
                }

                FileOutputStream streamer = new FileOutputStream(userDataFile, false);
                String person = "";
                for(int i = 0; i < 5; i++) {
                    int j = i+1;
                    person = "Person "+j+",0\n";
                    streamer.write(person.getBytes());//persons
                }
                person = "TotalBier,0\n";
                streamer.write(person.getBytes());
                streamer.flush();
                streamer.close();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        peopleCount = numberOfUsers();
        totalCount = peopleCount+1;
    }

    public String getName(int whichPerson){
        peopleCount = numberOfUsers();
        totalCount = peopleCount+1;
        //row consist of position 0: Name position 1: Number
        String unsplittedData = "";
        String personsName = "";
        String[] rows = new String[totalCount];

        if(whichPerson <= peopleCount) {
            try {
                /**Store unsplitted Data in rows**/
                BufferedReader reader = new BufferedReader(new FileReader(directoryAndFile));
                for (int i = 0; (unsplittedData = reader.readLine()) != null && i < peopleCount; i++) {
                    rows[i] = unsplittedData;

                }
            } catch (IOException e) {
                System.out.println("Damn you need to make person by going to the settings and start renaming");
            }
            if (rows[whichPerson] == null) {
                rows[whichPerson] = "not named yet,0\n";
            }
            personsName = rows[whichPerson].split(",")[0];
        }
        else{
            personsName = "Index out of Bounds";
        }
        return personsName;
    }

    public void saveName(int whichPerson, String NewName){
        //region SETUP for Saving
        totalCount = numberOfUsers()+1;
        String[] currentNames = new String[totalCount], newData = new String[totalCount];
        int[] currentBalance = new int[totalCount];


        for(int row = 0; row < totalCount; row++) {
            currentNames[row] = getName(row);
            currentBalance[row] = getBalance(row);
        }

        int balance = getBalance(whichPerson);

        File specialDirectory = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Bierlijst Files");

        //endregion
        //region Data assembly
        for(int counter = 0; counter < totalCount; counter++) {
            newData[counter] = currentNames[counter] + "," + currentBalance[counter] + "\n";
        }
        newData[whichPerson] = NewName + "," + balance+"\n";
        //endregion

        //region File zoeken & Opslaan
        try {
            File userDataFile = new File(specialDirectory, fileName);
            FileOutputStream stream = new FileOutputStream(userDataFile, false);
            if (!userDataFile.exists()) {
                specialDirectory.mkdirs();
                specialDirectory.createNewFile();
            }
            for(int counter = 0; counter < totalCount;counter++) {
                stream.write(newData[counter].getBytes());//person 1
            }
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //endregion
    }

    public int getBalance(int whichPerson){
        //row consist of position 0: Name position 1: Number
        totalCount = numberOfUsers()+1;
        String unsplittedData = "";
        String[] rows = new String[totalCount];

        try {
            /**Store unsplitted Data in rows**/
            BufferedReader reader = new BufferedReader(new FileReader(directoryAndFile));
            int i = 0;
            while((unsplittedData = reader.readLine()) != null && i < totalCount)
            {
                rows[i] = unsplittedData;

                i++;
            }
        }

        catch(IOException e){System.out.println("Damn you need to make person by going to the settings and start renaming"); }

        try {
            int personsBalance = Integer.parseInt(rows[whichPerson].split(",")[1]);
            return personsBalance;
        }
        catch(Exception e){
            return 0;
        }
    }

    public void saveBalance(int whichPerson, int opslagAktie){
        //region SETUP for Saving
        peopleCount = numberOfUsers();
        totalCount = peopleCount+1;
        String[] currentListNames = new String[totalCount];
        String[] newUserData = new String[totalCount];
        int[] currentListBalances = new int[totalCount];
        for(int personrows = 0; personrows < totalCount; personrows++) {
            currentListNames[personrows] = getName(personrows);
            currentListBalances[personrows] = getBalance(personrows);
        }
        //endregion

        //region Data assembly
        if(whichPerson < peopleCount) {
            currentListBalances[totalCount-1] += opslagAktie;
            currentListBalances[whichPerson] += opslagAktie;
            //newUserData[whichPerson] = currentListNames[whichPerson] + "," + currentListBalances[whichPerson]+"\n";
            for(int counter = 0; counter < totalCount;counter++) {
                newUserData[counter] = currentListNames[counter] + "," + currentListBalances[counter] + "\n";
            }
        }
         else{
            for(int counter = 0; counter < peopleCount;counter++) {
                newUserData[counter] = currentListNames[counter] + "," + currentListBalances[counter] + "\n";
            }
             newUserData[whichPerson] = "totalBier" + "," + opslagAktie; //total Bier check
        }

        //endregion

        //region File Zoeken & Opslaan
        try {
            File specialDirectory = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "Bierlijst Files");
            File userDataFile = new File(specialDirectory, fileName);
            FileOutputStream stream = new FileOutputStream(userDataFile, false);
            if (!userDataFile.exists()) {
                specialDirectory.mkdirs();
                specialDirectory.createNewFile();
            }
            for(int counter = 0; counter < totalCount;counter++) {
                stream.write(newUserData[counter].getBytes());
            }
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //endregion
    }

    public int numberOfUsers(){
        int Count = 0;
        try {
            BufferedReader r = new BufferedReader(new FileReader(directoryAndFile));
            while((r.readLine()) != null)
            {
                Count++;
            }
        }
        catch(IOException e){e.printStackTrace();}
        return (Count-1);
    }

    public void popUP(String message, Context appContext){
        AlertDialog.Builder popUP_lowBier = new AlertDialog.Builder(appContext);
        Random ran = new Random();
        String welkeRespons = "";
        switch(ran.nextInt(5)){
            case 0: welkeRespons = "Ja, Ja, Is goed hoor"; break;
            case 1: welkeRespons = "Als't moet, hè!"; break;
            case 2: welkeRespons = "Ja toe maar"; break;
            case 3: welkeRespons = "Doe dan"; break;
            case 4: welkeRespons = "Okey dan"; break;
        }
        popUP_lowBier.setMessage(message)
                .setPositiveButton(welkeRespons, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();

    }

    public void popUP(int bierLeft, Context appContext){
        AlertDialog.Builder popUP_lowBier = new AlertDialog.Builder(appContext);
        Random ran = new Random();
        String welkeRespons = "";
        int[] peopleBalance = new int[numberOfUsers()];
        switch(ran.nextInt(5)){
            case 0: welkeRespons = "Ja, Ja, Is goed hoor..."; break;
            case 1: welkeRespons = "Als't moet, hè!"; break;
            case 2: welkeRespons = "Ja toe maar."; break;
            case 3: welkeRespons = "Doe dan!"; break;
            case 4: welkeRespons = "Okey dan"; break;
        }
        for(int i = 0; i < numberOfUsers(); i++){
            peopleBalance[i]= getBalance(i);
        }

        //region Search for Lowest Balance
        int lowestValue = peopleBalance[0];
        int lowestPosition = 0;
        for(int i=1;i < peopleBalance.length;i++){
            if(peopleBalance[i] < lowestValue){
                lowestValue = peopleBalance[i];
                lowestPosition = i;
            }
        }
        //endregion

        //region Actual PopUp Creation
        if(bierLeft < 12 && bierLeft > 0) {

            popUP_lowBier.setMessage("WARNING!\nHet Bier is bijna op...\n"+
                    getName(lowestPosition)+", je hebt te veel gezopen.\nKoop ff wat bij!")
                    .setPositiveButton(welkeRespons, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            popUP_lowBier.show();

        }
        else if(getBalance(numberOfUsers()) <= 0){
            popUP_lowBier.setMessage("WARNING!\nHet Bier is HELEMAAL op...\n"+"Hey "+
                    getName(lowestPosition)+", Je hebt echt veel gezopen.\nKoop ff wat bij!")
                    .setPositiveButton(welkeRespons, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            popUP_lowBier.show();
        }
        //endregion
    }

}
