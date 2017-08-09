package fr.dinnerwolph.api.utils;

import fr.dinnerwolph.api.Api;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zyuiop
 */
public class ScoreboardSign {
    private boolean created = false;
    private final VirtualTeam[] lines = new VirtualTeam[15];
    private final Player player;
    private BukkitTask bukkitTask;
    private String objectiveName;
    private final Api api = Api.getInstance();

    /**
     * Create a scoreboard sign for a given player and using a specifig objective name
     *
     * @param player        the player viewing the scoreboard sign
     * @param objectiveName the name of the scoreboard sign (displayed at the top of the scoreboard)
     */
    public ScoreboardSign(Player player, String objectiveName) {
        this.player = player;
        this.objectiveName = objectiveName;
        bukkitTask = new lastLine().runTaskTimer(Api.getInstance(), 15 * 20, 15 * 20);
        api.getScoreboardSigns().put(player, this);
    }

    /**
     * Send the initial creation packets for this scoreboard sign. Must be called at least once.
     */
    public void create() {
        if (created)
            return;

        PlayerConnection player = getPlayer();
        player.sendPacket(createObjectivePacket(0, objectiveName));
        player.sendPacket(setObjectiveSlot());
        int i = 0;
        while (i < lines.length)
            sendLine(i++);
        created = true;
        setLine("§bplay.opsky.fr");
    }

    /**
     * Send the packets to remove this scoreboard sign. A destroyed scoreboard sign must be recreated using {@link ScoreboardSign#create()} in order
     * to be used again
     */
    public void destroy() {
        if (!created)
            return;

        getPlayer().sendPacket(createObjectivePacket(1, null));
        for (VirtualTeam team : lines)
            if (team != null)
                getPlayer().sendPacket(team.removeTeam());

        created = false;
        bukkitTask.cancel();
    }

    public void stopTask() {
        bukkitTask.cancel();
    }

    public void startTask() {
        bukkitTask.cancel();
        bukkitTask = new lastLine().runTaskTimer(Api.getInstance(), 15 * 20, 15 * 20);
    }

    /**
     * Change the name of the objective. The name is displayed at the top of the scoreboard.
     *
     * @param name the name of the objective, max 32 char
     */
    public void setObjectiveName(String name) {
        this.objectiveName = name;
        if (created)
            getPlayer().sendPacket(createObjectivePacket(2, name));
    }

    /**
     * Change a scoreboard line and send the packets to the player. Can be called async.
     *
     * @param line  the number of the line (0 <= line < 14)
     *              15 is the servername
     * @param value the new value for the scoreboard line
     */
    public void setLine(int line, String value) {
        VirtualTeam team = getOrCreateTeam(line);
        String old = team.getCurrentPlayer();
        team.setValue(value);
        if (old != null && created)
            getPlayer().sendPacket(removeLine(old));
        sendLine(line);
    }

    private void setLine(String value) {
        VirtualTeam team = getOrCreateTeam(14);
        String old = team.getCurrentPlayer();
        team.setValue(value);
        if (old != null && created)
            getPlayer().sendPacket(removeLine(old));
        sendLine();
    }

    /**
     * Remove a given scoreboard line
     *
     * @param line the line to remove
     */
    public void removeLine(int line) {
        VirtualTeam team = getOrCreateTeam(line);
        String old = team.getCurrentPlayer();

        if (old != null && created) {
            getPlayer().sendPacket(removeLine(old));
            getPlayer().sendPacket(team.removeTeam());
        }

        lines[line] = null;
    }

    /**
     * Get the current value for a line
     *
     * @param line the line
     * @return the content of the line
     */
    public String getLine(int line) {
        if (line > 13)
            return null;
        if (line < 0)
            return null;
        return getOrCreateTeam(line).getValue();
    }

    /**
     * Get the team assigned to a line
     *
     * @return the {@link VirtualTeam} used to display this line
     */
    public VirtualTeam getTeam(int line) {
        if (line > 13)
            return null;
        if (line < 0)
            return null;
        return getOrCreateTeam(line);
    }

    private PlayerConnection getPlayer() {
        return ((CraftPlayer) player).getHandle().playerConnection;
    }

    private void sendLine(int line) {
        if (line > 13)
            return;
        if (line < 0)
            return;
        if (!created)
            return;

        int score = (15 - line);
        VirtualTeam val = getOrCreateTeam(line);
        for (Packet packet : val.sendLine())
            getPlayer().sendPacket(packet);
        getPlayer().sendPacket(sendScore(val.getCurrentPlayer(), score));
        val.reset();
    }

    private void sendLine() {
        VirtualTeam val = getOrCreateTeam(14);
        for (Packet packet : val.sendLine())
            getPlayer().sendPacket(packet);
        getPlayer().sendPacket(sendScore(val.getCurrentPlayer(), 1));
    }

    private VirtualTeam getOrCreateTeam(int line) {
        if (lines[line] == null)
            lines[line] = new VirtualTeam("__fakeScore" + line);

        return lines[line];
    }

    /*
        Factories
         */
    private PacketPlayOutScoreboardObjective createObjectivePacket(int mode, String displayName) {
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
        // Nom de l'objectif
        setField(packet, "a", player.getName());

        // Mode
        // 0 : créer
        // 1 : Supprimer
        // 2 : Mettre à jour
        setField(packet, "d", mode);

        if (mode == 0 || mode == 2) {
            setField(packet, "b", displayName);
            setField(packet, "c", IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
        }

        return packet;
    }

    private PacketPlayOutScoreboardDisplayObjective setObjectiveSlot() {
        PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective();
        // Slot
        setField(packet, "a", 1);
        setField(packet, "b", player.getName());

        return packet;
    }

    private PacketPlayOutScoreboardScore sendScore(String line, int score) {
        PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(line);
        setField(packet, "b", player.getName());
        setField(packet, "c", score);
        setField(packet, "d", PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE);

        return packet;
    }

    private PacketPlayOutScoreboardScore removeLine(String line) {
        return new PacketPlayOutScoreboardScore(line);
    }

    /**
     * This class is used to manage the content of a line. Advanced users can use it as they want, but they are encouraged to read and understand the
     * code before doing so. Use these methods at your own risk.
     */
    public class VirtualTeam {
        private final String name;
        private String prefix;
        private String suffix;
        private String currentPlayer;
        private String oldPlayer;

        private boolean prefixChanged, suffixChanged, playerChanged = false;
        private boolean first = true;

        private VirtualTeam(String name, String prefix, String suffix) {
            this.name = name;
            this.prefix = prefix;
            this.suffix = suffix;
        }

        private VirtualTeam(String name) {
            this(name, "", "");
        }

        public String getName() {
            return name;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            if (this.prefix == null || !this.prefix.equals(prefix))
                this.prefixChanged = true;
            this.prefix = prefix;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            if (this.suffix == null || !this.suffix.equals(prefix))
                this.suffixChanged = true;
            this.suffix = suffix;
        }

        private PacketPlayOutScoreboardTeam createPacket(int mode) {
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();

            //a :name
            //b: team display name
            //c: prefix
            //d suffix
            //e : nametagvisibility (always)
            //f : Collision Rule
            //i : mode
            //j : fridenly fire
            setField(packet, "a", name);
            setField(packet, "i", mode);
            setField(packet, "b", "");
            setField(packet, "c", prefix);
            setField(packet, "d", suffix);
            setField(packet, "e", "always");
            setField(packet, "j", 0);

            return packet;
        }

        public PacketPlayOutScoreboardTeam createTeam() {
            return createPacket(0);
        }

        public PacketPlayOutScoreboardTeam updateTeam() {
            return createPacket(2);
        }

        public PacketPlayOutScoreboardTeam removeTeam() {
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
            setField(packet, "a", name);
            setField(packet, "i", 1);
            first = true;
            return packet;
        }

        public void setPlayer(String name) {
            if (this.currentPlayer == null || !this.currentPlayer.equals(name))
                this.playerChanged = true;
            this.oldPlayer = this.currentPlayer;
            this.currentPlayer = name;
        }

        public Iterable<PacketPlayOutScoreboardTeam> sendLine() {
            List<PacketPlayOutScoreboardTeam> packets = new ArrayList<>();

            if (first) {
                packets.add(createTeam());
            } else if (prefixChanged || suffixChanged) {
                packets.add(updateTeam());
            }

            if (first || playerChanged) {
                if (oldPlayer != null)                                        // remove these two lines ?
                    packets.add(addOrRemovePlayer(4, oldPlayer));    //
                packets.add(changePlayer());
            }

            if (first)
                first = false;

            return packets;
        }

        public void reset() {
            prefixChanged = false;
            suffixChanged = false;
            playerChanged = false;
            oldPlayer = null;
        }

        public PacketPlayOutScoreboardTeam changePlayer() {
            return addOrRemovePlayer(3, currentPlayer);
        }

        public PacketPlayOutScoreboardTeam addOrRemovePlayer(int mode, String playerName) {
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
            setField(packet, "a", name);
            setField(packet, "i", mode);

            try {
                Field f = packet.getClass().getDeclaredField("h");
                f.setAccessible(true);
                ((List<String>) f.get(packet)).add(playerName);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            return packet;
        }

        public String getCurrentPlayer() {
            return currentPlayer;
        }

        public String getValue() {
            return getPrefix() + getCurrentPlayer() + getSuffix();
        }

        public void setValue(String value) {
            if (value.length() <= 16) {
                setPrefix("");
                setSuffix("");
                setPlayer(value);
            } else if (value.length() <= 32) {
                setPrefix(value.substring(0, 16));
                setPlayer(value.substring(16));
                setSuffix("");
            } else if (value.length() <= 48) {
                setPrefix(value.substring(0, 16));
                setPlayer(value.substring(16, 32));
                setSuffix(value.substring(32));
            } else {
                throw new IllegalArgumentException("Too long value ! Max 48 characters, value was " + value.length() + " !");
            }
        }
    }

    private class lastLine extends BukkitRunnable {
        private String line[] = {"§bplay.opsky.fr", "§3p§blay.opsky.fr", "§bp§3l§bay.opsky.fr",
                "§bpl§3a§by.opsky.fr", "§bpla§3y§b.opsky.fr", "§bplay§3.§bopsky.fr",
                "§bplay.§3o§bpsky.fr", "§bplay.o§3p§bsky.fr", "§bplay.op§3s§bky.fr",
                "§bplay.ops§3k§by.fr", "§bplay.opsk§3y§b.fr", "§bplay.opsky§3.§bfr",
                "§bplay.opsky.§3f§br", "§bplay.opsky.f§3r", "§bplay.opsky.fr"};
        int i = 0;

        @Override
        public void run() {
            new BukkitRunnable() {
                @Override
                public void run() {
                    setLine(line[i++]);
                    if (i >= line.length) {
                        this.cancel();
                        i = 0;

                    }
                }
            }.runTaskTimer(Api.getInstance(), 5L, 5L);
        }
    }

    private static void setField(Object edit, String fieldName, Object value) {
        try {
            Field field = edit.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(edit, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}