package org.nakedobjects.viewer.skylark.basic;

import org.nakedobjects.object.NakedClass;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.viewer.skylark.Canvas;
import org.nakedobjects.viewer.skylark.Color;
import org.nakedobjects.viewer.skylark.Picture;
import org.nakedobjects.viewer.skylark.ObjectContent;
import org.nakedobjects.viewer.skylark.Size;
import org.nakedobjects.viewer.skylark.Text;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.core.AbstractView;
import org.nakedobjects.viewer.skylark.util.PictureFactory;


/**
 *  
 */
public class IconGraphic {
    private ObjectContent content;
    private int iconHeight;
    private Picture icon;

    public IconGraphic(View view, int height) {
        content = (ObjectContent) view.getContent();
        iconHeight = height;
    }

    public IconGraphic(View view, Text style) {
        this(view, (style.getHeight() * 120) / 100);
    }

    public void draw(Canvas canvas, int x, int baseline) {
        Picture icon = icon();

        int xi = x + View.HPADDING;
        // TODO move down toward baseline
        int yi = baseline - getBaseline() + View.VPADDING;
        canvas.drawIcon(icon, xi, yi);

        if (AbstractView.DEBUG) {
            Size size = getSize();
            canvas.drawRectangle(x, baseline - getBaseline(), size.getWidth() - 1, size.getHeight() - 1, Color.DEBUG3);
            canvas.drawLine(0, getBaseline(), size.getWidth(), getBaseline(), Color.DEBUG3);
        }
    }

    public int getBaseline() {
        return iconHeight - 4;
    }

    protected ObjectContent getContent() {
        return content;
    }

    public Size getSize() {
        int height = View.VPADDING + iconHeight + View.VPADDING;

        Picture icon = icon();

        int iconWidth;
        iconWidth = icon.getWidth();

        int width = View.HPADDING + iconWidth + View.HPADDING;

        return new Size(width, height);
    }

    private Picture icon() {
        final NakedObject object = content.getObject();
        final String iconName = iconName(object);

        /*
         * If the grpahic based on a name provided by the object then the icon
         * could be changed at any time, so we won't lazily load it.
         */
        if (iconName == null && icon != null) {
            return icon;
        }

        if (iconName != null) {
            final Picture loadIcon = loadIcon(iconName);
            if (loadIcon != null) {
                icon = loadIcon;
                return loadIcon;
            }
        }

        icon = iconPicture(object);
        return icon;
    }

    protected Picture loadIcon(final NakedObjectSpecification specification, final String type) {
        String className = specification.getFullName().replace('.', '_') + type;
        Picture loadIcon = loadIcon(className);
        if (loadIcon == null) {
            className = specification.getShortName();
            loadIcon = loadIcon(className);
            if (loadIcon == null) {
                NakedObjectSpecification superclass = specification.superclass();
                if (superclass == null) {
                    return loadUnknownIcon();
                }
                return loadIcon(superclass, type);
            }
        }

        return loadIcon;
    }

    private Picture loadUnknownIcon() {
        return PictureFactory.getInstance().loadUnknownIcon(iconHeight, null);
    }

    private Picture loadIcon(final String iconName) {
        return PictureFactory.getInstance().loadIcon(iconName, iconHeight, null);
    }

    /**
     * The icon name is used in preference to the picture returned by the
     * iconPicture method. Return null to use the iconPicture version. This
     * particular version of the method provide a default implementation that
     * asks the object for an icon name.
     * 
     * @see NakedObject#getIconName()
     */
    protected String iconName(final NakedObject object) {
        return object.getIconName();
    }

    /**
     * If there is no icon name specified by the iconName method (i.e., it is
     * null) then this method can provide a picture to use. As a default this
     * particular method works upward through the class hierarchy until it finds
     * an image.
     * 
     * @see #iconName(NakedObject)
     */
    protected Picture iconPicture(final NakedObject object) {
        // work through class, and superclass, names of the object
        NakedObjectSpecification specification = object instanceof NakedClass ? ((NakedClass) object).forNakedClass() : object.getSpecification();
        return loadIcon(specification, "");
    }
}

/*
 * Naked Objects - a framework that exposes behaviourally complete business
 * objects directly to the user. Copyright (C) 2000 - 2004 Naked Objects Group
 * Ltd
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * The authors can be contacted via www.nakedobjects.org (the registered address
 * of Naked Objects Group is Kingsway House, 123 Goldworth Road, Woking GU21
 * 1NR, UK).
 */
