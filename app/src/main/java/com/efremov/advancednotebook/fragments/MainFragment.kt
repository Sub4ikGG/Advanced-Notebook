package com.efremov.advancednotebook.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.efremov.advancednotebook.R
import com.efremov.advancednotebook.data.Note
import com.efremov.advancednotebook.databinding.FragmentMainBinding
import com.efremov.advancednotebook.di.App
import com.efremov.advancednotebook.recyclerview.MainAdapter
import com.efremov.advancednotebook.viewmodel.MainViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private var adapter = MainAdapter()
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
    ): View? {
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

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}