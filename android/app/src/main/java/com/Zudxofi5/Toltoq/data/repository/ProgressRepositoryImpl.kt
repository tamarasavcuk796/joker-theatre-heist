package com.Zudxofi5.Toltoq.data.repository

import com.Zudxofi5.Toltoq.core.config.GameConfig
import com.Zudxofi5.Toltoq.data.local.PreferencesStorage
import com.Zudxofi5.Toltoq.data.sample.SampleData
import com.Zudxofi5.Toltoq.domain.model.ActProgress
import com.Zudxofi5.Toltoq.domain.model.CareerProgress
import com.Zudxofi5.Toltoq.domain.model.CostumeProgress
import com.Zudxofi5.Toltoq.domain.model.LevelProgress
import com.Zudxofi5.Toltoq.domain.model.RunOutcome
import com.Zudxofi5.Toltoq.domain.model.RunResult
import com.Zudxofi5.Toltoq.domain.repository.ProgressRepository

class ProgressRepositoryImpl(private val storage: PreferencesStorage) : ProgressRepository {

    override fun career(): CareerProgress {
        val levels = SampleData.levels
        var score = 0
        var stars = 0
        var cleared = 0
        for (level in levels) {
            score += storage.readInt(bestKey(level.id))
            val earned = storage.readInt(starKey(level.id))
            stars += earned
            if (earned > 0) cleared += 1
        }
        return CareerProgress(
            totalScore = score,
            stars = stars,
            maxStars = levels.size * 3,
            levelsCleared = cleared,
            totalLevels = levels.size,
            bestEscapeMoves = storage.readInt(KEY_BEST_ESCAPE),
            cleanRuns = storage.readInt(KEY_CLEAN_RUNS),
            pieces = pieceTotal(),
            maxPieces = GameConfig.TOTAL_COSTUMES * GameConfig.PIECES_PER_COSTUME
        )
    }

    override fun levelProgress(levelId: Int): LevelProgress = LevelProgress(
        levelId = levelId,
        stars = storage.readInt(starKey(levelId)),
        bestScore = storage.readInt(bestKey(levelId)),
        unlocked = isUnlocked(levelId)
    )

    override fun allLevelProgress(): List<LevelProgress> =
        SampleData.levels.map { levelProgress(it.id) }

    override fun acts(): List<ActProgress> = (1..GameConfig.ACTS).map { act ->
        val ids = actLevelIds(act)
        ActProgress(
            act = act,
            stars = ids.sumOf { storage.readInt(starKey(it)) },
            maxStars = ids.size * 3,
            unlocked = isActUnlocked(act)
        )
    }

    override fun nextUnlockedLevelId(): Int {
        val playable = SampleData.levels.filter { isUnlocked(it.id) }
        val fresh = playable.firstOrNull { storage.readInt(starKey(it.id)) == 0 }
        return fresh?.id ?: playable.lastOrNull()?.id ?: 1
    }

    override fun saveRun(result: RunResult): RunResult {
        if (result.outcome != RunOutcome.ESCAPED) return result
        val previousStars = storage.readInt(starKey(result.levelId))
        if (result.stars > previousStars) storage.writeInt(starKey(result.levelId), result.stars)
        val previousBest = storage.readInt(bestKey(result.levelId))
        val improved = result.score > previousBest
        if (improved) storage.writeInt(bestKey(result.levelId), result.score)
        val maxPieces = GameConfig.TOTAL_COSTUMES * GameConfig.PIECES_PER_COSTUME
        storage.writeInt(KEY_PIECES, minOf(maxPieces, pieceTotal() + result.pieces))
        if (!result.usedBell) storage.writeInt(KEY_CLEAN_RUNS, storage.readInt(KEY_CLEAN_RUNS) + 1)
        val bestEscape = storage.readInt(KEY_BEST_ESCAPE)
        if (bestEscape == 0 || result.movesUsed < bestEscape) {
            storage.writeInt(KEY_BEST_ESCAPE, result.movesUsed)
        }
        return result.copy(newBest = improved)
    }

    override fun costumes(): List<CostumeProgress> {
        val total = pieceTotal()
        val equipped = equippedCostumeId()
        val required = GameConfig.PIECES_PER_COSTUME
        return SampleData.costumes.map { costume ->
            val owned = (total - costume.id * required).coerceIn(0, required)
            CostumeProgress(costume, owned, required, equipped == costume.id && owned >= required)
        }
    }

    override fun equipCostume(costumeId: Int) {
        storage.writeInt(KEY_EQUIPPED, costumeId + 1)
    }

    override fun equippedCostumeId(): Int = storage.readInt(KEY_EQUIPPED) - 1

    override fun resetAll() {
        for (level in SampleData.levels) {
            storage.writeInt(starKey(level.id), 0)
            storage.writeInt(bestKey(level.id), 0)
        }
        storage.writeInt(KEY_PIECES, 0)
        storage.writeInt(KEY_CLEAN_RUNS, 0)
        storage.writeInt(KEY_BEST_ESCAPE, 0)
        storage.writeInt(KEY_EQUIPPED, 0)
    }

    private fun pieceTotal(): Int = storage.readInt(KEY_PIECES)

    private fun actLevelIds(act: Int): List<Int> {
        val first = (act - 1) * GameConfig.LEVELS_PER_ACT + 1
        return (first until first + GameConfig.LEVELS_PER_ACT).toList()
    }

    private fun isActUnlocked(act: Int): Boolean {
        if (act <= 1) return true
        val previous = actLevelIds(act - 1).sumOf { storage.readInt(starKey(it)) }
        return previous >= GameConfig.STARS_TO_UNLOCK_ACT
    }

    private fun isUnlocked(levelId: Int): Boolean {
        if (levelId <= 1) return true
        val act = (levelId - 1) / GameConfig.LEVELS_PER_ACT + 1
        if (!isActUnlocked(act)) return false
        if ((levelId - 1) % GameConfig.LEVELS_PER_ACT == 0) return true
        return storage.readInt(starKey(levelId - 1)) > 0
    }

    private fun starKey(levelId: Int) = "stars_" + levelId

    private fun bestKey(levelId: Int) = "best_" + levelId

    private companion object {
        const val KEY_PIECES = "pieces_total"
        const val KEY_CLEAN_RUNS = "clean_runs"
        const val KEY_BEST_ESCAPE = "best_escape"
        const val KEY_EQUIPPED = "equipped_costume"
    }
}
