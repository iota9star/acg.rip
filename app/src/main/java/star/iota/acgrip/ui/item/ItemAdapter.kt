/*
 *    Copyright 2017. iota9star
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package star.iota.acgrip.ui.item

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import star.iota.acgrip.Category
import star.iota.acgrip.MessageBar
import star.iota.acgrip.R
import star.iota.acgrip.ext.addFragmentToActivity
import java.util.*

internal class ItemAdapter : RecyclerView.Adapter<ItemAdapter.MyViewHolder>() {

    private val items: MutableList<ItemBean>

    init {
        items = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.textViewTitle.text = item.title
        holder.textViewDate.text = item.date
        holder.textViewSize.text = item.size
        holder.buttonDownload.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.download.trim { it <= ' ' }))
                intent.addCategory("android.intent.category.DEFAULT")
                holder.context.startActivity(intent)
            } catch (e: Exception) {
            }
        }
        holder.buttonShare.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.putExtra(Intent.EXTRA_TEXT, item.toString())
                shareIntent.type = "text/plain"
                holder.context.startActivity(Intent.createChooser(shareIntent, "分享至"))
            } catch (e: Exception) {
            }
        }
        if (TextUtils.isEmpty(item.sub) || TextUtils.isEmpty(item.subLink)) {
            holder.textViewSub.isEnabled = false
            holder.textViewSub.isClickable = false
            holder.textViewSub.text = "• • • • • •"
        } else {
            holder.textViewSub.isEnabled = true
            holder.textViewSub.isClickable = true
            holder.textViewSub.text = item.sub
        }
        holder.textViewSub.setOnClickListener { (holder.context as AppCompatActivity).addFragmentToActivity(ItemFragment.newInstance(Category.URL.id, item.subLink, item.sub), R.id.frame_layout_container) }
        holder.buttonInfo.setOnClickListener { showInfo(holder, item) }
    }

    @SuppressLint("InflateParams", "SetJavaScriptEnabled")
    private fun showInfo(holder: MyViewHolder, item: ItemBean) {
        val view = LayoutInflater.from(holder.context).inflate(R.layout.dialog_web, null)
        val webView = view.findViewById<WebView>(R.id.web_view)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.loadsImagesAutomatically = true
        webSettings.setSupportZoom(true)
        val webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (newProgress == 100) {
                    progressBar.visibility = View.GONE
                } else {
                    progressBar.visibility = View.VISIBLE
                }
            }
        }
        webView.webChromeClient = webChromeClient
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    try {
                        view.loadUrl(url)
                    } catch (e: Exception) {
                        MessageBar.create(holder.context, "加载错误：" + e.message)
                    }
                } else {
                    Snackbar.make((holder.context as AppCompatActivity).findViewById(android.R.id.content), "打开外部应用", Snackbar.LENGTH_LONG).setAction("嗯", {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        intent.addCategory("android.intent.category.DEFAULT")
                        holder.context.startActivity(intent)
                    }).show()

                }
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (url.contains("acg.rip")) {
                    runJS(view)
                }
            }
        }
        try {
            webView.loadUrl(item.url)
        } catch (e: Exception) {
            MessageBar.create(holder.context, "加载错误,请重试...")
        }

        webView.setDownloadListener { url, _, _, _, _ ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addCategory("android.intent.category.DEFAULT")
            holder.context.startActivity(intent)
        }
        AlertDialog.Builder(holder.context)
                .setIcon(R.mipmap.app_icon)
                .setTitle(item.title)
                .setView(view)
                .setOnDismissListener {
                    webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
                    webView.clearHistory()
                    (webView.parent as ViewGroup).removeView(webView)
                    webView.destroy()
                }
                .show()
    }

    private fun runJS(view: WebView) {
        val js = "function(){" +
                "   var body=document.getElementsByTagName(\"body\");" +
                "   var header=document.getElementsByTagName(\"header\");" +
                "   var search=document.getElementsByClassName(\"breadcrumb\");" +
                "   var footer=document.getElementsByClassName(\"footer\");" +
                "   body[0].style.marginTop='18px';" +
                "   body[0].style.marginBottom='16px';" +
                "   header[0].remove();" +
                "   search[0].remove();" +
                "   footer[0].remove();" +
                "}"
        view.loadUrl("javascript:($js)()")
    }

    fun clear() {
        val size = items.size
        items.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun add(items: List<ItemBean>) {
        val size = this.items.size
        this.items.addAll(items)
        notifyItemRangeInserted(size, items.size)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MyViewHolder(
            itemView: View,
            val context: Context = itemView.context,
            var textViewSub: TextView = itemView.findViewById(R.id.text_view_sub),
            var textViewTitle: TextView = itemView.findViewById(R.id.text_view_title),
            var textViewSize: TextView = itemView.findViewById(R.id.text_view_size),
            var textViewDate: TextView = itemView.findViewById(R.id.text_view_date),
            var buttonInfo: TextView = itemView.findViewById(R.id.text_view_info),
            var buttonShare: TextView = itemView.findViewById(R.id.text_view_share),
            var buttonDownload: TextView = itemView.findViewById(R.id.text_view_download)
    ) : RecyclerView.ViewHolder(itemView)
}
