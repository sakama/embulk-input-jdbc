package org.embulk.input.mysql;

import org.embulk.config.ConfigDiff;
import org.embulk.config.ConfigSource;
import org.embulk.input.MySQLInputPlugin;
import org.embulk.spi.InputPlugin;
import org.embulk.test.TestingEmbulk;
import org.embulk.test.TestingEmbulk.RunResult;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.nio.file.Path;

import static org.embulk.input.mysql.MySQLTests.execute;
import static org.embulk.test.EmbulkTests.readResource;
import static org.embulk.test.EmbulkTests.readSortedFile;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class IncrementalTest
{
    @Rule
    public TestingEmbulk embulk = TestingEmbulk.builder()
        .registerPlugin(InputPlugin.class, "mysql", MySQLInputPlugin.class)
        .build();

    private ConfigSource baseConfig;

    @Before
    public void setup()
    {
        baseConfig = MySQLTests.baseConfig();
    }

    @Test
    public void simpleInt() throws Exception
    {
        // setup first rows
        execute(readResource("expect/incremental/int/setup.sql"));

        Path out1 = embulk.createTempFile("csv");
        RunResult result1 = embulk.runInput(
                baseConfig.merge(embulk.loadYamlResource("expect/incremental/int/config_1.yml")),
                out1);
        assertThat(
                readSortedFile(out1),
                is(readResource("expect/incremental/int/expected_1.csv")));
        assertThat(
                result1.getConfigDiff(),
                is((ConfigDiff) embulk.loadYamlResource("expect/incremental/int/expected_1.diff")));

        // insert more rows
        execute(readResource("expect/incremental/int/insert_more.sql"));

        Path out2 = embulk.createTempFile("csv");
        RunResult result2 = embulk.runInput(
                baseConfig.merge(embulk.loadYamlResource("expect/incremental/int/config_2.yml")),
                out2);
        assertThat(
                readSortedFile(out2),
                is(readResource("expect/incremental/int/expected_2.csv")));
        assertThat(
                result2.getConfigDiff(),
                is((ConfigDiff) embulk.loadYamlResource("expect/incremental/int/expected_2.diff")));
    }

    @Test
    public void simpleDatetime() throws Exception
    {
        // setup first rows
        execute(readResource("expect/incremental/datetime/setup.sql"));

        Path out1 = embulk.createTempFile("csv");
        RunResult result1 = embulk.runInput(
                baseConfig.merge(embulk.loadYamlResource("expect/incremental/datetime/config_1.yml")),
                out1);
        assertThat(
                readSortedFile(out1),
                is(readResource("expect/incremental/datetime/expected_1.csv")));
        assertThat(
                result1.getConfigDiff(),
                is((ConfigDiff) embulk.loadYamlResource("expect/incremental/datetime/expected_1.diff")));

        // insert more rows
        execute(readResource("expect/incremental/datetime/insert_more.sql"));

        Path out2 = embulk.createTempFile("csv");
        RunResult result2 = embulk.runInput(
                baseConfig.merge(embulk.loadYamlResource("expect/incremental/datetime/config_2.yml")),
                out2);
        assertThat(
                readSortedFile(out2),
                is(readResource("expect/incremental/datetime/expected_2.csv")));
        assertThat(
                result2.getConfigDiff(),
                is((ConfigDiff) embulk.loadYamlResource("expect/incremental/datetime/expected_2.diff")));
    }

    @Test
    public void simpleTimestamp() throws Exception
    {
        // setup first rows
        execute(readResource("expect/incremental/timestamp/setup.sql"));

        Path out1 = embulk.createTempFile("csv");
        RunResult result1 = embulk.runInput(
                baseConfig.merge(embulk.loadYamlResource("expect/incremental/timestamp/config_1.yml")),
                out1);
        assertThat(
                readSortedFile(out1),
                is(readResource("expect/incremental/timestamp/expected_1.csv")));
        assertThat(
                result1.getConfigDiff(),
                is((ConfigDiff) embulk.loadYamlResource("expect/incremental/timestamp/expected_1.diff")));

        // insert more rows
        execute(readResource("expect/incremental/timestamp/insert_more.sql"));

        Path out2 = embulk.createTempFile("csv");
        RunResult result2 = embulk.runInput(
                baseConfig.merge(embulk.loadYamlResource("expect/incremental/timestamp/config_2.yml")),
                out2);
        assertThat(
                readSortedFile(out2),
                is(readResource("expect/incremental/timestamp/expected_2.csv")));
        assertThat(
                result2.getConfigDiff(),
                is((ConfigDiff) embulk.loadYamlResource("expect/incremental/timestamp/expected_2.diff")));
    }
}
