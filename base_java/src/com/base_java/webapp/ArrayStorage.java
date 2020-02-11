package com.base_java.webapp;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Watchable;
import java.util.HashMap;
import java.util.Map;

public class ArrayStorage {
    // field Map<String, Object>

    public void ShowMap(Map WathedMap){
        int i = 1;
        while (i <= WathedMap.size()){
            System.out.println(WathedMap.get(i));
            i++;
        }
    }

    public String GetByID (Map WatchedMap, int ID) throws IOException{
        if (ID > 0 || ID < WatchedMap.size())
            return WatchedMap.get(ID).toString();
        else {
            return "Illegal ID";
        }
    }

    public void DeleteByID (Map WatchedMap, int ID) {
        if (ID > 0 || ID < WatchedMap.size()) {
            WatchedMap.remove(ID);
            System.out.println("Element with ID: " + ID + " was deleted");
        } else {
            System.out.println("Incorrect ID");
        }
    }

    public void PrintToFile (Map WathedMap, String PATH) throws IOException {
        int i = 1;
        try {
            FileWriter OutputFile = new FileWriter(PATH, true);
                while (i <= WathedMap.size()) {
                    OutputFile.write(WathedMap.get(i).toString());
                }
        OutputFile.close();
        }catch(IOException ex){
            System.out.println("Bad path");
        }
    }

    public void InsertToEnd (Map WatchedMap, String NewData){
        WatchedMap.put(WatchedMap.size()+1, NewData);
    }

    public void ClearMap (Map WatchedMap){
        WatchedMap.clear();
        System.out.println("Your base is empty now");
    }
    // save
    // get
    // update
    // delete

    public static void main(String[] args) {
        Map<Integer, String> MyBase = new HashMap<Integer, String>();
        ArrayStorage MyArray = new ArrayStorage();
        MyBase.put(1, "Ivan");
        MyBase.put(2, "Feodor");
        MyBase.put(3, "Anatoli Petrovich");
        MyArray.ShowMap(MyBase);
        MyArray.InsertToEnd(MyBase, "Evgen");
        MyArray.ShowMap(MyBase);
        MyArray.ClearMap(MyBase);
        MyArray.ShowMap(MyBase);
    }
}
