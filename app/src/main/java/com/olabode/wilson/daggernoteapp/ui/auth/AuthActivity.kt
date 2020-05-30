package com.olabode.wilson.daggernoteapp.ui.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import com.olabode.wilson.daggernoteapp.MainActivity
import com.olabode.wilson.daggernoteapp.R
import com.olabode.wilson.daggernoteapp.databinding.ActivityAuthBinding
import java.util.concurrent.Executor

class AuthActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var binding: ActivityAuthBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)


        val preferences: SharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(this)
        val isFingerPrintEnabled = preferences
            .getBoolean(getString(R.string.SHARED_PREF_FINGERPRINT), false)

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        this@AuthActivity,
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                    finish()
                    Toast.makeText(
                        this@AuthActivity,
                        "Authentication succeeded!", Toast.LENGTH_SHORT
                    ).show()
                }

            })

        if (!isFingerPrintEnabled) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        if (isFingerPrintEnabled) {
            showBiometricPrompt()
        }

        binding.fingerprintAuth.setOnClickListener {
            if (isFingerPrintEnabled) {
                showBiometricPrompt()
            } else {

            }
        }

        binding.login.setOnClickListener {
            if (TextUtils.isEmpty(binding.passwordTextField.text.toString().trim())) {
                Toast.makeText(this, getString(R.string.blank_field_prompt), Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (isValidCredential()) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }

    }

    private fun isValidCredential(): Boolean {
        //TODO implemnent password as alternative for fingerprint
        return true
    }


    private fun showBiometricPrompt() {
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.title_prompt))
            .setSubtitle(getString(R.string.biometric_prompt_subtitle))
            .setNegativeButtonText(getString(R.string.cancel))
            .build()
        biometricPrompt.authenticate(promptInfo)
    }
}