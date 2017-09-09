package star.iota.acgrip.ui.item;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import star.iota.acgrip.Contracts;
import star.iota.acgrip.MessageBar;
import star.iota.acgrip.R;
import star.iota.acgrip.base.BaseFragment;


public class ItemFragment extends BaseFragment implements ItemContract.View {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private int page;
    private int type;
    private ItemPresenter presenter;
    private ItemAdapter itemAdapter;
    private boolean isRefresh;
    private boolean isLoading;
    private String param;

    public static ItemFragment newInstance(int type, String title) {
        ItemFragment fragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ItemFragment newInstance(int type, String param, String title) {
        ItemFragment fragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("title", title);
        bundle.putString("param", param);
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
        isRefresh = false;
        isLoading = false;
        presenter = new ItemPresenter(this);
        page = 1;
        Bundle arguments = getArguments();
        type = arguments.getInt("type", 0);
        param = arguments.getString("param");
        setTitle(arguments.getString("title", getString(R.string.app_name)));
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new LandingAnimator());
        itemAdapter = new ItemAdapter();
        recyclerView.setAdapter(itemAdapter);
    }

    private void initRefreshLayout() {
        refreshLayout.autoRefresh();
        refreshLayout.setEnableAutoLoadmore(true);
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                if (checkIsLoading()) {
                    return;
                }
                isRefresh = false;
                isLoading = true;
                request(page);
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (checkIsLoading()) {
                    return;
                }
                isRefresh = true;
                isLoading = true;
                itemAdapter.clear();
                request(1);
            }
        });
    }

    private void request(int page) {
        switch (type) {
            case Contracts.TYPE_SEARCH:
                presenter.search(param, page);
                break;
            case Contracts.TYPE_SUB:
                presenter.request(param, page);
                break;
            default:
                presenter.request(type, page);
        }
    }

    private boolean checkIsLoading() {
        if (isLoading) {
            MessageBar.create(mContext, "数据正在加载中，请等待...");
            return true;
        }
        return false;
    }

    @Override
    public void success(List<ItemBean> items) {
        if (isRefresh) {
            page = 2;
            refreshLayout.finishRefresh(true);
        } else {
            page++;
            refreshLayout.finishLoadmore(true);
        }
        isLoading = false;
        itemAdapter.add(items);
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
        finishSearching();
        isLoading = false;
    }

    private void finishSearching() {
        if (isRefresh) {
            refreshLayout.finishRefresh();
        } else {
            refreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.cancel();
        }
    }
}
