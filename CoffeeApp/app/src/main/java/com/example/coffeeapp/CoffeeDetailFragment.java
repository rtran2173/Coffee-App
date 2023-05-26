package com.example.coffeeapp;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeapp.CoffeeDetailFragmentDirections;
import com.example.coffeeapp.Model.CofeModel;
import com.example.namespace.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.util.HashMap;


public class CoffeeDetailFragment extends Fragment {

    NavController navController;
    int quantity = 0;
    FirebaseFirestore firebaseFirestore;
    Button add, sub, order;
    Button helpb;
    TextView helpt;
    TextView coffeesize, quantityview, orderINFO;
    String coffeeid, name;
    double price = 0;


    double totalprice = 0;


    public CoffeeDetailFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coffee_detail, container, false);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coffeesize = view.findViewById(R.id.coffeeSize);
        add = view.findViewById(R.id.incrementcoffee);
        sub = view.findViewById(R.id.decrementcoffee);
        helpb = view.findViewById(R.id.helpb);
        helpt = view.findViewById(R.id.helpt);
        quantityview = view.findViewById(R.id.quantitydetailnumber);
        firebaseFirestore = FirebaseFirestore.getInstance();
        navController = Navigation.findNavController(view);
        order = view.findViewById(R.id.order);
        orderINFO = view.findViewById(R.id.orderINFO);
        name = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getCoffeesize();
        coffeeid = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getId();
        price = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getPrice();

        coffeesize.setText(name + " $" + String.valueOf(price));

        // fetching recent quantity and displaying it from firestore
        firebaseFirestore.collection("coffees").document(coffeeid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent( DocumentSnapshot value, FirebaseFirestoreException error) {

                CofeModel cofeModel = value.toObject(CofeModel.class);
                quantity = cofeModel.getQuantity();
                quantityview.setText(String.valueOf(quantity));

                totalprice = quantity * price;
                orderINFO.setText(String.valueOf("Total Price is $" + totalprice));

                if (quantity == 0){
                    firebaseFirestore.collection("Cart").document(name).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete( Task<Void> task) {

                        }
                    });
                }

            }
        });
        helpb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpt.setText("Press - to decrease your quantity, Press + to increase your quantity, and Press order to update cart");
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (quantity < 10){

                    quantity ++; //quantity = quantity + 1; similar
                    quantityview.setText(String.valueOf(quantity));
                    //Updating quantity
                    firebaseFirestore.collection("coffees").document(coffeeid).update("quantity",quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {

                        }
                    });



                }else {
                    Toast.makeText(getContext(), "You have reached the max quantity", Toast.LENGTH_SHORT).show();
                }

                totalprice = quantity * price;
                orderINFO.setText(String.valueOf("Total Price is " + totalprice));



            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(quantity == 0){
                    Toast.makeText(getContext(), "Nothing in cart", Toast.LENGTH_SHORT).show();
                    quantityview.setText(String.valueOf(quantity));
                    quantity = 0;
                    totalprice = 0;


                }

                else {
                    quantity--; //quantity = quantity - 1; similar
                    quantityview.setText(String.valueOf(quantity));

                    totalprice = quantity * price;
                    orderINFO.setText(String.valueOf("Total Price is " + totalprice));
                    //Updating quantity
                    firebaseFirestore.collection("coffees").document(coffeeid).update("quantity",quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {

                        }
                    });


                }






            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (quantity == 0){

                    navController.navigate(R.id.action_coffeeDetailFragment_to_allCoffeeListFragment);
                    Toast.makeText(getContext(), "You did not order " + name, Toast.LENGTH_SHORT).show();

                } else {

                    AddToCart();

                    com.example.coffeeapp.CoffeeDetailFragmentDirections.ActionCoffeeDetailFragmentToAllCoffeeListFragment
                            action = CoffeeDetailFragmentDirections.actionCoffeeDetailFragmentToAllCoffeeListFragment();

                                          action.setQuantity(quantity);
                                          navController.navigate(action);
                    Toast.makeText(getContext(), "You've ordered " + name, Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private void AddToCart() {



            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("coffeesize", name);
            hashMap.put("quantity", quantity);
            hashMap.put("totalprice", totalprice);
            hashMap.put("coffeeid", coffeeid);

            // creating new collection for cart

            firebaseFirestore.collection("Cart").document(name).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {


                }
         });



        }

    }
