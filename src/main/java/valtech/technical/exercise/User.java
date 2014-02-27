package valtech.technical.exercise;

import java.util.ArrayList;
import java.util.List;

/**
 * A user who can have a list of posted messages as well as a list of other
 * users he/she is following.
 */
public class User {

    private final List<User> follows = new ArrayList<>();
    private final List<Post> posts = new ArrayList<>();

    public void addPost(Post post) {
        posts.add(post);
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void addFollow(User follow) {
        follows.add(follow);
    }

    public List<User> getFollows() {
        return follows;
    }
}
