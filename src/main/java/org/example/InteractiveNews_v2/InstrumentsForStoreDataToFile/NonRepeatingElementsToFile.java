package org.example.InteractiveNews_v2.InstrumentsForStoreDataToFile;

import java.io.*;
import java.util.HashSet;

public class NonRepeatingElementsToFile{
    private String pathOfFile;
    private HashSet<String> resultSet;
    private static boolean isReedFile;

    NonRepeatingElementsToFile(){
        resultSet = new HashSet<String>();
        pathOfFile = "";
        isReedFile = false;
    }

    public NonRepeatingElementsToFile(String pathOfFile){
        resultSet = new HashSet<String>();
        this.pathOfFile = pathOfFile;
        isReedFile = false;
    }

    public void setPathOfFile(String pathOfFile) {
        this.pathOfFile = pathOfFile;
        isReedFile = false;
    }

    public HashSet<String> getResultSet() {
        return resultSet;
    }

    private void takeCurrentData(){
        try(BufferedReader br = new BufferedReader(new FileReader(this.pathOfFile))){
            String buffer = br.readLine();
            while (buffer != null){
                resultSet.add(buffer.toLowerCase());
                buffer = br.readLine();
            }
            isReedFile = true;
        }catch (Exception ex){
            ex.getStackTrace();
            isReedFile = false;
        }
    }

    public void addToFile(String inputData){
        if (!isReedFile) takeCurrentData();
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(pathOfFile, true))) {
            if (resultSet.add(inputData.toLowerCase())) {
                bw.write(inputData.toLowerCase() + "\n");
                bw.flush();
            }
        } catch (Exception ex){
            ex.getStackTrace();
        }
    }

    public void addToFile(String[] inputDataArray){
        if (!isReedFile) takeCurrentData();
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(pathOfFile, true))) {
            for(int index = 0; index < inputDataArray.length; index++) {
                if (resultSet.add(inputDataArray[index].toLowerCase())) {
                    bw.append(inputDataArray[index].toLowerCase() + "\n");
                    bw.flush();
                }
            }
        } catch (Exception ex){
            ex.getStackTrace();
        }
    }
}
