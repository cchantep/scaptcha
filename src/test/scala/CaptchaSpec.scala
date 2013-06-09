package scaptcha

import org.specs2.mutable.Specification

object CaptchaSpec extends Specification with Captcha with CaptchaFixtures {
  "Captcha" title

  "Temporal text" should {
    "be expected one for combination 0" in {
      temporalText(5, sec0) aka "text" mustEqual "ABCDE"
    }

    "be expected one for combination 30" in {
      temporalText(3, sec30) aka "text" mustEqual "EFG"
    }
  }
}

sealed trait CaptchaFixtures {
  import java.util.Calendar

  lazy val key = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

  lazy val sec0 = { 
    cal.set(Calendar.SECOND, 0)
    cal.getTime()
  }

  lazy val sec30 = { 
    cal.set(Calendar.SECOND, 30)
    cal.getTime()
  }

  private lazy val cal = Calendar.getInstance()
}
