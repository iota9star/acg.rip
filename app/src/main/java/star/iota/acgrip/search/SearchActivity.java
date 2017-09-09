package star.iota.acgrip.search;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.bezierlayout.BezierLayout;

import java.util.List;

import jp.wasabeef.recyclerview.animators.LandingAnimator;
import star.iota.acgrip.R;
import star.iota.acgrip.SnackbarUtils;
import star.iota.acgrip.adapter.LinearLayoutItemDecoration;
import star.iota.acgrip.adapter.ShowRecyclerViewAdapter;
import star.iota.acgrip.bean.ItemBean;

public class SearchActivity extends AppCompatActivity implements SearchContract.View {
    private TwinklingRefreshLayout mTwinklingRefreshLayout;
    private int mPage;
    private SearchPresenter mPresenter;
    private ShowRecyclerViewAdapter mShowRecyclerViewAdapter;
    private EditText mEditTextKeyword;
    private String keywords;
    private boolean isSearching;
    private boolean isLoading;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });
        initConfig();
        initSearchBar();
        initRecyclerView();
        initRefreshLayout();
    }

    private void initSearchBar() {
        mEditTextKeyword = (EditText) findViewById(R.id.edit_text_keyword);
        ImageButton mImageButtonSearch = (ImageButton) findViewById(R.id.image_button_search);
        mImageButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTwinklingRefreshLayout.startRefresh();
            }
        });
    }

    private void initConfig() {
        mPresenter = new SearchPresenter(this);
        mPage = 1;
        isSearching = false;
        isLoading = false;
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new LandingAnimator());
        recyclerView.addItemDecoration(new LinearLayoutItemDecoration(mContext.getResources().getDimensionPixelOffset(R.dimen.v4dp)));
        mShowRecyclerViewAdapter = new ShowRecyclerViewAdapter();
        recyclerView.setAdapter(mShowRecyclerViewAdapter);
    }

    private void initRefreshLayout() {
        mTwinklingRefreshLayout = (TwinklingRefreshLayout) findViewById(R.id.twinkling_refresh_layout);
        mTwinklingRefreshLayout.setAutoLoadMore(true);
        BezierLayout headerView = new BezierLayout(mContext);
        mTwinklingRefreshLayout.setHeaderView(headerView);
        mTwinklingRefreshLayout.setMaxHeadHeight(mContext.getResources().getDimensionPixelOffset(R.dimen.v64dp));
        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                if (checkKeywordsIsEmpty() || checkIsLoading()) {
                    refreshLayout.finishRefreshing();
                    return;
                }
                isSearching = true;
                isLoading = true;
                mPresenter.request(keywords, 1);
                mShowRecyclerViewAdapter.clear();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (checkKeywordsIsEmpty() || checkIsLoading()) {
                    refreshLayout.finishLoadmore();
                    return;
                }
                isSearching = false;
                isLoading = true;
                mPresenter.request(keywords, mPage);
            }
        });
    }

    private boolean checkKeywordsIsEmpty() {
        keywords = mEditTextKeyword.getText().toString();
        if (keywords.trim().equals("")) {
            SnackbarUtils.create(this, "请输入搜索关键字");
            return true;
        } else {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                IBinder windowToken = SearchActivity.this.getCurrentFocus().getWindowToken();
                if (windowToken != null) {
                    imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return false;
    }

    private boolean checkIsLoading() {
        if (isLoading) {
            SnackbarUtils.create(this, "数据正在加载中，慢点来...");
            return true;
        }
        return false;
    }

    @Override
    public void success(List<ItemBean> items) {
        if (isSearching) {
            mPage = 2;
            mTwinklingRefreshLayout.finishRefreshing();
        } else {
            mPage++;
            mTwinklingRefreshLayout.finishLoadmore();
        }
        isLoading = false;
        mShowRecyclerViewAdapter.add(items);
    }

    @Override
    public void error() {
        otherDeal("数据加载出错？再试试...");
    }

    @Override
    public void noData() {
        otherDeal("貌似没有数据了？");
    }

    private void otherDeal(String content) {
        SnackbarUtils.create(this, content);
        finishSearching();
        isLoading = false;
    }

    private void finishSearching() {
        if (isSearching) {
            mTwinklingRefreshLayout.finishRefreshing();
        } else {
            mTwinklingRefreshLayout.finishLoadmore();
        }
    }
}
