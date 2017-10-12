package star.iota.jptv.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import star.iota.acgrip.ui.MainActivity

abstract class BaseToolbarFragment : Fragment() {

    protected var containerView: View? = null
    private var preTitle: CharSequence? = null

    protected abstract fun getContainerViewId(): Int

    protected abstract fun init()

    protected fun setTitle(title: CharSequence?) {
        (activity as MainActivity).toolbar?.title = title
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preTitle = (activity as MainActivity).toolbar?.title
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (containerView == null) {
            containerView = inflater!!.inflate(getContainerViewId(), container, false)
        }
        init()
        return containerView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setTitle(preTitle)
    }

}