package fr.utc.nf28.moka.agent;

/**
 * common Transaction between two agents
 */
public class A2ATransaction {
	private String mType;
	private Object mContent;
	private String mContentClass;

	public A2ATransaction(){
	}

	public A2ATransaction(String type, Object content) {
		this.mType = type;
		this.mContent = content;
		this.mContentClass = content.getClass().getName();
	}

	public void setType(String type){
		this.mType = type;
	}

	public String getType(){
		return mType;
	}

	public void setContent(Object content){
		this.mContent = content;
	}

	public Object getContent(){
		return mContent;
	}

	public void setContentClass(String contentClass) {
		this.mContentClass = contentClass;
	}

	public String getContentClass() {
		return mContentClass;
	}
}
