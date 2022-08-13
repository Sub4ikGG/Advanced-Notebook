package com.efremov.advancednotebook.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.efremov.advancednotebook.R
import com.efremov.advancednotebook.data.QAModel
import com.efremov.advancednotebook.databinding.QuestionAnswerItemBinding

class QAAdapter : RecyclerView.Adapter<QAAdapter.ViewHolder>() {
    private var data = ArrayList<QAModel>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = QuestionAnswerItemBinding.bind(view)

        fun bind(model: QAModel) {
            val question = "Q: ${model.question}?"
            binding.questionTextView.text = question
            binding.answerTextView.text = model.answer

            binding.selectorQuestionAnswer.setOnClickListener {
                binding.selectorQuestionAnswer.animate().apply {
                    duration = 300
                    val rotation = if (binding.answerTextView.visibility == View.GONE) 180f else 0f
                    rotationX(rotation)
                }

                binding.answerTextView.visibility =
                    if (binding.answerTextView.visibility == View.GONE) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QAAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.question_answer_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: QAAdapter.ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun put(data: ArrayList<QAModel>) {
        this.data = data
        notifyDataSetChanged()
    }
}