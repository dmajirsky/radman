package software.netcore.radman.buisness.service.attribute;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.ConversionService;
import software.netcore.radman.buisness.service.attribute.dto.AuthenticationAttributeDto;
import software.netcore.radman.buisness.service.attribute.dto.AuthorizationAttributeDto;
import software.netcore.radman.data.internal.entity.RadCheckAttribute;
import software.netcore.radman.data.internal.entity.RadReplyAttribute;
import software.netcore.radman.data.internal.repo.RadCheckAttributeRepo;
import software.netcore.radman.data.internal.repo.RadReplyAttributeRepo;
import software.netcore.radman.data.radius.repo.RadCheckRepo;
import software.netcore.radman.data.radius.repo.RadGroupCheckRepo;
import software.netcore.radman.data.radius.repo.RadGroupReplyRepo;
import software.netcore.radman.data.radius.repo.RadReplyRepo;

public class AttributeServiceTest {

    private AttributeService attributeService = Configuration.attributeService;
    private RadCheckAttributeRepo radCheckAttributeRepo = Configuration.checkAttributeRepo;
    private RadReplyAttributeRepo radReplyAttributeRepo = Configuration.replyAttributeRepo;
    private ConversionService conversionServiceMock = AttributeServiceTest.Configuration.conversionService;
    private RadCheckRepo radCheckRepo = Configuration.radCheckRepo;
    private RadReplyRepo radReplyRepo = Configuration.radReplyRepo;
    private RadGroupCheckRepo radGroupCheckRepo = Configuration.radGroupCheckRepo;
    private RadGroupReplyRepo radGroupReplyRepo = Configuration.radGroupReplyRepo;

    private AuthenticationAttributeDto authDto;
    private RadCheckAttribute radCheckAttribute;
    private AuthorizationAttributeDto autzDto;
    private RadReplyAttribute radReplyAttribute;

    @Before
    public void before() {
        authDto = new AuthenticationAttributeDto();
        authDto.setId(1L);
        authDto.setName("authName");

        radCheckAttribute = new RadCheckAttribute();
        radCheckAttribute.setDescription(authDto.getDescription());
        radCheckAttribute.setSensitiveData(false);

        autzDto = new AuthorizationAttributeDto();
        autzDto.setId(1L);
        autzDto.setName("autzName");

        radReplyAttribute = new RadReplyAttribute();
        radReplyAttribute.setDescription(autzDto.getDescription());
        radReplyAttribute.setSensitiveData(false);

    }

    @After
    public void after() {
        Mockito.reset(radCheckAttributeRepo);
        Mockito.reset(radReplyAttributeRepo);
        Mockito.reset(conversionServiceMock);
    }

    @Test
    public void test_01_createAuthAttribute() {
        Mockito.when(conversionServiceMock.convert(authDto, RadCheckAttribute.class)).thenReturn(radCheckAttribute);
        attributeService.createAuthenticationAttribute(authDto);
        Mockito.verify(radCheckAttributeRepo).save(radCheckAttribute);
    }

    @Test
    public void test_02_createAutzAttribute() {
        Mockito.when(conversionServiceMock.convert(autzDto, RadReplyAttribute.class)).thenReturn(radReplyAttribute);
        attributeService.createAuthorizationAttribute(autzDto);
        Mockito.verify(radReplyAttributeRepo).save(radReplyAttribute);
    }

    @Test
    public void test_03_updateAuthAttribute() {
        authDto.setName("newName");
        radCheckAttribute.setName(authDto.getName());
        Mockito.when(conversionServiceMock.convert(authDto, RadCheckAttribute.class)).thenReturn(radCheckAttribute);
        attributeService.updateAuthenticationAttribute(authDto);
        Mockito.verify(radCheckAttributeRepo).save(radCheckAttribute);
    }

    @Test
    public void test_04_updateAutzAttribute() {
        autzDto.setName("newName");
        radReplyAttribute.setName(autzDto.getName());
        Mockito.when(conversionServiceMock.convert(autzDto, RadReplyAttribute.class)).thenReturn(radReplyAttribute);
        attributeService.updateAuthorizationAttribute(autzDto);
        Mockito.verify(radReplyAttributeRepo).save(radReplyAttribute);
    }

    @Test
    public void test_05_deleteAuthAttribute() {
        attributeService.deleteAuthenticationAttribute(authDto, true);
        Mockito.verify(radCheckAttributeRepo).deleteById(authDto.getId());
        Mockito.verify(radCheckRepo).deleteAllByAttribute(authDto.getName());
        Mockito.verify(radGroupCheckRepo).deleteAllByAttribute(authDto.getName());
    }

    @Test
    public void test_06_deleteAutzAttribute() {
        attributeService.deleteAuthorizationAttribute(autzDto, true);
        Mockito.verify(radReplyAttributeRepo).deleteById(autzDto.getId());
        Mockito.verify(radReplyRepo).deleteAllByAttribute(autzDto.getName());
        Mockito.verify(radGroupReplyRepo).deleteAllByAttribute(autzDto.getName());
    }

    private static class Configuration {
        static RadCheckAttributeRepo checkAttributeRepo = Mockito.mock(RadCheckAttributeRepo.class);
        static RadReplyAttributeRepo replyAttributeRepo = Mockito.mock(RadReplyAttributeRepo.class);

        static RadCheckRepo radCheckRepo = Mockito.mock(RadCheckRepo.class);
        static RadReplyRepo radReplyRepo = Mockito.mock(RadReplyRepo.class);
        static RadGroupCheckRepo radGroupCheckRepo = Mockito.mock(RadGroupCheckRepo.class);
        static RadGroupReplyRepo radGroupReplyRepo = Mockito.mock(RadGroupReplyRepo.class);

        static ConversionService conversionService = Mockito.mock(ConversionService.class);

        static AttributeService attributeService = new AttributeService(checkAttributeRepo, replyAttributeRepo,
                radCheckRepo, radReplyRepo, radGroupCheckRepo, radGroupReplyRepo, conversionService);
    }

}
