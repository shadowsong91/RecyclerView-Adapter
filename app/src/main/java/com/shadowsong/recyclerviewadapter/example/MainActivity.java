package com.shadowsong.recyclerviewadapter.example;

import java.util.ArrayList;

import com.shadowsong.recyclerviewadapter.BaseRecyclerAdapter;
import com.shadowsong.recyclerviewadapter.data.ListWrapper;
import com.shadowsong.recyclerviewadapter.data.SourceLoadCallback;
import com.shadowsong.recyclerviewadapter.data.SourceLoader;
import com.shadowsong.recyclerviewadapter.viewholder.BaseViewHolder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initList();
  }

  private boolean initErrorOccurred = false;
  private boolean loadMoreErrorOccurred = false;

  private void initList() {
    final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    recyclerView
        .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    ListWrapper listWrapper = new ListWrapper(new SourceLoader() {
      @Override
      public void load(final String cursor, final SourceLoadCallback callback) {
        // assume loading data cost 2 seconds, so we can see the loading status
        recyclerView.postDelayed(new Runnable() {
          @Override
          public void run() {
            int start = 0;
            if (!TextUtils.isEmpty(cursor)) {
              start = Integer.valueOf(cursor);
            }
            ArrayList<Object> list = new ArrayList<>();
            for (int i = 0; i < 20; i++) {// add 20 items each time
              list.add("" + (start + i));
            }
            // cursor will be used as a start when loading more
            // assume error occurred when start == 0 and start == 40
            if (start == 0 && !initErrorOccurred) {
              // null means error, cursor and hasMore will be ignored when error
              initErrorOccurred = true;
              callback.onSourceLoaded("" + (start + 20), null, false);
            } else if (start == 40 && !loadMoreErrorOccurred) {
              loadMoreErrorOccurred = true;
              callback.onSourceLoaded("" + (start + 20), null, false);
            } else {
              callback.onSourceLoaded("" + (start + 20), list, start < 200);
            }

          }
        }, 2000);// assume loading data cost 2 seconds, so we can see the loading status
      }
    });

    DataAdapter adapter = new DataAdapter();
    adapter.setData(listWrapper);
    recyclerView.setAdapter(adapter);
    listWrapper.load();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    // noinspection SimplifiableIfStatement
    if (id == com.shadowsong.recyclerviewadapter.example.R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  class DataAdapter extends BaseRecyclerAdapter {

    @Override
    protected BaseViewHolder onCreateContentViewHolder(ViewGroup parent, int viewType) {
      return new ContentViewHolder(parent);
    }

    @Override
    protected BaseViewHolder onCreateErrorHolder(ViewGroup parent) {
      return new TextViewHolder(parent, "ERROR! Click to reload");
    }

    @Override
    protected BaseViewHolder onCreateLoadingMoreErrorViewHolder(ViewGroup parent) {
      return new TextViewHolder(parent, "Loading more ERROR! Click to reload");
    }

    @Override
    protected BaseViewHolder onCreatePlaceholder(ViewGroup parent) {
      return new TextViewHolder(parent, "Loading...");
    }

    @Override
    protected BaseViewHolder onCreateLoadingMoreViewHolder(ViewGroup parent) {
      return new TextViewHolder(parent, "Loading more...");
    }

    @Override
    protected int getErrorHolderCount() {
      return 3;
    }

    @Override
    protected int getPlaceholderCount() {
      return 20;
    }

    @Override
    protected boolean usePlaceholder() {
      return true;
    }

    @Override
    protected boolean useErrorHolder() {
      return true;
    }
  }

  class TextViewHolder extends BaseViewHolder {

    private TextView contentTextView;
    private String content;

    public TextViewHolder(ViewGroup parent, String content) {
      super(parent, R.layout.cell_content);
      this.content = content;
    }

    @Override
    public void onInitView(View view) {
      contentTextView = (TextView) view.findViewById(R.id.content_tv);
    }

    @Override
    public void fillData(int position, Object data) {
      contentTextView.setText(content);
    }
  }

  class ContentViewHolder extends BaseViewHolder {

    private TextView contentTextView;

    public ContentViewHolder(ViewGroup parent) {
      super(parent, R.layout.cell_content);
    }

    @Override
    public void onInitView(View view) {
      contentTextView = (TextView) view.findViewById(R.id.content_tv);
    }

    /**
     *
     * @param position
     * @param data item from the 2nd parameter in SourceLoaderCallback
     */
    @Override
    public void fillData(int position, Object data) {
      contentTextView.setText("Item " + data);
    }
  }


}
