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

package uk.gov.hmrc.test.api.features.postrequests

import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json
import uk.gov.hmrc.test.api.helpers.IhtpHelper

class PostRequestSpec extends AnyWordSpec with Matchers with BeforeAndAfterAll with IhtpHelper {

  override protected def afterAll(): Unit =
    try wsClient.close()
    finally super.afterAll()

  "The ihtp-payment-notice POST end point" should {
    "return 201 with a form bundle number and IHT payment reference" in {
      val response            = postIhtpPaymentNotice(jsonFile = "PostSubmitSuccess.json")
      withClue(s"POST $ihtpPostPaymentNoticeEndpoint returned ${response.status}: ${response.body}") {
        response.status mustBe 201
      }
      val responseBody        = Json.parse(response.body)
      val ihtResponse         = responseBody \ "success" \ "ihtResponse"
      val formBundleNo        = (ihtResponse \ "formBundleNo").as[String]
      val ihtPaymentReference = (ihtResponse \ "ihtPaymentReference").as[String]

      val actual = Json.parse(response.body)

      println(s"RESPONSE BODY:\n${Json.prettyPrint(actual)}")

      formBundleNo.length        must (be >= 1 and be <= 15)
      ihtPaymentReference.length must (be >= 1 and be <= 17)
    }
  }

}
