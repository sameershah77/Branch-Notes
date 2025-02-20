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
import com.sameer.branchnotes.databinding.FragmentUpdateNotesBinding
import com.sameer.branchnotes.model.Notes
import com.sameer.branchnotes.viewmodel.NotesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UpdateNotesFragment : Fragment() {
    private lateinit var binding : FragmentUpdateNotesBinding
    private lateinit var notesViewModel: NotesViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUpdateNotesBinding.inflate(inflater,container,false)
        val view = binding.root
        // Get data from arguments
        val id = arguments?.getInt("id",0)
        val title = arguments?.getString("title")
        val note = arguments?.getString("note")
        val time = arguments?.getString("time")

        // Set data to views
        binding.title.setText(title)
        binding.note.setText(note)
        binding.time.text = time

        animateFab()

        notesViewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]

        binding.updateNote.setOnClickListener {
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
                binding.updateNote.isEnabled = false
                val newNote = Notes(id!!, title, note, time)
                lifecycleScope.launch(Dispatchers.IO) {
                    notesViewModel.updateNote(newNote)
                    withContext(Dispatchers.Main) {
                        // Re-enable FAB after insertion is done
                        binding.updateNote.isEnabled = true
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
        binding.updateNote.setOnTouchListener { view, event ->
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