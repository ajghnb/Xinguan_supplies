package com.example.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 18237
 */
@Data
public class PageVo<T>{

    private long total;

    private int pageNum;

    private int pageSize;

    private int pages;

    private List<T> result = new ArrayList<>();

    public PageVo(long total, int pageNum, int pageSize, List<T> data) {
        this.total = total;
        this.result = data;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pages = countPages(pageNum, pageSize, data);
    }

    private int countPages(int pageNum, int pageSize, List<T> data) {
        if (data.size() % pageSize == 0) {
            return data.size() / pageSize;
        } else {
            return data.size() / pageSize + 1;
        }
    }
}
