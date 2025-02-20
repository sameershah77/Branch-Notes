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
import com.sameer.branchnotes.R
import com.sameer.branchnotes.databinding.FragmentAddNotesBinding
import com.sameer.branchnotes.model.Notes
import com.sameer.branchnotes.viewmodel.NotesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNotesFragment : Fragment() {
    private lateinit var binding: FragmentAddNotesBinding
    private lateinit var notesViewModel: NotesViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddNotesBinding.inflate(inflater,container,false)
        val view = binding.root

        animateFab()
        notesViewModel = ViewModelProvider(requireActivity()).get(NotesViewModel::class.java)

        lifecycleScope.launch(Dispatchers.Default) {
            while (true) {
                val currentTime = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date())
                withContext(Dispatchers.Main) {
                    binding.time.text = currentTime
                }
                delay(1000) // Update every second
            }
        }

        binding.addNote.setOnClickListener {
            val title = binding.title.text.toString()
            val note = binding.note.text.toString()
            val time = binding.time.text.toString()
            if(title.isEmpty() && note.isEmpty()) {
                binding.title.error = "Title is required"
                binding.note.error = "Note is required"
            }
            else if(title.isEmpty()) {
                binding.title.error = "Title is required"
            } else if(note.isEmpty()) {
                binding.note.error = "Note is required"
            }
            else {
                binding.addNote.isEnabled = false

                val newNote = Notes(0, title, note, time)

                lifecycleScope.launch(Dispatchers.IO) {
                    notesViewModel.insertNote(newNote)
                    withContext(Dispatchers.Main) {
                        // Re-enable FAB after insertion is done
                        binding.addNote.isEnabled = true
                        delay(500)
                        replaceFragment(ShowNotesFragment())
                    }
                }
            }
        }

        return view
    }
    fun replaceFragment(fragment: Fragment) {
        val fragManager = requireActivity().supportFragmentManager  // Get fragment manager of the activity
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.replace(R.id.frameLayout, fragment)  // Replace the fragment inside the activity's FrameLayout
        fragTransaction.disallowAddToBackStack()
        fragTransaction.commit()
    }


    fun animateFab() {
        binding.addNote.setOnTouchListener { view, event ->
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
}