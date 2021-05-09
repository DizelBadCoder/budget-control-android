package dizel.budget_control.custom

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatDialog
import dizel.budget_control.R
import dizel.budget_control.databinding.DialogColorPickerBinding

class ColorPicker(
    context: Context?
): AppCompatDialog(context), SeekBar.OnSeekBarChangeListener {

    private var _binding: DialogColorPickerBinding? = null
    private val binding = _binding!!

    var color = Color.WHITE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.dialog_color_picker, null)
        _binding = DialogColorPickerBinding.bind(view).apply {
            vSeekBarColorBlue.setOnSeekBarChangeListener(this@ColorPicker)
            vSubmitButton.setOnClickListener { dismiss() }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.id) {
            R.id.vSeekBarColorRed -> {
                plusColor(red = progress)
                binding.vTextValueRed.text = progress.toString()
            }
            R.id.vSeekBarColorGreen -> {
                plusColor(green = progress)
                binding.vTextValueGreen.text = progress.toString()
            }
            R.id.vSeekBarColorBlue -> {
                plusColor(blue = progress)
                binding.vTextValueBlue.text = progress.toString()
            }
        }
    }

    private fun plusColor(
        red: Int = 0,
        green: Int = 0,
        blue: Int = 0
    ) {
        color = Color.rgb(red, green, blue)
        binding.vColorBox.setBackgroundColor(color)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}