import android.os.SystemClock
import android.support.annotation.IdRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup

fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, @IdRes fragmentContainer: Int) {
    supportFragmentManager.beginTransaction()
            .replace(fragmentContainer, fragment)
            .commit()
}

fun AppCompatActivity.addFragmentToActivity(fragment: Fragment, @IdRes fragmentContainer: Int) {
    supportFragmentManager.beginTransaction()
            .add(fragmentContainer, fragment, fragment::class.java.simpleName)
            .addToBackStack(fragment::class.java.simpleName)
            .commitAllowingStateLoss()
}

fun AppCompatActivity.removeFragmentsView(@IdRes fragmentContainer: Int) {
    (findViewById<View>(fragmentContainer) as ViewGroup).removeAllViews()
}

private val mHints = LongArray(2)

fun AppCompatActivity.exit() {
    if (supportFragmentManager.backStackEntryCount > 0) {
        supportFragmentManager.popBackStack()
    } else {
        System.arraycopy(mHints, 1, mHints, 0, mHints.size - 1)
        mHints[mHints.size - 1] = SystemClock.uptimeMillis()
        Snackbar.make(findViewById(android.R.id.content), "真的要退出了吗？\n再摁一次返回键？", Snackbar.LENGTH_SHORT).setAction("嗯") { System.exit(0) }.show()
        if (SystemClock.uptimeMillis() - mHints[0] <= 1600) {
            System.exit(0)
        }
    }
}