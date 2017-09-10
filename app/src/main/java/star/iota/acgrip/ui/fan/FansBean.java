package star.iota.acgrip.ui.fan;

import java.util.List;

public class FansBean {

    private final String week;
    private final List<FanBean> fans;
    private final boolean active;

    public FansBean(String week, List<FanBean> fans, boolean active) {
        this.week = week;
        this.fans = fans;
        this.active = active;
    }

    String getWeek() {
        return week;
    }

    List<FanBean> getFans() {
        return fans;
    }

    boolean isActive() {
        return active;
    }

    public static class FanBean {
        private final String name;
        private final String url;

        public FanBean(String name, String url) {
            this.name = name;
            this.url = url;
        }

        String getName() {
            return name;
        }

        String getUrl() {
            return url;
        }
    }

}
