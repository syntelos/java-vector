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
package xmpp;

import org.jivesoftware.smack.util.StringUtils;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.prefs.BackingStoreException;
import java.util.prefs.NodeChangeListener;
import java.util.prefs.PreferenceChangeListener;

/**
 * Desktop preferences 
 * 
 * <h3>Security</h3>
 * 
 * <p> Persistent desktop preferences require desktop database
 * read/write access rights.  These rights are available when the
 * executable JAR is executed locally.  From Web Start JNLP, all jar
 * files must be signed and access requirements declared (in JNLP).
 * </p>
 * 
 * <p> When access is denied, instances of this class will substitute
 * temporary storage that is not persistent to the client
 * desktop. </p>
 */
public class Preferences
    extends java.util.prefs.Preferences
{
    public final static Preferences Instance = new Preferences();

    public final static String Session = "Session";

    public final static String GetSession(){
        return Instance.get(Session,StringUtils.randomString(6));
    }
    public final static void SetSession(String m){
        if (null != m){
            m = m.trim();
            if (0 < m.length()){
                Preferences.ResourceValue = null;
                Instance.put(Session,m);
            }
        }
    }

    public final static String Host = "Host";

    private volatile static String HostValue ;

    public final static String GetHost(){
        return (Preferences.HostValue = Instance.get(Host,XAddress.Default.Host));
    }
    public final static void SetHost(String m){
        if (null != m){
            m = m.trim();
            if (0 < m.length()){
                Preferences.HostValue = m;
                Instance.put(Host,m);
            }
        }
    }
    public final static boolean IsHostDefault(String host){
        if (null == Preferences.HostValue){
            Preferences.HostValue = GetHost();
        }

        final String defaultHostStatic = XAddress.Default.Host;

        final String defaultHostUser = Preferences.HostValue;

        if (defaultHostStatic.equals(defaultHostUser))
            return (defaultHostStatic.equals(host));
        else
            return (defaultHostStatic.equals(host)||
                    defaultHostUser.equals(host));
    }

    public final static String Password = "Password";

    public final static String GetPassword(){
        return Instance.get(Password,null);
    }
    public final static void SetPassword(String m){
        if (null != m){
            m = m.trim();
            if (0 < m.length())
                Instance.put(Password,m);
        }
    }

    public final static String Resource = "Resource";

    private volatile static String ResourceValue ;

    public final static String GetResource(){
        return Instance.get(Resource,XAddress.Default.Resource);
    }
    public final static String ComposeResource(){
        return (Preferences.ResourceValue = (Preferences.GetResource()+'.'+Preferences.GetSession()));
    }
    public final static void SetResource(String m){
        if (null != m){
            m = m.trim();
            if (0 < m.length()){
                Preferences.ResourceValue = null;
                Instance.put(Resource,m);
            }
        }
    }
    public final static boolean IsResourceDefault(String resource){
        if (null == Preferences.ResourceValue){
            Preferences.ResourceValue = ComposeResource();
        }

        return (Preferences.ResourceValue.equals(resource));
    }

    public final static String Logon = "Logon";

    public final static String GetLogon(){

        return Instance.get(Logon,null);
    }
    public final static XAddress ComposeLogon(){

        return new XAddress.From();
    }
    public final static void SetLogon(String m){
        if (null != m){
            Instance.put(Logon,m);
        }
    }

    public final static String To = "To";

    public final static String GetTo(){

        return Instance.get(To,null);
    }
    public final static String GetToIdentifier(){
        try {
            return (new XAddress.To()).identifier;
        }
        catch (IllegalArgumentException notFound){
            return null;
        }
    }
    public final static String GetToLogon(){
        try {
            return (new XAddress.To()).logon;
        }
        catch (IllegalArgumentException notFound){
            return null;
        }
    }
    public final static XAddress ComposeTo(){

        return new XAddress.To();
    }
    public final static void SetTo(String m){
        if (null != m){
            Instance.put(To,m);
        }
    }
    public final static void SetTo(XAddress m){
        if (null != m){
            Instance.put(To,m.full);
        }
    }

    public final static String Port = "Port";

    public final static int GetPort(){
        return Instance.getInt(Port,XAddress.Default.Port);
    }
    public final static void SetPort(int m){
        if (0 < m){
            Instance.putInt(Port,m);
        }
    }
    public final static void SetPort(String m){
        if (null != m){
            try {
                SetPort(Integer.parseInt(m));
            }
            catch (NumberFormatException exc){
            }
        }
    }

    public final static class TemporaryStorage
        extends java.util.prefs.Preferences
    {

        private final java.util.Map<String,Object> storage = new java.util.HashMap<String,Object>();


        public TemporaryStorage(){
            super();
        }


        public void put(String key, String value){
            this.storage.put(key,value);
        }
        public String get(String key, String def){
            Object value = this.storage.get(key);
            if (null == value)
                return def;
            else
                return (String)value;
        }
        public void remove(String key){
            this.storage.remove(key);
        }
        public void clear() throws BackingStoreException {
            this.storage.clear();
        }
        public void putInt(String key, int value){
            this.storage.put(key, value);
        }
        public int getInt(String key, int def){
            Object value = this.storage.get(key);
            if (null == value)
                return def;
            else
                return ((Number)value).intValue();
        }
        public void putLong(String key, long value){
            this.storage.put(key, value);
        }
        public long getLong(String key, long def){
            Object value = this.storage.get(key);
            if (null == value)
                return def;
            else
                return ((Number)value).longValue();
        }
        public void putBoolean(String key, boolean value){
            this.storage.put(key, value);
        }
        public boolean getBoolean(String key, boolean def){
            Object value = this.storage.get(key);
            if (null == value)
                return def;
            else
                return ((Boolean)value).booleanValue();
        }
        public void putFloat(String key, float value){
            this.storage.put(key, value);
        }
        public float getFloat(String key, float def){
            Object value = this.storage.get(key);
            if (null == value)
                return def;
            else
                return ((Number)value).floatValue();
        }
        public void putDouble(String key, double value){
            this.storage.put(key, value);
        }
        public double getDouble(String key, double def){
            Object value = this.storage.get(key);
            if (null == value)
                return def;
            else
                return ((Number)value).doubleValue();
        }
        public void putByteArray(String key, byte[] value){
            this.storage.put(key, value);
        }
        public byte[] getByteArray(String key, byte[] def){
            Object value = this.storage.get(key);
            if (null == value)
                return def;
            else
                return (byte[])value;
        }
        public String[] keys()
            throws BackingStoreException
        {
            return (String[])this.storage.keySet().toArray(new String[]{});
        }
        public String[] childrenNames()
            throws BackingStoreException
        {
            throw new UnsupportedOperationException();
        }
        public java.util.prefs.Preferences parent(){
            throw new UnsupportedOperationException();
        }
        public java.util.prefs.Preferences node(String pathName){
            throw new UnsupportedOperationException();
        }
        public boolean nodeExists(String pathName)
            throws BackingStoreException
        {
            throw new UnsupportedOperationException();
        }
        public void removeNode()
            throws BackingStoreException
        {
            throw new UnsupportedOperationException();
        }
        public String name(){
            throw new UnsupportedOperationException();
        }
        public String absolutePath(){
            throw new UnsupportedOperationException();
        }
        public boolean isUserNode(){
            throw new UnsupportedOperationException();
        }
        public String toString(){
            throw new UnsupportedOperationException();
        }
        public void flush()
            throws BackingStoreException
        {
            throw new UnsupportedOperationException();
        }
        public void sync()
            throws BackingStoreException
        {
            throw new UnsupportedOperationException();
        }
        public void addPreferenceChangeListener(PreferenceChangeListener pcl){
            throw new UnsupportedOperationException();
        }
        public void removePreferenceChangeListener(PreferenceChangeListener pcl){
            throw new UnsupportedOperationException();
        }
        public void addNodeChangeListener(NodeChangeListener ncl){
            throw new UnsupportedOperationException();
        }
        public void removeNodeChangeListener(NodeChangeListener ncl){
            throw new UnsupportedOperationException();
        }
        public void exportNode(OutputStream os)
            throws IOException, BackingStoreException
        {
            throw new UnsupportedOperationException();
        }
        public void exportSubtree(OutputStream os)
            throws IOException, BackingStoreException
        {
            throw new UnsupportedOperationException();
        }
    }


    private final java.util.prefs.Preferences storage;


    public Preferences(){
        super();
        java.util.prefs.Preferences storage;
        try {
            storage = java.util.prefs.Preferences.userNodeForPackage(Preferences.class);
        }
        catch (SecurityException sec){

            storage = new Preferences.TemporaryStorage();
        }
        this.storage = storage;
    }


    public void put(String key, String value){
        this.storage.put(key,value);
    }
    public String get(String key, String def){
        return this.storage.get(key,def);
    }
    public void remove(String key){
        this.storage.remove(key);
    }
    public void clear() throws BackingStoreException {
        this.storage.clear();
    }
    public void putInt(String key, int value){
        this.storage.putInt(key, value);
    }
    public int getInt(String key, int def){
        return this.storage.getInt(key, def);
    }
    public void putLong(String key, long value){
        this.storage.putLong(key, value);
    }
    public long getLong(String key, long def){
        return this.storage.getLong(key, def);
    }
    public void putBoolean(String key, boolean value){
        this.storage.putBoolean(key, value);
    }
    public boolean getBoolean(String key, boolean def){
        return this.storage.getBoolean(key, def);
    }
    public void putFloat(String key, float value){
        this.storage.putFloat(key, value);
    }
    public float getFloat(String key, float def){
        return this.storage.getFloat(key, def);
    }
    public void putDouble(String key, double value){
        this.storage.putDouble(key, value);
    }
    public double getDouble(String key, double def){
        return this.storage.getDouble(key, def);
    }
    public void putByteArray(String key, byte[] value){
        this.storage.putByteArray(key, value);
    }
    public byte[] getByteArray(String key, byte[] def){
        return this.storage.getByteArray(key, def);
    }
    public String[] keys()
        throws BackingStoreException
    {
        return this.storage.keys();
    }
    public String[] childrenNames()
        throws BackingStoreException
    {
        throw new UnsupportedOperationException();
    }
    public java.util.prefs.Preferences parent(){
        throw new UnsupportedOperationException();
    }
    public java.util.prefs.Preferences node(String pathName){
        throw new UnsupportedOperationException();
    }
    public boolean nodeExists(String pathName)
        throws BackingStoreException
    {
        throw new UnsupportedOperationException();
    }
    public void removeNode()
        throws BackingStoreException
    {
        throw new UnsupportedOperationException();
    }
    public String name(){
        throw new UnsupportedOperationException();
    }
    public String absolutePath(){
        throw new UnsupportedOperationException();
    }
    public boolean isUserNode(){
        throw new UnsupportedOperationException();
    }
    public String toString(){
        throw new UnsupportedOperationException();
    }
    public void flush()
        throws BackingStoreException
    {
        throw new UnsupportedOperationException();
    }
    public void sync()
        throws BackingStoreException
    {
        throw new UnsupportedOperationException();
    }
    public void addPreferenceChangeListener(PreferenceChangeListener pcl){
        throw new UnsupportedOperationException();
    }
    public void removePreferenceChangeListener(PreferenceChangeListener pcl){
        throw new UnsupportedOperationException();
    }
    public void addNodeChangeListener(NodeChangeListener ncl){
        throw new UnsupportedOperationException();
    }
    public void removeNodeChangeListener(NodeChangeListener ncl){
        throw new UnsupportedOperationException();
    }
    public void exportNode(OutputStream os)
        throws IOException, BackingStoreException
    {
        throw new UnsupportedOperationException();
    }
    public void exportSubtree(OutputStream os)
        throws IOException, BackingStoreException
    {
        throw new UnsupportedOperationException();
    }
}
