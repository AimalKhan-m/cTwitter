package com.example.poster_1;

import java.util.List;

public class ArrayPojo {
    private List<String> retweeters ;

    public ArrayPojo(List<String> retweeters) {
        this.retweeters = retweeters;
    }

    public void setRetweeters(List<String> retweeters) {
        this.retweeters = retweeters;
    }

    public List<String> getRetweeters() {
        return retweeters;
    }
}
