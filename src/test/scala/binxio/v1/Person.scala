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

package binxio.v1

import binxio.AvroOps._
import com.sksamuel.avro4s.AvroNamespace
import org.apache.avro.Schema

object Person {
  val avroSchema: Schema = {
    schema"""
      {
        "type" : "record",
        "name" : "Person",
        "namespace" : "com.github.dnvriend",
        "fields" : [ {
          "name" : "name",
          "type" : "string",
          "default" : ""
        } ]
      }
    """
  }
}

@AvroNamespace("com.github.dnvriend")
case class Person(name: String = "")

