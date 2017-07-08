package com.example.android.workflowtracker.data;

import android.provider.BaseColumns;

/**
 * Created by Cian on 07/07/2017.
 */

public class WorkContract {

    private WorkContract(){}

    public static final class WorkEntry implements BaseColumns {
        public static final String TABLE_NAME = "workflow";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_WF_WORK_TIME = "total_work";
        public static final String COLUMN_WF_BREAK_TIME = "total_break";
        public static final String COLUMN_WF_NUM_OF_BREAKS = "no_of_breaks";
    }
}
