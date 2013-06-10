package scaptcha

import java.io.InputStream

import java.util.{ Calendar, Date }

import java.awt.{ Color, Font }

case class CaptchaText(
  value: String,
  code: Int) {

  override val toString = value
}

trait Captcha {
  def key: String

  def textImage(text: String, background: Option[Color] = None, foreground: Option[Color] = None): InputStream =
    Image.stream(124, 30, 5, 22, text,
      foreground.getOrElse(Color.BLACK),
      background.getOrElse(Color.WHITE),
      Font.decode(null).deriveFont(20.0f))

  def temporalText(len: Int, time: Date = new Date()): CaptchaText = {
    val cal = Calendar.getInstance()
    cal.setTime(time)

    val code = cal.get(Calendar.SECOND)

    CaptchaText(keyStream.slice(code, code + len).mkString, code)
  }

  def matches(code: Int, text: String): Boolean = 
    keyStream.slice(code, code + text.length).mkString == text

  private def keyStream: Stream[Char] = new Iterator[Char] {
    def hasNext = true

    var i = 0
    def next(): Char = {
      if (i == key.length) {
        i = 1
        key.charAt(0)
      } else {
        i += 1
        key.charAt(i - 1)
      }
    }
  }.toStream
}
