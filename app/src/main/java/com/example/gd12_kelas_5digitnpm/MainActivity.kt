package com.example.gd12_kelas_5digitnpm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.gd12_kelas_5digitnpm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonPdf.setOnClickListener(this)
        binding.buttonScanqr.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_pdf -> {
                intent = Intent(applicationContext,pdfactivity::class.java)
                startActivity(intent)
            }
            R.id.button_scanqr -> {
                intent = Intent(applicationContext, qrcodeactivity::class.java)
                startActivity(intent)
            }
        }

    }
}