/*
 *    Copyright 2017. iota9star
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package star.iota.acgrip.ui.item

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import jp.wasabeef.recyclerview.animators.LandingAnimator
import star.iota.acgrip.Category
import star.iota.acgrip.MessageBar
import star.iota.acgrip.R
import star.iota.acgrip.base.BaseToolbarFragment
import star.iota.acgrip.ui.MainActivity


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

    override fun doSome() {
        initConfig()
        initRecyclerView()
        initRefreshLayout()
    }

    private fun initConfig() {
        isRefresh = false
        isLoading = false
        presenter = ItemPresenter(this)
        page = 1
        type = arguments.getInt(PARAMS_TYPE, 0)
        param = arguments.getString(PARAMS_KEY, "")
        setTitle(arguments.getString(PARAMS_TITLE, getString(R.string.app_name)))
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
            Category.SEARCH.id -> presenter?.search(param, page)
            Category.URL.id -> presenter?.request(param, page)
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
        private val PARAMS_TITLE = "title"
        private val PARAMS_TYPE = "type"
        private val PARAMS_KEY = "key"

        fun newInstance(type: Int?, title: String?): ItemFragment {
            val fragment = ItemFragment()
            val bundle = Bundle()
            bundle.putInt(PARAMS_TYPE, type!!)
            bundle.putString(PARAMS_TITLE, title)
            fragment.arguments = bundle
            return fragment
        }

        fun newInstance(type: Int?, key: String?, title: String?): ItemFragment {
            val fragment = ItemFragment()
            val bundle = Bundle()
            bundle.putInt(PARAMS_TYPE, type!!)
            bundle.putString(PARAMS_TITLE, title)
            bundle.putString(PARAMS_KEY, key)
            fragment.arguments = bundle
            return fragment
        }
    }
}
