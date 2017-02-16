package com.appbook.book.entity;

/**
 * Created by zhangzhongping on 16/10/31.
 */

public class NnumberAll {
    private long TIME_END;
    private int NUMBER;
    private int currentPage;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTIME_END() {
        return TIME_END;
    }

    public int getNUMBER() {
        return NUMBER;
    }

    public void setTIME_END(long TIME_END) {
        this.TIME_END = TIME_END;
    }

    public void setNUMBER(int NUMBER) {
        this.NUMBER = NUMBER;
    }
}
