package com.example.mavicnotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.example.mavicnotes.database.NoteDatabase
import com.example.mavicnotes.repository.NoteRepository
import com.example.mavicnotes.viewmodel.NoteViewModel
import com.example.mavicnotes.viewmodel.NoteViewModelFactory
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricManager
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.Executor


class MainActivity : AppCompatActivity() {

    lateinit var biometricPrompt: BiometricPrompt
    lateinit var promptInfo: BiometricPrompt.PromptInfo
    lateinit var mMainLayout: ConstraintLayout
    private lateinit var textView: TextView

    lateinit var noteViewModel: NoteViewModel
    val calendar=Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)






        setupViewModel()

        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Toast.makeText(applicationContext, "Biometric hardware is ready", Toast.LENGTH_SHORT).show()
                // Perform your desired action here when there's no error
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(applicationContext, "Device doesn't have biometric hardware", Toast.LENGTH_SHORT).show()
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(applicationContext, "Biometric hardware is unavailable", Toast.LENGTH_SHORT).show()
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(applicationContext, "No biometrics are enrolled on this device", Toast.LENGTH_SHORT).show()
            }
        }


        val executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                // Handle authentication error
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)

                // Authentication succeeded, do something
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                // Authentication failed
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Is this Mutuma?")
            .setDescription("Use Finger Print to log in")
            .setDeviceCredentialAllowed(true)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun setupViewModel(){
        val noteRepository = NoteRepository(NoteDatabase(this))
        val viewModelProviderFactory = NoteViewModelFactory(application, noteRepository)
        noteViewModel = ViewModelProvider(this, viewModelProviderFactory)[NoteViewModel::class.java]
    }


}