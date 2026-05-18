![wither_storm_mod_title_outline_large](https://github.com/nonamecrackers2/crackers-wither-storm-mod/assets/105086648/2b904b59-5cc8-4f6d-907c-ce48e344b8d0)

[![CurseForge Downloads](https://img.shields.io/curseforge/dt/621405?style=flat-square&logo=curseforge&label=CurseForge&color=orange&link=https%3A%2F%2Fwww.curseforge.com%2Fminecraft%2Fmc-mods%2Fcrackers-wither-storm-mod)](https://www.curseforge.com/minecraft/mc-mods/crackers-wither-storm-mod)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/kWjNGDUH?style=flat-square&logo=modrinth&label=Modrinth&link=https%3A%2F%2Fmodrinth.com%2Fmod%2Fcrackers-wither-storm-mod)](https://modrinth.com/mod/crackers-wither-storm-mod)
[![CurseForge Game Versions](https://img.shields.io/curseforge/game-versions/621405?style=flat-square&label=Latest%20version)](https://www.curseforge.com/minecraft/mc-mods/crackers-wither-storm-mod/files)
[![Support me on Patreon](https://img.shields.io/endpoint.svg?url=https%3A%2F%2Fshieldsio-patreon.vercel.app%2Fapi%3Fusername%3Dnonamecrackers2%26type%3Dpatrons&style=flat-square)](https://patreon.com/nonamecrackers2)
[![Discord](https://img.shields.io/discord/987817685293355028?style=flat-square&logo=discord&label=Discord&color=%235865F2)](https://discord.gg/cracker-s-modded-community-987817685293355028)
[![Snapshot Build](https://github.com/nonamecrackers2/crackers-wither-storm-mod/actions/workflows/snapshot_build.yml/badge.svg)](https://github.com/nonamecrackers2/crackers-wither-storm-mod/actions/workflows/snapshot_build.yml)
[![Release Build](https://github.com/nonamecrackers2/crackers-wither-storm-mod/actions/workflows/release_build.yml/badge.svg)](https://github.com/nonamecrackers2/crackers-wither-storm-mod/actions/workflows/release_build.yml)

# Welcome to the official repository for Cracker's Wither Storm Mod!

~~The mod is currently not open source~~ **The mod is now open source!** The source code is a decompilation of the original jar, fixed up and made buildable under the Forge MDK.

Translators can still feel free to use the lang files included in this repo to translate the mod to other languages. To submit a translation, make a pull request. To submit a feature, make an issue using the suggestion template. To make a bug report, make an issue using the bug report template.

---

## Technical Details

| | |
|---|---|
| **Minecraft** | 1.20.1 |
| **Mod Loader** | Forge 47.4.10 |
| **Java** | 17 (Eclipse Temurin recommended) |
| **Mappings** | Official (Mojang) |
| **Build System** | Gradle 8.8 (via wrapper) |

### Dependencies

| Dependency | Version | Required |
|---|---|---|
| [CrackersLib](https://modrinth.com/mod/crackerslib) | 1.20.1-0.4.1 | Required |
| [JEI](https://modrinth.com/mod/jei) | 15.20.0.106 | Optional |

---

## Building from Source

Make sure you have **JDK 17** installed and set as your default Java.

```bash
git clone https://github.com/nonamecrackers2/crackers-wither-storm-mod.git
cd crackers-wither-storm-mod
./gradlew build --no-daemon
```

On Windows:

```bat
gradlew.bat build --no-daemon
```

The built jar will be in `build/libs/`. The reobfuscated jar (the one you install in your mods folder) is the one **without** `-sources` or `-dev` in the filename.

### Cleaning the build

```bash
# Linux / macOS
rm -rf .gradle build run run-data out mcmodsrepo

# Windows
rd /s /q .gradle build run run-data out mcmodsrepo
```

---

## CI / GitHub Actions

Two workflows are included:

- **Snapshot Build** — runs on every push to any branch and on pull requests. Artifacts are retained for 14 days.
- **Release Build** — runs automatically on pushes to `main` and on `v*.*.*` tags. Can also be triggered manually. Artifacts are retained for 90 days.

Both workflows can be triggered manually from the **Actions** tab on GitHub.
