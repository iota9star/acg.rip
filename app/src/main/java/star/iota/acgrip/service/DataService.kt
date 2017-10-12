package star.iota.acgrip.service

import star.iota.acgrip.ui.fan.FansBean
import star.iota.acgrip.ui.item.ItemBean

interface DataService {

    fun getItems(type: Int, page: Int): List<ItemBean>?

    fun getItems(url: String, page: Int): List<ItemBean>?

    fun search(keyword: String, page: Int): List<ItemBean>?

    fun getItems(baseUrl: String): List<ItemBean>?

    fun getFans(baseUrl: String): List<FansBean>?
}