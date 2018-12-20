package parser;

/**
 * @Description
 * @Author Stringing
 * @Date 2018/12/19 22:28
 */
public class Variable {
    private String strVal;
    private Integer numVal;

    public String getStrVal() {
        return strVal;
    }

    public Integer getNumVal() {
        return numVal;
    }

    public Variable(String strVal){
        this.strVal = strVal;
        this.numVal = null;
    }

    public Variable(Integer numVal){
        this.numVal = numVal;
        this.strVal = null;
    }

    @Override
    public String toString() {
        if(strVal == null)return String.valueOf(numVal);
        if(numVal == null)return strVal;
        return null;
    }
}
