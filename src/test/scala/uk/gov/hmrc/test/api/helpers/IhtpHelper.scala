package uk.gov.hmrc.test.api.helpers

import com.typesafe.config.ConfigFactory
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.libs.ws.StandaloneWSResponse
import uk.gov.hmrc.test.api.config.ContractTestConfig

import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar

trait IhtpHelper extends HttpUtils {

  def ihtpPaymentNoticeEndpoint = s"$etmpHost/etmp/RESTAdapter/pods/reports/ihtp-payment-notice"

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
      case None       => s"$etmpHost/etmp/RESTAdapter/pods/reports/ihtp-payment-notice"
    }
    postUrl(url, Some(buildIhtpHeaders()), requestBody)
  }

  def postIhtpPaymentNotice(jsonFile: String, ackRef: String): StandaloneWSResponse = {
    val requestBody = buildRequestBody(jsonFile, ackRef)
    println(s"Ack Ref for jsonFile is $ackRef. Submitted to HIP at ${Calendar.getInstance().getTime}")
    buildIhtpPaymentNoticeUrl(requestBody = requestBody)
  }

  val Unauthorized: String = Json.obj("message" -> "Unauthorized").toString

}
