package star.iota.acgrip.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import star.iota.acgrip.bean.ItemBean;

public class DataSource {

    public List<ItemBean> get(int type, int page) {
        String baseUrl;
        if (type == 0) {
            baseUrl = "https://acg.rip/page/" + page;
        } else {
            baseUrl = "https://acg.rip/" + type + "/page/" + page;
        }
        return remoteData(baseUrl);
    }

    public List<ItemBean> get(String keyword, int page) {
        String baseUrl = "https://acg.rip/page/" + page + "?term=" + keyword;
        return remoteData(baseUrl);
    }

    private List<ItemBean> remoteData(String baseUrl) {
        try {
            List<ItemBean> itemBeans = new ArrayList<>();
            Elements items = Jsoup.connect(baseUrl)
                    .timeout(10000)
                    .userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36")
                    .ignoreHttpErrors(true)
                    .get()
                    .select("body > div.container > table > tbody > tr");
            for (Element item : items) {
                String publisher = item.select("td.date.hidden-xs.hidden-sm > div:nth-child(1) > a").text();
                String releaseTime = item.select("td.date.hidden-xs.hidden-sm > div:nth-child(2)").text();
                String title = item.select("td.title > span > a").text();
                String url = item.select("td.title > span > a").attr("abs:href");
                String download = item.select("td.action > a").attr("abs:href");
                String size = item.select("td.size").text();
                String link = item.select("td.peers > div.row > div.seed > span").text() + "/"
                        + item.select("td.peers > div.row > div.leech > span").text() + "-"
                        + item.select("td.peers > div.done > span").text();
                ItemBean bean = new ItemBean(publisher, releaseTime, title, url, download, size, link);
                itemBeans.add(bean);
            }
            return itemBeans;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
