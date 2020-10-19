package com.redstar.magic.pluginlib.db;

import com.redstar.magic.pluginlib.pm.version.PluginDataBase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;


/**
 * Created by chen.huarong on 2019-09-27.
 */
@RunWith(AndroidJUnit4.class)
public class MigrationTest {

    private static final String TEST_DB = "migration-test";

    @Rule
    public MigrationTestHelper helper;

    public MigrationTest() {
        helper = new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
                PluginDataBase.class.getCanonicalName(),
                new FrameworkSQLiteOpenHelperFactory());
    }

    @Test
    public void migrate1To2() throws IOException {
        SupportSQLiteDatabase db = helper.createDatabase(TEST_DB, 1);

        // db has schema version 1. insert some data using SQL queries.
        // You cannot use DAO classes because they expect the latest schema.
//        db.execSQL("create table 'plugin' ('id' integer, 'pluginName' string, '')");
        for (int i = 0; i < 10; i++) {
            db.execSQL("insert into 'plugin' values(:i,'pluginName', '1', '1.0', 0, '')");
        }

        // Prepare for the next version.
        db.close();

        // Re-open the database with version 2 and provide
        // MIGRATION_1_2 as the migration process.
//        db = helper.runMigrationsAndValidate(TEST_DB, 2, true, new Migration(1, 2) {
//            @Override
//            public void migrate(@NonNull SupportSQLiteDatabase database) {
//
//            }
//        });

        // MigrationTestHelper automatically verifies the schema changes,
        // but you need to validate that the data was migrated properly.
    }

}
