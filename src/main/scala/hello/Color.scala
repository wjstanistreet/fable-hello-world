package hello

/** A 24-bit RGB color packed into a single Int (0xRRGGBB).
  *
  * Opaque type: zero allocation per color, but the raw Int never leaks out.
  */
opaque type Rgb = Int

object Rgb:
  def apply(r: Int, g: Int, b: Int): Rgb =
    ((r.clamp255 << 16) | (g.clamp255 << 8) | b.clamp255)

  /** Convert HSV (h, s, v all in [0, 1]) to RGB. */
  def fromHsv(h: Double, s: Double, v: Double): Rgb =
    val hue    = ((h % 1.0) + 1.0) % 1.0 * 6.0
    val sector = hue.toInt % 6
    val f      = hue - hue.floor
    val p      = v * (1 - s)
    val q      = v * (1 - f * s)
    val t      = v * (1 - (1 - f) * s)
    val (r, g, b) = sector match
      case 0 => (v, t, p)
      case 1 => (q, v, p)
      case 2 => (p, v, t)
      case 3 => (p, q, v)
      case 4 => (t, p, v)
      case _ => (v, p, q)
    Rgb((r * 255).round.toInt, (g * 255).round.toInt, (b * 255).round.toInt)

  extension (c: Rgb)
    def red: Int   = (c >> 16) & 0xff
    def green: Int = (c >> 8) & 0xff
    def blue: Int  = c & 0xff

    /** Linear interpolation toward another color, t in [0, 1]. */
    def lerp(other: Rgb, t: Double): Rgb =
      inline def mix(a: Int, b: Int) = (a + (b - a) * t).round.toInt
      Rgb(mix(c.red, other.red), mix(c.green, other.green), mix(c.blue, other.blue))

  extension (n: Int) private def clamp255: Int = math.max(0, math.min(255, n))

/** ANSI escape helpers as extension methods on String. */
object Ansi:
  val Reset      = "[0m"
  val HideCursor = "[?25l"
  val ShowCursor = "[?25h"

  def cursorUp(lines: Int): String = s"[${lines}A"

  extension (s: String)
    def fg(c: Rgb): String =
      import Rgb.*
      s"[38;2;${c.red};${c.green};${c.blue}m$s$Reset"
    def bold: String = s"[1m$s$Reset"
    def dim: String  = s"[2m$s$Reset"
