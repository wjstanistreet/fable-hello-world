package hello

/** Renders bitmap-font text as true-color terminal art.
  *
  * Uses the upper-half-block character so every terminal row carries two
  * pixel rows, each with its own 24-bit color.
  */
object Banner:

  /** A shader decides the color of a pixel from its position and time. */
  type Shader = (x: Int, y: Int, t: Double) => Rgb

  /** Sweeping rainbow: hue moves with x and time. */
  val rainbow: Shader = (x, y, t) =>
    Rgb.fromHsv(x / 42.0 - t, 0.85, 1.0)

  /** Classic plasma: interfering sine waves in x, y and time. */
  val plasma: Shader = (x, y, t) =>
    val v = math.sin(x / 6.0 + t * 4) +
      math.sin((x + y) / 5.0 + t * 3) +
      math.sin(math.hypot(x - 30, y - 7) / 4.0 - t * 5)
    Rgb.fromHsv(v / 6.0 + 0.5, 0.8, 1.0)

  private val unlit = Rgb(28, 28, 38)

  /** One frame of the banner as terminal lines. Pixel rows are paired: the
    * top of each pair colors the half-block's foreground, the bottom its
    * background.
    */
  def frame(grid: Vector[Vector[Boolean]], shader: Shader, t: Double): Vector[String] =
    import Rgb.*
    val height = grid.length
    val width  = if height == 0 then 0 else grid.head.length
    Vector.tabulate((height + 1) / 2) { pair =>
      val sb   = StringBuilder()
      val yTop = pair * 2
      val yBot = yTop + 1
      for x <- 0 until width do
        def colorAt(y: Int): Rgb =
          if y < height && grid(y)(x) then shader(x, y, t) else unlit
        val top = colorAt(yTop)
        val bot = colorAt(yBot)
        sb ++= s"[38;2;${top.red};${top.green};${top.blue}m"
        sb ++= s"[48;2;${bot.red};${bot.green};${bot.blue}m▀"
      sb ++= Ansi.Reset
      sb.result()
    }

  /** Animate the banner in place, then leave the final frame on screen. */
  def animate(text: String, shader: Shader, frames: Int, delayMs: Int): Unit =
    val grid  = Font.render(text)
    val lines = (grid.length + 1) / 2
    print(Ansi.HideCursor)
    try
      for i <- 0 until frames do
        val t = i * delayMs / 1000.0
        frame(grid, shader, t).foreach(println)
        if i < frames - 1 then
          Thread.sleep(delayMs)
          print(Ansi.cursorUp(lines))
    finally print(Ansi.ShowCursor)

  /** Print a single static frame. */
  def show(text: String, shader: Shader): Unit =
    frame(Font.render(text), shader, 0.0).foreach(println)
