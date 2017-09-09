package star.iota.acgrip.bean;

public class ItemBean {
    private final String publisher;
    private final String releaseTime;
    private final String title;
    private final String url;
    private final String download;
    private final String size;
    private final String link;

    public ItemBean(String publisher, String releaseTime, String title, String url, String download, String size, String link) {
        this.publisher = publisher;
        this.releaseTime = releaseTime;
        this.title = title;
        this.url = url;
        this.download = download;
        this.size = size;
        this.link = link;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public String getTitle() {
        return title;
    }


    public String getUrl() {
        return url;
    }

    public String getDownload() {
        return download;
    }


    public String getSize() {
        return size;
    }


    public String getLink() {
        return link;
    }

}
