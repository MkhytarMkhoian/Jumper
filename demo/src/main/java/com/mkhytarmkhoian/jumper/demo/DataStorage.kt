/*
 * Copyright 2022 Mkhytar Mkhoian.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.mkhytarmkhoian.jumper.demo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.io.IOException

interface DataStorage {

  suspend fun <T> put(key: Preferences.Key<T>, value: T)

  suspend fun <T> get(key: Preferences.Key<T>): T?

  suspend fun <T> remove(key: Preferences.Key<T>)

  suspend fun <T> hasKey(key: Preferences.Key<T>): Boolean

  suspend fun clear()
}

class DataStorageImpl(
  context: Context
) : DataStorage {

  companion object {
    private const val STORE_NAME = "lalafo_data_store"
  }

  private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = STORE_NAME,
    produceMigrations = { context ->
      listOf(
        SharedPreferencesMigration(context, "com.lalafo_preferences"),
        SharedPreferencesMigration(context, "notifications_settings"),
      )
    })

  private val dataStore: DataStore<Preferences> = context.preferencesDataStore

  override suspend fun <T> put(key: Preferences.Key<T>, value: T) {
    dataStore.edit { preferences ->
      preferences[key] = value
    }
  }

  private suspend fun getPreferences(): Preferences {
    return try {
      dataStore.data.first()
    } catch (e: IOException) {
      emptyPreferences()
    } catch (e: Exception) {
      throw e
    }
  }

  override suspend fun <T> get(key: Preferences.Key<T>): T? {
    return try {
      val result = getPreferences()[key]
      result
    } catch (e: Exception) {
      null
    }
  }

  override suspend fun <T> remove(key: Preferences.Key<T>) {
    dataStore.edit { preferences ->
      preferences.remove(key)
    }
  }

  override suspend fun <T> hasKey(key: Preferences.Key<T>): Boolean {
    return getPreferences().contains(key)
  }

  override suspend fun clear() {
    dataStore.edit { preferences ->
      preferences.clear()
    }
  }
}
