package com.shadowsong.recyclerviewadapter.data;

/**
 * Created by zhoumo on 16/5/9.
 */
public interface SourceLoader {

    void load(String cursor, SourceLoadCallback callback);

}
