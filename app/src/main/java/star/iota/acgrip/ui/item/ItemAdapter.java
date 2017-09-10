package star.iota.acgrip.ui.item;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import star.iota.acgrip.Contracts;
import star.iota.acgrip.MessageBar;
import star.iota.acgrip.R;
import star.iota.acgrip.base.BaseActivity;

class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private final List<ItemBean> items;

    ItemAdapter() {
        items = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ItemBean item = items.get(position);
        holder.textViewTitle.setText(item.getTitle());
        holder.textViewSub.setText(item.getSub());
        holder.textViewDate.setText(item.getDate());
        holder.textViewSize.setText(item.getSize());
        holder.buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getDownload().trim()));
                intent.addCategory("android.intent.category.DEFAULT");
                holder.context.startActivity(intent);
            }
        });
        holder.buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, item.toString());
                shareIntent.setType("text/plain");
                holder.context.startActivity(Intent.createChooser(shareIntent, "分享至"));
            }
        });
        if (TextUtils.isEmpty(item.getSub()) || TextUtils.isEmpty(item.getSubLink())) {
            holder.buttonSub.setEnabled(false);
            holder.buttonSub.setClickable(false);
        } else {
            holder.buttonSub.setEnabled(true);
            holder.buttonSub.setClickable(true);
        }
        holder.buttonSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) holder.context).addFragment(ItemFragment.newInstance(Contracts.TYPE_URL, item.getSubLink(), item.getSub()));
            }
        });
        holder.buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfo(holder, item);
            }
        });
    }

    private void showInfo(final MyViewHolder holder, ItemBean item) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(holder.context).inflate(R.layout.dialog_web, null);
        final WebView webView = view.findViewById(R.id.web_view);
        final ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setSupportZoom(true);
        WebChromeClient webChromeClient = new WebChromeClient() {
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
                        MessageBar.create(holder.context, "加载错误,请重试...");
                    }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains("acg.rip")) {
                    runJS(view);
                }
            }
        });
        try {
            webView.loadUrl(item.getUrl());
        } catch (Exception e) {
            MessageBar.create(holder.context, "加载错误,请重试...");
        }
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addCategory("android.intent.category.DEFAULT");
                holder.context.startActivity(intent);
            }
        });
        new AlertDialog.Builder(holder.context)
                .setIcon(R.mipmap.app_icon)
                .setTitle(item.getTitle())
                .setView(view)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
                        webView.clearHistory();
                        ((ViewGroup) webView.getParent()).removeView(webView);
                        webView.destroy();
                    }
                })
                .show();
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

    void clear() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void add(List<ItemBean> items) {
        int size = this.items.size();
        this.items.addAll(items);
        notifyItemRangeInserted(size, items.size());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        @BindView(R.id.text_view_sub)
        TextView textViewSub;
        @BindView(R.id.text_view_title)
        TextView textViewTitle;
        @BindView(R.id.text_view_size)
        TextView textViewSize;
        @BindView(R.id.text_view_date)
        TextView textViewDate;
        @BindView(R.id.button_sub)
        Button buttonSub;
        @BindView(R.id.button_info)
        Button buttonInfo;
        @BindView(R.id.button_share)
        Button buttonShare;
        @BindView(R.id.button_download)
        Button buttonDownload;

        MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }
    }
}
