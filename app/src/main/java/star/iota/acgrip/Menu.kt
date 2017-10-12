package star.iota.acgrip

enum class Menu(val id: Int?, val menu: String?, val url: String?) {
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
    ABOUT(null, "关于", null)
}
