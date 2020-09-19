package com.pawelzabczynski.config

import com.pawelzabczynski.version.BuildInfo
import com.typesafe.scalalogging.StrictLogging
import pureconfig.ConfigSource

import scala.collection.immutable.TreeMap
import com.pawelzabczynski.version.BuildInfo
import pureconfig.generic.auto._

trait ConfigModule extends StrictLogging {
  lazy val config: Config = ConfigSource.default.loadOrThrow[Config]

  def logConfig(): Unit = {
    val baseinfo =
      s"""
         |${config.app.name} configuration
         |""".stripMargin

    val info = TreeMap(BuildInfo.toMap.toSeq: _*).foldLeft(baseinfo) {
      case (str, (k, v)) => str + s"$k $v\n"
    }
    logger.info(info)
  }

}
