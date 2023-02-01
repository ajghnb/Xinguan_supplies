package com.example.common.utils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 18237
 */
public class ListPageUtils {
    /**
     * 手动分页类
     *
     * @param datas
     * @param pageSize
     * @param pageNo
     * @param <T>
     * @return
     */
    public static <T> List<T> getPage(List<T> datas, int pageSize, int pageNo) {
        int startNum = (pageNo - 1) * pageSize + 1;
        if (startNum > datas.size()) {
            return null;
        }
        List<T> res = new ArrayList<>();
        int rum = datas.size() - startNum;
        if (rum < 0) {
            return null;
        }
        if (rum == 0) {
            int index = datas.size() - 1;
            res.add(datas.get(index));
            return res;
        }
        if (rum / pageSize >= 1) {
            for (int i = startNum; i < startNum + pageSize; i++) {
                res.add(datas.get(i - 1));
            }
            return res;
        } else if ((rum / pageSize == 0) && rum > 0) {
            for (int j = startNum; j <= datas.size(); j++) {
                res.add(datas.get(j - 1));
            }
            return res;
        } else {
            return null;
        }
    }
}
