package com.example.studentstudyplanner.data.repository

import com.example.studentstudyplanner.data.local.dao.UserDao
import com.example.studentstudyplanner.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val loggedInUser: Flow<UserEntity?> = userDao.getLoggedInUser()

    suspend fun signUp(name: String, email: String, pass: String): Result<UserEntity> {
        val trimmedName = name.trim()
        val trimmedEmail = email.trim()

        if (trimmedEmail.isNotBlank()) {
            val existing = userDao.getUserByEmail(trimmedEmail)
            if (existing != null) {
                return Result.failure(Exception("An account with this email already exists."))
            }
        }

        val existingByName = userDao.getUserByName(trimmedName)
        if (existingByName != null) {
            return Result.failure(Exception("An account with this name already exists."))
        }

        userDao.logoutAll()
        val newUser = UserEntity(
            name = trimmedName,
            email = trimmedEmail,
            password = pass,
            isLoggedIn = true
        )
        val newId = userDao.insertUser(newUser)
        return Result.success(newUser.copy(id = newId))
    }

    suspend fun logIn(nameOrEmail: String, pass: String): Result<UserEntity> {
        val query = nameOrEmail.trim()
        val user = if (query.contains("@")) {
            userDao.getUserByEmail(query)
        } else {
            userDao.getUserByName(query)
        }

        if (user == null) {
            return Result.failure(Exception("Account not found. Please create an account."))
        }

        if (user.password.isNotBlank() && pass.isNotBlank() && user.password != pass) {
            return Result.failure(Exception("Incorrect password."))
        }

        userDao.logoutAll()
        userDao.setLoggedIn(user.id)
        return Result.success(user)
    }

    suspend fun logout() {
        userDao.logoutAll()
    }
}
