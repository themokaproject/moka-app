package fr.utc.nf28.moka.agent;

import android.util.Log;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class ReceptionBehaviour extends Behaviour {

	/**
	 * LogCat tag
	 */
	private static final String TAG = makeLogTag(ReceptionBehaviour.class);

	@Override
	public void action() {
		final ACLMessage message = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
		if (message != null) {
			final String content = message.getContent();
			Log.i(TAG, content);
		} else {
			block();
		}
	}

	@Override
	public boolean done() {
		return false;
	}
}
