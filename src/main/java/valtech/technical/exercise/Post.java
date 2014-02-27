package valtech.technical.exercise;

import java.util.Objects;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

/**
 * A posted message with its timestamp and the name of the user who has created
 * it.
 *
 * ATTENTION: The {@link java.lang.Comparable} implementation will compare posts
 * only by their timestamp in a reverse logic to provide a reverse sorting of
 * posts by their timestamp (newest first). However, if a true comparison of
 * post shall be needed at a later time, this implemetation must be put into an
 * own {@link java.util.Comparator} implementation.
 */
public class Post implements Comparable<Post> {

    private final String message;
    private final String username;
    private final long timestamp;

    public Post(String message, String username) {
        this.message = message;
        this.username = username;
        this.timestamp = System.currentTimeMillis();
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public String getAge() {
        return getAgeString(System.currentTimeMillis() - timestamp);
    }

    public static String getAgeString(long timeDiff) {
        int number = 0;
        String unit = "";
        try {
            Duration dur = DatatypeFactory.newInstance().newDuration(timeDiff);
            if (dur.getYears() > 0) {
                number = dur.getYears();
                unit = "year";
            } else if (dur.getMonths() > 0) {
                number = dur.getMonths();
                unit = "month";
            } else if (dur.getDays() > 0) {
                number = dur.getDays();
                unit = "day";
            } else if (dur.getHours() > 0) {
                number = dur.getHours();
                unit = "hour";
            } else if (dur.getMinutes() > 0) {
                number = dur.getMinutes();
                unit = "minute";
            } else {
                number = dur.getSeconds();
                unit = "second";
            }
            if (number > 1 || number == 0) {
                unit += "s";
            }
        } catch (DatatypeConfigurationException ex) {
            ex.printStackTrace();
        }
        return String.format("%d %s ago", number, unit);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.message);
        hash = 61 * hash + (int) (this.timestamp ^ (this.timestamp >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Post other = (Post) obj;
        if (!Objects.equals(this.message, other.message)) {
            return false;
        }
        if (this.timestamp != other.timestamp) {
            return false;
        }
        return true;
    }

    /**
     * ATTENTION: The {@link java.lang.Comparable} implementation will compare
     * posts only by their timestamp in a reverse logic to provide a reverse
     * sorting of posts by their timestamp (newest first). However, if a true
     * comparison of post shall be needed at a later time, this implemetation
     * must be put into an own {@link java.util.Comparator} implementation.
     */
    @Override
    public int compareTo(Post o) {
        int result = 0;
        if (timestamp < o.timestamp) {
            result = 1;
        } else if (timestamp > o.timestamp) {
            result = -1;
        }
        return result;
    }
}
