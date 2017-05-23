package tw.com.aber.vo;

import java.io.Serializable;

public class StockModTypeVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mod_type;
	private String main_reason;
	private String second_reason;
	private String mod_act;
	private String memo;

	public String getMod_type() {
		return mod_type;
	}

	public void setMod_type(String mod_type) {
		this.mod_type = mod_type;
	}

	public String getMain_reason() {
		return main_reason;
	}

	public void setMain_reason(String main_reason) {
		this.main_reason = main_reason;
	}

	public String getSecond_reason() {
		return second_reason;
	}

	public void setSecond_reason(String second_reason) {
		this.second_reason = second_reason;
	}

	public String getMod_act() {
		return mod_act;
	}

	public void setMod_act(String mod_act) {
		this.mod_act = mod_act;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
