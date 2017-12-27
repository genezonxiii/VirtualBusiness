package tw.com.aber.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import tw.com.aber.dao.LoginDao;
import tw.com.aber.vo.MenuVO;
import tw.com.aber.vo.UserVO;

public class LoginService {
	private static final Logger logger = LogManager.getLogger(LoginService.class);

	private LoginDao dao;

	public LoginService() {
		dao = new LoginDao();
	}

	public List<UserVO> selectlogin(String p_email, String p_password, String p_unicode) {
		return dao.loginDB(p_email, p_password, p_unicode);
	}

	public Boolean checkuser(String p_email) {
		return dao.checkuser(p_email);
	}

	public Boolean checkconnect() {
		return dao.checkconnect();
	}

	public Boolean checkunicode(String unicode) {
		return dao.checkunicode(unicode);
	}

	public List<MenuVO> getMenuList() {
		List<MenuVO> main = null;

		logger.debug("getMainMenu start");

		main = dao.getMainMenuDB();
		logger.debug("getMainMenu end");

		for (int i = 0; i < main.size(); i++) {
			List<MenuVO> subMenu = null;

			logger.debug("get subMenu start:" + main.get(i).getId());

			subMenu = setSubMenu(main.get(i).getId());
			main.get(i).setSubMenu(subMenu);

			logger.debug("get subMenu end");
		}
		;

		return main;
	}

	public List<MenuVO> setSubMenu(String parent_id) {
		List<MenuVO> temp = null;

		temp = dao.getSubMenuDB(parent_id);

		if (temp == null) {
			return null;
		} else {
			for (int i = 0; i < temp.size(); i++) {
				List<MenuVO> tempSub = null;
				tempSub = setSubMenu(temp.get(i).getId());
				temp.get(i).setSubMenu(tempSub);
			}
		}
		return temp;
	}

	public String getMenuListToString() {
		List<MenuVO> list = getMenuList();

		logger.debug("result getMenu list size: " + list.size());

		Gson gson = new Gson();
		String jsonStrList = gson.toJson(list);

		return jsonStrList;
	}

	public static void main(String[] args) {
		LoginService service = new LoginService();
		List<UserVO> list = service.selectlogin("", "", "");
		logger.debug(list);
	}
}
