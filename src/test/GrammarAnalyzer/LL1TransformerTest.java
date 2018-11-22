package test.GrammarAnalyzer;

import GrammarAnalyzer.Grammar;
import GrammarAnalyzer.LL1Transformer;
import org.junit.Test;

import java.io.IOException;


/**
 * @Description LL(1)文法转换测试
 * @Author Stringing
 * @Date 2018/11/22 16:48
 */
public class LL1TransformerTest {

    @Test
    public void removeLeftRecursionTest() throws IOException {
        Grammar g = new Grammar("resource/g.txt");
        System.out.println(g);
        LL1Transformer.removeLeftRecursion(g);
        System.out.println("---------------------");
        System.out.println(g);
    }
}
