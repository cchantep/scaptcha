# SCaptcha

Scala captcha, or Simple captcha, and probably both Simple Scala (functional) captcha.

## Usage

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
    getString("application.secret").
    getOrElse(System.identityHashCode(this).toString)

  def image(text: String) = Action {
    val imageStream = textImage(temporalText(8))

    Ok.stream(Enumerator.fromStream(imageStream)).as("image/png")
  }
}
```

## Build

`sbt package`