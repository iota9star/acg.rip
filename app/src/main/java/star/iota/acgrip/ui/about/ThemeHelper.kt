package star.iota.acgrip.ui.about

import android.content.Context
import android.content.SharedPreferences
import star.iota.acgrip.R

object ThemeHelper {

    private val THEME_ID = "theme_id"

    private fun getSharePreference(context: Context): SharedPreferences {
        return context.getSharedPreferences("multiple_theme", Context.MODE_PRIVATE)
    }

    fun setTheme(context: Context, themeId: Int) {
        getSharePreference(context).edit()
                .putInt(THEME_ID, themeId)
                .apply()
    }

    fun getTheme(context: Context): Int {
        return getSharePreference(context).getInt(THEME_ID, R.color.blue)
    }
}