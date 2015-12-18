import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.SourceStringReader;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class main {
		static String UML = "";
		static String folderP = "/Users/PKatiyar/Desktop/CMPE-202/Mid Term/uml-parser-test-3";
		static String out = "uml.png";
	    public static void main(String[] args) throws Exception {
	        // creates an input stream for the file to be parsed
	    	if(args.length == 2) {
	    			folderP =args[0];
	    			out = args[1];
	    	} else {
	    		System.out.println("Given wrong number of arguments. Correct format: <input folder> <output png file>");
	    	}
	    	File folder = new File(folderP);
	    	for(File fileEntry: folder.listFiles()) 
	    	if(!fileEntry.isDirectory() && isJava(fileEntry)){
		        FileInputStream in = new FileInputStream(fileEntry);
		        CompilationUnit cu;
		        try {
		            cu = JavaParser.parse(in);
		        } finally {
		            in.close();
		        }
		        MethodVisitor mv = new MethodVisitor();
		        mv.visit(cu, null);
		        mv.printUml();
	    	}
	    	for(Pair s: usingTypes)
	    		UML += s.toString(myClasses);
	    	OutputStream png = new FileOutputStream(out);
	    	//System.out.println(UML);
	    	SourceStringReader reader = new SourceStringReader("@startuml\n" + UML + "@enduml\n");
	    	String desc = reader.generateImage(png);
	    }
		static Set<Pair> usingTypes = new HashSet<Pair>();
		static List<String> myClasses = new ArrayList<String>();

	    private static class MethodVisitor extends VoidVisitorAdapter<Object> {
			@Override
			public void visit(ConstructorDeclaration arg0, Object arg1) {
				// TODO Auto-generated method stub
				if((arg0.getModifiers() & Modifier.PUBLIC) > 0)
					fields.add("+" + arg0.getDeclarationAsString(false, false));
				super.visit(arg0, arg1);
			}
			List<String> methods = new ArrayList<String>();
    		List<String> fields = new ArrayList<String>();
			private boolean isAbstract;
			@Override
			public void visit(FieldDeclaration arg0, Object arg1) {
				// TODO Auto-generated method stub
				char type = ' ';
				if((arg0.getModifiers() & Modifier.PUBLIC) == Modifier.PUBLIC) {
					type = '+';
					for(Iterator<?>  it = arg0.getVariables().iterator(); it.hasNext(); ) {
						if(getTemplateValue(arg0.getType().toString()) != "")
							usingTypes.add(new Pair(className, getTemplateValue(arg0.getType().toString()), true));
						else {
							fields.add(type + " " + it.next() + ":" + arg0.getType());
							usingTypes.add(new Pair(className, arg0.getType().toString()));
						}
					}
				}
				else if((arg0.getModifiers() & Modifier.PRIVATE) == Modifier.PRIVATE) {
					for(Iterator<?>  it = arg0.getVariables().iterator(); it.hasNext(); ) {
						VariableDeclarator v = (VariableDeclarator) it.next();
						if(getTemplateValue(arg0.getType().toString()) != "")
							usingTypes.add(new Pair(className, getTemplateValue(arg0.getType().toString()), true));
						else {
							nonPublic.add(new Variable(v.toString(), arg0.getType().toString(), Modifier.PRIVATE));
							usingTypes.add(new Pair(className, arg0.getType().toString()));
						}
					}
				}
				if(type != ' ')
				super.visit(arg0, arg1);
			}

			private String getTemplateValue(String string) {
				int i = string.indexOf('<');
				int i2 = string.indexOf('>');
				if(i >= 0 && i < i2)  {
					return string.substring(i+1, i2);
				}
				return "";
			}
			@Override
			public void visit(MethodDeclaration arg0, Object arg1) {
				// TODO Auto-generated method stub
				if((arg0.getModifiers() & Modifier.PUBLIC) == Modifier.PUBLIC) {
						if(arg0.getName().substring(0, 3) == "get" || arg0.getName().substring(0, 3) == "set")
							publicAcces.add(new Pair(arg0.getName() + "( "+planeL(arg0.getParameters()) +") :" + arg0.getType().toString(), Character.toLowerCase(arg0.getName().substring(3).charAt(0)) + (arg0.getName().substring(3).length() > 1 ? arg0.getName().substring(3).substring(1) : "")));
						else
							publicAcces.add(new Pair(arg0.getName() + "( "+planeL(arg0.getParameters()) +") :" + arg0.getType().toString(), Character.toLowerCase(arg0.getName().substring(3).charAt(0)) + (arg0.getName().substring(3).length() > 1 ? arg0.getName().substring(3).substring(1) : "")));
						uses(arg0.getParameters());
				}
				super.visit(arg0, arg1);
			}

	        public void visit(ClassOrInterfaceDeclaration arg0, Object arg1) {
				// TODO Auto-generated method stub
	        	className = arg0.getName();
	        	isInterface = arg0.toString().indexOf("interface")!=-1;
	        	isAbstract = arg0.toString().indexOf("abstract")!=-1;
	        	myClasses.add(className);
	        	for(Iterator<?> it = arg0.getImplements().iterator(); it.hasNext();)
	        		inheritance += className + " ..|> " + it.next() + "\n";
	        	for(Iterator<?> it = arg0.getExtends().iterator(); it.hasNext();)
	        		inheritance += className + " ---|> " +it.next() + "\n";
				super.visit(arg0, arg1);
			}
	    	public void printUml() {
	    		for(Iterator<Variable> it = nonPublic.iterator(); it.hasNext(); ) {
	    			Variable v = (Variable) it.next();
	    			if(find(v.name)) {
	    				fields.add("+ " + v.name + ":" + v.type + "\n");
	    			} else {
	    				fields.add(((v.modifier & Modifier.PUBLIC) > 0? "+" : "- ") + v.name + ":" + v.type + "\n");
	    			}
	    		}
	    		for(Iterator<Pair> it = publicAcces.iterator(); it.hasNext(); )
	    			fields.add("+ " + it.next().who);
				UML += (isInterface? "interface " : (isAbstract ? "abstract ":"class  ")) + className + " {\n";
				for(Iterator<String> it = fields.iterator(); it.hasNext(); )
					UML += "\t" + it.next() + "\n";
				UML += "}\n";
				UML += inheritance +"\n";
	    	}
    		

			private boolean find(String name) {
				boolean result = false;
	    		for(int i = 0; i < publicAcces.size(); i++ ) {
	    			Pair p = publicAcces.get(i);
	    			if(p.what.compareTo(name) == 0) {
	    				publicAcces.remove(i);
	    				i--;
	    				result = true;
	    			}
	    		}
				return result;
			}

			private void uses(List<Parameter> list) {
	    		for(Iterator<Parameter> it = list.iterator(); it.hasNext();) {
	    			Parameter p = (Parameter) it.next();
	    			usingTypes.add(new Pair(className, p.getType().toString(), 1));
	    		}
	    	}
	    	private String planeL(List<?> l) {
    			String res = "";
    			for(Iterator<?> it = l.iterator(); it.hasNext(); ) {
    				res += it.next();
    			}
    			return res;
    		}
			String className;
			String inheritance = "";
			private List<Pair> publicAcces = new ArrayList<Pair>();
			private List<Variable> nonPublic = new ArrayList<Variable>();
			private boolean isInterface = false;
	    }
	    private static boolean isJava(File f) {
	    	String n = f.getName();
			return n.substring(n.length() - 4).equals("java");
	    }
}
