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

package uk.gov.hmrc.test.api.helpers

import play.api.libs.json.JsValue
import play.api.libs.ws.StandaloneWSResponse

import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

trait IhtpHelper extends HttpUtils {

  def ihtpPaymentNoticeEndpoint: String = s"$etmpHost/etmp/RESTAdapter/pods/reports/ihtp-payment-notice"

  def getCorrelationId: String = java.util.UUID.randomUUID().toString

  private def buildIhtpHeaders(
  ): Seq[(String, String)] =
    Seq(
      "Accept"                -> "application/json; charset=utf-8",
      "Content-Type"          -> "application/json",
      "CorrelationId"         -> getCorrelationId,
      "X-Message-Type"        -> "Request",
      "X-Originating-System"  -> "MDTP",
      "X-Receipt-Date"        -> DateTimeFormatter.ISO_INSTANT.format(Instant.now().truncatedTo(ChronoUnit.SECONDS)),
      "X-Regime-Type"         -> "IHTP",
      "X-Transmitting-System" -> "HIP"
    )

  def buildIhtpPaymentNoticeUrl(
    requestBody: JsValue,
    overrideUrl: Option[String] = None
  ): StandaloneWSResponse = {

    val url = overrideUrl match {
      case Some(path) => s"$etmpHost$path"
      case None       => ihtpPaymentNoticeEndpoint
    }
    postUrl(url, Some(buildIhtpHeaders()), requestBody)
  }

  def postIhtpPaymentNotice(jsonFile: String): StandaloneWSResponse = {
    val requestBody = buildRequestBody(jsonFile)
    buildIhtpPaymentNoticeUrl(requestBody = requestBody)
  }

}
