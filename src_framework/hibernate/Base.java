/*package hibernate;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import win.qianheng.entity.Customer;
import win.qianheng.entity.HibernateSessionFactory;
public class Base {
	public void addCustomer(Customer customer) {
		Session session=HibernateSessionFactory.getSession();
		Transaction trs=session.beginTransaction();
		try {
			session.save(customer);
			trs.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(null!=trs)trs.rollback();
		}finally{
			if(null!=session)session.close();
		}
	}

	public void deleteCustomer(Customer customer) {
		Session session=HibernateSessionFactory.getSession();
		Transaction trs=session.beginTransaction();
		try{
			session.delete(customer);
			trs.commit();
		}catch(Exception e){
			if(null!=trs)trs.rollback();
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
	}

	public void updateCustomer(Customer customer) {
		Session session=HibernateSessionFactory.getSession();
		Transaction trs=session.beginTransaction();
		try{
			session.update(customer);
			trs.commit();
		}catch(Exception e){
			if(null!=trs)trs.rollback();
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
	}

	public Customer getCustomerById(BigDecimal id) {
		Session session=HibernateSessionFactory.getSession();
		Customer customer;
		try {
// load方法认为该数据在数据库中一定存在，可以放心的使用代理来延迟加载，如果在使用过程中发现了问题，只能抛异常；而对于get方法，hibernate一定要获取到真实的数据，否则返回null
			customer = (Customer)session.get(Customer.class, id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}finally{
			if(null!=session)session.close();
		}
		return customer;

	}

	public List<Customer> getAllCustomer() {
		Session session=HibernateSessionFactory.getSession();
		List<Customer> list=null;
		try {
			Query query=session.createQuery("from Customer");
			list=query.list();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return list;
	}
}
*/