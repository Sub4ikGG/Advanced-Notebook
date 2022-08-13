package com.efremov.advancednotebook.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.efremov.advancednotebook.databinding.FragmentAboutAppBinding


class AboutAppFragment : Fragment() {

    private var _binding: FragmentAboutAppBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAboutAppBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        binding.apply {
            githubCardView.setOnClickListener {
                openUrl("https://github.com/Sub4ikGG")
            }

            hhCardView.setOnClickListener {
                openUrl("https://hh.ru/resume/0e51471eff09adf50c0039ed1f516242497943")
            }

            telegramCardView.setOnClickListener {
                openUrl("https://t.me/sub4ikgg")
            }
        }
    }

    private fun openUrl(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
}