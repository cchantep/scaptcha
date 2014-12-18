package scaptcha

import java.io.InputStream

import java.util.{ Calendar, Date }

import java.awt.{ Color, Font }

case class CaptchaInfo(
  value: String,
  code: Int) {

  override val toString = value
}

trait Captcha {
  /** Alphanumeric seed */
  def seed: String

  /**
   * (First integration step)
   * Returns captcha information (value & code), generated using seed.
   * [[CaptchaInfo.value]] must be then passed to [[textImage]] (text arg).
   * 
   * {{{
   * // Generates information for a temporal captcha with 8 characters
   * val info: CaptchaInfo = captchaInst.temporalCaptcha(8)
   * 
   * // then
   * val imgStream: java.io.InputStream = textImage(info.value)
   * // ready to display provided image stream of captcha
   * }}}
   * 
   * @param len Length of generated catcha
   * @param time Captcha time (temporal marker)
   */
  def temporalCaptcha(len: Int, time: Date = new Date()): CaptchaInfo = {
    val cal = Calendar.getInstance()
    cal.setTime(time)

    val code = cal.get(Calendar.MILLISECOND) % 100

    CaptchaInfo(keyStream.slice(code, code + len).mkString, code)
  }

  /**
   * Returns image stream (image/png) generated for catcha `text`.
   * 
   * @param text Previously generated [[CatchaInfo.value]]
   * @param background Image background color
   * @param foreground Image foreground color
   */
  def textImage(text: String, background: Option[Color] = None, foreground: Option[Color] = None, width: Int = 124, height: Int = 30): InputStream =
    Image.stream(width, height, 5, 22, text,
      foreground.getOrElse(Color.BLACK),
      background.getOrElse(Color.WHITE),
      Font.decode(null).deriveFont(20.0f))

  /**
   * Checks whether `text` typed in form is matching catcha 
   * whose `code` is specified one.
   * 
   * @param code Previously generated [[CatchaInfo.code]]
   * @param length Length of previously generated [[CatchaInfo.value]]
   * @param text Text typed by user in form
   * @return true if `text` matches specified catcha, or false
   */
  def matches(code: Int, length: Int, text: String): Boolean =
    keyStream.slice(code, code + length).mkString == text

  private lazy val keyStream: Stream[Char] = new Iterator[Char] {
    def hasNext = true

    var i = 0
    def next(): Char = {
      if (i == seed.length) {
        i = 1
        seed.charAt(0)
      } else {
        i += 1
        seed.charAt(i - 1)
      }
    }
  }.toStream
}
