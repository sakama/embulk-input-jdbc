package org.embulk.input.mysql;

import com.google.common.base.Throwables;
import com.google.common.io.ByteStreams;
import org.embulk.config.ConfigSource;
import org.embulk.test.EmbulkTests;

import java.io.IOException;
import java.util.ArrayList;

import static java.util.Locale.ENGLISH;

public class MySQLTests
{
    public static ConfigSource baseConfig()
    {
        return EmbulkTests.config("EMBULK_INPUT_MYSQL_TEST_CONFIG");
    }

    public static void execute(String sql)
    {
        ConfigSource config = baseConfig();

        ArrayList<String> args = new ArrayList<>();
        args.add("mysql");

        args.add("-u");
        args.add(config.get(String.class, "user"));

        if (!config.get(String.class, "password").isEmpty()) {
            args.add("-p" + config.get(String.class, "password"));
        }

        args.add("-h");
        args.add(config.get(String.class, "host"));

        args.add("-P");
        args.add(config.get(String.class, "port", "3306"));

        args.add(config.get(String.class, "database"));
        args.add("-e");
        args.add(sql);

        ProcessBuilder pb = new ProcessBuilder(args);
        pb.redirectErrorStream(true);
        int code;
        try {
            Process process = pb.start();
            ByteStreams.copy(process.getInputStream(), System.out);
            code = process.waitFor();
        } catch (IOException | InterruptedException ex) {
            throw Throwables.propagate(ex);
        }
        if (code != 0) {
            throw new RuntimeException(String.format(ENGLISH,
                        "Command finished with non-zero exit code. Exit code is %d.", code));
        }
    }
}
