package ca.ibodrov.concord.test;

import ca.ibodrov.concord.testcontainers.Concord;
import ca.ibodrov.concord.testcontainers.ConcordProcess;
import ca.ibodrov.concord.testcontainers.Payload;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import shaded.com.walmartlabs.concord.client.ProcessEntry;

public class SimpleTest {

    private final Concord<?> concord = new Concord<>()
            .mode(Concord.Mode.DOCKER);

    @Before
    public void setUp() {
        concord.start();
    }

    @After
    public void tearDown() {
        concord.close();
    }

    @Test
    public void test() throws Exception {
        String nameValue = "name_" + System.currentTimeMillis();

        String yml = "" +
                "flows: \n" +
                "  default:\n" +
                "    - log: Hello, ${name}!";

        ConcordProcess p = concord.processes()
                .start(new Payload()
                        .concordYml(yml)
                        .parameter("arguments.name", nameValue));

        p.waitForStatus(ProcessEntry.StatusEnum.FINISHED);
        p.assertLog(".*Hello, " + nameValue + ".*");
    }
}
