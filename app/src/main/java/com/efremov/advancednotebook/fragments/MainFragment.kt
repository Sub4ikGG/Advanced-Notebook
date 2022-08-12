package com.efremov.advancednotebook.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.efremov.advancednotebook.R
import com.efremov.advancednotebook.data.Note
import com.efremov.advancednotebook.databinding.FragmentMainBinding
import com.efremov.advancednotebook.di.App
import com.efremov.advancednotebook.recyclerview.MainAdapter
import com.efremov.advancednotebook.showSnackbarMessage
import com.efremov.advancednotebook.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


const val EDIT_NOTE_ARG = "edit_note_arg"

class MainFragment : Fragment(), MainAdapter.OnNoteClickListener,
    EditNoteBottomSheetFragment.OnFabEventListener {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var _binding: FragmentMainBinding? = null
    private var adapter = MainAdapter(this)
    private val mainViewModel: MainViewModel by viewModels()

    private val binding get() = _binding!!

    private var mIth = ItemTouchHelper(
        object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
            0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder, target: ViewHolder
            ): Boolean {
                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition

                val fromNote = adapter.getItems()[fromPos]
                val toNote = adapter.getItems()[toPos]

                adapter.swapItems(fromPos, toPos)
                val pair = mainViewModel.swapNotes(fromNote, toNote)

                CoroutineScope(Dispatchers.IO).launch {
                    mainViewModel.updateNote(pair.first)
                    mainViewModel.updateNote(pair.second)
                }

                return true // true if moved, false otherwise
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                // remove from adapter
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity().applicationContext as App).appComponent.apply {
            inject(mainViewModel)
            inject(adapter)
            inject(this@MainFragment)
        }
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
        val layoutManager = if (sharedPreferences.getString(LAYOUT_ARG, "grid") == "grid")
            GridLayoutManager(context, 2)
            else LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.notesRcView.layoutManager = layoutManager
        binding.notesRcView.adapter = adapter

        mIth.attachToRecyclerView(binding.notesRcView)
    }

    private fun setupViewModel() {
        CoroutineScope(Dispatchers.IO).launch {
            mainViewModel.update()
        }
        mainViewModel.allData.observe(viewLifecycleOwner) { list ->
            Log.d("Bug", "Received list = $list")
            if (list.size != adapter.getItems().size)
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

        showSnackbarMessage(requireView(), "Note '${note.title}' successfully saved.")
    }

    override fun onNoteDeleted(note: Note) {
        val position = adapter.getItems().indexOf(note)
        adapter.deleteItem(note, position)

        CoroutineScope(Dispatchers.IO).launch {
            delay(500) // If not write delay(500+) then animation of deleting item will be broken
            mainViewModel.deleteNote(note)
        }

        showSnackbarMessage(requireView(),"Note '${note.title}' deleted.")
    }
}