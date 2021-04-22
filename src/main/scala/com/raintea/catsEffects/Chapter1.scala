package com.raintea.catsEffects

import cats.effect.Ref
import io.estatico.newtype.macros.newtype
import dev.profunktor.redis4cats.RedisCommands

class Chapter1 {
  trait Counter[F[_]] {
    def incr: F[Unit]
    def get: F[Int]
  }

  @newtype case class RedisKey(value: String)

  class LiveCounter[F[_]]( key: RedisKey,
                           cmd: RedisCommands[F, String, Int] ) extends Counter[F] {
    def incr: F[Unit] =
      cmd.incr(key).void

    def get: F[Int] =
      cmd.get(key).map(_.getOrElse(0))
  }


  class TestCounter[F[_]] ( ref: Ref[F, Int]
                          ) extends Counter[F] {
    def incr: F[Unit] = ref.update(_ + 1)
    def get: F[Int] = ref.get
  }
}
