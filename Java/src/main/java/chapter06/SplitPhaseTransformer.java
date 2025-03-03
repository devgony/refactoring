package chapter06;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

class SplitPhaseTransformer {
    static class Order {
        @JsonProperty("status")
        String status;
    }

    static long run(String[] args) throws IOException {
        CommandLine commandLine = new CommandLine(args);
        return countOrders(commandLine);
    }

    private static long countOrders(CommandLine commandLine) throws IOException {
        File input = Paths.get(commandLine.filename()).toFile();
        ObjectMapper mapper = new ObjectMapper();
        Order[] orders = mapper.readValue(input, Order[].class);
        if (commandLine.onlyCountReady())
            return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
        else
            return orders.length;
    }

    static public class CommandLine {
        String[] args;

        public CommandLine(String[] args) {
            if (args.length == 0)
                throw new RuntimeException("must supply a filename");
            this.args = args;
        }

        private String filename() {
            return this.args[args.length - 1];
        }

        private boolean onlyCountReady() {
            return Stream.of(args).anyMatch(arg -> "-r".equals(arg));
        }
    }
}
