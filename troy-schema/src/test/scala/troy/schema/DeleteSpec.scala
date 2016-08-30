/*
 * Copyright 2016 Tamer AbdulRadi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package troy.schema

import org.scalatest._
import troy.cql.ast.{ SelectStatement, _ }
import troy.cql.ast.ddl.{ Keyspace => CqlKeyspace, Table => CqlTable }
import troy.cql.ast.dml.Select

class DeleteSpec extends FlatSpec with Matchers {
  val schemaStatements = CqlParser.parseSchema("""
     CREATE KEYSPACE test WITH replication = {'class': 'SimpleStrategy' , 'replication_factor': '1'};
     CREATE TABLE test.posts (
       author_id uuid,
       post_id uuid,
       author_name text static,
       post_rating int,
       post_title text,
       PRIMARY KEY ((author_id), post_id)
     );
   """).get
  val schema = SchemaEngine(schemaStatements).get

  "Schema" should "support simple delete statement" ignore {
    val statement = parse("DELETE FROM test.posts WHERE author_id = uuid();")
    val (rowType, variableTypes) = schema(statement).get
    rowType.asInstanceOf[SchemaEngine.Columns].types.isEmpty shouldBe true
    variableTypes.isEmpty shouldBe true
  }

  it should "delete statement with variables" in {
    val statement = parse("DELETE FROM test.posts WHERE author_id = ?; ")
    val (rowType, variableTypes) = schema(statement).get
    rowType.asInstanceOf[SchemaEngine.Columns].types.isEmpty shouldBe true
    variableTypes.size shouldBe 1
    variableTypes(0) shouldBe DataType.uuid
  }

  it should "delete statement with IF EXISTS flag" in {
    val statement = parse("DELETE FROM test.posts WHERE author_id = ? IF EXISTS;")
    val (rowType, variableTypes) = schema(statement).get
    variableTypes shouldBe Seq(DataType.uuid)

    val columnTypes = rowType.asInstanceOf[SchemaEngine.Columns].types
    columnTypes.size shouldBe 1
    columnTypes(0) shouldBe DataType.boolean
  }

  def parse(s: String) = CqlParser.parseDML(s) match {
    case CqlParser.Success(result, _) =>
      result
    case CqlParser.Failure(msg, _) =>
      fail(msg)
  }
}
