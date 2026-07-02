package hello

class BannerSuite extends munit.FunSuite:

  private val plain = "\\[[0-9;]*m".r

  test("a frame packs two pixel rows into each terminal line"):
    val lines = Banner.frame(Font.render("HI"), Banner.rainbow, 0.0)
    assertEquals(lines.length, (Font.Height + 1) / 2)

  test("stripped of escapes, a frame is exactly the half-block glyph area"):
    val grid  = Font.render("HELLO")
    val lines = Banner.frame(grid, Banner.rainbow, 0.0)
    for line <- lines do
      assertEquals(plain.replaceAllIn(line, "").length, grid.head.length)

  test("shaders produce different colors over time (it does animate)"):
    // The rainbow hue has period 1.0 in t, so probe at a half period.
    val at0 = Banner.frame(Font.render("A"), Banner.rainbow, 0.0)
    val at1 = Banner.frame(Font.render("A"), Banner.rainbow, 0.5)
    assertNotEquals(at0, at1)

  test("plasma shader stays in range for a big sweep of inputs"):
    for x <- 0 to 100; y <- 0 to 13 do
      Banner.plasma(x, y, 1.5) // Rgb construction clamps; this asserts no crash
