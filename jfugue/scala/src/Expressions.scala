package org.jfugue.parsers.msp5.expressions
import org.jfugue.Environment

abstract class Expression[T] {
  type W = T
  def eval(env: Environment): T
}

abstract class NumericExpression[T] extends Expression[T] {
  /*  type E
  def +(y : E) : E
  def -(y : E) : E
  def *(y : E) : E
  def /(y : E) : E
*/ }

/*case class PlusExp[T <: NumericExpression[_]](x : T, y : T) extends OpsNumExp[T] {
  def eval(env: Environment) = x.eval(env) + y.eval(env)
}
case class MinusExp[T](x : T, y : T) extends OpsNumExp[T] {
  def eval(env: Environment) = x.eval(env) - y.eval(env)
}
case class TimesExp[T](x : T, y : T) extends OpsNumExp[T] {
  def eval(env: Environment) = x.eval(env) * y.eval(env)
}*/

abstract class IntExp extends NumericExpression[Int] {
  implicit def wrapper(x: Int) = LInt(x)

  def +(x: IntExp, y: IntExp): IntExp = (x, y) match {
    case (LInt(xx), LInt(yy)) => LInt(xx + yy)
    case (xx, yy) => Plus(xx, yy)
  }
  case class Plus(x: IntExp, y: IntExp) extends IntExp {
    def eval(env: Environment) = x.eval(env) + y.eval(env)
  }

  def -(x: IntExp, y: IntExp): IntExp = (x, y) match {
    case (LInt(xx), LInt(yy)) => LInt(xx - yy)
    case (xx, yy) => Minus(xx, yy)
  }
  case class Minus(x: IntExp, y: IntExp) extends IntExp {
    def eval(env: Environment) = x.eval(env) - y.eval(env)
  }

  def *(x: IntExp, y: IntExp): IntExp = (x, y) match {
    case (LInt(xx), LInt(yy)) => LInt(xx * yy)
    case (xx, yy) => Times(xx, yy)
  }
  case class Times(x: IntExp, y: IntExp) extends IntExp {
    def eval(env: Environment) = x.eval(env) * y.eval(env)
  }

  def /(x: IntExp, y: IntExp): IntExp = (x, y) match {
    case (LInt(xx), LInt(yy)) => LInt(xx / yy)
    case (xx, yy) => Quot(xx, yy)
  }
  case class Quot(x: IntExp, y: IntExp) extends IntExp {
    def eval(env: Environment) = x.eval(env) / y.eval(env)
  }

  def %(y: IntExp): IntExp = (this, y) match {
    case (LInt(xx), LInt(yy)) => LInt(xx % yy)
    case (xx, yy) => Minus(xx, yy)
  }
  case class Rem(x: IntExp, y: IntExp) extends IntExp {
    def eval(env: Environment) = x.eval(env) % y.eval(env)
  }

  def unary_-(): IntExp = this match {
    case LInt(xx) => LInt(-xx)
    case xx => Negate(xx)
  }
  case class Negate(x: IntExp) extends IntExp {
    def eval(env: Environment) = -x.eval(env)
  }

}
case class LInt(int: Int) extends IntExp {
  def eval(env: Environment) = int
}
case class IntInDict(key: String) extends IntExp {
  def eval(env: Environment) = env.getIntFromDictionary(key)
}

abstract class ByteExp extends Expression[Byte] {
  implicit def wrapper(x: Byte) = LByte(x)

  /*def +(x: ByteExp, y: ByteExp): ByteExp = (x, y) match {
    case (LByte(xx), LByte(yy)) => LByte(xx + yy)
    case (xx, yy) => Plus(xx, yy)
  }
  case class Plus(x: ByteExp, y: ByteExp) extends ByteExp {
    def eval(env: Environment) = x.eval(env) + y.eval(env)
  }

  def -(x: ByteExp, y: ByteExp): ByteExp = (x, y) match {
    case (LByte(xx), LByte(yy)) => LByte(xx - yy)
    case (xx, yy) => Minus(xx, yy)
  }
  case class Minus(x: ByteExp, y: ByteExp) extends ByteExp {
    def eval(env: Environment) = x.eval(env) - y.eval(env)
  }

  def *(x: ByteExp, y: ByteExp): ByteExp = (x, y) match {
    case (LByte(xx), LByte(yy)) => LByte(xx * yy)
    case (xx, yy) => Times(xx, yy)
  }
  case class Times(x: ByteExp, y: ByteExp) extends ByteExp {
    def eval(env: Environment) = x.eval(env) * y.eval(env)
  }

  def /(x: ByteExp, y: ByteExp): ByteExp = (x, y) match {
    case (LByte(xx), LByte(yy)) => LByte(xx / yy)
    case (xx, yy) => Quot(xx, yy)
  }
  case class Quot(x: ByteExp, y: ByteExp) extends ByteExp {
    def eval(env: Environment) = x.eval(env) / y.eval(env)
  }

  def %(y: ByteExp): ByteExp = (this, y) match {
    case (LByte(xx), LByte(yy)) => LByte(xx % yy)
    case (xx, yy) => Minus(xx, yy)
  }
  case class Rem(x: ByteExp, y: ByteExp) extends ByteExp {
    def eval(env: Environment) = x.eval(env) % y.eval(env)
  }

  def unary_-(): ByteExp = this match {
    case LByte(xx) => LByte(-xx)
    case xx => Negate(xx)
  }
  case class Negate(x: ByteExp) extends ByteExp {
    def eval(env: Environment) = -x.eval(env)
  }*/
}
case class LByte(byte: Byte) extends ByteExp {
  def eval(env: Environment) = byte
}
case class ByteInDict(key: String) extends ByteExp {
  def eval(env: Environment) = env.getByteFromDictionary(key)
}
case class LSB(int: IntExp) extends ByteExp {
  def eval(env: Environment) = (int.eval(env) % 128).asInstanceOf[Byte]
}
case class MSB(int: IntExp) extends ByteExp {
  def eval(env: Environment) = (int.eval(env) / 128).asInstanceOf[Byte]
}

abstract class LongExp extends Expression[Long] {
  implicit def wrapper(x: Long) = LLong(x)
}
case class LLong(long: Long) extends LongExp {
  def eval(env: Environment) = long
}
case class LongInDict(key: String) extends LongExp {
  def eval(env: Environment) = env.getLongFromDictionary(key)
}

abstract class DoubleExp extends Expression[Double] {
  implicit def wrapper(x: Double) = LDouble(x)
}
case class LDouble(double: Double) extends DoubleExp {
  def eval(env: Environment) = double
}
case class DoubleInDict(key: String) extends DoubleExp {
  def eval(env: Environment) = env.getDoubleFromDictionary(key)
}
