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

package star.iota.acgrip.ui.fan

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nex3z.flowlayout.FlowLayout
import star.iota.acgrip.Category
import star.iota.acgrip.R
import star.iota.acgrip.ext.addFragmentToActivity
import star.iota.acgrip.ui.item.ItemFragment
import java.util.*

internal class FanAdapter : RecyclerView.Adapter<FanAdapter.MyViewHolder>() {

    private val items: MutableList<FansBean>

    init {
        items = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_fans, parent, false))
    }

    @SuppressLint("InflateParams")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.flowLayoutFans.removeAllViews()
        if (item.isActive) {
            holder.textViewWeek.text = ("『 " + item.week + " 』")
        } else {
            holder.textViewWeek.text = item.week
        }
        val inflater = LayoutInflater.from(holder.context)
        for (bean in item.fans) {
            @SuppressLint("InflateParams") val fan = inflater.inflate(R.layout.item_fan, null) as TextView
            fan.text = bean.name
            fan.setOnClickListener { (holder.context as AppCompatActivity).addFragmentToActivity(ItemFragment.newInstance(Category.URL.id, bean.url, bean.name), R.id.frame_layout_container) }
            holder.flowLayoutFans.addView(fan)
        }
    }

    fun clear() {
        val size = items.size
        items.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun add(items: List<FansBean>) {
        val size = this.items.size
        this.items.addAll(items)
        notifyItemRangeInserted(size, items.size)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class MyViewHolder(
            itemView: View,
            val context: Context = itemView.context,
            var textViewWeek: TextView = itemView.findViewById(R.id.text_view_week),
            var flowLayoutFans: FlowLayout = itemView.findViewById(R.id.flow_layout_fans)
    ) : RecyclerView.ViewHolder(itemView)
}
