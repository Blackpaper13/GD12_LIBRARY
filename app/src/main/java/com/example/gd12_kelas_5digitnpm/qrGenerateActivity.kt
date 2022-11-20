package com.example.gd12_kelas_5digitnpm

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.gd12_kelas_5digitnpm.databinding.ActivityQrGenerateBinding
import net.glxn.qrgen.core.scheme.VCard
import java.io.File
import java.io.FileOutputStream

class qrGenerateActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityQrGenerateBinding

    private var qrImage : Bitmap? = null
    val EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrGenerateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnText.setOnClickListener(this)
        binding.btnVCard.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        binding.btnGenerateQR.setOnClickListener(this)


        //check permission storage
        if (!checkPermissionForExternalStorage()) {
            requestPermissionForExternalStorage()
        }
    }

    override fun onClick(v: View?) {
       when(v?.id) {
            R.id.btn_text -> {
                binding.inputText.visibility = View.VISIBLE
                binding.layoutVCard.visibility = View.GONE
                binding.btnGenerateQR.visibility = View.VISIBLE
            }
           R.id.btn_vCard->
           {
               binding.inputText.visibility = View.GONE
               binding.layoutVCard.visibility = View.VISIBLE
               binding.btnGenerateQR.visibility = View.VISIBLE
           }
           R.id.btn_generateQR->
           {
               if(binding.layoutVCard.visibility == View.VISIBLE) {

                   if(binding.inputName.text.toString().isNullOrEmpty() && binding.inputEmail.text.toString().isNullOrEmpty()
                       && binding.inputAddress.text.toString().isNullOrEmpty() && binding.inputPhoneNumber.text.toString().isNullOrEmpty()
                       && binding.inputWebsite.text.toString().isNullOrEmpty())
                   {
                       Toast.makeText(applicationContext,"Semuanya tidak boleh kosong" , Toast.LENGTH_SHORT).show()
                   }
                   else
                   {
                       generateQRCode()
                   }
               }
               else if(binding.inputText.visibility == View.VISIBLE) {
                   if(!binding.inputText.text.toString().isNullOrEmpty())
                   {
                       generateQRCode()
                   }
                   else
                   {
                       binding.inputText.error = "Isilah Field berikut"
                   }
               }
           }
           R.id.btn_save->
           {
               if (!checkPermissionForExternalStorage()) {
                   Toast.makeText(this, "External Storage perlu diizinkan mohon buka pengaturan perizinan", Toast.LENGTH_LONG).show()
               }
               else
               {
                   if(qrImage != null){saveImage(qrImage!!)}
               }
           }

       }
    }

    private fun saveImage(qrImage: Bitmap): String {
        var savedImagePath: String? = null

        var imageFileName = "QR" + getTimeStamp() + ".jpg"
        var storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "/QRGenerator")
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            var imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.getAbsolutePath()
            try {
                var fOut = FileOutputStream(imageFile)
                qrImage.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            // Add the image to the system gallery
            var mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            var f = savedImagePath?.let { File(it) }
            var contentUri = Uri.fromFile(f)
            mediaScanIntent.setData(contentUri)
            sendBroadcast(mediaScanIntent)
            Toast.makeText(this,"QR CODE sudah tersimpan di Gallery",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this,"ERROR SAVING IMAGE",Toast.LENGTH_SHORT).show()
        }
        return savedImagePath!!
    }

    private fun getTimeStamp(): Any {
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()

        return ts
    }

    private fun generateQRCode() {
        if(binding.layoutVCard.visibility == View.VISIBLE)
        {
            val vCard = VCard(binding.inputName.text.toString())
                .setEmail(binding.inputEmail.text.toString())
                .setAddress(binding.inputAddress.text.toString())
                .setPhoneNumber(binding.inputPhoneNumber.text.toString())
                .setWebsite(binding.inputWebsite.text.toString())
            qrImage = net.glxn.qrgen.android.QRCode.from(vCard).bitmap()
            if(qrImage != null)
            {
                binding.imageViewQrCode.setImageBitmap(qrImage)
                binding.btnSave.visibility = View.VISIBLE
            }
        }
        else if(binding.inputText.visibility == View.VISIBLE)
        {
            qrImage = net.glxn.qrgen.android.QRCode.from(binding.inputText.text.toString()).bitmap()
            if(qrImage != null)
            {
                binding.imageViewQrCode.setImageBitmap(qrImage)
                binding.btnSave.visibility = View.VISIBLE
            }
        }
    }

    private fun checkPermissionForExternalStorage(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionForExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "External Storage perlu diizinkan mohon buka pengaturan perizinan", Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf<String>(WRITE_EXTERNAL_STORAGE), EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
        }
    }


}