package net.pseudow.receivers;

import net.pseudow.Core;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ReceiverManager implements IReceiverManager {
    private final Map<Player, ReceiverType> receivers;
    private final Core core;

    public ReceiverManager(Core core) {
        this.receivers = new HashMap<>();
        this.core = core;
    }

    @Override
    public boolean setReceiver(Player player, ReceiverType type) {
        if(receivers.containsKey(player)) if(receivers.get(player) == type) return false;

        receivers.put(player, type);

        core.getPetManager().getPlayersPet().values().forEach(pet -> {
            if(core.shouldSendPet(player, type, pet)) {
                pet.sendInitPacket(player);
            } else {
                pet.sendDestroyPacket(player);
            }
        });

        return true;
    }

    @Override
    public void removeReceiver(Player player) {
        receivers.remove(player);
        core.getPetManager().getPlayersPet().values().forEach(pet -> pet.sendDestroyPacket(player));
    }

    @Override
    public Map<Player, ReceiverType> getReceivers() {
        return receivers;
    }
}
