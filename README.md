# Clickhouse sink for akka-stream

This is a prototype of akka-stream compatible Sink for Clickhouse database.
Main features:

* Can transparently write any case class (if key names and types match).
* Able to generate DDL for arbitrary case class.
* has batching, back-pressure and stuff.
* supports Array, Nested data types

TODO:
* nullable fields

## Example

DDL for case class:

```scala
  import io.findify.clickhousesink.encoder.generic._
  import io.findify.clickhousesink.encoder.generic.auto._
  
  case class Simple(key: String, value: Int)
  
  val encoder = deriveEncoder[Simple]
  encoder.ddl("simple", "ENGINE = Memory") 
  // will emit "CREATE TABLE simple (key String,value Int32)"
```

Writing rows to database:

```scala
  import akka.actor.ActorSystem
  import akka.stream.ActorMaterializer
  import akka.stream.scaladsl.{Keep, Sink, Source}
  import io.findify.clickhousesink.ClickhouseSink
  
  case class Simple(key: String, value: Int)

  val source = Source(List(Simple("a", 1), Simple("b", 2), Simple("c", 3)))
  val sink = Sink.fromGraph(new ClickhouseSink[Simple](
    host = "localhost",
    port = 8123,
    table = "simple"
  ))
  val result = source.runWith(sink)
  // will perform "INSERT INTO simple VALUES ('a', 1),('b',2),('c',3);"

```

## Licence

The MIT License (MIT)

Copyright (c) 2017 Findify AB

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.