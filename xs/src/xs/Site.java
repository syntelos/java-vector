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

import gap.*;
import gap.data.*;
import gap.util.*;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.xmpp.*;

import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;

/**
 * Deliver pages and interpret invite API
 */
public class Site
    extends gap.service.Servlet
{
    private XMPPService xs;


    public Site(){
        super();
    }


    protected final XMPPService getXMPPService(){
        if (null == this.xs)
            this.xs = XMPPServiceFactory.getXMPPService();

        return xs;
    }
    protected void doPost(Request req, Response rep)
        throws ServletException, IOException
    {
        req.setViewerDisabled();
        if ("invite".equals(req.getSource())){
            try {
                final XAddress address = new XAddress.Logon(req.getParameter("address"));

                final JID jid = address.toJID();

                if (null != jid){

                    final XMPPService xs = this.getXMPPService();

                    xs.sendInvitation(jid,Version.From);
                }
            }
            catch (RuntimeException exc){
            }
        }
        rep.sendRedirect("/index.html");
    }

}
