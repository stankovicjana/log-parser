/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Pointer
 *  com.sun.jna.ptr.ByReference
 *  java.lang.Object
 *  java.lang.String
 */
package com.sun.jna.ptr;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByReference;

public class IntByReference
extends ByReference {
    public IntByReference() {
        this(0);
    }

    public IntByReference(int value) {
        super(4);
        this.setValue(value);
    }

    public void setValue(int value) {
        this.getPointer().setInt(0L, value);
    }

    public int getValue() {
        return this.getPointer().getInt(0L);
    }

    public String toString() {
        return String.format((String)"int@0x%1$x=0x%2$x (%2$d)", (Object[])new Object[]{Pointer.nativeValue((Pointer)this.getPointer()), this.getValue()});
    }
}
