package org.example.InteractiveNews_v2.ParseWorking;

import java.util.List;
import java.util.Map;

public abstract class AbstractParseHTML<OutputListOfTags extends List, InputTagList extends List> implements ParseHTML<OutputListOfTags, InputTagList> {

    public String inputStringForConvert;

    public OutputListOfTags outputListOfTags;

    public static final int MINIMAL_DATA_LENGTH_FOR_HTML_MNEMONIC = 6;

    public static final int NOT_FOUND = -1;

    public String getInputStringForConvert() {
        return "Current String is: \"" + inputStringForConvert + "\"";
    };

    public void setInputStringForConvert(String inputStringForConvert) {
        this.inputStringForConvert = inputStringForConvert;
    }

    public void parseToHTMLelements(boolean isEmptyTag) {   //isEmptyTag used for enable add to List empty tags <>
        if (this.inputStringForConvert.contains("<") && this.inputStringForConvert.contains(">")) {
            byte[] bytesArrayOfString = inputStringForConvert.getBytes();
            int beginIndex = 0;
            int endIndex = 0;
            boolean isFirstSearchedChar = false;
            String tag;
            for(int currentIndex = 0; currentIndex < inputStringForConvert.length(); currentIndex++) {
                if(bytesArrayOfString[currentIndex] == '<' && beginIndex >= 0){
                    beginIndex = currentIndex;
                    isFirstSearchedChar = true;
                    continue;
                }
                if(bytesArrayOfString[currentIndex] == '>' && !isFirstSearchedChar)
                    continue;
                if(bytesArrayOfString[currentIndex] == '>' && endIndex == 0 && beginIndex > 0) {
                        endIndex = currentIndex;
                        tag = inputStringForConvert.substring(beginIndex, endIndex + 1);
                        beginIndex = 0;
                        endIndex = 0;

                    if (tag.length() > MINIMAL_DATA_LENGTH_FOR_HTML_MNEMONIC) {
                        for (Map.Entry<String, String> findingHTMLmnemonics : SetExpressionHTMLmnemonic.HTMLmnemonic.entrySet()) {
                            tag = tag.replaceAll(findingHTMLmnemonics.getKey(), findingHTMLmnemonics.getValue());
                        }
                    }

                    if (tag.length() < 3 && !isEmptyTag) {
                        beginIndex = 0;
                        continue;
                    }
                    else
                        outputListOfTags.add(tag);
                }
            }
        }
    }


    public boolean isListOfTags(InputTagList anyTagList) throws ExceptionListConsistsNonTags {
        Object[] elementsOfArrayList = anyTagList.toArray();
        String buffer;
        int countOfElements = 0;
        for(Object o: elementsOfArrayList){
            buffer = o.toString();
            if (buffer.startsWith("<") && buffer.endsWith(">")){
                buffer = buffer.substring(1, buffer.length() - 1);
                if (!buffer.contains(">") && !buffer.contains("<"))
                    countOfElements++;
            }else{
                throw new ExceptionListConsistsNonTags();
            }
        }
        if (countOfElements == anyTagList.size())
            return true;
        else
            return false;
    }

    public boolean isTag (String inputHTMLString) throws Exception{ //Check '<' and '>'
        byte[] arrayOfBytes = inputHTMLString.getBytes();
        if (arrayOfBytes[0] == '<' && arrayOfBytes[inputHTMLString.length()-1] == '>') {
            if (inputHTMLString.length() > 2)
                for (int i = 1; i < inputHTMLString.length() - 2; i++) {
                    if (arrayOfBytes[i] == '<' || arrayOfBytes[i] == '>') {
                        throw new Exception("Searched string is not tag!" + inputHTMLString);
                    }
                }
            return true;
        } else {
            return false;
        }
    }

    public int indexOfTag (InputTagList inputListOfTags, String searchedElement){
        try {
            if (isListOfTags(inputListOfTags) && isTag(searchedElement)) {
                return inputListOfTags.indexOf(searchedElement);
            } else
                return NOT_FOUND;
        } catch (ExceptionListConsistsNonTags ex){
            ex.getStackTrace();
            return NOT_FOUND;
        } catch (Exception ex){
            ex.getStackTrace();
            return NOT_FOUND;
        }
    }

    public int indexOfTag (String searchedElement) {
        try{
            if (isTag(searchedElement)) {
                return this.outputListOfTags.indexOf(searchedElement);
            }
        } catch (Exception ex){
            ex.getStackTrace();
        }
        return NOT_FOUND;
    }

    public int lastIndexOfTag (InputTagList inputListOfTags, String searchedElement){
        try {
            if (isListOfTags(inputListOfTags) && isTag(searchedElement)) {
                return inputListOfTags.lastIndexOf(searchedElement);
            } else
                return NOT_FOUND;
        } catch (ExceptionListConsistsNonTags ex){
            ex.getStackTrace();
            return  NOT_FOUND;
        } catch (Exception ex){
            ex.getStackTrace();
            return NOT_FOUND;
        }
    }

    public int lastIndexOfTag (String searchedElement){
        try {
            if (isTag(searchedElement)) {
                return this.outputListOfTags.lastIndexOf(searchedElement);
            }
        } catch (Exception ex){
            ex.getStackTrace();
        }
        return NOT_FOUND;
    }

    public boolean isContainsTag (InputTagList inputListOfTags, String searchedElement){
        try {
            if (isListOfTags(inputListOfTags) && isTag(searchedElement)) {
                if (inputListOfTags.contains(searchedElement))
                    return true;
                else
                    return false;
            } else
                return false;
        } catch (ExceptionListConsistsNonTags ex){
            ex.getStackTrace();
            return false;
        } catch (Exception ex){
            ex.getStackTrace();
            return false;
        }
    }

    public boolean isContainsTag (String searchedElement){
        try {
            if (isTag(searchedElement))
                if (this.outputListOfTags.contains(searchedElement))
                    return true;
                else
                    return false;
        }catch (Exception ex){
            ex.getStackTrace();
        }
        return false;
    }

    public String searchTagByIndexForward (int beginIndex, String[] searchingElement) throws Exception {
        if (beginIndex > outputListOfTags.size()) throw new Exception("Not correct index \'" + beginIndex + "\' for searching in outputListOfTags");
        for (int i = beginIndex; i < outputListOfTags.size(); i++) {
            for (int j = 0; j < searchingElement.length; j++)
                if(outputListOfTags.get(i).toString().contains(searchingElement[j]))
                    return outputListOfTags.get(i).toString();
        }
        return "";
    }

    public String searchTagWithIndexBackward(int beginIndex, String[] searchingElement) throws Exception {
        if (beginIndex == 0) throw new Exception("Not correct index \'" + beginIndex + "\' for searching in outputListOfTags");
        for (int i = beginIndex; i > 0; i--) {
            for (int j = 0; j < searchingElement.length; j++)
                if(outputListOfTags.get(i).toString().contains(searchingElement[j]))
                    return outputListOfTags.get(i).toString();
        }
        return "";
    }
}
