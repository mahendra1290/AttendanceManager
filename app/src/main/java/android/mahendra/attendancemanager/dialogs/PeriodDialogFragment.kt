package android.mahendra.attendancemanager.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.mahendra.attendancemanager.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import java.util.*

class PeriodDialogFragment : DialogFragment() {
    private var subjectTitle: String? = null
    private var allSubjectTitles: List<String> = emptyList()

    private var mCallback: Callbacks? = null

    interface Callbacks {
        val subjectTitles: List<String>
        fun onPeriodSelected(title: String?)
        fun onDeletePeriod(title: String?)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_PERIOD_TITLE, subjectTitle)
        outState.putStringArrayList(KEY_TITLE_OPTIONS, allSubjectTitles as ArrayList<String>)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mCallback = context as Callbacks
        } catch (ex: ClassCastException) {
            Log.e(TAG, "onAttach: $ex")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mCallback = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            subjectTitle = savedInstanceState.getString(KEY_PERIOD_TITLE, null)
            allSubjectTitles = savedInstanceState.getStringArrayList(KEY_TITLE_OPTIONS) as List<String>
        } else {
            allSubjectTitles = mCallback!!.subjectTitles
            subjectTitle = arguments?.getString(ARG_PERIOD_TITLE, null)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialogTitle: Int
        val positiveButtonText: Int
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_choose_period, null)
        val removePeriodButton = view.findViewById<Button>(R.id.remove_period_button)

        removePeriodButton.setOnClickListener { v1: View? ->
            mCallback!!.onDeletePeriod(subjectTitle)
            Toast.makeText(activity, "Removed $subjectTitle", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        if (subjectTitle != null) {
            removePeriodButton.visibility = View.VISIBLE
            dialogTitle = R.string.update_period
            positiveButtonText = R.string.update
        } else {
            removePeriodButton.visibility = View.GONE
            dialogTitle = R.string.add_period
            positiveButtonText = R.string.add
        }

        val spinner = view.findViewById<Spinner>(R.id.spinner_select_period)
        val adapter: ArrayAdapter<String> = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item)
        adapter.addAll(allSubjectTitles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        if (subjectTitle != null) {
            val position = adapter.getPosition(subjectTitle)
            spinner.setSelection(position)
        }

        val builder = AlertDialog.Builder(activity)
        builder.setTitle(dialogTitle)
        builder.setView(view)
        builder.setPositiveButton(positiveButtonText) { dialog: DialogInterface?, which: Int -> mCallback!!.onPeriodSelected(spinner.selectedItem.toString()) }
        builder.setNegativeButton(R.string.cancel) { dialog: DialogInterface?, which: Int -> }
        return builder.create()
    }

    companion object {
        private const val TAG = "PeriodDialogFragment"
        private const val ARG_PERIOD_TITLE = "period title"
        private const val KEY_TITLE_OPTIONS = "title options"
        private const val KEY_PERIOD_TITLE = "period_title"
        fun newInstance(periodTitle: String?): PeriodDialogFragment {
            val args = Bundle()
            args.putString(ARG_PERIOD_TITLE, periodTitle)
            val fragment = PeriodDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}