package com.municipality.backend.application.user

import com.municipality.backend.domain.model.core.error.InsufficientPermissionException
import com.municipality.backend.domain.model.municipality.MunicipalityId
import com.municipality.backend.domain.model.municipality.district.DistrictId
import com.municipality.backend.domain.model.user.*
import com.municipality.backend.domain.service.user.Users
import com.municipality.backend.shared_code_for_tests.TestGroup
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.testng.annotations.Test
import java.util.*


class UserAppServiceTest {

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_none_admin_tries_to_create_an_internal_user() {
        // given
        val users = mockk<Users>()
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val command = RegisterInternalUserCommand(
            AnonymousUser(),
            Username("ben"),
            Email("test@gmail.com"),
            UnencryptedPassword("thisISMYStrong@Pass2"),
            FirstName("Ben"),
            LastName("Doe"),
            false,
            emptySet(),
            emptySet(),
            emptySet(),
            emptySet()
        )

        // when

        // then
        assertThatThrownBy { appService.register(command) }
            .isInstanceOf(InsufficientPermissionException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_provided_with_missing_information_when_creating_internal_user() {
        // given
        val users = mockk<Users>()
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val command = RegisterInternalUserCommand(
            RegisteredUserBuilder().admin().build(),
            Username("ben"),
            Email("test@gmail.com"),
            UnencryptedPassword("thisISMYStrong@Pass2"),
            FirstName(null),
            LastName("Doe"),
            false,
            emptySet(),
            emptySet(),
            emptySet(),
            emptySet()
        )

        // when

        // then
        assertThatThrownBy { appService.register(command) }
            .isInstanceOf(MissingInformationException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_provided_with_invalid_email() {
        // given
        val users = mockk<Users>()
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val command = RegisterInternalUserCommand(
            RegisteredUserBuilder().admin().build(),
            Username("ben"),
            Email("invalid"),
            UnencryptedPassword("thisISMYStrong@Pass2"),
            FirstName("Jogn"),
            LastName("Doe"),
            false,
            emptySet(),
            emptySet(),
            emptySet(),
            emptySet()
        )

        // when

        // then
        assertThatThrownBy { appService.register(command) }
            .isInstanceOf(InvalidEmailException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_provided_with_existing_email() {
        // given
        val users = mockk<Users>()
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val email = Email("test@gmail.com")
        every { users.exists(email) }.returns(true)
        val command = RegisterInternalUserCommand(
            RegisteredUserBuilder().admin().build(),
            Username("ben"),
            email,
            UnencryptedPassword("thisISMYStrong@Pass2"),
            FirstName("Jogn"),
            LastName("Doe"),
            false,
            emptySet(),
            emptySet(),
            emptySet(),
            emptySet()
        )

        // when

        // then
        assertThatThrownBy { appService.register(command) }
            .isInstanceOf(EmailAlreadyExistsException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_provided_with_weak_password() {
        // given
        val users = mockk<Users>()
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val command = RegisterInternalUserCommand(
            RegisteredUserBuilder().admin().build(),
            Username("ben"),
            Email("invalid"),
            UnencryptedPassword("weak"),
            FirstName("Jogn"),
            LastName("Doe"),
            false,
            emptySet(),
            emptySet(),
            emptySet(),
            emptySet()
        )

        // when

        // then
        assertThatThrownBy { appService.register(command) }
            .isInstanceOf(WeakPasswordException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_provided_with_existing_username() {
        // given
        val users = mockk<Users>(relaxed = true)
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val username = Username("ben")
        every { users.exists(username) }.returns(true)
        val command = RegisterInternalUserCommand(
            RegisteredUserBuilder().admin().build(),
            username,
            Email("test@gmail.com"),
            UnencryptedPassword("thisISMYStrong@Pass2"),
            FirstName("Jogn"),
            LastName("Doe"),
            false,
            emptySet(),
            emptySet(),
            emptySet(),
            emptySet()
        )

        // when

        // then
        assertThatThrownBy { appService.register(command) }
            .isInstanceOf(UsernameAlreadyExistsException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun register_internal_user_without_roles() {
        // given
        val users = mockk<Users>(relaxed = true)
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val email = Email("test@gmail.com")
        val username = Username("ben")
        val unencryptedPassword = UnencryptedPassword("thisISMYStrong@Pass2")
        val cryptedPassword = CryptedPassword("cryptedPass")
        every { users.exists(email) }.returns(false)
        every { users.exists(username) }.returns(false)
        every { passwords.encrypt(unencryptedPassword) }.returns(cryptedPassword)
        val command = RegisterInternalUserCommand(
            RegisteredUserBuilder().admin().build(),
            username,
            email,
            unencryptedPassword,
            FirstName("John"),
            LastName("Doe"),
            false,
            emptySet(),
            emptySet(),
            emptySet(),
            emptySet()
        )

        // when
        appService.register(command)

        // then
        val captureSlot = slot<RegisteredUser>()
        verify { users.register(capture(captureSlot)) }
        val result = captureSlot.captured
        assertThat(result.email).isEqualTo(email)
        assertThat(result.username).isEqualTo(username)
        assertThat(result.cryptedPassword).isEqualTo(cryptedPassword)
        assertThat(result.firstName).isEqualTo(command.firstName)
        assertThat(result.lastName).isEqualTo(command.lastName)
        assertThat(result.roles.all()).isEmpty()
    }

    @Test(groups = [TestGroup.UNIT])
    fun register_internal_user_with_roles() {
        // given
        val users = mockk<Users>(relaxed = true)
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val email = Email("test@gmail.com")
        val username = Username("ben")
        val unencryptedPassword = UnencryptedPassword("thisISMYStrong@Pass2")
        val cryptedPassword = CryptedPassword("cryptedPass")
        every { users.exists(email) }.returns(false)
        every { users.exists(username) }.returns(false)
        every { passwords.encrypt(unencryptedPassword) }.returns(cryptedPassword)
        val municipalityId1 = MunicipalityId()
        val municipalityId2 = MunicipalityId()
        val municipalityId3 = MunicipalityId()
        val districtId1 = DistrictId()
        val districtId2 = DistrictId()
        val command = RegisterInternalUserCommand(
            RegisteredUserBuilder().admin().build(),
            username,
            email,
            unencryptedPassword,
            FirstName("John"),
            LastName("Doe"),
            true,
            setOf(municipalityId1, municipalityId2),
            setOf(municipalityId3),
            setOf(districtId1),
            setOf(districtId2)
        )

        // when
        appService.register(command)

        // then
        val captureSlot = slot<RegisteredUser>()
        verify { users.register(capture(captureSlot)) }
        val result = captureSlot.captured
        assertThat(result.email).isEqualTo(email)
        assertThat(result.username).isEqualTo(username)
        assertThat(result.cryptedPassword).isEqualTo(cryptedPassword)
        assertThat(result.firstName).isEqualTo(command.firstName)
        assertThat(result.lastName).isEqualTo(command.lastName)
        assertThat(result.isAdmin()).isTrue
        assertThat(result.isResponsible(municipalityId1)).isTrue
        assertThat(result.isResponsible(municipalityId2)).isTrue
        assertThat(result.isResponsible(municipalityId3)).isFalse
        assertThat(result.isAuditor(municipalityId3)).isTrue
        assertThat(result.isResponsible(districtId1)).isTrue
        assertThat(result.isResponsible(districtId2)).isFalse
        assertThat(result.isAuditor(districtId1)).isFalse
        assertThat(result.isAuditor(districtId2)).isTrue
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_username_was_not_found() {
        // given
        val users = mockk<Users>(relaxed = true)
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val username = Username("john")
        var password = UnencryptedPassword("mypass")
        every { users.by(username) }.returns(Optional.empty())

        // when

        // then
        assertThatThrownBy { appService.login(username, password) }
            .isInstanceOf(IncorrectUsernameOrPasswordException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_password_is_incorrect() {
        // given
        val users = mockk<Users>(relaxed = true)
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val username = Username("john")
        var password = UnencryptedPassword("mypass")
        val user = RegisteredUserBuilder().build()
        every { users.by(username) }.returns(Optional.of(user))
        every { passwords.areEquals(user.cryptedPassword, password) }.returns(false)

        // when

        // then
        assertThatThrownBy { appService.login(username, password) }
            .isInstanceOf(IncorrectUsernameOrPasswordException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun login_user() {
        // given
        val users = mockk<Users>(relaxed = true)
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val username = Username("john")
        var password = UnencryptedPassword("mypass")
        val user = RegisteredUserBuilder().build()
        val session = Session("myCoolSession")
        every { users.by(username) }.returns(Optional.of(user))
        every { passwords.areEquals(user.cryptedPassword, password) }.returns(true)
        every { sessions.create(user) }.returns(session)

        // when
        val result = appService.login(username, password)

        // then
        verify { sessions.create(user) }
        assertThat(result).isEqualTo(session)
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_user_is_not_registered() {
        // given
        val users = mockk<Users>(relaxed = true)
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val user = AnonymousUser()

        // when

        // then
        assertThatThrownBy { appService.profile(user) }
            .isInstanceOf(InsufficientPermissionException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun return_user_profile() {
        // given
        val users = mockk<Users>(relaxed = true)
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val user = RegisteredUserBuilder().build()

        // when
        val result = appService.profile(user)

        // then
        assertThat(result).isEqualTo(user)
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_user_is_not_registered_and_tries_to_update_profile() {
        // given
        val users = mockk<Users>(relaxed = true)
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val user = AnonymousUser()
        val command = UpdateProfileCommand(
            user,
            Email("newEmail@test.com"),
            Optional.of(UnencryptedPassword("myStrongP1ss")),
            FirstName("John"),
            LastName("Doe")
        )

        // when

        // then
        assertThatThrownBy { appService.updateProfile(command) }
            .isInstanceOf(InsufficientPermissionException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_not_all_information_are_provided_when_updating_profile() {
        // given
        val users = mockk<Users>(relaxed = true)
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val user = RegisteredUserBuilder.DEFAULT
        val command = UpdateProfileCommand(
            user,
            Email("newEmail@test.com"),
            Optional.of(UnencryptedPassword("myStrongP1ss")),
            FirstName(null),
            LastName("Doe")
        )

        // when

        // then
        assertThatThrownBy { appService.updateProfile(command) }
            .isInstanceOf(MissingInformationException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_a_weak_password_is_provided_when_updating_profile() {
        // given
        val users = mockk<Users>(relaxed = true)
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val user = RegisteredUserBuilder.DEFAULT
        val command = UpdateProfileCommand(
            user,
            Email("newEmail@test.com"),
            Optional.of(UnencryptedPassword("weak")),
            FirstName("John"),
            LastName("Doe")
        )

        // when

        // then
        assertThatThrownBy { appService.updateProfile(command) }
            .isInstanceOf(WeakPasswordException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_a_invalid_email_is_provided_when_updating_profile() {
        // given
        val users = mockk<Users>(relaxed = true)
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val user = RegisteredUserBuilder.DEFAULT
        val command = UpdateProfileCommand(
            user,
            Email("newEmail"),
            Optional.of(UnencryptedPassword("strong@1Pass")),
            FirstName("John"),
            LastName("Doe")
        )

        // when

        // then
        assertThatThrownBy { appService.updateProfile(command) }
            .isInstanceOf(InvalidEmailException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_a_existing_email_is_provided_when_updating_profile() {
        // given
        val users = mockk<Users>(relaxed = true)
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val user = RegisteredUserBuilder.DEFAULT
        val email = Email("email@gmail.com")
        every { users.exists(email) }.returns(true)
        val command = UpdateProfileCommand(
            user,
            email,
            Optional.of(UnencryptedPassword("strong@1Pass")),
            FirstName("John"),
            LastName("Doe")
        )

        // when

        // then
        assertThatThrownBy { appService.updateProfile(command) }
            .isInstanceOf(EmailAlreadyExistsException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun udpate_user_profile() {
        // given
        val users = mockk<Users>(relaxed = true)
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val user = RegisteredUserBuilder.DEFAULT
        val email = Email("newEmail@gmail.com")
        every { users.exists(email) }.returns(false)
        val password = UnencryptedPassword("strong@1Pass")
        val encryptedPassword = CryptedPassword("cryptedPass")
        every { passwords.encrypt(password) }.returns(encryptedPassword)
        val firstName = FirstName("John")
        val lastName = LastName("Doe")
        val command = UpdateProfileCommand(user, email, Optional.of(password), firstName, lastName)

        // when
        appService.updateProfile(command)

        // then
        val captor = slot<RegisteredUser>()
        verify { users.update(capture(captor)) }
        val result = captor.captured
        assertThat(result.id).isEqualTo(user.id)
        assertThat(result.username).isEqualTo(user.username)
        assertThat(result.email).isEqualTo(email)
        assertThat(result.cryptedPassword).isEqualTo(encryptedPassword)
        assertThat(result.firstName).isEqualTo(firstName)
        assertThat(result.lastName).isEqualTo(lastName)
    }

    @Test(groups = [TestGroup.UNIT])
    fun udpate_user_profile_without_password() {
        // given
        val users = mockk<Users>(relaxed = true)
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val user = RegisteredUserBuilder.DEFAULT
        val email = Email("newEmail@gmail.com")
        every { users.exists(email) }.returns(false)
        val firstName = FirstName("John")
        val lastName = LastName("Doe")
        val command = UpdateProfileCommand(user, email, Optional.empty(), firstName, lastName)

        // when
        appService.updateProfile(command)

        // then
        val captor = slot<RegisteredUser>()
        verify { users.update(capture(captor)) }
        val result = captor.captured
        assertThat(result.cryptedPassword).isEqualTo(user.cryptedPassword)
    }
}