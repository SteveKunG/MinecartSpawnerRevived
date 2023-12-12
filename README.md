# MinecartSpawnerRevived
Reviving Minecart Spawner with care and bug fixes!

Support with Minecraft 1.20.x for Fabric (Quilt maybe?)~/Forge~ (Soon)

~Download on [Modrinth](https://modrinth.com/mod/minecart-spawner-revived)/[CurseForge](https://curseforge.com/minecraft/mc-mods/minecart-spawner-revived)~ (Soon)

### List of bug fixes
- When entity recreated from a packet, send a request SpawnData packet to the server
- Re-added mob renderer for the Spawner Minecart.
    - Resolves [MC-65065](https://bugs.mojang.com/browse/MC-65065).
- Fix Spawner Minecart spawner particles position. [MC-66894](https://bugs.mojang.com/browse/MC-66894).
- Fix Spawn Eggs cannot be used on Spawner Minecart to change entity to spawn. [MC-110427](https://bugs.mojang.com/browse/MC-110427).
- Add experience drop when Spawner Minecart is destroyed. Same as regular spawner block. No bug reported yet.

### Installation
Make sure to install on both client and server side for better result!
- Install Fabric Loader~/MinecraftForge~ (Soon)
- Copy mod into `mods` folder