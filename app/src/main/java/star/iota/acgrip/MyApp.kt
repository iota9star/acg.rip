package star.iota.acgrip

import android.app.Application
import com.scwang.smartrefresh.header.DropboxHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter


class MyApp : Application() {
    companion object {
        init {
            SmartRefreshLayout.setDefaultRefreshHeaderCreater { context, _ -> DropboxHeader(context) }
            SmartRefreshLayout.setDefaultRefreshFooterCreater { context, _ -> ClassicsFooter(context) }
        }
    }
}
