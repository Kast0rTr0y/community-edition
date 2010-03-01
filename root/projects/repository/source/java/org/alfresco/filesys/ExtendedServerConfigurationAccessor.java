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
package org.alfresco.filesys;

import org.alfresco.jlan.server.config.ServerConfigurationAccessor;

/**
 * An interface exposing some extended capabilities of the AbstractServerConfigurationBean.
 * 
 * @author dward
 */
public interface ExtendedServerConfigurationAccessor extends ServerConfigurationAccessor
{

    /**
     * Get the local server name and optionally trim the domain name
     * 
     * @param trimDomain
     *            boolean
     * @return String
     */
    public String getLocalServerName(boolean trimDomain);

    /**
     * Get the local domain/workgroup name
     * 
     * @return String
     */
    public String getLocalDomainName();
}
