package star.iota.acgrip.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import star.iota.acgrip.Contracts;
import star.iota.acgrip.R;
import star.iota.acgrip.base.BaseActivity;
import star.iota.acgrip.ui.about.AboutFragment;
import star.iota.acgrip.ui.item.ItemFragment;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void init() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void setFirstFragment() {
        showFragment(ItemFragment.newInstance(Contracts.TYPE_ALL, Contracts.MENU_ALL));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            exit();
        }
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.frame_layout_container;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        removeFragmentContainerChildrenViews();
        switch (item.getItemId()) {
            case R.id.action_list:
                showFragment(ItemFragment.newInstance(Contracts.TYPE_ALL, Contracts.MENU_ALL));
                break;
            case R.id.action_anime:
                showFragment(ItemFragment.newInstance(Contracts.TYPE_ANIME, Contracts.MENU_ANIME));
                break;
            case R.id.action_jp_tv:
                showFragment(ItemFragment.newInstance(Contracts.TYPE_TV, Contracts.MENU_TV));
                break;
            case R.id.action_variety:
                showFragment(ItemFragment.newInstance(Contracts.TYPE_VARIETY, Contracts.MENU_VARIETY));
                break;
            case R.id.action_music:
                showFragment(ItemFragment.newInstance(Contracts.TYPE_MUSIC, Contracts.MENU_MUSIC));
                break;
            case R.id.action_collection:
                showFragment(ItemFragment.newInstance(Contracts.TYPE_COLLECTION, Contracts.MENU_COLLECTION));
                break;
            case R.id.action_other:
                showFragment(ItemFragment.newInstance(Contracts.TYPE_OTHERS, Contracts.MENU_OTHERS));
                break;
            case R.id.action_about:
                showFragment(new AboutFragment());
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (!intent.getAction().equals(Intent.ACTION_SEARCH)) {
            return;
        }
        String keywords = intent.getStringExtra(SearchManager.QUERY);
        removeFragmentContainerChildrenViews();
        showFragment(ItemFragment.newInstance(Contracts.TYPE_SEARCH, keywords, Contracts.MENU_SEARCH + keywords));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }
}
