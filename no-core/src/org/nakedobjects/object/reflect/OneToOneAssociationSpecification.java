package org.nakedobjects.object.reflect;

import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.control.About;
import org.nakedobjects.object.security.Session;


public class OneToOneAssociationSpecification extends AssociationSpecification {
	private final OneToOneAssociation delegatedTo;
	
    public OneToOneAssociationSpecification(String name, NakedObjectSpecification type, OneToOneAssociation association) {
        super(name, type);
        this.delegatedTo = association;
    }
    
    public boolean canAccess(Session session, NakedObject object) {
    	return getAbout(session, object, null).canAccess().isAllowed();
    }
    
    public boolean canUse(Session session, NakedObject object) {
    	return getAbout(session, object, null).canUse().isAllowed();
    }
    
    public void clear(NakedObject inObject) {
    	Naked associate = get(inObject);
    	if(associate != null) {
    		clearAssociation(inObject, (NakedObject) associate);
    	}
    }

    public void clearAssociation(NakedObject inObject, NakedObject associate) {
        if (associate == null) {
            throw new NullPointerException("Must specify the item to remove/dissociate");
        }
    	delegatedTo.clearAssociation(inObject, associate);
    }

	public Naked get(NakedObject fromObject) {
		return delegatedTo.getAssociation(fromObject);
	}
    
    public About getAbout(Session session, NakedObject object, NakedObject value) {
    	return delegatedTo.getAbout(session, object, value);
    }

    public String getLabel(Session session, NakedObject object) {
      	About about = getAbout(session, object, (NakedObject) get(object));

        return getLabel(about);
    }

	public boolean hasAbout() {
		return delegatedTo.hasAbout();
	}

    public boolean hasAddMethod() {
        return delegatedTo.hasAddMethod();
    }

	public void initData(NakedObject inObject, Object associate) {
    	delegatedTo.initData(inObject, associate);
	}

	public boolean isDerived() {
		return delegatedTo.isDerived();
	}

  public void setAssociation(NakedObject inObject, NakedObject associate) {
        if (associate == null) {
            throw new NullPointerException("Must specify an object to be associated");
        }

    	delegatedTo.setAssociation(inObject, associate);
    }
	
    public String toString() {
        return "OneToOne [" + super.toString() + "]";
    }
}

/*
Naked Objects - a framework that exposes behaviourally complete
business objects directly to the user.
Copyright (C) 2000 - 2003  Naked Objects Group Ltd

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

The authors can be contacted via www.nakedobjects.org (the
registered address of Naked Objects Group is Kingsway House, 123 Goldworth
Road, Woking GU21 1NR, UK).
*/