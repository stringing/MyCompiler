package test.parser;

import lexer.LexAnalyzer;
import org.junit.Test;
import parser.ReversePolishTransformer;
import parser.Variable;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Description
 * @Author Stringing
 * @Date 2018/12/20 13:51
 */
public class ReversePolishTransformerTest {
    @Test
    public void transToReversePolishTest() throws IOException {
        //词法分析器提词
        File program = new File("resource/experiment4.txt");
        LexAnalyzer.lexProcess(program);
        List<List<Variable>> list = ReversePolishTransformer.transToReversePolish(LexAnalyzer.getTypeList());
        for(List<Variable> variables : list){
            for(int i = variables.size() - 1; i >= 0; i--){
                if(i != 0){
                    System.out.print(variables.get(i) + ", ");
                }else{
                    System.out.print(variables.get(i));
                }
            }
            System.out.println();
        }
    }
}
