
public class ASMifier implements Test{
	
	
	public Object[] call (Object... args) {		
		Long longObject = (Long) new Object();
		
		String str = (String) args[0];
		
		return args;
	}
	
	public void test () {		
		call("dfgd", 1, 100.7);
	}
	
	public static void main (String[] args) throws Exception {
		
		org.objectweb.asm.util.ASMifier.main(new String[] {"Test"});
	}
}

interface Test {
	public Long aaa = new Long(24);
}