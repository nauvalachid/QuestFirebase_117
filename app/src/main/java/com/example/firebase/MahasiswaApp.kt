package com.example.firebase

import android.app.Application
import com.example.firebase.di.MahasiswaContainer

class MahasiswaAppApp : Application() {
    //Fungsinya untuk menyimpan instance ContainerApp
    lateinit var containerApp : MahasiswaContainer

    override fun onCreate() {
        super.onCreate()
        //Membuat instance ContainerApp
        containerApp = MahasiswaContainer(this)
        //instance adalah object yang dibuat dari class
    }
}