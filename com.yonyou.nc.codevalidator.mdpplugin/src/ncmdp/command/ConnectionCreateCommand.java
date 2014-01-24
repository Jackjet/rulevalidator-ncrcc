package ncmdp.command;

import java.lang.reflect.Constructor;
import java.util.List;

import ncmdp.model.AggregationRelation;
import ncmdp.model.BusiItfImplConnection;
import ncmdp.model.BusinInterface;
import ncmdp.model.Cell;
import ncmdp.model.Connection;
import ncmdp.model.ConvergeConnection;
import ncmdp.model.DependConnection;
import ncmdp.model.Entity;
import ncmdp.model.Enumerate;
import ncmdp.model.ExtendConnection;
import ncmdp.model.Note;
import ncmdp.model.NoteConnection;
import ncmdp.model.Reference;
import ncmdp.model.Relation;
import ncmdp.model.ValueObject;
import ncmdp.util.MDPLogger;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
/**
 * 创建连线
 * @author wangxmn
 *
 */
public class ConnectionCreateCommand extends Command {
	private Connection conn = null;

	private Cell source = null;

	private Cell target = null;

	private Class<Connection> conCls = null;

	private Constructor<Connection> constructor = null;

	public ConnectionCreateCommand(Cell source, Class<Connection> conCls) {
		super();
		this.source = source;
		this.conCls = conCls;
		try {
			constructor = conCls.getConstructor(new Class[] { Cell.class, Cell.class });
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(), e);
		}
	}

	public void setTarget(Cell target) {
		this.target = target;
	}

	@Override
	public boolean canExecute() {
		//对于业务接口，只能作为目标，并只能用于接口连接连接实体或值对象,且只能和一个节点相连
		if (source instanceof BusinInterface
				|| (source instanceof Reference && ((Reference) source).getReferencedCell() instanceof BusinInterface)) {
			return false;
		} else if (target instanceof BusinInterface
				|| (target instanceof Reference && ((Reference) target).getReferencedCell() instanceof BusinInterface)) {
			//当目标地址是业务接口时，连线必须是实现类型的
			if (!BusiItfImplConnection.class.equals(this.conCls) || !(source instanceof ValueObject)) { return false; }
		}
		//枚举只能和注释建立连接
		if ((source instanceof Enumerate || target instanceof Enumerate)
				&& !NoteConnection.class.equals(conCls)) { return false; }
		//源不能为reference
		if (source instanceof Reference) { return false; }
		//对于关联和聚合,源和目标才能相同
		if (source.equals(target)) {
			if (!(Relation.class.equals(this.conCls) || AggregationRelation.class.equals(this.conCls))) { return false; }
		}
		//扩展关系只能在实体之间或值对象之间建立
		if (ExtendConnection.class.equals(conCls)) {
			if (!((source instanceof Entity && (target instanceof Entity || (target instanceof Reference && ((Reference) target)
					.getReferencedCell() instanceof Entity))) || (source instanceof ValueObject && (target instanceof ValueObject || (target instanceof Reference && ((Reference) target)
					.getReferencedCell() instanceof ValueObject))))) { return false; }
		}
		//对于注释连接，有且仅有一个节点为注释节点, 且两个节点之间，只能由一个注释连接
		if (((source instanceof Note) ^ (target instanceof Note))) {
			if (!NoteConnection.class.equals(conCls))
				return false;
		}
		if (NoteConnection.class.equals(conCls)) {
			if (!isSingleCon(this.conCls, source, target) || !isSingleCon(this.conCls, target, source)) { return false; }
		}
		//对于继承，聚合，依赖，两个单元之间的关联关系只能建立一次
		if (ExtendConnection.class.equals(this.conCls) || DependConnection.class.equals(this.conCls)
				|| ConvergeConnection.class.equals(this.conCls)) {
			if (!isSingleCon(this.conCls, source, target)) { return false; }
		}
		return true;
	}

	/**
	 * 表示sourceCell和targetCell是否已经有了一次关联关系了
	 * @param connCls
	 * @param sourCell
	 * @param targetCell
	 * @return
	 */
	private static boolean isSingleCon(Class<Connection> connCls, Cell sourCell, Cell targetCell) {
		if (sourCell == null)
			return true;
		List<Connection> sourceConnections = sourCell.getSourceConnections();
		int count = sourceConnections == null ? 0 : sourceConnections.size();
		for (int i = 0; i < count; i++) {
			Connection relation = sourceConnections.get(i);
			if (relation.getClass().equals(connCls) && relation.getTarget().equals(targetCell)) { return false; }
		}
		return true;
	}

	@Override
	public void execute() {
		try {
			//传入原目标和目的目标，创建一个Connection，然后调用connection的connect方法来创建连接
			conn = (Connection) constructor.newInstance(new Object[] { source, target });
			conn.setCreateIndustry(source.getCreateIndustry());//设置源目标的行业编码
			if (source.equals(target)) {
				Point p = source.getLocation();
				Dimension size = source.getSize();
				Point p1 = new Point(p.x + size.width / 2, p.y - 20);
				Point p2 = new Point(p.x + size.width + 20, p.y - 20);
				Point p3 = new Point(p.x + size.width + 20, p.y + size.height / 2);
				conn.addBendPoint(0, p1);
				conn.addBendPoint(1, p2);
				conn.addBendPoint(2, p3);
			}
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(), e);
		}
		conn.createConn();//说实话，这句话放着其实没啥用呢
	}

	@Override
	public void redo() {
		conn.connect();
	}

	@Override
	public void undo() {
		conn.disConnect();
	}

}
