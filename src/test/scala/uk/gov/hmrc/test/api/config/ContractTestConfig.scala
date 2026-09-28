package uk.gov.hmrc.test.api.config

import com.typesafe.config.ConfigFactory

trait ContractTestConfig {

  private val config = ConfigFactory.load("environment.conf")
//  println(config.root().render())
  private val env = Option(System.getProperty("env")).getOrElse(throw new IllegalArgumentException("Didn't find environments property"))

  // ETMP API Config
  val etmpHost: String        = config.getString(s"environments.$env.hip.host")
//  val ihtpBearerToken: String = config.getString(s"common.bearerToken")
  val ihtpEnvironment: String = config.getString(s"common.environment")

}
