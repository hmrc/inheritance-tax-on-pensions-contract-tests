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

package uk.gov.hmrc.test.api.features.getoverviewrequests

import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.shouldBe
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json
import uk.gov.hmrc.test.api.helpers.IhtpHelper

import scala.io.Source

class GetOverviewRequestSpec extends AnyWordSpec with Matchers with BeforeAndAfterAll with IhtpHelper {

  override protected def afterAll(): Unit =
    try wsClient.close()
    finally super.afterAll()

  "The ihtp-overview GET end point" should {
    "return 200 for a successful retrieval" in {

      val pstrNumber  = "24000001IN"
      val theFromDate = "2026-01-01"
      val theDateTo   = "2026-12-31"
      val theStatus   = "Not reconciled"

      val response = getIhtpOverviewRequest(
        pstr = Some(pstrNumber),
        dateFrom = Some(theFromDate),
        dateTo = Some(theDateTo),
        status = Some(theStatus),
        overrideUrl = Some(
          s"$etmpHost/etmp/RESTAdapter/pods/reports/ihtp-overview?pstr=$pstrNumber&dateFrom=$theFromDate&dateTo=$theDateTo&status=$theStatus"
        )
      )

      response.status shouldBe 200

      val actual   = Json.parse(response.body)
      val expected = Json.parse(Source.fromResource("GetOverviewSuccess.json").mkString)

      Json.prettyPrint(actual) shouldBe Json.prettyPrint(expected)
    }
  }

  "The ihtp-overview GET end point for a Bad Request" should {
    "return 400 status" in {

      val pstrNumber  = "24000001IN"
      val theFromDate = "2026-01-01"
      val theDateTo   = "2026-12-31"
      val theStatus   = "BAD_REQUEST"

      val response = getIhtpOverviewRequest(
        pstr = Some(pstrNumber),
        dateFrom = Some(theFromDate),
        dateTo = Some(theDateTo),
        status = Some(theStatus),
        overrideUrl = Some(
          s"$etmpHost/etmp/RESTAdapter/pods/reports/ihtp-overview?pstr=$pstrNumber&dateFrom=$theFromDate&dateTo=$theDateTo&status=$theStatus"
        )
      )

      response.status shouldBe 400

      checkResponseBody(response.body, badRequestForGetOverviewRequest)
    }
  }

}
