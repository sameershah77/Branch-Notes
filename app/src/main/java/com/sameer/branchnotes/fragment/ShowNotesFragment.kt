package com.sameer.branchnotes.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sameer.branchnotes.R
import com.sameer.branchnotes.adapter.NotesNewAdapter
import com.sameer.branchnotes.databinding.FragmentShowNotesBinding
import com.sameer.branchnotes.model.Notes
import com.sameer.branchnotes.utils.NotesAdapterItemClick
import com.sameer.branchnotes.viewmodel.NotesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShowNotesFragment : Fragment(), NotesAdapterItemClick {
    private lateinit var binding: FragmentShowNotesBinding
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var adapter: NotesNewAdapter
    private lateinit var notes : List<Notes>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentShowNotesBinding.inflate(inflater,container,false)
        val view = binding.root

        animateFab()

        binding.makeNote.setOnClickListener {
            binding.makeNote.isEnabled = false
            lifecycleScope.launch {
                delay(500)
                binding.makeNote.isEnabled = true
                replaceFragment(AddNotesFragment())
            }
        }

        notesViewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]
        adapter = NotesNewAdapter(this)

        binding.notesRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.notesRecyclerView.adapter = adapter

        notesViewModel.getAllNotes().observe(viewLifecycleOwner) { newNotes ->
            notes = newNotes
            adapter.submitList(newNotes)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition

                if (fromPosition != RecyclerView.NO_POSITION && toPosition != RecyclerView.NO_POSITION) {
                    adapter.moveItem(fromPosition, toPosition) // Call moveItem function
                }
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    notesViewModel.deleteNote(notes[position])
                    adapter.removeItem(position)
                }
            }

            override fun isLongPressDragEnabled(): Boolean {
                return true // Enable long-press drag
            }

            override fun isItemViewSwipeEnabled(): Boolean {
                return true // Enable swipe
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.notesRecyclerView)

        return view
    }

    fun replaceFragment(fragment: Fragment) {
        val fragManager = requireActivity().supportFragmentManager  // Get fragment manager of the activity
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.replace(R.id.frameLayout, fragment)  // Replace the fragment inside the activity's FrameLayout
        fragTransaction.addToBackStack(null)  // Optional: if you want to add this transaction to the back stack for navigation
        fragTransaction.commit()
    }

    fun animateFab() {
        binding.makeNote.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Scale down the FAB
                    ObjectAnimator.ofFloat(view, "scaleX", 0.9f).apply {
                        duration = 100
                        start()
                    }
                    ObjectAnimator.ofFloat(view, "scaleY", 0.9f).apply {
                        duration = 100
                        start()
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // Scale back the FAB to original size
                    ObjectAnimator.ofFloat(view, "scaleX", 1f).apply {
                        duration = 100
                        start()
                    }
                    ObjectAnimator.ofFloat(view, "scaleY", 1f).apply {
                        duration = 100
                        start()
                    }

                    // Perform click for accessibility
                    view.performClick()
                }
            }
            false // Continue with default click behavior
        }
    }

    override fun onNoteItemClick(note: Notes, position: Int) {
        val fragment = UpdateNotesFragment().apply {
            arguments = Bundle().apply {
                putInt("id",note.id)
                putString("title", note.title)
                putString("note", note.note)
                putString("time", note.time)
            }
        }
        val fragManager = requireActivity().supportFragmentManager  // Get fragment manager of the activity
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.replace(R.id.frameLayout, fragment)  // Replace the fragment inside the activity's FrameLayout
        fragTransaction.addToBackStack(null)  // Optional: if you want to add this transaction to the back stack for navigation
        fragTransaction.commit()
    }
}