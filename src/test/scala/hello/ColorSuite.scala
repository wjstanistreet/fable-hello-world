package hello

import Rgb.*

class ColorSuite extends munit.FunSuite:

  test("components round-trip through the packed representation"):
    val c = Rgb(12, 200, 255)
    assertEquals(c.red, 12)
    assertEquals(c.green, 200)
    assertEquals(c.blue, 255)

  test("components are clamped to [0, 255]"):
    val c = Rgb(-20, 300, 128)
    assertEquals(c.red, 0)
    assertEquals(c.green, 255)
    assertEquals(c.blue, 128)

  test("HSV primaries land on the expected corners"):
    assertEquals(Rgb.fromHsv(0.0, 1.0, 1.0), Rgb(255, 0, 0))
    assertEquals(Rgb.fromHsv(1.0 / 3, 1.0, 1.0), Rgb(0, 255, 0))
    assertEquals(Rgb.fromHsv(2.0 / 3, 1.0, 1.0), Rgb(0, 0, 255))

  test("zero saturation is grayscale"):
    val c = Rgb.fromHsv(0.42, 0.0, 0.5)
    assertEquals(c.red, c.green)
    assertEquals(c.green, c.blue)

  test("hue wraps around and tolerates negatives"):
    assertEquals(Rgb.fromHsv(1.25, 1.0, 1.0), Rgb.fromHsv(0.25, 1.0, 1.0))
    assertEquals(Rgb.fromHsv(-0.75, 1.0, 1.0), Rgb.fromHsv(0.25, 1.0, 1.0))

  test("lerp endpoints and midpoint"):
    val a = Rgb(0, 0, 0)
    val b = Rgb(200, 100, 50)
    assertEquals(a.lerp(b, 0.0), a)
    assertEquals(a.lerp(b, 1.0), b)
    assertEquals(a.lerp(b, 0.5), Rgb(100, 50, 25))
