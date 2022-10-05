package org.example.InteractiveNews_v2.ParseWorking;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SetExpressionHTMLmnemonic {
    public static final Map<String, String> HTMLmnemonic = Stream.of(new String[][] {
            {"&iquest;",   "?"},
            {"&iexcl;",    "?"},
            {"&laquo;",    "«"},
            {"&raquo;",   "»"},
            {"&lsaquo;",  "‹"},
            {"&rsaquo;",  "›"},
            {"&quot;",  "\""},
            {"&lsquo;",   "‘"},
            {"&rsquo;",   "’"},
            {"&ldquo;",   "“"},
            {"&rdquo;",   "”"},
            {"&sbquo;",   "‚"},
            {"&bdquo;",   "„"},
            {"&sect;",    "§"},
            {"&para;",    "¶"},
            {"&dagger;",    "†"},
            {"&Dagger;",  "‡"},
            {"&bull;",  "•"},
            {"&middot;",    "·"},
            {"&mdash;",   "—"},
            {"&ndash;",   "–"},
            {"&hellip;",  "…"},
            {"&nbsp;",    " "}
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

    public Map<String, String> HTMLmnemonicMap(){
        return this.HTMLmnemonic;
    }

}
