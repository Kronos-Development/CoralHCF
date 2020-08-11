package net.frozenorb.foxtrot;

import com.comphenix.protocol.ProtocolLibrary;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import lombok.Getter;
import lombok.Setter;
import net.frozenorb.foxtrot.abilities.AbilityHandler;
import net.frozenorb.foxtrot.chat.ChatHandler;
import net.frozenorb.foxtrot.crates.CrateHandler;
import net.frozenorb.foxtrot.deathmessage.DeathMessageHandler;
import net.frozenorb.foxtrot.elevators.ElevatorListener;
import net.frozenorb.foxtrot.events.EventHandler;
import net.frozenorb.foxtrot.events.citadel.CitadelHandler;
import net.frozenorb.foxtrot.events.conquest.ConquestHandler;
import net.frozenorb.foxtrot.events.region.carepackage.CarePackageHandler;
import net.frozenorb.foxtrot.events.region.cavern.CavernHandler;
import net.frozenorb.foxtrot.events.region.glowmtn.GlowHandler;
import net.frozenorb.foxtrot.listener.*;
import net.frozenorb.foxtrot.map.MapHandler;
import net.frozenorb.foxtrot.map.deathban.purgatory.PurgatoryHandler;
import net.frozenorb.foxtrot.packetborder.PacketBorderThread;
import net.frozenorb.foxtrot.persist.RedisSaveTask;
import net.frozenorb.foxtrot.persist.maps.*;
import net.frozenorb.foxtrot.powers.PowersHandler;
import net.frozenorb.foxtrot.powers.PowersListener;
import net.frozenorb.foxtrot.protocol.ClientCommandPacketAdaper;
import net.frozenorb.foxtrot.protocol.SignGUIPacketAdaper;
import net.frozenorb.foxtrot.pvpclasses.PvPClassHandler;
import net.frozenorb.foxtrot.server.EnderpearlCooldownHandler;
import net.frozenorb.foxtrot.server.ServerHandler;
import net.frozenorb.foxtrot.tab.FoxtrotTabLayoutProvider;
import net.frozenorb.foxtrot.tasks.RallyExpireTask;
import net.frozenorb.foxtrot.team.TeamHandler;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.commands.team.TeamClaimCommand;
import net.frozenorb.foxtrot.team.commands.team.subclaim.TeamSubclaimCommand;
import net.frozenorb.foxtrot.team.dtr.DTRHandler;
import net.frozenorb.foxtrot.util.RegenUtils;
import net.frozenorb.qlib.command.FrozenCommandHandler;
import net.frozenorb.qlib.qLib;
import net.frozenorb.qlib.tab.FrozenTabHandler;
import net.frozenorb.qlib.util.ClassUtils;
import net.minecraft.server.v1_7_R4.Item;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.SpigotConfig;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public class Foxtrot extends JavaPlugin {

	public static String MONGO_DB_NAME = "HCTeams";

	@Getter private static Foxtrot instance;

	@Getter private MongoClient mongoPool;

	@Getter private ChatHandler chatHandler;
	@Getter private PvPClassHandler pvpClassHandler;
	@Getter private CarePackageHandler carePackageHandler;
	@Getter private TeamHandler teamHandler;
	@Getter private ServerHandler serverHandler;
	@Getter private PurgatoryHandler purgatoryHandler;
	@Getter private MapHandler mapHandler;
	@Getter private CitadelHandler citadelHandler;
	@Getter private EventHandler eventHandler;
	@Getter private ConquestHandler conquestHandler;
	@Getter private CavernHandler cavernHandler;
	@Getter private GlowHandler glowHandler;
	@Getter private AbilityHandler abilityHandler;

	@Getter private PlaytimeMap playtimeMap;
	@Getter private PowersHandler powersHandler;
	@Getter private OppleMap oppleMap;
	@Getter private DeathbanMap deathbanMap;
	@Getter private PvPTimerMap PvPTimerMap;
	@Getter private StartingPvPTimerMap startingPvPTimerMap;
	@Getter private DeathsMap deathsMap;
	@Getter private KillsMap killsMap;
	@Getter private ChatModeMap chatModeMap;
	@Getter private CrateHandler crateHandler;
	@Getter private FishingKitMap fishingKitMap;
	@Getter private ToggleGlobalChatMap toggleGlobalChatMap;
	@Getter private ToggleLFFMessageMap toggleLFFMessageMap;
	@Getter private StaffBoardMap staffBoardMap;
	@Getter private AbilityCooldownsMap abilityCooldownsMap;
	@Getter private FDisplayMap fDisplayMap;

	@Getter private TeamColorMap teamColorMap;
	@Getter private EnemyColorMap enemyColorMap;
	@Getter private AllyColorMap allyColorMap;
	@Getter private ArcherTagColorMap archerTagColorMap;
	@Getter private FocusColorMap focusColorMap;

	@Getter private ClassCooldownsMap classCooldownsMap;
	@Getter private ChatSpyMap chatSpyMap;
	@Getter private DiamondMinedMap diamondMinedMap;
	@Getter private GoldMinedMap goldMinedMap;
	@Getter private IronMinedMap ironMinedMap;
	@Getter private CoalMinedMap coalMinedMap;
	@Getter private RedstoneMinedMap redstoneMinedMap;
	@Getter private LapisMinedMap lapisMinedMap;
	@Getter private EmeraldMinedMap emeraldMinedMap;
	@Getter private FirstJoinMap firstJoinMap;
	@Getter private LastJoinMap lastJoinMap;
	@Getter private SoulboundLivesMap soulboundLivesMap;
	@Getter private FriendLivesMap friendLivesMap;
	@Getter private WrappedBalanceMap wrappedBalanceMap;
	@Getter private ToggleFoundDiamondsMap toggleFoundDiamondsMap;
	@Getter private ToggleDeathMessageMap toggleDeathMessageMap;
	@Getter private TabListModeMap tabListModeMap;
	@Getter private CobblePickupMap cobblePickupMap;
	@Getter private KDRMap kdrMap;

	@Getter private CombatLoggerListener combatLoggerListener;
	@Getter
	@Setter
	// for the case of some commands in the plugin,
	// a player shouldn't be able to do them in a duel
	// thus this predicate exists to test that to avoid dep. issues
	private Predicate<Player> inDuelPredicate = (player) -> false;

	@Override
	public void onEnable() {
		if (Bukkit.getPluginManager().getPlugin("Helium").getName().toLowerCase() == null) {
			System.out.println("*********************************************");
			System.out.println("               ATTENTION");
			System.out.println("YOU NEED THE PROPER RANK CORE TO RUN THIS PLUGIN");
			System.out.println("Contact Cody if issues occur.");
			System.out.println("*********************************************");
			this.getServer().shutdown();
			return;
		}

		if (Bukkit.getServerName().contains(" ")) {
			System.out.println("*********************************************");
			System.out.println("               ATTENTION");
			System.out.println("SET server-name VALUE IN server.properties TO");
			System.out.println("A PROPER SERVER NAME. THIS WILL BE USED AS THE");
			System.out.println("MONGO DATABASE NAME.");
			System.out.println("*********************************************");
			this.getServer().shutdown();
			return;
		}

		SpigotConfig.onlyCustomTab = true; // because I know we'll forget
		instance = this;
		saveDefaultConfig();
		
		try {
			String host = getConfig().getString("Mongo.Host", "127.0.0.1");
			String authDB = getConfig().getString("Mongo.AuthDB", "admin");
			String username = getConfig().getString("Mongo.Username", "MineHQ");
			String password = getConfig().getString("Mongo.Password", "");

			boolean authRequired = password.length() > 0;
			ServerAddress address = new ServerAddress(host, 27017);

			if (!authRequired) {
				mongoPool = new MongoClient(address);
			} else {
				mongoPool = new MongoClient(address, MongoCredential.createCredential(
						username,
						authDB,
						password.toCharArray()
				), MongoClientOptions.builder()
						.retryWrites(true)
						.build());
			}

			MONGO_DB_NAME = Bukkit.getServerName();
		} catch (Exception e) {
			e.printStackTrace();
		}

		(new DTRHandler()).runTaskTimer(this, 20L, 1200L);
		(new RedisSaveTask()).runTaskTimerAsynchronously(this, 1200L, 1200L);
		(new PacketBorderThread()).start();

		setupHandlers();
		setupPersistence();
		setupListeners();

		FrozenTabHandler.setLayoutProvider(new FoxtrotTabLayoutProvider());

		ProtocolLibrary.getProtocolManager().addPacketListener(new SignGUIPacketAdaper());
		ProtocolLibrary.getProtocolManager().addPacketListener(new ClientCommandPacketAdaper());

		for (World world : Bukkit.getWorlds()) {
			world.setThundering(false);
			world.setStorm(false);
			world.setWeatherDuration(Integer.MAX_VALUE);
			world.setGameRuleValue("doFireTick", "false");
			world.setGameRuleValue("mobGriefing", "false");
			world.setGameRuleValue("doMobGriefing", "false");
			System.out.println("ok this should work");
		}

		if (getConfig().getBoolean("legions")) {
			try {
				Field field = Item.class.getDeclaredField("maxStackSize");
				field.setAccessible(true);
				field.setInt(Item.getById(322), 6);
			} catch (NoSuchFieldException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if (getConfig().getBoolean("powers")) {
			System.out.println("*********************************************");
			System.out.println("               Powers Mode Enabled");
			System.out.println("*********************************************");
		}

		// we just define this here while we're testing, if we actually
		// accept this feature it'll be moved to somewhere better
		new ServerFakeFreezeTask().runTaskTimerAsynchronously(this, 20L, 20L);
	}

	@Override
	public void onDisable() {
		getEventHandler().saveEvents();

		for (Player player : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
			getPlaytimeMap().playerQuit(player.getUniqueId(), false);
			player.setMetadata("loggedout", new FixedMetadataValue(this, true));
		}

		for (String playerName : PvPClassHandler.getEquippedKits().keySet()) {
			PvPClassHandler.getEquippedKits().get(playerName).remove(getServer().getPlayerExact(playerName));
		}

		for (Entity e : this.combatLoggerListener.getCombatLoggers()) {
			if (e != null) {
				e.remove();
			}
		}

		RedisSaveTask.save(null, false);
		Foxtrot.getInstance().getServerHandler().save();
		Foxtrot.getInstance().getMapHandler().save();
		Foxtrot.getInstance().getMapHandler().getStatsHandler().save();

		RegenUtils.resetAll();

		qLib.getInstance().runRedisCommand((jedis) -> {
			jedis.save();
			return null;
		});
	}

	private void setupHandlers() {
		serverHandler = new ServerHandler();
		mapHandler = new MapHandler();
		mapHandler.load();

		teamHandler = new TeamHandler();
		LandBoard.getInstance().loadFromTeams();

		chatHandler = new ChatHandler();
		citadelHandler = new CitadelHandler();
		powersHandler = new PowersHandler();
		crateHandler = new CrateHandler();
		pvpClassHandler = new PvPClassHandler();
		eventHandler = new EventHandler();
		conquestHandler = new ConquestHandler();
		carePackageHandler = new CarePackageHandler();
		abilityHandler = new AbilityHandler();


		if (getConfig().getBoolean("glowstoneMountain", false)) {
			glowHandler = new GlowHandler();
		}

		if (getConfig().getBoolean("cavern", false)) {
			cavernHandler = new CavernHandler();
		}

		FrozenCommandHandler.registerAll(this);
		for (Class<?> clazz : ClassUtils.getClassesInPackage(this, getClass().getPackage().getName())) {
			if (clazz == null || clazz.getCanonicalName() == null) continue;
			if (clazz.getCanonicalName().contains("conditional")) continue;
			FrozenCommandHandler.registerClass(clazz);
		}

		DeathMessageHandler.init();
		DTRHandler.loadDTR();
	}

	private void setupListeners() {
		getServer().getPluginManager().registerEvents(new MapListener(), this);
		getServer().getPluginManager().registerEvents(new AntiGlitchListener(), this);
		getServer().getPluginManager().registerEvents(new BasicPreventionListener(), this);
		getServer().getPluginManager().registerEvents(new BorderListener(), this);
		getServer().getPluginManager().registerEvents((combatLoggerListener = new CombatLoggerListener()), this);
		getServer().getPluginManager().registerEvents(new CrowbarListener(), this);
		getServer().getPluginManager().registerEvents(new EnchantmentLimiterListener(), this);
		getServer().getPluginManager().registerEvents(new EnderpearlCooldownHandler(), this);
		getServer().getPluginManager().registerEvents(new EndListener(), this);
		getServer().getPluginManager().registerEvents(new PowersListener(), this);
		getServer().getPluginManager().registerEvents(new ElevatorListener(), this);
		getServer().getPluginManager().registerEvents(new FoundDiamondsListener(), this);
		getServer().getPluginManager().registerEvents(new FoxListener(), this);
		getServer().getPluginManager().registerEvents(new GoldenAppleListener(), this);
		getServer().getPluginManager().registerEvents(new KOTHRewardKeyListener(), this);
		getServer().getPluginManager().registerEvents(new PvPTimerListener(), this);
		getServer().getPluginManager().registerEvents(new PotionLimiterListener(), this);
		getServer().getPluginManager().registerEvents(new NetherPortalListener(), this);
		getServer().getPluginManager().registerEvents(new PortalTrapListener(), this);
		getServer().getPluginManager().registerEvents(new SignSubclaimListener(), this);
		getServer().getPluginManager().registerEvents(new SpawnerTrackerListener(), this);
		getServer().getPluginManager().registerEvents(new SpawnListener(), this);
		getServer().getPluginManager().registerEvents(new SpawnTagListener(), this);
		getServer().getPluginManager().registerEvents(new StaffUtilsListener(), this);
		getServer().getPluginManager().registerEvents(new TeamListener(), this);
		getServer().getPluginManager().registerEvents(new WebsiteListener(), this);
		getServer().getPluginManager().registerEvents(new TeamSubclaimCommand(), this);
		getServer().getPluginManager().registerEvents(new TeamClaimCommand(), this);
		getServer().getPluginManager().registerEvents(new StatTrakListener(), this);
		getServer().getPluginManager().registerEvents(new RedstoneListener(), this);
		getServer().getPluginManager().registerEvents(new StrengthListener(), this);

		if (getServerHandler().isReduceArmorDamage()) {
			getServer().getPluginManager().registerEvents(new ArmorDamageListener(), this);
		}

		if (getServerHandler().isBlockEntitiesThroughPortals()) {
			getServer().getPluginManager().registerEvents(new EntityPortalListener(), this);
		}

		if (getServerHandler().isBlockRemovalEnabled()) {
			getServer().getPluginManager().registerEvents(new BlockRegenListener(), this);
		}

		// Register CheatBreaker specific listeners
		if (Bukkit.getPluginManager().getPlugin("UniversalClientAPI") != null && Bukkit.getPluginManager().getPlugin("UniversalClientAPI").isEnabled()) {
			getServer().getPluginManager().registerEvents(new ClientListener(), this);
			Bukkit.getScheduler().runTaskTimer(this, new RallyExpireTask(), 0, 20);
		}

		// Register kitmap specific listeners
		if (getServerHandler().isVeltKitMap() || getMapHandler().isKitMap()) {
			getServer().getPluginManager().registerEvents(new KitMapListener(), this);
			getServer().getPluginManager().registerEvents(new CarePackageHandler(), this);
		}

		getServer().getPluginManager().registerEvents(new BlockConvenienceListener(), this);

		//getServer().getPluginManager().registerEvents(new ChunkLimiterListener(), this );
		//getServer().getPluginManager().registerEvents(new IPListener(), this );
		//getServer().getPluginManager().registerEvents(new Prot3Sharp3Listener(), this);
	}

	private void setupPersistence() {
		(playtimeMap = new PlaytimeMap()).loadFromRedis();
		(oppleMap = new OppleMap()).loadFromRedis();
		(deathbanMap = new DeathbanMap()).loadFromRedis();
		(PvPTimerMap = new PvPTimerMap()).loadFromRedis();
		(startingPvPTimerMap = new StartingPvPTimerMap()).loadFromRedis();
		(deathsMap = new DeathsMap()).loadFromRedis();
		(killsMap = new KillsMap()).loadFromRedis();
		(chatModeMap = new ChatModeMap()).loadFromRedis();
		(toggleGlobalChatMap = new ToggleGlobalChatMap()).loadFromRedis();
		(toggleLFFMessageMap = new ToggleLFFMessageMap()).loadFromRedis();
		(abilityCooldownsMap = new AbilityCooldownsMap()).loadFromRedis();
		(fDisplayMap = new FDisplayMap()).loadFromRedis();

		(teamColorMap = new TeamColorMap()).loadFromRedis();
		(enemyColorMap = new EnemyColorMap()).loadFromRedis();
		(allyColorMap = new AllyColorMap()).loadFromRedis();
		(archerTagColorMap = new ArcherTagColorMap()).loadFromRedis();
		(focusColorMap = new FocusColorMap()).loadFromRedis();

		(staffBoardMap = new StaffBoardMap()).loadFromRedis();
		(classCooldownsMap = new ClassCooldownsMap()).loadFromRedis();
		(fishingKitMap = new FishingKitMap()).loadFromRedis();
		(soulboundLivesMap = new SoulboundLivesMap()).loadFromRedis();
		(friendLivesMap = new FriendLivesMap()).loadFromRedis();
		(chatSpyMap = new ChatSpyMap()).loadFromRedis();
		(diamondMinedMap = new DiamondMinedMap()).loadFromRedis();
		(goldMinedMap = new GoldMinedMap()).loadFromRedis();
		(ironMinedMap = new IronMinedMap()).loadFromRedis();
		(coalMinedMap = new CoalMinedMap()).loadFromRedis();
		(redstoneMinedMap = new RedstoneMinedMap()).loadFromRedis();
		(lapisMinedMap = new LapisMinedMap()).loadFromRedis();
		(emeraldMinedMap = new EmeraldMinedMap()).loadFromRedis();
		(firstJoinMap = new FirstJoinMap()).loadFromRedis();
		(lastJoinMap = new LastJoinMap()).loadFromRedis();
		(wrappedBalanceMap = new WrappedBalanceMap()).loadFromRedis();
		(toggleFoundDiamondsMap = new ToggleFoundDiamondsMap()).loadFromRedis();
		(toggleDeathMessageMap = new ToggleDeathMessageMap()).loadFromRedis();
		(tabListModeMap = new TabListModeMap()).loadFromRedis();
		(cobblePickupMap = new CobblePickupMap()).loadFromRedis();
		(kdrMap = new KDRMap()).loadFromRedis();
	}

}
