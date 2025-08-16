package bg.sofia.uni.fmi.mjt.socialnetwork;

import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.SocialFeedPost;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Comparator;

public class SocialNetworkImpl implements SocialNetwork {
    private Set<UserProfile> users;
    private List<Post> posts;

    public SocialNetworkImpl() {
        this.users = new HashSet<>();
        this.posts = new ArrayList<>();
    }

    private Set<UserProfile> findFriendsNetwork(UserProfile start) {
        Set<UserProfile> visited = new HashSet<>();
        Queue<UserProfile> queue = new ArrayDeque<>();

        visited.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {
            UserProfile curr = queue.poll();

            for (UserProfile friend : curr.getFriends()) {
                if (!visited.contains(friend)) {
                    visited.add(friend);
                    queue.add(friend);
                }
            }
        }

        return visited;
    }

    @Override
    public void registerUser(UserProfile userProfile) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile cannot be null!");
        }
        if (users.contains(userProfile)) {
            throw new UserRegistrationException("User is already registered!");
        }
        users.add(userProfile);
    }

    @Override
    public Set<UserProfile> getAllUsers() {
        return Collections.unmodifiableSet(users);
    }

    @Override
    public Post post(UserProfile userProfile, String content) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile cannot be null!");
        }
        if (content == null) {
            throw new IllegalArgumentException("Post content cannot be null!");
        }
        if (!users.contains(userProfile)) {
            throw new UserRegistrationException("User is not registered!");
        }
        Post newPost = new SocialFeedPost(userProfile, content);
        posts.add(newPost);
        return newPost;
    }

    @Override
    public Collection<Post> getPosts() {
        return Collections.unmodifiableList(posts);
    }

    @Override
    public Set<UserProfile> getReachedUsers(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null");
        }

        UserProfile author = post.getAuthor();
        Set<UserProfile> friendsNetwork = findFriendsNetwork(author);

        Set<UserProfile> reached = new HashSet<>();
        for (UserProfile user : friendsNetwork) {
            if (!user.equals(author) && !Collections.disjoint(user.getInterests(), author.getInterests())) {
                reached.add(user);
            }
        }
        return reached;
    }

    @Override
    public Set<UserProfile> getMutualFriends(UserProfile userProfile1, UserProfile userProfile2) throws UserRegistrationException {
        if (userProfile1 == null || userProfile2 == null) {
            throw new IllegalArgumentException("Users cannot be null");
        }
        if (!users.contains(userProfile1) || !users.contains(userProfile2)) {
            throw new UserRegistrationException("One or both users not registered");
        }

        Set<UserProfile> mutual = new HashSet<>(userProfile1.getFriends());
        mutual.retainAll(userProfile2.getFriends());
        return mutual;
    }

    @Override
    public SortedSet<UserProfile> getAllProfilesSortedByFriendsCount() {
        SortedSet<UserProfile> sorted = new TreeSet<>(new Comparator<UserProfile>() {
            @Override
            public int compare(UserProfile o1, UserProfile o2) {
                int friendsComparison = Integer.compare(o2.getFriends().size(), o1.getFriends().size());
                if (friendsComparison == 0) {
                    return o1.getUsername().compareTo(o2.getUsername());
                }
                return friendsComparison;
            }
        });

        sorted.addAll(users);
        return sorted;
    }

}
