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

package uk.gov.hmrc.test.api.features.getrequests

import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.shouldBe
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json
import uk.gov.hmrc.test.api.helpers.IhtpHelper

import scala.io.Source

class GetRequestSpec extends AnyWordSpec with Matchers with BeforeAndAfterAll with IhtpHelper {

  override protected def afterAll(): Unit =
    try wsClient.close()
    finally super.afterAll()

  "The ihtp-payment-notice GET end point using IHT payment reference and version number" should {
    "return 200 for a successful retrieval" in {

      val pstrNumber                = "24000001IN"
      val ihtPaymentReferenceNumber = "A123456/25A629671"
      val theVersionNumber          = "001"

      val response = getIhtpRequest(
        pstr = Some(pstrNumber),
        ihtPaymentReference = Some(ihtPaymentReferenceNumber),
        versionNumber = Some(theVersionNumber),
        overrideUrl = Some(
          s"$etmpHost/etmp/RESTAdapter/pods/reports/ihtp-payment-notice?pstr=$pstrNumber&ihtPaymentReference=$ihtPaymentReferenceNumber&versionNumber=$theVersionNumber"
        )
      )

      response.status shouldBe 200

      val actual   = Json.parse(response.body)
      val expected = Json.parse(Source.fromResource("GetRetrieveSuccessWithIhtPaymentAndVersion.json").mkString)

      Json.prettyPrint(actual) shouldBe Json.prettyPrint(expected)
    }
  }

  "The ihtp-payment-notice GET end point for No Records" should {
    "return 422 status" in {

      val pstrNumber     = Some("24000001IN")
      val actualFbNumber = Some("999999999999")

      val response = getIhtpRequest(
        pstr = pstrNumber,
        fbNumber = actualFbNumber,
        overrideUrl = Some(
          s"$etmpHost/etmp/RESTAdapter/pods/reports/ihtp-payment-notice?pstr=$pstrNumber&fbNumber=$actualFbNumber"
        )
      )

      response.status shouldBe 422

      checkResponseBody(response.body, noRecordsResponseForGetRequest)
    }
  }

}
