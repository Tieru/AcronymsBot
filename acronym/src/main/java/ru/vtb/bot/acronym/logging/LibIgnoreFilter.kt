package ru.vtb.bot.acronym.logging

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply

class LibIgnoreFilter : Filter<ILoggingEvent>() {

    override fun decide(event: ILoggingEvent): FilterReply {
        return when {
            event.loggerName.startsWith("io.grpc") -> FilterReply.DENY
            else -> FilterReply.NEUTRAL
        }
    }
}
