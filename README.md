# SCaptcha

Scala captcha, or Simple captcha, and probably both Simple Scala (functional) captcha.

[![Build Status](https://travis-ci.org/cchantep/scaptcha.png)](https://travis-ci.org/cchantep/scaptcha)

## Usage

### SBT

To use SCaptcha in your SBT (or Play) projects, you can either built it yourself (see thereafter), or add following resolver:

```scala
resolvers += "Applicius Snapshots" at "https://raw.github.com/applicius/mvn-repo/master/snapshots/"

libraryDependencies += "scaptcha" %% "scaptcha" % "1.0-SNAPSHOT"
```

### Playframework

It can be integrated as Play controller.

First in view (e.g. temporal catcha of 8 characters):

```html
  @defining(Captcha.temporalCaptcha(8)) { captcha =>
  <form action="yourAction">
    <img src="@ctxPath@routes.Captcha.image(captcha.value)" />
    <input type="text" name="passphrase" value="" />
    <input type="hidden" name="captchaCode" value="@captcha.code" />
  </form>
  }
```

Then in controllers:

```scala
package controllers

import play.api.Play
import play.api.libs.iteratee.Enumerator
import play.api.mvc.{ Action, Controller }

object Captcha extends Controller with scaptcha.Captcha {
  // Use Play application secret as scaptcha seed
  lazy val seed = Play.current.configuration.
    getString("application.secret").map(_.filter(_ != '/')).
    getOrElse(System.identityHashCode(this).toString)

  def image(text: String) = Action {
    val imageStream = textImage(text)

    Ok.stream(Enumerator.fromStream(imageStream)).as("image/png")
  }
}
```

Finally, when form including captcha is submitted, it can be checked easily:

```scala
Captcha.matches(captchaCode/* catcha.code */, length/* of captcha.value */, passphrase) // Boolean
```

## Build

`sbt package`
