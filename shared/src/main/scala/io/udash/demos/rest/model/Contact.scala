package io.udash.demos.rest.model

import com.avsystem.commons.serialization.{HasGenCodec, transparent}

@transparent
case class ContactId(value: Int)
object ContactId extends HasGenCodec[ContactId]

case class Contact(id: ContactId, firstName: String, lastName: String, phone: String, email: String)
object Contact extends HasGenCodec[Contact]
