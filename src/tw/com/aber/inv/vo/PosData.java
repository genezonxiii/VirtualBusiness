package tw.com.aber.inv.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "POSDATA")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "id", "sellerId", "posId", "posSn", "companyName", "groupNum" })

public class PosData {
	private static final long serialVersionUID = 1L;

	/*
	 * D01
	 */
	@XmlElement(name = "ID")
	private String id;

	/*
	 * D01
	 */
	@XmlElement(name = "SELLERID")
	private String sellerId;

	/*
	 * D01
	 */
	@XmlElement(name = "POSID")
	private String posId;

	/*
	 * D01
	 */
	@XmlElement(name = "POSSN")
	private String posSn;

	/*
	 * D01
	 */
	@XmlElement(name = "COMPANY_NAME")
	private String companyName;

	/*
	 * D01
	 */
	@XmlElement(name = "GROUPNUM")
	private String groupNum;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public String getPosSn() {
		return posSn;
	}

	public void setPosSn(String posSn) {
		this.posSn = posSn;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(String groupNum) {
		this.groupNum = groupNum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
