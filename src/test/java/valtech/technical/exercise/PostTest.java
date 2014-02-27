package valtech.technical.exercise;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import org.junit.Test;

/**
 * This will the output of the age of a posted message.
 */
public class PostTest {

    @Test
    public void zeroAge() {
        assertThat(Post.getAgeString(0), is("0 seconds ago"));
    }

    @Test
    public void oneSecondAge() {
        assertThat(Post.getAgeString(1000), is("1 second ago"));
    }

    @Test
    public void twoSecondsAge() {
        assertThat(Post.getAgeString(2000), is("2 seconds ago"));
    }

    @Test
    public void fiftyNineSecondsAge() {
        assertThat(Post.getAgeString(59999), is("59 seconds ago"));
    }

    @Test
    public void oneMinuteAge() {
        // 1 minute = 60 seconds
        assertThat(Post.getAgeString(60000), is("1 minute ago"));
    }

    @Test
    public void oneMinutePlusAge() {
        assertThat(Post.getAgeString(61000), is("1 minute ago"));
    }

    @Test
    public void twoMinutesAge() {
        // 2 minutes = 2 * 60 seconds
        assertThat(Post.getAgeString(120000), is("2 minutes ago"));
    }

    @Test
    public void fiftyNineMinutesAge() {
        assertThat(Post.getAgeString(3599999), is("59 minutes ago"));
    }

    @Test
    public void oneHourAge() {
        // 1 hour = 60 minutes
        assertThat(Post.getAgeString(3600000), is("1 hour ago"));
    }

    @Test
    public void twoHoursAge() {
        assertThat(Post.getAgeString(7200000), is("2 hours ago"));
    }

    @Test
    public void twentyThreeHoursAge() {
        assertThat(Post.getAgeString(86399999), is("23 hours ago"));
    }

    @Test
    public void oneDayAge() {
        // 1 day = 24 hours
        assertThat(Post.getAgeString(86400000), is("1 day ago"));
    }

    @Test
    public void twoDaysAge() {
        assertThat(Post.getAgeString(172800000), is("2 days ago"));
    }

    @Test
    public void thirtyDaysAge() {
        assertThat(Post.getAgeString(2678399999L), is("30 days ago"));
    }

    @Test
    public void oneMonthAge() {
        // 1 month = 31 days
        assertThat(Post.getAgeString(2678400000L), is("1 month ago"));
    }

    @Test
    public void twoMonthsAge() {
        assertThat(Post.getAgeString(5356800000L), is("2 months ago"));
    }

    @Test
    public void elevenMonthsAge() {
        assertThat(Post.getAgeString(31535999999L), is("11 months ago"));
    }

    @Test
    public void oneYearAge() {
        // 1 year = 365 days
        assertThat(Post.getAgeString(31536000000L), is("1 year ago"));
    }

    @Test
    public void twoYearsAge() {
        assertThat(Post.getAgeString(63072000000L), is("2 years ago"));
    }
}
