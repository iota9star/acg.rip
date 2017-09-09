package star.iota.acgrip.web;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import star.iota.acgrip.R;
import star.iota.acgrip.SnackbarUtils;


public class WebActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        String baseUrl = getIntent().getStringExtra("url");
        if (baseUrl == null || baseUrl.length() < 5) {
            SnackbarUtils.create(this, "数据接收有误，请返回重试？");
            return;
        }
        initWebView(baseUrl);
    }

    private void initWebView(String baseUrl) {
        webView = (WebView) findViewById(R.id.web_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setSupportZoom(true);
        WebChromeClient webChromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                setTitle(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        };
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://") || url.startsWith("https://"))
                    try {
                        view.loadUrl(url);
                    } catch (Exception e) {
                        SnackbarUtils.create(WebActivity.this, "网页加载错误？");
                    }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                if (url.contains("acg.rip")) {
//                    runJS(view);
//                }
                runJS(view);
            }
        });
        try {
            webView.loadUrl(baseUrl);
        } catch (Exception e) {
            SnackbarUtils.create(WebActivity.this, "网页加载错误？");
        }
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addCategory("android.intent.category.DEFAULT");
                startActivity(intent);
            }
        });
    }

    private void runJS(WebView view) {
        String js =
                "function(){" +
                        "   var body=document.getElementsByTagName(\"body\");" +
                        "   var header=document.getElementsByTagName(\"header\");" +
                        "   var search=document.getElementsByClassName(\"breadcrumb\");" +
                        "   var footer=document.getElementsByClassName(\"footer\");" +
                        "   body[0].style.marginTop='18px';" +
                        "   body[0].style.marginBottom='16px';" +
                        "   header[0].remove();" +
                        "   search[0].remove();" +
                        "   footer[0].remove();" +
                        "}";
        view.loadUrl("javascript:(" + js + ")()");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_copy:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("url", webView.getUrl()));
                SnackbarUtils.create(WebActivity.this, "当前链接已复制到剪切板？");
                return true;
            case R.id.action_browser:
                Intent browser = new Intent(Intent.ACTION_VIEW);
                browser.setData(Uri.parse(webView.getUrl()));
                startActivity(browser);
                return true;
            case R.id.action_share:
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_TEXT, webView.getTitle() + " : " + webView.getUrl());
                share.setType("text/plain");
                startActivity(Intent.createChooser(share, "分享到"));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
