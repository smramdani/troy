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

package troy.cql.ast

//
//trait SchemaAlteringStatement

//
//case class KeyspaceReplication(`class`: String, replicationFactor: Int)
//case class CreateKeyspaceStatement(name: String, replication: KeyspaceReplication) extends SchemaAlteringStatement
//
//trait CreateIndexStatement extends SchemaAlteringStatement
//
//trait ModificationStatement extends Statement
//case class Insert extends ModificationStatement
//case class Update extends ModificationStatement
//case class Delete extends ModificationStatement

trait ConsistencyLevel
object ConsistencyLevel {
  case object Any extends ConsistencyLevel
  case object One extends ConsistencyLevel
  case object Quorum extends ConsistencyLevel
  case object All extends ConsistencyLevel
  case object LocalQuorum extends ConsistencyLevel
  case object EachQuorum extends ConsistencyLevel
}

case class KeyspaceName(name: String)

case class TableName(keyspace: Option[KeyspaceName], table: String) {
  override def toString = keyspace.map(_.name + ".").getOrElse("") + table
}
case class FunctionName(keyspace: Option[KeyspaceName], table: String)
