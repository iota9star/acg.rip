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
import exit
import removeFragmentsView
import replaceFragmentInActivity
import star.iota.acgrip.R
import star.iota.acgrip.ui.about.AboutFragment
import star.iota.acgrip.ui.fan.FanFragment
import star.iota.acgrip.ui.item.ItemFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var toolbar: Toolbar? = null
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var searchView: SearchView
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout


    fun floatToolbar(isScroll: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isScroll) {
                appBarLayout.elevation = resources.getDimension(R.dimen.v2dp)
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
        replaceFragmentInActivity(ItemFragment.newInstance(star.iota.acgrip.Menu.ALL.id, star.iota.acgrip.Menu.ALL.menu), R.id.frame_layout_container)
        if (Aesthetic.isFirstTime()) {
            Aesthetic.get()
                    .isDark(true)
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
            R.id.action_week -> replaceFragmentInActivity(FanFragment.newInstance(star.iota.acgrip.Menu.WEEK.url, star.iota.acgrip.Menu.WEEK.menu), R.id.frame_layout_container)
            R.id.action_list -> replaceFragmentInActivity(ItemFragment.newInstance(star.iota.acgrip.Menu.ALL.id, star.iota.acgrip.Menu.ALL.menu), R.id.frame_layout_container)
            R.id.action_anime -> replaceFragmentInActivity(ItemFragment.newInstance(star.iota.acgrip.Menu.ANIME.id, star.iota.acgrip.Menu.ANIME.menu), R.id.frame_layout_container)
            R.id.action_jp_tv -> replaceFragmentInActivity(ItemFragment.newInstance(star.iota.acgrip.Menu.TV.id, star.iota.acgrip.Menu.TV.menu), R.id.frame_layout_container)
            R.id.action_variety -> replaceFragmentInActivity(ItemFragment.newInstance(star.iota.acgrip.Menu.VARIETY.id, star.iota.acgrip.Menu.VARIETY.menu), R.id.frame_layout_container)
            R.id.action_music -> replaceFragmentInActivity(ItemFragment.newInstance(star.iota.acgrip.Menu.MUSIC.id, star.iota.acgrip.Menu.MUSIC.menu), R.id.frame_layout_container)
            R.id.action_collection -> replaceFragmentInActivity(ItemFragment.newInstance(star.iota.acgrip.Menu.COLLECTION.id, star.iota.acgrip.Menu.COLLECTION.menu), R.id.frame_layout_container)
            R.id.action_other -> replaceFragmentInActivity(ItemFragment.newInstance(star.iota.acgrip.Menu.OTHERS.id, star.iota.acgrip.Menu.OTHERS.menu), R.id.frame_layout_container)
            R.id.action_about -> replaceFragmentInActivity(AboutFragment.newInstance(star.iota.acgrip.Menu.ABOUT.menu), R.id.frame_layout_container)
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onNewIntent(intent: Intent) {
        if (intent.action != Intent.ACTION_SEARCH) {
            return
        }
        val keywords = intent.getStringExtra(SearchManager.QUERY)
        replaceFragmentInActivity(ItemFragment.newInstance(star.iota.acgrip.Menu.SEARCH.id, keywords, star.iota.acgrip.Menu.SEARCH.menu + keywords), R.id.frame_layout_container)
    }
}
