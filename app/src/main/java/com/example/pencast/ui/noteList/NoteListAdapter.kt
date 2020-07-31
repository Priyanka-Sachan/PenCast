package com.example.pencast.ui.noteList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pencast.databinding.CardAboutMeArticleBinding
import com.example.pencast.databinding.CardNoteListBinding
import com.example.pencast.ui.article.ArticleInfo
import com.example.pencast.ui.note.Note

class NoteListAdapter(private val noteClickListener: NoteClickListener) :
    ListAdapter<Note,
            NoteListAdapter.ViewHolder>(NoteListDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, noteClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: CardNoteListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Note, noteClickListener: NoteClickListener) {
            binding.note = item
            binding.noteClickListener = noteClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardNoteListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class NoteListDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.noteId == newItem.noteId
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}

class NoteClickListener(val noteClickListener: (note: Note) -> Unit) {
    fun onClick(note: Note) = noteClickListener(note)
}
