package com.sameer.branchnotes.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.sameer.branchnotes.R
import com.sameer.branchnotes.databinding.ActivityMainBinding
import com.sameer.branchnotes.db.NotesDB
import com.sameer.branchnotes.fragment.ShowNotesFragment
import com.sameer.branchnotes.model.Notes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(ShowNotesFragment())
    }

    fun replaceFragment(fragment: Fragment) {
        val fragManager = supportFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.replace(R.id.frameLayout, fragment)
        fragTransaction.commit()
    }
}