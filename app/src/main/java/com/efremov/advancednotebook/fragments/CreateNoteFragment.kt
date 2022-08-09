package com.efremov.advancednotebook.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.efremov.advancednotebook.R
import com.efremov.advancednotebook.data.Note
import com.efremov.advancednotebook.data.Remind
import com.efremov.advancednotebook.databinding.FragmentCreateNoteBinding
import com.efremov.advancednotebook.di.App
import com.efremov.advancednotebook.room.NoteRepository
import com.efremov.advancednotebook.viewpager2.ColorAdapter
import com.efremov.advancednotebook.viewpager2.FontAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [CreateNoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateNoteFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private var _binding: FragmentCreateNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var colorAdapter: ColorAdapter
    private lateinit var fontAdapter: FontAdapter
    private lateinit var colorViewPager: ViewPager2
    private lateinit var fontViewPager: ViewPager2

    @Inject
    lateinit var repository: NoteRepository

    private val scope = CoroutineScope(Dispatchers.IO + CoroutineName("CreateNoteFragment-scope"))
    private var colors = listOf(
        R.color.default_note_color,
        R.color.green_note_color,
        R.color.blue_note_color,
        R.color.pink_note_color,
        R.color.red_note_color
    )
    private var fonts = listOf(
        "sans-serif",
        "sans-serif-light",
        "sans-serif-thin",
        "monospace",
        "cursive",
        "sans-serif-smallcaps",
        "serif",
        //"casual",
        //"serif-monospace"
    )
    private var currentColor = colors[0]
    private var currentFont = fonts[0]

    private var remind = Remind(-1, -1, -1, -1, -1)
    private var scaled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity().applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateNoteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()

        colorAdapter = ColorAdapter(requireActivity()); fontAdapter = FontAdapter(requireActivity())
        colorAdapter.put(colors); fontAdapter.put(fonts)

        binding.timeNote.text = getDateByTimestamp(System.currentTimeMillis() / 1000L)

        colorViewPager = binding.topColorViewPager; fontViewPager = binding.fontViewPager
        colorViewPager.adapter = colorAdapter; fontViewPager.adapter = fontAdapter

        scope.launch {
            while (!this@CreateNoteFragment.isVisible) delay(500)
            while (this@CreateNoteFragment.isVisible) {
                if (colors[colorViewPager.currentItem] != currentColor) {
                    currentColor = colors[colorViewPager.currentItem]
                    launch(Dispatchers.Main) {
                        changePreviewFabColor()

                        fontViewPager.animate().apply {
                            duration = 300
                            alpha(0f)
                        }.withEndAction {
                            val fontPosition = fontViewPager.currentItem
                            fontAdapter.setColor(currentColor)
                            fontViewPager.adapter = fontAdapter
                            fontViewPager.currentItem = fontPosition
                            fontViewPager.animate().apply {
                                duration = 300
                                alpha(1f)
                            }
                        }
                    }
                } else if (fonts[fontViewPager.currentItem] != currentFont) {
                    currentFont = fonts[fontViewPager.currentItem]
                    launch(Dispatchers.Main) {
                        changePreviewFont()

                        colorViewPager.animate().apply {
                            duration = 300
                            alpha(0f)
                        }.withEndAction {
                            val colorPosition = colorViewPager.currentItem
                            colorAdapter.setFont(currentFont)
                            colorViewPager.adapter = colorAdapter
                            colorViewPager.currentItem = colorPosition
                            colorViewPager.animate().apply {
                                duration = 300
                                alpha(1f)
                            }
                        }
                    }
                } else delay(200)
            }
        }
    }

    private fun setupClickListeners() {
        binding.createFab.setOnClickListener {
            binding.createFab.press {
                if (binding.titleNote.text.isEmpty()) showMessage("Error: note title is empty.")
                else if (binding.textNote.text.isEmpty()) showMessage("Error: note content is empty.")
                else {

                    val note = prepareNote(
                        binding.titleNote.text.toString(),
                        binding.textNote.text.toString(),
                        currentColor,
                        currentFont
                    )

                    try {
                        scope.launch { repository.insertNote(note) }
                        showMessage("Successfully create note.")
                        findNavController().popBackStack()
                    } catch (e: Exception) {
                        Log.d("Try-catch tracker", "${e.message}")
                        showMessage("Error: can`t create note.")
                    }

                }
            }
        }

        binding.qaFab.setOnClickListener {
            binding.qaFab.press {
                Snackbar.make(requireView(), R.string.preview_help, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
            }
        }

        binding.setReminderFab.setOnClickListener {
            binding.setReminderFab.press {
                val calendar = Calendar.getInstance()
                DatePickerDialog(
                    requireContext(),
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }

        binding.removeDateFab.setOnClickListener {
            binding.removeDateFab.press {
                when (binding.timeNote.visibility) {
                    View.VISIBLE -> binding.timeNote.visibility = View.INVISIBLE
                    View.INVISIBLE -> binding.timeNote.visibility = View.GONE
                    else -> binding.timeNote.visibility = View.VISIBLE
                }
            }
        }

        binding.reminderImageView.setOnClickListener {
            binding.reminderCardView.press {
                remind = Remind(-1, -1, -1, -1, -1)
                binding.reminderCardView.visibility = View.GONE
                Snackbar.make(requireView(), R.string.reminder_deleted, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
            }
        }

        binding.shareNoteFab.setOnClickListener {
            binding.shareNoteFab.press {
                if (binding.titleNote.text.isEmpty()) showMessage("Error: note title is empty.")
                else if (binding.textNote.text.isEmpty()) showMessage("Error: note content is empty.")
                else {
                    val title = binding.titleNote.text.toString()
                    val content = binding.textNote.text.toString()
                    val date = binding.timeNote.text.toString()

                    val shareIntent = Intent().apply {
                        this.action = Intent.ACTION_SEND
                        this.putExtra(Intent.EXTRA_TEXT, "$title\n\n$content\n\nWritten - $date")
                        this.type = "text/plain"
                    }

                    ContextCompat.startActivity(requireContext(), shareIntent, Bundle())
                }
            }
        }

        binding.topBarNote.setOnClickListener {
            if (!scaled)
                binding.previewNote.animate().apply {
                    duration = 1000
                    translationY(100f)
                    scaleX(1.6f)
                    scaleY(1.6f)
                }
            else
                binding.previewNote.animate().apply {
                    duration = 1000
                    translationY(0f)
                    scaleX(1f)
                    scaleY(1f)
                }
            scaled = !scaled
        }
    }

    private fun prepareNote(
        title: String,
        content: String,
        color: Int,
        font: String,
    ): Note {
        val date = when (binding.timeNote.visibility) {
            View.VISIBLE -> binding.timeNote.text.toString()
            View.INVISIBLE -> "0"
            View.GONE -> "-1"
            else -> {
                ""
            }
        }

        return Note(0, title, content, date, color, font)
    }

    private fun getDateByTimestamp(timestamp: Long): String {
        val simpleDate = SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.KOREA)

        return simpleDate.format(Date(timestamp * 1000))
    }

    private fun changePreviewFont() {
        val typeface = Typeface.create(currentFont, Typeface.NORMAL)
        binding.titleNote.typeface = typeface
        binding.textNote.typeface = typeface
        binding.timeNote.typeface = typeface
        binding.reminderTextView.typeface = typeface
    }

    private fun changePreviewFabColor() {
        val color = ContextCompat.getColor(requireContext(), currentColor)
        binding.topBarNote.setCardBackgroundColor(color)
        binding.createFab.backgroundTintList = ColorStateList.valueOf(color)
        binding.qaFab.backgroundTintList = ColorStateList.valueOf(color)
        binding.setReminderFab.backgroundTintList = ColorStateList.valueOf(color)
        binding.removeDateFab.backgroundTintList = ColorStateList.valueOf(color)
        binding.shareNoteFab.backgroundTintList = ColorStateList.valueOf(color)
        binding.reminderImageView.backgroundTintList = ColorStateList.valueOf(color)
    }

    private fun View.press(qux: () -> Unit) {
        this.animate().apply {
            duration = 200
            scaleX(0.8f)
            scaleY(0.8f)
        }.withEndAction {
            qux()
            this.animate().apply {
                duration = 200
                scaleX(1f)
                scaleY(1f)
            }
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
            .setAction("Action", null).show()
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dath: Int) {
        remind.year = year; remind.month = month; remind.dayOfMonth = dath

        val calendar = Calendar.getInstance()
        TimePickerDialog(
            requireContext(),
            this,
            calendar.get(Calendar.HOUR),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        remind.hour = hour; remind.minute = minute

        val text =
            "Reminder:\n- I will remind you of this note at " +
                    "${if (remind.hour < 10) "0${remind.hour}" else remind.hour}:" +
                    "${if (remind.minute < 10) "0${remind.minute}" else remind.minute} on " +
                    "${if (remind.dayOfMonth < 10) "0${remind.dayOfMonth}" else remind.dayOfMonth}." +
                    "${if (remind.month < 10) "0${remind.month}" else remind.month}." +
                    "${remind.year}"
        binding.reminderTextView.text = text

        binding.reminderCardView.animate().apply {
            duration = 0
            alpha(0f)
            translationX(-1000f)

            binding.reminderCardView.visibility = View.VISIBLE
        }.withEndAction {
            binding.reminderCardView.animate().apply {
                duration = 700
                alpha(1f)
                translationX(1f)
            }
        }

        /*Snackbar.make(requireView(), R.string.reminder_created, Snackbar.LENGTH_SHORT)
            .setAction("Action", null).show()*/
    }

    companion object {
        @JvmStatic
        fun newInstance() = CreateNoteFragment()
    }
}