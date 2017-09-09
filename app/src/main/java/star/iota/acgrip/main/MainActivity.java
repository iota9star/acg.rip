package star.iota.acgrip.main;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import star.iota.acgrip.R;
import star.iota.acgrip.SnackbarUtils;
import star.iota.acgrip.about.AboutActivity;
import star.iota.acgrip.broadcast.NetStatusBroadcastReceiver;
import star.iota.acgrip.data.DataType;
import star.iota.acgrip.fragment.ItemFragment;
import star.iota.acgrip.search.SearchActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private NetStatusBroadcastReceiver mNetStatusBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNetBroadcastReceiver();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        showFragment(ItemFragment.newInstance(DataType.TYPE_ALL));
    }

    private void initNetBroadcastReceiver() {
        mNetStatusBroadcastReceiver = new NetStatusBroadcastReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetStatusBroadcastReceiver, mFilter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            SnackbarUtils.create(this, "真的要退出了吗？", "嗯", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.exit(0);
                }
            });
        }
    }

    private void showFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction tx = fm.beginTransaction();
            tx.replace(R.id.frame_layout_container, fragment);
            tx.commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_list:
                showFragment(ItemFragment.newInstance(DataType.TYPE_ALL));
                setTitle("ACG.RIP");
                break;
            case R.id.action_anime:
                showFragment(ItemFragment.newInstance(DataType.TYPE_ANIME));
                setTitle("动画");
                break;
            case R.id.action_jp_tv:
                showFragment(ItemFragment.newInstance(DataType.TYPE_TV));
                setTitle("日剧");
                break;
            case R.id.action_variety:
                showFragment(ItemFragment.newInstance(DataType.TYPE_VARIETY));
                setTitle("综艺");
                break;
            case R.id.action_music:
                showFragment(ItemFragment.newInstance(DataType.TYPE_MUSIC));
                setTitle("音乐");
                break;
            case R.id.action_collection:
                showFragment(ItemFragment.newInstance(DataType.TYPE_COLLECTION));
                setTitle("合集");
                break;
            case R.id.action_other:
                showFragment(ItemFragment.newInstance(DataType.TYPE_OTHERS));
                setTitle("其它");
                break;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNetStatusBroadcastReceiver != null) {
            unregisterReceiver(mNetStatusBroadcastReceiver);
        }
    }
}
