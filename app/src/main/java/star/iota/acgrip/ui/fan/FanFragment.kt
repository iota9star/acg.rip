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

package star.iota.acgrip.ui.fan

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import jp.wasabeef.recyclerview.animators.LandingAnimator
import star.iota.acgrip.MessageBar
import star.iota.acgrip.R
import star.iota.acgrip.base.BaseToolbarFragment
import star.iota.acgrip.ui.MainActivity


class FanFragment : BaseToolbarFragment(), FanContract.View {

    override fun getContainerViewId(): Int {
        return R.layout.fragment_item
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: SmartRefreshLayout
    private var presenter: FanPresenter? = null
    private lateinit var fanAdapter: FanAdapter
    private var isLoading: Boolean = false
    private lateinit var url: String

    override fun doSome() {
        initConfig()
        initRecyclerView()
        initRefreshLayout()
    }

    private fun initConfig() {
        presenter = FanPresenter(this)
        url = arguments.getString(PARAMS_URL)
        setTitle(arguments.getString(PARAMS_TITLE, getString(R.string.app_name)))
    }

    private fun initRecyclerView() {
        recyclerView = containerView!!.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = LandingAnimator()
        fanAdapter = FanAdapter()
        recyclerView.adapter = fanAdapter
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
        refreshLayout.isEnableLoadmore = false
        refreshLayout.isEnableAutoLoadmore = true
        refreshLayout.setOnRefreshListener(OnRefreshListener {
            if (checkIsLoading()) {
                return@OnRefreshListener
            }
            isLoading = true
            fanAdapter.clear()
            presenter?.request(url)
        })
    }

    private fun checkIsLoading(): Boolean {
        if (isLoading) {
            MessageBar.create(activity, "数据正在加载中，请等待...")
            return true
        }
        return false
    }

    override fun success(fans: List<FansBean>) {
        refreshLayout.finishRefresh(true)
        isLoading = false
        fanAdapter.add(fans)
    }

    override fun error(error: String?) {
        otherDeal(error)
    }

    override fun noData() {
        otherDeal("当前选项貌似已经没有数据了？")
    }

    private fun otherDeal(content: String?) {
        MessageBar.create(activity, content)
        refreshLayout.finishRefresh()
        isLoading = false
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.unsubscribe()
    }

    companion object {
        private val PARAMS_TITLE = "title"
        private val PARAMS_URL = "url"
        fun newInstance(url: String?, title: String?): FanFragment {
            val fragment = FanFragment()
            val bundle = Bundle()
            bundle.putString("url", url)
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }
}
