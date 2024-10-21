package com.dam.echirinosv3.data.repository

import com.dam.echirinosv3.data.model.Incidencia
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class IncsRepository(private val refFS: DocumentReference) {
    fun getAllIncsByFiltro(idDpto: String, fecha: String, estado: Int): Flow<List<Incidencia>> {
        var query: Query = refFS.collection("incs")

        query = if (idDpto == "") { // todos los dptos
            if (estado != -1) {
                query
                    .whereEqualTo("estado", estado == 1)
                    .whereGreaterThanOrEqualTo("fecha", fecha)
                    .orderBy("fecha").orderBy("idDpto").orderBy("id")
            } else {
                query
                    .whereGreaterThanOrEqualTo("fecha", fecha)
                    .orderBy("fecha").orderBy("idDpto").orderBy("id")
            }
        } else {
            if (estado != -1) {
                query
                    .whereEqualTo("idDpto", idDpto.toInt())
                    .whereEqualTo("estado", estado == 1)
                    .whereGreaterThanOrEqualTo("fecha", fecha)
                    .orderBy("fecha").orderBy("idDpto").orderBy("id")
            } else {
                query
                    .whereEqualTo("idDpto", idDpto.toInt())
                    .whereGreaterThanOrEqualTo("fecha", fecha)
                    .orderBy("fecha").orderBy("idDpto").orderBy("id")
            }
        }
        return query.dataObjects<Incidencia>()
    }

    suspend fun insertInc(inc: Incidencia) {
        refFS.collection("incs")
            .document(inc.idDpto.toString() + "_" + inc.id + "_" + inc.fecha).set(inc)
            .await()
    }

    suspend fun updateInc(inc: Incidencia) {
        refFS.collection("incs")
            .document(inc.idDpto.toString() + "_" + inc.id + "_" + inc.fecha)
            .set(inc, SetOptions.merge()).await()
    }

    suspend fun deleteInc(inc: Incidencia) {
        refFS.collection("incs")
            .document(inc.idDpto.toString() + "_" + inc.id + "_" + inc.fecha).delete()
            .await()
    }
}