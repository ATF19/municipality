package com.municipality.backend.application.user

import com.municipality.backend.domain.model.core.DEFAULT_PAGE_SIZE
import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.core.PageSize
import com.municipality.backend.domain.model.core.error.InsufficientPermissionException
import com.municipality.backend.domain.model.district.DistrictId
import com.municipality.backend.domain.model.municipality.MunicipalityId
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
            false
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
            false
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
            false
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
            false
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
            false
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
            false
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
            false
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
            true
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

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_non_admin_tries_to_load_all_users() {
        // given
        val users = mockk<Users>(relaxed = true)
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val user = SystemUser()

        // when

        // then
        assertThatThrownBy { appService.all(user, PageNumber(0), PageSize(10)) }
            .isInstanceOf(InsufficientPermissionException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_all_users() {
        // given
        val users = mockk<Users>()
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val user = RegisteredUserBuilder().admin().build()
        val user1 = RegisteredUserBuilder().build()
        val user2 = RegisteredUserBuilder().build()
        val user3 = RegisteredUserBuilder().build()
        val user4 = RegisteredUserBuilder().build()
        val page = Page(
            listOf(user1, user2, user3, user4),
            PageNumber(0), DEFAULT_PAGE_SIZE, 10
        )
        every { users.all(PageNumber(0), DEFAULT_PAGE_SIZE) }.returns(page)

        // when
        val all = appService.all(user, PageNumber(0), DEFAULT_PAGE_SIZE)

        // then
        assertThat(all).isEqualTo(page)
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_non_admin_tries_to_update_internal_user() {
        // given
        val users = mockk<Users>(relaxed = true)
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val user = SystemUser()
        val email = Email("newEmail@gmail.com")
        every { users.exists(email) }.returns(false)
        val password = UnencryptedPassword("strong@1Pass")
        val encryptedPassword = CryptedPassword("cryptedPass")
        every { passwords.encrypt(password) }.returns(encryptedPassword)
        val firstName = FirstName("John")
        val lastName = LastName("Doe")
        val command = UpdateInternalUserCommand(user, email, Optional.of(password), firstName, lastName, true)

        // when

        // then
        assertThatThrownBy { appService.updateInternalUser(command) }
            .isInstanceOf(InsufficientPermissionException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun update_internal_user() {
        // given
        val users = mockk<Users>(relaxed = true)
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val user = RegisteredUserBuilder().admin().build()
        val email = Email("newEmail@gmail.com")
        every { users.exists(email) }.returns(false)
        val password = UnencryptedPassword("strong@1Pass")
        val encryptedPassword = CryptedPassword("cryptedPass")
        every { passwords.encrypt(password) }.returns(encryptedPassword)
        val firstName = FirstName("John")
        val lastName = LastName("Doe")
        val command = UpdateInternalUserCommand(user, email, Optional.of(password), firstName, lastName, true)

        // when
        appService.updateInternalUser(command)

        // then
        val captor = slot<RegisteredUser>()
        verify { users.update(capture(captor)) }
        val result = captor.captured
        assertThat(result.isAdmin()).isTrue
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_no_admin_tries_to_delete_a_user() {
        // given
        val users = mockk<Users>(relaxed = true)
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val user = SystemUser()
        val toDelete = RegisteredUserBuilder().build()
        val command = DeleteUserCommand(user, toDelete.id)

        // when

        // then
        assertThatThrownBy { appService.deleteUser(command) }
            .isInstanceOf(InsufficientPermissionException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun delete_a_user() {
        // given
        val users = mockk<Users>(relaxed = true)
        val passwords = mockk<Passwords>()
        val sessions = mockk<Sessions>()
        val appService = UserAppService(users, passwords, sessions)
        val user = RegisteredUserBuilder().admin().build()
        val toDelete = RegisteredUserBuilder().build()
        val command = DeleteUserCommand(user, toDelete.id)
        every { users.by(toDelete.id) }.returns(toDelete)

        // when
        appService.deleteUser(command)

        // then
        verify { users.delete(toDelete) }
    }
}