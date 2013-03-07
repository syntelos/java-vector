/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2012, The DigiVac Company
 * 
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package xs;

import oso.data.Person;

import gap.*;
import gap.data.*;
import gap.util.*;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.xmpp.*;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogRecord;
import javax.servlet.ServletException;

/**
 * Primary service maintains the {@link oso.data.Person Person} table
 * as its XMPP roster.
 * 
 * A chat message sent to this service is repeated to available
 * membership.
 * 
 * {@link At} commands provide an interactive feature set.
 */
public class XMPP
    extends gap.service.XMPPServlet
{
    protected final static Logger Log = Logger.getLogger(XMPP.class.getName());

    public enum Actor {
        help, invite, rtfm;


        public final static String HelpText = "@invite <invitee-email> -- Send invitation to invitee; @help -- This message.";

        public final static Actor For(At.Command at){
            try {
                return Actor.valueOf(at.head);
            }
            catch (RuntimeException exc){
                return null;
            }
        }
    }
    protected static void Occupied(XAddress from, XMPPService xs){

        xs.sendPresence(from.toJID(),PresenceType.AVAILABLE,PresenceShow.CHAT,"occupied",Version.From);
    }
    protected static void Idle(XAddress from, XMPPService xs){

        xs.sendPresence(from.toJID(),PresenceType.AVAILABLE,PresenceShow.AWAY,"idle",Version.From);
    }
    protected static void Probe(XAddress from, XMPPService xs){

        if (Person.ActiveRecipients(from)){

            XMPP.Occupied(from,xs);
        }
        else {

            XMPP.Idle(from,xs);
        }
    }
    protected static void Subscribe(XAddress from, XMPPService xs){

        Person personFrom = Person.GetCreate(from);
        personFrom.setAvailable(true);
        personFrom.setResource(from.resource);
        if (personFrom.isDirty()){

            personFrom.save();

            XMPP.Occupied(from,xs);
        }
    }
    protected static void Unsubscribe(XAddress from, XMPPService xs){

        Person personFrom = Person.Get(from);
        if (null != personFrom)
            personFrom.drop();
    }
    protected static void Available(XAddress from, XMPPService xs){
        Person personFrom = Person.Get(from);
        if (null != personFrom){
            final String resourceFrom = from.resource;
            final String resourcePerson = personFrom.getResource();

            if (null != resourceFrom && null != resourcePerson){

                if (resourceFrom.equals(resourcePerson)){

                    if (personFrom.setAvailable(true)){
                        personFrom.save();
                    }
                }
            }
            else if (personFrom.setAvailable(true)){
                personFrom.save();
            }
        }
        else {

            XMPP.Subscribe(from,xs);
        }
    }
    protected static void Unavailable(XAddress from, XMPPService xs){
        Person personFrom = Person.Get(from);
        if (null != personFrom){
            final String resourceFrom = from.resource;
            final String resourcePerson = personFrom.getResource();

            if (null != resourceFrom && null != resourcePerson){

                if (resourceFrom.equals(resourcePerson)){

                    personFrom.setAvailable(false);
                    /*
                     * Too sticky.. (permit to follow)
                     */
                    personFrom.setResource(null);

                    if (personFrom.isDirty())
                        personFrom.save();
                }
            }
            else {
                personFrom.setAvailable(false);
                personFrom.setResource(null);

                if (personFrom.isDirty())
                    personFrom.save();
            }
        }
    }

    public XMPP(){
        super();
    }


    /**
     * Chat with at commands
     */
    @Override
    protected void doChat(Request req, Response rep, XMPPService xs,
                          com.google.appengine.api.xmpp.Message msg)
        throws ServletException, IOException
    {
        final XAddress from = new XAddress.From(msg);
        Log.log(Level.INFO,String.format("FROM %s",from));
        try {
            final String body = msg.getBody();
            /*
             * Process body for 
             * At command, or
             * Normal chat.
             */
            final At.Command at = new At.Command(body);
            switch(at.at){
            /*
             * At command
             */
            case Head:{
                Person personFrom = Person.Get(from);

                if (null != personFrom){

                    personFrom.setResource(from.resource);
                    personFrom.setAvailable(true);
                    if (personFrom.isDirty())
                        personFrom.save();

                    Actor action = Actor.For(at);
                    if (null != action){
                        switch(action){
                        case rtfm:
                        case help:{

                            final Message 
                                xm = new MessageBuilder().withMessageType(MessageType.CHAT)
                                .withFromJid(Version.From)
                                .withBody(Actor.HelpText)
                                .withRecipientJids(from.toRecipients()).build();

                            xs.sendMessage(xm);

                            break;
                        }
                        case invite:{
                            try {
                                final XAddress invitee = new XAddress.Logon(at.tail);

                                final JID inviteeJid = invitee.toJID();
                                if (null != inviteeJid){
                                    xs.sendInvitation(inviteeJid,Version.From);

                                    final StringBuilder message = new StringBuilder();

                                    message.append("Invited '");
                                    message.append(invitee.logon);
                                    message.append("'");

                                    final Message 
                                        xm = new MessageBuilder().withMessageType(MessageType.CHAT)
                                        .withFromJid(Version.From)
                                        .withBody(message.toString())
                                        .withRecipientJids(from.toRecipients()).build();

                                    xs.sendMessage(xm);
                                }
                                else {
                                    final StringBuilder message = new StringBuilder();

                                    message.append("Incomplete address '");
                                    message.append(at.tail);
                                    message.append("'");

                                    final Message 
                                        xm = new MessageBuilder().withMessageType(MessageType.CHAT)
                                        .withFromJid(Version.From)
                                        .withBody(message.toString())
                                        .withRecipientJids(from.toRecipients()).build();

                                    xs.sendMessage(xm);
                                }
                            }
                            catch (RuntimeException exc){

                                final StringBuilder message = new StringBuilder();

                                message.append("Unrecognized invitee '");
                                message.append(at.tail);
                                message.append("'");

                                final Message 
                                    xm = new MessageBuilder().withMessageType(MessageType.CHAT)
                                    .withFromJid(Version.From)
                                    .withBody(message.toString())
                                    .withRecipientJids(from.toRecipients()).build();

                                xs.sendMessage(xm);
                            }
                            break;
                        }
                        default:{

                            final StringBuilder message = new StringBuilder();

                            message.append("Unimplemented request '");
                            message.append(at.head);
                            message.append("'");

                            final Message 
                                xm = new MessageBuilder().withMessageType(MessageType.CHAT)
                                .withFromJid(Version.From)
                                .withBody(message.toString())
                                .withRecipientJids(from.toRecipients()).build();

                            xs.sendMessage(xm);
                            break;
                        }
                        }
                    }
                    else {

                        final StringBuilder message = new StringBuilder();

                        message.append("Unrecognized request '");
                        message.append(at.head);
                        message.append("'");

                        final Message 
                            xm = new MessageBuilder().withMessageType(MessageType.CHAT)
                            .withFromJid(Version.From)
                            .withBody(message.toString())
                            .withRecipientJids(from.toRecipients()).build();

                        xs.sendMessage(xm);
                    }
                    this.ok(req,rep);
                }
                else {
                    Log.log(Level.INFO,String.format("Unknown FROM '%s'",from));

                    this.er(req,rep,400,"Bad request");
                }
                break;
            }
            /*
             * Normal chat
             */
            case Tail:{

                Person personFrom = Person.Get(from);

                if (null != personFrom){

                    personFrom.setAvailable(true);
                    personFrom.setResource(from.resource);
                    if (personFrom.isDirty())
                        personFrom.save();


                    final StringBuilder message = new StringBuilder();
                    message.append('@');
                    message.append(from.identifier);
                    message.append(": ");
                    message.append(body);

                    JID[] recipients = Person.ListRecipients(from);
                    if (null != recipients){

                        final Message 
                            xm = new MessageBuilder().withMessageType(MessageType.CHAT)
                            .withFromJid(Version.From)
                            .withBody(message.toString())
                            .withRecipientJids(recipients).build();

                        xs.sendMessage(xm);
                    }
                    this.ok(req,rep);
                }
                else {
                    Log.log(Level.INFO,String.format("Unknown FROM '%s'",from));

                    this.er(req,rep,400,"Bad request");
                }
                break;
            }
            default:
                Log.log(Level.INFO,String.format("EMPTY @ FROM '%s'",from));

                this.ok(req,rep);
            }
        }
        catch (RuntimeException exc){
            LogRecord log = new LogRecord(Level.INFO,String.format("FROM '%s'",from));
            log.setThrown(exc);

            Log.log(log);

            this.er(req,rep,400,"Failed request");
        }
    }
    @Override
    protected void doSubscribe(Request req, Response rep, XMPPService xs, 
                               com.google.appengine.api.xmpp.Subscription sub)
        throws ServletException, IOException
    {
        final XAddress from = new XAddress.From(sub);
        Log.log(Level.INFO,String.format("FROM %s",from));
        try {
            XMPP.Subscribe(from,xs);

            this.ok(req,rep);
        }
        catch (RuntimeException exc){
            LogRecord log = new LogRecord(Level.INFO,String.format("FROM '%s'",from));
            log.setThrown(exc);

            Log.log(log);

            this.er(req,rep,400,"Failed request");
        }
    }
    @Override
    protected void doUnsubscribe(Request req, Response rep, XMPPService xs, 
                                 com.google.appengine.api.xmpp.Subscription sub)
        throws ServletException, IOException
    {
        final XAddress from = new XAddress.From(sub);
        Log.log(Level.INFO,String.format("FROM %s",from));
        try {
            XMPP.Unsubscribe(from,xs);

            this.ok(req,rep);
        }
        catch (RuntimeException exc){
            LogRecord log = new LogRecord(Level.INFO,String.format("FROM '%s'",from));
            log.setThrown(exc);

            Log.log(log);

            this.er(req,rep,400,"Failed request");
        }
    }
    @Override
    protected void doSubscribed(Request req, Response rep, XMPPService xs, 
                                com.google.appengine.api.xmpp.Subscription sub)
        throws ServletException, IOException
    {
        this.doSubscribe(req,rep,xs,sub);
    }
    @Override
    protected void doUnsubscribed(Request req, Response rep, XMPPService xs, 
                                  com.google.appengine.api.xmpp.Subscription sub)
        throws ServletException, IOException
    {
        this.doUnsubscribe(req,rep,xs,sub);
    }

    @Override
    protected void doAvailable(Request req, Response rep, XMPPService xs, 
                               com.google.appengine.api.xmpp.Presence pre)
        throws ServletException, IOException
    {
        final XAddress from = new XAddress.From(pre);
        Log.log(Level.INFO,String.format("FROM %s",from));
        try {
            switch(pre.getPresenceShow()){
            case NONE:
            case CHAT:

                XMPP.Available(from,xs);
                break;

            default:

                XMPP.Unavailable(from,xs);
                break;
            }
            this.ok(req,rep);
        }
        catch (RuntimeException exc){
            LogRecord log = new LogRecord(Level.INFO,String.format("FROM '%s'",from));
            log.setThrown(exc);

            Log.log(log);

            this.er(req,rep,400,"Failed request");
        }
    }
    @Override
    protected void doUnavailable(Request req, Response rep, XMPPService xs, 
                                 com.google.appengine.api.xmpp.Presence pre)
        throws ServletException, IOException
    {
        final XAddress from = new XAddress.From(pre);
        Log.log(Level.INFO,String.format("FROM %s",from));
        try {

            XMPP.Unavailable(from,xs);

            this.ok(req,rep);
        }
        catch (RuntimeException exc){
            LogRecord log = new LogRecord(Level.INFO,String.format("FROM '%s'",from));
            log.setThrown(exc);

            Log.log(log);

            this.er(req,rep,400,"Failed request");
        }
    }
    @Override
    protected void doProbe(Request req, Response rep, XMPPService xs, 
                           com.google.appengine.api.xmpp.Presence pre)
        throws ServletException, IOException
    {
        final XAddress from = new XAddress.From(pre);
        Log.log(Level.INFO,String.format("FROM %s",from));
        try {
            XMPP.Probe(from,xs);

            this.ok(req,rep);
        }
        catch (RuntimeException exc){
            LogRecord log = new LogRecord(Level.INFO,String.format("FROM '%s'",from));
            log.setThrown(exc);

            Log.log(log);

            this.er(req,rep,400,"Failed request");
        }
    }

}
