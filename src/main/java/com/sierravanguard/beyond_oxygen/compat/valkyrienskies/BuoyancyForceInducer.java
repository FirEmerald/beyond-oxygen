package com.sierravanguard.beyond_oxygen.compat.valkyrienskies;
import com.sierravanguard.beyond_oxygen.BOConfig;
import com.sierravanguard.beyond_oxygen.BOServerConfig;
import org.jetbrains.annotations.NotNull;
import org.valkyrienskies.core.api.ships.LoadedServerShip;
import org.valkyrienskies.core.api.ships.PhysShip;
import org.valkyrienskies.core.api.ships.ShipPhysicsListener;
import org.valkyrienskies.core.api.world.PhysLevel;

public final class BuoyancyForceInducer implements ShipPhysicsListener {
    public static final double AIR_DENSITY = 1.2929;

    private final BuoyancyData data;

    public BuoyancyForceInducer() {
        this.data = new BuoyancyData();
    }
    @Override
    public void physTick(@NotNull PhysShip physShip, @NotNull PhysLevel physLevel) {
        double defaultDensity = BOConfig.getDefaultDensity();
        if (data.totalVolume <= 0 || defaultDensity <= 0) {
            physShip.setBuoyantFactor(1);
            return;
        }
        //buoyance = DEFAULT_DENSITY / (newMass / newVolume) = DEFAULT_DENSITY * newVolume / newMass
        //oldVolume = oldMass / DEFAULT_DENSITY
        //newVolume = oldVolume + data.totalVolume = oldMass / DEFAULT_DENSITY + data.totalVolume
        //newMass = oldMass + (data.totalVolume * AIR_DENSITY)
        //buoyance = DEFAULT_DENSITY * (oldMass / DEFAULT_DENSITY + data.totalVolume) / (oldMass + data.totalVolume * AIR_DENSITY) = (oldMass + data.totalVolume * DEFAULT_DENSITY) / (oldMass + data.totalVolume * AIR_DENSITY)
        double oldMass = physShip.getMass();
        double buoyance = (oldMass + data.totalVolume * defaultDensity) / (oldMass + data.totalVolume * AIR_DENSITY);
        physShip.setBuoyantFactor(buoyance);
    }
    public static BuoyancyForceInducer tickOnShip(final LoadedServerShip ship, double sealedVolume) {
        BuoyancyForceInducer attachment = ship.getOrPutAttachment(BuoyancyForceInducer.class, BuoyancyForceInducer::new);

        attachment.data.totalVolume = sealedVolume;

        return attachment;
    }
}

