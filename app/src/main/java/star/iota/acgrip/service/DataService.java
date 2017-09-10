package star.iota.acgrip.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import star.iota.acgrip.ui.fan.FansBean;
import star.iota.acgrip.ui.item.ItemBean;

public class DataService {

    public List<ItemBean> getItems(int type, int page) {
        String baseUrl;
        if (type == 0) {
            baseUrl = "https://acg.rip/page/" + page;
        } else {
            baseUrl = "https://acg.rip/" + type + "/page/" + page;
        }
        return getItems(baseUrl);
    }

    public List<ItemBean> getItems(String url, int page) {
        String baseUrl = url + "/page/" + page;
        return getItems(baseUrl);
    }

    public List<ItemBean> search(String keyword, int page) {
        String baseUrl = "https://acg.rip/page/" + page + "?term=" + keyword;
        return getItems(baseUrl);
    }

    private List<ItemBean> getItems(String baseUrl) {
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

    public List<FansBean> getFans(String baseUrl) {
        try {
            List<FansBean> list = new ArrayList<>();
            Document document = Jsoup.connect(baseUrl)
                    .timeout(60000)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 UBrowser/6.1.3228.1 Safari/537.36")
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .get();
            Elements navsElements = document.select("#bangumi-box > ul > li");
            Elements fansElements = document.select("#bangumi-box > div.tab-content > div.tab-pane");
            for (int i = 0; i < 8; i++) {
                Element navElement = navsElements.get(i);
                String nav = navElement.select("a").text();
                boolean active = navElement.hasClass("active");
                List<FansBean.FanBean> fans = new ArrayList<>();
                for (Element element : fansElements.get(i).select("a.label-bangumi")) {
                    String name = element.text();
                    String url = element.attr("abs:href");
                    fans.add(new FansBean.FanBean(name, url));
                }
                list.add(new FansBean(nav, fans, active));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
