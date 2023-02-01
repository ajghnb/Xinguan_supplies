package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.Page;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author 18237
 */
@Getter
@Setter
public class PageData<T> {

    private static final long serialVersionUID = -9202109574544652243L;
    /** 总记录数 */
    private long total;

    /** 第几页 */
    private int pageNum;

    /** 每页记录数 */
    private int pageSize;

    /** 总页数 */
    private int pages;

    /** 结果集 */
    private List<T> rows;

    public PageData() {}

    /**
     * 包装Page对象. 因为直接返回Page对象,
     * 在JSON处理以及其他情况下会被当成List来处理,
     * 而出现一些问题。
     */
    public PageData(List<T> list) {
        if (list instanceof Page) {
            Page<T> page = (Page<T>) list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.rows = page.getResult();
        }
    }

    public PageData(Page<T> page) {
        this.pageNum = page.getPageNum();
        this.pageSize = page.getPageSize();
        this.total = page.getTotal();
        this.pages = page.getPages();
        this.rows = page.getResult();
    }

    public PageData(PageData<?> data, List<T> list) {
        this.setTotal(data.getTotal());
        this.setPageNum(data.getPageNum());
        this.setPageSize(data.getPageSize());
        this.setPages(data.getPages());
        this.setRows(list);
    }

    public PageData(PageVo<T> page){
        this.pageNum = page.getPageNum();
        this.pageSize = page.getPageSize();
        this.total = page.getTotal();
        this.pages = page.getPages();
        this.rows = page.getResult();
    }

    public <V> PageData<V> apply(Function<List<T>, List<V>> fun) {
        List<V> v = fun.apply(this.rows);
        return new PageData<>(this, v);
    }

    public <V> PageData<V> convert(Function<T, V> fun) {
        List<V> v = this.rows.stream().map(fun).collect(Collectors.toList());
        return new PageData<>(this, v);
    }
}
