package com.example.distributoreautomatico.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.distributoreautomatico.R;
import com.example.distributoreautomatico.business.Drink;
import com.example.distributoreautomatico.dao.DaoDrink;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Locale;

public class DrinkAdminAdapter extends BaseAdapter {
    private final Context CONTEXT;
    private List<Drink> drinks;
    private final DaoDrink daoDrink;

    public DrinkAdminAdapter(Context context, List<Drink> drinks) {
        this.CONTEXT = context;
        this.drinks = drinks;

        this.daoDrink = DaoDrink.getInstance(CONTEXT);
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return drinks.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return drinks.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return drinks.get(position).hashCode();
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(CONTEXT).inflate(R.layout.drink_row_admin, parent, false);

        // get the drink from the current position
        Drink drink = (Drink)getItem(position);

        // get the elements of a single row and setting parameters based on the drink
        EditText drinkName = convertView.findViewById(R.id.drink_name_edit_text);
        drinkName.setText(drink.getName());

        EditText drinkCost = convertView.findViewById(R.id.drink_cost_edit_text);
        drinkCost.setText(String.format(Locale.getDefault(), "%.2f", drink.getCost()));

        EditText drinkQuantity = convertView.findViewById(R.id.drink_quantity_edit_text);
        drinkQuantity.setText(String.format(Locale.getDefault(), "%d", drink.getQuantity()));

        ImageButton editButton = convertView.findViewById(R.id.drink_edit_button);
        editButton.setOnClickListener(v -> {
            // if information in the edit texts is not valid don't go further
            if (isDrinkValid(drinkName, drinkCost, drinkQuantity))
                return;

            // if no errors were found execute the update
            if (daoDrink.updateDrink(
                    drink.getId(),
                    drinkName.getText().toString(),
                    Float.parseFloat(drinkCost.getText().toString().replace(",", ".")),
                    Integer.parseInt(drinkQuantity.getText().toString())
            )) {
                // update the list
                updateList();

                // display a snack bar
                Snackbar.make(parent, CONTEXT.getString(R.string.successful_drink_edit_message), Snackbar.LENGTH_SHORT).show();
            } else Snackbar.make(parent, CONTEXT.getString(R.string.error_drink_edit_message), Snackbar.LENGTH_SHORT).show();
        });

        ImageButton deleteButton = convertView.findViewById(R.id.drink_delete_button);
        deleteButton.setOnClickListener(v -> {
            // delete drink from the db
            if (daoDrink.deleteDrink(drink.getId())) {
                // update list
                updateList();

                // display a snack bar
                Snackbar.make(parent, CONTEXT.getString(R.string.successful_drink_delete_message), Snackbar.LENGTH_SHORT).show();
            } else Snackbar.make(parent, CONTEXT.getString(R.string.error_drink_delete_message), Snackbar.LENGTH_SHORT).show();
        });

        // return the row
        return convertView;
    }

    /**
     * This function checks the validity of the drink
     *
     * @param drinkName The drink name EditText
     * @param drinkCost The drink cost EditText
     * @param drinkQuantity The drink quantity EditText
     * @return true if is valid
     */
    public boolean isDrinkValid(EditText drinkName, EditText drinkCost, EditText drinkQuantity) {
        // name (if edit text is empty)
        drinkName.setError(drinkName.getText().toString().isEmpty() ? CONTEXT.getString(R.string.enter_drink_information_error, CONTEXT.getString(R.string.drink_name)) : null);

        // cost
        if (drinkCost.getText().toString().isEmpty()) // if edit text is empty
            drinkCost.setError(CONTEXT.getString(R.string.enter_drink_information_error, CONTEXT.getString(R.string.drink_cost)));
        else if (Float.parseFloat(drinkCost.getText().toString().replace(",", ".")) <= 0) // if number is not positive
            drinkCost.setError(CONTEXT.getString(R.string.enter_drink_cost_or_quantity_not_positive, CONTEXT.getString(R.string.drink_cost)));
        else // all fine
            drinkCost.setError(null);

        // quantity
        if (drinkQuantity.getText().toString().isEmpty()) // if edit text is empty
            drinkQuantity.setError(CONTEXT.getString(R.string.enter_drink_information_error, CONTEXT.getString(R.string.drink_quantity)));
        else if (Integer.parseInt(drinkQuantity.getText().toString()) <= 0) // if number is not positive
            drinkQuantity.setError(CONTEXT.getString(R.string.enter_drink_cost_or_quantity_not_positive, CONTEXT.getString(R.string.drink_quantity)));
        else // all fine
            drinkQuantity.setError(null);


        // returns true if none of the edit texts have error
        return drinkName.getError() != null
                || drinkCost.getError() != null
                || drinkQuantity.getError() != null;
    }

    /**
     * Changes the adapter data source with a new one
     *
     * @param drinks the new List of drinks as the new data source
     */
    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
    }

    /**
     * Updates the list in an activity
     */
    public void updateList() {
        setDrinks(daoDrink.getAllDrinks());
        notifyDataSetChanged();
    }
}
