package io.findify.clickhouse.format.decoder

import io.findify.clickhouse.format.Field
import io.findify.clickhouse.format.Field._

class ByteDecoder extends Decoder[Byte, ByteNumber] {
  override def decodeValue: PartialFunction[Field, Byte] = {
    case UInt8(i) => i
    case Int8(i) => i
  }
}
