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

package com.mkhytarmkhoian.jumper.demo.v2

import com.mkhytarmkhoian.jumper.MigrationStep
import com.mkhytarmkhoian.jumper.demo.UserStorage

class TemplateMessagesMigrationStep(
    private val userStorage: UserStorage
) : MigrationStep {

    override suspend fun execute() {
        val buyers = getTemplateMessages(userStorage.userId(), TemplateType.BUYER)
        val sellers = getTemplateMessages(userStorage.userId(), TemplateType.SELLER)

        buyers.forEach { item ->
            save(item, TemplateType.BUYER, userStorage.userId())
        }

        sellers.forEach { item ->
            save(item, TemplateType.SELLER, userStorage.userId())
        }
    }

    // It could be DataStore
    private fun save(message: String, templateType: TemplateType, userId: Long) {

    }

    // It could be SQLite database
    private fun getTemplateMessages(userId: Long, templateType: TemplateType): List<String> {
        return listOf("sample1", "sample2", "sample2")
    }

    enum class TemplateType {
        BUYER, SELLER;
    }
}