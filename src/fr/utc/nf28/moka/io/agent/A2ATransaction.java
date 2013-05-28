package fr.utc.nf28.moka.io.agent;

/**
 * common Transaction between two agents
 */
public class A2ATransaction {
	private String mType;
	private Object mContent;
	private String mContentClass;

	public A2ATransaction() {
	}

	public A2ATransaction(String type, Object content) {
		mType = type;
		mContent = content;
		mContentClass = content.getClass().getName();
	}

	public String getType() {
		return mType;
	}

	public void setType(String type) {
		mType = type;
	}

	public Object getContent() {
		return mContent;
	}

	public void setContent(Object content) {
		mContent = content;
	}

	public String getContentClass() {
		return mContentClass;
	}

	public void setContentClass(String contentClass) {
		mContentClass = contentClass;
	}
}
