package com.patrykzdral.musicalworldcore.register;

import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.persistance.entity.VerificationToken;
import com.patrykzdral.musicalworldcore.persistance.repository.RoleRepository;
import com.patrykzdral.musicalworldcore.persistance.repository.VerificationTokenRepository;
import com.patrykzdral.musicalworldcore.persistance.repository.UserRepository;
import com.patrykzdral.musicalworldcore.validation.exception.ApplicationException;
import com.patrykzdral.musicalworldcore.services.user.model.RegisterUserRequestDTO;
import com.patrykzdral.musicalworldcore.services.user.service.RegisterUserService;
import com.patrykzdral.musicalworldcore.services.user.service.impl.RegisterUserServiceImpl;
import com.patrykzdral.musicalworldcore.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class RegisterUserServiceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private RegisterUserService registerUserService = null;
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @Mock
    private Clock clock;

    @Before
    public void setup(){
        registerUserService = new RegisterUserServiceImpl(roleRepository,passwordEncoder,userRepository, verificationTokenRepository, clock);
    }

    @Test
    public void shouldBeTokenExpiredReturned(){
        //given
        when(clock.instant()).thenReturn(TestUtils.getInstant(1996,12,12,12,12,12));
        when(verificationTokenRepository.findByToken(any())).thenReturn(Optional.of(VerificationToken
                .builder()
                .expiryDate(TestUtils.getZonedTime(1995,12,12,12,12,12))
                .build()));
        //when
        String validation = registerUserService.validateVerificationToken("example token");

        //then
        Assert.assertEquals(validation,"expired");
        Assert.assertNull(null);
    }

    @Test
    public void shouldBeTokenNotValidReturned(){
        //given
        when(verificationTokenRepository.findByToken(any())).thenReturn(Optional.empty());
        //when
        String validation = registerUserService.validateVerificationToken("example token");

        //then
        Assert.assertEquals(validation,"invalidToken");
        Assert.assertNull(null);
    }

    @Test
    public void shouldBeValidTokenReturned(){
        //given
        when(clock.instant()).thenReturn(TestUtils.getInstant(2018,11,12,12,12,12));
        when(verificationTokenRepository.findByToken(any())).thenReturn(Optional.of(VerificationToken
                .builder()
                .expiryDate(TestUtils.getZonedTime(2019,12,12,12,12,12))
                .user(new User())
                .build()));
        //when
        String validation = registerUserService.validateVerificationToken("example token");

        //then
        Assert.assertEquals(validation,"valid");
        Assert.assertNull(null);
    }

    @Test
    public void shouldBeEmailExistsInternalExceptionThrown(){
        //given
        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(User.builder().build()));
        RegisterUserRequestDTO registerUserRequestDTO =RegisterUserRequestDTO.builder().build();
        //expect
        thrown.expect(ApplicationException.class);
        //thrown.expect(hasProperty(ERROR_CODE,is(CONFIRMATION_ERROR)));
        thrown.expectMessage("Email is already in DB");

        registerUserService.registerUserAccount(registerUserRequestDTO);
    }
    @Test
    public void shouldBeUsernameExistsInternalExceptionThrown(){
        //given
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(User.builder().build()));

        RegisterUserRequestDTO registerUserRequestDTO =RegisterUserRequestDTO.builder().build();
        //expect
        thrown.expect(ApplicationException.class);
        //thrown.expect(hasProperty(ERROR_CODE,is(CONFIRMATION_ERROR)));
        thrown.expectMessage("Username is already in DB");

        registerUserService.registerUserAccount(registerUserRequestDTO);
    }

    @Test
    public void shouldBeRegistered(){
        //given
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(User.builder().build());
        RegisterUserRequestDTO registerUserRequestDTO =RegisterUserRequestDTO.builder().email("example@emial.com").password("1234Oko32!").username("12345").build();

        //when
        User user =registerUserService.registerUserAccount(registerUserRequestDTO);

        //then
        Assert.assertNotNull(user);
    }

}
