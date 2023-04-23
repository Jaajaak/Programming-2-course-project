package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite kirjasto-ohjelmalle
 * @author jaakko vikström
 * @version 24.5.2019
 */
@RunWith(Suite.class)
@SuiteClasses({
    kirjasto.test.ArtistiTest.class,
    kirjasto.test.ArtistitTest.class,
    kirjasto.test.BiisiTest.class,
    kirjasto.test.BiisitTest.class,
    kirjasto.test.KirjastoTest.class
    })
public class AllTests {
 //
}