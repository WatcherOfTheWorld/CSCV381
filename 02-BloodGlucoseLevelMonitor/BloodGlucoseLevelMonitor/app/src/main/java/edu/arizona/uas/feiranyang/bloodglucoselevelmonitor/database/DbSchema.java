package edu.arizona.uas.feiranyang.bloodglucoselevelmonitor.database;

public class DbSchema {
    public static final class Table{
        public static final String NAME = "level";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String FASTING = "fasting";
            public static final String BF = "bf";
            public static final String LUNCH = "lunch";
            public static final String DINNER = "dinner";
        }
    }

}
