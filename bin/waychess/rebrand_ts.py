"""Rebrand user-visible 'Lichess' mentions inside TypeScript string literals.

Literals must contain a space to count as prose. References to third-party
tools named after Lichess (e.g. the 'Lichess Tools' extension) are kept.
"""
import re, sys, pathlib

literal = re.compile(r"'(?:[^'\\\n]|\\.)*'|\"(?:[^\"\\\n]|\\.)*\"|`(?:[^`\\]|\\.)*`")
word = re.compile(r'(?<![\w/.@:-])Lichess(?![\w-])(?! Tools)(?!\.org)')

def fix(m):
    lit = m.group(0)
    return word.sub('WayChess', lit) if ' ' in lit else lit

for root in sys.argv[1:]:
    for p in pathlib.Path(root).rglob('*.ts'):
        if 'node_modules' in p.parts or p.name.endswith('.d.ts') or '.test.' in p.name:
            continue
        s = p.read_text(encoding='utf-8')
        n = literal.sub(fix, s)
        if n != s:
            p.write_text(n, encoding='utf-8'); print(p)
