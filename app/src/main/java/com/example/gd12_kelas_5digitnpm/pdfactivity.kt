package com.example.gd12_kelas_5digitnpm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.gd12_kelas_5digitnpm.databinding.ActivityPdfactivityBinding

class pdfactivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityPdfactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        binding.pdfInput.setOnClickListener(this)
        binding.pdfTable.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.pdf_input -> {
                intent = Intent(applicationContext,pdfbyinputActivity::class.java)
                startActivity(intent)
            }
            R.id.pdf_table -> {
                intent = Intent(applicationContext,qrGenerateActivity::class.java)
                startActivity(intent)
            }
        }
    }
}