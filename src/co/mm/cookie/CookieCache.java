/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mm.cookie;

import okhttp3.Cookie;

import java.util.Collection;

public interface CookieCache extends Iterable<Cookie> {

    void addAll(Collection<Cookie> cookies);
    void clear();

}
