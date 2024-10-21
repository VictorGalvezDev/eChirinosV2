package com.dam.echirinosv3.data.model

import android.content.Context
import com.dam.echirinosv3.R
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class AppDatabase private constructor() {
    companion object {
        @Volatile
        private var Instance: AppDatabase? = null
        private lateinit var refFS: DocumentReference
        fun getDatabase(context: Context): AppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                val dbFS = FirebaseFirestore.getInstance()
                refFS = dbFS.collection(context.resources.getString(R.string.proyectos_database))
                    .document(context.resources.getString(R.string.value_pref_dbname))
                val dpto = Departamento(0, "admin", "a")
                refFS.collection("dptos").document(dpto.id.toString()).set(dpto)
                AppDatabase()
            }
        }
    }

    fun getRefFS(): DocumentReference {
        return refFS
    }
}
