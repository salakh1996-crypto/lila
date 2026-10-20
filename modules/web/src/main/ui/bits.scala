package lila.web
package ui

import lila.ui.*
import lila.ui.ScalatagsTemplate.*
import lila.core.i18n.Translate

object bits:

  object splitNumber extends NumberHelper:
    private val NumberFirstRegex = """(\d++)\s(.+)""".r
    private val NumberLastRegex = """\s(\d++)$""".r.unanchored

    def apply(s: Frag)(using ctx: Context)(using Translate): Frag =
      if ctx.blind then s
      else
        val rendered = s.render
        rendered match
          case NumberFirstRegex(number, html) =>
            frag(
              strong((~number.toIntOption).localize),
              br,
              raw(html)
            )
          case NumberLastRegex(n) if rendered.length > n.length + 1 =>
            frag(
              raw(rendered.dropRight(n.length + 1)),
              br,
              strong((~n.toIntOption).localize)
            )
          case h => raw(h.replaceIf('\n', "<br>"))

  lazy val stage = a(
    href := "https://lichess.org",
    style := """
background: #7f1010;
color: #fff;
position: fixed;
bottom: 0;
left: 0;
padding: .5em 1em;
border-top-right-radius: 3px;
z-index: 99;
"""
  ):
    "This is an empty WayChess preview website"

  val connectLinks: Frag = div(cls := "connect-links")(
    a(href := "https://github.com/salakh1996-crypto/lila", targetBlank, noFollow)("GitHub")
  )

  val logo = raw:
    """<svg class="lichess-logo-svg" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 50 50"><path d="M6 17 15 42 25 23 35 42 44 17" style="fill:none" stroke-width="6.5" stroke-linecap="round" stroke-linejoin="round"/><circle cx="25" cy="9.5" r="6" style="stroke:none"/></svg>"""
