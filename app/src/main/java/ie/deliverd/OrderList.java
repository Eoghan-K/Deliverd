package ie.deliverd;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class OrderList extends ArrayAdapter<Order> {
    private Activity context;
    private List<Order> orderList;


    public OrderList(Activity context, List<Order> orderList){
        super(context, R.layout.order_list_layout, orderList);
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.order_list_layout, null, true);

        TextView textViewTitle = listViewItem.findViewById(R.id.orderTitle);
        TextView textViewCustomer = listViewItem.findViewById(R.id.customerName);

        Order order = orderList.get(position);

        textViewTitle.setText(order.getOrderTitle());
        textViewCustomer.setText(order.getCustomerName());

        return  listViewItem;
    }
}
