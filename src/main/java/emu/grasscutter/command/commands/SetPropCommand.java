package emu.grasscutter.command.commands;

import emu.grasscutter.server.packet.send.PacketSceneAreaUnlockNotify;
import emu.grasscutter.server.packet.send.PacketScenePointUnlockNotify;
import java.util.Collection;
import emu.grasscutter.data.GameData;
import emu.grasscutter.net.packet.BasePacket;
import emu.grasscutter.server.packet.send.PacketOpenStateChangeNotify;
import java.util.Iterator;
import emu.grasscutter.game.tower.TowerLevelRecord;
import emu.grasscutter.game.player.Player;
import emu.grasscutter.game.props.PlayerProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import emu.grasscutter.command.Command;
import emu.grasscutter.command.CommandHandler;

@Command(label = "setProp", aliases = { "prop" }, usage = { "<prop> <value>" }, permission = "player.setprop", permissionTargeted = "player.setprop.others")
public final class SetPropCommand implements CommandHandler
{
    Map<String, SetPropCommand.Prop> props;
    private static final List<Integer> sceneAreas;
    
    public SetPropCommand() {
        this.props = new HashMap<String, SetPropCommand.Prop>();
        for (final PlayerProperty prop : PlayerProperty.values()) {
            final String name = prop.toString().substring(5);
            final String key = name.toLowerCase();
            this.props.put(key, new SetPropCommand.Prop(name, prop));
        }
        final SetPropCommand.Prop worldlevel = new SetPropCommand.Prop("World Level", PlayerProperty.PROP_PLAYER_WORLD_LEVEL, SetPropCommand.PseudoProp.WORLD_LEVEL);
        this.props.put("worldlevel", worldlevel);
        this.props.put("wl", worldlevel);
        final SetPropCommand.Prop abyss = new SetPropCommand.Prop("Tower Level", SetPropCommand.PseudoProp.TOWER_LEVEL);
        this.props.put("abyss", abyss);
        this.props.put("abyssfloor", abyss);
        this.props.put("ut", abyss);
        this.props.put("tower", abyss);
        this.props.put("towerlevel", abyss);
        this.props.put("unlocktower", abyss);
        final SetPropCommand.Prop bplevel = new SetPropCommand.Prop("BP Level", SetPropCommand.PseudoProp.BP_LEVEL);
        this.props.put("bplevel", bplevel);
        this.props.put("bp", bplevel);
        this.props.put("battlepass", bplevel);
        final SetPropCommand.Prop godmode = new SetPropCommand.Prop("GodMode", SetPropCommand.PseudoProp.GOD_MODE);
        this.props.put("godmode", godmode);
        this.props.put("god", godmode);
        final SetPropCommand.Prop nostamina = new SetPropCommand.Prop("UnlimitedStamina", SetPropCommand.PseudoProp.UNLIMITED_STAMINA);
        this.props.put("unlimitedstamina", nostamina);
        this.props.put("us", nostamina);
        this.props.put("nostamina", nostamina);
        this.props.put("nostam", nostamina);
        this.props.put("ns", nostamina);
        final SetPropCommand.Prop unlimitedenergy = new SetPropCommand.Prop("UnlimitedEnergy", SetPropCommand.PseudoProp.UNLIMITED_ENERGY);
        this.props.put("unlimitedenergy", unlimitedenergy);
        this.props.put("ue", unlimitedenergy);
        final SetPropCommand.Prop setopenstate = new SetPropCommand.Prop("SetOpenstate", SetPropCommand.PseudoProp.SET_OPENSTATE);
        this.props.put("setopenstate", setopenstate);
        this.props.put("so", setopenstate);
        final SetPropCommand.Prop unsetopenstate = new SetPropCommand.Prop("UnsetOpenstate", SetPropCommand.PseudoProp.UNSET_OPENSTATE);
        this.props.put("unsetopenstate", unsetopenstate);
        this.props.put("uo", unsetopenstate);
        final SetPropCommand.Prop unlockmap = new SetPropCommand.Prop("UnlockMap", SetPropCommand.PseudoProp.UNLOCK_MAP);
        this.props.put("unlockmap", unlockmap);
        this.props.put("um", unlockmap);
    }
    
    public void execute(final Player sender, final Player targetPlayer, final List<String> args) {
        if (args.size() != 2) {
            this.sendUsageMessage(sender, new String[0]);
            return;
        }
        final String propStr = args.get(0).toLowerCase();
        final String valueStr = args.get(1).toLowerCase();
        if (!this.props.containsKey(propStr)) {
            this.sendUsageMessage(sender, new String[0]);
            return;
        }
        int value;
        try {
            final String lowerCase = valueStr.toLowerCase();
            int int1 = 0;
            switch (lowerCase) {
                case "on":
                case "true": {
                    int1 = 1;
                    break;
                }
                case "off":
                case "false": {
                    int1 = 0;
                    break;
                }
                case "toggle": {
                    int1 = -1;
                    break;
                }
                default: {
                    int1 = Integer.parseInt(valueStr);
                    break;
                }
            }
            value = int1;
        }
        catch (NumberFormatException ignored) {
            CommandHandler.sendTranslatedMessage(sender, "commands.execution.argument_error", new Object[0]);
            return;
        }
        boolean success = false;
        final SetPropCommand.Prop prop = this.props.get(propStr);
        boolean b = false;
        switch (SetPropCommand.SetPropCommand$2.$SwitchMap$emu$grasscutter$command$commands$SetPropCommand$PseudoProp[prop.pseudoProp.ordinal()]) {
            case 1: {
                b = targetPlayer.setWorldLevel(value);
                break;
            }
            case 2: {
                b = targetPlayer.getBattlePassManager().setLevel(value);
                break;
            }
            case 3: {
                b = this.setTowerLevel(sender, targetPlayer, value);
                break;
            }
            case 4:
            case 5:
            case 6: {
                b = this.setBool(sender, targetPlayer, prop.pseudoProp, value);
                break;
            }
            case 7: {
                b = this.setOpenState(targetPlayer, value, 1);
                break;
            }
            case 8: {
                b = this.setOpenState(targetPlayer, value, 0);
                break;
            }
            case 9: {
                b = this.unlockMap(targetPlayer);
                break;
            }
            default: {
                b = targetPlayer.setProperty(prop.prop, value);
                break;
            }
        }
        success = b;
        if (success) {
            if (targetPlayer == sender) {
                CommandHandler.sendTranslatedMessage(sender, "commands.generic.set_to", new Object[] { prop.name, valueStr });
            }
            else {
                final String uidStr = targetPlayer.getAccount().getId();
                CommandHandler.sendTranslatedMessage(sender, "commands.generic.set_for_to", new Object[] { prop.name, uidStr, valueStr });
            }
        }
        else if (prop.prop != PlayerProperty.PROP_NONE) {
            final int min = targetPlayer.getPropertyMin(prop.prop);
            final int max = targetPlayer.getPropertyMax(prop.prop);
            CommandHandler.sendTranslatedMessage(sender, "commands.generic.invalid.value_between", new Object[] { prop.name, min, max });
        }
    }
    
    private boolean setTowerLevel(final Player sender, final Player targetPlayer, final int topFloor) {
        final List<Integer> floorIds = (List<Integer>)targetPlayer.getServer().getTowerSystem().getAllFloors();
        if (topFloor < 0 || topFloor > floorIds.size()) {
            CommandHandler.sendTranslatedMessage(sender, "commands.generic.invalid.value_between", new Object[] { "Tower Level", 0, floorIds.size() });
            return false;
        }
        final Map<Integer, TowerLevelRecord> recordMap = (Map<Integer, TowerLevelRecord>)targetPlayer.getTowerManager().getRecordMap();
        for (final int floor : floorIds.subList(0, topFloor)) {
            if (!recordMap.containsKey(floor)) {
                recordMap.put(floor, new TowerLevelRecord(floor));
            }
        }
        for (final int floor : floorIds.subList(topFloor, floorIds.size())) {
            if (recordMap.containsKey(floor)) {
                recordMap.remove(floor);
            }
        }
        if (topFloor > 8) {
            recordMap.get(floorIds.get(7)).setLevelStars(0, 6);
        }
        return true;
    }
    
    private boolean setBool(final Player sender, final Player targetPlayer, final SetPropCommand.PseudoProp pseudoProp, final int value) {
        boolean b = false;
        switch (SetPropCommand.SetPropCommand$2.$SwitchMap$emu$grasscutter$command$commands$SetPropCommand$PseudoProp[pseudoProp.ordinal()]) {
            case 4: {
                b = targetPlayer.inGodmode();
                break;
            }
            case 5: {
                b = targetPlayer.getUnlimitedStamina();
                break;
            }
            case 6: {
                b = !targetPlayer.getEnergyManager().getEnergyUsage();
                break;
            }
            default: {
                b = false;
                break;
            }
        }
        boolean enabled = b;
        boolean b2 = false;
        switch (value) {
            case -1: {
                b2 = !enabled;
                break;
            }
            case 0: {
                b2 = false;
                break;
            }
            default: {
                b2 = true;
                break;
            }
        }
        enabled = b2;
        switch (SetPropCommand.SetPropCommand$2.$SwitchMap$emu$grasscutter$command$commands$SetPropCommand$PseudoProp[pseudoProp.ordinal()]) {
            case 4: {
                targetPlayer.setGodmode(enabled);
                break;
            }
            case 5: {
                targetPlayer.setUnlimitedStamina(enabled);
                break;
            }
            case 6: {
                targetPlayer.getEnergyManager().setEnergyUsage(!enabled);
                break;
            }
            default: {
                return false;
            }
        }
        return true;
    }
    
    private boolean setOpenState(final Player targetPlayer, final int state, final int value) {
        targetPlayer.sendPacket((BasePacket)new PacketOpenStateChangeNotify(state, value));
        return true;
    }
    
    private boolean unlockMap(final Player targetPlayer) {
        GameData.getScenePointsPerScene().forEach((sceneId, scenePoints) -> {
            targetPlayer.getUnlockedScenePoints((int)sceneId).addAll(scenePoints);
            targetPlayer.getUnlockedSceneAreas((int)sceneId).addAll(SetPropCommand.sceneAreas);
            return;
        });
        final int playerScene = targetPlayer.getSceneId();
        targetPlayer.sendPacket((BasePacket)new PacketScenePointUnlockNotify(playerScene, (Iterable)targetPlayer.getUnlockedScenePoints(playerScene)));
        targetPlayer.sendPacket((BasePacket)new PacketSceneAreaUnlockNotify(playerScene, (Iterable)targetPlayer.getUnlockedSceneAreas(playerScene)));
        return true;
    }
    
    static {
        sceneAreas = new SetPropCommand.SetPropCommand$1().getSceneArea();
    }
}