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

import androidx.datastore.preferences.core.intPreferencesKey
import com.mkhytarmkhoian.jumper.MigrationStorage

class MigrationStorageImpl(
    private val dataStorage: DataStorage
) : MigrationStorage {

    override suspend fun setCurrentVersion(version: Int) {
        dataStorage.put(CURRENT_VERSION, version)
    }

    override suspend fun getCurrentVersion(): Int {
        return dataStorage.get(CURRENT_VERSION) ?: 0
    }

    override fun getUpgradeToVersion(): Int {
        return UPGRADE_TO_VERSION
    }

    companion object {
        private val CURRENT_VERSION = intPreferencesKey("CURRENT_VERSION")
        private const val UPGRADE_TO_VERSION = 2
    }
}