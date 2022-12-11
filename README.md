# NullTracker
# Projektová dokumentace
https://gitlab.com/FIS-VSE/4IT115/2022ZS/st1100/kadm09/nulltracker/-/wikis/Project-overview

# Projektová metodika
Variace na Agile řízení (inkrementální vývoj s postupným vyhodnocováním.)
- Pro kanban implementaci se využije GitLab Board.
- Stand up a vyhodnocení sprintu - TBD

# Jak pracovat s VCS
## Protected branches na projektu
**MAIN** - Protected zamčená větev, nelze direct pushnout. Merge request
musí schválit alespoň dva vývojáři na projektu (viz. projektová doukemnatce).
Reprezentuje PRODUKČNÍ stav kódu.  

**DEVELOP** - Protected větev, nelze direct push. 
Reprezentuje aktuální vývojový stavu projektu. VYCHÁZÍ Z NÍ VEŠKERÉ
NOVÉ FEATURES. (viz. pravidla branchování)

## Pravidla branchování
- Nový vývoj probíhá branchováním z větve DEVELOP.
- branchnuté větve budou prefixnuté jako FEATURE - *popis featury*
- **Nová feature začíná z čistého developu - NIKDY NE Z AKTIVNÍ FEATURE VĚTVE** (git pull, git checkout)
- Commity na feature větvi jsou prefixnuté buďto GitLab Issue, nebo doménou, které se vývoj týká (NOISSUE_FE,NOISSUE_BE)
- Feature větve se zavírají mergem do DEVELOP, potřeba schválení alespoň dvou vývojářů.
- V předem schválených iteracích se bude postupně mergovat do MAINu s patřičným SEMVER inkrementem.

side note: Momentálně je povolen direct push do DEVu, zamknu ho až budou mít všichni 
lokální kopie repa, POTÉ POUZE MERGE

## Práce s DBS
TBD

