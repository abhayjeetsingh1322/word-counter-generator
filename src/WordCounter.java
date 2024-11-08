import java.util.Comparator;

import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * This program takes a input of a file that has text, and generates a HTML page
 * that shows the words it contains and their count.
 *
 * @author A. Singh
 */
public final class WordCounter {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private WordCounter() {
        // no code needed here
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {

        //Creating output and input streams
        SimpleWriter out = new SimpleWriter1L();
        SimpleReader in = new SimpleReader1L();

        //Asking for input file
        out.print("Please enter the name of the file "
                + "(include folder name, file name, and extension): ");
        String fileName = in.nextLine();

        //Asking for output file
        out.print("Please enter the name of the output file "
                + "(include folder name, file name, and extension): ");
        String outputFileName = in.nextLine();

        //Creating set for separators and using method to extract
        final String separatorsStr = " \t\n,.;:-";
        Set<Character> separatorSet = new Set1L<>();
        generateElements(separatorsStr, separatorSet);

        //Creating map for words and count, and queue for correct order
        //Calling method to update them based on the input file
        Queue<String> uniqueWords = new Queue1L<>();
        Map<String, Integer> wordsAndCount = new Map1L<>();
        updateMapAndQueue(wordsAndCount, uniqueWords, separatorSet, fileName);

        //Creating a comparator and sorting the queue (@updates queue)
        Comparator<String> stringComp = new StringLT();
        uniqueWords.sort(stringComp);

        //Calling method to create HTML page
        createPage(uniqueWords, wordsAndCount, fileName, outputFileName);

        //Telling user the program is done
        out.print("Valid HTML pages have been generated and the program "
                + "has termainted.");

        //Closing input and output streams
        out.close();
        in.close();
    }

    /**
     * Generates the set of characters in the given {@code String} into the
     * given {@code Set}.
     *
     * @param str
     *            the given {@code String}
     * @param charSet
     *            the {@code Set} to be replaced
     * @replaces charSet
     * @ensures charSet = entries(str)
     */
    private static void generateElements(String str, Set<Character> charSet) {
        assert str != null : "Violation of: str is not null";
        assert charSet != null : "Violation of: charSet is not null";

        //Staring position
        int position = 0;

        //Entering while loop until all separators are covered
        while (position < str.length()) {

            //Getting character
            char x = str.charAt(position);

            //If separatorSet doesn't contain the character adding it
            if (!charSet.contains(x)) {
                charSet.add(x);
            }

            position++;
        }
    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    private static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {

        //Declaring a variable for final index
        int finalPosition = position;

        //Checking if the current index is a separator
        if (separators.contains(text.charAt(position))) {

            //Entering while loop until separators are extracted or length is
            //crossed, and incrementing final index
            while (finalPosition < text.length()
                    && separators.contains(text.charAt(finalPosition))) {
                finalPosition++;
            }

            //Else if the current index is a letter
        } else {

            //Entering while loop until letters are extracted or length is
            //crossed, and incrementing final index
            while (finalPosition < text.length()
                    && !separators.contains(text.charAt(finalPosition))) {
                finalPosition++;
            }
        }

        //Getting a substring from initial index to final index
        String nextStr = text.substring(position, finalPosition);

        return nextStr;
    }

    /**
     * Method updates map that contains words and their count and adds words in
     * a queue.
     *
     * @param wordsAndCount
     *            Map that contains words (key) and their count (value)
     * @param uniqueWords
     *            Queue that contains the words in order
     * @param charSet
     *            Set that contains possible separators in text
     * @param fileName
     *            String that contains the name of the input file
     * @updates wordsAndCount, uniqueWords
     * @requires charSet all possible separators
     * @ensures <pre>
     * uniqueWords contains  entries of words from text from fileName &
     * wordsAndCount contains entries as keys and associated counts as values.
     *  </pre>
     */
    private static void updateMapAndQueue(Map<String, Integer> wordsAndCount,
            Queue<String> uniqueWords, Set<Character> charSet,
            String fileName) {

        //Creating a input stream from the input file
        SimpleReader in = new SimpleReader1L(fileName);

        //Entering loop until at the end of file
        while (!in.atEOS()) {

            //Getting line from input file
            String text = in.nextLine() + "\n";

            //Declaring a index variable for the line
            int position = 0;

            //Entering loop until index variable equals text length
            while (position < text.length()) {

                //Calling method to get word or separators
                String wordOrSeparator = nextWordOrSeparator(text, position,
                        charSet);

                //Updating index variable
                position += wordOrSeparator.length();

                //Checking if the string extracted is a word by checking first
                //character
                if (!charSet.contains(wordOrSeparator.charAt(0))) {

                    //If Map has the word updating count (value)
                    if (wordsAndCount.hasKey(wordOrSeparator)) {
                        int count = wordsAndCount.value(wordOrSeparator);
                        count++;
                        wordsAndCount.replaceValue(wordOrSeparator, count);

                        //Else adding word to Map with word (key) and
                        //count (value) and adding word to Queue
                    } else {
                        wordsAndCount.add(wordOrSeparator, 1);
                        uniqueWords.enqueue(wordOrSeparator);
                    }
                }
            }
        }

        //Closing input stream
        in.close();
    }

    /**
     * Method generates valid HTML format page for the words and their counts.
     *
     * @param uniqueWords
     *            Queue that contains the words in order
     * @param wordsAndCount
     *            Map that contains words (key) and their count (value)
     * @param fileName
     *            String that contains the name of the input file
     * @param outputFileName
     *            String that contains the name of the output file
     * @ensures <pre>
     * Valid HTML page will be generated and will be saved to location provided
     * by outputFileName.
     * </pre>
     */
    private static void createPage(Queue<String> uniqueWords,
            Map<String, Integer> wordsAndCount, String fileName,
            String outputFileName) {

        //Creating output stream to the output file
        SimpleWriter out = new SimpleWriter1L(outputFileName);

        //Declaring a temporary queue
        Queue<String> temp = uniqueWords.newInstance();

        //Printing HTML lines
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Words Counted in " + fileName + "</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2>Words Counted in " + fileName + "</h2>");
        out.println("<hr />");
        out.println("<table border=\"1\">");
        out.println("<tr>");
        out.println("<th>Words</th>");
        out.println("<th>Counts</th>");
        out.println("</tr>");

        //Entering loop until the queue is empty
        while (uniqueWords.length() > 0) {

            //Taking out the word from queue
            String word = uniqueWords.dequeue();

            //Adding word to temporary queue
            temp.enqueue(word);

            //Getting count (value) from map
            int count = wordsAndCount.value(word);

            //Printing HTML lines
            out.println("<tr>");
            out.println("<td>" + word + "</td>");
            out.println("<td>" + count + "</td>");
            out.println("</tr>");
        }

        //Printing HTML lines
        out.println("</tables>");
        out.println("</body>");
        out.println("</html>");

        //Restoring queue and closing output stream
        uniqueWords.transferFrom(temp);
        out.close();
    }

    /**
     * Returns zero if strings are equal. Negative if str1 comes first not str2,
     * which is correct order. Positive integer if str2 comes first not str1,
     * which is not the correct order. Used to sort strings alphabetically.
     *
     * @author A. Singh
     *
     */
    private static class StringLT implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {
            return (str1.compareToIgnoreCase(str2));
        }
    }
}
