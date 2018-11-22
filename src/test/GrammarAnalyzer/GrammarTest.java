package test.GrammarAnalyzer;

import GrammarAnalyzer.Grammar;
import org.junit.Test;

import java.io.IOException;

/**
 * @Description 语法结构测试
 * @Author Stringing
 * @Date 2018/11/22 11:24
 */
public class GrammarTest {

    @Test
    public void GrammarStructureTest(){
        Grammar g = new Grammar();
        g.Vn.add('S');
        g.Vn.add('A');
        g.Vt.add('a');
        g.Vt.add('b');
        g.Vt.add(null);
        g.S = 'S';
        g.addPformula('S', "αAβ");
        g.addPformula('S', "θQ");
        g.addPformula('A', "γB");
        System.out.println(g);
    }

    @Test
    public void GrammarReadTest() throws IOException {
        Grammar g = new Grammar("resource/g.txt");
        System.out.println(g);
    }
}
