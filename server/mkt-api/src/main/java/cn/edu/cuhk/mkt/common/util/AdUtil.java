package cn.edu.cuhk.mkt.common.util;

import cn.edu.cuhk.mkt.common.enums.AdSyncEnum;
import cn.edu.cuhk.mkt.config.AdConfig;
import cn.edu.cuhk.mkt.entity.ad.AdOrgDTO;
import cn.edu.cuhk.mkt.entity.ad.AdStaffDTO;
import cn.edu.cuhk.mkt.entity.ad.AdStudentDTO;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * AD域认证工具类
 *
 * @author taokai
 */
@Slf4j
public class AdUtil {

    /**
     * 检测AD域登录认证是否成功
     * @param userName 账号
     * @param password 密码
     * @return 成功：true，失败：false
     */
    public static Boolean checkAdLogin(String userName, String password){
        String host = AdConfig.getHost();
        String port = AdConfig.getPort();
        String domain = AdConfig.getDomain();
        return false;
    }

    /**
     * AD 域信息获取
     *
     * @param adDomain ad 域名
     * @param adPort   ad 端口
     * @param adSyncEnum 同步类型
     */
    public static NamingEnumeration<SearchResult> readLdap(String adDomain, String adPort, AdSyncEnum adSyncEnum) {
        DirContext ctx = null;
        NamingEnumeration<SearchResult> answer = null;
        try {
            ctx = new InitialDirContext(new Hashtable<String, String>() {{
                put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                put(Context.PROVIDER_URL, "ldap://" + adDomain + ":" + adPort);
                put(Context.SECURITY_AUTHENTICATION, "simple");
                put(Context.SECURITY_PRINCIPAL, "CN=getad-ydmh,OU=Manage Srv,OU=CUHKSZ Account,DC=CUHK,DC=EDU,DC=CN");
                put(Context.SECURITY_CREDENTIALS, "8Wh4CeD&.4C.QTiApbXyu?KW");
            }});

            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            switch (adSyncEnum)
            {
                case ORG_STAFF:
                    answer = ctx.search(AdSyncEnum.ORG_STAFF.getAdKey(), "(&(|(CN=*)))", searchControls);
                    break;
                case ORG_STUDENT:
                    answer = ctx.search(AdSyncEnum.ORG_STUDENT.getAdKey(), "(&(|(OU=*)))", searchControls);
                    break;
                case STAFF:
                    answer = ctx.search(AdSyncEnum.STAFF.getAdKey(), "(&(|(employeeID=*)))", searchControls);
                    break;
                case STUDENT:
                    answer = ctx.search(AdSyncEnum.STUDENT.getAdKey(), "(&(|(employeeID=*)))", searchControls);
                    break;
                default:
                    break;
            }
        } catch (CommunicationException e) {
            log.error("AD域连接失败，{}", e.getMessage(), e);
            AssertUtil.isNull(null, "AD域连接失败");
        } catch (Exception e) {
            log.error("AD域操作异常，{}", e.getMessage(), e);
            AssertUtil.isNull(null, "AD域操作异常");
        } finally {
            try {
                if(ctx != null){
                    ctx.close();
                }
            }catch (Exception e){
                log.error("AD域认证关闭流异常，{}", e.getMessage(), e);
            }
        }
        return answer;
    }

    /**
     * 获取AD域组织信息(包括教师组织，学生组织)
     *
     * @param adDomain 域名
     * @param adPort   端口
     * @param type     同步类型
     * @return AD域组织信息对象集合
     * @throws NamingException
     */
    @SneakyThrows
    public static List<AdOrgDTO> getAdOrgList(String adDomain, String adPort, AdSyncEnum type) {
        NamingEnumeration<SearchResult> answer = readLdap(adDomain, adPort, type);

        if (answer == null) {
            return null;
        }

        List<AdOrgDTO> result = Lists.newArrayList();

        while (answer.hasMoreElements()) {
            SearchResult sr;
            sr = answer.next();
            Attributes attrs = sr.getAttributes();
            NamingEnumeration<? extends Attribute> attrList = attrs.getAll();
            Attribute attr;
            AdOrgDTO entity = new AdOrgDTO();
            while (attrList.hasMore()) {
                attr = attrList.next();
                String attrName = attr.getID();
                String value = attr.get().toString();
                switch (attrName) {
                    case "description":
                        entity.setDescription(value);
                        break;
                    case "distinguishedName":
                        entity.setDistinguishedName(value);
                        break;
                    case "name":
                        entity.setName(value);
                        break;
                    default:
                }
            }
            result.add(entity);
        }
        return result;
    }

    /**
     * 获取AD域教师信息
     *
     * @param adDomain 域名
     * @param adPort   端口
     * @param type     同步类型
     * @return AD域教师信息对象集合
     * @throws NamingException
     */
    @SneakyThrows
    public static List<AdStaffDTO> getAdStaffList(String adDomain, String adPort, AdSyncEnum type) {
        NamingEnumeration<SearchResult> answer = readLdap(adDomain, adPort, type);

        if (answer == null) {
            return null;
        }

        List<AdStaffDTO> result = Lists.newArrayList();
        while (answer.hasMoreElements()) {
            SearchResult sr;
            sr = answer.next();
            Attributes attrs = sr.getAttributes();
            NamingEnumeration<? extends Attribute> attrList = attrs.getAll();
            Attribute attr;
            AdStaffDTO entity = new AdStaffDTO();
            while (attrList.hasMore()) {
                attr = attrList.next();
                String attrName = attr.getID();
                String value = attr.get().toString();
                switch (attrName) {
                    case "department":
                        entity.setDepartment(value);
                        break;
                    case "name":
                        entity.setName(value);
                        break;
                    case "displayName":
                        entity.setDisplayName(value);
                        break;
                    case "employeeID":
                        entity.setEmployeeId(value);
                        break;
                    case "ipPhone":
                        entity.setIpPhone(value);
                        break;
                    case "mail":
                        entity.setMail(value);
                        break;
                    case "mailNickname":
                        entity.setMailNickName(value);
                        break;
                    case "title":
                        entity.setTitle(value);
                        break;
                    case "mobile":
                        entity.setMobile(value);
                        break;
                    case "physicalDeliveryOfficeName":
                        entity.setPhysicalDeliveryOfficeName(value);
                        break;
                    case "distinguishedName":
                        entity.setDistinguishedName(value);
                        break;
                    case "memberOf":
                        try{
                            NamingEnumeration list=attr.getAll();
                            List<String> memberofs=new ArrayList<>();
                            String valueStr = "";
                            while (list.hasMore()) {
                                valueStr= list.next().toString();
                                memberofs.add(valueStr);
                            }
                            entity.setMemberof(memberofs);
                        }catch (Exception e){
                        }
                        break;
                    default:
                }
            }
            result.add(entity);
        }
        return result;
    }

    /**
     * 获取AD域学生信息
     *
     * @param adDomain 域名
     * @param adPort   端口
     * @param type     同步类型
     * @return AD域学生信息对象集合
     * @throws NamingException
     */
    @SneakyThrows
    public static List<AdStudentDTO> getAdStudentList(String adDomain, String adPort, AdSyncEnum type) {
        NamingEnumeration<SearchResult> answer = readLdap(adDomain, adPort, type);

        if (answer == null) {
            return null;
        }

        List<AdStudentDTO> result = Lists.newArrayList();
        while (answer.hasMoreElements()) {
            SearchResult sr;
            sr = answer.next();
            Attributes attrs = sr.getAttributes();
            NamingEnumeration<? extends Attribute> attrList = attrs.getAll();
            Attribute attr;
            AdStudentDTO entity = new AdStudentDTO();
            while (attrList.hasMore()) {
                attr = attrList.next();
                String attrName = attr.getID();
                String value = attr.get().toString();
                switch (attrName) {
                    case "department":
                        entity.setDepartment(value);
                        break;
                    case "description":
                        entity.setDescription(value);
                        break;
                    case "name":
                        entity.setName(value);
                        break;
                    case "displayName":
                        entity.setDisplayName(value);
                        break;
                    case "employeeID":
                        entity.setEmployeeId(value);
                        break;
                    case "employeeType":
                        entity.setEmployeeType(value);
                        break;
                    case "mail":
                    case "userPrincipalName":
                        entity.setMail(value);
                        break;
                    case "mailNickname":
                        entity.setMailNickName(value);
                        break;
                    case "title":
                        entity.setTitle(value);
                        break;
                    case "distinguishedName":
                        entity.setDistinguishedName(value);
                        break;
                    default:
                }
            }
            result.add(entity);
        }
        return result;
    }
    
}
