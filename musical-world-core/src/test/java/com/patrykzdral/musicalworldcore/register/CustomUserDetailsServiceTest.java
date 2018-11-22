package com.patrykzdral.musicalworldcore.register;

import com.patrykzdral.musicalworldcore.persistance.entity.Role;
import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.persistance.repository.UserRepository;
import com.patrykzdral.musicalworldcore.services.user.service.impl.CustomUserDetailsService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomUserDetailsServiceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private UserRepository userRepository;

    private UserDetailsService userDetailsService;

    @Before
    public void setup(){
        userDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    public void shouldBeUsernameNotFoundExceptionThrown(){
        //given
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        //expect
        thrown.expect(UsernameNotFoundException.class);
        //thrown.expect(hasProperty(ERROR_CODE,is(CONFIRMATION_ERROR)));
        thrown.expectMessage("User with the name patryk not found in the database");

        //when
        userDetailsService.loadUserByUsername("patryk");
    }

    @Test
    public void shouldBeUserDetailsReturned(){
        //given
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(User.builder()
                .username("patryk")
                .password("password")
                .confirmed(false)
                .rememberMe(false)
                .email("patrykz13@gmail.com")
                .roles(Collections.singleton(Role.builder()
                        .name("user")
                        .build()))
                .build()));

        //when
        UserDetails userDetails = userDetailsService.loadUserByUsername("patryk");

        //then
        Assert.assertNotNull(userDetails);
    }
}
