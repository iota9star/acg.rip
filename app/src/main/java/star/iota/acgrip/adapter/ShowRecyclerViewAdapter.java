package star.iota.acgrip.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import star.iota.acgrip.R;
import star.iota.acgrip.bean.ItemBean;
import star.iota.acgrip.web.WebActivity;

public class ShowRecyclerViewAdapter extends RecyclerView.Adapter<ShowRecyclerViewAdapter.MyViewHolder> {

    private final List<ItemBean> items;

    public ShowRecyclerViewAdapter() {
        items = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ItemBean item = items.get(position);
        holder.link.setText(item.getLink());
        holder.publisher.setText(item.getPublisher());
        holder.releaseTime.setText(item.getReleaseTime());
        holder.size.setText(item.getSize());
        holder.title.setText(item.getTitle());
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getDownload().trim()));
                intent.addCategory("android.intent.category.DEFAULT");
                holder.context.startActivity(intent);
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        "\n标题：" + item.getTitle()
                                + "\n\n发布者：" + item.getPublisher()
                                + "\n\n时间：" + item.getReleaseTime()
                                + "\n\n文件大小：" + item.getSize()
                                + "\n\n连接情况：" + item.getLink()
                                + "\n\n发布者地址：" + item.getUrl()
                                + "\n\n详情地址：" + item.getDownload().replace(".torrent", "")
                                + "\n\n种子地址：" + item.getDownload()
                                + "\n"
                );
                shareIntent.setType("text/plain");
                holder.context.startActivity(Intent.createChooser(shareIntent, "分享至"));
            }
        });
        holder.publisher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.context, WebActivity.class);
                intent.putExtra("url", item.getUrl());
                holder.context.startActivity(intent);
            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.context, WebActivity.class);
                intent.putExtra("url", item.getDownload().replace(".torrent", ""));
                holder.context.startActivity(intent);
            }
        });
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
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
        private final TextView publisher;
        private final TextView releaseTime;
        private final TextView title;
        private final Button open;
        private final Button share;
        private final TextView size;
        private final TextView link;
        private final Context context;

        MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            publisher = itemView.findViewById(R.id.publisher);
            releaseTime = itemView.findViewById(R.id.releaseTime);
            title = itemView.findViewById(R.id.title);
            open = itemView.findViewById(R.id.open);
            share = itemView.findViewById(R.id.share);
            size = itemView.findViewById(R.id.size);
            link = itemView.findViewById(R.id.link);
        }
    }
}
