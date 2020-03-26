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

class SubjectAdapter(
        private val subjectListViewModel: SubjectListViewModel
) : RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {
    var subjects = emptyList<Subject>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        return SubjectViewHolder.from(parent, subjectListViewModel)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.bind(subjects[position])
    }

    override fun getItemCount() = subjects.size

    class SubjectViewHolder private constructor(
            private val binding: ListItemSubjectBinding,
            private val subjectListViewModel: SubjectListViewModel
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
            val detailViewModel = SubjectDetailViewModel(subjectListViewModel)
            binding.subjectDetailViewModel = detailViewModel
            binding.moreOptions.setOnClickListener(this)
        }

        companion object {
            fun from(parent: ViewGroup, subjectListViewModel: SubjectListViewModel): SubjectViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = DataBindingUtil.inflate<ListItemSubjectBinding>(
                        inflater, R.layout.list_item_subject, parent, false)
                return SubjectViewHolder(binding, subjectListViewModel)
            }
        }
    }
}

class SubjectDiffCallback : DiffUtil.ItemCallback<Subject>() {
    override fun areItemsTheSame(oldItem: Subject, newItem: Subject): Boolean {
//        Timber.i("are item same $oldItem == $newItem")
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Subject, newItem: Subject): Boolean {
//        Timber.i("are content same $oldItem == $newItem")
        return oldItem == newItem
    }
}
