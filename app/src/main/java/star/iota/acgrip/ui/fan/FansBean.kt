package star.iota.acgrip.ui.fan

class FansBean(internal val week: String, internal val fans: List<FanBean>, internal val isActive: Boolean) {
    class FanBean(internal val name: String, internal val url: String)
}
