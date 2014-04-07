package scaptcha

import org.specs2.mutable.Specification

object CaptchaSpec extends Specification with Captcha with CaptchaFixtures {
  "Captcha" title

  "Temporal text" should {
    "be expected one for combination 0" in {
      temporalCaptcha(5, msec0).toString aka "text" mustEqual "ABCDE"
    }

    "be expected one for combination 30" in {
      temporalCaptcha(3, msec30).toString aka "text" mustEqual "EFG"
    }
  }

  "Reverse check" should {
    "be ok" in {
      (matches(0, 5, "ABCDE") aka "matches 0" must beTrue).
        and(matches(30, 3, "EFG") aka "matches 30" must beTrue)
    }

    "not be ok" in {
      (matches(0, 3, "EFG") aka "matches 0" must beFalse).
        and(matches(0, 5, "ABC") aka "matches 0" must beFalse)
    }
  }
}

sealed trait CaptchaFixtures {
  import java.util.Calendar

  lazy val seed = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

  lazy val msec0 = { 
    cal.set(Calendar.MILLISECOND, 0)
    cal.getTime()
  }

  lazy val msec30 = { 
    cal.set(Calendar.MILLISECOND, 30)
    cal.getTime()
  }

  private lazy val cal = Calendar.getInstance()
}
