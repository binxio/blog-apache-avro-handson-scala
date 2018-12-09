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

class AvroTest extends TestSpec {
  it should "serialize v1.person" in {
    v1.Person("").toAvroBinary().hex shouldBe "00"
    v1.Person("Dennis").toAvroBinary().hex shouldBe "0C44656E6E6973"
    "0C44656E6E6973".parseAvroBinary[v1.Person, v1.Person].success.value shouldBe v1.Person("Dennis")
  }
  it should "serialize v2.person" in {
    v2.Person("", 0).toAvroBinary().hex shouldBe "0000"
    v2.Person("Dennis", 42).toAvroBinary().hex shouldBe "0C44656E6E697354"
    "0C44656E6E697354".parseAvroBinary[v2.Person, v2.Person].success.value shouldBe v2.Person("Dennis", 42)
  }
  it should "serialize v3.person" in {
    v3.Person(0, "").toAvroBinary().hex shouldBe "0000"
    v3.Person(42, "Dennis").toAvroBinary().hex shouldBe "540C44656E6E6973"
    "540C44656E6E6973".parseAvroBinary[v3.Person, v3.Person].success.value shouldBe v3.Person(42, "Dennis")
  }
  it should "serialize v4.person" in {
    v4.Person(0, "", None).toAvroBinary().hex shouldBe "000000"
    v4.Person(42, "Dennis", None).toAvroBinary().hex shouldBe "540C44656E6E697300"
    v4.Person(42, "Dennis", Some(v4.LivingAddress(Some("Laapersveld 27"), Some("Hilversum"), Some("1213 VB")))).toAvroBinary().hex shouldBe "540C44656E6E697302021C4C61617065727376656C64203237021248696C76657273756D020E31323133205642"
    "540C44656E6E697302021C4C61617065727376656C64203237021248696C76657273756D020E31323133205642".parseAvroBinary[v4.Person, v4.Person].success.value shouldBe
      v4.Person(42, "Dennis", Some(v4.LivingAddress(Some("Laapersveld 27"), Some("Hilversum"), Some("1213 VB"))))
  }

  it should "evolve v1.Person" in {
    "0C44656E6E6973".parseAvroBinary[v2.Person, v1.Person].success.value shouldBe v2.Person("Dennis", 0)
    "0C44656E6E6973".parseAvroBinary[v3.Person, v1.Person].success.value shouldBe v3.Person(0, "Dennis")

    "540C44656E6E6973".parseAvroBinary[v1.Person, v3.Person].success.value shouldBe v1.Person("Dennis")
    "540C44656E6E6973".parseAvroBinary[v2.Person, v3.Person].success.value shouldBe v2.Person("Dennis", 42)

    "540C44656E6E697302021C4C61617065727376656C64203237021248696C76657273756D020E31323133205642".parseAvroBinary[v1.Person, v4.Person].success.value shouldBe v1.Person("Dennis")

    "0C44656E6E6973".parseAvroBinary[v1.Cat, v1.Person].success.value shouldBe v1.Cat("Dennis")
  }
}
