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

    @Test
    public void removeLeftGeneTest() throws IOException {
        Grammar g = new Grammar("resource/g2.txt");
        System.out.println(g);
        LL1Transformer.removeLeftRecursion(g);
        System.out.println("---------------------");
        System.out.println(g);
        LL1Transformer.removeLeftGene(g);
        System.out.println("---------------------");
        System.out.println(g);
    }

    @Test
    public void transToLL1Test() throws IOException {
        long t1 = System.currentTimeMillis();
        Grammar g = new Grammar("resource/g2.txt");
        System.out.println(g);
        LL1Transformer.transToLL1(g);
        System.out.println("---------------------");
        System.out.println(g);
        long t2 = System.currentTimeMillis();
        System.out.println("------------- 总运行时长：" + (t2 - t1) + "ms -------------");
    }

    @Test
    public void transToLL1Test2() throws IOException {
        Grammar g = new Grammar("resource/grammar_1.txt");
        System.out.println(g);
        LL1Transformer.transToLL1(g);
        System.out.println("---------------------");
        System.out.println(g);
    }
}
