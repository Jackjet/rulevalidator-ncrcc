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
 * ��������
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
		//����ҵ��ӿڣ�ֻ����ΪĿ�꣬��ֻ�����ڽӿ���������ʵ���ֵ����,��ֻ�ܺ�һ���ڵ�����
		if (source instanceof BusinInterface
				|| (source instanceof Reference && ((Reference) source).getReferencedCell() instanceof BusinInterface)) {
			return false;
		} else if (target instanceof BusinInterface
				|| (target instanceof Reference && ((Reference) target).getReferencedCell() instanceof BusinInterface)) {
			//��Ŀ���ַ��ҵ��ӿ�ʱ�����߱�����ʵ�����͵�
			if (!BusiItfImplConnection.class.equals(this.conCls) || !(source instanceof ValueObject)) { return false; }
		}
		//ö��ֻ�ܺ�ע�ͽ�������
		if ((source instanceof Enumerate || target instanceof Enumerate)
				&& !NoteConnection.class.equals(conCls)) { return false; }
		//Դ����Ϊreference
		if (source instanceof Reference) { return false; }
		//���ڹ����;ۺ�,Դ��Ŀ�������ͬ
		if (source.equals(target)) {
			if (!(Relation.class.equals(this.conCls) || AggregationRelation.class.equals(this.conCls))) { return false; }
		}
		//��չ��ϵֻ����ʵ��֮���ֵ����֮�佨��
		if (ExtendConnection.class.equals(conCls)) {
			if (!((source instanceof Entity && (target instanceof Entity || (target instanceof Reference && ((Reference) target)
					.getReferencedCell() instanceof Entity))) || (source instanceof ValueObject && (target instanceof ValueObject || (target instanceof Reference && ((Reference) target)
					.getReferencedCell() instanceof ValueObject))))) { return false; }
		}
		//����ע�����ӣ����ҽ���һ���ڵ�Ϊע�ͽڵ�, �������ڵ�֮�䣬ֻ����һ��ע������
		if (((source instanceof Note) ^ (target instanceof Note))) {
			if (!NoteConnection.class.equals(conCls))
				return false;
		}
		if (NoteConnection.class.equals(conCls)) {
			if (!isSingleCon(this.conCls, source, target) || !isSingleCon(this.conCls, target, source)) { return false; }
		}
		//���ڼ̳У��ۺϣ�������������Ԫ֮��Ĺ�����ϵֻ�ܽ���һ��
		if (ExtendConnection.class.equals(this.conCls) || DependConnection.class.equals(this.conCls)
				|| ConvergeConnection.class.equals(this.conCls)) {
			if (!isSingleCon(this.conCls, source, target)) { return false; }
		}
		return true;
	}

	/**
	 * ��ʾsourceCell��targetCell�Ƿ��Ѿ�����һ�ι�����ϵ��
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
			//����ԭĿ���Ŀ��Ŀ�꣬����һ��Connection��Ȼ�����connection��connect��������������
			conn = (Connection) constructor.newInstance(new Object[] { source, target });
			conn.setCreateIndustry(source.getCreateIndustry());//����ԴĿ�����ҵ����
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
		conn.createConn();//˵ʵ������仰������ʵûɶ����
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
