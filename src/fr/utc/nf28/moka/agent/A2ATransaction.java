package fr.utc.nf28.moka.agent;

/**
 * common Transaction between two agents
 */
public class A2ATransaction {
	public final String mType;
	public Object mContent;

	public A2ATransaction(String type) {
		mType = type;
	}

	public A2ATransaction(String type, Object content) {
		this(type);
		mContent = content;
	}
}
