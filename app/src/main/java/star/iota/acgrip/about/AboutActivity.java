package star.iota.acgrip.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import moe.feng.alipay.zerosdk.AlipayZeroSdk;
import star.iota.acgrip.R;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });
        ImageButton open = (ImageButton) findViewById(R.id.image_button_open);
        ImageButton thumb = (ImageButton) findViewById(R.id.image_button_thumb_up);
        ImageButton alipay = (ImageButton) findViewById(R.id.image_button_donation);
        open.setOnClickListener(this);
        thumb.setOnClickListener(this);
        alipay.setOnClickListener(this);
        initVersion();
    }

    private void initVersion() {
        TextView version = (TextView) findViewById(R.id.text_view_version);
        try {
            String packageName = getPackageName();
            String versionName = getPackageManager().getPackageInfo(
                    packageName, 0).versionName;
            version.setText(versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        switch (v.getId()) {
            case R.id.image_button_thumb_up:
                intent.setData(Uri.parse("market://details?id=star.iota.acgrip"));
                break;
            case R.id.image_button_open:
                intent.setData(Uri.parse("https://acg.rip/"));
                break;
            case R.id.image_button_donation:
                AlipayZeroSdk.startAlipayClient(this, getResources().getString(R.string.alipay_code));
                return;
        }
        startActivity(intent);
    }
}
