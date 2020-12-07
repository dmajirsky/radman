package software.netcore.radman.buisness.service.user.system;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import software.netcore.radman.buisness.service.attribute.AttributeService;
import software.netcore.radman.buisness.service.user.system.dto.AuthProviderDto;
import software.netcore.radman.buisness.service.user.system.dto.RoleDto;
import software.netcore.radman.buisness.service.user.system.dto.SystemUserDto;
import software.netcore.radman.data.internal.entity.AuthProvider;
import software.netcore.radman.data.internal.entity.Role;
import software.netcore.radman.data.internal.entity.SystemUser;
import software.netcore.radman.data.internal.repo.RadCheckAttributeRepo;
import software.netcore.radman.data.internal.repo.RadReplyAttributeRepo;
import software.netcore.radman.data.internal.repo.SystemUserRepo;
import software.netcore.radman.data.radius.repo.RadCheckRepo;
import software.netcore.radman.data.radius.repo.RadGroupCheckRepo;
import software.netcore.radman.data.radius.repo.RadGroupReplyRepo;
import software.netcore.radman.data.radius.repo.RadReplyRepo;

import java.util.Objects;

@RunWith(MockitoJUnitRunner.class)
public class SystemUserServiceTest {

    private SystemUserService systemUserServiceMock = Configuration.userService;
    private SystemUserRepo systemUserRepoMock = Configuration.systemUserRepoMock;
    private ConversionService conversionServiceMock = Configuration.conversionService;

    private SystemUserDto userDto;
    private SystemUser user;

    @Before
    public void before() {
        userDto = new SystemUserDto();
        userDto.setId(1L);
        userDto.setUsername("user1");
        userDto.setPassword("passwd");
        userDto.setRole(RoleDto.ADMIN);
        userDto.setAuthProvider(AuthProviderDto.LOCAL);

        user = new SystemUser();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setRole(Role.ADMIN);
        user.setAuthProvider(AuthProvider.LOCAL);
    }

    @After
    public void after() {
        Mockito.reset(systemUserRepoMock);
        Mockito.reset(conversionServiceMock);
    }

    @Test
    public void test_01_createUser() {
        Mockito.when(conversionServiceMock.convert(userDto, SystemUser.class)).thenReturn(user);
        systemUserServiceMock.createSystemUser(userDto);
        Mockito.verify(systemUserRepoMock).save(user);
    }

    @Test
    public void test_02_updateUser() {
        userDto.setUsername("newName");
        user.setUsername(userDto.getUsername());
        Mockito.when(conversionServiceMock.convert(userDto, SystemUser.class)).thenReturn(user);
        systemUserServiceMock.updateSystemUser(userDto);
        Mockito.verify(systemUserRepoMock).save(user);
    }

    @Test
    public void test_03_deleteUser() {
        systemUserServiceMock.deleteSystemUser(userDto);
        Mockito.verify(systemUserRepoMock).deleteById(Mockito.any(Long.class));
    }

    private static class Configuration {
        static ConversionService conversionService = Mockito.mock(ConversionService.class);
        static PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        static SystemUserRepo systemUserRepoMock = Mockito.mock(SystemUserRepo.class);

        static SystemUserService userService = new SystemUserService(systemUserRepoMock, conversionService, passwordEncoder);
    }

}
