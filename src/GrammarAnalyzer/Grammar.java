package GrammarAnalyzer;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description 文法数据结构
 * @Author Stringing
 * @Date 2018/11/21 11:27
 */

public class Grammar {
    //非终结符集合
    protected Set<Character> Vn;
    //终结符集合
    protected Set<Character> Vt;
    //开始符
    protected Character S;
    //产生式
    protected List<Pair<Character, List<Character[]>>> P;

    public Grammar(){
        Vn = new HashSet<>();
        Vt = new HashSet<>();
        P = new ArrayList<>();
    }

    /**
     *
     * @param nonterminal
     * @param candiniset
     */
    public void addPformula(Character nonterminal, Character[] candiniset){

    }
}
