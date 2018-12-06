package parser;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 优先表
 * @Author Stringing
 * @Date 2018/12/6 10:33
 */
public class PrecedenceTable {
    private List<Character> column;
    private List<Character> row;
    private Character[][] table;

    public PrecedenceTable(Grammar grammar){
        column = new ArrayList<>();
        row = new ArrayList<>();
        table = new Character[grammar.Vt.size() + 1][grammar.Vt.size() + 1];
        addTerminals(grammar);
        preSet(grammar);
    }

    private void preSet(Grammar grammar) {
        for(Character vt : grammar.Vt){
            setPrecedence('#', vt, '<');
            setPrecedence(vt, '#', '>');
        }
        setPrecedence('#', '#', '=');
    }

    private void addTerminals(Grammar grammar) {
        column.addAll(grammar.Vt);
        row.addAll(grammar.Vt);
        column.add('#');
        row.add('#');
    }

    public int compare(Character c, Character r){
        Character precedence = table[column.indexOf(c)][row.indexOf(r)];
        if(precedence == '<')
            return -1;
        if(precedence == '=')
            return 0;
        if(precedence == '>')
            return 1;
        return Integer.MIN_VALUE;
    }

    public void setPrecedence(Character c, Character r, Character precedence){
        table[column.indexOf(c)][row.indexOf(r)] = precedence;
    }

    public void printTable(){
        System.out.print("  ");
        for(int i = 0; i < row.size(); i++){
            System.out.printf(" %10s", row.get(i));
        }
        System.out.println();
        for(int i = 0; i < table.length; i++){
            System.out.print(" " + column.get(i));
            for(int j = 0; j < table[i].length; j++){
                if(table[i][j] == null) {
                    System.out.printf(" %10s", " ");
                }
                else {
                    System.out.printf(" %10s", table[i][j]);
                }
            }
            System.out.println();
        }
    }
}
