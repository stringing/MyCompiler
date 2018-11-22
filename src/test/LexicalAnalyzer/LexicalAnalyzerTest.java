package test.LexicalAnalyzer;

import LexicalAnalyzer.LexAnalyzer;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

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
}
