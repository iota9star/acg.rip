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

package star.iota.acgrip.ui.item

class ItemBean(val title: String, internal val sub: String, internal val subLink: String, internal val url: String, internal val download: String, internal val date: String, internal val size: String, private val linkCount: String) {

    override fun toString(): String {
        return ("\n" + title
                + "\n\n发布者：" + sub
                + "\n\n时间：" + date
                + "\n\n文件大小：" + size
                + "\n\n连接情况：" + linkCount
                + "\n\n发布者地址：" + subLink
                + "\n\n详情地址：" + download.replace(".torrent", "")
                + "\n\n种子地址：" + download
                + "\n")
    }

}
