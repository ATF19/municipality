package com.municipality.backend.infrastructure.persistence.repository.user

import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.user.Email
import com.municipality.backend.domain.model.user.RegisteredUser
import com.municipality.backend.domain.model.user.RegisteredUserId
import com.municipality.backend.domain.model.user.Username
import com.municipality.backend.domain.service.user.Users
import com.municipality.backend.infrastructure.persistence.repository.PageBuilder
import org.springframework.stereotype.Repository

@Repository
class UserRepository(
    private val userJpaRepository: UserJpaRepository
) : Users {

    override fun register(user: RegisteredUser) {
        userJpaRepository.save(user)
    }

    override fun all(page: PageNumber) = userJpaRepository.findAll(PageBuilder.builder.build(page)).content

    override fun by(userId: RegisteredUserId) = userJpaRepository
        .findById(userId)
        .orElseThrow { NoSuchElementException("No user is registered with the ID '${userId.rawId}'") }


    override fun by(username: Username) = userJpaRepository.findByUsername(username)

    override fun exists(username: Username) = userJpaRepository.existsByUsername(username)

    override fun exists(email: Email) = userJpaRepository.existsByEmail(email)
}