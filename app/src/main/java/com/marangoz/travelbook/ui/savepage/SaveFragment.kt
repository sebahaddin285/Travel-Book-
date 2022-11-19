package com.marangoz.travelbook.ui.savepage

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.marangoz.travelbook.R
import com.marangoz.travelbook.databinding.FragmentSaveBinding
import com.marangoz.travelbook.model.TravelBook
import com.marangoz.travelbook.repository.Repository
import com.marangoz.travelbook.room.TravelBookDao
import com.marangoz.travelbook.room.TravelBookDataBase

@SuppressLint("StaticFieldLeak")
private lateinit var binding: FragmentSaveBinding
private lateinit var viewModel: SaveFragmentViewModel
var imageBitmap: Bitmap? = null

class SaveFragment : Fragment() {
    private val db: TravelBookDataBase by lazy { TravelBookDataBase.accsessDatabase(requireContext())!! }
    private val tDao: TravelBookDao by lazy { db.getTravelBookDao() }
    private val repo: Repository by lazy { Repository(tDao) }
    val viewModelFactory by lazy { SaveFragmentViewModelFactory(repo) }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveBinding.inflate(inflater, container, false)

        //send repository to viewmodel
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(SaveFragmentViewModel::class.java)

        binding.saveButton.setOnClickListener() {
            if (binding.titleText.text!!.isEmpty() || binding.dateTextA.text!!.isEmpty() || imageBitmap == null) {
                val sb = Snackbar.make(it, resources.getString(R.string.bosbirak), Snackbar.LENGTH_SHORT)
                sb.setTextColor(Color.BLUE)
                sb.setBackgroundTint(Color.WHITE)
                sb.show()
                return@setOnClickListener
            }
            val travelBook = TravelBook(
                binding.titleText.text.toString(), binding.dateTextA.text.toString(),
                imageBitmap!!
            )
            viewModel.insertTravelBook(travelBook)
            val sb = Snackbar.make(it, resources.getString(R.string.islembasari), Snackbar.LENGTH_SHORT)
            sb.setTextColor(Color.BLUE)
            sb.setBackgroundTint(Color.WHITE)
            sb.show()
            Navigation.findNavController(it).navigate(R.id.passToHomePage)

        }
        binding.imageView2.setOnClickListener() {
            pickImage()
        }

        binding.dateTextA.setOnClickListener() {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker: DatePickerDialog

            datePicker = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { datePicker, i1, i2, i3 ->

                    binding.dateTextA.setText("$i3/${i2 + 1}/$i1")

                },
                year,
                month,
                day
            )

            datePicker.setTitle(resources.getString(R.string.tarihsec))
            datePicker.setButton(DialogInterface.BUTTON_POSITIVE, resources.getString(R.string.ayar), datePicker)
            datePicker.setButton(DialogInterface.BUTTON_NEGATIVE, resources.getString(R.string.iptal), datePicker)
            datePicker.show()
        }






        return binding.root
    }


    private fun pickImage() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_REQUEST_CODE
            )
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                return
            }
            val uri = data?.data
            if (uri != null) {
                imageBitmap = Bitmap.createScaledBitmap(uriToBitmap(uri),400,400,true)
                binding.imageView2.setImageBitmap(imageBitmap)
            }
        }
    }

    private fun uriToBitmap(uri: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
    }

    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 1000
        const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 1001
    }
}