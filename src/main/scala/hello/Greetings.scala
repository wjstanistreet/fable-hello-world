package hello

/** "Hello, World!" in many tongues. */
enum Greeting(val language: String, val text: String):
  case English    extends Greeting("English", "Hello, World!")
  case Spanish    extends Greeting("Spanish", "¡Hola, Mundo!")
  case French     extends Greeting("French", "Bonjour, le Monde !")
  case German     extends Greeting("German", "Hallo, Welt!")
  case Italian    extends Greeting("Italian", "Ciao, Mondo!")
  case Portuguese extends Greeting("Portuguese", "Olá, Mundo!")
  case Dutch      extends Greeting("Dutch", "Hallo, Wereld!")
  case Swedish    extends Greeting("Swedish", "Hej, Världen!")
  case Polish     extends Greeting("Polish", "Witaj, Świecie!")
  case Turkish    extends Greeting("Turkish", "Merhaba, Dünya!")
  case Russian    extends Greeting("Russian", "Привет, мир!")
  case Greek      extends Greeting("Greek", "Γεια σου, Κόσμε!")
  case Hebrew     extends Greeting("Hebrew", "שלום, עולם!")
  case Arabic     extends Greeting("Arabic", "مرحبا بالعالم!")
  case Hindi      extends Greeting("Hindi", "नमस्ते, दुनिया!")
  case Thai       extends Greeting("Thai", "สวัสดีชาวโลก!")
  case Chinese    extends Greeting("Chinese", "你好，世界！")
  case Japanese   extends Greeting("Japanese", "こんにちは、世界！")
  case Korean     extends Greeting("Korean", "안녕하세요, 세계!")
  case Esperanto  extends Greeting("Esperanto", "Saluton, Mondo!")

object Greetings:
  import Ansi.*

  private val padTo = Greeting.values.map(_.language.length).max + 2

  /** Print every greeting, each label tinted a different hue. */
  def printAll(typewriterMs: Int = 0): Unit =
    val all = Greeting.values
    for (g, i) <- all.zipWithIndex do
      val hue   = i.toDouble / all.length
      val label = (g.language + ":").padTo(padTo, ' ').fg(Rgb.fromHsv(hue, 0.6, 1.0))
      if typewriterMs > 0 then
        print(s"  $label")
        for ch <- g.text do
          print(ch)
          Console.flush()
          Thread.sleep(typewriterMs)
        println()
      else println(s"  $label${g.text}")
