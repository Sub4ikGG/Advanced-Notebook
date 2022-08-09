package com.efremov.advancednotebook.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.efremov.advancednotebook.R
import com.efremov.advancednotebook.data.Note
import com.efremov.advancednotebook.databinding.FragmentMainBinding
import com.efremov.advancednotebook.di.App
import com.efremov.advancednotebook.recyclerview.MainAdapter
import com.efremov.advancednotebook.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val EDIT_NOTE_ARG = "edit_note_arg"

class MainFragment : Fragment(), MainAdapter.OnNoteClickListener, EditNoteBottomSheetFragment.OnFabEventListener {

    private var _binding: FragmentMainBinding? = null
    private var adapter = MainAdapter(this)
    private val mainViewModel: MainViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity().applicationContext as App).appComponent.inject(mainViewModel)
        (requireActivity().applicationContext as App).appComponent.inject(adapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater)

        setupRecyclerView()
        setupViewModel()
        setupClickListeners()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.notesRcView.layoutManager = GridLayoutManager(context, 2)
        binding.notesRcView.adapter = adapter
    }

    private fun setupViewModel() {
        mainViewModel.update()
        mainViewModel.allData.observe(viewLifecycleOwner) { list ->
            adapter.put(list as ArrayList<Note>)
        }
    }

    private fun setupClickListeners() {
        binding.createNoteFab.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_createNoteFragment)
        }
    }

    override fun onNoteLongClick(position: Int) {
        val note = adapter.getItems()[position]
        adapter.deleteItem(note, position)
        CoroutineScope(Dispatchers.IO).launch {
            delay(500) // If not write delay(500+) then animation of deleting item will be broken
            mainViewModel.deleteNote(note)
        }

        Snackbar.make(requireView(), "Note \"${note.title}\" deleted.", Snackbar.LENGTH_SHORT)
            .setAction("Action", null).show()
    }

    override fun onNoteClick(position: Int) {
        val note = adapter.getItems()[position]

        val bundle = Bundle()
        bundle.putParcelable(EDIT_NOTE_ARG, note)

        val bottomSheet = EditNoteBottomSheetFragment(this)
        bottomSheet.arguments = bundle
        bottomSheet.show(parentFragmentManager, "Edit Note")
    }

    override fun onNoteSaved(note: Note) {
        adapter.changeItem(note)
        CoroutineScope(Dispatchers.IO).launch { mainViewModel.updateNote(note) }

        showMessage("Note '${note.title}' successfully saved.")
    }

    override fun onNoteDeleted(note: Note) {
        val position = adapter.getItems().indexOf(note)
        adapter.deleteItem(note, position)

        CoroutineScope(Dispatchers.IO).launch {
            delay(500) // If not write delay(500+) then animation of deleting item will be broken
            mainViewModel.deleteNote(note) }

        showMessage("Note '${note.title}' deleted.")
    }

    private fun showMessage(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
            .setAction("Action", null).show()
    }
}