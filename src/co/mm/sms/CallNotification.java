/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mm.sms;

import org.smslib.AGateway;
import org.smslib.ICallNotification;

/**
 *
 * @author 
 */
public class CallNotification implements ICallNotification {

    /**
     *
     * @param gateway
     * @param callerId
     */
    @Override
    public void process(AGateway gateway, String callerId) {
//        System.out.println(">>> New call detected from Gateway: " + gateway.getGatewayId() + " : " + callerId);
    }
}
