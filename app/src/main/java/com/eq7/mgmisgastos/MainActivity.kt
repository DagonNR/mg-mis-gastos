package com.eq7.mgmisgastos

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.btnTest)
        val tv = findViewById<TextView>(R.id.tvResult)

        btn.setOnClickListener {
            val data = hashMapOf(
                "status" to "ok",
                "createdAt" to FieldValue.serverTimestamp()
            )

            db.collection("ping")
                .add(data)
                .addOnSuccessListener { doc ->
                    tv.text = "Conectado. Doc: ${doc.id}"

                    doc.get().addOnSuccessListener { snap ->
                        tv.append("\nLeído: ${snap.data}")
                    }
                }
                .addOnFailureListener { e ->
                    tv.text = "Error: ${e.message}"
                }
        }
    }
}
