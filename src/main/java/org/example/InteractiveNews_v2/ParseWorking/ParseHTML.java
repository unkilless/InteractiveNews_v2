package org.example.InteractiveNews_v2.ParseWorking;

public interface ParseHTML <OutputListOfTags, InputTagList> {

    String getInputStringForConvert();

    void setInputStringForConvert(String inputStringForConvert);

    void parseToHTMLelements(boolean isEmptyTag);

    int indexOfTag (InputTagList inputListOfTags, String searchedElement);

    int lastIndexOfTag (InputTagList inputListOfTags, String searchedElement);

    boolean isContainsTag (InputTagList inputListOfTags, String searchedElement);


}
