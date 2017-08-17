/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peter.model.httpbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author peter
 */
public abstract class AbstractMessage {
    
    @JsonProperty
    protected final String latestVersion = "6";

    public String getVersion() {
        return latestVersion;
    }
    
    
}
