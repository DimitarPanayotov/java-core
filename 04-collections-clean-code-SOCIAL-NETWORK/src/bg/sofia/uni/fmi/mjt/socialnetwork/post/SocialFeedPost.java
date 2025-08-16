package bg.sofia.uni.fmi.mjt.socialnetwork.post;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SocialFeedPost implements Post {
    private UserProfile author;
    private String content;

    private static int idNumber = 0;
    private String uniqueId;

    private LocalDateTime publishedOn;

    private Map<ReactionType, Set<UserProfile>> reactions;

    public SocialFeedPost(UserProfile author, String content) {
        if (author == null || content == null || content.isBlank()) {
            throw new IllegalArgumentException("Author and content of a post cannot be null or blank!");
        }
        this.author = author;
        this.content = content;
        this.uniqueId = "post-" + (++idNumber);
        this.publishedOn = LocalDateTime.now();
        this.reactions = new EnumMap<>(ReactionType.class);
    }

    @Override
    public String getUniqueId() {
        return uniqueId;
    }

    @Override
    public UserProfile getAuthor() {
        return author;
    }

    @Override
    public LocalDateTime getPublishedOn() {
        return publishedOn;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public boolean addReaction(UserProfile userProfile, ReactionType reactionType) {
        if (userProfile == null) {
            throw new IllegalArgumentException("Profile cannot be null!");
        }
        if (reactionType == null) {
            throw new IllegalArgumentException("Reaction type cannot be null!");
        }

        for (Map.Entry<ReactionType, Set<UserProfile>> entry : reactions.entrySet()) {
            if (entry.getValue().contains(userProfile)) {
                if (entry.getKey() == reactionType) {
                    return false;
                }
            }
            entry.getValue().remove(userProfile);
            break;
        }

        Set<UserProfile> usersReacted = reactions.get(reactionType);
        if (usersReacted == null) {
            usersReacted = new HashSet<>();
            reactions.put(reactionType, usersReacted);
        }

        usersReacted.add(userProfile);
        return true;
    }

    @Override
    public boolean removeReaction(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("User cannot be null!");
        }

        for (Map.Entry<ReactionType, Set<UserProfile>> entry : reactions.entrySet()) {
            Set<UserProfile> users = entry.getValue();
            if (users.contains(userProfile)) {
                users.remove(userProfile);
                if (users.isEmpty()) {
                    reactions.remove(entry.getKey());
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<ReactionType, Set<UserProfile>> getAllReactions() {
        return Collections.unmodifiableMap(reactions);
    }

    @Override
    public int getReactionCount(ReactionType reactionType) {
        if (reactionType == null) {
            throw new IllegalArgumentException("Reaction type cannot be null!");
        }

        Set<UserProfile> users = reactions.get(reactionType);
        if (users == null) {
            return 0;
        }

        return users.size();
    }

    @Override
    public int totalReactionsCount() {
        int count = 0;
        for (Map.Entry<ReactionType, Set<UserProfile>> entry : reactions.entrySet()) {
            count += entry.getValue().size();
        }
        return count;
    }
}
