package com.efremov.advancednotebook.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.efremov.advancednotebook.R
import com.efremov.advancednotebook.data.QAModel
import com.efremov.advancednotebook.databinding.FragmentHelpBinding
import com.efremov.advancednotebook.qaList
import com.efremov.advancednotebook.recyclerview.QAAdapter


class HelpFragment : Fragment() {

    private var _binding: FragmentHelpBinding? = null
    private var adapter = QAAdapter()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHelpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        adapter.put(qaList)
    }

    private fun setupRecyclerView() {
        binding.helpRcView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.helpRcView.adapter = adapter
    }
}