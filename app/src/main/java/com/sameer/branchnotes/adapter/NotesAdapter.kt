package com.sameer.branchnotes.adapter


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.sameer.branchnotes.R
import com.sameer.branchnotes.fragment.UpdateNotesFragment
import com.sameer.branchnotes.model.Notes
import com.sameer.branchnotes.viewmodel.NotesViewModel
import java.util.Collections

class NotesAdapter(private val fragmentManager: FragmentManager,private val arr: MutableList<Notes>,private val notesViewModel: NotesViewModel) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private val colors = listOf(
        Color.parseColor("#86690E0E"),
        Color.parseColor("#860E4569"),
        Color.parseColor("#860E1D69"),
        Color.parseColor("#86310E69"),
        Color.parseColor("#86690E35"),
        Color.parseColor("#860E6917"),
        Color.parseColor("#860E6969"),
        Color.parseColor("#8669520E"),
        Color.parseColor("#8658690E"),
        Color.parseColor("#86460E69")
    )


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        var note: TextView = itemView.findViewById(R.id.desc)
        var noteCard: CardView = itemView.findViewById(R.id.noteCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.note.text = arr[position].note
        holder.title.text = arr[position].title
        val randomColor = colors[position % colors.size] // Ensures colors are repeated but evenly distributed
        holder.noteCard.setCardBackgroundColor(randomColor)

    }
    override fun getItemCount(): Int {
        return arr.size
    }

    // Function to update the list and notify changes
    fun updateNotes(newNotes: List<Notes>) {
        arr.clear()
        arr.addAll(newNotes)
        notifyDataSetChanged()
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        if (fromPosition < arr.size && toPosition < arr.size) {
            Collections.swap(arr, fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    fun removeItem(position: Int) {
        if (position < arr.size) {
            notesViewModel.deleteNote(arr[position])

            arr.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
