package star.iota.acgrip.ui.fan;

import android.os.AsyncTask;

import java.util.List;

import star.iota.acgrip.service.DataService;

class FanPresenter implements FanContract.Presenter {

    private final FanContract.View view;
    private AsyncTask<Void, Void, List<FansBean>> asyncTask;

    FanPresenter(FanContract.View view) {
        this.view = view;
    }


    @Override
    public void request(final String url) {
        asyncTask = new AsyncTask<Void, Void, List<FansBean>>() {
            @Override
            protected List<FansBean> doInBackground(Void... params) {
                DataService source = new DataService();
                return source.getFans(url);
            }

            @Override
            protected void onPostExecute(List<FansBean> items) {
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
