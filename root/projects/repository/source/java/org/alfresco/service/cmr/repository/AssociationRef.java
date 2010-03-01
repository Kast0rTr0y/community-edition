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
package org.alfresco.service.cmr.repository;

import java.io.Serializable;
import java.util.StringTokenizer;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.service.namespace.QName;
import org.alfresco.util.EqualsHelper;

/**
 * This class represents a regular, named node relationship between two nodes.
 * 
 * @author Derek Hulley
 */
public class AssociationRef implements EntityRef, Serializable
{
    private static final long serialVersionUID = 3977867284482439475L;
    
    private static final String FILLER = "|";

    private Long id;
    private NodeRef sourceRef;
    private QName assocTypeQName;
    private NodeRef targetRef;

    /**
     * Construct a representation of a source --- name ----> target
     * relationship.
     *
     * @param id
     *            unique identifier
     * @param sourceRef
     *            the source reference - never null
     * @param assocTypeQName
     *            the qualified name of the association type - never null
     * @param targetRef
     *            the target node reference - never null.
     */
    public AssociationRef(Long id, NodeRef sourceRef, QName assocTypeQName, NodeRef targetRef)
    {
        this.id = id;
        this.sourceRef = sourceRef;
        this.assocTypeQName = assocTypeQName;
        this.targetRef = targetRef;

        // check
        if (sourceRef == null)
        {
            throw new IllegalArgumentException("Source reference may not be null");
        }
        if (assocTypeQName == null)
        {
            throw new IllegalArgumentException("QName may not be null");
        }
        if (targetRef == null)
        {
            throw new IllegalArgumentException("Target reference may not be null");
        }
    }
    
    /**
     * @param childAssocRefStr a string of the form <b>sourceNodeRef|targetNodeRef|assocTypeQName</b>
     */
    public AssociationRef(String assocRefStr)
    {
        StringTokenizer tokenizer = new StringTokenizer(assocRefStr, FILLER);
        if (tokenizer.countTokens() != 3)
        {
            throw new AlfrescoRuntimeException("Unable to parse association string: " + assocRefStr);
        }
        String idStr = tokenizer.nextToken();
        String sourceNodeRefStr = tokenizer.nextToken();
        String targetNodeRefStr = tokenizer.nextToken();
        String assocTypeQNameStr = tokenizer.nextToken();
        
        this.id = new Long(idStr);
        this.sourceRef = new NodeRef(sourceNodeRefStr);
        this.targetRef = new NodeRef(targetNodeRefStr);
        this.assocTypeQName = QName.createQName(assocTypeQNameStr);
    }

    /**
     * @return Returns a string of the form <b>sourceNodeRef|targetNodeRef|assocTypeQName|assocQName</b>
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder(180);
        sb.append(id).append(FILLER)
          .append(sourceRef).append(FILLER)
          .append(targetRef).append(FILLER)
          .append(assocTypeQName);
        return sb.toString();
    }

    /**
     * Compares:
     * <ul>
     * <li>{@link #id}</li>
     * <li>{@link #sourceRef}</li>
     * <li>{@link #targetRef}</li>
     * <li>{@link #assocTypeQName}</li>
     * </ul>
     */
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof AssociationRef))
        {
            return false;
        }
        AssociationRef other = (AssociationRef) o;

        return (EqualsHelper.nullSafeEquals(this.id, other.id)
                && EqualsHelper.nullSafeEquals(this.sourceRef, other.sourceRef)
                && EqualsHelper.nullSafeEquals(this.assocTypeQName, other.assocTypeQName)
                && EqualsHelper.nullSafeEquals(this.targetRef, other.targetRef));
    }

    public int hashCode()
    {
        int hashCode = (getId() == null) ? 0 : getId().hashCode();
        hashCode = 37 * hashCode + ((getSourceRef() == null) ? 0 : getSourceRef().hashCode());
        hashCode = 37 * hashCode + ((getTypeQName() == null) ? 0 : getTypeQName().hashCode());
        hashCode = 37 * hashCode + getTargetRef().hashCode();
        return hashCode;
    }

    /**
     * Gets the unique identifier for this association.
     * 
     * @return the unique identifier for this association
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * Get the qualified name of the source-target association
     * 
     * @return Returns the qualified name of the source-target association.
     */
    public QName getTypeQName()
    {
        return assocTypeQName;
    }

    /**
     * @return Returns the child node reference - never null
     */
    public NodeRef getTargetRef()
    {
        return targetRef;
    }

    /**
     * @return Returns the parent node reference, which may be null if this
     *         represents the imaginary reference to the root node
     */
    public NodeRef getSourceRef()
    {
        return sourceRef;
    }
}
