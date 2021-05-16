package dizel.budget_control.budget.view.create_category

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import dizel.budget_control.R
import dizel.budget_control.budget.domain.Currency
import dizel.budget_control.budget.view.create_budget.CreateBudgetViewModel
import dizel.budget_control.custom.ColorPicker
import dizel.budget_control.databinding.FragmentCreateCategoryBinding
import dizel.budget_control.utils.ResultRequest
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CreateCategoryFragment : Fragment(R.layout.fragment_create_category) {

    private val viewModel by viewModel<CreateCategoryViewModel>()

    private var _binding: FragmentCreateCategoryBinding? = null
    private val binding get() = _binding!!

    private var colorCategory = Color.WHITE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateCategoryBinding.bind(view).apply {
            vColorPick.setOnClickListener { openColorPicker() }
            vSubmitButton.setOnClickListener { createCategory() }

            val adapter = ArrayAdapter(
                view.context,
                android.R.layout.simple_spinner_dropdown_item,
                Currency.values().map { "${it.name} - ${it.symbol}" }
            )
            vSpinnerCurrency.adapter = adapter
        }
        setUpToolbar()
    }

    private fun createCategory() {
        val name = binding.vNameCategory.text.toString().ifEmpty { null }
        val money = binding.vMoneyCategory.text.toString().toLongOrNull()

        if (name == null || money == null) {
            showError(getString(R.string.invalidate_fields))
            return
        }

        val currency = Currency.values()[binding.vSpinnerCurrency.selectedItemPosition]
        val params = CreateCategoryViewModel.Params(
            title = name,
            color = colorCategory,
            money = money,
            currency = currency
        )



        viewModel.createCategory(params).observe(viewLifecycleOwner) {
            when (it) {
                is ResultRequest.Success -> {
                    parentFragmentManager.popBackStack()
                }
                is ResultRequest.Error -> {
                    showError(it.exception.message ?: getString(R.string.unknown_error))
                }
                is ResultRequest.Loading -> {  }
            }
        }
    }

    private fun setUpToolbar() {
        with(binding.vToolBar) {
            (requireActivity() as AppCompatActivity).setSupportActionBar(this)
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener { requireActivity().onBackPressed() }
        }
    }

    private fun openColorPicker() {
        ColorPicker(context).apply {
            setOnDismissListener {
                val colorState = ColorStateList.valueOf(color)
                binding.vColorPick.backgroundTintList = colorState
            }
            show()
        }
    }

    private fun showError(mes: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.error_stub_title)
            .setMessage(mes)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    companion object {
        const val FRAGMENT_NAME = "CreateCategoryFragment"
    }
}