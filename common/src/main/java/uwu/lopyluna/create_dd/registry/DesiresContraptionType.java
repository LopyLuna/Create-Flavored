package uwu.lopyluna.create_dd.registry;

import com.simibubi.create.content.contraptions.ContraptionType;
import uwu.lopyluna.create_dd.DesireUtil;
import uwu.lopyluna.create_dd.content.blocks.contraptions.contraption_block.BoatContraption;

public class DesiresContraptionType {
    public static final ContraptionType BOAT = ContraptionType.register(DesireUtil.asResource("boat").toString(), BoatContraption::new);
}
