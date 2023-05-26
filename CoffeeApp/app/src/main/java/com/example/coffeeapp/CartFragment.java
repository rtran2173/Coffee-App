package com.example.coffeeapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeapp.Adapter.CartAdapter;
import com.example.coffeeapp.Model.CartModel;
import com.example.namespace.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {


    CartAdapter madapter;
    RecyclerView recyclerView;
    FirebaseFirestore firestore;
    Button orderbutton, helpb;
    TextView orderSummary, helpt;
    NavController navController;

    List<CartModel> cartModelList = new ArrayList<>();

    double totalOrderCost = 0;
    List<Double> saveTotalCost = new ArrayList<Double>();


    public CartFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        firestore = FirebaseFirestore.getInstance();
        madapter = new CartAdapter();
        helpb = view.findViewById(R.id.helpb);
        helpt = view.findViewById(R.id.helpt);
        orderbutton = view.findViewById(R.id.orderNow);
        recyclerView = view.findViewById(R.id.cartRecView);
        orderSummary = view.findViewById(R.id.orderSummary);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firestore.collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {

                cartModelList.clear();
                if (task.isSuccessful()){

                    for (DocumentSnapshot ds: task.getResult().getDocuments()){

                       CartModel cartModel = ds.toObject(CartModel.class);
                       cartModelList.add(cartModel);

                       madapter.setCartModelList(cartModelList);
                       recyclerView.setAdapter(madapter);
                    }
                }

            }
        });

        // adding the total of all orders
        firestore.collection("Cart").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent( QuerySnapshot value, FirebaseFirestoreException error) {

                if(value!=null){
                    for (DocumentSnapshot ds: value.getDocuments()){

                        CartModel cartModel = ds.toObject(CartModel.class);
                        // we are adding all prices into a list of all integers
                        double valueofallprices = cartModel.getTotalprice();
                        saveTotalCost.add(valueofallprices);
                    }
                    for (int i=0; i < saveTotalCost.size(); i++){

                        totalOrderCost += Double.parseDouble(String.valueOf(saveTotalCost.get(i)));
                    }
                    orderSummary.setText("Total is $"+ String.valueOf(totalOrderCost));
                    totalOrderCost = 0;
                    saveTotalCost.clear();
                }

            }
        });

        helpb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpt.setText("Press order now to reset cart");
            }
        });

        // cart should be empty
        orderbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firestore.collection("coffees").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete( Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){

                            QuerySnapshot tasks = task.getResult();

                            for (DocumentSnapshot ds: tasks.getDocuments()){

                                ds.getReference().update("quantity",0);
                            }
                        }

                    }
                });
                firestore.collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){

                            QuerySnapshot tasks = task.getResult();

                            for (DocumentSnapshot ds: tasks.getDocuments()){

                                ds.getReference().delete();
                            }
                        }

                    }
                });

                navController.navigate(R.id.action_cartFragment_to_allCoffeeListFragment);
                Toast.makeText(getContext(), "Order Placed", Toast.LENGTH_SHORT).show();
            }
        });


    }
}