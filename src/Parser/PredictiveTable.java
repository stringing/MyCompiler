package Parser;


import javafx.util.Pair;

import java.util.*;

/**
 * @Description 预测分析表
 * @Author Stringing
 * @Date 2018/11/29 13:18
 */
public class PredictiveTable {
    private List<Character> nonterminals;
    private List<Character> terminals;
    private Pair<Character, String>[][] table;

    public PredictiveTable(Grammar grammar){
        nonterminals = new ArrayList<>();
        terminals = new ArrayList<>();
        table = new Pair[grammar.Vn.size()][grammar.Vt.size() + 1];
        addNonterminals(grammar.Vn);
        addTerminals(grammar.Vt);
    }

    private void addNonterminals(Set<Character> vn){
        for(Character c : vn){
            nonterminals.add(c);
        }
    }

    private void addTerminals(Set<Character> vt){
        for(Character c : vt){
            terminals.add(c);
        }
        terminals.add('#');
    }

    public void addPformula(Character nonterminal, Character terminal, Character left, String right){
        table[nonterminals.indexOf(nonterminal)][terminals.indexOf(terminal)] = new Pair<>(left, right);
    }

    public String getAlpha(Character nonterminal, Character terminal){
        Pair<Character, String> p = table[nonterminals.indexOf(nonterminal)][terminals.indexOf(terminal)];
        return p == null ? null : p.getValue();
    }

    public void printTable(){
        System.out.print("  ");
        for(int i = 0; i < terminals.size(); i++){
            System.out.printf(" %10s", terminals.get(i));
        }
        System.out.println();
        for(int i = 0; i < table.length; i++){
            System.out.print(" " + nonterminals.get(i));
            for(int j = 0; j < table[i].length; j++){
                if(table[i][j] == null) {
                    System.out.printf(" %10s", " ");
                }
                else {
                    System.out.printf(" %10s", table[i][j].getKey() + "->" + table[i][j].getValue());
                }
            }
            System.out.println();
        }
    }

}
