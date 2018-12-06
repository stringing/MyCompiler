package test.lexer;

import lexer.LexAnalyzer;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * @Description
 * @Author Stringing
 * @Date 2018/11/22 12:22
 */
public class LexicalAnalyzerTest {

    @Test
    public void LexicalTest() throws IOException {
        File program = new File("resource/expression.txt");
        LexAnalyzer.lexProcess(program);
        LexAnalyzer.printTypes();
    }

    @Test
    public void ooooo() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("C:\\Users\\lenovo\\Desktop\\t.txt"));
        while(sc.hasNext()){
            System.out.println(sc.next());
        }
    }
}
