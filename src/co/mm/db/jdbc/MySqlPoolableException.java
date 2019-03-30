/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mm.db.jdbc;

@SuppressWarnings("serial")

public class MySqlPoolableException extends Exception {

    public MySqlPoolableException(final String msg, Exception e) {
        super(msg, e);
    }
}
