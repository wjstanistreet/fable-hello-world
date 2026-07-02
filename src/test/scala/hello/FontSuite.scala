package hello

class FontSuite extends munit.FunSuite:

  test("every supported glyph is exactly 5x7"):
    for c <- ('A' to 'Z') ++ ('0' to '9') ++ "!?,.-' " do
      val g = Font.glyph(c)
      assertEquals(g.length, Font.Height, s"glyph '$c' row count")
      for row <- g do
        assertEquals(row.length, Font.Width, s"glyph '$c' row '$row'")

  test("lowercase maps to uppercase glyphs"):
    assertEquals(Font.glyph('h'), Font.glyph('H'))

  test("unknown characters fall back to '?'"):
    assertEquals(Font.glyph('~'), Font.glyph('?'))
    assert(!Font.supports('~'))

  test("rendered grid is rectangular with 1-column gaps between glyphs"):
    val grid = Font.render("HI")
    assertEquals(grid.length, Font.Height)
    val expectedWidth = 2 * Font.Width + 1
    for row <- grid do assertEquals(row.length, expectedWidth)
    assert(grid.forall(row => !row(Font.Width)), "gap column should be unlit")

  test("rendering the empty string yields empty rows"):
    val grid = Font.render("")
    assertEquals(grid.length, Font.Height)
    assert(grid.forall(_.isEmpty))

  test("'L' looks like an L"):
    val grid = Font.render("L")
    assert(grid.last.forall(identity), "bottom row fully lit")
    assert(grid.take(6).forall(row => row.head && !row.last), "left column lit, right unlit above the base")
