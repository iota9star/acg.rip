package star.iota.acgrip.ui.item

import star.iota.jptv.base.BasePresenter
import star.iota.jptv.base.BaseView

internal interface ItemContract {

    interface View : BaseView {
        fun success(items: List<ItemBean>)
    }

    interface Presenter : BasePresenter {
        fun request(type: Int, page: Int)

        fun request(url: String, page: Int)

        fun search(keywords: String, page: Int)
    }
}
