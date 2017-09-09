package star.iota.acgrip.ui.about;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import star.iota.acgrip.R;
import star.iota.acgrip.base.BaseFragment;


public class AboutFragment extends BaseFragment {
    @BindView(R.id.text_view_version)
    TextView textViewVersion;

    @OnClick({R.id.text_view_acg_rip, R.id.text_view_grade_app})
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        switch (view.getId()) {
            case R.id.text_view_acg_rip:
                intent.setData(Uri.parse(getString(R.string.acg_rip)));
                break;
            case R.id.text_view_grade_app:
                intent.setData(Uri.parse("market://details?id=" + mContext.getPackageName()));
                break;
        }
        if (intent.getData() != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void init() {
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            textViewVersion.setText(packageInfo.versionName + " ( " + packageInfo.versionCode + " )");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }
}
