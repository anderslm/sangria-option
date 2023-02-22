package example

import sangria.schema._
import sangria.execution._
import sangria.macros._
import sangria.marshalling.circe._
import scala.concurrent.ExecutionContext.Implicits.global

object Hello extends App {

  val FieldWithOption = ObjectType[Unit, Option[String]](
    "Option", 
    fields[Unit, Option[String]](Field("hello", StringType, resolve = _.value.getOrElse("Another greeting"))))

  val QueryTypeThatWorks = ObjectType("Query", fields[Unit, Unit](
    Field("hello", FieldWithOption, resolve = _ => Some("Hello world!"))
  ))

  val QueryTypeThatFails = ObjectType("Query", fields[Unit, Unit](
    Field("hello", FieldWithOption, resolve = _ => None)
  ))

  val schema = Schema(QueryTypeThatFails)

  val query = graphql"{ hello { hello } }"

  val result = Executor.execute(schema, query)

  result.foreach(res => println(res.spaces2))
}