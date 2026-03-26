# Figma AI Design Brief — Blade Toss

---

## 1. Art Style & Color Palette

**Style:** Dark, gritty wood-and-steel aesthetic with a carnival knife-throwing booth atmosphere. Think weathered oak, hand-forged steel blades, and dramatic spotlight lighting against deep night backgrounds. The art direction blends rustic craftsmanship with visceral impact — every blade throw should feel weighty and satisfying.

**Primary Palette:**
- `#1A0F0A` — Deep charcoal-brown (background base)
- `#3B1F0D` — Warm dark wood (panels, log surfaces)
- `#C8843A` — Aged oak amber (highlights, log grain)
- `#8BA8B5` — Cold steel blue-grey (blade bodies)

**Accent:**
- `#E8C84A` — Polished gold / apple yellow (bonus apples, stage clear glow)
- `#D93030` — Blood red (danger state, game over flash)

**Font Mood:** Bold, slightly weathered display typeface — strong serifs or condensed gothic. Should evoke carnival signage or antique woodcut printing. Pairing: heavy display title font + clean condensed sans for scores/stage numbers.

---

## 2. App Icon — icon_512.png (512×512px)

**Export path:** `icon_512.png` (root directory)

A square canvas filled with a deep radial gradient from `#2A1208` at center to `#0D0705` at corners, evoking a dramatic spotlight on dark wood. The central symbol is a single large throwing knife viewed in a 3/4 overhead perspective, its blade embedded diagonally into a circular cross-section of a polished log — visible wood grain rings rendered in warm amber tones. The blade catches a specular highlight along its steel edge in `#C8D8E0`. A second smaller blade is stuck at a different angle to communicate gameplay instantly. A soft orange glow (`#C8843A` at 40% opacity) radiates from beneath the log slice. The mood is sharp, immediate, and slightly dangerous — like a carnival game booth at midnight.

---

## 3. UI Screens (480×854 portrait)

---

### MainMenuScreen

**A) BACKGROUND IMAGE**

Deep night sky background with a radial vignette — `#0D0705` outer edges bleeding into `#1A0F0A` center. A large weathered wooden log cross-section dominates the center-left of the composition, rendered with visible growth rings in amber and brown tones, softly lit from above-right. Three throwing knives are already embedded in the log at dramatic angles, their steel blades catching cool highlight glints. Subtle sawdust particle effects drift downward. A decorative empty banner shape — worn parchment-style, horizontally centered near the top third — sits blank, ready for the game title text. Two decorative flanking torch sconces cast warm orange pools of light at left and right edges. Bottom quarter features a dark wooden stage-plank texture.

**B) BUTTON LAYOUT**
```
BLADE TOSS (title label)  | top-Y=80px  | x=centered          | size=360x72
PLAY                       | top-Y=420px | x=centered          | size=260x60
STAGE SELECT               | top-Y=500px | x=centered          | size=260x52
LEADERBOARD                | top-Y=570px | x=centered          | size=260x52
HOW TO PLAY                | top-Y=640px | x=centered          | size=260x52
SETTINGS                   | top-Y=760px | x=right@20px        | size=60x60
```

---

### StageSelectScreen

**A) BACKGROUND IMAGE**

Same deep wood-and-night atmosphere as the main menu, but the background features a large decorative cork board or weathered wooden wall filling the entire canvas, textured with dark oak planks running horizontally. Ten empty decorative card frames are arranged in a 2×5 grid pattern, each frame styled as a small rough-cut wooden placard with nail holes at corners — interiors completely blank, awaiting stage number rendering by code. The upper portion has a blank horizontal banner shape in aged parchment with decorative rope borders. Ambient candlelight glow washes from below in warm amber, casting long soft shadows from each plaque frame. Stage card area spans from approximately Y=160px to Y=780px.

**B) BUTTON LAYOUT**
```
SELECT STAGE (label)       | top-Y=60px  | x=centered          | size=300x56
STAGE 1                    | top-Y=180px | x=left@40px         | size=180x90
STAGE 2                    | top-Y=180px | x=right@40px        | size=180x90
STAGE 3                    | top-Y=290px | x=left@40px         | size=180x90
STAGE 4                    | top-Y=290px | x=right@40px        | size=180x90
STAGE 5                    | top-Y=400px | x=left@40px         | size=180x90
STAGE 6                    | top-Y=400px | x=right@40px        | size=180x90
STAGE 7                    | top-Y=510px | x=left@40px         | size=180x90
STAGE 8                    | top-Y=510px | x=right@40px        | size=180x90
STAGE 9                    | top-Y=620px | x=left@40px         | size=180x90
STAGE 10                   | top-Y=620px | x=right@40px        | size=180x90
BACK                       | top-Y=774px | x=left@20px         | size=120x44
```

---

### GameScreen

**A) BACKGROUND IMAGE**

Minimal, focused gameplay backdrop. The full canvas is a deep charcoal-to-black radial gradient (`#0D0705` → `#1A0F0A`), with a single intense warm spotlight cone descending from the top-center, casting a lit circle roughly 240px wide at vertical center Y=380px — this is where the rotating log will be rendered by code. The surrounding darkness has subtle wood-grain vertical wall texture at very low opacity (10–15%), suggesting a barn or carnival booth interior. A narrow wooden floor plank strip runs across the very bottom (Y=780px to Y=854px) in `#2A1208`. The lower-center area (Y=680px to Y=780px) is kept completely clear — this is where the blade launch point renders. No decorative clutter in the center gameplay zone. Small ambient dust motes float in the spotlight cone.

**B) BUTTON LAYOUT**
```
SCORE (label + value)      | top-Y=30px  | x=centered          | size=200x48
STAGE (label + number)     | top-Y=30px  | x=right@20px        | size=120x44
BLADES REMAINING (counter) | top-Y=80px  | x=centered          | size=180x36
PAUSE                      | top-Y=20px  | x=left@20px         | size=60x60
```

---

### StageClearScreen

**A) BACKGROUND IMAGE**

Celebratory overlay scene. Dark background retained but now flooded with bursting golden light rays emanating from the center — `#E8C84A` at core fading to transparent. The wooden log cross-section is depicted at center with all blades cleanly embedded, surrounded by a radiant glow halo. Stylized wood-chip and sawdust explosion particles spray outward in all directions in amber and gold tones. Two empty decorative banner shapes sit vertically stacked: a large wide banner frame at upper-center (Y≈240–320px) styled as weathered parchment with rope fringing, and a smaller narrow banner below it (Y≈360–410px). Confetti-like wood shavings drift downward throughout. Bottom half remains dark to ensure score text legibility.

**B) BUTTON LAYOUT**
```
STAGE CLEAR! (label)       | top-Y=180px | x=centered          | size=340x64
SCORE (label + value)      | top-Y=280px | x=centered          | size=280x52
BONUS (apple bonus value)  | top-Y=350px | x=centered          | size=240x44
NEXT STAGE                 | top-Y=500px | x=centered          | size=260x60
RETRY                      | top-Y=580px | x=centered          | size=260x52
MAIN MENU                  | top-Y=660px | x=centered          | size=260x52
```

---

### GameOverScreen

**A) BACKGROUND IMAGE**

Dramatic failure scene. The full canvas bleeds from near-black `#0D0705` at edges to a deep blood-red undertone `#2A0808` at center, lit by a harsh red-tinted spotlight. A close-up stylized depiction of two crossed throwing blades — the fatal collision — sits at center-screen as decorative art, rendered in cold steel tones with a crimson glow at the crossing point. Cracked wood splinter fragments radiate outward from the impact zone. A large empty tombstone-shaped panel frame sits centered at Y≈280–440px — blank inside, styled with rough stone texture and weathered edges, serving as the score display backdrop. Thin red fracture lines spread from center toward screen edges. The mood is punishing but stylish — death screen that makes the player want to immediately retry.

**B) BUTTON LAYOUT**
```
GAME OVER (label)          | top-Y=160px | x=centered          | size=320x72
SCORE (label + value)      | top-Y=310px | x=centered          | size=260x52
BEST (high score value)    | top-Y=375px | x=centered          | size=240x44
RETRY                      | top-Y=510px | x=centered          | size=260x60
STAGE SELECT               | top-Y=590px | x=centered          | size=260x52
MAIN MENU                  | top-Y=670px | x=centered          | size=260x52
```

---

### LeaderboardScreen

**A) BACKGROUND IMAGE**

Trophy hall aesthetic. Dark walnut-paneled background with subtle vertical wood grain throughout. A large decorative empty scroll / leaderboard frame dominates the center of the canvas from Y=150px to Y=700px — styled as aged parchment or leather-bound ledger with ornate corner hardware in brass/gold tones, interior completely blank. The scroll has five faint horizontal ruling lines suggesting rows but containing no text. At the very top, two empty flanking banner shapes hold positions for rank badge decorations. Warm brass candlelight glows softly from lower-left and lower-right corners, casting orange-gold warmth across the scroll surface. Top area (Y=40–130px) has a decorative carved wooden title plaque frame, blank interior, with rope-and-nail border styling.

**B) BUTTON LAYOUT**
```
LEADERBOARD (label)        | top-Y=55px  | x=centered          | size=300x56
RANK 1 row                 | top-Y=200px | x=centered          | size=400x56
RANK 2 row                 | top-Y=270px | x=centered          | size=400x56
RANK 3 row                 | top-Y=340px | x=centered          | size=400x56
RANK 4 row                 | top-Y=410px | x=centered          | size=400x56
RANK 5 row                 | top-Y=480px | x=centered          | size=400x56
RANK 6 row                 | top-Y=550px | x=centered          | size=400x56
RANK 7 row                 | top-Y=620px | x=centered          | size=400x56
BACK                       | top-Y=774px | x=left@20px         | size=120x44
SHARE                      | top-Y=774px | x=right@20px        | size=120x44
```

---

### SettingsScreen

**A) BACKGROUND IMAGE**

Workshop bench aesthetic. The background depicts a dark craftsman's workbench surface in deep brown `#1A0F0A` with worn tool marks and subtle grain. The upper 40% of the canvas shows a pegboard-style wall with empty circular and rectangular mounting hooks — purely decorative with no icons. A large empty rectangular panel frame sits centered at Y=200–640px, styled as a dark iron-banded wooden case with riveted corner plates — interior completely blank, serving as the settings options backdrop. Ambient warm workshop lamp glow comes from above-center, casting a tight warm pool. The overall mood is mechanical and purposeful — a place where blades are maintained.

**B) BUTTON LAYOUT**
```
SETTINGS (label)           | top-Y=80px  | x=centered          | size=280x56
SOUND ON/OFF (toggle)      | top-Y=250px | x=centered          | size=320x56
MUSIC ON/OFF (toggle)      | top-Y=330px | x=centered          | size=320x56
VIBRATION ON/OFF (toggle)  | top-Y=410px | x=centered          | size=320x56
DIFFICULTY (selector)      | top-Y=490px | x=centered          | size=320x56
BACK                       | top-Y=774px | x=left@20px         | size=120x44
```

---

### HowToPlayScreen

**A) BACKGROUND IMAGE**

Instructional scroll aesthetic. Full canvas background in deep dark wood `#1A0F0A` with parchment-warm center light. A very large vertically-oriented open scroll dominates the canvas from Y=100px to Y=730px — styled as aged tan parchment (`#D4A96A` base tone) with rolled ends top and bottom, shadow-casting slight curl. The scroll interior is fully blank — no text, no illustrations, just the warm parchment texture. Decorative weathered rope ties the scroll at the rolled ends. At the top-left and top-right of the scroll, small decorative throwing knife silhouette shapes are embossed into the parchment edges as corner ornaments. The background visible outside the scroll edges shows the same dark wood wall as other screens. Soft warm candlelight from below-center warms the parchment bottom half.

**B) BUTTON LAYOUT**
```
HOW TO PLAY (label)        | top-Y=55px  | x=centered          | size=300x56
INSTRUCTION LINE 1         | top-Y=180px | x=centered          | size=400x40
INSTRUCTION LINE 2         | top-Y=240px | x=centered          | size=400x40
INSTRUCTION LINE 3         | top-Y=300px | x=centered          | size=400x40
INSTRUCTION LINE 4         | top-Y=360px | x=centered          | size=400x40
INSTRUCTION LINE 5         | top-Y=420px | x=centered          | size=400x40
INSTRUCTION LINE 6         | top-Y=480px | x=centered          | size=400x40
INSTRUCTION LINE 7         | top-Y=540px | x=centered          | size=400x40
BACK                       | top-Y=774px | x=left@20px         | size=120x44
```

---

## 4. Export Checklist

```
- icon_512.png (512x512)
- ui/screen_mainmenu.png (480x854)
- ui/screen_stageselect.png (480x854)
- ui/screen_game.png (480x854)
- ui/screen_stageclear.png (480x854)
- ui/screen_gameover.png (480x854)
- ui/screen_leaderboard.png (480x854)
- ui/screen_settings.png (480x854)
- ui/screen_howtoplay.png (480x854)
- feature_banner.png (1024x500)
```

---

## 5. Feature Banner — feature_banner.png (1024×500)

A dramatic wide-format landscape composition lit by a single intense overhead spotlight cutting through deep darkness. The right two-thirds of the banner shows a large polished log cross-section mounted on an unseen rotating mechanism, dramatically lit in warm amber — five throwing blades already embedded at dynamic angles, steel surfaces catching cold specular highlights against the warm wood. A sixth blade is caught mid-flight, frozen just before impact, trailing a faint motion blur arc from right to left. A lone red apple glows on the log surface near the top, radiating soft golden light. The left third features the title **"BLADE TOSS"** rendered in large bold condensed gothic lettering, each letter slightly distressed and wood-stamped in feel, colored in polished gold `#E8C84A` with a hard dark drop shadow. Beneath the title, a secondary line reads **"Can you stick them all?"** in smaller steel-grey italic. The background is near-black `#0D0705` with a deep red vignette glow `#2A0808` behind the log, and faint sawdust particle haze throughout the atmosphere. Color palette strictly matches Section 1 — no white bars, no device frames, full-bleed edge to edge.