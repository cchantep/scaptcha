# SCaptcha

Scala captcha, or Simple captcha, and probably both Simple Scala (functional) captcha.

## Usage

### SBT

To use SCaptcha in your SBT (or Play) projects, you can either built it yourself (see thereafter), or add following resolver:

`"Applicius Snapshots" at "https://raw.github.com/applicius/mvn-repo/master/snapshots/"`

### Playframework

It can be integrated as Play controller.

```scala
package controllers

import play.api.Play
import play.api.libs.iteratee.Enumerator
import play.api.mvc.{ Action, Controller }

object Captcha extends Controller with scaptcha.Captcha {
  // Use Play application secret as scaptcha key
  lazy val key = Play.current.configuration.
    getString("application.secret").map(_.filter(_ != '/')).
    getOrElse(System.identityHashCode(this).toString)

  def image(text: String) = Action {
    val imageStream = textImage(temporalText(8))

    Ok.stream(Enumerator.fromStream(imageStream)).as("image/png")
  }
}
```

Then it can be added in view with:

```html
  @defining(Captcha.temporalText(8)) { captcha =>
  <form action="yourAction">
    <img src="@ctxPath@routes.Captcha.image(captcha.value)" />
    <input type="text" name="passphrase" value="" />
    <input type="hidden" name="captchaCode" value="@captcha.code" />
  </form>
  }
```

When form including captcha is submitted, it can be checked easily:

```scala
Captcha.matches(captchaCode,passphrase) // Boolean
```

## Build

`sbt package`

[![Build Status](https://travis-ci.org/cchantep/scaptcha.png)](https://travis-ci.org/cchantep/scaptcha)
