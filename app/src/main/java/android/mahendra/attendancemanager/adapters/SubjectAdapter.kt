package android.mahendra.attendancemanager.adapters

import android.content.Context
import android.mahendra.attendancemanager.R
import android.mahendra.attendancemanager.databinding.ListItemSubjectBinding
import android.mahendra.attendancemanager.models.Subject
import android.mahendra.attendancemanager.utilities.InjectorUtils
import android.mahendra.attendancemanager.viewmodels.subject.SubjectDetailViewModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class SubjectAdapter(
        private val callbacks: Callbacks
) : ListAdapter<Subject, SubjectAdapter.SubjectViewHolder>(SubjectDiffCallback()) {
    private lateinit var context: Context

    interface Callbacks {
        fun onSubjectOptionClicked(subject: Subject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ListItemSubjectBinding>(
                inflater, R.layout.list_item_subject, parent, false)
        context = parent.context
        return SubjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SubjectViewHolder(
            private val binding: ListItemSubjectBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private var subject: Subject? = null
        fun bind(subject: Subject) {
            this.subject = subject
            binding.subjectDetailViewModel!!.subject = subject
            binding.invalidateAll()
            binding.notifyChange()
            binding.executePendingBindings()
        }

        override fun onClick(v: View) {
            this@SubjectAdapter.callbacks.onSubjectOptionClicked(subject!!)

        }

        init {
            val detailViewModel: SubjectDetailViewModel =
                    InjectorUtils.provideSubjectDetailViewModel(context)
            binding.subjectDetailViewModel = detailViewModel
            binding.moreOptions.setOnClickListener(this)
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
