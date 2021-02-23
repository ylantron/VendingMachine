package com.example.distributoreautomatico.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.distributoreautomatico.R;
import com.example.distributoreautomatico.business.Drink;
import com.example.distributoreautomatico.dao.DaoDrink;
import com.example.distributoreautomatico.dao.DaoProfit;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Locale;

public class DrinkGuestAdapter extends BaseAdapter {
    private final Context CONTEXT;
    private List<Drink> drinks;
    private final DaoDrink daoDrink;
    private final DaoProfit daoProfit;

    public DrinkGuestAdapter(Context context, List<Drink> drinks) {
        this.CONTEXT = context;
        this.drinks = drinks;

        this.daoDrink = new DaoDrink(CONTEXT);
        this.daoProfit = new DaoProfit(CONTEXT);
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
            convertView = LayoutInflater.from(CONTEXT).inflate(R.layout.drink_row_guest, parent, false);

        // get the drink from the current position
        Drink drink = (Drink)getItem(position);

        // get the elements of a single row and setting parameters based on the drink
        TextView drinkName = convertView.findViewById(R.id.drink_name_text_view);
        drinkName.setText(drink.getName());

        TextView drinkCost = convertView.findViewById(R.id.drink_cost_text_view);
        drinkCost.setText(String.format(Locale.getDefault(), "%.2f", drink.getCost()));

        TextView drinkQuantity = convertView.findViewById(R.id.drink_quantity_text_view);
        drinkQuantity.setText(String.format(Locale.getDefault(), "%d", drink.getQuantity()));

        Button buyButton = convertView.findViewById(R.id.buy_drink_button);
        buyButton.setOnClickListener(v -> {
            // removes 1 from quantity of the drink
            daoDrink.updateDrink(
                    drink.getId(),
                    drinkName.getText().toString(),
                    Float.parseFloat(drinkCost.getText().toString().replace(",", ".")),
                    Integer.parseInt(drinkQuantity.getText().toString()) - 1
            );

            // keep track of the profit
            daoProfit.addOperation(drink);

            // update the list
            updateList();

            // display a snack bar
            Snackbar.make(parent, CONTEXT.getString(R.string.successful_drink_buy_message), Snackbar.LENGTH_SHORT).show();
        });

        // disables the button if there are no more drinks
        buyButton.setEnabled(drink.getQuantity() > 0);

        // return the row
        return convertView;
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
