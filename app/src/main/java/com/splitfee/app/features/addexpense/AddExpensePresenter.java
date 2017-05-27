package com.splitfee.app.features.addexpense;

import com.splitfee.app.data.dao.AppConfigDao;
import com.splitfee.app.data.usecase.ManageExpense;
import com.splitfee.app.data.usecase.viewparam.CategoryViewParam;
import com.splitfee.app.data.usecase.viewparam.ExpenseViewParam;
import com.splitfee.app.data.usecase.viewparam.PersonViewParam;
import com.splitfee.app.data.usecase.viewparam.SplitViewParam;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;
import com.splitfee.app.features.BasePresenter;
import com.splitfee.app.utils.schedulers.BaseSchedulerProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by huteri on 5/7/17.
 */

public class AddExpensePresenter extends BasePresenter<AddExpenseView> {

    private final ManageExpense manageExpense;
    private final BaseSchedulerProvider scheduler;
    private final AppConfigDao appConfig;

    private TripViewParam trip;
    private ExpenseViewParam expense;

    private String[] categoryTitles = {"Transport", "Food", "Sport", "Shopping", "Movies", "Other"};

    private List<Integer> payers = new ArrayList<>();
    private List<Integer> splits = new ArrayList<>();
    private String currency;

    @Inject
    public AddExpensePresenter(ManageExpense manageExpense, AppConfigDao appConfigDao, BaseSchedulerProvider schedulerProvider) {
        this.manageExpense = manageExpense;
        this.scheduler = schedulerProvider;
        this.appConfig = appConfigDao;
    }

    @Override
    public void onCreateView(AddExpenseView view) {
        super.onCreateView(view);

        initExpense();

        loadCategories();
        loadPayers();
        loadSplits();

        currency = appConfig.getCurrency().blockingGet();
        getView().showCurrency(currency);

        if (expense.getAmount().doubleValue() == 0)
            getView().performEditExpense();
    }

    public void setTrip(TripViewParam trip) {
        this.trip = trip;
    }

    public void setExpense(ExpenseViewParam expense) {
        this.expense = expense;
    }

    public void tapSplitExpense(int position) {

        if (expense.getAmount() == null || expense.getAmount().doubleValue() == 0) {
            getView().showAmountError();
            return;
        }
        getView().showExpenseEditDialog(expense.getSplit().get(position).getAmount(), position, expense.getAmount(), currency);
    }

    public void tapPayerExpense(int position) {

        if (expense.getAmount() == null || expense.getAmount().doubleValue() == 0) {
            getView().showAmountError();
            return;
        } else if (expense.getPayers().get(position).getAmount().compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        getView().showExpenseEditDialog(expense.getPayers().get(position).getAmount(), 1000 + position, expense.getAmount(), currency);
    }

    public void tapEditExpenseName(String s) {
        getView().showExpenseEditDialog(BigDecimal.ZERO, 100, BigDecimal.ZERO, currency);
    }

    public void doneEditExpensePerPerson(int reference, BigDecimal fullNumber) {

        if (reference >= 1000) {
            reference -= 1000;

            onEditExpenseDone(reference, fullNumber, expense.getPayers(), payers);

            getView().showPayers(expense.getPayers());
        } else if (reference == 100) {
            doneEditingAmount(fullNumber);
        } else {
            onEditExpenseDone(reference, fullNumber, expense.getSplit(), splits);

            getView().showSplits(expense.getSplit());
        }
    }

    public void tapCategory(CategoryViewParam categoryViewParam, String s) {

        if (s.length() == 0 || Arrays.asList(categoryTitles).contains(s)) {
            expense.setCategory(categoryViewParam);
            getView().showExpenseName(categoryViewParam.getName());
        }
    }

    public void tapPayerContainer(int adapterPosition) {
        onTapSplitItem(adapterPosition, expense.getPayers(), payers);

        getView().showPayers(expense.getPayers());
    }

    public void tapSplitContainer(int position) {
        onTapSplitItem(position, expense.getSplit(), splits);

        getView().showSplits(expense.getSplit());
    }

    public void tapSaveExpense(String expenseName) {

        expense.setNote(expenseName);
        if (expense.getTotalPayers().compareTo(expense.getAmount()) != 0) {
            getView().showTotalPayersNotEqual(expense.getTotalPayers().toPlainString());
            return;
        } else if (expense.getTotalSplit().compareTo(expense.getAmount()) != 0) {
            getView().showTotalSplitNotEqual(expense.getTotalSplit().toPlainString());
            return;
        }

        manageExpense.saveExpense(trip, expense)
                .subscribeOn(scheduler.computation())
                .observeOn(scheduler.ui())
                .subscribe(() -> {

                    if (!isViewAttached())
                        return;

                    getView().navigateBackToTripDetail(expense);
                });
    }

    private void onEditExpenseDone(int position, BigDecimal fullNumber, List<SplitViewParam> splits, List<Integer> lastEdit) {
        splits.get(position).setAmount(fullNumber);

        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i < splits.size(); i++) {
            total = total.add(splits.get(i).getAmount());
        }

        BigDecimal excess = total.subtract(expense.getAmount());

        for (int i = 0; i < splits.size(); i++) {
            if (i != position && !lastEdit.contains(i)) {
                BigDecimal amount = splits.get(i).getAmount();

                if (excess.doubleValue() > 0) {

                    if (amount.compareTo(excess) >= 0) {
                        BigDecimal remain = amount.subtract(excess);
                        splits.get(i).setAmount(remain);
                        excess = BigDecimal.ZERO;
                    } else if (amount.compareTo(BigDecimal.ZERO) > 0) {
                        splits.get(i).setAmount(BigDecimal.ZERO);
                        excess = excess.subtract(amount);
                    }

                } else if (excess.doubleValue() < 0) {

                    if (amount.compareTo(BigDecimal.ZERO) > 0) {
                        splits.get(i).setAmount(amount.add(excess.abs()));
                        excess = BigDecimal.ZERO;
                    }
                } else {
                    break;
                }

                if (splits.get(i).getAmount().compareTo(BigDecimal.ZERO) == 0)
                    lastEdit.remove(Integer.valueOf(i));
            }
        }

        if (excess.doubleValue() != 0) {

            for (int i = 0; i < lastEdit.size(); i++) {
                int index = lastEdit.get(i);

                if (index != position) {

                    BigDecimal amount = splits.get(index).getAmount();

                    if (excess.doubleValue() > 0) {

                        if (amount.compareTo(excess) >= 0) {
                            BigDecimal remain = amount.subtract(excess);
                            splits.get(index).setAmount(remain);
                            excess = BigDecimal.ZERO;
                        } else if (amount.compareTo(BigDecimal.ZERO) > 0) {

                            splits.get(index).setAmount(BigDecimal.ZERO);
                            excess = excess.subtract(amount);

                            lastEdit.remove(Integer.valueOf(index));
                        }
                    } else if (excess.doubleValue() < 0) {

                        if (amount.compareTo(BigDecimal.ZERO) > 0) {
                            splits.get(index).setAmount(amount.add(excess.abs()));
                            excess = BigDecimal.ZERO;
                        }
                    } else {
                        break;
                    }

                    if (splits.get(index).getAmount().compareTo(BigDecimal.ZERO) == 0)
                        lastEdit.remove(Integer.valueOf(index));
                }
            }
        }

        if (excess.doubleValue() < 0) {

            for (int i = 0; i < splits.size(); i++) {

                if (i != position) {

                    splits.get(i).setAmount(excess.abs());
                    excess = BigDecimal.ZERO;
                    break;
                }
            }
        }

        if (excess.doubleValue() != 0) {
            getView().showCantCalculate();
        }

        lastEdit.remove(Integer.valueOf(position));
        lastEdit.add(position);
    }

    private void onTapSplitItem(int adapterPosition, List<SplitViewParam> splits, List<Integer> lastEdit) {
        boolean found = false;

        List<Integer> pos = new ArrayList<>();
        BigDecimal totalCalculation = BigDecimal.ZERO;

        for (int i = 0; i < splits.size(); i++) {

            if (i != adapterPosition && splits.get(i).getAmount().compareTo(BigDecimal.ZERO) > 0 && !lastEdit.contains(i)) {
                pos.add(i);
                totalCalculation = totalCalculation.add(splits.get(i).getAmount());
            }
        }

        if (splits.get(adapterPosition).getAmount().compareTo(BigDecimal.ZERO) == 0) {

            if (pos.size() > 0) {
                pos.add(adapterPosition);

                BigDecimal[] result = divideEqually(totalCalculation, pos.size());

                for (int i = 0; i < pos.size(); i++) {
                    splits.get(pos.get(i)).setAmount(result[i]);
                }

                found = true;
            }

            if (!found) {

                Integer selected = lastEdit.get(0);
                BigDecimal current = splits.get(selected).getAmount();

                BigDecimal[] bigDecimals = divideEqually(current, 2);

                splits.get(selected).setAmount(bigDecimals[0]);
                splits.get(adapterPosition).setAmount(bigDecimals[1]);
            }
        } else {

            if (pos.size() > 0) {

                totalCalculation = totalCalculation.add(splits.get(adapterPosition).getAmount());

                BigDecimal[] result = divideEqually(totalCalculation, pos.size());

                for (int i = 0; i < pos.size(); i++) {
                    splits.get(pos.get(i)).setAmount(result[i]);
                }

                splits.get(adapterPosition).setAmount(BigDecimal.ZERO);
                found = true;
            }

            if (!found) {
                for (int i = 0; i < lastEdit.size(); i++) {
                    if (lastEdit.get(i) != adapterPosition) {
                        splits.get(lastEdit.get(i)).setAmount(splits.get(lastEdit.get(i)).getAmount().add(splits.get(adapterPosition).getAmount()));
                        splits.get(adapterPosition).setAmount(BigDecimal.ZERO);

                        lastEdit.remove(Integer.valueOf(lastEdit.get(i)));

                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                getView().showLastSplitError();
            }
        }

        if (splits.get(adapterPosition).getAmount().compareTo(BigDecimal.ZERO) == 0) {
            lastEdit.remove(Integer.valueOf(adapterPosition));
        }
    }

    private void loadCategories() {
        String[] icons = {"ic_cat_transport", "ic_cat_food_drink", "ic_cat_sport", "ic_cat_shopping", "ic_cat_cinema", "ic_cat_other"};

        List<CategoryViewParam> categories = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            categories.add(new CategoryViewParam(icons[i], categoryTitles[i]));
        }

        getView().showCategories(categories);

        if (expense.getCategory() == null) {
            expense.setCategory(categories.get(0));
            getView().showExpenseName(categories.get(0).getName());
        }
    }

    private void loadPayers() {

        getView().showPayers(expense.getPayers());
    }

    private void loadSplits() {
        getView().showSplits(expense.getSplit());
    }

    private void initExpense() {
        if (expense != null) {
            getView().showExpenseName(expense.getNote());
            getView().showAmount(expense.getAmount());
            getView().selectCategory(expense.getCategory());
            return;
        }

        List<SplitViewParam> split = new ArrayList<>();
        List<SplitViewParam> payers = new ArrayList<>();

        for (PersonViewParam personViewParam : trip.getPersons()) {
            split.add(new SplitViewParam(personViewParam, 0));
            payers.add(new SplitViewParam(personViewParam, 0));
        }

        expense = new ExpenseViewParam();
        expense.setSplit(split);
        expense.setPayers(payers);
    }

    private void doneEditingAmount(BigDecimal amount) {
        amount = amount.setScale(appConfig.getMinimumMoney().blockingGet(), RoundingMode.FLOOR);

        expense.setAmount(amount);

        BigDecimal[] divides = divideEqually(amount, expense.getSplit().size());

        for (int i = 0; i < expense.getSplit().size(); i++) {
            expense.getSplit().get(i).setAmount(divides[i]);
        }

        getView().showSplits(expense.getSplit());

        expense.getPayers().get(0).setAmount(amount);
        for (int i = 1; i < expense.getPayers().size(); i++) {
            expense.getPayers().get(i).setAmount(BigDecimal.ZERO);
        }

        getView().showPayers(expense.getPayers());

        getView().showAmount(amount);
    }

    private BigDecimal[] divideEqually(BigDecimal total, int count) {

        total = total.setScale(appConfig.getMinimumMoney().blockingGet(), RoundingMode.FLOOR);

        BigDecimal splitCount = BigDecimal.valueOf(count);

        BigDecimal perPerson = total.divide(splitCount, RoundingMode.FLOOR);

        BigDecimal remainder = total.subtract(perPerson.multiply(splitCount));

        BigDecimal[] result = new BigDecimal[count];
        for (int i = 0; i < count; i++) {

            if (i == 0) {
                result[i] = perPerson.add(remainder);
            } else {
                result[i] = perPerson;
            }
        }

        return result;
    }
}
