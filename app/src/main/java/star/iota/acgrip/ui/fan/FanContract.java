package star.iota.acgrip.ui.fan;

import java.util.List;

interface FanContract {

    interface View {

        void success(List<FansBean> fans);

        void error();

        void noData();
    }

    interface Presenter {
        void request(String url);

        void cancel();
    }
}
