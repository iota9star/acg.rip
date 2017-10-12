package star.iota.acgrip.ui.fan

import star.iota.jptv.base.BasePresenter
import star.iota.jptv.base.BaseView

internal interface FanContract {
    interface View : BaseView {
        fun success(fans: List<FansBean>)
    }

    interface Presenter : BasePresenter {
        fun request(url: String)
    }
}
