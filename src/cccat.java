import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class cccat {
    private static final String OPTION_NUMBER_LINES = "-n";
    private static final String OPTION_NUMBER_NONBLANK = "-b";

    public static void main(String[] args) throws IOException {
        String lineNumberSetting = args.length > 0 ? getLineNumberSetting(args[0]) : "";
        List<String> inputs = parseInputs(args, lineNumberSetting);

        if (inputs.isEmpty()) {
            inputs.add("-"); // default to standard input
        }

        processInputs(inputs, lineNumberSetting);
    }

    private static void processInputs(List<String> inputs, String lineNumberSetting) throws IOException {
        for (String input : inputs) {
            Path path = input.equals("-") ? null : Path.of(input);
            printFile(path, lineNumberSetting);
        }
    }

    private static void printFile(Path filePath, String lineNumberSetting) throws IOException {
        int lineNumber = 1;
        try (BufferedReader reader = filePath == null ? new BufferedReader(new InputStreamReader(System.in))
                : Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber = printLine(line, lineNumber, lineNumberSetting);
            }
        }
    }

    private static int printLine(String line, int lineNumber, String lineNumberSetting) {
        boolean shouldPrintLineNum = !lineNumberSetting.isEmpty() && (lineNumberSetting.equals(OPTION_NUMBER_LINES)
                || (lineNumberSetting.equals(OPTION_NUMBER_NONBLANK) && !line.trim().isEmpty()));

        if (shouldPrintLineNum) {
            System.out.printf("%d %s%n", lineNumber++, line);
        } else {
            System.out.println(line);
        }
        return lineNumber;
    }

    private static List<String> parseInputs(String[] args, String lineNumberSetting) {
        List<String> inputs = new ArrayList<>();
        int startIndex = lineNumberSetting.isEmpty() ? 0 : 1;
        for (int i = startIndex; i < args.length; i++) {
            inputs.add(args[i]);
        }
        return inputs;
    }

    private static String getLineNumberSetting(String arg) {
        if (arg.equals(OPTION_NUMBER_LINES) || arg.equals(OPTION_NUMBER_NONBLANK)) {
            return arg;
        }
        return "";
    }

    private static byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int length;
        while ((length = inputStream.read(data)) != -1) {
            buffer.write(data, 0, length);
        }
        return buffer.toByteArray();
    }

}