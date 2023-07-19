# NullTracker
# Projektová dokumentace (Obsolote)
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
- Použitá DB -> **MongoDB** (Použítý Java Sync Driver - verze 4.8.x)
- Bližší info viz. Wiki TBD
- Abstrakce SQL příkazů probíhá incode pomocí metod a objektů natáhlých z driveru viz. https://l.facebook.com/l.php?u=https%3A%2F%2Fwww.mongodb.com%2Fdocs%2Fmanual%2Ftutorial%2F%3Ffbclid%3DIwAR01GRkaEJ33j_SvbXWfuDn1l-lY_krS10NlMfpacTAn3dKYZFFErs1z6jo&h=AT255vnaVT_eZMDGhoVnxTN86hY4_vsbG_-VbyirtPniNmGQYk3kPpkGswF_cK3sabOQRl8OaHyPgJwlMi6tJaDz67Ub4-QGTpoPl3Xgko7m_CGJgdyMZ27Jxsm39bvgKiPiKZoyGJWErb21QWw
- Práce s Mongo commandy mimo Try-Catch blok se nedoporučuje, IDEA (Ultimate) bude tohle vracet jako warning
- Data lze accessnout remote v MongoDB Atlas webadminu, práce přes code vyžaduje otevření konexe pomocí ConnectionURI, která je předefinovaná na *martin_dev* usera a je open na všechny IP
- Kapacita uložiště je 512 MB



