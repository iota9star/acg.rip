package star.iota.acgrip.ui.item;

import java.util.List;

interface ItemContract {

    interface View {

        void success(List<ItemBean> items);

        void error();

        void noData();
    }

    interface Presenter {
        void request(int type, int page);

        void request(String url, int page);

        void search(String keywords, int page);

        void cancel();
    }
}
