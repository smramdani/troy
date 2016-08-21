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

import troy.cql.ast._
import org.scalatest._
import troy.cql.ast.dml.Select
import troy.cql.ast.SelectStatement

class SchemaValidationTest extends FlatSpec with Matchers {

  "Schema" should "fetch fields" in {

    val authorId = Table.Column("author_id", DataType.text, false, false)
    val authorName = Table.Column("author_name", DataType.text, true, false)
    val authorAge = Table.Column("author_age", DataType.int, true, false)
    val postId = Table.Column("post_id", DataType.text, false, false)
    val postTitle = Table.Column("post_title", DataType.text, false, false)

    val schema = Schema(Seq(
      CreateKeyspace(false, KeyspaceName("test"), Seq(Keyspace.Replication(Seq(("class", "SimpleStrategy"), ("replication_factor", "1"))))),
      CreateTable(false, TableName(Some(KeyspaceName("test")), "posts"), Seq(
        authorId,
        authorName,
        authorAge,
        postId,
        postTitle
      ), Some(Table.PrimaryKey(Seq("author_id"), Seq("post_id"))), Nil)
    )).right.get

    schema(select(table("test", "posts"), "author_id", "author_name", "author_age", "post_id", "post_title"))
      .right.get._1 shouldBe types(authorId, authorName, authorAge, postId, postTitle)
  }

  def table(keyspace: String, table: String) = TableName(Some(KeyspaceName(keyspace)), table)

  def select(table: TableName, columnNames: String*): SelectStatement = {
    import Select._
    val sci = columnNames.map(n => SelectionClauseItem(ColumnName(n), None))
    SelectStatement(None, SelectClause(sci), table, None, None, None, None, false)
  }

  def types(columns: Table.Column*) = Schema.Columns(columns.map(_.dataType))
}
