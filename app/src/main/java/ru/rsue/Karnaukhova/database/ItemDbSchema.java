package ru.rsue.Karnaukhova.database;

import androidx.annotation.Nullable;

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
            public static final String ITEMID = "itemId";
            public static final @Nullable String LISTID = "listId";
            public static final String QUANTITYBOUGHT = "quantityBought";
            public static final String BUYONDATE = "buyOnDate";
            public static final String ISPRIORITY = "isPriority";
        }
    }

    public static final class ItemTable {
        public static final String NAME = "Item";

        public static class Cols {
            public static final String UUID = "uuid";
            public static final String NAMEITEM = "nameItem";
            public static final String WEIGHTUNITID = "weightUnitId";
            public static final String PRICEFORONE = "priceForOne";
            public static final @Nullable String COLOR = "color";
        }
    }

    public static final class UserTable {
        public static final String NAME = "User";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String LOGIN = "Login";
            public static final String PASSWORD = "Password";
            public static final String NICKNAME = "Nickname";
        }
    }

    //
    public static final class ListTable {
        public static final String NAME = "List";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String LISTNAME = "listName";
            public static final String OWNERUSERID = "ownerUserId";
        }
    }

    public static final class AllowedUserToListTable {
        public static final String NAME = "AllowedUserToList";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String LISTID = "listId";
            public static final String USERID = "userId";
        }
    }
}