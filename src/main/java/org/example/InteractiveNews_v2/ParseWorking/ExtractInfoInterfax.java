package org.example.InteractiveNews_v2.ParseWorking;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ExtractInfoInterfax {

    private static final String FILTER_CLOUD_FILTERS = "href=";
    private static final String FILTER_CLOUD_TITLE = "title=";
    private static final String FILTER_CLOUD_TABINDEX = "tabindex=";

    public static LocalDateTime extractDateFromTag(String stringToExtracting){
        try{
            LocalDateTime date = LocalDateTime.parse(stringToExtracting.substring(stringToExtracting.indexOf("\"") + 1, stringToExtracting.lastIndexOf("\"")));
            return date;
        }catch(Exception ex){
            return null;
        }
    }

    public static LocalDateTime extractDateFromString(String stringToExtracting){
        try{
            LocalDateTime date = LocalDateTime.parse(stringToExtracting);
            return date;
        }catch(Exception ex){
            return null;
        }
    }

    public static String extractDateStringFormat (String stringToExtracting){
        LocalDateTime date = extractDateFromTag(stringToExtracting);
        return date.toString();
    }

    public static String extractTimeString (String stringToExtracting){
        try{
            SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
            Date time = formatterTime.parse(stringToExtracting.substring(stringToExtracting.lastIndexOf("T") + 1,
                                                                            stringToExtracting.length()));
            return formatterTime.format(time);
        }catch(Exception ex){
            return null;
        }
    }

    public static String extractDateString (String stringToExtracting){
        try{
            SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
            String buffer = stringToExtracting.substring(stringToExtracting.indexOf("\"") + 1,
                    stringToExtracting.lastIndexOf("T"));
            Date date = formatterDate.parse(buffer);
            return formatterDate.format(date);
        }catch(Exception ex){
            return null;
        }
    }

    public ArrayList<String> extractNewsData (String stringToExtracting){
        ArrayList<String> resultList = new ArrayList<String>();
        try {
                resultList.add(stringToExtracting.substring(stringToExtracting.indexOf(FILTER_CLOUD_FILTERS) + FILTER_CLOUD_FILTERS.length() + 1,
                                                            stringToExtracting.indexOf(FILTER_CLOUD_TITLE) - 2));
                resultList.add(stringToExtracting.substring(stringToExtracting.indexOf(FILTER_CLOUD_TITLE) + FILTER_CLOUD_TITLE.length() + 1,
                                                            stringToExtracting.indexOf(FILTER_CLOUD_TABINDEX) - 2));
                resultList.add(getFilter(resultList.get(0)));

        } catch (IndexOutOfBoundsException exception){
            exception.getStackTrace();
        }
        return resultList;
    }

    public String getFilter (String stringToExtracting){
        stringToExtracting = stringToExtracting.toLowerCase(Locale.ROOT);
        if (stringToExtracting.contains("sport")) return "sport";
        try {
            return stringToExtracting.substring(1, stringToExtracting.lastIndexOf("/"));
        } catch (IndexOutOfBoundsException ex) {
            return stringToExtracting;
        }
    }
}
