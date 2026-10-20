package lila.web
package ui

import play.api.mvc.RequestHeader

import lila.core.i18n.{ I18nKey as trans, Translate }
import lila.ui.*
import lila.ui.ScalatagsTemplate.{ *, given }

def mobileRedirect(using req: RequestHeader)(using Translate) =
  val callbackUrl = "org.lichess.mobile://login-callback" + req.rawQueryString.nonEmptyOption.so("?" + _)
  Page(trans.app.returningToApp.txt()).i18n(_.app):
    main(cls := "page-small box box-pad")(
      boxTop(
        h1(cls := "text")(trans.app.returningToApp())
      ),
      p(trans.app.ifAppDoesNotOpenAutomatically(trans.app.openTheApp())),
      a(href := callbackUrl, cls := "button")(trans.app.openTheApp())
    )

// WayChess has no mobile app yet: the page explains how to use the site from a phone.
def mobile(helpers: Helpers)(renderedCmsPage: Frag)(using t: Translate) =
  import helpers.*
  val ru = t.lang.language == "ru"

  Page(if ru then "Мобильное приложение WayChess" else "WayChess mobile app")
    .css("bits.mobile")
    .hrefLangs(lila.ui.LangPath(routes.Main.app)):
      main(
        div(cls := "mobile page-small box box-pad")(
          h1(cls := "box__top")(if ru then "Мобильное приложение WayChess" else "WayChess mobile app"),
          p(
            if ru then "Мобильное приложение WayChess в разработке."
            else "The WayChess mobile app is in development."
          ),
          p(
            if ru then
              "Пока откройте сайт в браузере телефона и добавьте его на главный экран: в Chrome — «Добавить на главный экран», в Safari — «Поделиться» → «На экран «Домой»»."
            else
              "Meanwhile, open the site in your phone's browser and add it to your home screen: \"Add to Home screen\" in Chrome, or Share → \"Add to Home Screen\" in Safari."
          ),
          renderedCmsPage
        )
      )
