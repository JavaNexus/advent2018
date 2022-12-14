package pl.javanexus.year2022.day7;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class NoSpaceLeftOnDevice {

    public static final String COMMAND_PREFIX = "$";
    public static final Pattern LS_FILE_PATTERN = Pattern.compile("(\\d+) (.+)");

    enum Command {
        CD("\\$ cd (.+)") {
            @Override
            public void execute(Browser browser, String line) {
                browser.cd(getDirName(line));
            }

            private String getDirName(String line) {
                Matcher matcher = Pattern.compile(getPattern()).matcher(line);
                if (matcher.find()) {
                    return matcher.group(1);
                } else {
                    throw new IllegalArgumentException("Unknown cd command format: " + line);
                }
            }
        },
        LS("\\$ ls") {
            @Override
            public void execute(Browser browser, String line) {
                //no-op
            }
        },
        ;

        private final String pattern;

        Command(String pattern) {
            this.pattern = pattern;
        }

        public static Command getCommand(String line) {
            for (Command command : Command.values()) {
                if (line.matches(command.pattern)) {
                    return command;
                }
            }
            throw new IllegalArgumentException("Unknown command: " + line);
        }

        public abstract void execute(Browser browser, String line);

        public String getPattern() {
            return pattern;
        }
    }

    public int sumDirectoriesSizes(Stream<String> lines, int threshold) {
        return parseCommands(lines, threshold).sizeWithinThresholdSum;
    }

    public int findSizeOfDirectoryToDelete(Stream<String> lines, int diskSize, int updateSize) {
        Browser browser = parseCommands(lines, 0);
        int freeSpace = diskSize - browser.currentDir.getSize();
        int requiredSpace = updateSize - freeSpace;

        return browser.getSmallestSubDir(browser.currentDir, requiredSpace, updateSize);
    }

    private Browser parseCommands(Stream<String> lines, int threshold) {
        final Browser browser = new Browser(threshold);

        Iterator<String> iterator = lines.iterator();
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (isCommand(line)) {
                Command.getCommand(line).execute(browser, line);
            } else {
                Matcher matcher = LS_FILE_PATTERN.matcher(line);
                if (matcher.find()) {
                    browser.addFileToCurrentDir(Integer.parseInt(matcher.group(1)));
                }
            }
        }
        browser.goBackToRoot();

        return browser;
    }

    private boolean isCommand(String line) {
        return line.startsWith(COMMAND_PREFIX);
    }



    private static class Browser {

        private Dir currentDir;
        private final Deque<Dir> path = new LinkedList<>();

        private final int threshold;
        private int sizeWithinThresholdSum;

        public Browser(int threshold) {
            this.threshold = threshold;
        }

        public void addFileToCurrentDir(int fileSize) {
            currentDir.addFile(fileSize);
        }

        public void cd(String name) {
            if ("..".equals(name)) {
                leaveCurrentDir();
            } else {
                if (currentDir != null) {
                    path.push(currentDir);
                }
                currentDir = new Dir(name);
            }
        }

        public void leaveCurrentDir() {
            Dir subDir = currentDir;
            currentDir = path.pop();
            currentDir.addSubDir(subDir);

            if (subDir.size <= threshold) {
                sizeWithinThresholdSum += subDir.size;
            }
        }

        public void goBackToRoot() {
            while (path.size() > 0) {
                leaveCurrentDir();
            }
        }

        private int getSmallestSubDir(Dir dir, int threshold, int minSize) {
            for (Dir subDir : dir.subDirs) {
                if (subDir.size >= threshold) {
                    minSize = getSmallestSubDir(subDir, threshold, Math.min(subDir.size, minSize));
                }
            }
            return minSize;
        }
    }

    private static class Dir {

        private final String name;
        private int size;

        private final List<Dir> subDirs = new ArrayList<>();

        public Dir(String name) {
            this.name = name;
        }

        private void addFile(int size) {
            this.size += size;
        }

        private void addSubDir(Dir subDir) {
            subDirs.add(subDir);
            this.size += subDir.size;
        }

        public String getName() {
            return name;
        }

        public int getSize() {
            return size;
        }
    }
}
