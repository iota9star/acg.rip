package star.iota.acgrip.fragment;

import java.util.List;

import star.iota.acgrip.bean.ItemBean;

public interface ItemContract {

    interface View {

        void success(List<ItemBean> items);

        void error();

        void noData();
    }

    interface Presenter {
        void request(int type, int page);
    }
}
