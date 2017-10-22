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

import android.content.Context
import android.content.SharedPreferences
import android.support.v4.content.ContextCompat
import star.iota.acgrip.R

object ThemeHelper {

    private val THEME_COLOR = "theme_color"
    private val THEME_TINT = "is_tint"

    private fun getSharePreference(context: Context): SharedPreferences {
        return context.getSharedPreferences("multiple_theme", Context.MODE_PRIVATE)
    }

    fun setTheme(context: Context, color: Int) {
        getSharePreference(context).edit()
                .putInt(THEME_COLOR, color)
                .apply()
    }

    fun getTheme(context: Context): Int {
        return getSharePreference(context).getInt(THEME_COLOR, ContextCompat.getColor(context, R.color.blue))
    }

    fun setTint(context: Context, isTint: Boolean) {
        getSharePreference(context).edit()
                .putBoolean(THEME_TINT, isTint)
                .apply()
    }

    fun isTint(context: Context): Boolean {
        return getSharePreference(context).getBoolean(THEME_TINT, false)
    }
}