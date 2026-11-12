package com.Zudxofi5.Toltoq.data.repository

import com.Zudxofi5.Toltoq.data.sample.SampleData
import com.Zudxofi5.Toltoq.domain.model.Level
import com.Zudxofi5.Toltoq.domain.repository.LevelRepository

class LevelRepositoryImpl : LevelRepository {

    override fun levels(): List<Level> = SampleData.levels

    override fun level(id: Int): Level =
        SampleData.levels.firstOrNull { it.id == id } ?: SampleData.levels.first()
}
