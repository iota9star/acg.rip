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
