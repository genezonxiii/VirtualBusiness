package tw.com.aber.invoice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "B")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "B1","B2","B3","B4","B5","B6","B7" })

public class B {
	private static final long serialVersionUID = 1L;
	/*
	 * C0401
	 */
	@XmlElement(name = "B1")
	private String B1;
	/*
	 * C0401
	 */
	@XmlElement(name = "B2")
	private String B2;
	/*
	 * C0401
	 */
	@XmlElement(name = "B3")
	private String B3;
	/*
	 * C0401
	 */
	@XmlElement(name = "B4")
	private String B4;
	/*
	 * C0401
	 */
	@XmlElement(name = "B5")
	private String B5;
	/*
	 * C0401
	 */
	@XmlElement(name = "B6")
	private String B6;
	/*
	 * C0401
	 */
	@XmlElement(name = "B7")
	private String B7;
	public String getB1() {
		return B1;
	}
	public void setB1(String b1) {
		B1 = b1;
	}
	public String getB2() {
		return B2;
	}
	public void setB2(String b2) {
		B2 = b2;
	}
	public String getB3() {
		return B3;
	}
	public void setB3(String b3) {
		B3 = b3;
	}
	public String getB4() {
		return B4;
	}
	public void setB4(String b4) {
		B4 = b4;
	}
	public String getB5() {
		return B5;
	}
	public void setB5(String b5) {
		B5 = b5;
	}
	public String getB6() {
		return B6;
	}
	public void setB6(String b6) {
		B6 = b6;
	}
	public String getB7() {
		return B7;
	}
	public void setB7(String b7) {
		B7 = b7;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
