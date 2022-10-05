package org.example.InteractiveNews_v2.ParseWorking;

import org.example.InteractiveNews_v2.Threads.DataExtractingNewsThread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class InteractiveNewspaper {

    public static final int DATETIME_FIELD = 0;
    public static final int URL_FIELD = 1;
    public static final int DATA_FIELD = 2;
    public static final int TAG_FIELD = 3;
    public static final String MAIN_DATA_FILE_PATH = "D:\\Java\\Tests\\interfax.txt";
    public static final String TAGS_FILE_PATH = "D:\\Java\\Tests\\interfax_filters.txt";

    public static void main(String[] args) {
        DataExtractingNewsThread newsData = new DataExtractingNewsThread();
        Timer mainUpdateTimer = new Timer();
        mainUpdateTimer.schedule(newsData, 5000, 360000);
        Scanner readCmd = new Scanner(System.in);
        mainMenu();
        int mainMenuIndex = -1;
        while(true) {
            try {
                mainMenuIndex = readCmd.nextInt();
            }catch (InputMismatchException ex){
                System.out.println("Wrong cmd! Try again!");
                mainMenuIndex = -1;
            }
            switch (mainMenuIndex) {
                case 1:
                    subMenuInProcess(readCmd, newsData);
                    break;
                case 2:
                    ArrayList<String> lastData = newsData.takeLastDateInfo();
                    Iterator it = lastData.iterator();
                    while (it.hasNext()) {
                        System.out.println(it.next().toString());
                    }
                    System.out.println();
                    mainMenu();
                    break;
                case 0:
                    System.out.println("Exit");
                    mainUpdateTimer.cancel();
                    return;
                default:
                    mainMenu();
                    readCmd.nextLine();
                    break;
            }
        }
    }

    public static void mainMenu() {
        System.out.println("Interactive newspaper (v.0.1)");
        System.out.println("Main menu:");
        System.out.println("\t1-Show news by...");
        System.out.println("\t2-Get info of last update;");
        System.out.println("\t0-Exit");
    }

    public static void subMenuOfSelect() {
        System.out.println("Shows news by...");
        System.out.println("\t1-Show tags;");
        System.out.println("\t2-Take info by tag;");
        System.out.println("\t3-Take info by date;");
        System.out.println("\t4-Take info by date and time (result in hour period);");
        System.out.println("\t5-Take info by tag and date;");
        System.out.println("\t0-Back to main menu");
    }

    public static void subMenuInProcess(Scanner readSubCmd, DataExtractingNewsThread newsThread){
        subMenuOfSelect();
        Scanner inputSubCmd = new Scanner(System.in);
        int subMenuIndex = -1;
        while(true) {
            try {
                subMenuIndex = readSubCmd.nextInt();
            }catch (InputMismatchException ex){
                subMenuIndex = -1;
            }
            switch (subMenuIndex) {
                case 1:
                    //Show tags
                    Iterator it = cloudOfTags(readSubCmd).iterator();
                    while (it.hasNext()){
                        System.out.println(it.next().toString());
                    }
                    subMenuOfSelect();
                    break;
                case 2:
                    //Search by tag
                    String searchedTag = "";
                    System.out.println("Please enter one of tag");
                    do {
                        searchedTag = inputSubCmd.nextLine();
                    }while (searchedTag.isEmpty());

                    tagSelect(searchedTag);
                    subMenuOfSelect();
                    break;
                case 3:
                    //Search by date
                    System.out.println("Input date in yyyy-MM-dd format, for example 2022-08-02");
                    String stringDate = inputSubCmd.next();

                    if (isCorrectDateInput(stringDate)) {
                        dateSelect(stringDate);
                    }
                    subMenuOfSelect();
                    break;
                case 4:
                    //Search by date time
                    System.out.println("Input date in yyyy-MM-dd HH:mm format, for example 2022-08-02_18:00");
                    String stringDateTime = inputSubCmd.next();
                    searchByDateTime(stringDateTime);
                    subMenuOfSelect();
                    break;
                case 5:
                    //take info by tag and date
                    String tag = "";
                    System.out.println("Please enter one of tag");
                    do {
                        tag = inputSubCmd.nextLine();
                    }while (tag.isEmpty());
                    System.out.println("Input date in yyyy-MM-dd format, for example 2022-08-02");
                    String dateStringFormat = inputSubCmd.nextLine();
                    int count = 0;
                    File fileFilter = new File(TAGS_FILE_PATH);
                    if(isCorrectDateInput(dateStringFormat) && (cloudOfTags(readSubCmd).contains(tag)) || !fileFilter.exists()){
                        try (BufferedReader br = new BufferedReader(new FileReader(MAIN_DATA_FILE_PATH))) {
                            List resultListByTag;
                            String buffer = br.readLine();

                            while (buffer != null) {
                                resultListByTag = Arrays.asList(buffer.split("\t"));
                                if (ExtractInfoInterfax.extractDateString(resultListByTag.get(DATETIME_FIELD).toString()).equals(dateStringFormat) &&
                                    resultListByTag.get(3).toString().equals(tag.toLowerCase(Locale.ROOT))){
                                        printResultDateTimeData(resultListByTag);
                                        count++;
                                }
                                buffer = br.readLine();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (count == 0) noDataMessage();
                    subMenuOfSelect();
                    break;
                case 0:
                    //back to main menu
                    mainMenu();
                    return;
                default:
                    //wrong cmd
                    subMenuOfSelect();
                    readSubCmd.nextLine();
                    break;
            }
        }
    }

    public static void dateSelect(String dateStringFormat){
        try (BufferedReader br = new BufferedReader(new FileReader(MAIN_DATA_FILE_PATH))) {
            List resultListByTag;
            String buffer = br.readLine();
            int count = 0;
            while (buffer != null) {
                resultListByTag = Arrays.asList(buffer.split("\t"));
                if (ExtractInfoInterfax.extractDateString(resultListByTag.get(DATETIME_FIELD).toString()).equals(dateStringFormat)){
                    printResultDateTimeData(resultListByTag);
                    count++;
                }
                buffer = br.readLine();
            }
            if (count == 0) noDataMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void tagSelect(String tag){
        try(BufferedReader br = new BufferedReader(new FileReader(MAIN_DATA_FILE_PATH))){
            String buffer = br.readLine();
            List resultListByTag;
            int count = 0;
            while (buffer != null){
                resultListByTag = Arrays.asList(buffer.split("\t"));
                if(resultListByTag.get(3).equals(tag.toLowerCase(Locale.ROOT))) {
                    printResultDateTimeData(resultListByTag);
                    count++;
                }
                buffer = br.readLine();
            }
            if (count == 0){
                System.out.println("\u001B[31m" + "No any info for your tag: \"" + tag + "\"" + "\u001B[0m");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isCorrectDateInput(String dateStringFormat){
        try {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            if (dateStringFormat != null) {
                Date searchedDate = dateFormatter.parse(dateStringFormat);
            }
            return true;
        } catch (Exception ex){
            System.out.print("\nSomething goes wrong: \n\t" + ex.getMessage() + "\n");
            System.out.print("\u001B[31m" + "Wrong format date. Try again...\n" + "\u001B[0m");
            return false;
        }
    }

    public static ArrayList<String> cloudOfTags (Scanner inputCmd){
        ArrayList<String> tags = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(TAGS_FILE_PATH))){
            String buffer = br.readLine();
            while (buffer != null){
                tags.add(buffer);
                buffer = br.readLine();
            }
        } catch (Exception e) {
            System.out.println("No tags info \n " + e.getMessage());
            inputCmd.nextLine();
        }
        return tags;
    }

    public static void searchByDateTime (String searchedDateTime){
        searchedDateTime = searchedDateTime.replace("_", "T");
        try(BufferedReader br = new BufferedReader(new FileReader(MAIN_DATA_FILE_PATH))) {
            LocalDateTime searchedDate = ExtractInfoInterfax.extractDateFromString(searchedDateTime);
            List resultListByTag;
            String buffer = br.readLine();
            int count = 0;
            while (buffer != null){
                resultListByTag = Arrays.asList(buffer.split("\t"));
                LocalDateTime currentDate = ExtractInfoInterfax.extractDateFromString(resultListByTag.get(DATETIME_FIELD).toString());
                if(currentDate.compareTo(searchedDate) > 0 && currentDate.compareTo(searchedDate.plusHours(1)) < 0) {
                    printResultDateTimeData(resultListByTag);
                    count++;
                }
                buffer = br.readLine();
            }
            if (count == 0) noDataMessage();
        }catch (IOException ex){
            System.out.println(ex.getMessage());
            System.out.println("IOFile error");
        }catch(Exception ex){
            System.out.println("Something goes wrong: \n\t" + ex.getMessage());
            System.out.println("\u001B[31m" + "Wrong format date or time. Try again..." + "\u001B[0m");
        }
    }

    public static void noDataMessage(){
        System.out.println("\u001B[31m" + "No data..." + "\u001B[0m");
    }

    public static void printResultDateTimeData(List resultListByTag){
        System.out.println(ExtractInfoInterfax.extractDateString(resultListByTag.get(DATETIME_FIELD).toString()) +
                "\t" + ExtractInfoInterfax.extractTimeString(resultListByTag.get(DATETIME_FIELD).toString()) +
                "\t" + resultListByTag.get(DATA_FIELD) +
                "\t" + resultListByTag.get(URL_FIELD));
    }
}
