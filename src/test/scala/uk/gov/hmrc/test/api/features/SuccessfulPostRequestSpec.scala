package uk.gov.hmrc.test.api.features

import org.scalatest.{BeforeAndAfterAll, GivenWhenThen}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json
import play.api.libs.ws.DefaultBodyReadables.readableAsByteArray
import uk.gov.hmrc.apitestrunner.http.HttpClient
import uk.gov.hmrc.test.api.helpers.IhtpHelper

class SuccessfulPostRequestSpec
    extends AnyWordSpec
    with Matchers
    with BeforeAndAfterAll
    with IhtpHelper
    with HttpClient {
  "The ihtp-payment-notice POST end point" should {
    "return 201" in {
      val ackRef       = newAckRef()
      val response     = postIhtpPaymentNotice(jsonFile = "PostSubmitSuccess.json", newAckRef())
      response.status mustBe 201
      val responseBody = Json.parse(response.body)
      val successJson  = responseBody \ "success"
      (successJson \ "acknowledgementReference").as[String] mustBe ackRef
      (successJson \ "processingDate").asOpt[String].nonEmpty mustBe true
    }
  }

}
