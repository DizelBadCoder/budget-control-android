package dizel.budget_control.budget.view.create_budget

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dizel.budget_control.R
import dizel.budget_control.budget.domain.entity.Currency
import dizel.budget_control.budget.view.budget_details.BudgetDetailsFragment
import dizel.budget_control.budget.view.budget_list.BudgetListFragment
import dizel.budget_control.databinding.FragmentCreateBudgetBinding
import dizel.budget_control.core.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

@Composable
fun CreateBudgetScreen(viewModel: CreateBudgetViewModel = viewModel()) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val currencyItems = Currency.values().map { "${it.name} - ${it.symbol}" }
    var selectedCurrency by remember { mutableStateOf(currencyItems.first()) }
    var title by remember { mutableStateOf("") }
    var money by remember { mutableStateOf("") }

    CreateBudgetScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CreateBudgetTopBar()
        },
        content = {
            Column(modifier = Modifier.padding(16.dp)) {
                BudgetTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(id = R.string.title)) }
                )
                AddSpacer()
                BudgetTextField(
                    value = money,
                    onValueChange = { money = it },
                    label = { Text(stringResource(id = R.string.money)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                AddSpacer()
                BudgetDropdownMenu(
                    items = currencyItems,
                    selectedItem = selectedCurrency,
                    onItemSelected = { selectedCurrency = it }
                )
                AddSpacer()
                BudgetButton(onClick = { createBudget(title, money, selectedCurrency, viewModel, context, scaffoldState) }) {
                    Text(text = stringResource(id = R.string.submit))
                }
            }
        }
    )
}

    fun navigateToBudgetDetails(navController: NavController, id: String) {
        navController.navigate("budgetDetails/$id")
    }

    private fun createBudget() {
        val title = binding.vNameBudget.text.toString().ifEmpty { null }
        val money = binding.vMoneyBudget.text.toString().toDoubleOrNull()

        if (title == null || money == null) {
            showError(getString(R.string.invalidate_fields))
            return
        }

        val currency = Currency.values()[binding.vSpinnerCurrency.selectedItemPosition]
        val params = CreateBudgetViewModel.NewBudgetParams(title, money, currency)

        viewModel.createBudget(params).observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultRequest.Success -> {
                    val id = result.data
                    navigateToBudgetDetails(id)
                }
                is ResultRequest.Error -> {
                    showError(result.exception.message ?: getString(R.string.unknown_error))
                }
                is ResultRequest.Loading -> { }
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

    @Composable
fun ErrorDialog(message: String, openDialog: MutableState<Boolean>) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = stringResource(id = R.string.error_stub_title)) },
            text = { Text(text = message) },
            confirmButton = {
                BudgetButton(onClick = { openDialog.value = false }) {
                    Text(text = stringResource(id = R.string.ok))
                }
            }
        )
    }
}

@Composable
fun CreateBudgetScaffold(
    scaffoldState: ScaffoldState,
    topBar: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = topBar
    ) {
        content()
    }
}
}