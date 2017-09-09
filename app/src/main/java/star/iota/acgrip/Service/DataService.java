package star.iota.acgrip.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import star.iota.acgrip.ui.item.ItemBean;

public class DataService {

    public List<ItemBean> get(int type, int page) {
        String baseUrl;
        if (type == 0) {
            baseUrl = "https://acg.rip/page/" + page;
        } else {
            baseUrl = "https://acg.rip/" + type + "/page/" + page;
        }
        return get(baseUrl);
    }

    public List<ItemBean> get(String url, int page) {
        String baseUrl = url + "/page/" + page;
        return get(baseUrl);
    }

    public List<ItemBean> search(String keyword, int page) {
        String baseUrl = "https://acg.rip/page/" + page + "?term=" + keyword;
        return get(baseUrl);
    }

    private List<ItemBean> get(String baseUrl) {
        System.out.println("url: " + baseUrl);
        try {
            List<ItemBean> beans = new ArrayList<>();
            Elements elements = Jsoup.connect(baseUrl)
                    .timeout(60000)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 UBrowser/6.1.3228.1 Safari/537.36")
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .get()
                    .select("body > div.container > table > tbody > tr");
            for (Element ele : elements) {
                String title = ele.select("td.title > span.title > a").text();
                String sub = ele.select("td.title > span.label.label-team > a").text();
                String subLink = ele.select("td.title > span.label.label-team > a").attr("abs:href");
                String url = ele.select("td.title > span.title > a").attr("abs:href");
                String download = ele.select("td.action > a").attr("abs:href");
                String date = ele.select("td.date > div:nth-child(2) > time").text();
                String size = ele.select("td.size").text();
                String linkCount = ele.select("td.peers > div.row > div.seed > span").text() + "/"
                        + ele.select("td.peers > div.row > div.leech > span").text() + "-"
                        + ele.select("td.peers > div.done > span").text();
                beans.add(new ItemBean(title, sub, subLink, url, download, date, size, linkCount));
            }
            return beans;
        } catch (Exception e) {
            return null;
        }
    }
}
