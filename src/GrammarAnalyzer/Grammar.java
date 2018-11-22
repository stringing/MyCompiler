package GrammarAnalyzer;


import java.io.*;
import java.util.*;

/**
 * @Description 文法数据结构
 * @Author Stringing
 * @Date 2018/11/21 11:27
 */

public class Grammar {

    //产生式，用Hash表表示推导关系
    protected static class Pformula{
        protected Map<Character, List<String>> pformula;

        protected Pformula(){
            pformula = new HashMap<>();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for(Map.Entry<Character, List<String>> entry : pformula.entrySet()){
                sb.append(entry.getKey() + "->");
                for(int i = 0; i < entry.getValue().size(); i++) {
                    String tmp = entry.getValue().get(i);
                    if (i != entry.getValue().size() - 1) {
                        sb.append(tmp + "|");
                    }else{
                        sb.append(tmp + "\n");
                    }
                }
            }
            return sb.toString();
        }
    }

    //非终结符集合
    public Set<Character> Vn;
    //终结符集合
    public Set<Character> Vt;
    //开始符
    public Character S;
    //产生式
    public Pformula P;

    public Grammar(){
        Vn = new HashSet<>();
        Vt = new HashSet<>();
        P = new Pformula();
    }

    public Grammar(String grammarPath) throws IOException {
        this();
        readOriginalGrammar(new File(grammarPath));
    }

    /**
     *  添加产生式 A -> α
     * @param nonterminal 非终结符
     * @param candiniset 候选首符集
     */
    public void addPformula(Character nonterminal, String candiniset){
        List<String> candinisets;
        if(P.pformula.containsKey(nonterminal)){
            candinisets = P.pformula.get(nonterminal);
            candinisets.add(candiniset);
        }else{
            candinisets = new ArrayList<>();
            candinisets.add(candiniset);
        }
        P.pformula.put(nonterminal, candinisets);
    }

    /**
     * 读入原始文法并整理
     * @param file 原始文法文件
     * @throws IOException
     */
    public void readOriginalGrammar(File file) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String formula;
        while((formula = br.readLine()) != null){
            Character nonterminal = formula.charAt(0);
            char[] candiniset = formula.substring(3, formula.length() - 1).toCharArray();
            if(S == null)S = nonterminal;
            Vn.add(nonterminal);
            for(char c : candiniset){
                if(Character.isAlphabetic(c) && Character.isUpperCase(c)){
                    Vn.add(c);
                }else{
                    Vt.add(c);
                }
            }
            addPformula(nonterminal, charsToString(candiniset));
        }
    }

    private String charsToString(char[] candiniset) {
        StringBuilder sb = new StringBuilder();
        for(char c : candiniset)sb.append(c);
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Vn: \n" + Vn.toString() + "\n"
                + "Vt: \n" + Vt.toString().replace("null", "ε") + "\n"
                + "S: \n" + S + "\n"
                + "P: \n" + P.toString();
    }
}