package com.efremov.advancednotebook.fragments

import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.efremov.advancednotebook.R
import com.efremov.advancednotebook.data.Settings
import com.efremov.advancednotebook.data.noteColors
import com.efremov.advancednotebook.data.noteFonts
import com.efremov.advancednotebook.databinding.FragmentSettingsBinding
import com.efremov.advancednotebook.di.App
import com.efremov.advancednotebook.showSnackbarMessage
import javax.inject.Inject

const val STORAGE_NAME = "settings"

const val LAYOUT_ARG = "layout"
const val CONFIRM_ARG = "agreement"
const val PAINT_ARG = "paint_environment"

const val BASE_COLOR_ARG = "base_color"
const val BASE_FONT_ARG = "base_font"

class SettingsFragment : Fragment() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var _binding: FragmentSettingsBinding? = null
    private var settings: Settings = Settings()

    private val binding get() = _binding!!

    private var colorOn = 0
    private var colorOf = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Filling Settings from Shared Preferences
        (requireActivity().applicationContext as App).appComponent.inject(this)

        settings.layout = sharedPreferences.getString(LAYOUT_ARG, "grid").toString()
        settings.confirm = sharedPreferences.getBoolean(CONFIRM_ARG, false)
        settings.paintEnvironment = sharedPreferences.getBoolean(PAINT_ARG, false)

        settings.baseColor = sharedPreferences.getInt(BASE_COLOR_ARG, R.color.default_note_color)
        settings.baseFont = sharedPreferences.getString(BASE_FONT_ARG, "sans-serif").toString()

        colorOn = ContextCompat.getColor(requireContext(), R.color.green_note_color)
        colorOf = ContextCompat.getColor(requireContext(), R.color.default_note_color)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(layoutInflater)

        binding.apply {
            changeNoteLayoutFab.backgroundTintList =
                ColorStateList.valueOf(if (settings.layout == "grid") colorOn else colorOf)
            turnConfirmFab.backgroundTintList =
                ColorStateList.valueOf(if (settings.confirm) colorOn else colorOf)
            paintEnvironmentFab.backgroundTintList =
                ColorStateList.valueOf(if (settings.paintEnvironment) colorOn else colorOf)

            val fabColor = ContextCompat.getColor(requireContext(), settings.baseColor)
            changeBaseColorFab.backgroundTintList =
                ColorStateList.valueOf(fabColor)

            val typeface = Typeface.create(settings.baseFont, Typeface.NORMAL)
            featuresTextView.typeface = typeface
        }

        setupOnClickListeners()
        setupOnLongClickListeners()

        return binding.root
    }

    private fun setupOnClickListeners() {
        binding.apply {
            changeNoteLayoutFab.setOnClickListener {
                changeNoteLayoutFab.press {
                    settings.layout = if (settings.layout == "grid") "no-grid" else "grid"
                    sharedPreferences.edit().apply {
                        putString(LAYOUT_ARG, settings.layout)
                        apply()
                    }

                    changeNoteLayoutFab.backgroundTintList =
                        ColorStateList.valueOf(if (settings.layout == "grid") colorOn else colorOf)
                    showSnackbarMessage(requireView(), "Now 'Layout' is ${settings.layout}")
                }
            }

            turnConfirmFab.setOnClickListener {
                turnConfirmFab.press {
                    settings.confirm = !settings.confirm
                    sharedPreferences.edit().apply {
                        putBoolean(CONFIRM_ARG, settings.confirm)
                        apply()
                    }

                    turnConfirmFab.backgroundTintList =
                        ColorStateList.valueOf(if (settings.confirm) colorOn else colorOf)
                    showSnackbarMessage(
                        requireView(),
                        "Now 'Action Confirm' is ${if (settings.confirm) "on" else "off"}"
                    )
                }
            }

            paintEnvironmentFab.setOnClickListener {
                paintEnvironmentFab.press {
                    settings.paintEnvironment = !settings.paintEnvironment
                    sharedPreferences.edit().apply {
                        putBoolean(PAINT_ARG, settings.paintEnvironment)
                        apply()
                    }

                    paintEnvironmentFab.backgroundTintList =
                        ColorStateList.valueOf(if (settings.paintEnvironment) colorOn else colorOf)
                    showSnackbarMessage(
                        requireView(),
                        "Now 'Paint Environment' is ${if (settings.paintEnvironment) "on" else "off"}"
                    )
                }
            }

            changeBaseColorFab.setOnClickListener {
                changeBaseColorFab.press {
                    val newIndex = noteColors.indexOf(settings.baseColor) + 1
                    if (newIndex != noteColors.size)
                        settings.baseColor = noteColors[noteColors.indexOf(settings.baseColor) + 1]
                    else
                        settings.baseColor = noteColors[0]

                    val fabColor = ContextCompat.getColor(requireContext(), settings.baseColor)
                    changeBaseColorFab.backgroundTintList =
                        ColorStateList.valueOf(fabColor)

                    sharedPreferences.edit().apply {
                        putInt(BASE_COLOR_ARG, settings.baseColor)
                        apply()
                    }

                    showSnackbarMessage(requireView(), "Base color is changed")
                }
            }

            changeBaseFontFab.setOnClickListener {
                changeBaseFontFab.press {
                    val newIndex = noteFonts.indexOf(settings.baseFont) + 1
                    if (newIndex != noteFonts.size)
                        settings.baseFont = noteFonts[noteFonts.indexOf(settings.baseFont) + 1]
                    else
                        settings.baseFont = noteFonts[0]

                    val fabTypeface = Typeface.create(settings.baseFont, Typeface.NORMAL)
                    featuresTextView.typeface = fabTypeface

                    sharedPreferences.edit().apply {
                        putString(BASE_FONT_ARG, settings.baseFont)
                        apply()
                    }

                    showSnackbarMessage(
                        requireView(),
                        "Now your base font is '${settings.baseFont}'"
                    )
                }
            }
        }
    }

    private fun setupOnLongClickListeners() {
        binding.apply {
            changeNoteLayoutFab.setOnLongClickListener {
                showSnackbarMessage(requireView(), "Another display of notes")
                true
            }

            turnConfirmFab.setOnLongClickListener {
                showSnackbarMessage(requireView(), "Turn on/of action confirmation ")
                true
            }

            paintEnvironmentFab.setOnLongClickListener {
                showSnackbarMessage(
                    requireView(),
                    "Paint your environment when you select features"
                )
                true
            }

            changeBaseColorFab.setOnLongClickListener {
                showSnackbarMessage(requireView(), "Change base color of your note")
                true
            }

            changeBaseFontFab.setOnLongClickListener {
                showSnackbarMessage(requireView(), "Change base font of your note")
                true
            }

            removeAdFab.setOnLongClickListener {
                showSnackbarMessage(requireView(), "Soon")
                true
            }
        }
    }

    private fun View.press(qux: () -> Unit) {
        this.animate().apply {
            qux()
            duration = 200
            scaleX(0.8f)
            scaleY(0.8f)
        }.withEndAction {
            this.animate().apply {
                duration = 200
                scaleX(1f)
                scaleY(1f)
            }
        }
    }
}