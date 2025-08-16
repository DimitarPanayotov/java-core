package bg.sofia.uni.fmi.mjt.socialnetwork.profile;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

public class DefaultUserProfile implements UserProfile {
    private String username;
    private Set<Interest> interests;
    private Set<UserProfile> friends;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DefaultUserProfile that = (DefaultUserProfile) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }

    private void validateInterestsInput(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException("Cannot add null interest!");
        }
    }

    private void validateUserInput(UserProfile userProfile) {
        if (this.equals(userProfile) || userProfile == null) {
            throw new IllegalArgumentException("Cannot add yourself or null as a friend!");
        }
    }

    public DefaultUserProfile(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank!");
        }
        this.username = username;
        this.interests = new HashSet<>();
        this.friends = new HashSet<>();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<Interest> getInterests() {
        return Collections.unmodifiableSet(interests);
    }

    @Override
    public boolean addInterest(Interest interest) {
        validateInterestsInput(interest);

        if (interests.contains(interest)) {
            return false;
        } else {
            interests.add(interest);
            return true;
        }
    }

    @Override
    public boolean removeInterest(Interest interest) {
        validateInterestsInput(interest);

        if (interests.contains(interest)) {
            interests.remove(interest);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Collection<UserProfile> getFriends() {
        return Collections.unmodifiableSet(friends);
    }

    @Override
    public boolean addFriend(UserProfile userProfile) {
        validateUserInput(userProfile);

        if (friends.contains(userProfile)) {
            return false;
        } else {
            friends.add(userProfile);
            userProfile.addFriend(this);
            return true;
        }
    }

    @Override
    public boolean unfriend(UserProfile userProfile) {
        validateUserInput(userProfile);

        if (friends.contains(userProfile)) {
            friends.remove(userProfile);
            userProfile.unfriend(this);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isFriend(UserProfile userProfile) {
        validateUserInput(userProfile);

        return friends.contains(userProfile);
    }
}
