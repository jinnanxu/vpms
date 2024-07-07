package com.vpms.controller.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.vpms.dao.mapper.UserMapper;
import com.vpms.dao.model.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.vpms.author.JwtConfig;
import com.vpms.controller.vo.JsonResult;

import java.util.List;

/**
 * 前台学生相关接口
 */
@RestController
@RequestMapping("/user")
public class FrontUserController {

	Logger logger = LoggerFactory.getLogger(FrontUserController.class);

	@Resource
	private JwtConfig jwtConfig ;
	@Autowired
	private UserMapper userMapper;

	/**
	 * 前台用户登录
	 * @param pwd
	 * @param mobile
	 * @return
	 */
	@PostMapping("/login")
	public JsonResult login (@RequestParam("pwd") String pwd, @RequestParam("mobile") String mobile){
		JSONObject json = new JSONObject();
		User record = new User();
		//以手机号与密码为条件查询数据库，看是否存在用户
		record.setPwd(pwd);
		record.setMobile(mobile);
		User user = userMapper.selectOne(record);
		if(user==null) {
			return JsonResult.error("403", "用户不存在");
		}
		if(user.getStatus() == -1){
			JsonResult jsonResult = JsonResult.error("500", "该用户已被冻结");
			jsonResult.setSuccess(false);
			return jsonResult;
		}
		//将手机号存入token中，之后就可以在TokenInterceptor中根据token取到用户
		String token = jwtConfig.createToken(user.getUserId()+"");
		if (!StringUtils.isEmpty(token)) {
			json.put("token",token) ;
		}
		json.put("user", user);
		return JsonResult.success(json) ;
	}

	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	@PostMapping("/register")
	public JsonResult register (User user){
		User record = new User();
		//以手机号为条件查询数据库，看是否存在用户，存在则不请允许注册
		record.setMobile(user.getMobile());
		int existCount = userMapper.selectCount(record);
		if(existCount>0) {
			return JsonResult.error("500", "该手机号已注册");
		}
		//将新用户保存到数据库
		userMapper.insertSelective(user);
		return JsonResult.success(user) ;
	}

	@RequestMapping("updateUser")
	public JsonResult updateUser(User user, HttpServletRequest request) {
		userMapper.updateByPrimaryKeySelective(user);
		User newUser = userMapper.selectByPrimaryKey(user.getUserId());
		JsonResult ret = new JsonResult();
		ret.setData(newUser);
		return ret;
	}

}
