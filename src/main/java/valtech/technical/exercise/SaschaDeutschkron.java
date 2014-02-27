package valtech.technical.exercise;

/**
 * Application wrapper.
 */
public class SaschaDeutschkron {

    public static void main(String[] args) {
        FakeTwitter fTwitter = new FakeTwitter();
        fTwitter.run();
    }
}
