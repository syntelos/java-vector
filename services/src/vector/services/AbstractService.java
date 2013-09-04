/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2013, The DigiVac Company
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
package vector.services;

import vector.data.DataMessage;

/**
 * 
 */
public abstract class AbstractService
    extends services.Classes
{
    /**
     * Overloaded interface proves and runs subclasses
     */
    public interface Service {

        public DataMessage[] evaluate(Object... argv);
    }

    protected final lxl.List<AbstractService.Service> services = new lxl.ArrayList();


    protected AbstractService(Class jclass){
        super(jclass);
    }



    public DataMessage[] evaluate(Object... argv){

        /*
         * Services evaluate input as a set of effects
         */
        DataMessage[] re = null;

        for (AbstractService.Service service: this.services){
            try {
                DataMessage[] r0 = service.evaluate(argv);

                re = DataMessage.Add(re,r0);
            }
            catch (RuntimeException rex){

                re = DataMessage.Add(re,new DataMessage.Error("# "+rex.getMessage()));
            }
        }
        return re;
    }
}
