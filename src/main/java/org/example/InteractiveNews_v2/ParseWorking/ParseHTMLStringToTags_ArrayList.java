package org.example.InteractiveNews_v2.ParseWorking;

import java.util.ArrayList;
import java.util.LinkedList;

public class ParseHTMLStringToTags_ArrayList extends AbstractParseHTML<ArrayList, ArrayList> {

    private static ArrayList resultList;
    LinkedList<MoreResult> resultListWithIndexes;

    public ParseHTMLStringToTags_ArrayList() {
        outputListOfTags = new ArrayList();
        resultList = new ArrayList();
        resultListWithIndexes = new LinkedList();
        this.inputStringForConvert = "";
    }

    public ParseHTMLStringToTags_ArrayList(String inputStringForConvert) {
        outputListOfTags = new ArrayList();
        resultList = new ArrayList();
        resultListWithIndexes = new LinkedList();
        this.inputStringForConvert = inputStringForConvert;
    }

    public ArrayList searchTagBySign (ArrayList inputArrayListOfTags, String[] searchStrings, String[] ignoreStrings){
        try {
            resultList.clear();
            if (isListOfTags(inputArrayListOfTags)) {
                Object[] arrayOfTags = inputArrayListOfTags.toArray();
                int counter = 0;
                for (Object o : arrayOfTags) {
                    for (int i = 0; i <= searchStrings.length - 1; i++) {
                        if (o.toString().contains(searchStrings[i])) {
                            counter++;
                        } else {
                            counter = 0;
                            break;
                        }
                    }
                    if (counter == searchStrings.length) {
                        counter = 0;
                        for (int j = 0; j <= ignoreStrings.length - 1; j++) {
                            if (!o.toString().contains(ignoreStrings[j]))
                                counter++;
                            if (counter == ignoreStrings.length) {
                                resultList.add(o.toString());
                                break;
                            }
                        }
                    }
                    counter = 0;
                }
                return resultList;
            } else
                return resultList;
        }catch (ExceptionListConsistsNonTags ex){
            ex.getStackTrace();
            return null;
        }
    }

    public LinkedList searchTagBySignWithIndex (ArrayList inputArrayListOfTags, String[] searchStrings, String[] ignoreStrings) {
        try {
            resultListWithIndexes.clear();
            if (isListOfTags(inputArrayListOfTags)) {
                int index = -1;
                Object[] arrayOfTags = inputArrayListOfTags.toArray();
                int counter = 0;
                for (Object o : arrayOfTags) {
                    index++;
                    for (int i = 0; i <= searchStrings.length - 1; i++) {
                        if (o.toString().contains(searchStrings[i])) {
                            counter++;
                        } else {
                            counter = 0;
                            break;
                        }
                    }
                    if (counter == searchStrings.length) {
                        counter = 0;
                        for (int j = 0; j <= ignoreStrings.length - 1; j++) {
                            if (!o.toString().contains(ignoreStrings[j]))
                                counter++;
                            if (counter == ignoreStrings.length) {
                                MoreResult result = new MoreResult();
                                result.setMoreResult(o.toString(), index);
                                resultListWithIndexes.add(result);
                                break;
                            }
                        }
                    }
                    counter = 0;
                }
                return resultListWithIndexes;
            } else
                return resultListWithIndexes;
        } catch (ExceptionListConsistsNonTags ex) {
            ex.getStackTrace();
            return null;
        }
    }

    public ArrayList subInfoByTagsForward(ArrayList inputArrayListOfTags, String begin, String end) throws ExceptionNotConsistsTag, ExceptionIncorrectBeginEndTags {
        try {
            resultList.clear();
            if (isListOfTags(inputArrayListOfTags)) {
                if (!isContainsTag(inputArrayListOfTags, begin) || !isContainsTag(inputArrayListOfTags, end))
                    throw new ExceptionNotConsistsTag();
                int endIndex = indexOfTag(inputArrayListOfTags, end);
                int beginIndex = indexOfTag(inputArrayListOfTags, begin);
                if (beginIndex >= endIndex) throw new ExceptionIncorrectBeginEndTags();
                if (!begin.equals(end)) {
                    for (int i = 0; i <= endIndex - beginIndex; i++) {
                        resultList.add(inputArrayListOfTags.get(beginIndex + i));
                    }
                }
            }
            return resultList;
        } catch (ExceptionListConsistsNonTags ex){
            ex.getStackTrace();
            return resultList;
        }
    }

    public ArrayList subInfoByTagsBackward(ArrayList inputArrayListOfTags, String begin, String end) throws ExceptionNotConsistsTag, ExceptionIncorrectBeginEndTags {
        try {
            resultList.clear();
            if (isListOfTags(inputArrayListOfTags)) {
                if (!isContainsTag(inputArrayListOfTags, begin) || !isContainsTag(inputArrayListOfTags, end))
                    throw new ExceptionNotConsistsTag();
                int endIndex = lastIndexOfTag(inputArrayListOfTags, end);
                int beginIndex = indexOfTag(inputArrayListOfTags, begin);
                if (beginIndex >= endIndex) throw new ExceptionIncorrectBeginEndTags();
                if (!begin.equals(end)) {
                    for (int i = 0; i <= endIndex - beginIndex; i++) {
                        resultList.add(inputArrayListOfTags.get(beginIndex + i));
                    }
                }
            }
            return resultList;
        }catch (ExceptionListConsistsNonTags ex){
            ex.getStackTrace();
            return resultList;
        }
    }
 }


