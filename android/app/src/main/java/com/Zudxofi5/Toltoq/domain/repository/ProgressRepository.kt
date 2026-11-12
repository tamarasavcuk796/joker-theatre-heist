package com.Zudxofi5.Toltoq.domain.repository

import com.Zudxofi5.Toltoq.domain.model.ActProgress
import com.Zudxofi5.Toltoq.domain.model.CareerProgress
import com.Zudxofi5.Toltoq.domain.model.CostumeProgress
import com.Zudxofi5.Toltoq.domain.model.LevelProgress
import com.Zudxofi5.Toltoq.domain.model.RunResult

interface ProgressRepository {
    fun career(): CareerProgress
    fun levelProgress(levelId: Int): LevelProgress
    fun allLevelProgress(): List<LevelProgress>
    fun acts(): List<ActProgress>
    fun nextUnlockedLevelId(): Int
    fun saveRun(result: RunResult): RunResult
    fun costumes(): List<CostumeProgress>
    fun equipCostume(costumeId: Int)
    fun equippedCostumeId(): Int
    fun resetAll()
}
