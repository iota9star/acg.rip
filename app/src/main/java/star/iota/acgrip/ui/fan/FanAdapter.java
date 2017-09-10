package star.iota.acgrip.ui.fan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import star.iota.acgrip.Contracts;
import star.iota.acgrip.R;
import star.iota.acgrip.base.BaseActivity;
import star.iota.acgrip.ui.item.ItemFragment;

class FanAdapter extends RecyclerView.Adapter<FanAdapter.MyViewHolder> {

    private final List<FansBean> items;

    FanAdapter() {
        items = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fans, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FansBean item = items.get(position);
        holder.textViewWeek.setText(item.getWeek());
        holder.flowLayoutFans.removeAllViews();
        if (item.isActive()) {
            holder.flowLayoutFans.setBackground(ContextCompat.getDrawable(holder.context, R.drawable.bg_fans_active));
        } else {
            holder.flowLayoutFans.setBackground(ContextCompat.getDrawable(holder.context, R.drawable.bg_fans_default));
        }
        LayoutInflater inflater = LayoutInflater.from(holder.context);
        for (final FansBean.FanBean bean : item.getFans()) {
            @SuppressLint("InflateParams") TextView fan = (TextView) inflater.inflate(R.layout.item_fan, null);
            fan.setText(bean.getName());
            fan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((BaseActivity) holder.context).addFragment(ItemFragment.newInstance(Contracts.TYPE_URL, bean.getUrl(), bean.getName()));
                }
            });
            holder.flowLayoutFans.addView(fan);
        }
    }

    void clear() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void add(List<FansBean> items) {
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
        @BindView(R.id.text_view_week)
        TextView textViewWeek;
        @BindView(R.id.flow_layout_fans)
        FlowLayout flowLayoutFans;

        MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }
    }
}
