package com.olabode.wilson.daggernoteapp.ui.settings


import android.content.*
import android.net.Uri
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.olabode.wilson.daggernoteapp.R
import com.olabode.wilson.daggernoteapp.work.ClearTrashWorker
import dagger.android.support.AndroidSupportInjection
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 */
class Settings : PreferenceFragmentCompat() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_main, rootKey)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val biometricManager = BiometricManager.from(context!!)
        val fingerprint =
            findPreference<SwitchPreferenceCompat>(getString(R.string.key_fingerprint))

        executor = ContextCompat.getMainExecutor(context)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    fingerprint?.isChecked = false
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    setFingerPrint(true)
                    Toast.makeText(
                        context,
                        getString(R.string.auth_success), Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    setFingerPrint(false)
                    fingerprint?.isChecked = false
                    Toast.makeText(
                        context, getString(R.string.auth_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })


        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                fingerprint?.let {
                    it.setOnPreferenceChangeListener { _, _ ->
                        if (!it.isChecked) {
                            promptInfo = BiometricPrompt.PromptInfo.Builder()
                                .setTitle(getString(R.string.auth_noteskeep))
                                .setSubtitle(getString(R.string.biometric_prompt_subtitle))
                                .setNegativeButtonText(getString(R.string.cancel))
                                .build()
                            biometricPrompt.authenticate(promptInfo)
                        }
                        true
                    }
                }
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                fingerprint?.let { it.isChecked = false }
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                fingerprint?.let { it.isEnabled = false }
            }
        }


        fingerprint?.let { switch ->
            switch.setOnPreferenceClickListener {
                when (biometricManager.canAuthenticate()) {
                    BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                        Toast.makeText(
                            context, getString(R.string.text_enable_fingerprint_prompt),
                            Toast.LENGTH_SHORT
                        ).show()
                        switch.isChecked = false
                    }
                    BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
                    BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                        switch.isChecked = false
                        Toast.makeText(
                            context, getString(
                                R.string
                                    .fingerprint_compatibility_prompt
                            ), Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                true
            }
        }


        val ratePreference = findPreference<Preference>(getString(R.string.key_rate))
        ratePreference?.let {
            it.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                rate(context!!)
                true
            }
        }


        val feedback = findPreference<Preference>(getString(R.string.key_send_feedback))
        feedback?.let {
            it.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                sendFeedback()
                true
            }
        }


        val darkMode = findPreference<SwitchPreferenceCompat>(getString(R.string.key_mode_dark))
        darkMode?.let {
            it.setOnPreferenceChangeListener { _, _ ->
                if (!it.isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    activity?.recreate()
                    setMode(true)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    activity?.recreate()
                    setMode(false)
                }
                true
            }
        }


        val clearTrash =
            findPreference<SwitchPreferenceCompat>(getString(R.string.key_auto_empty_trash))
        clearTrash?.let {
            it.setOnPreferenceChangeListener { _, _ ->
                if (!it.isChecked) {
                    setupRecurringWork()
                } else {
                    WorkManager.getInstance(activity!!)
                        .cancelAllWorkByTag(ClearTrashWorker.WORK_NAME)

                }
                true
            }

        }


        return super.onCreateView(inflater, container, savedInstanceState)

    }


    private fun rate(context: Context) {
        val uri: Uri = Uri.parse("market://details?id=" + context.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market back stack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        // To count with Play market back stack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            context.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.packageName)
                )
            )
        }
    }


    private fun sendFeedback() {
        val intent =
            Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "whilson03@gmail.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_email))
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.dear) + "")
        startActivity(Intent.createChooser(intent, getString(R.string.text_feedback)))
    }


    object GoogleFeedbackUtils {
        private val TAG = GoogleFeedbackUtils::class.java.simpleName

        /**
         * Binds the Google Feedback service.
         *
         * @param context the context
         */
        fun bindFeedback(context: Context) {
            val intent = Intent(Intent.ACTION_BUG_REPORT)
            intent.component = ComponentName(
                "com.google.android.gms",
                "com.google.android.gms.feedback.LegacyBugReportService"
            )
            val serviceConnection: ServiceConnection = object : ServiceConnection {
                override fun onServiceConnected(
                    name: ComponentName,
                    service: IBinder
                ) {
                    try {
                        service.transact(Binder.FIRST_CALL_TRANSACTION, Parcel.obtain(), null, 0)
                    } catch (e: RemoteException) {
//                        Log.e(TAG, "RemoteException", e)
                    }
                }

                override fun onServiceDisconnected(name: ComponentName) {}
            }
            // Bind to the service after creating it if necessary
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }


    private fun setMode(isDark: Boolean) {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(getString(R.string.SHARED_PREF_DARK_MODE_KEY), isDark)
        editor.apply()
    }

    private fun setupRecurringWork() {
        val repeatingRequest =
            PeriodicWorkRequestBuilder<ClearTrashWorker>(30, TimeUnit.DAYS)
                .addTag(ClearTrashWorker.WORK_NAME)
                .build()
        WorkManager.getInstance(activity!!).enqueue(repeatingRequest)
    }


    private fun setFingerPrint(isFingerPrintEnable: Boolean) {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(getString(R.string.SHARED_PREF_FINGERPRINT), isFingerPrintEnable)
        editor.apply()
    }

}