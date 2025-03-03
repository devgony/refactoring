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
        if (args.length == 0)
            throw new RuntimeException("must supply a filename");
        CommandLine commandLine = new CommandLine(args);
        return countOrders(commandLine, args);
    }

    private static long countOrders(CommandLine commandLine, String[] args) throws IOException {
        File input = Paths.get(commandLine.filename()).toFile();
        ObjectMapper mapper = new ObjectMapper();
        Order[] orders = mapper.readValue(input, Order[].class);
        if (onlyCountReady(args))
            return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
        else
            return orders.length;
    }

    private static boolean onlyCountReady(String[] args) {
        return Stream.of(args).anyMatch(arg -> "-r".equals(arg));
    }

    static public class CommandLine {
        String[] args;

        public CommandLine(String[] args) {
            this.args = args;
        }

        private String filename() {
            return this.args[args.length - 1];
        }
    }
}
