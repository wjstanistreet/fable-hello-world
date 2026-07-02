package hello

import Ansi.*

/** The most over-engineered "Hello, World!" this side of the JVM.
  *
  * Usage: run [options] [text]
  *   --static      render one frame instead of animating
  *   --plasma      use the plasma shader instead of the rainbow sweep
  *   --quiet       banner only, skip the multilingual greetings
  *   --duration N  animation length in seconds (default 4)
  */
@main def hello(args: String*): Unit =
  val opts = Options.parse(args)

  val unsupported = opts.text.filterNot(Font.supports)
  if unsupported.nonEmpty then
    Console.err.println(s"warning: no glyph for ${unsupported.distinct.mkString("'", "', '", "'")}, using '?'")

  println()
  if opts.static || !Terminal.isInteractive then Banner.show(opts.text, opts.shader)
  else Banner.animate(opts.text, opts.shader, frames = opts.durationSec * 1000 / 33, delayMs = 33)
  println()

  if !opts.quiet then
    Greetings.printAll(typewriterMs = if opts.static || !Terminal.isInteractive then 0 else 4)
    println()
    println("  Powered by ".dim + s"Scala ${Terminal.scalaVersion}".bold.fg(Rgb(220, 50, 50)) + " and exactly zero dependencies.".dim)
    println()

final case class Options(
    text: String = "HELLO, WORLD!",
    static: Boolean = false,
    quiet: Boolean = false,
    durationSec: Int = 4,
    shader: Banner.Shader = Banner.rainbow
)

object Options:
  def parse(args: Seq[String]): Options =
    def loop(rest: List[String], acc: Options): Options = rest match
      case Nil                       => acc
      case "--static" :: tail        => loop(tail, acc.copy(static = true))
      case "--plasma" :: tail        => loop(tail, acc.copy(shader = Banner.plasma))
      case "--quiet" :: tail         => loop(tail, acc.copy(quiet = true))
      case "--duration" :: n :: tail => loop(tail, acc.copy(durationSec = n.toIntOption.getOrElse(4)))
      case text :: tail              => loop(tail, acc.copy(text = text.toUpperCase))
    loop(args.toList, Options())

object Terminal:
  /** Animations only make sense on a live terminal, not in a pipe or a CI log. */
  def isInteractive: Boolean =
    System.console() != null && sys.env.get("TERM").exists(_ != "dumb")

  /** The Scala version, read from the scala3-library jar manifest. */
  def scalaVersion: String =
    Option(classOf[CanEqual[?, ?]].getPackage)
      .flatMap(p => Option(p.getImplementationVersion))
      .getOrElse("3")
