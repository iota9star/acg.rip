package star.iota.acgrip.ui.fan;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import star.iota.acgrip.MessageBar;
import star.iota.acgrip.R;
import star.iota.acgrip.base.BaseFragment;


public class FanFragment extends BaseFragment implements FanContract.View {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private FanPresenter presenter;
    private FanAdapter fanAdapter;
    private boolean isLoading;
    private String url;

    public static FanFragment newInstance(String url, String title) {
        FanFragment fragment = new FanFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void init() {
        initConfig();
        initRecyclerView();
        initRefreshLayout();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_item;
    }

    private void initConfig() {
        isLoading = false;
        presenter = new FanPresenter(this);
        Bundle arguments = getArguments();
        url = arguments.getString("url");
        setTitle(arguments.getString("title", getString(R.string.app_name)));
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new LandingAnimator());
        fanAdapter = new FanAdapter();
        recyclerView.setAdapter(fanAdapter);
    }

    private void initRefreshLayout() {
        refreshLayout.autoRefresh();
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableAutoLoadmore(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (checkIsLoading()) {
                    return;
                }
                isLoading = true;
                fanAdapter.clear();
                presenter.request(url);
            }
        });
    }

    private boolean checkIsLoading() {
        if (isLoading) {
            MessageBar.create(mContext, "数据正在加载中，请等待...");
            return true;
        }
        return false;
    }

    @Override
    public void success(List<FansBean> items) {
        refreshLayout.finishRefresh(true);
        isLoading = false;
        fanAdapter.add(items);
    }

    @Override
    public void error() {
        otherDeal("数据加载出错？再试试...");
    }

    @Override
    public void noData() {
        otherDeal("当前选项貌似已经没有数据了？");
    }

    private void otherDeal(String content) {
        MessageBar.create(mContext, content);
        refreshLayout.finishRefresh();
        isLoading = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.cancel();
        }
    }
}
