package tw.com.aber.sf.vo;

public class ResponseUtil {
	private static final long serialVersionUID = 1L;

	private Response response;
	private ResponseFail responseFail;

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public ResponseFail getResponseFail() {
		return responseFail;
	}

	public void setResponseFail(ResponseFail responseFail) {
		this.responseFail = responseFail;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
