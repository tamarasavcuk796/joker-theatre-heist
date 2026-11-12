package com.Zudxofi5.Toltoq.domain.repository

import com.Zudxofi5.Toltoq.domain.model.Level

interface LevelRepository {
    fun levels(): List<Level>
    fun level(id: Int): Level
}
