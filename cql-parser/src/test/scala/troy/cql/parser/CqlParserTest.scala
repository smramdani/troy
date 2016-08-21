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

package troy.cql.parser

import org.scalatest._
import troy.cql.ast._
import troy.cql.ast.ddl.{ Keyspace, KeyspaceName, Table }

class CqlParserTest extends FlatSpec with Matchers {

  "Cql Parser" should "parse multiline create table" in {
    val statement = parseSchemaAs[CreateTable](
      """
        CREATE TABLE test.posts (
          author_id text PRIMARY KEY
        );
      """
    )
    statement.ifNotExists shouldBe false
    statement.columns.size shouldBe 1
    statement.columns.head.dataType shouldBe DataType.text
    statement.columns.head.isPrimaryKey shouldBe true
    statement.columns.head.isStatic shouldBe false
    statement.columns.head.name shouldBe "author_id"
  }

  it should "parse multi-fields create table" in {
    val statement = parseSchemaAs[CreateTable](
      """
        CREATE TABLE test.posts (
          author_id text PRIMARY KEY,
          author_name text static,
          author_age int static,
          post_id text,
          post_title text
        );
      """
    )
    statement.ifNotExists shouldBe false
    statement.columns shouldBe Seq(
      Table.Column("author_id", DataType.text, false, true),
      Table.Column("author_name", DataType.text, true, false),
      Table.Column("author_age", DataType.int, true, false),
      Table.Column("post_id", DataType.text, false, false),
      Table.Column("post_title", DataType.text, false, false)
    )
    statement.primaryKey.isEmpty shouldBe true // Primary is defined inline instead
    statement.options.isEmpty shouldBe true
  }

  it should "parse create table with clustering columns" in {
    val statement = parseSchemaAs[CreateTable](
      """
        CREATE TABLE test.posts (
          author_id text,
          author_name text static,
          author_age int static,
          post_id text,
          post_title text,
          PRIMARY KEY ((author_id), post_id)
        );
      """
    )
    statement.ifNotExists shouldBe false
    statement.columns shouldBe Seq(
      Table.Column("author_id", DataType.text, false, false), // Yes, it is not THE primary key, only a partition key
      Table.Column("author_name", DataType.text, true, false),
      Table.Column("author_age", DataType.int, true, false),
      Table.Column("post_id", DataType.text, false, false),
      Table.Column("post_title", DataType.text, false, false)
    )
    statement.primaryKey.get.partitionKeys shouldBe Seq("author_id")
    statement.primaryKey.get.clusteringColumns shouldBe Seq("post_id")
  }

  it should "parse different primary key styles" in {
    def pk(pk: String) =
      parseSchemaAs[CreateTable](s"CREATE TABLE test.posts ( author_id text, author_name text, post_id text, post_title text, PRIMARY KEY $pk);").primaryKey.get

    pk("((author_id), post_id)") shouldBe Table.PrimaryKey(Seq("author_id"), Seq("post_id"))
    pk("(author_id, post_id)") shouldBe Table.PrimaryKey(Seq("author_id"), Seq("post_id"))
    pk("((author_id, author_name), post_id)") shouldBe Table.PrimaryKey(Seq("author_id", "author_name"), Seq("post_id"))
    pk("((author_id, author_name), post_id, post_title)") shouldBe Table.PrimaryKey(Seq("author_id", "author_name"), Seq("post_id", "post_title"))
    pk("(author_id, post_id, post_title)") shouldBe Table.PrimaryKey(Seq("author_id"), Seq("post_id", "post_title"))
  }

  it should "parse multiple statements " in {
    val statements = parseSchema(
      """
            CREATE KEYSPACE test WITH replication = {'class': 'SimpleStrategy' , 'replication_factor': '1'};
            CREATE TABLE test.posts (
              author_id text,
              author_name text static,
              author_age int static,
              post_id text,
              post_title text,
              PRIMARY KEY ((author_id), post_id)
            );
          """
    )

    statements.size shouldBe 2
    statements shouldBe Seq(
      CreateKeyspace(false, KeyspaceName("test"), Seq(Keyspace.Replication(Seq(("class", "SimpleStrategy"), ("replication_factor", "1"))))),
      CreateTable(false, TableName(Some(KeyspaceName("test")), "posts"), Seq(
        Table.Column("author_id", DataType.text, false, false),
        Table.Column("author_name", DataType.text, true, false),
        Table.Column("author_age", DataType.int, true, false),
        Table.Column("post_id", DataType.text, false, false),
        Table.Column("post_title", DataType.text, false, false)
      ), Some(Table.PrimaryKey(Seq("author_id"), Seq("post_id"))), Nil)
    )

  }

  def parseQuery(statement: String) =
    CqlParser
      .parseQuery(statement) match {
        case CqlParser.Success(res: SelectStatement, _) => res
        case CqlParser.Success(res, _)                  => fail(s"$res is not SelectStatement")
        case CqlParser.Failure(msg, next)               => fail(s"Parse Failure: $msg, line = ${next.pos.line}, column = ${next.pos.column}")
      }

  def parseSchema(statement: String) =
    CqlParser.parseSchema(statement) match {
      case CqlParser.Success(res, _)    => res
      case CqlParser.Failure(msg, next) => fail(s"Parse Failure: $msg, line = ${next.pos.line}, column = ${next.pos.column}")
    }

  def parseSchemaAs[T](statement: String) =
    parseSchema(statement)
      .head
      .asInstanceOf[T]
}
