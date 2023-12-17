package ru.rsue.Karnaukhova.database;

public class ItemDbSchema {
    public static final class WeightUnitTable {
        public static final String NAME = "WeightUnit";
        public static class Cols {
            public static final String UUID = "uuid";
            public static final String NAMEWEIGHTUNIT = "nameWeightUnit";
        }
    }

    public static final class ItemInListTable {
        public static final String NAME = "ItemInList";
        public static class Cols {
            public static final String UUID = "uuid";
            public static final String COUNT = "count";
            public static final String ADDDATE = "addDate";
            public static final String ISBOUGHT = "isBought";
            public static final String ITEMID = "itemId";
        }
    }

    public static final class ItemTable {
        public static final String NAME = "Item";

        public static class Cols {
            public static final String UUID = "uuid";
            public static final String NAMEITEM = "nameItem";
            public static final String WEIGHTUNITID = "weightUnitId";
            public static final String PRICEFORONE = "priceForOne";
        }
    }
}