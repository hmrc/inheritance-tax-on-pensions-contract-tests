/*
 * Copyright 2026 HM Revenue & Customs
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

package uk.gov.hmrc.test.api.config

import com.typesafe.config.ConfigFactory

trait ContractTestConfig {

  private val config      = ConfigFactory.load()
  private val environment = config.getString("environment")
  private val hostPath    = s"$environment.hip.host"

  require(
    config.hasPath(hostPath) && config.getString(hostPath).trim.nonEmpty,
    s"Missing configuration '$hostPath'. Use environment=local for the stub, or configure the target HIP host."
  )

  val etmpHost: String = config.getString(hostPath).stripSuffix("/")

}
