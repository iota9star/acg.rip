package star.iota.acgrip.ui.about

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SwitchCompat
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.afollestad.aesthetic.Aesthetic
import io.reactivex.disposables.Disposable
import star.iota.acgrip.R
import star.iota.jptv.base.BaseToolbarFragment


class AboutFragment : BaseToolbarFragment(), View.OnClickListener {
    override fun getContainerViewId(): Int {
        return R.layout.fragment_about
    }

    private lateinit var textViewVersion: TextView
    private lateinit var textViewAcgrip: TextView
    private lateinit var textViewGradeApp: TextView
    private lateinit var linearLayoutTheme: LinearLayout
    private lateinit var imageViewPoint: ImageView
    private lateinit var switchCompatDark: SwitchCompat
    private lateinit var switchCompatTint: SwitchCompat
    private lateinit var isDarkSubscription: Disposable

    companion object {
        fun newInstance(title: String?): AboutFragment {
            val fragment = AboutFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.linear_layout_theme -> showThemeChooser()
            R.id.text_view_acg_rip -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.acg_rip))))
            R.id.text_view_grade_app -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.packageName)))
        }
    }

    override fun init() {
        setTitle(arguments.getString("title", activity.getString(R.string.app_name)))
        textViewVersion = containerView!!.findViewById(R.id.text_view_version)
        textViewAcgrip = containerView!!.findViewById(R.id.text_view_acg_rip)
        textViewGradeApp = containerView!!.findViewById(R.id.text_view_grade_app)
        linearLayoutTheme = containerView!!.findViewById(R.id.linear_layout_theme)
        imageViewPoint = containerView!!.findViewById(R.id.image_view_point)
        switchCompatDark = containerView!!.findViewById(R.id.switch_compat_dark)
        switchCompatTint = containerView!!.findViewById(R.id.switch_compat_tint)
        isDarkSubscription = Aesthetic.get()
                .isDark
                .subscribe { isDark -> switchCompatDark.isChecked = isDark!! }
        switchCompatTint.isChecked = ThemeHelper.isTint(activity)
        switchCompatDark.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                Aesthetic.get()
                        .activityTheme(R.style.AppThemeDark)
                        .isDark(true)
                        .textColorPrimaryRes(R.color.text_color_primary_dark)
                        .textColorSecondaryRes(R.color.text_color_secondary_dark)
                        .apply()
            } else {
                Aesthetic.get()
                        .activityTheme(R.style.AppTheme)
                        .isDark(false)
                        .textColorPrimaryRes(R.color.text_color_primary)
                        .textColorSecondaryRes(R.color.text_color_secondary)
                        .apply()
            }
        }
        switchCompatTint.setOnCheckedChangeListener { _, checked ->
            val theme = ThemeHelper.getTheme(activity)
            ThemeHelper.setTint(activity, checked)
            if (checked) {
                Aesthetic.get()
                        .colorPrimaryRes(theme)
                        .colorAccentRes(theme)
                        .colorStatusBarAuto()
                        .colorNavigationBarAuto()
                        .apply()
            } else {
                Aesthetic.get()
                        .colorPrimaryRes(R.color.white)
                        .colorAccentRes(theme)
                        .colorStatusBarAuto()
                        .colorNavigationBarAuto()
                        .apply()
            }
        }
        linearLayoutTheme.setOnClickListener(this)
        textViewAcgrip.setOnClickListener(this)
        textViewGradeApp.setOnClickListener(this)
        imageViewPoint.setColorFilter(ContextCompat.getColor(activity, ThemeHelper.getTheme(activity)))
        try {
            val packageInfo = activity.packageManager.getPackageInfo(activity.packageName, PackageManager.GET_CONFIGURATIONS)
            textViewVersion.text = (packageInfo.versionName + " ( " + packageInfo.versionCode + " )")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }

    override fun onDestroyView() {
        isDarkSubscription.dispose()
        super.onDestroyView()
    }

    @SuppressLint("InflateParams")
    private fun showThemeChooser() {
        val view = layoutInflater.inflate(R.layout.dialog_theme, null)
        themeChooser(view)
        AlertDialog.Builder(activity)
                .setView(view)
                .setTitle("主题管理")
                .show()
    }


    private fun themeChooser(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val headerView = view.findViewById<View>(R.id.view_header)
        headerView.setBackgroundColor(ContextCompat.getColor(activity, ThemeHelper.getTheme(activity)))
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        linearLayoutManager.isSmoothScrollbarEnabled = true
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
        val adapter = ThemeAdapter(getThemes())
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : ThemeAdapter.OnItemClickListener {
            override fun onClick(theme: ThemeBean) {
                ThemeHelper.setTheme(activity, theme.color)
                imageViewPoint.setColorFilter(ContextCompat.getColor(activity, theme.color))
                headerView.setBackgroundColor(ContextCompat.getColor(activity, theme.color))
                if (ThemeHelper.isTint(activity)) {
                    Aesthetic.get()
                            .colorPrimaryRes(theme.color)
                            .colorAccentRes(theme.color)
                            .colorStatusBarAuto()
                            .colorNavigationBarAuto()
                            .apply()
                } else {
                    Aesthetic.get()
                            .colorPrimaryRes(R.color.white)
                            .colorAccentRes(theme.color)
                            .colorStatusBarAuto()
                            .colorNavigationBarAuto()
                            .apply()
                }
//                when (theme.color) {
//                    R.color.red,
//                    R.color.pink,
//                    R.color.purple,
//                    R.color.deep_purple,
//                    R.color.indigo,
//                    R.color.blue,
//                    R.color.light_blue,
//                    R.color.cyan,
//                    R.color.teal,
//                    R.color.green,
//                    R.color.light_green,
//                    R.color.lime,
//                    R.color.yellow,
//                    R.color.amber,
//                    R.color.orange,
//                    R.color.deep_orange,
//                    R.color.brown,
//                    R.color.bilibili,
//                    R.color.grey,
//                    R.color.blue_grey,
//                    R.color.black,
//                    R.color.dark_black
//                }
            }
        })
    }

    private fun getThemes(): ArrayList<ThemeBean> {
        val themes = arrayListOf<ThemeBean>().apply {
            add(ThemeBean(R.color.red, "红色/red", false))
            add(ThemeBean(R.color.pink, "粉色/pink", false))
            add(ThemeBean(R.color.purple, "紫色/purple", false))
            add(ThemeBean(R.color.deep_purple, "深紫/Deep Purple", false))
            add(ThemeBean(R.color.indigo, "靛蓝/indigo", false))
            add(ThemeBean(R.color.blue, "蓝色/blue", false))
            add(ThemeBean(R.color.light_blue, "亮蓝/Light Blue", false))
            add(ThemeBean(R.color.cyan, "青色/cyan", false))
            add(ThemeBean(R.color.teal, "鸭绿/teal", false))
            add(ThemeBean(R.color.green, "绿色/green", false))
            add(ThemeBean(R.color.light_green, "亮绿/Light Green", false))
            add(ThemeBean(R.color.lime, "酸橙/lime", false))
            add(ThemeBean(R.color.yellow, "黄色/yellow", false))
            add(ThemeBean(R.color.amber, "琥珀/amber", false))
            add(ThemeBean(R.color.orange, "橙色/orange", false))
            add(ThemeBean(R.color.deep_orange, "暗橙/Deep Orange", false))
            add(ThemeBean(R.color.brown, "棕色/brown", false))
            add(ThemeBean(R.color.grey, "灰色/grey", false))
            add(ThemeBean(R.color.blue_grey, "蓝灰/Blue Grey", false))
            add(ThemeBean(R.color.bilibili, "哔哩哔哩/BiliBili", false))
            add(ThemeBean(R.color.black, "黑色/Black", false))
            add(ThemeBean(R.color.dark_black, "深黑/Deep Dark", false))
        }
        themes.filter { it.color == ThemeHelper.getTheme(activity) }.forEach { it.isSelected = true }
        return themes
    }
}
