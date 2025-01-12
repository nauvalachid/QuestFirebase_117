package com.example.firebase.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.firebase.repository.RepositoryMhs

class InsertViewModel (
    private val mhs: RepositoryMhs
) : ViewModel() {

}

data class MahasiswaEvent(
    val nim: String = "",
    val nama: String = "",
    val jenisKelamin: String = "",
    val alamat: String = "",
    val kelas: String = "",
    val angkatan: String = ""
)