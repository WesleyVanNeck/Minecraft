package net.minecraft.server;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.Nullable;

public class CommandTeleport {
    public static void a(com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> commanddispatcher) {
        LiteralCommandNode literalcommandnode = commanddispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandDispatcher.a("teleport").requires((commandlistenerwrapper) -> {
            return commandlistenerwrapper.hasPermission(2);
        })).then(((RequiredArgumentBuilder)CommandDispatcher.a("targets", ArgumentEntity.b()).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)CommandDispatcher.a("location", ArgumentVec3.a()).executes((commandcontext) -> {
            return a((CommandListenerWrapper)commandcontext.getSource(), ArgumentEntity.b(commandcontext, "targets"), ((CommandListenerWrapper)commandcontext.getSource()).getWorld(), ArgumentVec3.b(commandcontext, "location"), (IVectorPosition)null, (CommandTeleport.a)null);
        })).then(CommandDispatcher.a("rotation", ArgumentRotation.a()).executes((commandcontext) -> {
            return a((CommandListenerWrapper)commandcontext.getSource(), ArgumentEntity.b(commandcontext, "targets"), ((CommandListenerWrapper)commandcontext.getSource()).getWorld(), ArgumentVec3.b(commandcontext, "location"), ArgumentRotation.a(commandcontext, "rotation"), (CommandTeleport.a)null);
        }))).then(((LiteralArgumentBuilder)CommandDispatcher.a("facing").then(CommandDispatcher.a("entity").then(((RequiredArgumentBuilder)CommandDispatcher.a("facingEntity", ArgumentEntity.a()).executes((commandcontext) -> {
            return a((CommandListenerWrapper)commandcontext.getSource(), ArgumentEntity.b(commandcontext, "targets"), ((CommandListenerWrapper)commandcontext.getSource()).getWorld(), ArgumentVec3.b(commandcontext, "location"), (IVectorPosition)null, new CommandTeleport.a(ArgumentEntity.a(commandcontext, "facingEntity"), ArgumentAnchor.Anchor.FEET));
        })).then(CommandDispatcher.a("facingAnchor", ArgumentAnchor.a()).executes((commandcontext) -> {
            return a((CommandListenerWrapper)commandcontext.getSource(), ArgumentEntity.b(commandcontext, "targets"), ((CommandListenerWrapper)commandcontext.getSource()).getWorld(), ArgumentVec3.b(commandcontext, "location"), (IVectorPosition)null, new CommandTeleport.a(ArgumentEntity.a(commandcontext, "facingEntity"), ArgumentAnchor.a(commandcontext, "facingAnchor")));
        }))))).then(CommandDispatcher.a("facingLocation", ArgumentVec3.a()).executes((commandcontext) -> {
            return a((CommandListenerWrapper)commandcontext.getSource(), ArgumentEntity.b(commandcontext, "targets"), ((CommandListenerWrapper)commandcontext.getSource()).getWorld(), ArgumentVec3.b(commandcontext, "location"), (IVectorPosition)null, new CommandTeleport.a(ArgumentVec3.a(commandcontext, "facingLocation")));
        }))))).then(CommandDispatcher.a("destination", ArgumentEntity.a()).executes((commandcontext) -> {
            return a((CommandListenerWrapper)commandcontext.getSource(), ArgumentEntity.b(commandcontext, "targets"), ArgumentEntity.a(commandcontext, "destination"));
        })))).then(CommandDispatcher.a("location", ArgumentVec3.a()).executes((commandcontext) -> {
            return a((CommandListenerWrapper)commandcontext.getSource(), Collections.singleton(((CommandListenerWrapper)commandcontext.getSource()).g()), ((CommandListenerWrapper)commandcontext.getSource()).getWorld(), ArgumentVec3.b(commandcontext, "location"), VectorPosition.d(), (CommandTeleport.a)null);
        }))).then(CommandDispatcher.a("destination", ArgumentEntity.a()).executes((commandcontext) -> {
            return a((CommandListenerWrapper)commandcontext.getSource(), Collections.singleton(((CommandListenerWrapper)commandcontext.getSource()).g()), ArgumentEntity.a(commandcontext, "destination"));
        })));
        commanddispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandDispatcher.a("tp").requires((commandlistenerwrapper) -> {
            return commandlistenerwrapper.hasPermission(2);
        })).redirect(literalcommandnode));
    }

    private static int a(CommandListenerWrapper commandlistenerwrapper, Collection<? extends Entity> collection, Entity entity) {
        for(Entity entity1 : collection) {
            a(commandlistenerwrapper, entity1, commandlistenerwrapper.getWorld(), entity.locX, entity.locY, entity.locZ, EnumSet.noneOf(PacketPlayOutPosition.EnumPlayerTeleportFlags.class), entity.yaw, entity.pitch, (CommandTeleport.a)null);
        }

        if (collection.size() == 1) {
            commandlistenerwrapper.sendMessage(new ChatMessage("commands.teleport.success.entity.single", new Object[]{((Entity)collection.iterator().next()).getScoreboardDisplayName(), entity.getScoreboardDisplayName()}), true);
        } else {
            commandlistenerwrapper.sendMessage(new ChatMessage("commands.teleport.success.entity.multiple", new Object[]{collection.size(), entity.getScoreboardDisplayName()}), true);
        }

        return collection.size();
    }

    private static int a(CommandListenerWrapper commandlistenerwrapper, Collection<? extends Entity> collection, WorldServer worldserver, IVectorPosition ivectorposition, @Nullable IVectorPosition ivectorposition1, @Nullable CommandTeleport.a commandteleport$a) throws CommandSyntaxException {
        Vec3D vec3d = ivectorposition.a(commandlistenerwrapper);
        Vec2F vec2f = ivectorposition1 == null ? null : ivectorposition1.b(commandlistenerwrapper);
        EnumSet enumset = EnumSet.noneOf(PacketPlayOutPosition.EnumPlayerTeleportFlags.class);
        if (ivectorposition.a()) {
            enumset.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.X);
        }

        if (ivectorposition.b()) {
            enumset.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y);
        }

        if (ivectorposition.c()) {
            enumset.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.Z);
        }

        if (ivectorposition1 == null) {
            enumset.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.X_ROT);
            enumset.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y_ROT);
        } else {
            if (ivectorposition1.a()) {
                enumset.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.X_ROT);
            }

            if (ivectorposition1.b()) {
                enumset.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y_ROT);
            }
        }

        for(Entity entity : collection) {
            if (ivectorposition1 == null) {
                a(commandlistenerwrapper, entity, worldserver, vec3d.x, vec3d.y, vec3d.z, enumset, entity.yaw, entity.pitch, commandteleport$a);
            } else {
                a(commandlistenerwrapper, entity, worldserver, vec3d.x, vec3d.y, vec3d.z, enumset, vec2f.j, vec2f.i, commandteleport$a);
            }
        }

        if (collection.size() == 1) {
            commandlistenerwrapper.sendMessage(new ChatMessage("commands.teleport.success.location.single", new Object[]{((Entity)collection.iterator().next()).getScoreboardDisplayName(), vec3d.x, vec3d.y, vec3d.z}), true);
        } else {
            commandlistenerwrapper.sendMessage(new ChatMessage("commands.teleport.success.location.multiple", new Object[]{collection.size(), vec3d.x, vec3d.y, vec3d.z}), true);
        }

        return collection.size();
    }

    private static void a(CommandListenerWrapper commandlistenerwrapper, Entity entity, WorldServer worldserver, double d0, double d1, double d2, Set<PacketPlayOutPosition.EnumPlayerTeleportFlags> set, float f, float f1, @Nullable CommandTeleport.a commandteleport$a) {
        if (entity instanceof EntityPlayer) {
            entity.stopRiding();
            if (((EntityPlayer)entity).isSleeping()) {
                ((EntityPlayer)entity).a(true, true, false);
            }

            if (worldserver == entity.world) {
                ((EntityPlayer)entity).playerConnection.a(d0, d1, d2, f, f1, set);
            } else {
                ((EntityPlayer)entity).a(worldserver, d0, d1, d2, f, f1);
            }

            entity.setHeadRotation(f);
        } else {
            float f2 = MathHelper.g(f);
            float f3 = MathHelper.g(f1);
            f3 = MathHelper.a(f3, -90.0F, 90.0F);
            if (worldserver == entity.world) {
                entity.setPositionRotation(d0, d1, d2, f2, f3);
                entity.setHeadRotation(f2);
            } else {
                WorldServer worldserver1 = (WorldServer)entity.world;
                worldserver1.kill(entity);
                entity.dimension = worldserver.worldProvider.getDimensionManager();
                entity.dead = false;
                Entity entity1 = entity;
                entity = entity.P().a(worldserver);
                if (entity == null) {
                    return;
                }

                entity.v(entity1);
                entity.setPositionRotation(d0, d1, d2, f2, f3);
                entity.setHeadRotation(f2);
                boolean flag = entity.attachedToPlayer;
                entity.attachedToPlayer = true;
                worldserver.addEntity(entity);
                entity.attachedToPlayer = flag;
                worldserver.entityJoinedWorld(entity, false);
                entity1.dead = true;
            }
        }

        if (commandteleport$a != null) {
            commandteleport$a.a(commandlistenerwrapper, entity);
        }

        if (!(entity instanceof EntityLiving) || !((EntityLiving)entity).dc()) {
            entity.motY = 0.0D;
            entity.onGround = true;
        }

    }

    static class a {
        private final Vec3D a;
        private final Entity b;
        private final ArgumentAnchor.Anchor c;

        public a(Entity entity, ArgumentAnchor.Anchor argumentanchor$anchor) {
            this.b = entity;
            this.c = argumentanchor$anchor;
            this.a = argumentanchor$anchor.a(entity);
        }

        public a(Vec3D vec3d) {
            this.b = null;
            this.a = vec3d;
            this.c = null;
        }

        public void a(CommandListenerWrapper commandlistenerwrapper, Entity entity) {
            if (this.b != null) {
                if (entity instanceof EntityPlayer) {
                    ((EntityPlayer)entity).a(commandlistenerwrapper.k(), this.b, this.c);
                } else {
                    entity.a(commandlistenerwrapper.k(), this.a);
                }
            } else {
                entity.a(commandlistenerwrapper.k(), this.a);
            }

        }
    }
}