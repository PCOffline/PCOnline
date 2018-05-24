package EB.SampleBot;

import java.awt.Color;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class App extends ListenerAdapter {

	// Objects

	public static String botName = "PCOnline";
	public static String version = "1.0.5";
	public static String versionExtra = "Stable";

	// Config Values
	public static String prefix = ",";
	public static int waitTime = 5;
	public static String defaultChannel = "pconline";

	// Commands of other Bots
	public static String[] wynnstats = { "/stats", "/pstats", "/find", "/help", "/info", "/change", "/item",
			"/pdefault", "/territories", "/ping", "/tinfo", "/gdefault", "/leaderboard", "/pimg", "/duel", "/chance",
			"/mythic", "/war", "/count", "/botrole", "/restrict", "/togglefeed", "/guildimg", "/logonfeed", "/toggle" };
	public static String[] PCOnline = { prefix + "waffle", prefix + "search", prefix + "config", prefix + "waffles",
			prefix + "info", prefix + "help", prefix + "search", prefix + "announce", prefix + "time",
			prefix + "changelog", prefix + "funfact", prefix + "derp", prefix + "config" };
	public static String[] Pollerino = { "+poll", "+strawpoll", "+updates", "+invite", "+donate" };
	public static String[] moozic = { "$play", "$p", "$queue", "$q", "$nowplaying", "$np", "skip", "$s", "$v",
			"$voteskip", "$stop", "$st", "$pause", "$resume", "$join", "$j", "$leave", "$lv", "$repeat", "$rep",
			"$shuffle", "$reshuffle", "$resh", "$sh", "$fwd", "$rew", "$restart", "$h", "$history", "$export", "$ex",
			"$split", "$volume", "$vol", "$help" };
	public static String[] manager = { "%ping", "%help", "%find", "%test", "%online", "%info", "%invite", "%cflush",
			"%find", "%flush", "%stats", "%whois", "%server", "ship", "%summon", "%create", "%revoke", "%delete",
			"%desummon", "%add", "%allow", "%unadd", "%rem", "%remove", "%block", "%lock", "%unlock", "%name",
			"%rename", "%setname", "%collage", "%icon", "%avatar", "%blur", "%greyscale", "%flip", "grayscale", "%gray",
			"%grey", "%fliph", "%flipv", "flop", "%real", "%invert", "%pixelate", "%posterize", "%sepia", "%contrast" };

	public static String token = "NDQ1MTQ5NDg2NzcyMzg3ODUx.DehPeg.Y12dc8UNslS0TXunP87ZTwCbZUM";

	static Timer timer = new Timer();
	static boolean timerPassed = true;

	private static final int millisecond = 60 * 1000; // Millisecond to Minute

	Calendar calendar;

	private static boolean awaitingTime = false;
	private static int readyToCancel = 0;
	private static String msgID;
	private static boolean continueTime = false;
	private static String userID;

	/**
	 * Validate that every messages sent in a bot channel is a command, related to
	 * that channel
	 *
	 * @param commands
	 *            all the valid commands
	 * @param channelName
	 *            token of the channel
	 * @param botToken
	 *            token of the bot (to ignore its messages)
	 * @param e
	 *            event handler that receives the messages
	 */

	public static void botChannelValidation(String[] commands, String channelName, MessageReceivedEvent e) {

		// Declaration
		Message objMsg = e.getMessage();
		MessageChannel objChannel = e.getChannel();
		User objUser = e.getAuthor();

		// Run through each keyword, in the end (to prevent spam)
		// If no matching keyword was found, delete the message and send am embed
		if (objChannel.getName().equals(channelName))
			if (objUser.isBot()) // If the sender is bot, it's probably okay
				return; // You have nothing to do here, return to where you belong!
			else
				for (int i = 0; i < commands.length; i++) {
					if (objMsg.getContentRaw().toLowerCase().contains(commands[i].toLowerCase())
							|| objMsg.getContentRaw().equalsIgnoreCase(commands[i].toLowerCase()))
						return;
					if (i == commands.length - 1) {
						if (timerPassed) {
							objChannel.sendMessage(wrapMessageInEmbed(
									"Message by " + objUser.getAsMention()
											+ " has been deleted. \nReason: Invalid command of WynnStats bot", // Content
									"", null, // Footer (Message Deleted)
									"Message Deleted", // Title
									Color.RED, // Color
									objMsg.getChannel())).queue(); // Channel and Put in Queue
							timerPassed = false;
						}
						objMsg.delete().queue();
						return;
					}

				}
	}

	public static void main(String[] args) throws Exception {
		JDA jdaBot = new JDABuilder(AccountType.BOT).setToken(token).buildBlocking();
		jdaBot.addEventListener(new App());
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (!timerPassed)
					timerPassed = true;
			}
		}, waitTime * millisecond);

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (awaitingTime)
					readyToCancel++;
				if (readyToCancel >= 120)
					awaitingTime = false;
				readyToCancel = 0;
			}
		}, 1 / 60 * millisecond);
	}

	/**
	 * Generates an Embed message
	 *
	 * @param content
	 *            content of the embed
	 * @param footer
	 *            footer of the embed (subtitle)
	 * @param footerURL
	 *            image of the footer
	 * @param title
	 *            title of the embed
	 * @param color
	 *            side-color of the embed
	 * @param msgChannel
	 *            channel to send the embed in
	 * @return the embed message
	 * @category MessageTypes
	 */

	public static MessageEmbed wrapMessageInEmbed(String content, String footer, String footerURL, String title,
			Color color, MessageChannel msgChannel) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(botName);
		eb.setFooter(footer, footerURL);
		eb.setColor(color);
		eb.setDescription(content);
		eb.setTitle(title);
		return eb.build();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent e) {

		Message objMsg = e.getMessage();
		MessageChannel objChannel = e.getChannel();
		User objUser = e.getAuthor();

		botChannelValidation(wynnstats, "wynnstats", e);
		botChannelValidation(PCOnline, defaultChannel, e);
		botChannelValidation(Pollerino, "pollerino", e);
		botChannelValidation(moozic, "moozic", e);
		botChannelValidation(manager, "manager", e);

		// Waffle

		if (checkCommand("waffle", e) || checkCommand("waffles", e)) {
			objChannel.sendMessage("Who loves waffles? " + objUser.getAsMention() + " does!").queue();
			return;
		}

		// Info

		if (checkCommand("info", e))
			objChannel.sendMessage(
					wrapMessageInEmbed("Version: " + version + " " + versionExtra + "\n" + "Prefix: " + prefix, "",
							null, "Information about " + botName, Color.blue, objChannel))
					.queue();

		if (objMsg.getContentRaw().startsWith(prefix + "time") && objChannel.getName().equals(defaultChannel)) {
			objChannel.sendMessage(wrapMessageInEmbed(":regional_indicator_U: - Get time of UTC time zone\n"
					+ ":regional_indicator_G: - Get time of GMT time zone\n"
					+ ":regional_indicator_S: - Get time of a state/country/city\n" + ":regional_indicator_X: - Cancel",
					"react with U, G, S or X", null, "React With the Letter of Your Choice", Color.ORANGE, objChannel))
					.queue();
			awaitingTime = true;
			msgID = objMsg.getId();
			userID = objUser.getId();
			readyToCancel = 0;

			try {
				if (continueTime && userID.equalsIgnoreCase(objUser.getId())
						&& objChannel.getName().equalsIgnoreCase(defaultChannel))
					calendar = Calendar.getInstance(TimeZone.getTimeZone(objMsg.getContentRaw()));
				objChannel.sendMessage(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE))
						.queue();
			} catch (NullPointerException ex) {

			}
		}

	}

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent e) {
		if (awaitingTime)
			if (e.getMessageId().equalsIgnoreCase(msgID) && e.getUser().getId().equalsIgnoreCase(userID))
				switch (e.getReactionEmote().getName()) {
				case "\ud83c\uddfa":
					continueTime(0, e);
					return;
				case "\ud83c\uddfd":
					continueTime(3, e);
					return;
				case "\ud83c\uddec":
					continueTime(1, e);
					return;
				case "\ud83c\uddf8":
					continueTime(2, e);
					return;
				default:
					continueTime(-1, e);
					return;
				}
		awaitingTime = false;
	}

	private void continueTime(int o, MessageReactionAddEvent e) {
		if (awaitingTime) {
			continueTime = true;
			switch (o) {
			case 0:
				e.getChannel().sendMessage("Type in a UTC Time Zone\n" + "(e.g.) UTC+3").queue();
				return;
			case 1:
				e.getChannel().sendMessage("Type in a GMT Time Zone\n" + "(e.g. GMT+3)").queue();
				return;
			case 2:
				e.getChannel().sendMessage("Type in a State/Country/City\n" + "(e.g. USA)").queue();
				return;
			case 3:
				e.getChannel().sendMessage("Command Successfully Cancelled").queue();
				continueTime = false;
				return;
			default:
				e.getChannel().sendMessage("Invalid Reaction").queue();
				return;
			}
		}
	}

	/**
	 *
	 * @param command
	 *            command to look for
	 * @param e
	 *            event handler, reads messages
	 * @return true if the command was called
	 * @category Read Messages
	 */

	public static boolean checkCommand(String command, String[] channels, MessageReceivedEvent e) {

		// Declarations
		Message objMsg = e.getMessage();
		MessageChannel objChannel = e.getChannel();

		int option = -1;

		if (channels[0] == "" || channels[0] == null || channels[0].equals("") || channels[0].equals(null)
				|| channels[0].isEmpty())
			option = 0;

		// Check for command
		if (objMsg.getContentRaw().equalsIgnoreCase(prefix + command))
			switch (option) {
			case -1:
				return true;
			case 0:
				for (String channel : channels)
					if (objChannel.getName().equalsIgnoreCase(channel))
						return true;
				return false;
			}
		return false;

	}

	public static boolean checkCommand(String command, String channel, MessageReceivedEvent e) {

		// Declarations
		Message objMsg = e.getMessage();
		MessageChannel objChannel = e.getChannel();

		// Check for command
		if (objMsg.getContentRaw().equalsIgnoreCase(prefix + command)) {
			if (channel == "" || channel.isEmpty() || channel == null || channel.equals(null) || channel.equals(""))
				return true;
			if (channel.equals(objChannel.getName()))
				return true;
			return false;
		}
		return false;
	}

	public static boolean checkCommand(String command, MessageReceivedEvent e) {

		Message objMsg = e.getMessage();
		MessageChannel objChannel = e.getChannel();

		if (objMsg.getContentRaw().equalsIgnoreCase(prefix + command))
			if (objChannel.getName().equals(defaultChannel))
				return true;
		return false;
	}

}
