package org.example.InteractiveNews_v2.Threads;

import org.example.InteractiveNews_v2.ParseWorking.ExtractInfoInterfax;
import org.example.InteractiveNews_v2.ParseWorking.MoreResult;
import org.example.InteractiveNews_v2.ParseWorking.ParseHTMLStringToTags_LinkedList;
import org.example.InteractiveNews_v2.SQLinstruments.NewsRecord;
import org.example.InteractiveNews_v2.SQLinstruments.SQLFunctionality;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TimerTask;

public class DataExtractingNewsThreadSQL extends TimerTask {

    String[] usefulData = new String[] {"a href=", "title=\"", "tabindex=\"5\""};
    String[] unusefulData = new String[] {"class", "timeline", "=\"Фотохроника", "class=\"timeline_link\"", "=\"Что произошло за день:"};
    String[] filterForTimeline = new String[] {"time datetime="};

    final String DB_URL = "jdbc:postgresql://localhost:5432/News";
    final String DB_USER = "test";
    final String DB_PASSWORD = "123";
    final int URL_FIELD = 0;
    final int NEWSDATA_FIELD = 1;
    final int TAG_FIELD = 2;
    static final String NEWS_URL = "https://www.interfax.ru/";

    @Override
    public void run() {

        try(Connection dbConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            ParseHTMLStringToTags_LinkedList htmlInfo = new ParseHTMLStringToTags_LinkedList();
            BufferedReader inputReader;

            URL newsPortal = new URL(NEWS_URL);
            newsPortal.openConnection();

            inputReader = new BufferedReader(new InputStreamReader(newsPortal.openStream(), "cp1251"));
            String buffer;
            boolean isEmpty = false;
            while (!isEmpty) {
                buffer = inputReader.readLine();
                if (buffer != null) {
                    htmlInfo.setInputStringForConvert(buffer);
                    htmlInfo.parseToHTMLelements(false);
                } else
                    isEmpty = true;
            }

            LinkedList<MoreResult> usefulInfo = htmlInfo.searchTagBySignWithIndex(htmlInfo.outputListOfTags, usefulData, unusefulData);
            ExtractInfoInterfax dataNews = new ExtractInfoInterfax();
            SQLFunctionality sqlFunctionality = new SQLFunctionality();
            ArrayList dataFields;
            NewsRecord lastNewsRecord = sqlFunctionality.getLastInfo(dbConnection);
            LocalDateTime currentURLDate;
            LocalDateTime pastNewsRecordDate = lastNewsRecord.getDateTime().toLocalDateTime();
            String formatDateTimeStr = "";

            String test = "";
            boolean t1 = false;
            boolean t2 = false;

            for (int i = usefulInfo.size() - 1; i >= 0; i--) {
                dataFields = dataNews.extractNewsData(usefulInfo.get(i).getTag());

                if (dataFields.size() != 3)
                    continue;

                currentURLDate = ExtractInfoInterfax.extractDateFromString(ExtractInfoInterfax.extractDateStringFormat(htmlInfo.searchTagWithIndexBackward(usefulInfo.get(i).getIndexOfTag(), filterForTimeline)));

                t1 = lastNewsRecord.getDateTime().toLocalDateTime().equals(currentURLDate) && !lastNewsRecord.getURL().equals(dataFields.get(URL_FIELD));
                t2 = currentURLDate.compareTo(pastNewsRecordDate) > 0;

                if((lastNewsRecord.getDateTime() == null) ||
                        (lastNewsRecord.getDateTime().toLocalDateTime().equals(currentURLDate) && !lastNewsRecord.getURL().equals(dataFields.get(URL_FIELD))) ||
                        (currentURLDate.compareTo(pastNewsRecordDate) > 0)){
                    formatDateTimeStr = ExtractInfoInterfax.extractDateStringFormat(htmlInfo.searchTagWithIndexBackward(usefulInfo.get(i).getIndexOfTag(), filterForTimeline));
                    formatDateTimeStr = formatDateTimeStr.replace("T", " ");
                    test = sqlFunctionality.dateTimeToTimestamp(formatDateTimeStr).toString();
                    sqlFunctionality.update(dbConnection,
                                            sqlFunctionality.dateTimeToTimestamp(formatDateTimeStr),
                                            dataFields.get(URL_FIELD).toString(),
                                            dataFields.get(NEWSDATA_FIELD).toString(),
                                            dataFields.get(TAG_FIELD).toString());
                }
            }
        } catch(SQLException ex){
            System.out.println("Error #8: SQL problems \n Exception info: ");
            System.out.println(ex.getMessage());
        } catch(Exception ex){
                System.out.println("Error #2: Common error \n Exception info: ");
                System.out.println(ex.getMessage());
        }
    }

}
