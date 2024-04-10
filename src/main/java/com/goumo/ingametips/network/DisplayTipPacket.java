package com.goumo.ingametips.network;

import com.goumo.ingametips.client.TipHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DisplayTipPacket {
    private final String ID;

    public DisplayTipPacket(FriendlyByteBuf buffer) {
        ID = buffer.readUtf();
    }

    public DisplayTipPacket(String ID) {
        this.ID = ID;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.ID);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> TipHandler.addToRenderQueue(ID, false));
        ctx.get().setPacketHandled(true);
    }
}
