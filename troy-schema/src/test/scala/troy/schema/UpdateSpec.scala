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
import troy.cql.ast._
import troy.cql.ast.ddl.{ Keyspace => CqlKeyspace, Table => CqlTable }

class UpdateSpec extends FlatSpec with Matchers {
  import VTestUtils._

  val schemaStatements = CqlParser.parseSchema("""
     CREATE KEYSPACE test WITH replication = {'class': 'SimpleStrategy' , 'replication_factor': '1'};
     CREATE TABLE test.posts (
       author_id uuid,
       post_id uuid,
       author_name text static,
       post_rating int,
       post_title text,
       comments map<int, text>,
       PRIMARY KEY ((author_id), post_id)
     );

     CREATE TABLE test.post_details (
       author_id uuid,
       id uuid,
       rating int,
       title text,
       tags set<text>,
       comment_ids set<int>,
       comment_userIds list<uuid>,
       comment_bodies list<text>,
       comments map<int, text>,
       PRIMARY KEY ((author_id), id)
     );
   """).get
  val schema = SchemaEngine(schemaStatements).get

  "Schema" should "support simple update statement" ignore {
    val statement = parse("UPDATE test.posts SET post_title = 'Testing' WHERE author_id = uuid();")
    val (rowType, variableTypes) = schema(statement).get
    rowType.asInstanceOf[SchemaEngine.Columns].types.isEmpty shouldBe true
    variableTypes.isEmpty shouldBe true
  }

  it should "update statement with variables" in {
    val statement = parse("UPDATE test.posts SET post_title = 'Testing' WHERE author_id = ?; ")
    val (rowType, variableTypes) = schema(statement).get
    rowType.asInstanceOf[SchemaEngine.Columns].types.isEmpty shouldBe true
    variableTypes.size shouldBe 1
    variableTypes(0) shouldBe DataType.Uuid
  }

  it should "update statement with variables in updateParam and where clause" in {
    val statement = parse("UPDATE test.posts USING TTL ? SET post_title = 'Testing' WHERE author_id = ?; ")
    val (rowType, variableTypes) = schema(statement).get
    rowType.asInstanceOf[SchemaEngine.Columns].types.isEmpty shouldBe true
    variableTypes.size shouldBe 2
    variableTypes(0) shouldBe DataType.Int
    variableTypes(1) shouldBe DataType.Uuid
  }

  it should "update statement with no variables " in {
    val statement = parse("UPDATE test.posts USING TTL 400 SET post_title = 'Testing' WHERE post_title = 'test'; ")
    val (_, variableTypes) = schema(statement).get
    variableTypes.size shouldBe 0
  }

  it should "update statement with variables in updateParam clause" in {
    val statement = parse("UPDATE test.posts USING TTL ? SET post_title = 'Testing' WHERE post_title = 'test'; ")
    val (rowType, variableTypes) = schema(statement).get
    rowType.asInstanceOf[SchemaEngine.Columns].types.isEmpty shouldBe true

    variableTypes.size shouldBe 1
    variableTypes(0) shouldBe DataType.Int
  }

  it should "update statement with variables in set clause" in {
    val statement = parse("UPDATE test.posts USING TTL 400 SET post_title = ? WHERE author_id = ?; ")
    val (rowType, variableTypes) = schema(statement).get
    rowType.asInstanceOf[SchemaEngine.Columns].types.isEmpty shouldBe true

    variableTypes.size shouldBe 2
    variableTypes(0) shouldBe DataType.Text
    variableTypes(1) shouldBe DataType.Uuid
  }

  it should "update statement with IF EXISTS flag" in {
    val statement = parse("UPDATE test.posts SET post_title = 'Testing' WHERE author_id = ? IF EXISTS;")
    val (rowType, variableTypes) = schema(statement).get
    variableTypes shouldBe Seq(DataType.Uuid)

    val columnTypes = rowType.asInstanceOf[SchemaEngine.Columns].types
    columnTypes.size shouldBe 1
    columnTypes(0) shouldBe DataType.Boolean
  }

  it should "update statement with IF simple condition variables" in {
    val statement = parse("UPDATE test.posts SET post_title = 'Testing' WHERE author_id = ? IF post_title = ?;")
    val (rowType, variableTypes) = schema(statement).get
    variableTypes shouldBe Seq(DataType.Uuid, DataType.Text)

    val columnTypes = rowType.asInstanceOf[SchemaEngine.Columns].types
    columnTypes.size shouldBe 1
    columnTypes(0) shouldBe DataType.Boolean
  }

  it should "update statement with IF IN condition variables" in {
    val statement = parse("UPDATE test.posts SET post_title = 'Testing' WHERE author_id = ? IF post_title IN ?;")
    val (rowType, variableTypes) = schema(statement).get
    variableTypes shouldBe Seq(DataType.Uuid, DataType.Tuple(Seq(DataType.Text)))

    val columnTypes = rowType.asInstanceOf[SchemaEngine.Columns].types
    columnTypes.size shouldBe 1
    columnTypes(0) shouldBe DataType.Boolean
  }

  it should "update statement with IF CONTAINS KEY condition" in {
    val statement = parse("UPDATE test.posts SET post_title = 'Testing' WHERE author_id = ? IF comments CONTAINS KEY ?;")
    val (rowType, variableTypes) = schema(statement).get
    variableTypes shouldBe Seq(DataType.Uuid, DataType.Text)

    val columnTypes = rowType.asInstanceOf[SchemaEngine.Columns].types
    columnTypes.size shouldBe 1
    columnTypes(0) shouldBe DataType.Boolean
  }

  it should "update statement with IF complex condition" in {
    val statement = parse("UPDATE test.post_details SET post_title = 'Testing' WHERE author_id = ? IF comment_userIds CONTAINS ? AND comment_bodies CONTAINS ? AND rating = ?;")
    val (rowType, variableTypes) = schema(statement).get
    variableTypes shouldBe Seq(DataType.Uuid, DataType.Uuid, DataType.Text, DataType.Int)

    val columnTypes = rowType.asInstanceOf[SchemaEngine.Columns].types
    columnTypes.size shouldBe 1
    columnTypes(0) shouldBe DataType.Boolean
  }

  def parse(s: String) = CqlParser.parseDML(s) match {
    case CqlParser.Success(result, _) =>
      result
    case CqlParser.Failure(msg, _) =>
      fail(msg)
  }
}
