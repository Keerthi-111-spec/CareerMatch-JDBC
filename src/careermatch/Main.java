package careermatch;
import java.sql.*;
import java.util.*;
public class Main {
	static Scanner sc=new Scanner(System.in);
	public static void registerStudent() throws Exception{
		Connection con=DBConnection.getConnection();
		System.out.println("Student ID:");
		int id=sc.nextInt();
		sc.nextLine();
		System.out.println("Name:");
		String name=sc.nextLine();
		//sc.nextLine();
		System.out.println("CGPA:");
		double cgpa=sc.nextDouble();
		sc.nextLine();
		System.out.println("Skills:");
		String skills=sc.nextLine();
		
		PreparedStatement ps=con.prepareStatement("insert into students values(?,?,?,?)");
		ps.setInt(1,id);
		ps.setString(2,name);
		ps.setDouble(3,cgpa);
		ps.setString(4,skills);
		ps.executeUpdate();
		System.out.println("\nStudent Added");
		ps.close();
		con.close();
	}
	public static void addCompany() throws Exception{
		Connection con=DBConnection.getConnection();
		System.out.println("Company name:");
		String company=sc.nextLine();
		//sc.nextLine();
		System.out.println("Minimum cgpa:");
		Double cgpa=sc.nextDouble();
		sc.nextLine();
		System.out.println("Required skills:");
		String skills=sc.nextLine();
		PreparedStatement ps=con.prepareStatement("insert into companies(company_name,min_cgpa,required_skills)values(?,?,?)");
		ps.setString(1,company);
		ps.setDouble(2, cgpa);
		ps.setString(3,skills);
		ps.executeUpdate();
		System.out.println("Company added");
		ps.close();
		con.close();
	}
	public static void checkEligibility() throws Exception{
		Connection con=DBConnection.getConnection();
		System.out.println("Student ID:");
		int id=sc.nextInt();
		PreparedStatement ps=con.prepareStatement("select * from students where student_id=?");
		ps.setInt(1, id);;
		ResultSet student=ps.executeQuery();
		if(!student.next()) {
			System.out.println("student not found!");
			student.close();
			ps.close();
			con.close();
			return;
		}
		double cgpa=student.getDouble("cgpa");
		String skills=student.getString("skills").replace(",", " ").toLowerCase();
		Statement st=con.createStatement();
		ResultSet company=st.executeQuery("select * from companies");
		while(company.next()) {
			String cname=company.getString("company_name");
			double req=company.getDouble("min_cgpa");
			String reqSkills=company.getString("required_skills").replace(",", " ").toLowerCase();
			if(cgpa>=req && skills.contains(reqSkills)){
				System.out.println("\n Eligible for "+cname);
			}
			else {
				System.out.println("\nNot Eligible for "+cname);
				if(!skills.contains(reqSkills)){
			        System.out.println("Missing Skill: "+ reqSkills);
			    }
			}
		}
		company.close();
		student.close();
	    st.close();
	    ps.close();
	    con.close();
	}
	public static void apply() throws Exception {
	    Connection con = DBConnection.getConnection();
	    System.out.println("Student ID:");
	    int id = sc.nextInt();
	    sc.nextLine();
	    System.out.println("Company Name:");
	    String company = sc.nextLine();
	    PreparedStatement check =
	            con.prepareStatement(
	                    "SELECT * FROM applications WHERE student_id=? AND company_name=?"
	            );
	    check.setInt(1, id);
	    check.setString(2, company);
	    ResultSet rs = check.executeQuery();
	    if (rs.next()) {
	        System.out.println("\nAlready Applied!");
	        rs.close();
	        check.close();
	        con.close();
	        return;
	    }
	    rs.close();
	    check.close();
	    PreparedStatement ps =
	            con.prepareStatement(
	                    "INSERT INTO applications(student_id, company_name, status) VALUES (?, ?, ?)"
	            );
	    ps.setInt(1, id);
	    ps.setString(2, company);
	    ps.setString(3, "Applied");
	    int rows = ps.executeUpdate();
	    if (rows > 0) {
	        System.out.println("\nApplication Submitted Successfully");
	    }
	    ps.close();
	    con.close();
	}
	public static void dashboard() throws Exception {
	    Connection con = DBConnection.getConnection();
	    System.out.println("Enter Student ID:");
	    int id = sc.nextInt();
	    PreparedStatement ps1 =
	            con.prepareStatement(
	                    "SELECT * FROM students WHERE student_id=?"
	            );
	    ps1.setInt(1, id);
	    ResultSet student =
	            ps1.executeQuery();
	    if (!student.next()) {
	        System.out.println("Student Not Found");
	        student.close();
	        ps1.close();
	        con.close();
	        return;
	    }
	    System.out.println("\n===== DASHBOARD =====");
	    System.out.println("Name: "+student.getString("name") );
	    System.out.println("CGPA: "+student.getDouble("cgpa"));
	    System.out.println("Skills: "+ student.getString("skills"));
	    System.out.println("\nApplications:");
	    PreparedStatement ps2 =
	            con.prepareStatement(
	                    "SELECT * FROM applications WHERE student_id=?"
	            );

	    ps2.setInt(1, id);
	    ResultSet app=ps2.executeQuery();        
	    boolean found = false;
	    while (app.next()) {
	        found = true;
	        System.out.println(
	                app.getString("company_name")+"--"+app.getString("status"));
	    }
	    if (!found) {
	        System.out.println("No Applications");
	    }
	    app.close();
	    ps2.close();
	    student.close();
	    ps1.close();
	    con.close();
	}
	public static void updateSkills() throws Exception {
	    Connection con=DBConnection.getConnection();
	    System.out.println("Enter Student ID:");
	    int id =sc.nextInt();
	    sc.nextLine();
	    System.out.println("Enter New Skills:");
	    String skills=sc.nextLine();
	    PreparedStatement ps =
	            con.prepareStatement(
	                    "UPDATE students SET skills=? WHERE student_id=?"
	            );
	    ps.setString(1,skills);
	    ps.setInt(2,id);
	    int rows =ps.executeUpdate();
	    if (rows>0){
	        System.out.println("\nSkills Updated Successfully");
	    }
	    else {
	        System.out.println( "\nStudent Not Found");
	    }
	    ps.close();
	    con.close();
	}
	public static void main(String[] args) {
		while(true) {
			try {
				System.out.println("\n----CAREERMATCH----");
				System.out.println("1.Register Student");
				System.out.println("2.Add Company");
				System.out.println("3.check Eligibility:");
				System.out.println("4.Apply");
				System.out.println("5.Dashboard");
				System.out.println("6.Update Skills");
				System.out.println("7.Exit");
				int ch=sc.nextInt();
				sc.nextLine();
				switch(ch) {
				case 1:
					registerStudent();
					break;
				case 2:
					addCompany();
					break;
				case 3:
					checkEligibility();
					break;
				case 4:
					apply();
					break;
				case 5:
					dashboard();
					break;
				case 6:
					updateSkills();
					break;
				case 7:
					System.exit(0);
				default:
					System.out.println("Invalid");
				}
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
