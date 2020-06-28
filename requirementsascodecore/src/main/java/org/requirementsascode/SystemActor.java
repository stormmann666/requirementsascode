package org.requirementsascode;

class SystemActor extends Actor{
	private static final long serialVersionUID = 1598263321163535293L;

	public SystemActor() {
		super("System");
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SystemActor other = (SystemActor) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}
}
