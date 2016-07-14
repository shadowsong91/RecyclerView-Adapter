package com.shadowsong.recyclerviewadapter.data;

import java.util.ArrayList;

/**
 * Created by zhoumo on 16/5/9.
 */
public interface SourceLoadCallback {

    void onSourceLoaded(String cursor, ArrayList<Object> data, boolean hasMore);
}
