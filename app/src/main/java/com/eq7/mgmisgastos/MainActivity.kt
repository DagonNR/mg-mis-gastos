package com.eq7.mgmisgastos

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Source

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.btnTest)
        val tv = findViewById<TextView>(R.id.tvResult)

        btn.setOnClickListener {
            tv.text = "Verificando"
            val testDoc = db.collection("status").document("ping")
            val data = hashMapOf(
                "last_check" to FieldValue.serverTimestamp(),
                "device" to android.os.Build.MODEL
            )

            testDoc.set(data)
                .addOnSuccessListener {
                    testDoc.get(Source.SERVER)
                        .addOnSuccessListener { snapshot ->
                            tv.text = "Conectado a Firebase."
                            tv.append("\nÚltima conexión: ${snapshot.getTimestamp("last_check")?.toDate()}")
                        }
                        .addOnFailureListener { e ->
                            tv.text = "Escritura local OK, pero sin respuesta del servidor."
                        }
                }
                .addOnFailureListener { e ->
                    tv.text = "Error de conexión: ${e.message}"
                }
        }
    }
}