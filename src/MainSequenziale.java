import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

public class MainSequenziale {

    static String d = "C:\\Users\\gugli\\Downloads\\TestFolder";
    static int ni = 6;
    static int n = 10;
    static int maxl = 100;
    static ArrayList<String> list = new ArrayList<>();
    static int[] nItem = new int[ni];
    static final String cod = "0000000000";
    static final int max = 10;

    private static void recursive(File file){
        if(file.getName().endsWith(".java")) {

            try {
                int lines = Math.toIntExact(Files.lines(Path.of(file.getPath())).count());
                String input = cod.substring(0, max - String.valueOf(lines).length()) + lines + String.valueOf(lines).length() + file;
                if(list.size() == n - 1){
                    list.add(input);
                    Collections.sort(list);
                }
                else if(list.size() < n) {
                    list.add(input);
                }
                else if(input.compareTo(list.get(0)) >= 0) {
                    list.remove(0);
                    list.add(input);
                    Collections.sort(list);
                }
                if (lines < maxl)
                    nItem[lines / (maxl / (ni - 1))]++;
                else
                    nItem[ni - 1]++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if(file.listFiles() != null)
            for (File f : file.listFiles()) recursive(f);
    }

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        recursive(new File(d));
        //Controllare che la lista non sia vuota
        for(int i = list.size() - 1; i >= list.size() - n; i--)
            System.out.println(list.size() - i + ")" +
                    " " + list.get(i).subSequence(max + 1, list.get(i).length())
                    + " - " + list.get(i).subSequence(
                    58 - (int) list.get(i).charAt(max), max)
            );
        System.out.println(list.size());
        for(int i = 0; i < nItem.length; i++)
            if(i != (ni - 1))
                System.out.println("range: " + (maxl / (ni - 1) * i) + "-" + (maxl / (ni - 1) * (i + 1) - 1) + " = " + nItem[i]);
            else
                System.out.println("range: " + maxl + "-... = " + nItem[i]);
        System.out.println(System.currentTimeMillis() - time);
    }
}