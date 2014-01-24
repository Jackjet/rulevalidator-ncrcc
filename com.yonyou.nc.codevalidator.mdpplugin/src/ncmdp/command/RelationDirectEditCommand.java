package ncmdp.command;

import ncmdp.model.Relation;

import org.eclipse.gef.commands.Command;

public class RelationDirectEditCommand extends Command {
	private Relation relation = null;
	private String oldStr = null;
	private String newValue = null;
	private String propid = null;
	public RelationDirectEditCommand(Relation relation , String newValue, String propid) {
		super();
		this.relation = relation;
		this.newValue = newValue;
		this.propid = propid;
	}
	@Override
	public boolean canExecute() {
		return super.canExecute();
	}
	@Override
	public void execute() {
		if(Relation.SOURCE_CONSTRAINT.equals(propid)){
			oldStr = relation.getSourceConstraint();
		}else if(Relation.TARGET_CONSTRAINT.equals(propid)){
			oldStr = relation.getTargetConstraint();
		}
		redo();
	}
	@Override
	public void redo() {
		if(Relation.SOURCE_CONSTRAINT.equals(propid)){
			relation.setSourceConstraint(newValue);
		}else if(Relation.TARGET_CONSTRAINT.equals(propid)){
			relation.setTargetConstraint(newValue);
		}
		
	}
	@Override
	public void undo() {
		if(Relation.SOURCE_CONSTRAINT.equals(propid)){
			relation.setSourceConstraint(oldStr);
		}else if(Relation.TARGET_CONSTRAINT.equals(propid)){
			relation.setTargetConstraint(oldStr);
		}

	}
	
}
