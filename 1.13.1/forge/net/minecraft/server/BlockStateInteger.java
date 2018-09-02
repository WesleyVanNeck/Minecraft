package net.minecraft.server;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public class BlockStateInteger extends BlockState<Integer> {
    private final ImmutableSet<Integer> a;

    protected BlockStateInteger(String s, int i, int j) {
        super(s, Integer.class);
        if (i < 0) {
            throw new IllegalArgumentException("Min value of " + s + " must be 0 or greater");
        } else if (j <= i) {
            throw new IllegalArgumentException("Max value of " + s + " must be greater than min (" + i + ")");
        } else {
            HashSet hashset = Sets.newHashSet();

            for(int k = i; k <= j; ++k) {
                hashset.add(k);
            }

            this.a = ImmutableSet.copyOf(hashset);
        }
    }

    public Collection<Integer> d() {
        return this.a;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof BlockStateInteger && super.equals(object)) {
            BlockStateInteger blockstateinteger1 = (BlockStateInteger)object;
            return this.a.equals(blockstateinteger1.a);
        } else {
            return false;
        }
    }

    public int c() {
        return 31 * super.c() + this.a.hashCode();
    }

    public static BlockStateInteger of(String s, int i, int j) {
        return new BlockStateInteger(s, i, j);
    }

    public Optional<Integer> b(String s) {
        try {
            Integer integer = Integer.valueOf(s);
            return this.a.contains(integer) ? Optional.of(integer) : Optional.empty();
        } catch (NumberFormatException var3) {
            return Optional.empty();
        }
    }

    public String a(Integer integer) {
        return integer.toString();
    }
}