package star.iota.acgrip.ui.item

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import star.iota.acgrip.service.DataRepository

internal class ItemPresenter(private val view: ItemContract.View) : ItemContract.Presenter {
    companion object {
        private val compositeDisposable = CompositeDisposable()
    }

    override fun unsubscribe() {
        Companion.compositeDisposable.clear()
    }

    override fun request(type: Int, page: Int) {
        Companion.compositeDisposable.add(
                Flowable.just(DataRepository())
                        .map { service -> service.getItems(type, page) }
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

    override fun request(url: String, page: Int) {
        Companion.compositeDisposable.add(
                Flowable.just(DataRepository())
                        .map { service -> service.getItems(url, page) }
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

    override fun search(keywords: String, page: Int) {
        Companion.compositeDisposable.add(
                Flowable.just(DataRepository())
                        .map { service -> service.search(keywords, page) }
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
