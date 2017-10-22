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

package star.iota.acgrip

enum class Category(val id: Int?, val menu: String?, val url: String?) {
    ALL(0, "全部", null),
    ANIME(1, "动画", null),
    TV(2, "日剧", null),
    VARIETY(3, "综艺", null),
    MUSIC(4, "音乐", null),
    COLLECTION(5, "合集", null),
    OTHERS(9, "其他", null),
    WEEK(null, "每周", "https://acg.rip/"),
    SEARCH(99, "搜索：", null),
    URL(98, null, null),
    THEME(null, "主题", null),
    ABOUT(null, "关于", null)
}
