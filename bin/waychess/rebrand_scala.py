"""Rebrand user-visible 'Lichess' mentions inside Scala string literals.

Only literals that read as prose (contain a space) are touched, and
functional identifiers are skipped: translation keys (ns:key), the mobile
app User-Agent prefix, and the blog topic key stored in the database.
"""
import re, sys, pathlib

literal = re.compile(r'"(?:[^"\\\n]|\\.)*"')
word = re.compile(r'(?<![\w/.@:-])Lichess(?![\w-])(?! Mobile/)')
SKIP_FILES = {'UblogTopic.scala', 'LameName.scala'}

def fix_literal(m):
    lit = m.group(0)
    if ' ' not in lit or 'Lichess Mobile/' in lit:
        return lit
    return word.sub('WayChess', lit)

for root in sys.argv[1:]:
    for p in pathlib.Path(root).rglob('*.scala'):
        if p.name in SKIP_FILES or '/test/' in str(p):
            continue
        s = p.read_text(encoding='utf-8')
        n = literal.sub(fix_literal, s)
        if n != s:
            p.write_text(n, encoding='utf-8')
            print(p)
