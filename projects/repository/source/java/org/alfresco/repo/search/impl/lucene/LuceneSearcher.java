/*
 * Copyright (C) 2005-2010 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.repo.search.impl.lucene;

import java.util.List;

import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.NamespacePrefixResolver;
import org.alfresco.util.Pair;

/**
 * Lucene implementation specific entension to the seracher API
 * @author andyh
 *
 */
public interface LuceneSearcher extends SearchService
{
    /**
     * Check if the index exists 
     * @return - true if it exists
     */
   public boolean indexExists();
   /**
    * Ste the node service
    * @param nodeService NodeService
    */
   public void setNodeService(NodeService nodeService);
   /**
    * Set the name space service
    * @param namespacePrefixResolver NamespacePrefixResolver
    */
   public void setNamespacePrefixResolver(NamespacePrefixResolver namespacePrefixResolver);
   
   /**
    * Get top terms
    * 
    * @param field String
    * @param count int
    * @return List
    */
   public List<Pair<String, Integer>> getTopTerms(String field, int count);
   
   /**
    * Get a lucene searcher 
    * @return ClosingIndexSearcher
    */
   public ClosingIndexSearcher getClosingIndexSearcher();
}
