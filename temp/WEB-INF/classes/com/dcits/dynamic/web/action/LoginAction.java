package com.dcits.dynamic.web.action;

import com.alibaba.fastjson.JSON;
import com.dcits.dynamic.web.dao.WebUserDao;
import com.dcits.dynamic.web.dao.sys.WebUserRoleDao;
import com.dcits.dynamic.web.mapper.WebUser;
import com.dcits.dynamic.web.util.action.PageDrawManager;
import com.isprint.saml.readattribte.ReadAttribute;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Tim on 2015/5/25.
 */
@Controller
public class LoginAction {
    private static final Logger log = LoggerFactory.getLogger(LoginAction.class);

    //定义登陆成功后主页地址,直接访问到jsp,不经过spring mvc
    private static final String INDEX_PAGE = "index.jsp";

    @Resource
    WebUserDao webUserDao;
    @Resource
    WebUserRoleDao webUserRoleDao;

//    //单点登陆开关
//    @Value("${sso_flag}")
//    private String ssoFlag;

//    public void setSsoFlag(String ssoFlag) {
//        this.ssoFlag = ssoFlag;
//    }

    //单点登陆系统分配的 系统标识
    @Value("${sso_idpId}")
    private String ssoIdpId;

    public void setSsoIdpId(String ssoIdpId) {
        this.ssoIdpId = ssoIdpId;
    }

    //单点登陆SAML配置文件路径
    @Value("${sso_samlpath}")
    private String samlPropertiesPath;

    public void setSamlPropertiesPath(String samlPropertiesPath) {
        this.samlPropertiesPath = samlPropertiesPath;
    }

    @Value("${sso_portal}")
    private String ssoPortalUrl;

    public void setSsoPortalUrl(String ssoPortalUrl) {
        this.ssoPortalUrl = ssoPortalUrl;
    }
    //------------------------------------------------------------------------//

    @RequestMapping("/getLogin")
    @Transactional
    public void getLogin(HttpServletRequest request, PrintWriter printWriter,HttpServletResponse response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        String errorMsg = null;
        WebUser webUser = webUserDao.getUser(userId);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String sysdate = df.format(new Date());
        if (webUser == null) {
            errorMsg = "用户ID不存在！";
        } else {
            /*
            *统一认证不需要校验，只比较用户ID和密码
            String password1 = webUser.getPassword1();
            String password2 = webUser.getPassword2();
            String password3 = webUser.getPassword3();
            String password4 = webUser.getPassword4();
            String password5 = webUser.getPassword();
            String firstflag = webUser.getFirstflag();
            Date datesys = null;
            Date dateupd = null;
            String upddate = webUser.getUpddate();
            if (password.equals(password5)) {
                if ("A".equals(firstflag) && password5.matches("[a-zA-Z]+$") || password5.matches("\\d+$")) {
                    errorMsg = "系统升级，请修改密码";
                } else {
                    errorMsg = "密码不正确！";
                }
            } else if (password5.equals(DigestUtils.shaHex(password))) {
                if ("F".equals(firstflag)) {
                    errorMsg = "首次登陆必须修改密码";
                } else {
                    try {
                        datesys = df.parse(sysdate);
                        dateupd = df.parse(upddate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long days = (datesys.getTime() - dateupd.getTime()) / (1000 * 3600 * 24);
                    if (days >= 90) {
                        errorMsg = "密码存在超过90天必须修改密码";
                    } else {
                        Integer role = webUserRoleDao.selectRoleByuserId(userId);
                        if (role == null) {
                            errorMsg = "无权限，请先授权！";
                        } else {
                            errorMsg = "000000";
                            request.getSession().setMaxInactiveInterval(-1);
                            request.getSession().setAttribute("UserName", userId);
                            request.getSession().setAttribute("UserRole", role.toString());
                            String userRoleJsp = "role" + role.toString() + ".jsp";
                            request.getSession().setAttribute("userRoleJsp", userRoleJsp);
                            if (webUser.getLegalentity() != null) {
                                request.getSession().setAttribute("legalentity", webUser.getLegalentity());
                            }
                            if (webUser.getOrganization() != null) {
                                request.getSession().setAttribute("organization", webUser.getOrganization());
                            }
                            PageDrawManager.generateJspPage(request, role);
                        }
                    }
                }
            } else {
                errorMsg = "密码不正确！";
            }
            */
            //统一认证开关关闭时，通过本地登录，之简单比较用户、角色和密码，不做弱密码校验
            String passwordLocal = webUser.getPassword();
            if(passwordLocal.equals(password)){
                Integer role = webUserRoleDao.selectRoleByuserId(userId);
                if (role == null) {
                    errorMsg = "无权限，请先授权！";
                } else {
                    errorMsg = "000000";
                    request.getSession().setMaxInactiveInterval(-1);
                    request.getSession().setAttribute("UserName", userId);
                    request.getSession().setAttribute("UserRole", role.toString());
                    String userRoleJsp = "role" + role.toString() + ".jsp";
                    request.getSession().setAttribute("userRoleJsp", userRoleJsp);
                    if (webUser.getLegalentity() != null) {
                        request.getSession().setAttribute("legalentity", webUser.getLegalentity());
                    }
                    if (webUser.getOrganization() != null) {
                        request.getSession().setAttribute("organization", webUser.getOrganization());
                    }
                    PageDrawManager.generateJspPage(request, role);
                }
            } else {
                errorMsg = "密码不正确！";
            }


        }
        Map<String, String> finalResult = new HashMap<>();
        finalResult.put("errorMsg", errorMsg);
        String jsonStr = JSON.toJSONString(finalResult);
        printWriter.print(jsonStr);
        printWriter.flush();
        printWriter.close();
    }

    //@RequestMapping("/getUpdPassword")    接入统一认证不可以修改密码
    @Transactional
    public void getUpdLogin(HttpServletRequest request, PrintWriter printWriter) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String passwordnew = request.getParameter("password1");
        String errorMsg = null;
        WebUser webUser = webUserDao.getUser(userId);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String sysdate = df.format(new Date());
        if (webUser == null) {
            errorMsg = "用户ID不存在";
        } else {
            if (passwordnew.matches("^[0-9]*$")) {
                errorMsg = "密码强度不足，请重新输入";
            } else {
                if (passwordnew.matches("^[A-Za-z]+$")) {
                    errorMsg = "密码强度不足，请重新输入";
                } else {
                    String password1 = webUser.getPassword1();
                    String password2 = webUser.getPassword2();
                    String password3 = webUser.getPassword3();
                    String password4 = webUser.getPassword4();
                    String password5 = webUser.getPassword();
                    String firstflag = webUser.getFirstflag();
                    passwordnew = DigestUtils.shaHex(passwordnew);
                    if (password.equals(password5)) {
                        if ("A".equals(firstflag) && password5.matches("[a-zA-Z]+$") || password5.matches("\\d+$")) {
                            if (password1.equals(passwordnew)) {
                                errorMsg = "该密码与前五次密码相同，请重新输入";
                            } else {
                                if (password2.equals(passwordnew)) {
                                    errorMsg = "该密码与前五次密码相同，请重新输入";
                                } else {
                                    if (password3.equals(passwordnew)) {
                                        errorMsg = "该密码与前五次密码相同，请重新输入";
                                    } else {
                                        if (password4.equals(passwordnew)) {
                                            errorMsg = "该密码与前五次密码相同，请重新输入";
                                        } else {
                                            if (password5.equals(passwordnew)) {
                                                errorMsg = "该密码与前五次密码相同，请重新输入";
                                            } else {
                                                errorMsg = "000000";
                                                webUser.setUserId(userId);
                                                webUser.setPassword(passwordnew);
                                                webUser.setPassword1(password2);
                                                webUser.setPassword2(password3);
                                                webUser.setPassword3(password4);
                                                webUser.setPassword4(password5);
                                                webUser.setFirstflag("A");
                                                webUser.setUpddate(sysdate);
                                                webUserDao.setUser(webUser);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            errorMsg = "原密码不正确！";
                        }
                    } else if (password5.equals(DigestUtils.shaHex(password))) {
                        if (password1.equals(passwordnew)) {
                            errorMsg = "该密码与前五次密码相同，请重新输入";
                        } else {
                            if (password2.equals(passwordnew)) {
                                errorMsg = "该密码与前五次密码相同，请重新输入";
                            } else {
                                if (password3.equals(passwordnew)) {
                                    errorMsg = "该密码与前五次密码相同，请重新输入";
                                } else {
                                    if (password4.equals(passwordnew)) {
                                        errorMsg = "该密码与前五次密码相同，请重新输入";
                                    } else {
                                        if (password5.equals(passwordnew)) {
                                            errorMsg = "该密码与前五次密码相同，请重新输入";
                                        } else {
                                            errorMsg = "000000";
                                            webUser.setUserId(userId);
                                            webUser.setPassword(passwordnew);
                                            webUser.setPassword1(password2);
                                            webUser.setPassword2(password3);
                                            webUser.setPassword3(password4);
                                            webUser.setPassword4(password5);
                                            webUser.setFirstflag("A");
                                            webUser.setUpddate(sysdate);
                                            webUserDao.setUser(webUser);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        errorMsg = "原密码不正确！";
                    }
                }
            }
        }
        Map<String, String> finalResult = new HashMap<>();
        finalResult.put("errorMsg", errorMsg);
        String jsonStr = JSON.toJSONString(finalResult);
        printWriter.print(jsonStr);
        printWriter.flush();
        printWriter.close();
    }

    @RequestMapping("/loginOff")
    public void loginOff(HttpServletRequest request, PrintWriter printWriter) {
        request.getSession().removeAttribute("UserName");
        request.getSession().removeAttribute("UserRole");
    }


    /**
     * 单点登陆改造,进入应用根页面，即login后，根据开关判断进入不同的登陆地址
     *
     * @param request
     */
    @RequestMapping("/gologin")
    public void gologin(HttpServletRequest request, HttpServletResponse response) {
        String ssoFlag = getSSOFlag();
        log.info("ssoFlag :-------->" + ssoFlag);

        if ("true".equals(ssoFlag)) {
            log.info("使用 HF SSO 登陆,跳转到单点登陆地址");

            try {
                /*
                //TODO 无法按照示例,跳转到SSOUrl,只能直接重定向
                List authnContexts = new ArrayList();

                SPConfiguration conf = SPConfiguration.newInstance(samlPropertiesPath);
                SP sp = SP.newInstance(conf);
                AuthnRequest samlRequest = sp.generateAuthnRequest(ssoIdpId, authnContexts);
                String spRelayState = URLEncoder.encode("/" + INDEX_PAGE, "UTF-8");
                String AxMxSessionToken = request.getParameter("AxMxSessionToken"); //应该是null

                String redirectURL = (new StringBuilder())
                        .append(samlRequest.getSSOUrl()).append("?SAMLRequest=")
                        .append(samlRequest.getUrlEncodedAndDeflatedSAMLRequest())
                        .append("&RelayState=").append(spRelayState)
                        .append("&AxMxSessionToken=").append(AxMxSessionToken)
                        .append("&systemname=cmecweb").toString();


                response.sendRedirect(redirectURL);
                */

                //直接跳转到SSO主页
                response.sendRedirect(ssoPortalUrl);
            } catch (Exception e) {
                log.error("重定向页面出错", e);
            }

        } else {
            //原OM登陆方式
            log.info("使用SmartOM登陆方式");

            //将页面挑战到login.jsp
            try {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            } catch (Exception e) {
                log.error("重定向页面出错", e);
            }
        }
    }

    @RequestMapping("/acs")
    @Transactional
    public void acs(HttpServletRequest request, HttpServletResponse response, PrintWriter printWriter) {
        log.debug("sso acs request");

        String AxMxSessionToken = request.getParameter("AxMxSessionToken");
        if (AxMxSessionToken == null) {
            AxMxSessionToken = (String) request.getSession().getAttribute("AxMxSessionToken");
        }
        log.debug("sso acs AxMxSessionToken:" + AxMxSessionToken);

        String samlResponseParam = request.getParameter("SAMLResponse");
        log.debug("sso acs samlResponseParam:" + samlResponseParam);
        Map<String, String> attrMap = ReadAttribute.parseResponseParam(samlResponseParam);

        log.debug("attrMap:" + attrMap);

        //用户ID
        String userIdentity = attrMap.get("userid");
        log.debug("sso acs userIdentity:" + userIdentity);

        if (StringUtils.isEmpty(userIdentity)) {
            log.error("无法获得单点登陆用户ID,跳转回登陆页面");
            try {
                response.sendRedirect(ssoPortalUrl);
            } catch (IOException e) {
                log.error("重定向页面出错", e);
            }
            return;
        } else {
            Integer role = webUserRoleDao.selectRoleByuserId(userIdentity);
            //数据库设计已限定不可能为NULL

            log.info("role:" + role);
            //处理OM session
            PageDrawManager.generateJspPage(request, role);
            String userRoleJsp = "role" + role.toString() + ".jsp";

            request.getSession().setMaxInactiveInterval(-1);
            request.getSession().setAttribute("UserName", userIdentity);
            request.getSession().setAttribute("UserRole", role.toString());
            request.getSession().setAttribute("userRoleJsp", userRoleJsp);
            request.getSession().setAttribute("AxMxSessionToken", AxMxSessionToken);
            //重定向应用系统登录登陆后成功的页面
            try {
                //request.getRequestDispatcher("/" + INDEX_PAGE).forward(request, response);
                response.sendRedirect(request.getContextPath() + "/" + INDEX_PAGE);
            } catch (Exception e) {
                log.error("跳转页面出错", e);
            }
        }
    }

    /**
     * 实时读文件获取卡辅系统单点登陆开关状态
     * 测试发现，数据会存入缓存，还是需要重启服务器
     * @return
     */
    public static String getSSOFlag() {
        Properties props = new Properties();
        InputStream input = LoginAction.class.getClassLoader().getResourceAsStream("galaxy.properties");
        try {
            props.load(input);
        } catch (Exception e) {
            log.error("galaxy.properties文件获取sso_flag参数失败", e);
        }finally {
            try {
                if(input != null) input.close();
            } catch (IOException e) {
                log.error("文件流关闭失败", e);
            }
        }
        return props.getProperty("sso_flag");
    }
}