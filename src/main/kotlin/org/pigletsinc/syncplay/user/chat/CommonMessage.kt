package org.pigletsinc.syncplay.user.chat

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import java.time.LocalDateTime
import java.util.UUID

open class CommonMessage(
    val messageId: UUID? = UUID.randomUUID(),
    val sender: String?,
    val content: String?,
    @field:JsonSerialize(using = LocalDateTimeSerializer::class)
    @field:JsonDeserialize(using = LocalDateTimeDeserializer::class)
    val messageDate: LocalDateTime? = LocalDateTime.now(),
    var type: MessageType?,
)
