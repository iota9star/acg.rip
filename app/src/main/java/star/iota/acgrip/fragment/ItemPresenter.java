package star.iota.acgrip.fragment;

import android.os.AsyncTask;

import java.util.List;

import star.iota.acgrip.bean.ItemBean;
import star.iota.acgrip.data.DataSource;

public class ItemPresenter implements ItemContract.Presenter {

    private final ItemContract.View view;

    public ItemPresenter(ItemContract.View view) {
        this.view = view;
    }

    @Override
    public void request(final int type, final int page) {
        new AsyncTask<Void, Void, List<ItemBean>>() {
            @Override
            protected List<ItemBean> doInBackground(Void... params) {
                DataSource source = new DataSource();
                return source.get(type, page);
            }

            @Override
            protected void onPostExecute(List<ItemBean> items) {
                if (items == null) {
                    view.error();
                } else if (items.size() == 0) {
                    view.noData();
                } else {
                    view.success(items);
                }
            }
        }.execute();
    }
}
