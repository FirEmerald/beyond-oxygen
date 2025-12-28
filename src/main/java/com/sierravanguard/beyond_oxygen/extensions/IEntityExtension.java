package com.sierravanguard.beyond_oxygen.extensions;

import com.sierravanguard.beyond_oxygen.utils.HermeticArea;

import java.util.Collection;

public interface IEntityExtension {
    boolean beyond_oxygen$isInSealedArea();

    Collection<HermeticArea> beyond_oxygen$getAreasIn();

    void beyond_oxygen$setIsInSealedArea(boolean value);
}
