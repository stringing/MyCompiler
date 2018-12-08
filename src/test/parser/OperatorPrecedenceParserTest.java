package test.parser;

import javafx.util.Pair;
import lexer.LexAnalyzer;
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
        //词法分析器提词
        File program = new File("resource/expression.txt");
        LexAnalyzer.lexProcess(program);
        //语法分析
        Grammar g = new Grammar("resource/p90_5_4.txt");
        OperatorPrecedenceParser.createFirstvts(g);
        OperatorPrecedenceParser.createLastvts(g);
        OperatorPrecedenceParser.createPrecedenceTable(g);
        for(Pair<String, Boolean> p : OperatorPrecedenceParser.parseAll(g, LexAnalyzer.getTypeList())){
            System.out.println(p.getKey() + ": " + p.getValue());
        }
    }
}
