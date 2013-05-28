package fr.utc.nf28.moka.io.agent;

import java.util.ArrayList;

import fr.utc.nf28.moka.util.JadeUtils;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

/**
 * A base agent that can register skills
 * and retrieve agents based on their skills
 */
public class BaseAgent extends Agent {
	private final ServiceDescription mServiceDescription = new ServiceDescription();
	private final ArrayList<AID> mResults = new ArrayList<AID>();

	/**
	 * Retrieve all the agents AID that have a skill
	 * that matches the name
	 *
	 * @param skillName
	 * @return an arrayList of AIDs
	 */
	protected ArrayList<AID> getAgentsWithSkill(String skillName) {
		return getAgentsWithSkill(skillName, JadeUtils.JADE_SKILL_TYPE_DEFAULT);
	}

	/**
	 * Retrieve all the agents AID that have a skill
	 * that matches the name and the type
	 *
	 * @param skillName
	 * @param skillType
	 * @return an arrayList of AIDs
	 */
	protected ArrayList<AID> getAgentsWithSkill(String skillName, String skillType) {
		mResults.clear();
		try {
			final DFAgentDescription[] agentDescriptions = DFService.search(this, getAgentDescriptionWithService(skillName, skillType));
			for (DFAgentDescription ad : agentDescriptions) {
				mResults.add(ad.getName());
			}
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		return mResults;
	}

	protected AID getFirstAgentWithSkill(String skillName, String skillType) {
		return getAgentsWithSkill(skillName, skillType).get(0);
	}

	protected AID getFirstAgentWithSkill(String skillName) {
		return getAgentsWithSkill(skillName).get(0);
	}

	/**
	 * Register a skill with a name
	 *
	 * @param skillName
	 */
	protected void registerSkill(String skillName) {
		// use a default type
		// type is a mandatory field of a service-description
		registerSkill(skillName, JadeUtils.JADE_SKILL_TYPE_DEFAULT);
	}

	/**
	 * Register a skill with a name and a type
	 *
	 * @param skillName
	 * @param skillType
	 */
	protected void registerSkill(String skillName, String skillType) {
		try {
			DFService.register(this, getAgentDescriptionWithService(skillName, skillType));
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}

	/**
	 * send message with REQUEST performatif
	 *
	 * @param receivers use getAgentsWithSkill
	 * @param content   message content
	 */
	protected void sendRequestMessage(ArrayList<AID> receivers, String content) {
		final ACLMessage connectionMessage = new ACLMessage(ACLMessage.REQUEST);
		for (AID receiver : receivers) {
			connectionMessage.addReceiver(receiver);
		}
		connectionMessage.setContent(content);
		send(connectionMessage);
	}

	private DFAgentDescription getAgentDescriptionWithService(String skillName, String skillType) {
		final DFAgentDescription dfAgentDescription = new DFAgentDescription();
		mServiceDescription.setName(skillName);
		mServiceDescription.setType(skillType);
		dfAgentDescription.addServices(mServiceDescription);
		return dfAgentDescription;
	}
}
