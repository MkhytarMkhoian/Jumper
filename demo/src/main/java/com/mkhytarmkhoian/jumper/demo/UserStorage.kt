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

import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.runBlocking
import java.util.UUID

class UserStorage(
    private val dataStorage: DataStorage,
) {

    companion object {
        private val USER_HASH = stringPreferencesKey("USER_HASH")
        private val INTERNAL_ANALYTICS_USER_HASH = stringPreferencesKey("INTERNAL_ANALYTICS_USER_HASH")
    }

    suspend fun generateUserHashIfNeed(): String {
        var userHash = getUserHash()
        if (userHash == null) {
            userHash = UUID.randomUUID().toString()
            dataStorage.put(USER_HASH, userHash)
        }

        return userHash
    }

    suspend fun getInternalAnalyticsUserHash(): String? = dataStorage.get(INTERNAL_ANALYTICS_USER_HASH)

    fun getUserHash(): String? = runBlocking { dataStorage.get(USER_HASH) }

    suspend fun setUserHash(hash: String){
        dataStorage.put(USER_HASH, hash)
    }

    fun userId(): Long = 1243455L
}
