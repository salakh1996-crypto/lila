# WayChess

WayChess — шахматный сайт для игроков из России, работающий без VPN.
Основан на открытом коде [Lichess (lila)](https://github.com/lichess-org/lila)
и распространяется под той же лицензией **GNU AGPL v3**: исходный код WayChess,
включая все изменения, должен быть открыт для пользователей сайта.

WayChess не связан с Lichess. Название, логотип и домен Lichess не используются.

## Что изменено относительно Lichess

- **Название.** «Lichess» заменено на «WayChess» в переводах интерфейса
  (все языки, `translation/`), в шаблонах страниц (Scala) и во фронтенде (TypeScript).
  Внутренние идентификаторы (ключи переводов, имена классов, системный
  пользователь `lichess`) не менялись, чтобы не ломать код.
- **Логотип.** Логотипы Lichess (их лицензия разрешает использовать их только
  для ссылки на lichess.org) удалены. Новый знак WayChess — `public/logo/waychess*.svg|png`,
  `public/favicon.ico`, `public/apple-touch-icon.png`. В шапке сайта и на странице
  OAuth знак рисуется классом `.waychess-mark`.
- **Несвободные наборы фигур удалены:** alpha, chess7, companion, leipzig,
  reillycraig, riohacha, shahi-ivory-brown и 3D-набор Staunton.
- **Звуки.** Несвободные наборы `standard`, `other`, `instrument` (тема «Pentatonic»),
  `robot`, `woodland` удалены. Стандартный набор — `sfx` (AGPL). Звуки обучения,
  практики и «yeet» взяты из `sfx`. Прослушать все наборы: страница `/assets/oops/sounds.html`.
- **Исходный код.** Ссылки «Исходный код» на сайте ведут на https://github.com/salakh1996-crypto/lila (требование AGPL).
- **Конфигурация.** Имя сайта по умолчанию — `WayChess`, основной домен — `waychess.ru`
  (`conf/base.conf`). Шаблон боевой конфигурации — `conf/waychess.conf.example`,
  конфигурация веб-сервера — `deploy/Caddyfile`.

Замена названия сделана скриптами из `bin/waychess/`. Их можно запускать повторно
после слияния обновлений из Lichess: `bin/waychess/rebrand.sh` (см. «Обновления из Lichess»).

## Что нужно сделать до запуска

Обязательно:

1. **Тексты о Lichess.** FAQ (`modules/web/src/main/ui/FaqUi.scala`), страницы
   «О сайте», «Пожертвования», «Расходы», «Магазин» рассказывают о Lichess.
   Страницы вроде «О сайте» хранятся в базе (раздел CMS, `/cms`) и редактируются
   администратором.
2. **Ссылки на соцсети и приложение Lichess** — `modules/web/src/main/ui/bits.scala`,
   `modules/web/src/main/StaticContent.scala` (manifest, Play/App Store).
3. **Пожертвования.** Stripe и PayPal не работают с российскими счетами. Нужно
   подключить ЮKassa/CloudPayments или скрыть кнопку «Поддержать» в шапке
   (`modules/web/src/main/ui/layout.scala`, `site-title-nav__donate`).
4. **Юридическое.** ИП или ООО; политика конфиденциальности и пользовательское
   соглашение; хранение персональных данных на серверах в РФ (152-ФЗ);
   консультация юриста по реестру организаторов распространения информации (ОРИ)
   из-за чата и личных сообщений.
5. **Картинки.** В `public/images` есть изображения без свободной лицензии
   («The other artwork in public/images» в `COPYING.md`): иллюстрации, фоны,
   страница техработ. Нужно проверить каждую и заменить своими.

Желательно:

- Наборы фигур под лицензией CC BY-NC-SA (california, caliente, maestro, fresca,
  cardinal, icpieces, gioco, tatiana, staunty, dubrovny, anarcandy, disguised,
  cooke, monarchy, horsey, xkcd, minimal-warmth) разрешены только для
  некоммерческого использования. То же касается набора звуков `lisp`, который
  используется в задачах и Puzzle Storm. Если на сайте будет реклама или платные функции,
  их нужно убрать. Для наборов kosal и governor лицензию нужно уточнить.
- Значок `Icon.Logo` в иконочном шрифте (`public/font/lichess.woff2`) всё ещё
  рисует коня Lichess в нескольких меню.
- Отдельный логотип от дизайнера: текущий знак — рабочий вариант.

## Запуск для разработки

Проще всего через [lila-docker](https://github.com/lichess-org/lila-docker):

```bash
git clone https://github.com/lichess-org/lila-docker
cd lila-docker
git clone <репозиторий WayChess> repos/lila   # до первого запуска
./lila-docker start                          # выбрать Advanced
```

Сайт откроется на http://localhost:8080. Для сборки нужно около 12 ГБ памяти.

## Запуск на сервере

Рекомендуемый сервер для старта: 8 vCPU, 32 ГБ RAM, 200 ГБ NVMe, Ubuntu 24.04,
у российского провайдера (Selectel, Yandex Cloud, VK Cloud, Timeweb Cloud).

Компоненты:

| Компонент | Назначение | Порт |
| --- | --- | --- |
| lila | основной сайт (этот репозиторий) | 9663 |
| [lila-ws](https://github.com/lichess-org/lila-ws) | WebSocket: партии в реальном времени | 9664 |
| MongoDB 7 (replica set) | база данных | 27017 |
| Redis | связь lila и lila-ws | 6379 |
| Caddy | HTTPS и проксирование | 80, 443 |
| [fishnet](https://github.com/lichess-org/fishnet) | анализ партий Stockfish (по желанию) | — |

Шаги:

1. Установить JDK 21, sbt, Node.js 24.20+ и pnpm, Docker, Caddy.
2. Запустить MongoDB и Redis (например, в Docker) и инициализировать replica set.
   Создать индексы: `mongosh lichess bin/mongodb/indexes.js`.
3. Собрать фронтенд: `./ui/build -p`.
4. Скопировать `conf/waychess.conf.example` в `/etc/waychess/application.conf`
   и заполнить секреты.
5. Собрать и запустить lila:
   `sbt stage`, затем
   `target/universal/stage/bin/lila -Dconfig.file=/etc/waychess/application.conf`
   (лучше как сервис systemd).
6. Собрать и запустить lila-ws так же (`sbt stage`), указав `csrf.origin = "https://waychess.ru"`
   и адреса MongoDB и Redis.
7. Положить `deploy/Caddyfile` в `/etc/caddy/Caddyfile`, направить DNS домена
   на сервер и перезапустить Caddy: сертификат HTTPS выпустится сам.
8. Создать администратора и проверить регистрацию, игру, чат и почту.

## Обновления из Lichess

```bash
git remote add upstream https://github.com/lichess-org/lila.git
git fetch upstream
git merge upstream/master
```

После слияния снова прогнать замену названия (`bin/waychess/rebrand.sh`)
и проверить, не появились ли новые упоминания Lichess:
`grep -rn "Lichess" translation/source app modules ui --include=*.scala --include=*.ts --include=*.xml`.
