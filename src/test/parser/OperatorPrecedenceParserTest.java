package test.parser;

import org.junit.Test;
import parser.Grammar;
import parser.OperatorPrecedenceParser;
import parser.PredictiveParser;

import java.io.*;

/**
 * @Description
 * @Author Stringing
 * @Date 2018/12/6 16:13
 */
public class OperatorPrecedenceParserTest {
    @Test
    public void createFirstvtsTest() throws IOException {
        Grammar g = new Grammar("resource/p90_5_4.txt");
        OperatorPrecedenceParser.createFirstvts(g);
        //OperatorPrecedenceParser.printF();
        OperatorPrecedenceParser.printFirstvts();
    }

    @Test
    public void createLastvtsTest() throws IOException {
        Grammar g = new Grammar("resource/p90_5_4.txt");
        OperatorPrecedenceParser.createFirstvts(g);
        OperatorPrecedenceParser.createLastvts(g);
        //OperatorPrecedenceParser.printF();
        OperatorPrecedenceParser.printFirstvts();
        OperatorPrecedenceParser.printLastvts();
    }

    @Test
    public void createPrecedenceTableTest() throws IOException {
        Grammar g = new Grammar("resource/p90_5_4.txt");
//        System.out.println(g);
        OperatorPrecedenceParser.createFirstvts(g);
        OperatorPrecedenceParser.createLastvts(g);
        /*OperatorPrecedenceParser.printF();
        OperatorPrecedenceParser.printFirstvts();
        OperatorPrecedenceParser.printLastvts();*/
        OperatorPrecedenceParser.createPrecedenceTable(g);
        OperatorPrecedenceParser.printPrecedenceTable();
    }

    @Test
    public void parseTest() throws IOException {
        Grammar g = new Grammar("resource/p90_5_4.txt");
        OperatorPrecedenceParser.createFirstvts(g);
        OperatorPrecedenceParser.createLastvts(g);
        OperatorPrecedenceParser.createPrecedenceTable(g);
//        System.out.println(OperatorPrecedenceParser.parse(g, "i+i#"));
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("resource/expression_reset.txt"))));
        String expression;
        while((expression = br.readLine()) != null){
            expression = expression.replace(";", "");
            System.out.println(expression + " : " + OperatorPrecedenceParser.parse(g, expression + '#'));
//            System.out.println(OperatorPrecedenceParser.getS());
        }
    }
}
