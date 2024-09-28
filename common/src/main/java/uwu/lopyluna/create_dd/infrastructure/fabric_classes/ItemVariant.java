package uwu.lopyluna.create_dd.infrastructure.fabric_classes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public interface ItemVariant extends TransferVariant<Item> {
    /**
     * Retrieve a blank ItemVariant.
     */
    static ItemVariant blank() {
        return of(Items.AIR);
    }

    /**
     * Retrieve an ItemVariant with the item and tag of a stack.
     */
    static ItemVariant of(ItemStack stack) {
        return of(stack.getItem(), stack.getTag());
    }

    /**
     * Retrieve an ItemVariant with an item and without a tag.
     */
    static ItemVariant of(ItemLike item) {
        return of(item, null);
    }

    /**
     * Retrieve an ItemVariant with an item and an optional tag.
     */
    static ItemVariant of(ItemLike item, @Nullable CompoundTag tag) {
        return ItemVariantImpl.of(item.asItem(), tag);
    }

    /**
     * Return true if the item and tag of this variant match those of the passed stack, and false otherwise.
     */
    default boolean matches(ItemStack stack) {
        return isOf(stack.getItem()) && nbtMatches(stack.getTag());
    }

    /**
     * Return the item of this variant.
     */
    default Item getItem() {
        return getObject();
    }

    /**
     * Create a new item stack with count 1 from this variant.
     */
    default ItemStack toStack() {
        return toStack(1);
    }

    /**
     * Create a new item stack from this variant.
     *
     * @param count The count of the returned stack. It may lead to counts higher than maximum stack size.
     */
    default ItemStack toStack(int count) {
        if (isBlank()) return ItemStack.EMPTY;
        ItemStack stack = new ItemStack(getItem(), count);
        stack.setTag(copyNbt());
        return stack;
    }

    /**
     * Deserialize a variant from an NBT compound tag, assuming it was serialized using
     * {@link #toNbt}. If an error occurs during deserialization, it will be logged
     * with the DEBUG level, and a blank variant will be returned.
     */
    static ItemVariant fromNbt(CompoundTag nbt) {
        return ItemVariantImpl.fromNbt(nbt);
    }

    /**
     * Write a variant from a packet byte buffer, assuming it was serialized using
     * {@link #toPacket}.
     */
    static ItemVariant fromPacket(FriendlyByteBuf buf) {
        return ItemVariantImpl.fromPacket(buf);
    }
}
