package org.example.InteractiveNews_v2.ParseWorking;

public class ExceptionListConsistsNonTags extends Exception{
    ExceptionListConsistsNonTags(){
        super("Incorrect List of tags. One or more elements doesn't contains symbols '<' in begin or '>' in end of list's element.");
    }
}
