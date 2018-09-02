package net.minecraft.server;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NBTTagCompound implements NBTBase {
    private static final Logger f = LogManager.getLogger();
    private static final Pattern g = Pattern.compile("[A-Za-z0-9._+-]+");
    private final Map<String, NBTBase> map = Maps.newHashMap();

    public NBTTagCompound() {
    }

    public void write(DataOutput dataoutput) throws IOException {
        for(String s : this.map.keySet()) {
            NBTBase nbtbase = (NBTBase)this.map.get(s);
            a(s, nbtbase, dataoutput);
        }

        dataoutput.writeByte(0);
    }

    public void load(DataInput datainput, int i, NBTReadLimiter nbtreadlimiter) throws IOException {
        nbtreadlimiter.a(384L);
        if (i > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        } else {
            this.map.clear();

            byte b0;
            while((b0 = a(datainput, nbtreadlimiter)) != 0) {
                String s = b(datainput, nbtreadlimiter);
                nbtreadlimiter.a((long)(224 + 16 * s.length()));
                NBTBase nbtbase = a(b0, s, datainput, i + 1, nbtreadlimiter);
                if (this.map.put(s, nbtbase) != null) {
                    nbtreadlimiter.a(288L);
                }
            }

        }
    }

    public Set<String> getKeys() {
        return this.map.keySet();
    }

    public byte getTypeId() {
        return 10;
    }

    public int d() {
        return this.map.size();
    }

    public void set(String s, NBTBase nbtbase) {
        this.map.put(s, nbtbase);
    }

    public void setByte(String s, byte b0) {
        this.map.put(s, new NBTTagByte(b0));
    }

    public void setShort(String s, short short1) {
        this.map.put(s, new NBTTagShort(short1));
    }

    public void setInt(String s, int i) {
        this.map.put(s, new NBTTagInt(i));
    }

    public void setLong(String s, long i) {
        this.map.put(s, new NBTTagLong(i));
    }

    public void a(String s, UUID uuid) {
        this.setLong(s + "Most", uuid.getMostSignificantBits());
        this.setLong(s + "Least", uuid.getLeastSignificantBits());
    }

    @Nullable
    public UUID a(String s) {
        return new UUID(this.getLong(s + "Most"), this.getLong(s + "Least"));
    }

    public boolean b(String s) {
        return this.hasKeyOfType(s + "Most", 99) && this.hasKeyOfType(s + "Least", 99);
    }

    public void setFloat(String s, float fx) {
        this.map.put(s, new NBTTagFloat(fx));
    }

    public void setDouble(String s, double d0) {
        this.map.put(s, new NBTTagDouble(d0));
    }

    public void setString(String s, String s1) {
        this.map.put(s, new NBTTagString(s1));
    }

    public void setByteArray(String s, byte[] abyte) {
        this.map.put(s, new NBTTagByteArray(abyte));
    }

    public void setIntArray(String s, int[] aint) {
        this.map.put(s, new NBTTagIntArray(aint));
    }

    public void b(String s, List<Integer> list) {
        this.map.put(s, new NBTTagIntArray(list));
    }

    public void a(String s, long[] along) {
        this.map.put(s, new NBTTagLongArray(along));
    }

    public void c(String s, List<Long> list) {
        this.map.put(s, new NBTTagLongArray(list));
    }

    public void setBoolean(String s, boolean flag) {
        this.setByte(s, (byte)(flag ? 1 : 0));
    }

    public NBTBase get(String s) {
        return (NBTBase)this.map.get(s);
    }

    public byte d(String s) {
        NBTBase nbtbase = (NBTBase)this.map.get(s);
        return nbtbase == null ? 0 : nbtbase.getTypeId();
    }

    public boolean hasKey(String s) {
        return this.map.containsKey(s);
    }

    public boolean hasKeyOfType(String s, int i) {
        byte b0 = this.d(s);
        if (b0 == i) {
            return true;
        } else if (i != 99) {
            return false;
        } else {
            return b0 == 1 || b0 == 2 || b0 == 3 || b0 == 4 || b0 == 5 || b0 == 6;
        }
    }

    public byte getByte(String s) {
        try {
            if (this.hasKeyOfType(s, 99)) {
                return ((NBTNumber)this.map.get(s)).g();
            }
        } catch (ClassCastException var3) {
            ;
        }

        return 0;
    }

    public short getShort(String s) {
        try {
            if (this.hasKeyOfType(s, 99)) {
                return ((NBTNumber)this.map.get(s)).f();
            }
        } catch (ClassCastException var3) {
            ;
        }

        return 0;
    }

    public int getInt(String s) {
        try {
            if (this.hasKeyOfType(s, 99)) {
                return ((NBTNumber)this.map.get(s)).e();
            }
        } catch (ClassCastException var3) {
            ;
        }

        return 0;
    }

    public long getLong(String s) {
        try {
            if (this.hasKeyOfType(s, 99)) {
                return ((NBTNumber)this.map.get(s)).d();
            }
        } catch (ClassCastException var3) {
            ;
        }

        return 0L;
    }

    public float getFloat(String s) {
        try {
            if (this.hasKeyOfType(s, 99)) {
                return ((NBTNumber)this.map.get(s)).i();
            }
        } catch (ClassCastException var3) {
            ;
        }

        return 0.0F;
    }

    public double getDouble(String s) {
        try {
            if (this.hasKeyOfType(s, 99)) {
                return ((NBTNumber)this.map.get(s)).asDouble();
            }
        } catch (ClassCastException var3) {
            ;
        }

        return 0.0D;
    }

    public String getString(String s) {
        try {
            if (this.hasKeyOfType(s, 8)) {
                return ((NBTBase)this.map.get(s)).b_();
            }
        } catch (ClassCastException var3) {
            ;
        }

        return "";
    }

    public byte[] getByteArray(String s) {
        try {
            if (this.hasKeyOfType(s, 7)) {
                return ((NBTTagByteArray)this.map.get(s)).c();
            }
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, 7, classcastexception));
        }

        return new byte[0];
    }

    public int[] getIntArray(String s) {
        try {
            if (this.hasKeyOfType(s, 11)) {
                return ((NBTTagIntArray)this.map.get(s)).d();
            }
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, 11, classcastexception));
        }

        return new int[0];
    }

    public long[] o(String s) {
        try {
            if (this.hasKeyOfType(s, 12)) {
                return ((NBTTagLongArray)this.map.get(s)).d();
            }
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, 12, classcastexception));
        }

        return new long[0];
    }

    public NBTTagCompound getCompound(String s) {
        try {
            if (this.hasKeyOfType(s, 10)) {
                return (NBTTagCompound)this.map.get(s);
            }
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, 10, classcastexception));
        }

        return new NBTTagCompound();
    }

    public NBTTagList getList(String s, int i) {
        try {
            if (this.d(s) == 9) {
                NBTTagList nbttaglist = (NBTTagList)this.map.get(s);
                if (!nbttaglist.isEmpty() && nbttaglist.d() != i) {
                    return new NBTTagList();
                }

                return nbttaglist;
            }
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.a(s, 9, classcastexception));
        }

        return new NBTTagList();
    }

    public boolean getBoolean(String s) {
        return this.getByte(s) != 0;
    }

    public void remove(String s) {
        this.map.remove(s);
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("{");
        Object object = this.map.keySet();
        if (f.isDebugEnabled()) {
            ArrayList arraylist = Lists.newArrayList(this.map.keySet());
            Collections.sort(arraylist);
            object = arraylist;
        }

        for(String s : object) {
            if (stringbuilder.length() != 1) {
                stringbuilder.append(',');
            }

            stringbuilder.append(s(s)).append(':').append(this.map.get(s));
        }

        return stringbuilder.append('}').toString();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    private CrashReport a(String s, int i, ClassCastException classcastexception) {
        CrashReport crashreport = CrashReport.a(classcastexception, "Reading NBT data");
        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Corrupt NBT tag", 1);
        crashreportsystemdetails.a("Tag type found", () -> {
            return a[((NBTBase)this.map.get(s)).getTypeId()];
        });
        crashreportsystemdetails.a("Tag type expected", () -> {
            return a[i];
        });
        crashreportsystemdetails.a("Tag name", s);
        return crashreport;
    }

    public NBTTagCompound clone() {
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();

        for(String s : this.map.keySet()) {
            nbttagcompound1.set(s, ((NBTBase)this.map.get(s)).clone());
        }

        return nbttagcompound1;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else {
            return object instanceof NBTTagCompound && Objects.equals(this.map, ((NBTTagCompound)object).map);
        }
    }

    public int hashCode() {
        return this.map.hashCode();
    }

    private static void a(String s, NBTBase nbtbase, DataOutput dataoutput) throws IOException {
        dataoutput.writeByte(nbtbase.getTypeId());
        if (nbtbase.getTypeId() != 0) {
            dataoutput.writeUTF(s);
            nbtbase.write(dataoutput);
        }
    }

    private static byte a(DataInput datainput, NBTReadLimiter var1) throws IOException {
        return datainput.readByte();
    }

    private static String b(DataInput datainput, NBTReadLimiter var1) throws IOException {
        return datainput.readUTF();
    }

    static NBTBase a(byte b0, String s, DataInput datainput, int i, NBTReadLimiter nbtreadlimiter) throws IOException {
        NBTBase nbtbase = NBTBase.createTag(b0);

        try {
            nbtbase.load(datainput, i, nbtreadlimiter);
            return nbtbase;
        } catch (IOException ioexception) {
            CrashReport crashreport = CrashReport.a(ioexception, "Loading NBT data");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("NBT Tag");
            crashreportsystemdetails.a("Tag name", s);
            crashreportsystemdetails.a("Tag type", b0);
            throw new ReportedException(crashreport);
        }
    }

    public NBTTagCompound a(NBTTagCompound nbttagcompound1) {
        for(String s : nbttagcompound1.map.keySet()) {
            NBTBase nbtbase = (NBTBase)nbttagcompound1.map.get(s);
            if (nbtbase.getTypeId() == 10) {
                if (this.hasKeyOfType(s, 10)) {
                    NBTTagCompound nbttagcompound2 = this.getCompound(s);
                    nbttagcompound2.a((NBTTagCompound)nbtbase);
                } else {
                    this.set(s, nbtbase.clone());
                }
            } else {
                this.set(s, nbtbase.clone());
            }
        }

        return this;
    }

    protected static String s(String s) {
        return g.matcher(s).matches() ? s : NBTTagString.a(s, true);
    }

    protected static IChatBaseComponent t(String s) {
        if (g.matcher(s).matches()) {
            return (new ChatComponentText(s)).a(b);
        } else {
            IChatBaseComponent ichatbasecomponent = (new ChatComponentText(NBTTagString.a(s, false))).a(b);
            return (new ChatComponentText("\"")).addSibling(ichatbasecomponent).a("\"");
        }
    }

    public IChatBaseComponent a(String s, int i) {
        if (this.map.isEmpty()) {
            return new ChatComponentText("{}");
        } else {
            ChatComponentText chatcomponenttext = new ChatComponentText("{");
            Object object = this.map.keySet();
            if (f.isDebugEnabled()) {
                ArrayList arraylist = Lists.newArrayList(this.map.keySet());
                Collections.sort(arraylist);
                object = arraylist;
            }

            if (!s.isEmpty()) {
                chatcomponenttext.a("\n");
            }

            IChatBaseComponent ichatbasecomponent;
            for(Iterator iterator = ((Collection)object).iterator(); iterator.hasNext(); chatcomponenttext.addSibling(ichatbasecomponent)) {
                String s1 = (String)iterator.next();
                ichatbasecomponent = (new ChatComponentText(Strings.repeat(s, i + 1))).addSibling(t(s1)).a(String.valueOf(':')).a(" ").addSibling(((NBTBase)this.map.get(s1)).a(s, i + 1));
                if (iterator.hasNext()) {
                    ichatbasecomponent.a(String.valueOf(',')).a(s.isEmpty() ? " " : "\n");
                }
            }

            if (!s.isEmpty()) {
                chatcomponenttext.a("\n").a(Strings.repeat(s, i));
            }

            chatcomponenttext.a("}");
            return chatcomponenttext;
        }
    }

    // $FF: synthetic method
    public NBTBase clone() {
        return this.clone();
    }
}