package star.iota.acgrip.ui.item

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import jp.wasabeef.recyclerview.animators.LandingAnimator
import star.iota.acgrip.Menu
import star.iota.acgrip.MessageBar
import star.iota.acgrip.R
import star.iota.acgrip.ui.MainActivity
import star.iota.jptv.base.BaseToolbarFragment


class ItemFragment : BaseToolbarFragment(), ItemContract.View {
    override fun getContainerViewId(): Int {
        return R.layout.fragment_item
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: SmartRefreshLayout
    private var page: Int = 0
    private var type: Int = 0
    private var presenter: ItemPresenter? = null
    private lateinit var itemAdapter: ItemAdapter
    private var isRefresh: Boolean = false
    private var isLoading: Boolean = false
    private lateinit var param: String

    override fun init() {
        initConfig()
        initRecyclerView()
        initRefreshLayout()
    }

    private fun initConfig() {
        isRefresh = false
        isLoading = false
        presenter = ItemPresenter(this)
        page = 1
        type = arguments.getInt("type", 0)
        param = arguments.getString("param", "")
        setTitle(arguments.getString("title", getString(R.string.app_name)))
    }

    private fun initRecyclerView() {
        recyclerView = containerView!!.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = LandingAnimator()
        itemAdapter = ItemAdapter()
        recyclerView.adapter = itemAdapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var currentState: Int = -1
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                if (currentState == newState) return
                currentState = newState
                (activity as MainActivity).floatToolbar(newState != RecyclerView.SCROLL_STATE_IDLE)
            }
        })
    }

    private fun initRefreshLayout() {
        refreshLayout = containerView!!.findViewById(R.id.refresh_layout)
        refreshLayout.autoRefresh()
        refreshLayout.isEnableAutoLoadmore = true
        refreshLayout.setOnLoadmoreListener(OnLoadmoreListener {
            if (checkIsLoading()) {
                return@OnLoadmoreListener
            }
            isRefresh = false
            isLoading = true
            request(page)
        })
        refreshLayout.setOnRefreshListener(OnRefreshListener {
            if (checkIsLoading()) {
                return@OnRefreshListener
            }
            isRefresh = true
            isLoading = true
            itemAdapter.clear()
            request(1)
        })
    }

    private fun request(page: Int) {
        when (type) {
            Menu.SEARCH.id -> presenter?.search(param, page)
            Menu.URL.id -> presenter?.request(param, page)
            else -> presenter?.request(type, page)
        }
    }

    private fun checkIsLoading(): Boolean {
        if (isLoading) {
            MessageBar.create(activity, "数据正在加载中，请等待...")
            return true
        }
        return false
    }

    override fun success(items: List<ItemBean>) {
        if (isRefresh) {
            page = 2
            refreshLayout.finishRefresh(true)
        } else {
            page++
            refreshLayout.finishLoadmore(true)
        }
        isLoading = false
        itemAdapter.add(items)
    }

    override fun error(error: String?) {
        otherDeal(error)
    }

    override fun noData() {
        otherDeal("当前选项貌似已经没有数据了？")
    }

    private fun otherDeal(content: String?) {
        MessageBar.create(activity, content)
        finish()
        isLoading = false
    }

    private fun finish() {
        if (isRefresh) {
            refreshLayout.finishRefresh()
        } else {
            refreshLayout.finishLoadmore()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.unsubscribe()
    }

    companion object {
        fun newInstance(type: Int?, title: String?): ItemFragment {
            val fragment = ItemFragment()
            val bundle = Bundle()
            bundle.putInt("type", type!!)
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }

        fun newInstance(type: Int?, param: String?, title: String?): ItemFragment {
            val fragment = ItemFragment()
            val bundle = Bundle()
            bundle.putInt("type", type!!)
            bundle.putString("title", title)
            bundle.putString("param", param)
            fragment.arguments = bundle
            return fragment
        }
    }
}
