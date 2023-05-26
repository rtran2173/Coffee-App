package com.example.coffeeapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.example.coffeeapp.Model.CofeModel;
import com.example.namespace.R;

import java.util.List;

public class CoffeeAdapter extends RecyclerView.Adapter<CoffeeAdapter.CoffeeListHolder> {


    List<CofeModel> cofeModelList;

    GetOneCoffee interfacegetCoffee;

    public CoffeeAdapter(GetOneCoffee interfacegetCoffee) {
        this.interfacegetCoffee = interfacegetCoffee;
    }

    @Override
    public CoffeeAdapter.CoffeeListHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coffeeliststyle, parent, false);
        return new CoffeeListHolder(view);
    }

    @Override
    public void onBindViewHolder( CoffeeAdapter.CoffeeListHolder holder, int position) {

        holder.coffeesize.setText(cofeModelList.get(position).getCoffeesize());



    }

    @Override
    public int getItemCount() {
        return cofeModelList.size();
    }

    public void setCofeModelList(List<CofeModel> cofeModelList) {
        this.cofeModelList = cofeModelList;
    }

    class CoffeeListHolder extends ViewHolder implements View.OnClickListener{

        TextView coffeesize, description;
        ImageView imageView;

        public CoffeeListHolder(View itemView) {
            super(itemView);

            coffeesize = itemView.findViewById(R.id.coffeeSize);
            description = itemView.findViewById(R.id.coffeedetail);
            imageView = itemView.findViewById(R.id.coffeeImage);

            coffeesize.setOnClickListener(this);
            description.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            interfacegetCoffee.clickedCoffee(getAdapterPosition(), cofeModelList);

        }
    }
    public interface GetOneCoffee{
        void clickedCoffee(int position, List<CofeModel> cofeModels);
    }
}
