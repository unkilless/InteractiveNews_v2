package org.example.InteractiveNews_v2.ParseWorking;

import java.util.*;

public class ParseHTMLStringToTags_LinkedList extends AbstractParseHTML<LinkedList, LinkedList> {

    LinkedList resultList;
    LinkedList<MoreResult> resultListWithIndexes;

    public ParseHTMLStringToTags_LinkedList() {
        outputListOfTags = new LinkedList();
        resultList = new LinkedList();
        resultListWithIndexes = new LinkedList();
        this.inputStringForConvert = "";
    }

    public ParseHTMLStringToTags_LinkedList(String inputStringForConvert) {
        outputListOfTags = new LinkedList();
        resultList = new LinkedList();
        resultListWithIndexes = new LinkedList();
        this.inputStringForConvert = inputStringForConvert;
    }

    public LinkedList searchTagBySign(LinkedList inputLinkedListOfTags, String[] searchStrings, String[] ignoreStrings) {
        try {
            resultList.clear();
            if (isListOfTags(inputLinkedListOfTags)) {
                Object[] arrayOfTags = inputLinkedListOfTags.toArray();
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
        } catch (ExceptionListConsistsNonTags ex) {
            ex.getStackTrace();
            return null;
        }
    }

    public LinkedList searchTagBySignWithIndex (LinkedList inputLinkedListOfTags, String[] searchStrings, String[] ignoreStrings) {
        try {
            resultListWithIndexes.clear();
            if (isListOfTags(inputLinkedListOfTags)) {
                int index = -1;
                Object[] arrayOfTags = inputLinkedListOfTags.toArray();
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

    public LinkedList subInfoByTagsForward(LinkedList inputLinkedListOfTags, String begin, String end) throws ExceptionNotConsistsTag, ExceptionIncorrectBeginEndTags {
        try {
            resultList.clear();
            if (isListOfTags(inputLinkedListOfTags)) {
                if (!isContainsTag(inputLinkedListOfTags, begin) || !isContainsTag(inputLinkedListOfTags, end))
                    throw new ExceptionNotConsistsTag();
                int endIndex = indexOfTag(inputLinkedListOfTags, end);
                int beginIndex = indexOfTag(inputLinkedListOfTags, begin);
                if (beginIndex >= endIndex) throw new ExceptionIncorrectBeginEndTags();
                if (!begin.equals(end)) {
                    for (int i = 0; i <= endIndex - beginIndex; i++) {
                        resultList.add(inputLinkedListOfTags.get(beginIndex + i));
                    }
                }
            }
            return resultList;
        } catch (ExceptionListConsistsNonTags ex) {
            ex.getStackTrace();
            return resultList;
        }
    }

    public LinkedList subInfoByTagsBackward(LinkedList inputLinkedListOfTags, String begin, String end) throws ExceptionNotConsistsTag, ExceptionIncorrectBeginEndTags {
        try {
            resultList.clear();
            if (isListOfTags(inputLinkedListOfTags)) {
                if (!isContainsTag(inputLinkedListOfTags, begin) || !isContainsTag(inputLinkedListOfTags, end))
                    throw new ExceptionNotConsistsTag();
                int endIndex = lastIndexOfTag(inputLinkedListOfTags, end);
                int beginIndex = indexOfTag(inputLinkedListOfTags, begin);
                if (beginIndex >= endIndex) throw new ExceptionIncorrectBeginEndTags();
                if (!begin.equals(end)) {
                    for (int i = 0; i <= endIndex - beginIndex; i++) {
                        resultList.add(inputLinkedListOfTags.get(beginIndex + i));
                    }
                }
            }
            return resultList;
        } catch (ExceptionListConsistsNonTags ex) {
            ex.getStackTrace();
            return resultList;
        }
    }

}

