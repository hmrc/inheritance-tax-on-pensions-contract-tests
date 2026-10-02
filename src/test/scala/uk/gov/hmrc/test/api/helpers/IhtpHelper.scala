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

import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.libs.ws.StandaloneWSResponse

import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

trait IhtpHelper extends HttpUtils {

  def ihtpPostPaymentNoticeEndpoint: String = s"$etmpHost/etmp/RESTAdapter/pods/reports/ihtp-payment-notice"
  def ihtpGetOverviewEndpoint: String       = s"$etmpHost//etmp/RESTAdapter/pods/reports/ihtp-overview"

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
      case None       => ihtpPostPaymentNoticeEndpoint
    }
    postUrl(url, Some(buildIhtpHeaders()), requestBody)
  }

  def postIhtpPaymentNotice(jsonFile: String): StandaloneWSResponse = {
    val requestBody = buildRequestBody(jsonFile)
    buildIhtpPaymentNoticeUrl(requestBody = requestBody)
  }

  def getIhtpRequest(
    pstr: Option[String] = None,
    ihtPaymentReference: Option[String] = None,
    versionNumber: Option[String] = None,
    fbNumber: Option[String] = None,
    overrideUrl: Option[String] = None
  ): StandaloneWSResponse = {

    val headers = Seq(
      "Accept" -> "application/json"
    )
    val url     = overrideUrl match {
      case Some(path) => path
      case None       => ihtpPostPaymentNoticeEndpoint
    }
    get(url, requestHeaders = Some(headers))
  }

  def getIhtpOverviewRequest(
    pstr: Option[String] = None,
    ihtPaymentReference: Option[String] = None,
    versionNumber: Option[String] = None,
    fbNumber: Option[String] = None,
    dateFrom: Option[String] = None,
    dateTo: Option[String] = None,
    status: Option[String] = None,
    overrideUrl: Option[String] = None
  ): StandaloneWSResponse = {

    val headers = Seq(
      "Accept" -> "application/json"
    )
    val url     = overrideUrl match {
      case Some(path) => path
      case None       => ihtpGetOverviewEndpoint
    }
    get(url, requestHeaders = Some(headers))
  }

  val noRecordsResponseForGetRequest: JsObject = Json.obj(
    "errors" -> Json.obj(
      "processingDate" -> "2026-06-07T16:12:49Z",
      "code"           -> "003",
      "text"           -> "Request could not be processed"
    )
  )

  val badRequestForGetOverviewRequest: JsObject = Json.obj(
    "origin"   -> "HoD",
    "response" -> Json.obj(
      "error" -> Json.obj(
        "code"    -> "400",
        "logID"   -> "Example id",
        "message" -> "Example message"
      )
    )
  )

}
