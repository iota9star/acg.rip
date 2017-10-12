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
