/*
 *
 *  ---------------------------------------------------------------------------------------------------------
 *              Titel: PlayerOverview.java
 *             Auteur: BOBBM01
 *    Creatietijdstip: 24-10-2019 14:02
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
package com.maarten551.tictactoe_backend.dto;

import java.util.List;

import com.maarten551.tictactoe_backend.model.Player;

/**
 * @author BOBBM01
 */
public class PlayerOverview {
	public String sessionId;
	public List<Player> players;
}