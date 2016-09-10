package troy

package object schema {
  type Result[T] = V[Message, Message, T]
  object Result {
    def apply[T](value: T): Result[T] =
      V.success(value)
  }
}
