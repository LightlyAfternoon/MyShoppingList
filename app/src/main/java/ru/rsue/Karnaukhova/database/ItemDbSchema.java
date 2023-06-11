package ru.rsue.Karnaukhova.database;

public class ItemDbSchema {
    public static final class ItemTable {
        public static final String NAME = "Item";
        public static class Cols {
            public static final String UUID = "uuid";
            public static final String NAMEITEM = "nameItem";
            public static final String COUNT = "count";
            public static final String WEIGHTUNIT = "weightUnit";
            public static final String PRICEFORONE = "priceForOne";
            public static final String ADDDATE = "addDate";
        }
    }
}
