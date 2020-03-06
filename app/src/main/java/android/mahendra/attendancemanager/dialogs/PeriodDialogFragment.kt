package android.mahendra.attendancemanager.dialogs

import android.app.ActionBar
import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.mahendra.attendancemanager.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import java.util.*

class PeriodDialogFragment : DialogFragment() {
    private var subjectTitle: String? = null
    private var allSubjectTitles: List<String> = emptyList()
    private lateinit var toolbar: Toolbar
    private var callbacks: Callbacks? = null

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
            callbacks = context as Callbacks
        } catch (ex: ClassCastException) {
            throw java.lang.ClassCastException("callbacks must be implemented by $context")
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            subjectTitle = savedInstanceState.getString(KEY_PERIOD_TITLE, null)
            allSubjectTitles = savedInstanceState.getStringArrayList(KEY_TITLE_OPTIONS) as List<String>
        } else {
            allSubjectTitles = callbacks!!.subjectTitles
            subjectTitle = arguments?.getString(ARG_PERIOD_TITLE, null)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.dialog_choose_period, container, false)
        toolbar = view.findViewById(R.id.toolbar)
        val removePeriodButton = view.findViewById<Button>(R.id.remove_period_button)
        val periodTimeButton  = view.findViewById<Button>(R.id.period_time_button)
        val spinner = view.findViewById<Spinner>(R.id.spinner_select_period)
        removePeriodButton.setOnClickListener { v1: View? ->
            callbacks!!.onDeletePeriod(subjectTitle)
            Toast.makeText(activity, "Removed $subjectTitle", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        var hours: Int = 0
        var minutes: Int = 30
        periodTimeButton.setOnClickListener {
            val dialog = TimePickerDialog(
                    activity,
                    TimePickerDialog.OnTimeSetListener() { _: TimePicker, _: Int, _: Int ->

                    }, hours, minutes, false)
            dialog.show()
        }


        if (subjectTitle != null) {
            removePeriodButton.visibility = View.VISIBLE

        } else {
            removePeriodButton.visibility = View.GONE
        }
        val adapter: ArrayAdapter<String> = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item)
        adapter.addAll(allSubjectTitles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        if (subjectTitle != null) {
            val position = adapter.getPosition(subjectTitle)
            spinner.setSelection(position)
        }
        return view
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//
//
////        val builder = AlertDialog.Builder(activity)
////        builder.setTitle(dialogTitleId)
////        builder.setView(view)
////        builder.setPositiveButton(positiveButtonTextId) { dialog: DialogInterface?, which: Int -> callbacks!!.onPeriodSelected(spinner.selectedItem.toString()) }
////        builder.setNegativeButton(R.string.cancel) { dialog: DialogInterface?, which: Int -> }
////        return builder.create()
//        Log.i(TAG, "on create dialog called")
//        return super.onCreateDialog(savedInstanceState)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.title = "Add Period"
        toolbar.inflateMenu(R.menu.period_dialog_fragment)

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