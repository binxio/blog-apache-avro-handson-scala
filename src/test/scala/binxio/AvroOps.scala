// Copyright 2018 Dennis Vriend, binx.io
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package binxio

import java.io.{ ByteArrayOutputStream, OutputStream }

import com.sksamuel.avro4s._
import org.apache.avro.Schema
import org.apache.avro.file.SeekableByteArrayInput

import scala.util.Try

trait AvroOps {
  implicit def toAvroSerializeOpsImpl[A <: Product: SchemaFor: Encoder](a: A): AvroSerializeOpsImpl[A] = new AvroSerializeOpsImpl(a)

  implicit def toAvroDeserializeOpsImpl[A <: Product](bytes: Array[Byte]): AvroDeSerializeOpsImpl = new AvroDeSerializeOpsImpl(bytes)

  implicit def toAvroDeserializeStringOpsImpl(that: String): AvroDeSerializeStringOpsImpl = new AvroDeSerializeStringOpsImpl(that)

  implicit class AvroHelper(val sc: StringContext) {
    def schema(args: Any*): Schema = {
      val str: String = sc.parts.mkString
      new Schema.Parser().parse(str)
    }
  }
}

object AvroOps extends AvroOps

class AvroSerializeOpsImpl[A <: Product: SchemaFor: Encoder](data: A) {
  private def withOutputStream(f: OutputStream => Unit): Array[Byte] = {
    val baos = new ByteArrayOutputStream
    f(baos)
    baos.toByteArray
  }

  def toAvroBinary(): Array[Byte] = withOutputStream { os =>
    val output = AvroOutputStream.binary[A]
    val aos: AvroOutputStream[A] = output.to(os).build(implicitly[SchemaFor[A]].schema)
    aos.write(data)
    aos.flush()
    aos.close()
  }
}

class AvroDeSerializeOpsImpl(bytes: Array[Byte]) {
  def parseAvroBinary[R <: Product: SchemaFor: Decoder, W <: Product](implicit writerSchemaFor: SchemaFor[W]): Try[R] = {
    parseAvroBinary[R](writerSchemaFor.schema)
  }

  def parseAvroBinary[R <: Product: Decoder](writerSchema: Schema)(implicit readerSchemaFor: SchemaFor[R]): Try[R] = {
    new AvroBinaryInputStream[R](new SeekableByteArrayInput(bytes), writerSchema, readerSchemaFor.schema).tryIterator.next()
  }
}

class AvroDeSerializeStringOpsImpl(that: String) extends StringOps with AvroOps {
  def parseAvroBinary[R <: Product: SchemaFor: Decoder, W <: Product: SchemaFor]: Try[R] = {
    that.parseHex.parseAvroBinary[R, W]
  }

  def parseAvroBinary[R <: Product: SchemaFor: Decoder](writerSchema: Schema): Try[R] = {
    that.parseHex.parseAvroBinary[R](writerSchema)
  }
}