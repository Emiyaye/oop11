package it.unibo.oop.lab.streams;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Modify this small program adding new filters.
 * Realize this exercise using as much as possible the Stream library.
 *
 * 1) Convert to lowercase
 *
 * 2) Count the number of chars
 *
 * 3) Count the number of lines
 *
 * 4) List all the words in alphabetical order
 * 
 * 5) Write the count for each word, e.g. "word word pippo" should output "pippo
 * -> 1 word -> 2"
 *
 */
public final class LambdaFilter extends JFrame {

    private static final long serialVersionUID = 1760990730218643730L;

    private enum Command {
        /**
         * Commands.
         */
        IDENTITY("No modifications", Function.identity()),
        TO_LOWERCASE("Convert to lowercase", String::toLowerCase),
        COUNT_CHAR("Count the number of chars", e -> Integer.toString(e.length())),
        COUNT_LINES("Count the number of lines", e -> Long.toString(e.chars().filter(c -> c == '\n').count())),
        ALPHABETICAL_ORDER("All the words in alphabetical order", Command::alphaOrder),
        COUNT_WORD("the count for each word", Command::countWord);

        private static String alphaOrder(final String string) {
            StringTokenizer st = new StringTokenizer(string);
            Set<String> set = new HashSet<>();
            while (st.hasMoreTokens()) {
                set.add(st.nextToken());
            }
            return set.stream()
                    .sorted((a, b) -> a.compareTo(b))
                    .reduce((s1, s2) -> s1 + "\n" + s2)
                    .get();
        }

        private static String countWord(final String string) {
            StringTokenizer st = new StringTokenizer(string);
            Map<String, Integer> map = new HashMap<>();
            while (st.hasMoreTokens()) {
                String temp = st.nextToken();
                map.put(temp, map.containsKey(temp) ? map.get(temp) + 1 : 1);
            }
            return map.entrySet().stream()
                    .map(e -> e.getKey() + " -> " + e.getValue() + "\n")
                    .reduce((a, b) -> a + b)
                    .get();
        }

        private final String commandName;
        private final Function<String, String> fun;

        Command(final String name, final Function<String, String> process) {
            commandName = name;
            fun = process;
        }

        @Override
        public String toString() {
            return commandName;
        }

        public String translate(final String s) {
            return fun.apply(s);
        }
    }

    private LambdaFilter() {
        super("Lambda filter GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JPanel panel1 = new JPanel();
        final LayoutManager layout = new BorderLayout();
        panel1.setLayout(layout);
        final JComboBox<Command> combo = new JComboBox<>(Command.values());
        panel1.add(combo, BorderLayout.NORTH);
        final JPanel centralPanel = new JPanel(new GridLayout(1, 2));
        final JTextArea left = new JTextArea();
        left.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        final JTextArea right = new JTextArea();
        right.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        right.setEditable(false);
        centralPanel.add(left);
        centralPanel.add(right);
        panel1.add(centralPanel, BorderLayout.CENTER);
        final JButton apply = new JButton("Apply");
        apply.addActionListener(ev -> right.setText(((Command) combo.getSelectedItem()).translate(left.getText())));
        panel1.add(apply, BorderLayout.SOUTH);
        setContentPane(panel1);
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        final int sw = (int) screen.getWidth();
        final int sh = (int) screen.getHeight();
        setSize(sw / 4, sh / 4);
        setLocationByPlatform(true);
    }

    /**
     * @param a unused
     */
    public static void main(final String... a) {
        final LambdaFilter gui = new LambdaFilter();
        gui.setVisible(true);
    }
}
