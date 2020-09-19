package com.pawelzabczynski

import com.pawelzabczynski.config.ConfigModule

object Main {
  def main(args: Array[String]): Unit = {
    val configModule = new ConfigModule {}
    configModule.logConfig()
  }
}
