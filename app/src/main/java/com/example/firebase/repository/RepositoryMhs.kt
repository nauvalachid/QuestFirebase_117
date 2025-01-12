package com.example.firebase.repository

import com.example.firebase.model.Mahasiswa
import kotlinx.coroutines.flow.Flow

interface RepositoryMhs {
    suspend fun insertMhs(mahasiswa: Mahasiswa)

    fun getAllMhs() : Flow<List<Mahasiswa>>

    fun getMhs(nim : String) : Flow<Mahasiswa>

    suspend fun deleteMhs(Mahasiswa: Mahasiswa)

    suspend fun updateMhs(Mahasiswa: Mahasiswa)
}