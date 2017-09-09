package star.iota.acgrip.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


public class ItemFragment extends Fragment implements ItemContract.View {
    private TwinklingRefreshLayout twinklingRefreshLayout;
    private int page;
    private int type;
    private ItemPresenter presenter;
    private ShowRecyclerViewAdapter showRecyclerViewAdapter;
    private boolean isRefresh;
    private boolean isLoading;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        initConfig();
        initRecyclerView(view);
        initRefreshLayout(view);
        return view;
    }

    public static ItemFragment newInstance(int type) {
        ItemFragment fragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initConfig() {
        isRefresh = false;
        isLoading = false;
        presenter = new ItemPresenter(this);
        page = 1;
        type = getArguments().getInt("type", 0);
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new LandingAnimator());
        recyclerView.addItemDecoration(new LinearLayoutItemDecoration(mContext.getResources().getDimensionPixelOffset(R.dimen.v4dp)));
        showRecyclerViewAdapter = new ShowRecyclerViewAdapter();
        recyclerView.setAdapter(showRecyclerViewAdapter);
    }

    private void initRefreshLayout(View view) {
        twinklingRefreshLayout = view.findViewById(R.id.twinkling_refresh_layout);
        twinklingRefreshLayout.startRefresh();
        twinklingRefreshLayout.setAutoLoadMore(true);
        BezierLayout headerView = new BezierLayout(mContext);
        twinklingRefreshLayout.setHeaderView(headerView);
        twinklingRefreshLayout.setMaxHeadHeight(mContext.getResources().getDimensionPixelOffset(R.dimen.v64dp));
        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                if (checkIsLoading()) {
                    refreshLayout.finishRefreshing();
                    return;
                }
                isRefresh = true;
                isLoading = true;
                presenter.request(type, 1);
                showRecyclerViewAdapter.clear();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (checkIsLoading()) {
                    refreshLayout.finishLoadmore();
                    return;
                }
                isRefresh = false;
                isLoading = true;
                presenter.request(type, page);
            }
        });
    }

    private boolean checkIsLoading() {
        if (isLoading) {
            SnackbarUtils.create(getActivity(), "数据正在加载中，慢点来...");
            return true;
        }
        return false;
    }

    @Override
    public void success(List<ItemBean> items) {
        if (isRefresh) {
            page = 2;
            twinklingRefreshLayout.finishRefreshing();
        } else {
            page++;
            twinklingRefreshLayout.finishLoadmore();
        }
        isLoading = false;
        showRecyclerViewAdapter.add(items);
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
        SnackbarUtils.create(mContext, content);
        finishSearching();
        isLoading = false;
    }

    private void finishSearching() {
        if (isRefresh) {
            twinklingRefreshLayout.finishRefreshing();
        } else {
            twinklingRefreshLayout.finishLoadmore();
        }
    }
}
