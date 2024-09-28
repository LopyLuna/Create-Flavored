package uwu.lopyluna.create_dd.registry;

import net.minecraft.world.item.CreativeModeTab;
import uwu.lopyluna.create_dd.infrastructure.tabs.BaseCreativeModeTab;
import uwu.lopyluna.create_dd.infrastructure.tabs.BetaCreativeModeTab;
import uwu.lopyluna.create_dd.infrastructure.tabs.ClassicCreativeModeTab;
import uwu.lopyluna.create_dd.infrastructure.tabs.PalettesCreativeModeTab;

public class DesiresCreativeModeTabs {
	public static final CreativeModeTab BASE_CREATIVE_TAB = new BaseCreativeModeTab();
	public static final CreativeModeTab PALETTES_CREATIVE_TAB = new PalettesCreativeModeTab();
	public static final CreativeModeTab BETA_CREATIVE_TAB = new BetaCreativeModeTab();
	public static final CreativeModeTab CLASSIC_CREATIVE_TAB = new ClassicCreativeModeTab();

	public static void init() {
	}
}
