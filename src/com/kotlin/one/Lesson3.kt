package com.android.com.kotlin.one

enum class CharacterType {
    WARRIOR, MAGUS, COLOSSUS, DWARF
}

data class Weapon(val name: String, val power: Int)

interface Attacker {
    fun attack(target: Character): ActionResult
}

interface Healer {
    fun heal(target: Character): ActionResult
}

data class ActionResult(
    val actorName: String,
    val action: String,
    val targetName: String,
    val value: Int,
    val targetHpAfter: Int,
    val targetDied: Boolean = false
)

abstract class Character(
    val name: String,
    val weapon: Weapon,
    private val maxHp: Int
) {
    private var hp: Int = maxHp

    abstract val type: CharacterType

    fun currentHp(): Int = hp
    fun maxHp(): Int = maxHp

    fun isAlive(): Boolean = hp > 0

    fun receiveDamage(amount: Int): Boolean {
        if (!isAlive()) return true
        hp = (hp - amount).coerceAtLeast(0)
        return hp == 0
    }

    fun receiveHeal(amount: Int) {
        if (!isAlive()) return
        hp = (hp + amount).coerceAtMost(maxHp)
    }

    override fun toString(): String {
        val status = if (isAlive()) "ALIVE" else "DEAD"
        return "$name | $type | HP: $hp/$maxHp | Weapon: ${weapon.name}(${weapon.power}) | $status"
    }
}

class Warrior(name: String) : Character(
    name = name,
    weapon = Weapon("Sword", power = 25),
    maxHp = 120
), Attacker {
    override val type: CharacterType = CharacterType.WARRIOR

    override fun attack(target: Character): ActionResult {
        val died = target.receiveDamage(weapon.power)
        return ActionResult(
            actorName = name,
            action = "attacks",
            targetName = target.name,
            value = weapon.power,
            targetHpAfter = target.currentHp(),
            targetDied = died
        )
    }
}

class Magus(name: String) : Character(
    name = name,
    weapon = Weapon("Staff", power = 12),
    maxHp = 150
), Attacker, Healer {
    override val type: CharacterType = CharacterType.MAGUS

    override fun attack(target: Character): ActionResult {
        val died = target.receiveDamage(weapon.power)
        return ActionResult(
            actorName = name,
            action = "casts a spell on",
            targetName = target.name,
            value = weapon.power,
            targetHpAfter = target.currentHp(),
            targetDied = died
        )
    }

    override fun heal(target: Character): ActionResult {
        val healAmount = 30
        target.receiveHeal(healAmount)
        return ActionResult(
            actorName = name,
            action = "heals",
            targetName = target.name,
            value = healAmount,
            targetHpAfter = target.currentHp(),
            targetDied = false
        )
    }
}

class Colossus(name: String) : Character(
    name = name,
    weapon = Weapon("Hammer", power = 20),
    maxHp = 200
), Attacker {
    override val type: CharacterType = CharacterType.COLOSSUS

    override fun attack(target: Character): ActionResult {
        val died = target.receiveDamage(weapon.power)
        return ActionResult(
            actorName = name,
            action = "smashes",
            targetName = target.name,
            value = weapon.power,
            targetHpAfter = target.currentHp(),
            targetDied = died
        )
    }
}

class Dwarf(name: String) : Character(
    name = name,
    weapon = Weapon("Axe", power = 40),
    maxHp = 90
), Attacker {
    override val type: CharacterType = CharacterType.DWARF

    override fun attack(target: Character): ActionResult {
        val died = target.receiveDamage(weapon.power)
        return ActionResult(
            actorName = name,
            action = "strikes",
            targetName = target.name,
            value = weapon.power,
            targetHpAfter = target.currentHp(),
            targetDied = died
        )
    }
}

class Player(val name: String) {
    val team: MutableList<Character> = mutableListOf()

    fun aliveCharacters(): List<Character> = team.filter { it.isAlive() }
    fun isDefeated(): Boolean = aliveCharacters().isEmpty()
}

class Game {
    private val globalNames: MutableSet<String> = mutableSetOf()
    private var rounds: Int = 0

    fun start() {
        println(" Battle Arena (Console Prototype)")
        println("===================================")
        println("Rules: each player creates 3 characters with unique names and unique types in the team.\n")

        val p1 = createPlayer(1)
        val p2 = createPlayer(2)

        println("\nTeams created! Let the battle begin!\n")
        printTeams(p1, p2)

        fight(p1, p2)

        endGame(p1, p2)
    }

    private fun createPlayer(index: Int): Player {
        val playerName = readNonEmpty("Enter Player $index name: ")
        val player = Player(playerName)

        val usedTypes = mutableSetOf<CharacterType>()

        println("\n$playerName, create your team (3 characters).")
        println("Available types: WARRIOR, MAGUS, COLOSSUS, DWARF\n")

        while (player.team.size < 3) {
            val type = readType("Choose a type for character ${player.team.size + 1}: ", usedTypes)
            val name = readUniqueName("Choose a UNIQUE name for this $type: ")

            val character = createCharacter(type, name)
            player.team.add(character)
            usedTypes.add(type)

            println(" Added: $character\n")
        }

        return player
    }

    private fun createCharacter(type: CharacterType, name: String): Character {
        return when (type) {
            CharacterType.WARRIOR -> Warrior(name)
            CharacterType.MAGUS -> Magus(name)
            CharacterType.COLOSSUS -> Colossus(name)
            CharacterType.DWARF -> Dwarf(name)
        }
    }

    private fun fight(p1: Player, p2: Player) {
        var current = p1
        var opponent = p2

        while (!p1.isDefeated() && !p2.isDefeated()) {
            rounds++
            println("\n====================")
            println(" ROUND $rounds — ${current.name}'s turn")
            println("====================")

            val actor = chooseCharacter(current, "Choose your character:")
            if (!actor.isAlive()) {
                println(" That character is dead. Choose another.")
                continue
            }

            val canHeal = actor is Healer
            val action = chooseAction(canHeal)

            val result = when (action) {
                "1" -> {
                    val target = chooseCharacter(opponent, "Choose an enemy to attack:")
                    (actor as Attacker).attack(target)
                }
                "2" -> {
                    val ally = chooseCharacter(current, "Choose an ally to heal:")
                    (actor as Healer).heal(ally)
                }
                else -> error("Unexpected action")
            }

            printResult(result)


            println("\n Team status:")
            println("— ${p1.name}")
            p1.team.forEach { println("  $it") }
            println("— ${p2.name}")
            p2.team.forEach { println("  $it") }

            val tmp = current
            current = opponent
            opponent = tmp
        }
    }

    private fun endGame(p1: Player, p2: Player) {
        println("\n===================================")
        println("GAME OVER")
        println("===================================")

        val winner = if (p1.isDefeated()) p2 else p1
        println(" Winner: ${winner.name}")
        println(" Rounds played: $rounds\n")

        println(" Final characters status:")
        println("— ${p1.name}")
        p1.team.forEach { println("  $it") }
        println("— ${p2.name}")
        p2.team.forEach { println("  $it") }
    }

    private fun printTeams(p1: Player, p2: Player) {
        println("— ${p1.name} team:")
        p1.team.forEach { println("  $it") }
        println("\n— ${p2.name} team:")
        p2.team.forEach { println("  $it") }
        println()
    }

    private fun chooseAction(canHeal: Boolean): String {
        while (true) {
            println("\nChoose an action:")
            println("1) Attack enemy")
            if (canHeal) {
                println("2) Heal ally")
            }

            val choice = readln().trim()

            if (choice == "1") return choice
            if (canHeal && choice == "2") return choice

            if (canHeal) {
                println("Invalid choice. Try again.")
            } else {
                println("Invalid choice. Only Attack is available.")
            }
        }
    }

    private fun chooseCharacter(player: Player, prompt: String): Character {
        val alive = player.aliveCharacters()
        println("\n$prompt")
        alive.forEachIndexed { index, c ->
            println("${index + 1}) ${c.name} (${c.type}) HP: ${c.currentHp()}/${c.maxHp()} Weapon: ${c.weapon.name}(${c.weapon.power})")
        }

        while (true) {
            val input = readln().trim()
            val idx = input.toIntOrNull()
            if (idx != null && idx in 1..alive.size) return alive[idx - 1]
            println("Invalid selection. Choose a number between 1 and ${alive.size}.")
        }
    }

    private fun printResult(r: ActionResult) {
        when {
            r.action.contains("heals") -> {
                println("${r.actorName} ${r.action} ${r.targetName} for +${r.value} HP → ${r.targetName} HP: ${r.targetHpAfter}")
            }
            r.targetDied -> {
                println(" ${r.actorName} ${r.action} ${r.targetName} for ${r.value} damage → ${r.targetName} HP: ${r.targetHpAfter}")
                println(" ${r.targetName} has died!")
            }
            else -> {
                println("  ${r.actorName} ${r.action} ${r.targetName} for ${r.value} damage → ${r.targetName} HP: ${r.targetHpAfter}")
            }
        }
    }


    private fun readNonEmpty(prompt: String): String {
        while (true) {
            print(prompt)
            val input = readln().trim()
            if (input.isNotEmpty()) return input
            println(" Input cannot be empty.")
        }
    }

    private fun readUniqueName(prompt: String): String {
        while (true) {
            print(prompt)
            val name = readln().trim()
            if (name.isEmpty()) {
                println(" Name cannot be empty.")
                continue
            }
            if (globalNames.contains(name.lowercase())) {
                println(" Name already used in this game. Choose another.")
                continue
            }
            globalNames.add(name.lowercase())
            return name
        }
    }

    private fun readType(prompt: String, usedTypes: Set<CharacterType>): CharacterType {
        while (true) {
            print(prompt)
            val input = readln().trim().uppercase()
            val type = runCatching { CharacterType.valueOf(input) }.getOrNull()
            if (type == null) {
                println(" Invalid type. Use: WARRIOR, MAGUS, COLOSSUS, DWARF")
                continue
            }
            if (usedTypes.contains(type)) {
                println(" Type already used in your team. Choose another type.")
                continue
            }
            return type
        }
    }
}

fun main() {
    Game().start()
}