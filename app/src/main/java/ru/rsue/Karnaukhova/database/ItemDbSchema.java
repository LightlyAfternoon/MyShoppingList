package ru.rsue.Karnaukhova.database;

import androidx.annotation.Nullable;

public class ItemDbSchema {
    public static final class WeightUnitTable {
        public static final String NAME = "weight_unit";
        public static class Cols {
            public static final String UUID = "uuid_weight_unit";
            public static final String NAMEWEIGHTUNIT = "name_weight_unit";
        }
    }

    public static final class ItemInListTable {
        public static final String NAME = "item_in_list";
        public static class Cols {
            public static final String UUID = "uuid_item_in_list";
            public static final String COUNT = "count";
            public static final String ADDDATE = "add_date";
            public static final String ITEMID = "item_id";
            public static final @Nullable String LISTID = "list_id";
            public static final String QUANTITYBOUGHT = "quantity_bought";
            public static final @Nullable String BUYONDATE = "buy_on_date";
            public static final String ISPRIORITY = "is_priority";
            public static final String USERID = "user_id";
        }
    }

    public static final class ItemTable {
        public static final String NAME = "item";

        public static class Cols {
            public static final String UUID = "uuid_item";
            public static final String NAMEITEM = "name_item";
            public static final String WEIGHTUNITID = "weight_unit_id";
            public static final String PRICEFORONE = "price_for_one";
            public static final @Nullable String COLOR = "color";
            public static final @Nullable String USERID = "user_id";
        }
    }

    public static final class UserTable {
        public static final String NAME = "user";

        public static final class Cols {
            public static final String UUID = "uuid_user";
            public static final String LOGIN = "login";
            public static final String PASSWORD = "password";
            public static final String NICKNAME = "nickname";
        }
    }

    public static final class ListTable {
        public static final String NAME = "list";

        public static final class Cols {
            public static final String UUID = "uuid_list";
            public static final String LISTNAME = "list_name";
            public static final String OWNERUSERID = "owner_user_id";
        }
    }

    public static final class AllowedUserToListTable {
        public static final String NAME = "allowed_user_to_list";

        public static final class Cols {
            public static final String UUID = "uuid_allowed_user_to_list";
            public static final String LISTID = "list_id";
            public static final String USERID = "user_id";
        }
    }
}