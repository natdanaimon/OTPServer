/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mm.sms;

import org.smslib.AGateway;
import org.smslib.IOrphanedMessageNotification;
import org.smslib.InboundMessage;

/**
 *
 * @author 
 */
public class OrphanedMessageNotification implements IOrphanedMessageNotification {

    /**
     *
     * @param gateway
     * @param msg
     * @return
     */
    @Override
    public boolean process(AGateway gateway, InboundMessage msg) {
//        System.out.println(">>> Orphaned message part detected from " + gateway.getGatewayId());
//        System.out.println(msg);
        // Since we are just testing, return FALSE and keep the orphaned message part.
        return false;
    }
}
