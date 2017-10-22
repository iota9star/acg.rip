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

package star.iota.acgrip.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.afollestad.aesthetic.Aesthetic
import star.iota.acgrip.Category
import star.iota.acgrip.R
import star.iota.acgrip.ext.exit
import star.iota.acgrip.ext.removeFragmentsView
import star.iota.acgrip.ext.replaceFragmentInActivity
import star.iota.acgrip.ui.about.AboutFragment
import star.iota.acgrip.ui.fan.FanFragment
import star.iota.acgrip.ui.item.ItemFragment
import star.iota.acgrip.ui.theme.ThemeFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var toolbar: Toolbar? = null
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var searchView: SearchView
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout


    fun floatToolbar(isScroll: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isScroll) {
                appBarLayout.elevation = resources.getDimension(R.dimen.v4dp)
            } else {
                appBarLayout.elevation = 0f
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Aesthetic.resume(this)
    }

    override fun onPause() {
        Aesthetic.pause(this)
        super.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Aesthetic.attach(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        appBarLayout = findViewById(R.id.app_bar_layout)
        toolbar?.inflateMenu(R.menu.menu_main)
        val searchItem = toolbar?.menu?.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView.queryHint = getString(R.string.search_view_hit)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        navView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.setDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        replaceFragmentInActivity(ItemFragment.newInstance(Category.ALL.id, Category.ALL.menu), R.id.frame_layout_container)
        if (Aesthetic.isFirstTime()) {
            Aesthetic.get()
                    .activityTheme(R.style.AppTheme)
                    .textColorPrimaryRes(R.color.text_color_primary)
                    .textColorSecondaryRes(R.color.text_color_secondary)
                    .colorPrimaryRes(R.color.white)
                    .colorAccentRes(R.color.blue)
                    .colorStatusBarAuto()
                    .colorNavigationBarAuto()
                    .apply()
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START)
        else exit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        removeFragmentsView(R.id.frame_layout_container)
        when (item.itemId) {
            R.id.action_week -> replaceFragmentInActivity(FanFragment.newInstance(Category.WEEK.url, Category.WEEK.menu), R.id.frame_layout_container)
            R.id.action_list -> replaceFragmentInActivity(ItemFragment.newInstance(Category.ALL.id, Category.ALL.menu), R.id.frame_layout_container)
            R.id.action_anime -> replaceFragmentInActivity(ItemFragment.newInstance(Category.ANIME.id, Category.ANIME.menu), R.id.frame_layout_container)
            R.id.action_jp_tv -> replaceFragmentInActivity(ItemFragment.newInstance(Category.TV.id, Category.TV.menu), R.id.frame_layout_container)
            R.id.action_variety -> replaceFragmentInActivity(ItemFragment.newInstance(Category.VARIETY.id, Category.VARIETY.menu), R.id.frame_layout_container)
            R.id.action_music -> replaceFragmentInActivity(ItemFragment.newInstance(Category.MUSIC.id, Category.MUSIC.menu), R.id.frame_layout_container)
            R.id.action_collection -> replaceFragmentInActivity(ItemFragment.newInstance(Category.COLLECTION.id, Category.COLLECTION.menu), R.id.frame_layout_container)
            R.id.action_other -> replaceFragmentInActivity(ItemFragment.newInstance(Category.OTHERS.id, Category.OTHERS.menu), R.id.frame_layout_container)
            R.id.action_theme -> replaceFragmentInActivity(ThemeFragment.newInstance(Category.THEME.menu), R.id.frame_layout_container)
            R.id.action_about -> replaceFragmentInActivity(AboutFragment.newInstance(Category.ABOUT.menu), R.id.frame_layout_container)
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onNewIntent(intent: Intent) {
        if (intent.action != Intent.ACTION_SEARCH) {
            return
        }
        val keywords = intent.getStringExtra(SearchManager.QUERY)
        replaceFragmentInActivity(ItemFragment.newInstance(Category.SEARCH.id, keywords, Category.SEARCH.menu + keywords), R.id.frame_layout_container)
    }
}
