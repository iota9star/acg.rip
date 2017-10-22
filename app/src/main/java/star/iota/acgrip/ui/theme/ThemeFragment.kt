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

package star.iota.acgrip.ui.theme

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SwitchCompat
import android.widget.ImageView
import android.widget.LinearLayout
import com.afollestad.aesthetic.Aesthetic
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import io.reactivex.disposables.Disposable
import star.iota.acgrip.R
import star.iota.acgrip.base.BaseToolbarFragment


class ThemeFragment : BaseToolbarFragment() {

    override fun getContainerViewId(): Int = R.layout.fragment_theme

    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutTheme: LinearLayout
    private lateinit var imageViewPoint: ImageView
    private lateinit var switchCompatTint: SwitchCompat
    private lateinit var switchCompatNightly: SwitchCompat
    private lateinit var adapter: ThemeAdapter
    private lateinit var isDarkSubscription: Disposable

    companion object {
        private val PARAMS_TITLE = "title"
        fun newInstance(title: String?): ThemeFragment {
            val fragment = ThemeFragment()
            val bundle = Bundle()
            bundle.putString(PARAMS_TITLE, title)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun doSome() {
        setTitle(arguments.getString(PARAMS_TITLE))
        recyclerView = containerView!!.findViewById(R.id.recycler_view)
        recyclerView.isNestedScrollingEnabled = false
        linearLayoutTheme = containerView!!.findViewById(R.id.linear_layout_diy_theme)
        imageViewPoint = containerView!!.findViewById(R.id.image_view_point)
        switchCompatTint = containerView!!.findViewById(R.id.switch_compat_tint)
        switchCompatNightly = containerView!!.findViewById(R.id.switch_compat_nightly)

        imageViewPoint.setColorFilter(ThemeHelper.getTheme(activity))

        linearLayoutTheme.setOnClickListener {
            ColorPickerDialogBuilder
                    .with(activity)
                    .initialColor(ThemeHelper.getTheme(activity))
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener { color ->
                        applyTheme(color)
                        adapter.removeSelectedStatus()
                    }
                    .build()
                    .show()
        }

        isDarkSubscription = Aesthetic.get()
                .isDark
                .subscribe { isDark ->
                    switchCompatNightly.isChecked = isDark!!
                }

        switchCompatNightly.setOnCheckedChangeListener { _, isChecked ->
            val theme = ThemeHelper.getTheme(activity)
            if (isChecked) {
                Aesthetic.get()
                        .activityTheme(R.style.AppThemeDark)
                        .isDark(true)
                        .colorAccent(theme)
                        .textColorPrimaryRes(R.color.text_color_primary_dark)
                        .textColorSecondaryRes(R.color.text_color_secondary_dark)
                        .apply()
            } else {
                Aesthetic.get()
                        .activityTheme(R.style.AppTheme)
                        .isDark(false)
                        .colorAccent(theme)
                        .textColorPrimaryRes(R.color.text_color_primary)
                        .textColorSecondaryRes(R.color.text_color_secondary)
                        .apply()
            }
        }
        switchCompatTint.isChecked = ThemeHelper.isTint(activity)
        switchCompatTint.setOnCheckedChangeListener { _, isChecked ->
            val theme = ThemeHelper.getTheme(activity)
            ThemeHelper.setTint(activity, isChecked)
            if (isChecked) {
                Aesthetic.get()
                        .colorPrimary(theme)
                        .colorAccent(theme)
                        .colorStatusBarAuto()
                        .colorNavigationBarAuto()
                        .apply()
            } else {
                Aesthetic.get()
                        .colorPrimary(ContextCompat.getColor(activity, R.color.white))
                        .colorAccent(theme)
                        .colorStatusBarAuto()
                        .colorNavigationBarAuto()
                        .apply()
            }
        }

        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        linearLayoutManager.isSmoothScrollbarEnabled = true
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
        adapter = ThemeAdapter(getThemes())
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : ThemeAdapter.OnItemClickListener {
            override fun onClick(theme: ThemeBean) {
                val color = ContextCompat.getColor(activity, theme.color)
                applyTheme(color)
            }
        })
    }

    private fun applyTheme(color: Int) {
        imageViewPoint.setColorFilter(color)
        ThemeHelper.setTheme(activity, color)
        if (ThemeHelper.isTint(activity)) {
            Aesthetic.get()
                    .colorPrimary(color)
                    .colorAccent(color)
                    .colorStatusBarAuto()
                    .colorNavigationBarAuto()
                    .apply()
        } else {
            Aesthetic.get()
                    .colorPrimary(ContextCompat.getColor(activity, R.color.white))
                    .colorAccent(color)
                    .colorStatusBarAuto()
                    .colorNavigationBarAuto()
                    .apply()
        }
    }

    private fun getThemes(): ArrayList<ThemeBean> {
        val themes = arrayListOf<ThemeBean>().apply {
            add(ThemeBean(R.color.red, "红色/Red", false))
            add(ThemeBean(R.color.pink, "粉色/Pink", false))
            add(ThemeBean(R.color.purple, "紫色/Purple", false))
            add(ThemeBean(R.color.deep_purple, "深紫/Deep Purple", false))
            add(ThemeBean(R.color.indigo, "靛蓝/Indigo", false))
            add(ThemeBean(R.color.blue, "蓝色/Blue", false))
            add(ThemeBean(R.color.light_blue, "亮蓝/Light Blue", false))
            add(ThemeBean(R.color.cyan, "青色/Cyan", false))
            add(ThemeBean(R.color.teal, "鸭绿/Teal", false))
            add(ThemeBean(R.color.green, "绿色/Green", false))
            add(ThemeBean(R.color.light_green, "亮绿/Light Green", false))
            add(ThemeBean(R.color.lime, "酸橙/Lime", false))
            add(ThemeBean(R.color.yellow, "黄色/Yellow", false))
            add(ThemeBean(R.color.amber, "琥珀/Amber", false))
            add(ThemeBean(R.color.orange, "橙色/Orange", false))
            add(ThemeBean(R.color.deep_orange, "暗橙/Deep Orange", false))
            add(ThemeBean(R.color.brown, "棕色/Brown", false))
            add(ThemeBean(R.color.grey, "灰色/Grey", false))
            add(ThemeBean(R.color.blue_grey, "蓝灰/Blue Grey", false))
            add(ThemeBean(R.color.bilibili, "哔哩哔哩/BiliBili", false))
            add(ThemeBean(R.color.black, "黑色/Black", false))
            add(ThemeBean(R.color.dark_black, "深黑/Deep Dark", false))
        }
        themes.filter { it.color == ThemeHelper.getTheme(activity) }.forEach { it.isSelected = true }
        return themes
    }

    override fun onDestroyView() {
        isDarkSubscription.dispose()
        super.onDestroyView()
    }

}