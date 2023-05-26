package com.example.coffeeapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.coffeeapp.Model.CartModel;
import com.example.namespace.R;

import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {

    List<CartModel> cartModelList;


    @Override
    public CartHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartlistlayout, parent, false);
        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder( CartHolder holder, int position) {

        holder.price.setText("Ordered " + String.valueOf(cartModelList.get(position).getQuantity()) + " for $" + String.valueOf(cartModelList.get(position).getTotalprice()));

        holder.coffeesize.setText(cartModelList.get(position).getCoffeesize());

    }

    @Override
    public int getItemCount() {

        return cartModelList.size();
    }

    public void setCartModelList(List<CartModel> cartModelList){
        this.cartModelList = cartModelList;

    }
    class CartHolder extends ViewHolder{

        TextView coffeesize, price;
        ImageView imageofCoffee;
        public CartHolder( View itemView) {
            super(itemView);

            coffeesize = itemView.findViewById(R.id.cartcoffeeSize);
            price = itemView.findViewById(R.id.orderdetail);

        }
    }
}
