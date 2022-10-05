package org.example.InteractiveNews_v2.ParseWorking;

public class MoreResult {
    private String tag;
    private Integer indexOfTag;

    MoreResult() {
        this.tag = "";
        this.indexOfTag = -1;
    }

    public void setMoreResult(String tag, Integer indexOfTag) {
        this.tag = tag;
        this.indexOfTag = indexOfTag;
    }

    public Integer getIndexOfTag() {
        return indexOfTag;
    }

    public String getTag() {
        return tag;
    }
}
