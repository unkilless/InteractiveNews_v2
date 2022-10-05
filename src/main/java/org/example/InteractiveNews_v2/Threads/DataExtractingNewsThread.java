package org.example.InteractiveNews_v2.Threads;

import org.example.InteractiveNews_v2.InstrumentsForStoreDataToFile.NonRepeatingElementsToFile;
import org.example.InteractiveNews_v2.ParseWorking.*;
import org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TimerTask;

public class DataExtractingNewsThread extends TimerTask {

    String[] usefulData = new String[] {"a href=", "title=\"", "tabindex=\"5\""};
    String[] unusefulData = new String[] {"class", "timeline", "=\"Фотохроника", "class=\"timeline_link\"", "=\"Что произошло за день:"};
    String[] filterForTimeline = new String[] {"time datetime="};

    static final String URL = "https://www.interfax.ru/";
    static final String OUTPUT_FILE_PATH = "D:\\Java\\Tests\\interfax.txt";
    static final String OUTPUT_FILTER_FILE_PATH = "D:\\Java\\Tests\\interfax_filters.txt";

    @Override
    public void run() {
        BufferedReader inputReader;
        ParseHTMLStringToTags_LinkedList htmlInfo = new ParseHTMLStringToTags_LinkedList();

        try {
            URL newsPortal = new URL(URL);
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
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE_PATH, true))) {
                LinkedList<MoreResult> usefulInfo = htmlInfo.searchTagBySignWithIndex(htmlInfo.outputListOfTags, usefulData, unusefulData);
                ExtractInfoInterfax dataNews = new ExtractInfoInterfax();
                ArrayList dataFields;
                ArrayList lastStringOfOutputFile = takeLastDateInfo();

                int futureDateCheck = 1;
                boolean isFirstCounter = (lastStringOfOutputFile.size() == 4) ? false : true;
                LocalDateTime date;
                NonRepeatingElementsToFile filterByTagsFile = new NonRepeatingElementsToFile(OUTPUT_FILTER_FILE_PATH);

                for (int i = usefulInfo.size() - 1; i >= 0; i--) {
                    dataFields = dataNews.extractNewsData(usefulInfo.get(i).getTag());

                    if (dataFields.size() != 3)
                        continue;

                    filterByTagsFile.addToFile(dataNews.getFilter(dataFields.get(2).toString()));

                    date = ExtractInfoInterfax.extractDateFromString(ExtractInfoInterfax.extractDateStringFormat(htmlInfo.searchTagWithIndexBackward(usefulInfo.get(i).getIndexOfTag(), filterForTimeline)));

                    if (!isFirstCounter) {
                        futureDateCheck = date.compareTo(ExtractInfoInterfax.extractDateFromString(lastStringOfOutputFile.get(0).toString()));
                    }

                    if (futureDateCheck > 0 || (futureDateCheck == 0 && !dataFields.get(1).equals(lastStringOfOutputFile.get(2).toString()))) {
                        bw.write(ExtractInfoInterfax.extractDateStringFormat(htmlInfo.searchTagWithIndexBackward(usefulInfo.get(i).getIndexOfTag(), filterForTimeline)) + "\t");
                        bw.write(dataFields.get(0).toString() + "\t"
                                    + dataFields.get(1).toString() + "\t"
                                    + dataFields.get(2).toString() + "\n");
                        bw.flush();
                        // data news format: DateTime \t URL \t news \t filter \n
                        }
                    }

                } catch(ExceptionListConsistsNonTags ex){
                    System.out.println("Error #5: Errors with extracting info from URL \n Exception info: ");
                    System.out.println(ex.getMessage());
                } catch(ExceptionNotConsistsTag ex){
                    System.out.println("Error #6: Errors with filters of tags \n Exception info: ");
                    System.out.println(ex.getMessage());
                } catch(ExceptionIncorrectBeginEndTags ex){
                    System.out.println("Error #7: Errors with sub list \n Exception info: ");
                    System.out.println(ex.getMessage());
                }catch(Exception ex){
                    System.out.println("Error #2: Common error \n Exception info: ");
                    System.out.println(ex.getMessage());
                }

            } catch (IOException ex) {
                System.out.println("Error #1: incorrect try to connect to site: interfax \n Exception info: ");
                System.out.println(ex.getMessage());
            }
    }

    public ArrayList takeLastDateInfo(){
        ArrayList lastRecord = new ArrayList();
        try(ReversedLinesFileReader reverseReader = new ReversedLinesFileReader(new File(OUTPUT_FILE_PATH))){
            String buffer = reverseReader.readLine();
            if (buffer != null){
                for(int i = 0; i < 3; i++){
                    lastRecord.add(buffer.substring(0, buffer.indexOf("\t")));
                    buffer = buffer.substring(buffer.indexOf("\t") + 1, buffer.length());
                }
                lastRecord.add(buffer.substring(0, buffer.length()));
            }
        }catch (Exception ex){
            ex.getStackTrace();
        }
        return lastRecord;
    }
}
