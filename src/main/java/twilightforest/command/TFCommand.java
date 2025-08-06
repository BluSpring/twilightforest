package twilightforest.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class TFCommand {


	private CenterCommand centerCommand = new CenterCommand();


	private ConquerCommand conquerCommand = new ConquerCommand();


	private GenerateBookCommand generateBookCommand = new GenerateBookCommand();


	private InfoCommand infoCommand = new InfoCommand();


	private MapBiomesCommand mapBiomesCommand = new MapBiomesCommand();


	private MapLocatorCommand mapLocatorCommand = new MapLocatorCommand();


	private ShieldCommand shieldCommand = new ShieldCommand();


	private SinisterSpawnerCommand spawnerCommand = new SinisterSpawnerCommand();


	private DisplayPiecesCommand displayPiecesCommand = new DisplayPiecesCommand();


	private CountLootCommand countLootCommand = new CountLootCommand();


	private CountTemplateCommand countTemplateCommand = new CountTemplateCommand();


	private StructureDistanceCommand structureDistanceCommand = new StructureDistanceCommand();


	private ClearDisplayCommand clearDisplayCommand = new ClearDisplayCommand();

	public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
		LiteralArgumentBuilder<CommandSourceStack> structureBranch = Commands.literal("structure_util")
			.then(displayPiecesCommand.register())
			.then(clearDisplayCommand.register())
			.then(countLootCommand.register())
			.then(countTemplateCommand.register())
			.then(structureDistanceCommand.register());

		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("twilightforest")
			.executes(this::run)
			.then(centerCommand.register())
			.then(mapLocatorCommand.register())
			.then(conquerCommand.register())
			.then(generateBookCommand.register())
			.then(infoCommand.register())
			.then(mapBiomesCommand.register())
			.then(shieldCommand.register())
			.then(spawnerCommand.register(buildContext))
			.then(structureBranch);
		LiteralCommandNode<CommandSourceStack> node = dispatcher.register(builder);
		dispatcher.register(Commands.literal("tf").executes(this::run).redirect(node));
		dispatcher.register(Commands.literal("tffeature").executes(this::run).redirect(node));
	}

	private int run(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		throw new SimpleCommandExceptionType(Component.translatable("commands.tffeature.usage", ctx.getInput())).create();
	}
}
