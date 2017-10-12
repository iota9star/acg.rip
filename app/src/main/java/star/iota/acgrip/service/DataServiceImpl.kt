package star.iota.acgrip.service

import org.jsoup.Jsoup
import star.iota.acgrip.ui.fan.FansBean
import star.iota.acgrip.ui.item.ItemBean
import java.util.*

class DataServiceImpl : DataService {

    override fun getItems(type: Int, page: Int): List<ItemBean>? {
        val baseUrl: String = if (type == 0) {
            "https://acg.rip/page/" + page
        } else {
            "https://acg.rip/$type/page/$page"
        }
        return getItems(baseUrl)
    }

    override fun getItems(url: String, page: Int): List<ItemBean>? {
        val baseUrl = url + "/page/" + page
        return getItems(baseUrl)
    }

    override fun search(keyword: String, page: Int): List<ItemBean>? {
        val baseUrl = "https://acg.rip/page/$page?term=$keyword"
        return getItems(baseUrl)
    }

    override fun getItems(baseUrl: String): List<ItemBean>? {
        val beans = ArrayList<ItemBean>()
        val elements = Jsoup.connect(baseUrl)
                .timeout(60000)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 UBrowser/6.1.3228.1 Safari/537.36")
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .get()
                .select("body > div.container > table > tbody > tr")
        for (ele in elements) {
            val title = ele.select("td.title > span.title > a").text()
            val sub = ele.select("td.title > span.label.label-team > a").text()
            val subLink = ele.select("td.title > span.label.label-team > a").attr("abs:href")
            val url = ele.select("td.title > span.title > a").attr("abs:href")
            val download = ele.select("td.action > a").attr("abs:href")
            val date = ele.select("td.date > div:nth-child(2) > time").text()
            val size = ele.select("td.size").text()
            val linkCount = (ele.select("td.peers > div.row > div.seed > span").text() + "/"
                    + ele.select("td.peers > div.row > div.leech > span").text() + "-"
                    + ele.select("td.peers > div.done > span").text())
            beans.add(ItemBean(title, sub, subLink, url, download, date, size, linkCount))
        }
        return beans
    }

    override fun getFans(baseUrl: String): List<FansBean>? {
        val list = ArrayList<FansBean>()
        val document = Jsoup.connect(baseUrl)
                .timeout(60000)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 UBrowser/6.1.3228.1 Safari/537.36")
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .get()
        val navsElements = document.select("#bangumi-box > ul > li")
        val fansElements = document.select("#bangumi-box > div.tab-content > div.tab-pane")
        for (i in 0..7) {
            val navElement = navsElements[i]
            val nav = navElement.select("a").text()
            val active = navElement.hasClass("active")
            val fans = ArrayList<FansBean.FanBean>()
            for (element in fansElements[i].select("a.label-bangumi")) {
                val name = element.text()
                val url = element.attr("abs:href")
                fans.add(FansBean.FanBean(name, url))
            }
            list.add(FansBean(nav, fans, active))
        }
        return list
    }
}
