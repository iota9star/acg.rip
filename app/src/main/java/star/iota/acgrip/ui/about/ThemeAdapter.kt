package star.iota.acgrip.ui.about

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import star.iota.acgrip.R

class ThemeAdapter(private val themes: ArrayList<ThemeBean>) : RecyclerView.Adapter<ThemeAdapter.ViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_theme, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val theme = themes[position]
        if (theme.isSelected) {
            holder.point.setImageResource(R.drawable.ic_item_theme_checked)
        } else {
            holder.point.setImageResource(R.drawable.ic_item_theme_uncheck)
        }
        holder.point.setColorFilter(ContextCompat.getColor(holder.context, theme.color))
        holder.desc.text = theme.description
        holder.container.setOnClickListener {
            for (bean in themes) {
                bean.isSelected = false
            }
            theme.isSelected = true
            onItemClickListener?.onClick(theme)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return themes.size
    }

    fun add(themes: List<ThemeBean>) {
        val size = this.themes.size
        this.themes.addAll(themes)
        notifyItemRangeInserted(size, themes.size)
    }

    interface OnItemClickListener {
        fun onClick(theme: ThemeBean)
    }

    inner class ViewHolder(
            itemView: View,
            val context: Context = itemView.context,
            val point: ImageView = itemView.findViewById(R.id.iv_point),
            val desc: TextView = itemView.findViewById(R.id.tv_desc),
            val container: LinearLayout = itemView.findViewById(R.id.ll_container)
    ) : RecyclerView.ViewHolder(itemView)
}
