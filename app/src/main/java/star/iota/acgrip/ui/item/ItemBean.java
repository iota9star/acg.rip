package star.iota.acgrip.ui.item;

public class ItemBean {
    private final String title;
    private final String sub;
    private final String subLink;
    private final String url;
    private final String download;
    private final String date;
    private final String size;
    private final String linkCount;

    public ItemBean(String title, String sub, String subLink, String url, String download, String date, String size, String linkCount) {
        this.title = title;
        this.sub = sub;
        this.subLink = subLink;
        this.url = url;
        this.download = download;
        this.date = date;
        this.size = size;
        this.linkCount = linkCount;
    }

    @Override
    public String toString() {
        return "\n" + title
                + "\n\n发布者：" + sub
                + "\n\n时间：" + date
                + "\n\n文件大小：" + size
                + "\n\n连接情况：" + linkCount
                + "\n\n发布者地址：" + subLink
                + "\n\n详情地址：" + download.replace(".torrent", "")
                + "\n\n种子地址：" + download
                + "\n";
    }

    public String getTitle() {
        return title;
    }

    String getSub() {
        return sub;
    }

    String getSubLink() {
        return subLink;
    }

    String getUrl() {
        return url;
    }

    String getDownload() {
        return download;
    }

    String getDate() {
        return date;
    }

    String getSize() {
        return size;
    }

}
