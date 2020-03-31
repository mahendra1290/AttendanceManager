package android.mahendra.attendancemanager.adapters

import android.mahendra.attendancemanager.R
import android.mahendra.attendancemanager.databinding.ListItemSubjectBinding
import android.mahendra.attendancemanager.models.Subject
import android.mahendra.attendancemanager.utilities.InjectorUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class SubjectAdapter(
        private val subjectOptionClickListener: SubjectOptionClickListener
) : ListAdapter<Subject, SubjectAdapter.SubjectViewHolder>(SubjectDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        return SubjectViewHolder.from(parent, subjectOptionClickListener)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SubjectViewHolder private constructor(
            private val binding: ListItemSubjectBinding,
            private val subjectOptionClickListener: SubjectOptionClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(subject: Subject) {
            binding.subject = subject
            binding.subjectDetailViewModel!!.subject = subject
            binding.invalidateAll()
            binding.executePendingBindings()
        }

        init {
            val subjectDetailViewModel =
                    InjectorUtils.provideSubjectDetailViewModel(binding.root.context)
            binding.subjectDetailViewModel = subjectDetailViewModel
            binding.subjectOptionClickListener = subjectOptionClickListener
        }

        companion object {
            fun from(
                    parent: ViewGroup,
                    subjectClickListener: SubjectOptionClickListener
            ): SubjectViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = DataBindingUtil.inflate<ListItemSubjectBinding>(
                        inflater, R.layout.list_item_subject, parent, false)
                return SubjectViewHolder(binding, subjectClickListener)
            }
        }
    }
}

class SubjectDiffCallback : DiffUtil.ItemCallback<Subject>() {
    override fun areItemsTheSame(oldItem: Subject, newItem: Subject): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Subject, newItem: Subject): Boolean {
        return oldItem == newItem
    }
}

class SubjectOptionClickListener(val clickListener: (subjectTitle: String) -> Unit) {
    fun onClick(subject: Subject) = clickListener(subject.title)
}