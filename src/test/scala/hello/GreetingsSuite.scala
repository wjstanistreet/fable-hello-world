package hello

class GreetingsSuite extends munit.FunSuite:

  test("there are plenty of ways to say hello"):
    assert(Greeting.values.length >= 20)

  test("no greeting or language is blank"):
    for g <- Greeting.values do
      assert(g.language.trim.nonEmpty, s"$g language")
      assert(g.text.trim.nonEmpty, s"$g text")

  test("languages are unique"):
    val langs = Greeting.values.map(_.language)
    assertEquals(langs.distinct.length, langs.length)

class OptionsSuite extends munit.FunSuite:

  test("defaults"):
    val o = Options.parse(Nil)
    assertEquals(o.text, "HELLO, WORLD!")
    assert(!o.static && !o.quiet)
    assertEquals(o.durationSec, 4)

  test("flags and free text combine, text is uppercased"):
    val o = Options.parse(Seq("--static", "--quiet", "--duration", "9", "hi scala"))
    assert(o.static && o.quiet)
    assertEquals(o.durationSec, 9)
    assertEquals(o.text, "HI SCALA")

  test("bad duration falls back to the default"):
    assertEquals(Options.parse(Seq("--duration", "soon")).durationSec, 4)
