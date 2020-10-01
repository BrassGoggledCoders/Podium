package xyz.brassgoggledcoders.podium.container.reference;

import net.minecraft.util.IIntArray;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class SingleValueFunctionIntArray implements IIntArray {
    private final IntSupplier intSupplier;
    private final IntConsumer intConsumer;

    public SingleValueFunctionIntArray(IntSupplier intSupplier, IntConsumer intConsumer) {
        this.intSupplier = intSupplier;
        this.intConsumer = intConsumer;
    }

    @Override
    public int get(int index) {
        return this.intSupplier.getAsInt();
    }

    @Override
    public void set(int index, int value) {
        if (index == 0) {
            this.intConsumer.accept(value);
        }
    }

    @Override
    public int size() {
        return 1;
    }
}
