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

package com.mkhytarmkhoian.jumper.demo.v1

import com.mkhytarmkhoian.jumper.Migration
import com.mkhytarmkhoian.jumper.MigrationStep

class MigrationV1(
    private val userHashMigration: UserHashMigrationStep,
    private val fCMMigration: FCMMigrationStep
) : Migration {

    override fun getSteps(): List<MigrationStep> {
        return listOf(
            userHashMigration,
            fCMMigration,
        )
    }
}