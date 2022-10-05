package org.example.InteractiveNews_v2.ParseWorking;

import org.example.InteractiveNews_v2.SQLinstruments.NewsRecord;
import org.example.InteractiveNews_v2.SQLinstruments.SQLFunctionality;
import org.example.InteractiveNews_v2.Threads.DataExtractingNewsThreadSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class InteractiveNewspaper_SQLEdition {

    public static final int DATETIME_FIELD = 0;
    public static final int URL_FIELD = 1;
    public static final int DATA_FIELD = 2;
    public static final int TAG_FIELD = 3;
    public static final String MAIN_DATA_FILE_PATH = "D:\\Java\\Tests\\interfax.txt";
    public static final String TAGS_FILE_PATH = "D:\\Java\\Tests\\interfax_filters.txt";

    public static void main(String[] args) {

        final String DB_URL = "jdbc:postgresql://localhost:5432/News";
        final String DB_USER = "client";
        final String DB_PASSWORD = "456";

        DataExtractingNewsThreadSQL newsData = new DataExtractingNewsThreadSQL();
        SQLFunctionality sqlFunctionality = new SQLFunctionality();
        try(Connection dbConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)){

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
                        subMenuInProcess(readCmd, newsData, sqlFunctionality, dbConnection);
                        break;
                    case 2:
                        NewsRecord lastData = sqlFunctionality.getLastInfo(dbConnection);
                        System.out.println(lastData.toString());
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
        }catch (SQLException exception){
            System.out.println("Critical error. Cant open connection to DB!");
            System.out.println(exception.getMessage());
        }
    }

    public static void mainMenu() {
        System.out.println("Interactive newspaper (v.0.2)");
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

    public static void subMenuInProcess(Scanner readSubCmd, DataExtractingNewsThreadSQL newsThread, SQLFunctionality sqlFunctionality, Connection dbConnection){
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
                    Iterator it = sqlFunctionality.printTags(dbConnection).iterator();
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

                    ArrayList<NewsRecord> resultSelectByTag = new ArrayList<>(sqlFunctionality.selectByTag(dbConnection, searchedTag));
                    Iterator iteratorResultSelectTag = resultSelectByTag.iterator();
                    while (iteratorResultSelectTag.hasNext()){
                        System.out.println(iteratorResultSelectTag.next().toString());
                    }
                    subMenuOfSelect();
                    break;
                case 3:
                    //Search by date
                    System.out.println("Input date in yyyy-MM-dd format, for example 2022-08-02");
                    String stringDate = inputSubCmd.next();

                    if (isCorrectDateInput(stringDate)) {
                        ArrayList<NewsRecord> resultSelectByDate = new ArrayList<>(sqlFunctionality.selectByDate(dbConnection, stringDate));
                        Iterator iteratorResultSelectDate = resultSelectByDate.iterator();
                        while (iteratorResultSelectDate.hasNext()){
                            System.out.println(iteratorResultSelectDate.next().toString());
                        }
                        if (resultSelectByDate.size() == 0) noDataMessage();
                    } else
                        noDataMessage();
                    subMenuOfSelect();
                    break;
                case 4:
                    //Search by date time
                    System.out.println("Input date in yyyy-MM-dd HH:mm format, for example 2022-08-02_18:00");
                    String stringDateTime = inputSubCmd.next();
                    stringDateTime = stringDateTime.trim();
                    stringDateTime = stringDateTime.replace("_", " ");
                    if (!sqlFunctionality.checkLocaleDateTimeFormat(stringDateTime)){
                        System.out.println("Something goes wrong...");
                        System.out.println("\u001B[31m" + "Wrong format date or time. Try again..." + "\u001B[0m");
                        break;
                    }
                    ArrayList<NewsRecord> resultSelectByDateTime = new ArrayList<>(sqlFunctionality.selectByDateTime(dbConnection, stringDateTime));
                    Iterator iteratorResultSelectByDateTime = resultSelectByDateTime.iterator();
                    while(iteratorResultSelectByDateTime.hasNext()){
                        System.out.println(iteratorResultSelectByDateTime.next().toString());
                    }
                    if (resultSelectByDateTime.size() == 0) noDataMessage();
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
                    ArrayList<NewsRecord> tagsCloud = sqlFunctionality.printTags(dbConnection);
                    if(isCorrectDateInput(dateStringFormat) && (tagsCloud.contains(tag))){
                        ArrayList<NewsRecord> resultSelectByTagAndDate = new ArrayList<>(sqlFunctionality.selectByTagAndDate(dbConnection, tag, dateStringFormat));
                        Iterator iteratorResultSelectByTagAndDate = resultSelectByTagAndDate.iterator();
                        while(iteratorResultSelectByTagAndDate.hasNext()){
                            System.out.println(iteratorResultSelectByTagAndDate.next().toString());
                        }
                        if (resultSelectByTagAndDate.size() == 0) noDataMessage();
                    } else
                        noDataMessage();

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

    public static void noDataMessage(){
        System.out.println("\u001B[31m" + "No data..." + "\u001B[0m");
    }

}
