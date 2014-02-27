package valtech.technical.exercise;

/**
 * Supported commands by FakeTwitter with their identifying strings.
 */
public enum Command {

    QUIT("quit"),
    POST(" -> "),
    READ(""),
    FOLLOW(" follows "),
    WALL(" wall"),
    UNKNOWN(null);

    private final String id;

    private Command(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }
}
