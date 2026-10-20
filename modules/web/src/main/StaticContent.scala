package lila.web

import play.api.libs.json.{ JsArray, Json }

import lila.common.Json.given
import lila.core.config.NetConfig

final class StaticContent(net: NetConfig):

  val robotsTxt = """User-agent: *
Allow: /
Disallow: /@/*/download
Disallow: /game/export/
Disallow: /games/export/
Disallow: /api/
Disallow: /opening/config/
Disallow: /study/search
Disallow: /study/embed/
Disallow: /embed/
Disallow: /video?*
Disallow: /training/of-player
Allow: /game/export/gif/thumbnail/
"""

  val manifest = Json.obj(
    "name" -> net.domain,
    "short_name" -> "WayChess",
    "start_url" -> "/",
    "display" -> "standalone",
    "background_color" -> "#161512",
    "theme_color" -> "#161512",
    "description" -> "Free online chess that works in Russia without a VPN.",
    "icons" -> List(32, 64, 128, 192, 256, 512, 1024).map: size =>
      Json.obj(
        "src" -> s"//${net.assetDomain}/assets/logo/waychess-favicon-$size.png",
        "sizes" -> s"${size}x$size",
        "type" -> "image/png"
      )
  )

  val variantsJson = JsArray:
    chess.variant.Variant.list.all.map: v =>
      Json.obj(
        "id" -> v.id,
        "key" -> v.key,
        "name" -> v.name
      )

  def legacyQaQuestion(id: Int) =
    val faq = routes.Main.faq.url
    id match
      case 103 => s"$faq#acpl"
      case 258 => s"$faq#marks"
      case 13 => s"$faq#titles"
      case 87 => routes.User.ratingDistribution(PerfKey.blitz).url
      case 110 => s"$faq#name"
      case 29 => s"$faq#titles"
      case 4811 => s"$faq#lm"
      case 216 => routes.Main.app.url
      case 340 => s"$faq#trophies"
      case 6 => s"$faq#ratings"
      case 207 => s"$faq#hide-ratings"
      case 547 => s"$faq#leaving"
      case 259 => s"$faq#trophies"
      case 342 => s"$faq#provisional"
      case 50 => routes.Cms.help.url
      case 46 => s"$faq#name"
      case 122 => s"$faq#marks"
      case _ => faq

  val organizationScript = lila.ui.bits.structuredData("Organization"):
    Json.obj(
      "url" -> net.baseUrl,
      "sameAs" -> List("https://github.com/salakh1996-crypto/lila"),
      "logo" -> s"https://${net.assetDomain}/assets/logo/waychess.svg",
      "name" -> "WayChess",
      "description" -> "Free online chess that works in Russia without a VPN."
    )
