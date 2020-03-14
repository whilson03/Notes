package com.olabode.wilson.daggernoteapp.ui.settings


import android.content.*
import android.net.Uri
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.olabode.wilson.daggernoteapp.R


/**
 * A simple [Fragment] subclass.
 */
class Settings : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_main, rootKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val ratePreference = findPreference<Preference>(getString(R.string.key_rate))
        ratePreference?.let {
            it.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                rate(context!!)
                true
            }
        }


        val report = findPreference<Preference>(getString(R.string.key_bug_report))
        report?.let {
            it.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                GoogleFeedbackUtils.bindFeedback(context!!)
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

}