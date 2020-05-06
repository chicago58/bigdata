package com.wolf;

public class WordWithCount1 {
    public String word;
    public long count;

    public WordWithCount1(String word, long count) {
        this.word = word;
        this.count = count;
    }

    public WordWithCount1() {
    }

    @Override
    public String toString() {
        return "WordWithCount{" +
                "word='" + word + '\'' +
                ", count=" + count +
                '}';
    }
}
