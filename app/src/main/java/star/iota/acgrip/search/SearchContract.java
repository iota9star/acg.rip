package star.iota.acgrip.search;

import java.util.List;

import star.iota.acgrip.bean.ItemBean;

public interface SearchContract {

    interface View {

        void success(List<ItemBean> items);

        void error();

        void noData();
    }

    interface Presenter {
        void request(String keyword, int page);
    }
}
