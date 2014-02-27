package valtech.technical.exercise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * The "real" application. Reads from STDIN and prints to STDOUT. Can be safely
 * terminated by typing the "quit" command.
 *
 * Users will be automatically created on their first posting.
 *
 * Some methods are package private for testing. As for the time being there is
 * no need for public methods except
 * {@link valtech.technical.exercise.FakeTwitter#run()}
 */
public class FakeTwitter {

    // Users known to FakeTwitter
    private HashMap<String, User> users = new HashMap<String, User>();

    public void run() {
        System.out.println("Welcome to Fake Twitter!");
        System.out.println("Please type your commands...");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            String input = in.readLine();

            while (input != null) {
                Command c = retrieveCommand(input);

                switch (c) {
                    case POST:
                        post(input);
                        break;
                    case FOLLOW:
                        follow(input);
                        break;
                    case WALL:
                        wall(input);
                        break;
                    case READ:
                        read(input);
                        break;
                    case QUIT:
                        quit();
                        break;
                    default:
                        unknown(input);
                }

                input = in.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Try to identify the command the user was giving on STDIN.
     *
     * @param input The user's input.
     * @return The {@link valtech.technical.exercise.Command}
     */
    static Command retrieveCommand(String input) {
        Command result = Command.UNKNOWN;
        if (input != null && input.length() > 0) {
            result = Command.READ;
            for (Command c : Command.values()) {
                if (c.id() != null && c.id().length() > 0 && input.contains(c.id())) {
                    result = c;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Add the posted message to the post of the user if he/she can be
     * identified.
     *
     * @param input The user's input like "username -> posted message"
     */
    void post(String input) {
        String username = input.substring(0, input.indexOf(Command.POST.id())).trim();
        String message = input.substring(input.indexOf(Command.POST.id()) + Command.POST.id().length()).trim();

        if (username.length() < 1) {
            System.out.println("NO USER GIVEN FOR POSTING!");
        } else if (message.length() < 1) {
            System.out.println("NO MESSAGE GIVEN FOR POSTING!");
        } else {
            User user = users.get(username);
            if (user == null) {
                user = new User();
            }
            user.addPost(new Post(message, username));
            users.put(username, user);
        }
    }

    /**
     * Adds the second given user - if existent - to the list of users to follow
     * of the first given user - if existent.
     *
     * @param input The user's input like "Bob follows Charlie"
     */
    void follow(String input) {
        String username = input.substring(0, input.indexOf(Command.FOLLOW.id())).trim();
        String follows = input.substring(input.indexOf(Command.FOLLOW.id()) + Command.FOLLOW.id().length()).trim();

        User user = users.get(username);
        User follow = users.get(follows);

        if (user != null && follow != null) {
            user.addFollow(follow);
        } else {
            System.out.println("UNKNOWN USER OR FOLLOW-USER");
        }
    }

    private void wall(String input) {
        for (Post post : getWallPosts(input)) {
            System.out.println(String.format("%s - %s (%s)",
                    post.getUsername(), post.getMessage(), post.getAge()));
        }
    }

    /**
     * Return an sorted set of posts to be displayed on the wall of the given
     * user - if existent.
     *
     * @param input The user's input like "Bob wall"
     * @return A sorted set of all the posts for the user's wall.
     */
    SortedSet<Post> getWallPosts(String input) {
        TreeSet<Post> posts = new TreeSet<>();
        String username = input.substring(0, input.indexOf(Command.WALL.id())).trim();

        User user = users.get(username);

        if (user != null) {

            posts.addAll(user.getPosts());

            for (User f : user.getFollows()) {
                posts.addAll(f.getPosts());
            }
        } else {
            System.out.println("NO POSTS FOR THIS USER");
        }

        return posts;
    }

    private void read(String input) {
        for (Post post : getReadingPosts(input)) {
            System.out.println(String.format("%s (%s)", post.getMessage(), post.getAge()));
        }
    }

    /**
     * Return an sorted set of posts to be read for the given user - if
     * existent.
     *
     * @param input The user's input representing an existent username like
     * "Bob"
     * @return A sorted set of all the posts for the given user.
     */
    TreeSet<Post> getReadingPosts(String input) {
        TreeSet<Post> posts = new TreeSet<>();
        User user = users.get(input.trim());

        if (user != null) {
            posts.addAll(user.getPosts());
        } else {
            System.out.println("UNKNOWN USER");
        }

        return posts;
    }

    private void quit() {
        System.out.println("Good bye!");
        System.exit(0);
    }

    private void unknown(String input) {
        System.out.println(String.format("UNKNOWN OR EMPTY COMMAND: %s", input));
    }

    HashMap<String, User> getUsers() {
        return users;
    }
}
