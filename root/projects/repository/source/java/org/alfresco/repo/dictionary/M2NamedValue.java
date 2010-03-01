/*
 * Copyright (C) 2005-2010 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.repo.dictionary;

import java.util.List;

/**
 * Definition of a named value that can be used for property injection.
 * 
 * @author Derek Hulley
 */
public class M2NamedValue
{
    private String name;
    private String simpleValue = null;
    private List<String> listValue = null;
    
    /*package*/ M2NamedValue()
    {
    }


    @Override
    public String toString()
    {
        return (name + "=" + (simpleValue == null ? listValue : simpleValue));
    }

    public String getName()
    {
        return name;
    }
    
    /**
     * @return Returns the raw, unconverted value
     */
    public String getSimpleValue()
    {
        return simpleValue;
    }
    
    /**
     * @return Returns the list of raw, unconverted values
     */
    public List<String> getListValue()
    {
        return listValue;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public void setSimpleValue(String simpleValue)
    {
        this.simpleValue = simpleValue;
    }
    
    public void setListValue(List<String> listValue)
    {
        this.listValue = listValue;
    }
    
    public boolean hasSimpleValue()
    {
        return (this.simpleValue != null);
    }
    
    public boolean hasListValue()
    {
        return (this.listValue != null);
    }

}
