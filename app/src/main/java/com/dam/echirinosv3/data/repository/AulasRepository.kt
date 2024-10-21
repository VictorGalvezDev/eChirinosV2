package com.dam.echirinosv3.data.repository

import com.dam.echirinosv3.data.model.Aula
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class AulasRepository(private val refFS: DocumentReference) {

    fun getAllAulasByFiltro(idDpto: String): Flow<List<Aula>> {
        var query: Query = refFS.collection("aulas")
        query = if (idDpto == "") { // todos los dptos
            query.orderBy("idDpto").orderBy("id")
        } else {
            query.orderBy("idDpto").orderBy("id")
                .whereEqualTo("idDpto", idDpto.toInt())
        }
        return query.dataObjects<Aula>()
    }


    suspend fun insertAula(aula: Aula) {
        refFS.collection("aulas")
            .document(aula.idDpto.toString() + "-" + aula.id.toString()).set(aula).await()
    }

    suspend fun updateAula(aula: Aula) {
        refFS.collection("aulas")
            .document(aula.idDpto.toString() + "-" + aula.id.toString())
            .set(aula, SetOptions.merge()).await()
    }

    suspend fun deleteAula(aula: Aula) {
        refFS.collection("aulas")
            .document(aula.idDpto.toString() + "-" + aula.id.toString()).delete().await()
    }

}