package controller;

import view.Table;
import model.TableRecord;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.lang.Thread.currentThread;

public class SearchThread implements Runnable {

    private Integer n;
    private final ConcurrentLinkedQueue<Integer> queue;
    private final Table table;
    private final String book;

    public SearchThread(Integer n, ConcurrentLinkedQueue<Integer> queue, Table table, String book) {


        this.n = n;
        this.queue = queue;
        this.table = table;
        this.book = book;
    }

    @Override
    public void run() {
        int x = 1;
        while (x <= n && !Thread.interrupted()) {

            String word = getRandomWord(x);
            Integer y = Math.toIntExact(getSearchTime(word));

            table.updateTable(new TableRecord(x, y));
            queue.add(y);

            try {
                Thread.sleep(250);
            }
            catch (InterruptedException e) {
                break;
            }

            x += 1;
        }
        currentThread().interrupt();
    }

    private String getRandomWord(int length) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        StringBuilder word = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            word.append(c);
        }

        return word.toString();
    }

    private long getSearchTime(String word) {
        long start = System.currentTimeMillis();

        boolean key;
        for (int i = 0; i < this.book.length() - word.length(); i++) {
            key = true;
            for (int j = 0; j < word.length(); j++) {
                if (this.book.charAt(i + j) != word.charAt(j)) {
                    key = false;
                    break;
                }
            }
            if (key) {
                break;
            }
        }

        long finish = System.currentTimeMillis();

        return finish - start;
    }

}
