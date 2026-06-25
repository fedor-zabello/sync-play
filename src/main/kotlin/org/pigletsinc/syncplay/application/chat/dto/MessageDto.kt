package org.pigletsinc.syncplay.application.chat.dto

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.pigletsinc.syncplay.domain.chat.entity.MessageType
import java.time.LocalDateTime
import java.util.UUID

open class MessageDto(
    val messageId: UUID? = UUID.randomUUID(),
    val sender: String?,
    val content: String?,
    @field:JsonSerialize(using = LocalDateTimeSerializer::class)
    @field:JsonDeserialize(using = LocalDateTimeDeserializer::class)
    val messageDate: LocalDateTime? = LocalDateTime.now(),
    var type: MessageType?,
)
