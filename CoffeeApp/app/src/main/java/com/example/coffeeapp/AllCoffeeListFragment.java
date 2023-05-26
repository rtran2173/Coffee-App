package com.example.coffeeapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.coffeeapp.Adapter.CoffeeAdapter;
import com.example.coffeeapp.AllCoffeeListFragmentDirections;
import com.example.coffeeapp.MVVM.CoffeeViewModel;
import com.example.coffeeapp.Model.CartModel;
import com.example.coffeeapp.Model.CofeModel;
import com.example.namespace.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AllCoffeeListFragment extends Fragment implements CoffeeAdapter.GetOneCoffee {

    FirebaseFirestore firebaseFirestore;
    CoffeeAdapter madapter;
    RecyclerView recyclerView;
    CoffeeViewModel viewModel;

    Button helpb;
    TextView helpt;

    NavController navController;
    int quantity = 0;
    FloatingActionButton fab;
    TextView quantityOnfAB;
    List<Integer> savequantity = new ArrayList<>();
    int quantitysum = 0;


    public AllCoffeeListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_coffee_list, container, false);
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseFirestore = firebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recViewAll);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        madapter = new CoffeeAdapter(this);
        navController = Navigation.findNavController(view);
        helpb = view.findViewById(R.id.helpb);
        helpt = view.findViewById(R.id.helpt);
        quantityOnfAB = view.findViewById(R.id.quantityOnfAB);
        fab = view.findViewById(R.id.fab);
        viewModel = new ViewModelProvider(getActivity()).get(CoffeeViewModel.class);
        viewModel.getCofeeList().observe(getViewLifecycleOwner(), new Observer<List<CofeModel>>() {
            @Override
            public void onChanged(List<CofeModel> cofeModels) {

                madapter.setCofeModelList(cofeModels);
                recyclerView.setAdapter(madapter);
            }
        });

        quantity = AllCoffeeListFragmentArgs.fromBundle(getArguments()).getQuantity();

        firebaseFirestore.collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete( Task<QuerySnapshot> task) {

                if (task.isSuccessful()){

                    for(DocumentSnapshot ds: task.getResult().getDocuments()){

                        CartModel cartModel = ds.toObject(CartModel.class);
                        int initialquantity = cartModel.getQuantity();

                        savequantity.add(initialquantity);
                    }
                    for (int i=0; i < savequantity.size(); i++){

                        quantitysum+= Integer.parseInt(String.valueOf(savequantity.get(i)));
                    }
                    quantityOnfAB.setText(String.valueOf(quantitysum));

                    quantitysum = 0;
                    savequantity.clear();
                }

            }
        });
        helpb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpt.setText("Press a coffee size to choose quantity and press the cart button on the lower right to see cart");
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_allCoffeeListFragment_to_cartFragment);
            }
        });




    }

    @Override
    public void clickedCoffee(int position, List<CofeModel> cofeModels) {

        String coffeeid = cofeModels.get(position).getCoffeeid();
        String coffeesize = cofeModels.get(position).getCoffeesize();
        double price = cofeModels.get(position).getCoffeeprice();


        AllCoffeeListFragmentDirections.ActionAllCoffeeListFragmentToCoffeeDetailFragment
                action = AllCoffeeListFragmentDirections.actionAllCoffeeListFragmentToCoffeeDetailFragment();

        action.setCoffeesize(coffeesize);
        action.setPrice((int)price);
        action.setId(coffeeid);

    navController.navigate(action);
    }
}
