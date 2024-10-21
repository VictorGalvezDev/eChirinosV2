package com.dam.echirinosv3.data

import android.content.Context
import com.dam.echirinosv3.data.model.AppDatabase
import com.dam.echirinosv3.data.model.AppDatastore
import com.dam.echirinosv3.data.repository.AulasRepository
import com.dam.echirinosv3.data.repository.DptosRepository
import com.dam.echirinosv3.data.repository.IncsRepository
import com.dam.echirinosv3.data.repository.MainRepository

interface AppContainer {
    val dptosRepository: DptosRepository
    val aulasRepository: AulasRepository
    val incsRepository: IncsRepository
    val mainRepository: MainRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    override val mainRepository: MainRepository by lazy {
        MainRepository(context, AppDatastore.getDataStore(context))
    }

    override val dptosRepository: DptosRepository by lazy {
        DptosRepository(AppDatabase.getDatabase(context).getRefFS())
    }

    override val aulasRepository: AulasRepository by lazy {
        AulasRepository(AppDatabase.getDatabase(context).getRefFS())
    }

    override val incsRepository: IncsRepository by lazy {
        IncsRepository(AppDatabase.getDatabase(context).getRefFS())
    }
}