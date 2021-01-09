package net.pseudow;

import net.pseudow.commands.AmazingPetCommand;
import net.pseudow.listeners.ConnexionListeners;
import net.pseudow.listeners.GuiListeners;
import net.pseudow.receivers.ReceiverType;
import net.pseudow.updater.Updater;
import org.bukkit.plugin.java.JavaPlugin;

public class AmazingPet extends JavaPlugin {
    public boolean PREMIUM_VERSION;

    public boolean PARTICLES_ACTIVE;
    public boolean PET_INVISIBLE_ON_SNEAK;
    public boolean RECEIVER_ACTIVE_ON_JOIN;

    private AmazingPetCommand amazingPetCommand;
    private Core core;

    @Override
    public final void onEnable() {
        this.saveDefaultConfig();
        this.core = new Core(this);

        PREMIUM_VERSION = false;
        PARTICLES_ACTIVE = getConfig().getBoolean("particles-active");
        PET_INVISIBLE_ON_SNEAK = getConfig().getBoolean("pet-invisible-on-sneak");
        RECEIVER_ACTIVE_ON_JOIN = getConfig().getBoolean("receiver-active-on-join");

        new ConnexionListeners(this);
        new GuiListeners(this);
        new Updater(this);

        this.amazingPetCommand = new AmazingPetCommand(this);
        
        getServer().getOnlinePlayers().forEach(player -> core.getReceiverManager().setReceiver(player, RECEIVER_ACTIVE_ON_JOIN ? ReceiverType.ALL_PETS : ReceiverType.ONLY_MINE));
    }

    @Override
    public void onDisable() {
        core.disable();
    }

    public Core getCore() {
        return core;
    }

    public AmazingPetCommand getAmazingPetCommand() {
        return amazingPetCommand;
    }
}
