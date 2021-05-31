package org.techtown.mycalendar;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Data.class}, version = 2)
public abstract class UserDatabase extends RoomDatabase {
    public abstract UserRepository userRepository();
}
