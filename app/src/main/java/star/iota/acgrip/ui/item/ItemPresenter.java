package star.iota.acgrip.ui.item;

import android.os.AsyncTask;

import java.util.List;

import star.iota.acgrip.service.DataService;

class ItemPresenter implements ItemContract.Presenter {

    private final ItemContract.View view;
    private AsyncTask<Void, Void, List<ItemBean>> asyncTask;

    ItemPresenter(ItemContract.View view) {
        this.view = view;
    }

    @Override
    public void request(final int type, final int page) {
        asyncTask = new AsyncTask<Void, Void, List<ItemBean>>() {
            @Override
            protected List<ItemBean> doInBackground(Void... params) {
                DataService source = new DataService();
                return source.getItems(type, page);
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
        };
        asyncTask.execute();
    }

    @Override
    public void request(final String url, final int page) {
        asyncTask = new AsyncTask<Void, Void, List<ItemBean>>() {
            @Override
            protected List<ItemBean> doInBackground(Void... params) {
                DataService source = new DataService();
                return source.getItems(url, page);
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
        };
        asyncTask.execute();
    }

    @Override
    public void search(final String keywords, final int page) {
        asyncTask = new AsyncTask<Void, Void, List<ItemBean>>() {
            @Override
            protected List<ItemBean> doInBackground(Void... params) {
                DataService source = new DataService();
                return source.search(keywords, page);
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
        };
        asyncTask.execute();
    }

    @Override
    public void cancel() {
        if (asyncTask != null && !asyncTask.isCancelled()) {
            asyncTask.cancel(true);
        }
    }
}
