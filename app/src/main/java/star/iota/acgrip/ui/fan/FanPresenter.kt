package star.iota.acgrip.ui.fan

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import star.iota.acgrip.service.DataRepository

internal class FanPresenter(private val view: FanContract.View) : FanContract.Presenter {

    companion object {
        private val compositeDisposable = CompositeDisposable()
    }

    override fun unsubscribe() {
        Companion.compositeDisposable.clear()
    }

    override fun request(url: String) {
        Companion.compositeDisposable.add(
                Flowable.just(DataRepository())
                        .map { service -> service.getFans(url) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { items ->
                                    if (items != null) {
                                        if (items.isEmpty()) view.noData()
                                        else view.success(items)
                                    } else {
                                        view.error("未知错误")
                                    }
                                },
                                { error ->
                                    view.error(error.message)
                                }
                        )
        )
    }
}
