package uk.gov.hmrc.test.api.helpers

import org.apache.pekko.actor.ActorSystem
import play.api.libs.json.JsValue
import play.api.libs.json.Json.parse
import play.api.libs.ws.JsonBodyWritables.writeableOf_JsValue
import play.api.libs.ws.StandaloneWSResponse
import uk.gov.hmrc.test.api.config.ContractTestConfig
import play.api.libs.ws.ahc.AhcConfigBuilder

import java.lang.Thread.currentThread
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.*
import scala.concurrent.{Await, Awaitable}
import scala.io.Source._
import scala.language.postfixOps

trait HttpUtils extends ContractTestConfig with uk.gov.hmrc.apitestrunner.http.HttpClient {

  val builder: AhcConfigBuilder    = new AhcConfigBuilder()
  implicit val system: ActorSystem = ActorSystem()

  def get(
    url: String,
    requestHeaders: Option[Seq[(String, String)]] = None
  ): StandaloneWSResponse = {
    val request = wsClient.url(url)
    await {
      requestHeaders match {
        case Some(h) => request.withHttpHeaders(h: _*).get()
        case None    => request.get()
      }
    }
  }

  def postUrl(
    url: String,
    requestHeaders: Option[Seq[(String, String)]] = None,
    body: JsValue
  ): StandaloneWSResponse = {
    val request = wsClient.url(url)
    await {
      requestHeaders match {
        case Some(h) => request.withHttpHeaders(h: _*).post(body)
        case None    => request.post(body)
      }
    }
  }

  def buildRequestBody(sourceFileName: String, acknowledgementReference: String): JsValue = {
    val templateStr = readFileAsString(sourceFileName)
    readFileAsString(sourceFileName)
    parse(templateStr.replace("$AckRef$", acknowledgementReference))
  }

  def newAckRef(): String = {
    val ref = System.currentTimeMillis % 1000000000L
    f"BRPYXX$ref%09d"
  }

  private def readFileAsString(resourceName: String): String =
    fromInputStream(currentThread().getContextClassLoader.getResourceAsStream(resourceName)).mkString

  protected val awaitableTimeout: FiniteDuration = 30.seconds

  def await[A](f: Awaitable[A]): A = Await.result(f, awaitableTimeout)

}
