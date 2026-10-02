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

import play.api.libs.json.{JsValue, Json, Reads}
import play.api.libs.json.Json.parse
import play.api.libs.ws.JsonBodyWritables.writeableOf_JsValue
import play.api.libs.ws.StandaloneWSResponse
import uk.gov.hmrc.test.api.config.ContractTestConfig

import java.lang.Thread.currentThread
import scala.concurrent.duration.*
import scala.concurrent.{Await, Awaitable}
import scala.io.Source
import scala.util.Using

trait HttpUtils extends ContractTestConfig with uk.gov.hmrc.apitestrunner.http.HttpClient {

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

  def buildRequestBody(sourceFileName: String): JsValue = parse(readFileAsString(sourceFileName))

  def checkResponseBody[A](body: String, expected: A)(implicit rds: Reads[A]): Unit =
    Json
      .parse(body)
      .validate[A]
      .fold(
        invalid => {
          println(s"Invalid: $invalid\n\nActual: $body\n\nExpected: $expected")
          assert(assertion = false, s"Json Parse error. Actual Response:\n$body \n\n Json Errors:\n$invalid")
        },
        valid => assert(valid == expected, message = s"Expected $expected Actual: $valid")
      )

  private def readFileAsString(resourceName: String): String =
    Using.resource(Source.fromResource(resourceName, currentThread().getContextClassLoader))(_.mkString)

  protected val awaitableTimeout: FiniteDuration = 30.seconds

  def await[A](f: Awaitable[A]): A = Await.result(f, awaitableTimeout)

}
