package android.mahendra.attendancemanager.adapters

import android.content.Context
import android.mahendra.attendancemanager.R
import android.mahendra.attendancemanager.databinding.ListItemSubjectBinding
import android.mahendra.attendancemanager.models.Subject
import android.mahendra.attendancemanager.utilities.InjectorUtils
import android.mahendra.attendancemanager.viewmodels.subject.SubjectDetailViewModel
import android.mahendra.attendancemanager.viewmodels.subject.SubjectListViewModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class SubjectAdapter : RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {
    var data = emptyList<Subject>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        return SubjectViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    class SubjectViewHolder private constructor(
            private val binding: ListItemSubjectBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private var subject: Subject? = null
        fun bind(subject: Subject) {
            this.subject = subject
            binding.subjectDetailViewModel!!.subject = subject
            binding.executePendingBindings()
        }

        override fun onClick(v: View) {

        }

        init {
            val detailViewModel =
                    InjectorUtils.provideSubjectDetailViewModel(binding.root.context)
            binding.subjectDetailViewModel = detailViewModel
            binding.moreOptions.setOnClickListener(this)
        }

        companion object {
            fun from(parent: ViewGroup): SubjectViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = DataBindingUtil.inflate<ListItemSubjectBinding>(
                        inflater, R.layout.list_item_subject, parent, false)
                return SubjectViewHolder(binding)
            }
        }
    }
}
