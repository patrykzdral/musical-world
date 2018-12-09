package com.patrykzdral.musicalworldcore.services.friendship.service.impl;

import com.patrykzdral.musicalworldcore.persistance.entity.Friendship;
import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.persistance.repository.FriendshipRepository;
import com.patrykzdral.musicalworldcore.persistance.repository.UserRepository;
import com.patrykzdral.musicalworldcore.services.friendship.dto.FriendWithPhotoDTO;
import com.patrykzdral.musicalworldcore.services.friendship.dto.FriendshipListsDTO;
import com.patrykzdral.musicalworldcore.services.friendship.service.FriendshipService;
import com.patrykzdral.musicalworldcore.services.user.model.UserWithPhotoDTO;
import com.patrykzdral.musicalworldcore.validation.exception.ApplicationException;
import com.patrykzdral.musicalworldcore.validation.exception.ExceptionCode;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FriendshipImpl implements FriendshipService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    public FriendshipImpl(UserRepository userRepository, FriendshipRepository friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    @Transactional
    public void deleteFriend(String username, Long friendId) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.EXCEPTION_0012,
                        "UserEntity with email provided from authentication details is not exist"));

        var friend = userRepository.findById(friendId)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.EXCEPTION_0012,
                        "FriendEntity with email provided from authentication details is not exist"));

        var friendshipFromMe = friendshipRepository.findFriendshipEntityByUserAndAcceptedAndFromMeAndFriend(user, true, true, friend);
        var friendshipFromFriend = friendshipRepository.findFriendshipEntityByUserAndAcceptedAndFromMeAndFriend(friend, true, false, user);

        friendshipRepository.delete(friendshipFromMe);
        friendshipRepository.delete(friendshipFromFriend);
    }

    @Override
    public void inviteFriend(String username, Long friendId) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.EXCEPTION_0012,
                        "User does not exists"));

        var friend = userRepository.findById(friendId)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.EXCEPTION_0012,
                        "Friend does not exists"));

        friendshipRepository.save(Friendship.builder()
                .user(user)
                .friend(friend)
                .accepted(false)
                .fromMe(true)
                .build());

        friendshipRepository.save(Friendship.builder()
                .user(friend)
                .friend(user)
                .accepted(false)
                .fromMe(false)
                .build());
    }

    @Override
    @Transactional
    public void acceptFriend(String username, Long friendId) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.EXCEPTION_0012,
                        "User does not exists"));

        var friend = userRepository.findById(friendId)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.EXCEPTION_0012,
                        "Friend does not exists"));

        var friendshipFromMe = friendshipRepository.findFriendshipEntityByUserAndAcceptedAndFromMeAndFriend(user, false, false, friend);
        var friendshipFromFriend = friendshipRepository.findFriendshipEntityByUserAndAcceptedAndFromMeAndFriend(friend, false, true, user);

        friendshipFromMe.setAccepted(true);
        friendshipFromFriend.setAccepted(true);
    }

    @Override
    @Transactional
    public void rejectInvitation(String username, Long friendId) {
        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.EXCEPTION_0012,
                        "User does not exists"));

        var friend = userRepository.findById(friendId)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.EXCEPTION_0012,
                        "Friend does not exists"));

        var friendshipFromMe = friendshipRepository.findFriendshipEntityByUserAndAcceptedAndFromMeAndFriend(user, false, false, friend);
        var friendshipFromFriend = friendshipRepository.findFriendshipEntityByUserAndAcceptedAndFromMeAndFriend(friend, false, true, user);

        friendshipRepository.delete(friendshipFromMe);
        friendshipRepository.delete(friendshipFromFriend);
    }

    @Override
    @Transactional
    public void cancelInvitation(String username, Long friendId) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.EXCEPTION_0012,
                        "User does not exists"));

        var friend = userRepository.findById(friendId)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.EXCEPTION_0012,
                        "Friend does not exists"));

        var friendshipFromMe = friendshipRepository.findFriendshipEntityByUserAndAcceptedAndFromMeAndFriend(user, false, true, friend);
        var friendshipFromFriend = friendshipRepository.findFriendshipEntityByUserAndAcceptedAndFromMeAndFriend(friend, false, false, user);

        friendshipRepository.delete(friendshipFromMe);
        friendshipRepository.delete(friendshipFromFriend);
    }

    @Override
    @Transactional
    public FriendshipListsDTO getFriendshipLists(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.EXCEPTION_0012,
                        "User does not exists"));

        var acceptedFriendships = friendshipRepository.findAllByUserAndAccepted(user, true);
        acceptedFriendships.stream().filter(friend -> !Objects.nonNull(friend.getFriend()))
                .forEach(friendshipRepository::delete);
        acceptedFriendships = acceptedFriendships.stream().filter(friend -> Objects.nonNull(friend.getFriend()))
                .collect(Collectors.toList());

        var friendsWhoDidNotAcceptMe = friendshipRepository.findAllByUserAndAcceptedAndFromMe(user, false, true);
        friendsWhoDidNotAcceptMe.stream().filter(friend -> !Objects.nonNull(friend.getFriend()))
                .forEach(friendshipRepository::delete);
        friendsWhoDidNotAcceptMe = friendsWhoDidNotAcceptMe.stream().filter(friend -> Objects.nonNull(friend.getFriend()))
                .collect(Collectors.toList());

        var friendsIDidNotAccept = friendshipRepository.findAllByUserAndAcceptedAndFromMe(user, false, false);
        friendsIDidNotAccept.stream().filter(friend -> !Objects.nonNull(friend.getFriend()))
                .forEach(friendshipRepository::delete);
        friendsIDidNotAccept = friendsIDidNotAccept.stream().filter(friend -> Objects.nonNull(friend.getFriend()))
                .collect(Collectors.toList());

        var allFriendships = new ArrayList<Friendship>();
        allFriendships.addAll(acceptedFriendships);
        allFriendships.addAll(friendsWhoDidNotAcceptMe);
        allFriendships.addAll(friendsIDidNotAccept);

        var outsideOfFriendshipsUsers = userRepository.findAllByIdIsNot(user.getId()).stream()
                .filter(userEntity -> !checkIfUserIsInFriendship(userEntity, allFriendships))
                .collect(Collectors.toList());

        return FriendshipListsDTO.builder()
                .friends(acceptedFriendships.stream().map(friendship -> FriendWithPhotoDTO.builder()
                        .friendshipId(friendship.getId())
                        .username(friendship.getFriend().getUsername())
                        .friendId(friendship.getFriend().getId())
                        .firstName(friendship.getFriend().getFirstName())
                        .lastName(friendship.getFriend().getLastName())
                        .email(friendship.getFriend().getEmail())
                        .build())
                        .collect(Collectors.toList()))
                .friendsWhoDidNotAcceptMe(friendsWhoDidNotAcceptMe.stream().map(friendship -> FriendWithPhotoDTO.builder()
                        .friendshipId(friendship.getId())
                        .username(friendship.getFriend().getUsername())
                        .friendId(friendship.getFriend().getId())
                        .firstName(friendship.getFriend().getFirstName())
                        .lastName(friendship.getFriend().getLastName())
                        .email(friendship.getFriend().getEmail())
                        .build())
                        .collect(Collectors.toList()))
                .friendsIDidNotAccept(friendsIDidNotAccept.stream().map(friendship -> FriendWithPhotoDTO.builder()
                        .friendshipId(friendship.getId())
                        .username(friendship.getFriend().getUsername())
                        .friendId(friendship.getFriend().getId())
                        .firstName(friendship.getFriend().getFirstName())
                        .lastName(friendship.getFriend().getLastName())
                        .email(friendship.getFriend().getEmail())
                        .build())
                        .collect(Collectors.toList()))
                .outsideOfFriendshipsUsers(outsideOfFriendshipsUsers.stream().map(notFriend -> FriendWithPhotoDTO.builder()
                        .friendId(notFriend.getId())
                        .username(notFriend.getUsername())
                        .firstName(notFriend.getFirstName())
                        .lastName(notFriend.getLastName())
                        .email(notFriend.getEmail())
                        .build())
                        .collect(Collectors.toList())).build();
    }

    private boolean checkIfUserIsInFriendship(User userEntity, List<Friendship> allFriendships) {
        return allFriendships.stream().anyMatch(friend -> friend.getFriend().getId().equals(userEntity.getId()));
    }
}
