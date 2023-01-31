package com.hyonglow.tippy

import android.animation.ArgbEvaluator
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hyonglow.tippy.R

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
    lateinit var etSplit: EditText
    lateinit var etBaseAmount: EditText
    lateinit var tvTipDescription: TextView
    lateinit var tvTipPercentLabel: TextView
    lateinit var tvTipAmount: TextView
    lateinit var tvTotalAmount: TextView
    lateinit var seekBarTip: SeekBar

    private val INITIAL_TIP_PERCENT = 15

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        etBaseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateTip()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        etSplit = findViewById(R.id.etSplit)
        etSplit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateTip()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        tvTipDescription = findViewById(R.id.tvTipDescription)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipPercentLabel.text = "${INITIAL_TIP_PERCENT}%"
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        seekBarTip.progress = INITIAL_TIP_PERCENT
        seekBarTip.setOnSeekBarChangeListener(this)
        updateTipDescription(INITIAL_TIP_PERCENT)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        tvTipPercentLabel.text = "$progress%"
        Log.d("MainActivity", "progress=" + progress);
        Log.d("MainActivity", "tipPercent=" + progress);
        calculateTip()
        updateTipDescription(progress)
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0 until 10 -> "POOR"
            in 10 until 15 -> "ACCEPTABLE"
            in 15 until 20 -> "GOOD"
            in 20 until 25 -> "GREAT"
            else -> "AMAZING"
        }
        tvTipDescription.text = tipDescription

        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBarTip.max,
            getColor(R.color.color_worst_tip),
            getColor(R.color.color_best_tip),
        ) as Int

        val tipDescriptionColor = when (tipPercent) {
            in 0 until 10 -> Color.RED
            in 10 until 15 -> Color.CYAN
            in 15 until 20 -> Color.GREEN
            in 20 until 25 -> Color.MAGENTA
            else -> Color.BLUE
        }
        tvTipDescription.setTextColor(color)
    }

    private fun calculateTip() {
        var tipPercent = seekBarTip.progress
        var bill = if (etBaseAmount.text.isNullOrBlank()) 0.0
        else etBaseAmount.text.toString().toDouble()
        var split = if (etSplit.text.isNullOrBlank()) 1
        else etSplit.text.toString().toInt()
        var tipAmount = bill * tipPercent / 100
        tvTipAmount.text = String.format("$ %.2f ", tipAmount / split)
        tvTotalAmount.text = String.format("$ %.2f ", (bill + tipAmount) / split)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

}