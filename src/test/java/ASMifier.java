
public class ASMifier {
	public Object[] call (Object... args) {		
		return args;
	}
	
	public void test () {		
		call("dfgd", 1, 100.7);
	}
	
	public static void main (String[] args) throws Exception {
		
		
		org.objectweb.asm.util.ASMifier.main(new String[] {"ASMifier"});
	}
}
