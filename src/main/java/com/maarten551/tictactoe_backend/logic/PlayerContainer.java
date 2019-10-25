/*
 *
 *  ---------------------------------------------------------------------------------------------------------
 *              Titel: PlayerContainer.java
 *             Auteur: BOBBM01
 *    Creatietijdstip: 24-10-2019 09:58
 *          Copyright: (c) 2019 Belastingdienst / Centrum voor Applicatieontwikkeling en Onderhoud,
 *                     All Rights Reserved.
 *  ---------------------------------------------------------------------------------------------------------
 *                                              |   Unpublished work. This computer program includes
 *     De Belastingdienst                       |   Confidential, Properietary Information and is a
 *     Postbus 9050                             |   trade Secret of the Belastingdienst. No part of
 *     7300 GM  Apeldoorn                       |   this file may be reproduced or transmitted in any
 *     The Netherlands                          |   form or by any means, electronic or mechanical,
 *     http://belastingdienst.nl/               |   for the purpose, without the express written
 *                                              |   permission of the copyright holder.
 *  ---------------------------------------------------------------------------------------------------------
 *
 */
package com.maarten551.tictactoe_backend.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import com.maarten551.tictactoe_backend.model.Player;

@Service
public class PlayerContainer {
	private Map<String, Player> playersBySessionId;

	public PlayerContainer() {
		this.playersBySessionId = new HashMap<>();
	}

	/**
	 * Finds player by sessionId, if no player is found, create the player.
	 */
	public Player getPlayerBySessionId(String sessionId) {
		if (this.playersBySessionId.containsKey(sessionId)) {
			return this.playersBySessionId.get(sessionId);
		}

		Player newPlayer = new Player(sessionId);
		this.playersBySessionId.put(sessionId, newPlayer);

		return newPlayer;
	}

	public List<Player> getAllPlayers() {
		return new ArrayList<>(this.playersBySessionId.values());
	}

	public void removePlayerFromApplicationAndCleanup(String sessionId) {
		this.playersBySessionId.remove(sessionId);
	}
}
