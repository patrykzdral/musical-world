package com.patrykzdral.musicalworldcore.persistance.repository;

import com.patrykzdral.musicalworldcore.persistance.entity.Concert;
import com.patrykzdral.musicalworldcore.persistance.entity.Friendship;
import com.patrykzdral.musicalworldcore.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findAllByUserAndAccepted(User user, Boolean isAccepted);
    List<Friendship> findAllByUserAndAcceptedAndFromMe(User user, Boolean isAccepted, Boolean fromMe);
    Friendship findFriendshipEntityByUserAndAcceptedAndFromMeAndFriend(User user, Boolean isAccepted, Boolean fromMe, User friend);}
