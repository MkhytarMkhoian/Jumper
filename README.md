# Jumper
Library that provides API for migration in Android project

It will help you to migrate from v1 logic to v2 in a simple way with support versions

## Installation
```
implementation 'io.github.mkhytarmkhoian:jumper:1.0.0'
```

## Components

`MigrationStep` - it's an interface where you put logic you want to migrate

```kotlin
class FCMMigrationStep : MigrationStep {

  override suspend fun execute() {
    val gsmToken = "gsmToken"
    val fcmToken = "fcmToken"

    if (gsmToken != null && fcmToken == null) {
      updateToken(gsmToken)
    }
  }

  private fun updateToken(token: String) {
    // send token to the server
  }
}
```

Migration - represent migration component that contain different MigrationStep

```kotlin
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
```

MigrationFactory - it's a factory that you need to implement to provide Migration for each version of you app

```kotlin
class MigrationFactoryImpl(
  private val migrationV1: MigrationV1,
  private val migrationV2: MigrationV2,
) : MigrationFactory {

  override fun getMigrationStep(version: Int): Migration {
    return when (version) {
      1 -> migrationV1
      2 -> migrationV2
      else -> throw NullPointerException("No migration step for this version")
    }
  }
}
```

MigrationStorage is responsible for saving the current version of the app and the version to upgrade

```kotlin
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
```

MigrationRunner - runner that manages running migration

```kotlin
suspend fun onCreate() {
  val userStorage = UserStorage(dataStorage)
  val storage = MigrationStorageImpl(dataStorage)

  val userHashMigration = UserHashMigrationStep(userStorage)
  val fCMMigration = FCMMigrationStep()
  val migrationV1 = MigrationV1(userHashMigration, fCMMigration)

  val templateMessagesMigration = TemplateMessagesMigrationStep(userStorage)
  val migrationV2 = MigrationV2(templateMessagesMigration)

  val factory = MigrationFactoryImpl(migrationV1, migrationV2)
  val runner = MigrationRunner(storage, factory)

  runner.runMigration()
}
```

## License
```
Copyright 2022 Mkhytar Mkhoian.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
```