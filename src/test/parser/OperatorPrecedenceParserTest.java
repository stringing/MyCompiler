package test.parser;

import org.junit.Test;
import parser.Grammar;
import parser.OperatorPrecedenceParser;

import java.io.IOException;

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
}
