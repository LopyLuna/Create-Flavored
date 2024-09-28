package uwu.lopyluna.create_dd.infrastructure.data;

import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.data.DataGenerator;
import uwu.lopyluna.create_dd.Desires;
import uwu.lopyluna.create_dd.content.data_recipes.DesireProcessingRecipeGen;
import uwu.lopyluna.create_dd.content.data_recipes.DesiresRecipeGen;
import uwu.lopyluna.create_dd.content.data_recipes.MechanicalCraftingRecipeGen;
import uwu.lopyluna.create_dd.content.data_recipes.SequencedAssemblyRecipeGen;
import uwu.lopyluna.create_dd.content.data_tags.DesiresRegistrateTags;
import uwu.lopyluna.create_dd.infrastructure.ponder.DesirePonderTags;
import uwu.lopyluna.create_dd.infrastructure.ponder.DesiresPonderIndex;
import uwu.lopyluna.create_dd.registry.DesiresLangPartial;

import java.util.function.BiConsumer;

public class DesiresDatagen {
	public static void gatherData(GatherDataEvent event) {
		addExtraRegistrateData();

		DataGenerator generator = event.getGenerator();

		//if (event.includeClient()) {
			//generator.addProvider(true, DesiresAllSoundEvents.provider(generator));
		//}
		if (event.includeServer()) {

			generator.addProvider(true, new DesiresRecipeGen(generator));
			generator.addProvider(true, new SequencedAssemblyRecipeGen(generator));
			generator.addProvider(true, new MechanicalCraftingRecipeGen(generator));
			DesireProcessingRecipeGen.registerAll(generator);
		}
	}

	private static void addExtraRegistrateData() {
		DesiresRegistrateTags.addGenerators();

		Desires.REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
			BiConsumer<String, String> langConsumer = provider::add;

			providePartialLang(langConsumer);
			providePonderLang();
		});
	}

	private static void providePartialLang(BiConsumer<String, String> consumer) {
		DesiresLangPartial.provideLang(consumer);
	}

	private static void providePonderLang() {
		DesirePonderTags.register();
		DesiresPonderIndex.register();
	}
}
