package parser;

import org.junit.Test;
import parser.parsing.flightproviders.googleflights.utils.Patterns;

import java.util.regex.Matcher;

/**
 * Created by tswed on 24.11.2016.
 */
public class MatcherTest {
	@Test
	public void testMatcher() {
		String timeStr = "23h 55min";
		Matcher matcher = Patterns.durationPattern.matcher(timeStr);
		assert matcher.matches();
	}
}
