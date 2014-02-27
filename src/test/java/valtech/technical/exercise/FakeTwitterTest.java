package valtech.technical.exercise;

import java.util.TreeSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests main functionality of the FakeTwitter application like command
 * retrieval and command execution.
 *
 * ATTENTION: Due to the design of the application to use STDOUT for output this
 * test will also produce outputs on STDOUT. This is considered bad practice!
 *
 */
public class FakeTwitterTest {

    private FakeTwitter fake;

    @Before
    public void setUp() {
        fake = new FakeTwitter();
    }

    @Test
    public void nullCommand() {
        assertThat(FakeTwitter.retrieveCommand(null), is(Command.UNKNOWN));
    }

    @Test
    public void emptyCommand() {
        assertThat(FakeTwitter.retrieveCommand(""), is(Command.UNKNOWN));
    }

    @Test
    public void quitCommand() {
        assertThat(FakeTwitter.retrieveCommand("quit"), is(Command.QUIT));
    }

    @Test
    public void postCommand() {
        assertThat(FakeTwitter.retrieveCommand("user -> message"), is(Command.POST));
    }

    @Test
    public void noPostCommand() {
        assertThat(FakeTwitter.retrieveCommand("user->message"), not(Command.POST));
    }

    @Test
    public void followCommand() {
        assertThat(FakeTwitter.retrieveCommand("user follows otherUser"), is(Command.FOLLOW));
    }

    @Test
    public void wallCommand() {
        assertThat(FakeTwitter.retrieveCommand("user wall"), is(Command.WALL));
    }

    @Test
    public void readCommand() {
        assertThat(FakeTwitter.retrieveCommand("user"), is(Command.READ));
    }

    @Test
    public void readMultipleNameCommand() {
        assertThat(FakeTwitter.retrieveCommand("multiple name user"), is(Command.READ));
    }

    @Test
    public void postNoUserNorMessage() {
        fake.post(" -> ");
        assertThat(fake.getUsers().size(), is(0));
    }

    @Test
    public void postNoUser() {
        fake.post(" -> hello");
        assertThat(fake.getUsers().size(), is(0));
    }

    @Test
    public void postNoMessage() {
        fake.post("Bob -> ");
        assertThat(fake.getUsers().size(), is(0));
    }

    @Test
    public void postAddsUser() {
        fake.post("Bob -> hello");
        assertThat(fake.getUsers().size(), is(1));
    }

    @Test
    public void postUsernameIsKey() {
        fake.post("Bob -> hello");
        assertThat(fake.getUsers().get("Bob"), notNullValue(User.class));
    }

    @Test
    public void postUserHasPost() {
        fake.post("Bob -> hello");
        assertThat(fake.getUsers().get("Bob").getPosts().size(), is(1));
    }

    @Test
    public void postHasSameUsername() {
        fake.post("Bob -> hello");
        assertThat(fake.getUsers().get("Bob").getPosts().get(0).getUsername(), is("Bob"));
    }

    @Test
    public void postHasSameMessage() {
        fake.post("Bob -> hello");
        assertThat(fake.getUsers().get("Bob").getPosts().get(0).getMessage(), is("hello"));
    }

    @Test
    public void emptyFollowCreatesNoFollower() {
        fake.post("Bob -> hello");
        fake.follow(" follows ");
        assertThat(fake.getUsers().get("Bob").getFollows().size(), is(0));
    }

    @Test
    public void noFollowerCreatesNoFollower() {
        fake.post("Bob -> hello");
        fake.follow("Bob follows ");
        assertThat(fake.getUsers().get("Bob").getFollows().size(), is(0));
    }

    @Test
    public void noOneFollowsCreatesNoFollower() {
        fake.post("Bob -> hello");
        fake.post("Charlie -> hello");
        fake.follow(" follows Charlie");
        assertThat(fake.getUsers().get("Bob").getFollows().size(), is(0));
    }

    @Test
    public void nonExistentFollowerCreatesNoFollower() {
        fake.post("Bob -> hello");
        fake.follow("Bob follows Charlie");
        assertThat(fake.getUsers().get("Bob").getFollows().size(), is(0));
    }

    @Test
    public void followCreatesFollower() {
        fake.post("Bob -> hello");
        fake.post("Charlie -> hi");
        fake.follow("Bob follows Charlie");
        assertThat(fake.getUsers().get("Bob").getFollows().size(), is(1));
    }

    @Test
    public void followCreatesCorrectFollower() {
        fake.post("Bob -> hello");
        fake.post("Charlie -> hi");
        fake.follow("Bob follows Charlie");
        assertThat(fake.getUsers().get("Bob").getFollows().get(0),
                is(fake.getUsers().get("Charlie")));
    }

    @Test
    public void unknownUserHasNoPostsForReading() {
        fake.post("Bob -> hello");
        assertThat(fake.getReadingPosts("unknown").size(), is(0));
    }

    @Test
    public void onePostForReading() {
        fake.post("Bob -> hello");
        assertThat(fake.getReadingPosts("Bob").size(), is(1));
    }

    @Test
    public void twoPostsForReading() throws InterruptedException {
        fake.post("Bob -> hello");
        // to avoid racing condition
        Thread.sleep(1000);
        fake.post("Bob -> world");
        assertThat(fake.getReadingPosts("Bob").size(), is(2));
    }

    @Test
    public void twoPostsForReadingAreInProperOrder() throws InterruptedException {
        fake.post("Bob -> hello");
        // to avoid racing condition
        Thread.sleep(1000);
        fake.post("Bob -> world");
        TreeSet<Post> posts = fake.getReadingPosts("Bob");
        assertThat(posts.pollFirst().getMessage(), is("world"));
        assertThat(posts.pollFirst().getMessage(), is("hello"));
    }

    @Test
    public void unknownUserHasNoPostsForWall() {
        fake.post("Bob -> hello");
        assertThat(fake.getWallPosts("unknown wall").size(), is(0));
    }

    @Test
    public void onePostForWall() {
        fake.post("Bob -> hello");
        assertThat(fake.getWallPosts("Bob wall").size(), is(1));
    }

    @Test
    public void twoPostsForWall() throws InterruptedException {
        fake.post("Bob -> hello");
        // to avoid racing condition
        Thread.sleep(1000);
        fake.post("Bob -> world");
        assertThat(fake.getWallPosts("Bob wall").size(), is(2));
    }

    @Test
    public void twoPostsForWallAreInProperOrder() throws InterruptedException {
        fake.post("Bob -> hello");
        // to avoid racing condition
        Thread.sleep(1000);
        fake.post("Bob -> world");
        // casting to TreeSet as implemetation is known and polling-methods make testing easier ;-)
        TreeSet<Post> posts = (TreeSet) fake.getWallPosts("Bob wall");
        assertThat(posts.pollFirst().getMessage(), is("world"));
        assertThat(posts.pollFirst().getMessage(), is("hello"));
    }

    @Test
    public void followWillAddPostsToWall() throws InterruptedException {
        fake.post("Bob -> hello");
        // to avoid racing condition
        Thread.sleep(1000);
        fake.post("Charlie -> all work");
        // to avoid racing condition
        Thread.sleep(1000);
        fake.post("Charlie -> and no fun");
        // to avoid racing condition
        Thread.sleep(1000);
        fake.post("Bob -> world");

        fake.follow("Bob follows Charlie");
        assertThat(fake.getWallPosts("Bob wall").size(), is(4));
    }

    @Test
    public void followWillAddPostsToWallInProperOrder() throws InterruptedException {
        fake.post("Bob -> hello");
        // to avoid racing condition
        Thread.sleep(1000);
        fake.post("Charlie -> all work");
        // to avoid racing condition
        Thread.sleep(1000);
        fake.post("Charlie -> and no fun");
        // to avoid racing condition
        Thread.sleep(1000);
        fake.post("Bob -> world");

        fake.follow("Bob follows Charlie");
        // casting to TreeSet as implemetation is known and polling-methods make testing easier ;-)
        TreeSet<Post> posts = (TreeSet) fake.getWallPosts("Bob wall");
        assertThat(posts.pollFirst().getMessage(), is("world"));
        assertThat(posts.pollFirst().getMessage(), is("and no fun"));
        assertThat(posts.pollFirst().getMessage(), is("all work"));
        assertThat(posts.pollFirst().getMessage(), is("hello"));
    }
}
