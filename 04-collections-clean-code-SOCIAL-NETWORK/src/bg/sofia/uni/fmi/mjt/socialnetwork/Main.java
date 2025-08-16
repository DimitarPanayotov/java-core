package bg.sofia.uni.fmi.mjt.socialnetwork;

import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.ReactionType;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.DefaultUserProfile;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.Interest;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.Set;
import java.util.SortedSet;

public class Main {
    @SuppressWarnings("checkstyle:MethodLength")
    public static void main(String[] args) {
        SocialNetworkImpl network = new SocialNetworkImpl();

        UserProfile alice = new DefaultUserProfile("alice");
        UserProfile bob = new DefaultUserProfile("bob");
        UserProfile carol = new DefaultUserProfile("carol");
        UserProfile dave = new DefaultUserProfile("dave");
        UserProfile eve = new DefaultUserProfile("eve");

        try {
            network.registerUser(alice);
            network.registerUser(bob);
            network.registerUser(carol);
            network.registerUser(dave);
            network.registerUser(eve);
        } catch (UserRegistrationException e) {
            System.out.println("Registration error: " + e.getMessage());
        }

        alice.addInterest(Interest.MUSIC);
        alice.addInterest(Interest.TRAVEL);
        bob.addInterest(Interest.MUSIC);
        carol.addInterest(Interest.TRAVEL);
        dave.addInterest(Interest.GAMES);
        eve.addInterest(Interest.MUSIC);
        eve.addInterest(Interest.BOOKS);

        alice.addFriend(bob);
        bob.addFriend(carol);
        carol.addFriend(dave);

        Post post1 = null;
        Post post2 = null;
        try {
            post1 = network.post(alice, "Listening to new music!");
            post2 = network.post(bob, "I love traveling!");
        } catch (Exception e) {
            System.out.println("Posting error: " + e.getMessage());
        }

        post1.addReaction(bob, ReactionType.LIKE);
        post1.addReaction(carol, ReactionType.LOVE);
        post1.addReaction(dave, ReactionType.LIKE);

        post2.addReaction(alice, ReactionType.DISLIKE);
        post2.addReaction(carol, ReactionType.LIKE);

        System.out.println("\nAll posts:");
        for (Post p : network.getPosts()) {
            System.out.println(p.getContent() + " by " + p.getAuthor().getUsername());
        }

        Set<UserProfile> reached = network.getReachedUsers(post1);
        System.out.println("\nUsers who can see Alice's post:");
        for (UserProfile user : reached) {
            System.out.println("- " + user.getUsername());
        }

        try {
            Set<UserProfile> mutual = network.getMutualFriends(alice, carol);
            System.out.println("\nMutual friends of Alice and Carol:");
            for (UserProfile u : mutual) {
                System.out.println("- " + u.getUsername());
            }
        } catch (UserRegistrationException e) {
            System.out.println("Error getting mutual friends: " + e.getMessage());
        }

        SortedSet<UserProfile> sorted = network.getAllProfilesSortedByFriendsCount();
        System.out.println("\nUsers sorted by number of friends:");
        for (UserProfile u : sorted) {
            System.out.println(u.getUsername() + " - " + u.getFriends().size() + " friends");
        }

        System.out.println("\nReactions to Alice's post:");
        for (var entry : post1.getAllReactions().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().size() + " reactions");
        }

        System.out.println("\nTotal reactions on Bob's post: " + post2.totalReactionsCount());

        post1.removeReaction(dave);
        System.out.println("After removing Dave's reaction, total = " + post1.totalReactionsCount());
    }
}
