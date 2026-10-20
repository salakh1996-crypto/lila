// Renders WayChess raster logos from the SVG mark with headless Chromium.
// Usage: node bin/waychess/render-logos.mjs public   (needs playwright-core)
import { chromium } from 'playwright-core';
import { readFileSync } from 'node:fs';

const out = process.argv[2];
const BRAND = '#1f7a4d';
const mark = c => readFileSync(new URL('../../public/logo/waychess.svg', import.meta.url), 'utf8').replaceAll('#000', c);
const font = "font-family:'Noto Sans','DejaVu Sans',sans-serif;font-weight:700";

const icon = (size, rounded) =>
  `<div style="width:${size}px;height:${size}px;background:${BRAND};border-radius:${rounded ? size * 0.2 : 0}px;display:flex;align-items:center;justify-content:center">
     <div style="width:${size * 0.72}px;height:${size * 0.72}px">${mark('#fff')}</div></div>`;
const bare = (size, c) => `<div style="width:${size}px;height:${size}px">${mark(c)}</div>`;
const withName = (w, h, c, bg) =>
  `<div style="width:${w}px;height:${h}px;background:${bg};display:flex;align-items:center;justify-content:center;gap:${h * 0.08}px">
     <div style="width:${h * 0.62}px;height:${h * 0.62}px">${mark(c)}</div>
     <span style="${font};color:${c};font-size:${h * 0.42}px;letter-spacing:-0.01em">WayChess</span></div>`;

const jobs = [
  ...[32, 64, 128, 192, 256, 512, 1024].map(s => [`logo/waychess-favicon-${s}.png`, s, s, icon(s, true)]),
  ['logo/waychess-mono-128.png', 128, 128, bare(128, '#fff')],
  ['apple-touch-icon.png', 180, 180, icon(180, false)],
  ['logo/waychess-tile-wide.png', 460, 340,
    `<div style="width:460px;height:340px;background:${BRAND};display:flex;flex-direction:column;align-items:center;justify-content:center;gap:18px">
       <div style="width:150px;height:150px">${mark('#fff')}</div>
       <span style="${font};color:#fff;font-size:58px">WayChess</span></div>`],
  ['logo/waychess-with-name-dark.png', 2000, 412, withName(2000, 412, '#fff', 'transparent')],
  ['logo/waychess-with-name-light.png', 2000, 412, withName(2000, 412, '#111', 'transparent')],
];

const browser = await chromium.launch();
const page = await browser.newPage();
for (const [file, w, h, html] of jobs) {
  await page.setViewportSize({ width: w, height: h });
  await page.setContent(`<html><body style="margin:0;background:transparent">${html}</body></html>`);
  await page.screenshot({ path: `${out}/${file}`, omitBackground: true, clip: { x: 0, y: 0, width: w, height: h } });
}
await browser.close();
