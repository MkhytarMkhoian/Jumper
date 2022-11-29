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

package com.mkhytarmkhoian.jumper

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class MigrationRunnerTest {

    @get:Rule
    val coroutineRule = CoroutinesTestRule()

    private val migrationStorage: MigrationStorage = mock()
    private val migrationFactory: MigrationFactory = mock()

    private val migrationRunner = MigrationRunner(migrationStorage, migrationFactory)

    @Test
    fun do_migration_if_need() = runTest {
        whenever(migrationStorage.getCurrentVersion()).doReturn(0)
        whenever(migrationStorage.getUpgradeToVersion()).doReturn(2)

        val migrationStepV1: Migration = mock()
        val migrationStepV2: Migration = mock()

        whenever(migrationStepV1.getSteps()).doReturn(
            listOf(
                object : MigrationStep {
                    override suspend fun execute() {

                    }
                },
                object : MigrationStep {
                    override suspend fun execute() {

                    }
                }
            )
        )

        whenever(migrationStepV2.getSteps()).doReturn(
            listOf(
                object : MigrationStep {
                    override suspend fun execute() {

                    }
                }
            )
        )

        whenever(migrationFactory.getMigrationStep(1)).doReturn(migrationStepV1)
        whenever(migrationFactory.getMigrationStep(2)).doReturn(migrationStepV2)

        migrationRunner.runMigration()

        verify(migrationStorage).getCurrentVersion()
        verify(migrationStorage).getUpgradeToVersion()
        verify(migrationFactory).getMigrationStep(1)
        verify(migrationFactory).getMigrationStep(2)
        verify(migrationStorage).setCurrentVersion(1)
        verify(migrationStorage).setCurrentVersion(2)
    }

    @Test(expected = NullPointerException::class)
    fun do_migration_if_need_throw_exception_when_empty_steps() = runTest {
        whenever(migrationStorage.getCurrentVersion()).doReturn(0)
        whenever(migrationStorage.getUpgradeToVersion()).doReturn(2)

        val migrationStepV2: Migration = mock()

        whenever(migrationStepV2.getSteps()).doReturn(emptyList())

        migrationRunner.runMigration()
    }

    @Test
    fun do_not_migrate_when_version_the_same() = runTest {
        whenever(migrationStorage.getCurrentVersion()).doReturn(1)
        whenever(migrationStorage.getUpgradeToVersion()).doReturn(1)

        migrationRunner.runMigration()

        verify(migrationStorage).getCurrentVersion()
        verify(migrationStorage).getUpgradeToVersion()
        verifyNoInteractions(migrationFactory)
    }

    @Test
    fun do_not_migrate_when_old_version_bigger_than_new() = runTest {
        whenever(migrationStorage.getCurrentVersion()).doReturn(2)
        whenever(migrationStorage.getUpgradeToVersion()).doReturn(1)

        migrationRunner.runMigration()

        verify(migrationStorage).getCurrentVersion()
        verify(migrationStorage).getUpgradeToVersion()
        verifyNoInteractions(migrationFactory)
    }
}