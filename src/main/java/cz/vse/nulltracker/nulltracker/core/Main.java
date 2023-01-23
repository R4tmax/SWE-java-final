package cz.vse.nulltracker.nulltracker.core;

import cz.vse.nulltracker.nulltracker.database.DatabaseHandler;

public class Main {

    public static void main(String[] args) {
        DatabaseHandler.DBtestInit();
        //DatabaseHandler.DBinsertionTest();
        DatabaseHandler.DBdocumentReadTest();
        System.exit(0);
        //launch();
    }
}
