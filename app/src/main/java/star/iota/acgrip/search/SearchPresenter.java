package star.iota.acgrip.search;

import android.os.AsyncTask;

import java.util.List;

import star.iota.acgrip.bean.ItemBean;
import star.iota.acgrip.data.DataSource;

public class SearchPresenter implements SearchContract.Presenter {

    private final SearchContract.View view;

    public SearchPresenter(SearchContract.View view) {
        this.view = view;
    }

    @Override
    public void request(final String keyword, final int page) {
        new AsyncTask<Void, Void, List<ItemBean>>() {
            @Override
            protected List<ItemBean> doInBackground(Void... params) {
                DataSource source = new DataSource();
                return source.get(keyword, page);
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
