package org.jfugue.parsers.msp5.expressions

import org.jfugue.Environment
//import org.jfugue.elements.expressions.{
//  IntExp => IExp,
//  ByteExp => BExp,
//  LongExp => LExp,
//  DoubleExp => DExp
//}
	  

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

abstract class IntExp extends NumericExpression[Int] /*with IExp*/ {
  implicit def wrapper(x: Int) = LInt(x)
//  implicit def jwrapper(x: java.lang.Integer) = LInt(x)
}
case class LInt(int: Int) extends IntExp {
  def eval(env: Environment) = int
}
case class IntInDict(key: String) extends IntExp {
  def eval(env: Environment) = env.getIntFromDictionary(key)
}

abstract class ByteExp extends Expression[Byte] /*with BExp*/ {
  implicit def wrapper(x: Byte) = LByte(x)
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
