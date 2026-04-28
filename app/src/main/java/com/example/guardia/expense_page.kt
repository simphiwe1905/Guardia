package com.example.guardia

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class expense_page : AppCompatActivity() {

    lateinit var etExpenseName: EditText
    lateinit var etExpenseAmount: EditText
    lateinit var etExpenseCategory: EditText
    lateinit var etExpenseDate: EditText
    lateinit var etExpenseDescription: EditText
    lateinit var btnSaveExpense: Button
    lateinit var btnBackToDashboard: Button
    lateinit var btnExpenseMenu: ImageButton
    lateinit var btnExpenseSettings: ImageButton
    lateinit var imgExpensePhoto: ImageView
    lateinit var btnCapturePhoto: Button

    var userId: Long = -1
    var currentPhotoPath: String = ""
    var capturedImageUri: Uri? = null

    val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            capturedImageUri?.let { uri ->
                imgExpensePhoto.setImageURI(uri)
            }
        }
    }

    val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            imgExpensePhoto.setImageURI(uri)
            currentPhotoPath = saveImageToInternalStorage(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_page)

        supportActionBar?.hide()

        userId = intent.getLongExtra("USER_ID", -1)

        initializeViews()
        setupClickListeners()
        setupDatePicker()
    }

    fun initializeViews() {
        etExpenseName = findViewById(R.id.etExpenseName)
        etExpenseAmount = findViewById(R.id.etExpenseAmount)
        etExpenseCategory = findViewById(R.id.etExpenseCategory)
        etExpenseDate = findViewById(R.id.etExpenseDate)
        etExpenseDescription = findViewById(R.id.etExpenseDescription)
        btnSaveExpense = findViewById(R.id.btnSaveExpense)
        btnBackToDashboard = findViewById(R.id.btnBackToDashboard)
        btnExpenseMenu = findViewById(R.id.btnExpenseMenu)
        btnExpenseSettings = findViewById(R.id.btnExpenseSettings)
        imgExpensePhoto = findViewById(R.id.imgExpensePhoto)
        btnCapturePhoto = findViewById(R.id.btnCapturePhoto)
    }

    fun setupDatePicker() {
        etExpenseDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = String.format(
                        "%02d/%02d/%04d",
                        selectedDay,
                        selectedMonth + 1,
                        selectedYear
                    )
                    etExpenseDate.setText(formattedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }

    fun setupClickListeners() {
        btnCapturePhoto.setOnClickListener {
            showImagePickerDialog()
        }

        btnSaveExpense.setOnClickListener {
            val name = etExpenseName.text.toString().trim()
            val amountText = etExpenseAmount.text.toString().trim()
            val category = etExpenseCategory.text.toString().trim()
            val date = etExpenseDate.text.toString().trim()
            val description = etExpenseDescription.text.toString().trim()

            if (name.isEmpty() || amountText.isEmpty() || category.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    val amount = amountText.toDouble()
                    saveExpense(name, amount, category, date, description, currentPhotoPath)
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnBackToDashboard.setOnClickListener {
            val intent = Intent(this, dashboard::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
            finish()
        }

        btnExpenseMenu.setOnClickListener {
            Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show()
        }

        btnExpenseSettings.setOnClickListener {
            Toast.makeText(this, "Theme settings", Toast.LENGTH_SHORT).show()
        }
    }

    fun showImagePickerDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Remove Photo")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Add Photo")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> takePhoto()
                1 -> chooseFromGallery()
                2 -> removePhoto()
            }
        }
        builder.show()
    }

    fun takePhoto() {
        val photoFile = createImageFile()
        if (photoFile != null) {
            currentPhotoPath = photoFile.absolutePath
            capturedImageUri = FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                photoFile
            )
            capturedImageUri?.let { uri ->
                takePictureLauncher.launch(uri)
            }
        }
    }

    fun chooseFromGallery() {
        pickImageLauncher.launch("image/*")
    }

    fun removePhoto() {
        currentPhotoPath = ""
        capturedImageUri = null
        imgExpensePhoto.setImageResource(android.R.drawable.ic_menu_camera)
        Toast.makeText(this, "Photo removed", Toast.LENGTH_SHORT).show()
    }

    fun createImageFile(): File? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "EXPENSE_$timeStamp"
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            File.createTempFile(imageFileName, ".jpg", storageDir)
        } catch (e: Exception) {
            Toast.makeText(this, "Error creating image file", Toast.LENGTH_SHORT).show()
            null
        }
    }

    fun saveImageToInternalStorage(uri: Uri): String {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "EXPENSE_$timeStamp.jpg"
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val imageFile = File(storageDir, imageFileName)

            val inputStream = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(imageFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            imageFile.absolutePath
        } catch (e: Exception) {
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show()
            ""
        }
    }

    fun saveExpense(name: String, amount: Double, category: String, date: String, description: String, imagePath: String) {
        lifecycleScope.launch {
            try {
                val application = application as GuardiaApplication
                application.repository.addExpenseWithImage(userId, name, amount, category, date, description, imagePath)

                Toast.makeText(this@expense_page, "Expense saved successfully!", Toast.LENGTH_SHORT).show()
                clearFields()

                val intent = Intent(this@expense_page, dashboard::class.java)
                intent.putExtra("USER_ID", userId)
                startActivity(intent)
                finish()

            } catch (e: Exception) {
                Toast.makeText(this@expense_page, "Error saving expense: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun clearFields() {
        etExpenseName.text.clear()
        etExpenseAmount.text.clear()
        etExpenseCategory.text.clear()
        etExpenseDate.text.clear()
        etExpenseDescription.text.clear()
        currentPhotoPath = ""
        capturedImageUri = null
        imgExpensePhoto.setImageResource(android.R.drawable.ic_menu_camera)
    }
}