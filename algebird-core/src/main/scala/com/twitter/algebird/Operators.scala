/*
Copyright 2012 Twitter, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.twitter.algebird

object Operators {
  implicit def toPlus[T: Semigroup](t: T) = new PlusOp(t)
  implicit def toMinus[T: Group](t: T) = new MinusOp(t)
  implicit def toTimes[T: Ring](t: T) = new TimesOp(t)
  implicit def toDiv[T: Field](t: T) = new DivOp(t)
  implicit def toRichTraversable[T](t: TraversableOnce[T]) = new RichTraversable(t)
}

class PlusOp[T: Semigroup](t: T) {
  def +(other: T) = implicitly[Semigroup[T]].plus(t, other)
}

class MinusOp[T: Group](t: T) {
  def -(other: T) = implicitly[Group[T]].minus(t, other)
}

class TimesOp[T: Ring](t: T) {
  def *(other: T) = implicitly[Ring[T]].times(t, other)
}

class DivOp[T: Field](t: T) {
  def /(other: T) = implicitly[Field[T]].div(t, other)
}

class RichTraversable[T](t: TraversableOnce[T]) {
  def sumByKey[K, V](implicit ev: <:<[T, (K, V)], sg: Semigroup[V]): Map[K, V] =
    MapAlgebra.sumByKey(t.map { _.asInstanceOf[(K, V)] })

  def monoidSum(implicit monoid: Monoid[T]) = Monoid.sum(t)
  def ringProduct(implicit ring: Ring[T]) = Ring.product(t)
}
