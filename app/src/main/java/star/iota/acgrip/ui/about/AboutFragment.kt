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

package star.iota.acgrip.ui.about

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import star.iota.acgrip.R
import star.iota.acgrip.base.BaseToolbarFragment


class AboutFragment : BaseToolbarFragment(), View.OnClickListener {
    override fun getContainerViewId(): Int {
        return R.layout.fragment_about
    }

    private lateinit var textViewVersion: TextView
    private lateinit var textViewAcgrip: TextView
    private lateinit var textViewGradeApp: TextView

    companion object {
        private val PARAMS_TITLE = "title"
        fun newInstance(title: String?): AboutFragment {
            val fragment = AboutFragment()
            val bundle = Bundle()
            bundle.putString(PARAMS_TITLE, title)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.text_view_acg_rip -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.acg_rip))))
            R.id.text_view_grade_app -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.packageName)))
        }
    }

    override fun doSome() {
        setTitle(arguments.getString(PARAMS_TITLE, activity.getString(R.string.app_name)))
        textViewVersion = containerView!!.findViewById(R.id.text_view_version)
        textViewAcgrip = containerView!!.findViewById(R.id.text_view_acg_rip)
        textViewGradeApp = containerView!!.findViewById(R.id.text_view_grade_app)
        textViewAcgrip.setOnClickListener(this)
        textViewGradeApp.setOnClickListener(this)
        try {
            val packageInfo = activity.packageManager.getPackageInfo(activity.packageName, PackageManager.GET_CONFIGURATIONS)
            textViewVersion.text = (packageInfo.versionName + " ( " + packageInfo.versionCode + " )")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }
}
