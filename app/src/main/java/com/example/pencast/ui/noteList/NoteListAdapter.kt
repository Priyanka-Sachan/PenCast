package com.example.pencast.ui.noteList

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pencast.databinding.CardNoteListBinding
import com.example.pencast.ui.note.Note


class NoteListAdapter(private val noteClickListener: NoteClickListener) :
    ListAdapter<Note,
            NoteListAdapter.ViewHolder>(NoteListDiffCallback()) {

    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        tracker?.let {
            holder.bind(item, noteClickListener, it.isSelected(position.toLong()))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardNoteListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: CardNoteListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Note, noteClickListener: NoteClickListener, isActivated: Boolean = false) {
            binding.note = item
            binding.root.isActivated = isActivated
            binding.isSelected.isVisible=isActivated
            binding.noteClickListener = noteClickListener
            binding.executePendingBindings()
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? {
                    return getItem(adapterPosition).noteId
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

class NoteKeyProvider(private val adapter: NoteListAdapter) :
    ItemKeyProvider<Long>(SCOPE_CACHED) {
    override fun getKey(position: Int): Long? =
        adapter.currentList[position].noteId

    override fun getPosition(key: Long): Int =
        adapter.currentList.indexOfFirst { it.noteId == key }
}

class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as NoteListAdapter.ViewHolder).getItemDetails()
        }
        return null
    }
}
