package com.wolf.udf.json;

public class MovieRateBean {
    private String movie;
    private String rate;
    private String uid;
    private String timeStamp;

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public MovieRateBean(String movie, String rate, String uid, String timeStamp) {
        this.movie = movie;
        this.rate = rate;
        this.uid = uid;
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return movie + '\t' +
                rate + '\t' +
                uid + '\t' +
                timeStamp;
    }
}
