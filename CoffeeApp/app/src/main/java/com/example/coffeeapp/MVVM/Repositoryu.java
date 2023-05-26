package com.example.coffeeapp.MVVM;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.coffeeapp.Model.CofeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Repositoryu {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    List<CofeModel> cofeModelList = new ArrayList<>();

    CoffeeList interfacecoffeelist;

    public Repositoryu(CoffeeList interfacecoffeelist) {
        this.interfacecoffeelist = interfacecoffeelist;
    }

    public void getCoffee(){

        firebaseFirestore.collection("coffees").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {

                if(task.isSuccessful()) {

                    cofeModelList.clear();

                    for (DocumentSnapshot ds: Objects.requireNonNull(task.getResult()).getDocuments()){

                        CofeModel cofeModel = ds.toObject(CofeModel.class);
                        cofeModelList.add(cofeModel);
                        interfacecoffeelist.coffeeLists(cofeModelList);
                    }
                }
            }
        });
    }
    public interface CoffeeList{
        void coffeeLists(List<CofeModel> cofeModels);
    }
}
