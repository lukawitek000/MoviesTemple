package com.example.android.popularmovies.database;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DatabaseExecutor {

    private static final Object LOCK = new Object();
    private static  DatabaseExecutor instance;
    private final Executor diskIO;


    private DatabaseExecutor(Executor diskIO){
        this.diskIO = diskIO;
    }

    public static DatabaseExecutor getInstance(){
        if(instance == null){
            synchronized (LOCK){
                instance = new DatabaseExecutor(Executors.newSingleThreadExecutor());
            }
        }
        return instance;
    }


    public Executor getDiskIO() {
        return diskIO;
    }
}
