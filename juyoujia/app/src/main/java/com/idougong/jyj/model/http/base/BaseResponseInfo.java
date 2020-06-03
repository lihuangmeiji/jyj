package com.idougong.jyj.model.http.base;

/**
 * Created by zhaotun .
 */

public class BaseResponseInfo<T> extends BaseStatus {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
