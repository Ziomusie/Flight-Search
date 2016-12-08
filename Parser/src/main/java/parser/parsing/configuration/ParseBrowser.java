package parser.parsing.configuration;

/**
 * Created by tsweda on 2016-07-13.
 */
public enum ParseBrowser {
    HtmlUnitDriver,
    FirefoxPortable,    // nie wchodzi do executable jara! (nie używać na prodzie, hihi)
    FirefoxProxy,
    PhantomJS
}