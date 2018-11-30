package test.Parser;

import Parser.Grammar;
import Parser.LL1Transformer;
import Parser.PredictiveParser;
import org.junit.Test;

import java.io.*;

/**
 * @Description 预测分析程序的测试
 * @Author Stringing
 * @Date 2018/11/25 21:48
 */
public class PredictiveParserTest {

    @Test
    public void createFirstSetTest() throws IOException {
        Grammar g = new Grammar("resource/grammar_1.txt");
        LL1Transformer.transToLL1(g);
        PredictiveParser.createFirstForGrammar(g);
        System.out.println(PredictiveParser.firsts);
    }

    @Test
    public void createFollowSetTest() throws IOException {
        Grammar g = new Grammar("resource/grammar_1.txt");
        LL1Transformer.transToLL1(g);
        PredictiveParser.createFirstForGrammar(g);
        PredictiveParser.createFollowForGrammar(g);
        System.out.println(g);
        System.out.println(PredictiveParser.firsts);
        System.out.println(PredictiveParser.follows);
    }

    @Test
    public void createFirstSetTest2() throws IOException {
        Grammar g = new Grammar("resource/grammar_1.txt");
        LL1Transformer.transToLL1(g);
        PredictiveParser.createFirstForGrammar(g);
        System.out.println(PredictiveParser.firsts);
        System.out.println(PredictiveParser.createFirstSetForAlpha(")T", g));
    }

    @Test
    public void createFirstForAlphaTest(){
        Grammar g = createGrammarSample();
        PredictiveParser.createFirstForGrammar(g);
        PredictiveParser.createFollowForGrammarUtilNoIncrement(g);
        System.out.println(g);
        System.out.println(PredictiveParser.firsts);
        System.out.println(PredictiveParser.follows);
    }

    @Test
    public void createPredictiveTableTest(){
        Grammar g = createGrammarSample();
        PredictiveParser.createFirstForGrammar(g);
        PredictiveParser.createFollowForGrammarUtilNoIncrement(g);
        PredictiveParser.createPredictiveTable(g);
        PredictiveParser.pt.printTable();
    }

    @Test
    public void parseTest() throws IOException {
        Grammar g = createGrammarSample();
        PredictiveParser.createFirstForGrammar(g);
        PredictiveParser.createFollowForGrammarUtilNoIncrement(g);
        PredictiveParser.createPredictiveTable(g);
        PredictiveParser.pt.printTable();
//        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("resource/expression.txt"))));
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("resource/expression_reset.txt"))));
        String expression;
        while((expression = br.readLine()) != null){
            expression = expression.replace(";", "");
//            expression = expression.replaceAll("[0-9]", "i");
//            expression = expression.replaceAll("[a-z]", "i");
            System.out.println(expression + " : " + PredictiveParser.parse(g, expression));
        }
    }

    @Test
    public void parseTest2() throws IOException {
        //Grammar g = new Grammar("resource/grammar_1.txt");
        Grammar g = createGrammarSample2();
        //LL1Transformer.transToLL1(g);
        System.out.println(g);
        PredictiveParser.createFirstForGrammar(g);
        PredictiveParser.createFollowForGrammarUtilNoIncrement(g);
        PredictiveParser.createPredictiveTable(g);
        System.out.println(PredictiveParser.firsts);
        System.out.println(PredictiveParser.follows);
        PredictiveParser.pt.printTable();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("resource/expression_reset.txt"))));
        String expression;
        while((expression = br.readLine()) != null){
            expression = expression.replace(";", "");
            System.out.println(expression + " : " + PredictiveParser.parse(g, expression));
        }
    }


    private Grammar createGrammarSample(){
        Grammar g = new Grammar();
        g.Vt.add('+');
        g.Vt.add('*');
        g.Vt.add('(');
        g.Vt.add(')');
        g.Vt.add('i');
        g.Vn.add('E');
        g.Vn.add('A');
        g.Vn.add('T');
        g.Vn.add('B');
        g.Vn.add('F');
        g.S = 'E';
        g.addPformula('E', "TA");
        g.addPformula('A', "+TA");
        g.addPformula('A', "ε");
        g.addPformula('T', "FB");
        g.addPformula('B', "*FB");
        g.addPformula('B', "ε");
        g.addPformula('F', "(E)");
        g.addPformula('F', "i");
        return g;
    }

    private Grammar createGrammarSample2(){
        Grammar g = new Grammar();
        g.Vt.add('+');
        g.Vt.add('*');
        g.Vt.add('(');
        g.Vt.add(')');
        g.Vt.add('i');
        g.Vn.add('E');
        g.Vn.add('A');
        g.Vn.add('T');
        g.Vn.add('B');
        g.Vn.add('F');
        g.S = 'E';
        g.addPformula('E', "TA");
        g.addPformula('A', "+TA");
        g.addPformula('A', "ε");
        g.addPformula('T', "FB");
        g.addPformula('B', "*FB");
        g.addPformula('B', "ε");
        g.addPformula('F', "(E+T*F)");
        g.addPformula('F', "(E+F)");
        g.addPformula('F', "(T*F)");
        g.addPformula('F', "(F)");
        g.addPformula('F', "i");
        return g;
    }

}
