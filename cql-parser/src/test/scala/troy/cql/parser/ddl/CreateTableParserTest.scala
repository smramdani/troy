package troy.cql.parser.ddl

import org.scalatest._
import troy.cql.ast.ddl.Table
import troy.cql.ast.{ CreateTable, DataType }
import troy.cql.parser.ParserTestUtils.parseSchemaAs

class CreateTableParserTest extends FlatSpec with Matchers {
  "Create Table Parser" should "parse simple create table" in {
    val statement = parseSchemaAs[CreateTable]("CREATE TABLE test.posts (author_id text PRIMARY KEY);")
    statement.ifNotExists shouldBe false
    statement.tableName.keyspace.get.name shouldBe "test"
    statement.tableName.table shouldBe "posts"
    statement.columns.size shouldBe 1
    statement.columns.head.dataType shouldBe DataType.text
    statement.columns.head.isPrimaryKey shouldBe true
    statement.columns.head.isStatic shouldBe false
    statement.columns.head.name shouldBe "author_id"
  }

  it should "parse multiline create table" in {
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
}
