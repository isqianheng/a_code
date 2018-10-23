/*package hibernate;
import java.util.Arrays;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import cn.jbit.entity.Dept;
import cn.jbit.entity.Emp;
import cn.jbit.entity.HibernateSessionFactory;
public class Query {

	*//**
	 * @param args
	 *//*
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//baseQuery();
		//baseQuery2(800.0, 1500.0,"CLERK");
		//baseQuery2();
		//baseQuery8();
		//baseQuery9(3,2);
		//baseQuery13();
		testNamedQuery();

	}
	
	public static void testNamedQuery(){
		Session session=HibernateSessionFactory.getSession();		
		try {
			Query query=session.getNamedQuery("getAllEmpsBySal");
			query.setDouble(0, 1000.0);
            List<Emp> list=query.list();
           for (Emp emp : list) {
			System.out.println(emp.getEname()+","+emp.getDept().getDname()+","+emp.getSal());
		}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
	public static void baseQuery13(){
		Session session=HibernateSessionFactory.getSession();
		String hqlString="from Emp e inner join  fetch e.dept";//����������
		try {
			Query query=session.createQuery(hqlString);			
            List<Emp> list=query.list();
           for (Emp emp : list) {
			System.out.println(emp.getEname()+","+emp.getDept().getDname());
		}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
	
	
	public static void baseQuery12(){
		Session session=HibernateSessionFactory.getSession();
		String hqlString="from Emp e left outer join e.dept";
		try {
			Query query=session.createQuery(hqlString);			
            List<Object[]> list=query.list();
            for (Object[] objs: list) {
				Emp emp=(Emp)objs[0];
				Dept dept=(Dept)objs[1];
				System.out.println(emp.getEname()+","+dept.getDname());
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
	
	
	public static void baseQuery11(){
		Session session=HibernateSessionFactory.getSession();
		String hqlString="from Emp e inner join e.dept";
		try {
			Query query=session.createQuery(hqlString);			
            List<Object[]> list=query.list();
            for (Object[] objs: list) {
				Emp emp=(Emp)objs[0];
				Dept dept=(Dept)objs[1];
				System.out.println(emp.getEname()+","+dept.getDname());
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
	
	
	public static void baseQuery10(){
		Session session=HibernateSessionFactory.getSession();
		String hqlString="from Emp where dept.dname=? ";//��ʽ����
		try {
			Query query=session.createQuery(hqlString);	
			query.setString(0, "RESEARCH");
            List<Emp> list=query.list();
            for (Emp emp : list) {
				System.out.println(emp.getEname()+","+emp.getSal()+","+emp.getDept().getDname());
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
	//分页
	public static void baseQuery9(int pageSize,int page){
		Session session=HibernateSessionFactory.getSession();
		String hqlString="from Emp ";
		try {
			Query query=session.createQuery(hqlString);	
			query.setFirstResult((page-1)*pageSize);  
			query.setMaxResults(pageSize);
            List<Emp> list=query.list();
            for (Emp emp : list) {
				System.out.println(emp.getEname()+","+emp.getSal());
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
	
	public static void baseQuery8(){
		Session session=HibernateSessionFactory.getSession();
		String hqlString="from Emp  order by sal ";
		try {
			Query query=session.createQuery(hqlString);			
            List<Emp> list=query.list();
            for (Emp emp : list) {
				System.out.println(emp.getEname()+","+emp.getSal());
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
	
	public static void baseQuery7(){
		Session session=HibernateSessionFactory.getSession();
		String hqlString="select max(sal),min(sal) from Emp ";
		try {
			Query query=session.createQuery(hqlString);			
            Object[] count=(Object[])query.uniqueResult();
             System.out.println(Arrays.toString(count));
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
	
	
	public static void baseQuery6(){
		Session session=HibernateSessionFactory.getSession();
		String hqlString="select count(*) from Emp ";
		try {
			Query query=session.createQuery(hqlString);			
            Long count=(Long)query.uniqueResult();
             System.out.println(count);
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
	
	
	public static void baseQuery5(){
		Session session=HibernateSessionFactory.getSession();
		String hqlString="from Emp where ename=?";
		try {
			Query query=session.createQuery(hqlString);
			query.setString(0, "SMITH");
			Emp emp=(Emp)query.uniqueResult();
			System.out.println(emp);
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
	
	
	public static void baseQuery4(){
		Session session=HibernateSessionFactory.getSession();
		String hqlString="select new Emp(ename,sal) from Emp";
		try {
			Query query=session.createQuery(hqlString);
			List<Emp> list=query.list();
			for (Emp emp: list) {
				System.out.println(emp);
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
	
	public static void baseQuery3(){ 
		Session session=HibernateSessionFactory.getSession();
		String hqlString="select ename,sal from Emp";
		try {
			Query query=session.createQuery(hqlString);
			List<Object[]> list=query.list();
			for (Object[] objects : list) {
				System.out.println(objects[0]+","+objects[1]);
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
	
	public static void baseQuery2(){//֧�ֶ�̬��ѯ 
		Session session=HibernateSessionFactory.getSession();
		String hqlString="from java.io.Serializable";
		try {
			Query query=session.createQuery(hqlString);
			List  list=query.list();
			for (Object object : list) {
				System.out.println(object);
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
	
	public static void baseQuery2(Double minSal,Double maxSal,String job){
		Session session=HibernateSessionFactory.getSession();
		String hqlString="from Emp where job=:job and sal between :minSal and :maxSal ";
		try {
			Query query=session.createQuery(hqlString);
			query.setDouble("minSal", minSal);
			query.setDouble("maxSal", maxSal);
			query.setString("job", job);
			List<Emp> list=query.list();
			for (Emp emp : list) {
				System.out.println(emp.getJob()+","+emp.getEname()+","+emp.getSal());
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
	public static void baseQuery(){
		Session session=HibernateSessionFactory.getSession();
		String hqlString="from Emp";
		try {
			Query query=session.createQuery(hqlString);
			List<Emp> list=query.list();
			for (Emp emp : list) {
				System.out.println(emp);
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}

}
*/