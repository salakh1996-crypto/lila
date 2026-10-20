"""Replace the Lichess brand with WayChess in translation XML text nodes.

Keys (name="...") are left untouched. Site URLs (https://lichess.org/...)
are pointed at our own domain, since they refer to routes WayChess serves too.
"""
import re, sys, pathlib

BRAND = "WayChess"
DOMAIN = sys.argv[2] if len(sys.argv) > 2 else "waychess.ru"

text_node = re.compile(r'(<(?:string|item)\b[^>]*>)(.*?)(</(?:string|item)>)', re.S)
bare_domain = re.compile(r'(?<![/\w.@-])lichess\.org(?![\w])')
word = re.compile(r'(?<![/\w.@-])[Ll]ichess(?![\w-])(?!\.org)')

def fix(text):
    text = re.sub(r'(https?:)?//lichess\.org', lambda m: 'https://' + DOMAIN, text)
    text = bare_domain.sub(DOMAIN, text)
    text = word.sub(BRAND, text)
    return text

changed = 0
for p in pathlib.Path(sys.argv[1]).rglob('*.xml'):
    s = p.read_text(encoding='utf-8')
    n = text_node.sub(lambda m: m.group(1) + fix(m.group(2)) + m.group(3), s)
    if n != s:
        p.write_text(n, encoding='utf-8'); changed += 1
print("files changed:", changed)
