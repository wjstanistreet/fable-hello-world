# fable-hello-world

The most over-engineered "Hello, World!" this side of the JVM — and yet it has
**zero dependencies** (munit for tests aside).

Run it and you get:

- **A big animated banner.** "HELLO, WORLD!" rendered from a hand-drawn 5×7
  bitmap font, drawn with Unicode half-blocks so every terminal row carries two
  independently colored pixel rows, shaded by a sweeping 24-bit rainbow.
- **Hello in 20 languages**, typed out live, each label tinted its own hue.

```
sbt run
```

## Options

```
sbt "run [options] [text]"
```

| Option         | Effect                                          |
| -------------- | ----------------------------------------------- |
| `--static`     | render one frame instead of animating           |
| `--plasma`     | classic demoscene plasma instead of the rainbow |
| `--quiet`      | banner only, skip the greetings                 |
| `--duration N` | animation length in seconds (default 4)         |
| `text`         | shout something else, e.g. `sbt "run HI SCALA"` |

When stdout isn't an interactive terminal (pipes, CI logs), it prints a single
static frame instead of animating.

## Under the hood

A deliberately small tour of Scala 3:

- `opaque type Rgb = Int` — 24-bit colors packed into an `Int` with zero
  allocation, HSV→RGB conversion, and `lerp`, without ever leaking the raw int
  ([Color.scala](src/main/scala/hello/Color.scala))
- extension methods for ANSI styling: `"text".fg(color).bold`
- a `Shader` function type `(x, y, t) => Rgb` — the animation is just a pure
  function of pixel position and time ([Banner.scala](src/main/scala/hello/Banner.scala))
- an `enum` with fields for the greetings ([Greetings.scala](src/main/scala/hello/Greetings.scala))
- a recursive pattern-match CLI parser ([Main.scala](src/main/scala/hello/Main.scala))
- an embedded bitmap font as data ([Font.scala](src/main/scala/hello/Font.scala))

## Tests

```
sbt test
```

22 munit tests cover the font geometry, color math (HSV corners, clamping,
wrap-around hues), frame rendering, greetings, and CLI parsing.
