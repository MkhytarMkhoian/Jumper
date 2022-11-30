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

package io.github.mkhytarmkhoian.jumper

class MigrationRunner(
    private val storage: MigrationStorage,
    private val factory: MigrationFactory,
) {

    suspend fun runMigration() {
        val oldVersion = storage.getCurrentVersion()
        val newVersion = storage.getUpgradeToVersion()

        if (oldVersion >= newVersion) return

        for (version in (oldVersion + 1)..newVersion) {
            val migrationStep = factory.getMigrationStep(version)
            val steps = migrationStep.getSteps()
            if (steps.isNotEmpty()) {
                steps.forEach { step ->
                    step.execute()
                }
                storage.setCurrentVersion(version)
            } else {
                storage.setCurrentVersion(newVersion)
            }
        }
    }
}