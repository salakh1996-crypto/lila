package lila.web
package ui

import lila.ui.*
import lila.ui.ScalatagsTemplate.{ *, given }

val fideHandbookUrl = "https://handbook.fide.com/chapter/E012023"

final class FaqUi(helpers: Helpers, sitePages: SitePages)(
    standardRankableDeviation: Int,
    variantRankableDeviation: Int
):
  import helpers.{ given, * }
  import trans.faq as trf

  private def cmsPageUrl(key: String) = routes.Cms.lonePage(lila.core.id.CmsPageKey(key))

  private def ruEn(ru: String, en: String)(using ctx: Context) =
    if ctx.lang.language == "ru" then ru else en

  private def question(id: String, title: String, answer: Frag*) =
    details(
      st.id := id,
      cls := "question",
      name := "faq"
    )(
      summary(span(title)),
      div(cls := "answer")(answer)
    )

  def apply(using Context) =
    sitePages
      .SitePage(
        title = "Frequently Asked Questions",
        active = "faq"
      )
      .css("bits.faq"):
        div(cls := "faq box box-pad")(
          h1(cls := "box__top")(trf.frequentlyAskedQuestions()),
          h2("WayChess"),
          question(
            "name",
            ruEn("Что такое WayChess?", "What is WayChess?"),
            p(
              ruEn(
                "WayChess — бесплатный шахматный сайт, который работает в России без VPN. Здесь можно играть онлайн с соперниками со всего мира, решать задачи, участвовать в турнирах и анализировать партии.",
                "WayChess is a free chess website that works in Russia without a VPN. Play online against opponents from all over the world, solve puzzles, join tournaments and analyse your games."
              )
            )
          ),
          question(
            "lichess",
            ruEn("WayChess связан с Lichess?", "Is WayChess affiliated with Lichess?"),
            p(
              ruEn(
                "Нет. WayChess — независимый проект, основанный на открытом коде Lichess, который распространяется по лицензии GNU AGPL. Исходный код WayChess тоже открыт: ",
                "No. WayChess is an independent project based on the open source code of Lichess, which is distributed under the GNU AGPL license. The WayChess source code is open too: "
              ),
              a(href := "/source")(trans.site.sourceCode()),
              "."
            )
          ),
          question(
            "contributing",
            ruEn("Как помочь WayChess?", "How can I help WayChess?"),
            p(
              ruEn(
                "Расскажите о сайте друзьям и шахматному клубу, сообщайте об ошибках и предлагайте идеи на ",
                "Tell your friends and your chess club about the site, report bugs and suggest ideas on "
              ),
              a(href := "https://github.com/salakh1996-crypto/lila/issues")("GitHub"),
              "."
            )
          ),
          question(
            "keyboard-shortcuts",
            trf.keyboardShortcuts.txt(),
            p(
              trf.keyboardShortcutsExplanation()
            )
          ),
          h2(trf.fairPlay()),
          question(
            "rating-refund",
            trf.whenAmIEligibleRatinRefund.txt(),
            p(
              trf.ratingRefundExplanation()
            )
          ),
          question(
            "leaving",
            trf.preventLeavingGameWithoutResigning.txt(),
            p(
              trf.leavingGameWithoutResigningExplanation()
            )
          ),
          question(
            "mod-application",
            trf.howCanIBecomeModerator.txt(),
            p(
              trf.youCannotApply()
            )
          ),
          question(
            "correspondence",
            trf.isCorrespondenceDifferent.txt(),
            p(
              trf.youCanUseOpeningBookNoEngine()
            ),
            p(
              trf.pleaseReadFairPlayPage(a(href := cmsPageUrl("fair-play"))(trf.fairPlayPage()))
            )
          ),
          h2(trf.gameplay()),
          question(
            "time-controls",
            trf.howBulletBlitzEtcDecided.txt(),
            p(
              trf.basedOnGameDuration(strong(trf.durationFormula()))
            ),
            ul(
              li(trf.inferiorThanXsEqualYtimeControl(29, trans.site.ultraBullet())),
              li(trf.inferiorThanXsEqualYtimeControl(179, trans.site.bullet())),
              li(trf.inferiorThanXsEqualYtimeControl(479, trans.site.blitz())),
              li(trf.inferiorThanXsEqualYtimeControl(1499, trans.site.rapid())),
              li(trf.superiorThanXsEqualYtimeControl(1500, trans.site.classical()))
            )
          ),
          question(
            "variants",
            trf.whatVariantsCanIplay.txt(),
            p(
              trf.lichessSupportChessAnd(
                a(href := routes.Cms.variantHome)(trf.eightVariants())
              )
            )
          ),
          question(
            "acpl",
            trf.whatIsACPL.txt(),
            p(
              trf.acplExplanation()
            )
          ),
          question(
            "timeout",
            trf.insufficientMaterial.txt(),
            p(
              trf.lichessFollowFIDErules(a(href := fideHandbookUrl)(trf.fideHandbookX("§6.9")))
            )
          ),
          question(
            "en-passant",
            trf.discoveringEnPassant.txt(),
            p(
              trf.explainingEnPassant(
                a(href := "https://en.wikipedia.org/wiki/En_passant")(trf.goodIntroduction()),
                a(href := fideHandbookUrl)(trf.fideHandbook()),
                a(href := s"${routes.Learn.index}#/15")(trf.lichessTraining())
              )
            ),
            p(
              trf.watchIMRosenCheckmate(
                a(href := "https://www.reddit.com/r/AnarchyChess/comments/p9wuic/eric_rosen_ascending/")(
                  "en passant"
                )
              )
            )
          ),
          question(
            "threefold",
            trf.threefoldRepetition.txt(),
            p(
              trf.threefoldRepetitionExplanation(
                a(href := "https://en.wikipedia.org/wiki/Threefold_repetition")(
                  trf.threefoldRepetitionLowerCase()
                ),
                a(href := fideHandbookUrl)(trf.fideHandbook())
              )
            ),
            h4(trf.notRepeatedMoves()),
            p(
              trf.repeatedPositionsThatMatters(
                em(trf.positions())
              )
            ),
            h4(trf.weRepeatedthreeTimesPosButNoDraw()),
            p(
              trf.threeFoldHasToBeClaimed(
                a(href := routes.Pref.form("game-behavior"))(trf.configure())
              )
            )
          ),
          h2(trf.accounts()),
          question(
            "titles",
            trf.titlesAvailableOnLichess.txt(),
            p(
              trf.lichessRecognizeAllOTBtitles(
                a(href := "https://github.com/lichess-org/lila/wiki/Handling-title-verification-requests")(
                  trf.asWellAsManyNMtitles()
                )
              )
            ),
            ul(
              li("Grandmaster (GM)"),
              li("International Master (IM)"),
              li("FIDE Master (FM)"),
              li("Candidate Master (CM)"),
              li("Woman Grandmaster (WGM)"),
              li("Woman International Master (WIM)"),
              li("Woman FIDE Master (WFM)"),
              li("Woman Candidate Master (WCM)")
            ),
            p(
              trf.showYourTitle(
                a(href := routes.TitleVerify.index)(trf.verificationForm()),
                a(href := "#lm")("WayChess Master (LM)")
              )
            )
          ),
          question(
            "lm",
            trf.canIbecomeLM.txt(),
            p(strong(trf.noUpperCaseDot())),
            p(trf.lMtitleComesToYouDoNotRequestIt())
          ),
          question(
            "usernames",
            trf.whatUsernameCanIchoose.txt(),
            p(
              trf.usernamesNotOffensive(
                a(href := cmsPageUrl("username-policy"))(trf.guidelines())
              )
            )
          ),
          question(
            "change-username",
            trf.canIChangeMyUsername.txt(),
            p(trf.usernamesCannotBeChanged.txt())
          ),
          question(
            "trophies",
            trf.uniqueTrophies.txt(),
            h4("The Golden Zee"),
            p(
              trf.ownerUniqueTrophies(
                a(href := "https://lichess.org/@/ZugAddict")("ZugAddict")
              )
            ),
            p(
              trf.goldenZeeExplanation()
            )
          ),
          h2(trf.lichessRatings()),
          question(
            "ratings",
            trf.whichRatingSystemUsedByLichess.txt(),
            p(
              trf.ratingSystemUsedByLichess()
            ),
            p(
              a(href := cmsPageUrl("rating-systems"))("More about rating systems")
            )
          ),
          question(
            "provisional",
            trf.whatIsProvisionalRating.txt(),
            p(trf.provisionalRatingExplanation()),
            ul(
              li(
                trf.notPlayedEnoughRatedGamesAgainstX(
                  em(trf.similarOpponents())
                )
              ),
              li(
                trf.notPlayedRecently()
              )
            ),
            p(
              trf.ratingDeviationMorethanOneHundredTen()
            )
          ),
          question(
            "leaderboards",
            trf.howDoLeaderoardsWork.txt(),
            p(
              trf.inOrderToAppearsYouMust(
                a(href := routes.User.list)(trf.ratingLeaderboards())
              )
            ),
            ol(
              li(trf.havePlayedMoreThanThirtyGamesInThatRating()),
              li(trf.havePlayedARatedGameAtLeastOneWeekAgo()),
              li(
                trf.ratingDeviationLowerThanXinChessYinVariants(
                  standardRankableDeviation,
                  variantRankableDeviation
                )
              ),
              li(trf.beInTopTen())
            ),
            p(
              trf.secondRequirementToStopOldPlayersTrustingLeaderboards()
            )
          ),
          question(
            "high-ratings",
            trf.whyAreRatingHigher.txt(),
            p(
              trf.whyAreRatingHigherExplanation()
            ),
            p(
              a(href := cmsPageUrl("rating-systems"))("More about rating systems")
            )
          ),
          question(
            "hide-ratings",
            trf.howToHideRatingWhilePlaying.txt(),
            p(
              trf.enableZenMode(
                a(href := routes.Pref.form("display"))(trf.displayPreferences()),
                em("z")
              )
            )
          ),
          question(
            "disconnection-loss",
            trf.connexionLostCanIGetMyRatingBack.txt(),
            p(
              trf.weCannotDoThatEvenIfItIsServerSideButThatsRare()
            )
          ),
          h2(trf.howToThreeDots()),
          question(
            "browser-notifications",
            trf.enableDisableNotificationPopUps.txt(),
            p(
              img(
                src := assetUrl("images/connection-info.png"),
                alt := trf.viewSiteInformationPopUp.txt()
              )
            ),
            p(
              trf.lichessCanOptionnalySendPopUps()
            )
          ),
          question(
            "autoplay",
            trf.enableAutoplayForSoundsQ.txt(),
            p(trf.mostBrowsersPreventSoundAutoplay()),
            h3("Mozilla Firefox (", trf.desktop(), ")"),
            p(trf.enableAutoplayForSoundsFirefox()),
            h3("Google Chrome (", trf.desktop(), ")"),
            p(trf.enableAutoplayForSoundsChromeSiteInformation()),
            h3("Safari (", trf.desktop(), ")"),
            p(trf.enableAutoplayForSoundsSafari()),
            h3("Microsoft Edge (", trf.desktop(), ")"),
            p(trf.enableAutoplayForSoundsMicrosoftEdge())
          ),
          question(
            "make-a-bot",
            trf.makeBotTitle.txt(),
            p(
              trf.makeBotDescription.rawHtml(
                "https://lichess.org/blog/WvDNticAAMu_mHKP/welcome-lichess-bots",
                "https://lichess.org/@/thibault/blog/how-to-create-a-lichess-bot/FuKyvDuB"
              )
            )
          ),
          question(
            "stop-chess-addiction",
            trf.stopMyselfFromPlaying.txt(),
            p(
              trf.adviceOnMitigatingAddiction(
                a(href := "https://getcoldturkey.com")("ColdTurkey"),
                a(href := "https://freedom.to")("Freedom"),
                a(href := "https://www.proginosko.com/leechblock")("LeechBlock"),
                a(href := cmsPageUrl("userstyles"))(trf.lichessUserstyles()),
                a(href := "https://github.com/ornicar/userstyles/blob/master/lichess.fewer-pools.user.css")(
                  trf.fewerLobbyPools()
                ),
                a(href := "https://icd.who.int/browse/2024-01/mms/en#1448597234")(trf.mentalHealthCondition())
              )
            )
          )
        )
