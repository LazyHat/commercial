package com.lazyhat.work.tracker.ui.screen

import java.util.concurrent.TimeUnit

sealed class MainEvent {
    data class UpdateIp(val new: String) : MainEvent()
    data class UpdateDuration(val time: Long, val unit: TimeUnit) : MainEvent()
    object SaveIp : MainEvent()
    object RunSender : MainEvent()
    object StopSender : MainEvent()
}