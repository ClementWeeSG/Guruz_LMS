package io.udash.demos.rest.model

import com.avsystem.commons.serialization.{HasGenCodec, transparent}

@transparent
case class PhoneBookId(value: Int)
object PhoneBookId extends HasGenCodec[PhoneBookId]

case class PhoneBookInfo(id: PhoneBookId, name: String, description: String)
object PhoneBookInfo extends HasGenCodec[PhoneBookInfo]