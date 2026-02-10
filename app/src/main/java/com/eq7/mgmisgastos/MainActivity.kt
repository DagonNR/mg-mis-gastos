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
            tv.text = "Verificando conexión..."

            // 1. Usamos un documento fijo para no "ensuciar" la base de datos
            val testDoc = db.collection("status").document("ping")

            val data = hashMapOf(
                "last_check" to FieldValue.serverTimestamp(),
                "device" to android.os.Build.MODEL
            )

            // 2. Intentamos escribir.
            testDoc.set(data)
                .addOnSuccessListener {
                    // 3. Forzamos la lectura desde el SERVIDOR (no de la caché)
                    // Esto garantiza que el SDK realmente pudo hablar con Firebase.
                    testDoc.get(Source.SERVER)
                        .addOnSuccessListener { snapshot ->
                            tv.text = "¡Conectado exitosamente con Firestore!"
                            tv.append("\nÚltima sincronización: ${snapshot.getTimestamp("last_check")?.toDate()}")
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