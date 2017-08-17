/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peter.bl;

import com.peter.model.httpbody.AbstractMessage;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

/**
 *
 * @author peter
 */
@Component
public class AppBL {
    
    public AbstractMessage doSomething(String inputVersion) {
        User user = (User) getContext().getAuthentication().getPrincipal();
        
        return null;
    }
    
}
