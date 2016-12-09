package sl;

public class Elem {

	private java.lang.String Name;
	private Long Other;

	public Elem(java.lang.String name, Long other) {
		super();
		Name = name;
		Other = other;
	}

	public java.lang.String getName() {
		return Name;
	}

	public void setName(java.lang.String name) {
		this.Name = name;
	}

	public Long getOther() {
		return Other;
	}

	public void setOther(Long other) {
		this.Other = other;
	}

	@Override
	public java.lang.String toString() {
		return "Elem {Name=" + Name + ", Other=" + Other + "}";
	}

}
