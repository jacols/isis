package org.nakedobjects.persistence.cache;

import org.nakedobjects.object.LoadedObjects;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectContext;
import org.nakedobjects.object.NakedObjectRuntimeException;
import org.nakedobjects.object.io.Memento;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Category;


class Instances {
    private final static Category LOG = Category.getInstance(Instances.class);
    private NakedObjectSpecification cls;
    private Hashtable index = new Hashtable();
    private LoadedObjects loadedObjects;
    private Vector orderedInstances = new Vector();

    public Instances(NakedObjectSpecification cls, LoadedObjects loadedObjects) {
        if(cls == null || loadedObjects == null) {
            throw new NullPointerException();
        }
        this.cls = cls;
        this.loadedObjects = loadedObjects;
    }

    public NakedObjectSpecification getNakedClass() {
        return cls;
    }
    
    public void create(NakedObject object) {
        orderedInstances.addElement(object);
        index.put(object.getOid(), object);
    }

    public Enumeration instances() {
        return orderedInstances.elements();
    }

    int loadData(ObjectInputStream oos, NakedObjectContext context) throws IOException, ClassNotFoundException {
        int noInstances = oos.readInt();
        int size = 0;
        for (int i = 0; i < noInstances; i++) {
            Memento memento = (Memento) oos.readObject();
            LOG.debug("read 2: " + i + " " + memento);

            NakedObject object = loadedObjects.getLoadedObject(memento.getOid());
            memento.updateNakedObject(object, loadedObjects, context);
            LOG.debug("recreated " + object + " " + object.title());
            size++;
        }
        return size;
    }

    void loadIdentities(ObjectInputStream oos) throws IOException, ClassNotFoundException {
        int noInstances = oos.readInt();
        for (int i = 0; i < noInstances; i++) {
            Object oid = oos.readObject();
            LOG.debug("read 1: " + i + " " + cls.getFullName() + "/" + oid);

            NakedObject obj = (NakedObject) cls.acquireInstance();
            obj.setOid(oid);
            preload(oid, obj);
            loadedObjects.loaded(obj);
        }
    }

    public int numberInstances() {
        return orderedInstances.size();
    }

    private void preload(Object oid, NakedObject object) {
        orderedInstances.addElement(object);
        index.put(oid, object);
    }

    public NakedObject read(Object oid) {
        NakedObject object = (NakedObject) index.get(oid);
        if (object == null) { throw new NakedObjectRuntimeException("No object for " + oid); }
        return object;
    }

    public void remove(NakedObject object) {
        orderedInstances.remove(object);
        index.remove(object.getOid());
    }

    long saveData(ObjectOutputStream oos) throws IOException {
        oos.writeInt(numberInstances());

        Enumeration e = instances();
        int i = 0;
        while (e.hasMoreElements()) {
            NakedObject object = (NakedObject) e.nextElement();
            Memento memento = new Memento(object);
            LOG.debug("write 2: " + i++ + " " + cls.getFullName() + "/" + memento);
            oos.writeObject(memento);
        }
        return i;
    }

    void saveIdentities(ObjectOutputStream oos) throws IOException {
        oos.writeInt(numberInstances());

        Enumeration e = instances();
        int i = 0;
        while (e.hasMoreElements()) {
            NakedObject object = (NakedObject) e.nextElement();
            Object oid = object.getOid();
            oos.writeObject(oid);
            LOG.debug("write 1: " + i++ + " " + cls.getFullName() + "/" + oid);
        }
    }
}

/*
 * Naked Objects - a framework that exposes behaviourally complete business objects directly to the
 * user. Copyright (C) 2000 - 2004 Naked Objects Group Ltd
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 * 
 * The authors can be contacted via www.nakedobjects.org (the registered address of Naked Objects
 * Group is Kingsway House, 123 Goldworth Road, Woking GU21 1NR, UK).
 */