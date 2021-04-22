package com.raintea.catsEffects

import io.estatico.newtype.macros.newtype

class Chapter1 {
  trait Counter[F[_]] {
    def incr: F[Unit]
    def get: F[Int]
  }

  @newtype case class RedisKey(value: String)

  class LiveCounter[F[_]]( key: RedisKey,
                           cmd: RedisCommands[F, String, Int] ) extends Counter[F] {
    def incr: F[Unit] = cmd.incr(key).void
    def get: F[Int] = cmd.get(key).map(_.getOrElse(0))
  }
}
