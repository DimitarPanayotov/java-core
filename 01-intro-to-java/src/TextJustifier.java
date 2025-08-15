public class TextJustifier {
    public static String[] justifyText(String[] words, int maxWidth) {
        String[] lines = new String[1000];
        int n = words.length;
        int lineCount = 0;
        int i = 0;
        while (i < n) {
            int lineLen = words[i].length();
            int j = i + 1;
            while (j < n && lineLen + 1 + words[j].length() <= maxWidth) {
                lineLen += 1 + words[j].length();
                j++;
            }

            int numWords = j - i;
            int numSpaces = maxWidth;
            for (int k = i; k < j; k++) {
                numSpaces -= words[k].length();
            }

            StringBuilder line = new StringBuilder();

            if (j == n || numWords == 1) {
                for (int k = i; k < j; k++) {
                    line.append(words[k]);
                    if (k < j - 1) {
                        line.append(" ");
                    }
                }
                while (line.length() < maxWidth) {
                    line.append(" ");
                }
            } else {
                int gaps = numWords - 1;
                int spacePerGap = numSpaces / gaps;
                int extra = numSpaces % gaps;

                for (int k = i; k < j; k++) {
                    line.append(words[k]);
                    if (k < j - 1) {
                        for (int s = 0; s < spacePerGap; s++) {
                            line.append(" ");
                        }
                        if (extra > 0) {
                            line.append(" ");
                            extra--;
                        }
                    }
                }
            }

            lines[lineCount] = line.toString();
            lineCount++;
            i = j;
        }

        String[] result = new String[lineCount];
        for (int k = 0; k < lineCount; k++) {
            result[k] = lines[k];
        }
        return result;
    }

    public static void main(String[] args) {
        String[] example1 = {"The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog."};
        String[] justified1 = TextJustifier.justifyText(example1, 11);
        for (String line : justified1) {
            System.out.println("\"" + line + "\"");
        }

        System.out.println();

        String[] example2 =
            {"Science", "is", "what", "we", "understand", "well", "enough", "to", "explain", "to", "a", "computer."};
        String[] justified2 = TextJustifier.justifyText(example2, 20);
        for (String line : justified2) {
            System.out.println("\"" + line + "\"");
        }
    }
}



