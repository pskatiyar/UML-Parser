import java.util.List;


public class Pair {
	public Pair(String who, String what) {
		super();
		this.who = who;
		this.what = what;
	}
	public Pair(String className, String templateValue, boolean b) {
		this(className, templateValue);
		many = b;
		// TODO Auto-generated constructor stub
	}
	public Pair(String className, String templateValue, int t) {
		this(className, templateValue);
		tmp = t;
		// TODO Auto-generated constructor stub
	}
	public String who;
	public String what;
	public int tmp = 0;
	boolean many = false;
	public	String toString(List<String> l) {
		if(l.contains(what))
			return who + (many?" \"0....*\"": "") + (tmp == 1? "...>" :" ---> ") + what + ": uses\n";
			//return who + " \"0...\" --> \"00\" " + what + ": uses\n";
		return "";
	}
}
