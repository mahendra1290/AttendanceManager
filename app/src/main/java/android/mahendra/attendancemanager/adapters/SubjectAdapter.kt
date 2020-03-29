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

class SubjectAdapter(private val subjectClickListener: SubjectClickListener) : ListAdapter<Subject, SubjectAdapter.SubjectViewHolder>(SubjectDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        return SubjectViewHolder.from(parent, subjectClickListener)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SubjectViewHolder private constructor(
            private val binding: ListItemSubjectBinding,
            private val subjectClickListener: SubjectClickListener
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private var subject: Subject? = null
        fun bind(subject: Subject) {
            Timber.i("\nbind called for $subject")
            this.subject = subject
            binding.subjectDetailViewModel!!.subject = subject
            binding.invalidateAll()
            binding.executePendingBindings()
        }

        override fun onClick(v: View) {
            subjectClickListener.onSubjectOptionClick(subject!!)
        }

        init {
            Timber.i("View model created")
            val subjectDetailViewModel =
                    InjectorUtils.provideSubjectDetailViewModel(binding.root.context)
            binding.subjectDetailViewModel = subjectDetailViewModel
            binding.moreOptions.setOnClickListener(this)
        }

        companion object {
            fun from(parent: ViewGroup, subjectClickListener: SubjectClickListener): SubjectViewHolder {
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

abstract class SubjectClickListener {
    abstract fun onSubjectOptionClick(subject: Subject)
}