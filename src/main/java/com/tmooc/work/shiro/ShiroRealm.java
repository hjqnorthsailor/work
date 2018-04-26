package com.tmooc.work.shiro;

import com.tmooc.work.dao.UserDao;
import com.tmooc.work.entity.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    private UserDao userDao;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        User user= (User) principalCollection.getPrimaryPrincipal();
        user.getRoles().forEach(r->{simpleAuthorizationInfo.addRole(r.getRole());
            r.getPermissions().forEach(p->simpleAuthorizationInfo.addStringPermission(p.getPermission()));
        });
        return simpleAuthorizationInfo;
    }

    /**
     * 登陆操作
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken= (UsernamePasswordToken) authenticationToken;
        final String username = usernamePasswordToken.getUsername();
        final User userDb = userDao.findByUsername(username);
        if (userDb==null){
            return  null;
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo=new SimpleAuthenticationInfo(userDb,userDb.getPassword()
        ,ByteSource.Util.bytes(userDb.getSalt()),getName());
        return simpleAuthenticationInfo;
    }
}
