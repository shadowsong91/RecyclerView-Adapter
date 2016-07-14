package com.shadowsong.recyclerviewadapter.data;

import java.util.ArrayList;

import android.text.TextUtils;

/**
 * Created by zhoumo on 16/3/30.
 */
public class ListWrapper {

  public static final int STATUS_DONE = 0;
  public static final int STATUS_LOADING = 1;
  public static final int STATUS_ERROR = 2;
  protected ArrayList<Object> list;
  private OnDataPrepareListener onDataPrepareListener;
  private boolean hasMoreData = false;
  private int status;
  SourceLoader sourceLoader;
  String dataCursor;

  public ListWrapper(SourceLoader loader) {
    sourceLoader = loader;
    list = new ArrayList<>();
  }

  public void setSourceLoader(SourceLoader loader) {
    sourceLoader = loader;
  }

  public void load() {
    if (status == STATUS_LOADING) {
      return;
    }
    status = STATUS_LOADING;
    dataCursor = "";
    sourceLoader.load(dataCursor, new SourceLoadCallback() {
      @Override
      public void onSourceLoaded(String cursor, ArrayList<Object> data, boolean hasMore) {
        if (data == null) {
          status = STATUS_ERROR;
        } else {
          status = STATUS_DONE;
          dataCursor = cursor;
          hasMoreData(hasMore);
        }
        data(data);
      }
    });
    if (onDataPrepareListener != null) {
      onDataPrepareListener.onLoad(this);
    }
  }

  public void loadMore() {
    if (status == STATUS_LOADING) {
      return;
    }
    if (TextUtils.isEmpty(dataCursor)) {
      return;
    }
    status = STATUS_LOADING;
    sourceLoader.load(dataCursor, new SourceLoadCallback() {
      @Override
      public void onSourceLoaded(String cursor, ArrayList<Object> data, boolean hasMore) {
        if (data == null) {
          status = STATUS_ERROR;
          notifyDataPrepared();
        } else {
          status = STATUS_DONE;
          dataCursor = cursor;
          hasMoreData(hasMore);
          append(data);
        }
      }
    });
    if (onDataPrepareListener != null) {
      onDataPrepareListener.onLoadMore(this);
    }
  }


  public boolean hasMoreData() {
    return hasMoreData;
  }

  public void hasMoreData(boolean hasMoreData) {
    this.hasMoreData = hasMoreData;
  }

  public int status() {
    return status;
  }

  public void setOnDataPrepareListener(OnDataPrepareListener l) {
    onDataPrepareListener = l;
  }

  public Object dataAt(int index) {
    return list == null || index >= list.size() ? null : list.get(index);
  }

  public void data(ArrayList list) {
    this.list = list;
    notifyDataPrepared();
  }


  public void notifyDataPrepared() {
    if (onDataPrepareListener != null) {
      onDataPrepareListener.onPrepared(this);
    }
  }


  public int size() {
    return list == null ? 0 : list.size();
  }

  public void add(Object data) {
    list.add(data);
    notifyDataPrepared();
  }

  public void add(int index, Object data) {
    list.add(index, data);
    notifyDataPrepared();
  }

  public void append(ArrayList<Object> list) {
    if (list == null) {
      notifyDataPrepared();
      return;
    }
    this.list.addAll(list);
    notifyDataPrepared();
  }

}
