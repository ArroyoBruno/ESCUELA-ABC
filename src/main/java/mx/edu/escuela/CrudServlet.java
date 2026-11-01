package mx.edu.escuela;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

@WebServlet(urlPatterns={"/alumnos","/profesores","/materias","/profesor-materia","/alumno-materia"})
public class CrudServlet extends HttpServlet {

    public static class Field {
        String name;
        String label;
        String type;
        boolean required;

        Field(String n, String l, String t, boolean r) {
            this.name = n;
            this.label = l;
            this.type = t;
            this.required = r;
        }

        public String getName() {
            return name;
        }

        public String getLabel() {
            return label;
        }

        public String getType() {
            return type;
        }

        public boolean isRequired() {
            return required;
        }
    }

    private static final Map<String,String> TITLES=Map.of("alumnos","Alumnos","profesores","Profesores","materias","Materias","profesor-materia","Asignación de profesores y materias","alumno-materia","Inscripciones y calificaciones");
    private String entity(HttpServletRequest req){String p=req.getServletPath(); return p.startsWith("/")?p.substring(1):p;}
    private List<Field> fields(String e){return switch(e){
      case "alumnos" -> List.of(new Field("NOMBRE","Nombre","text",true),new Field("EDAD","Edad","number",true),new Field("PROMEDIO","Promedio","number",true));
      case "profesores" -> List.of(new Field("NOMBRE","Nombre","text",true),new Field("ESPECIALIDAD","Especialidad","text",true));
      case "materias" -> List.of(new Field("NOMBRE","Nombre de la materia","text",true),new Field("CREDITOS","Créditos","number",true));
      case "profesor-materia" -> List.of(new Field("PROFESOR_ID","Profesor","select-profesor",true),new Field("MATERIA_ID","Materia","select-materia",true));
      default -> List.of(new Field("ALUMNO_ID","Alumno","select-alumno",true),new Field("MATERIA_ID","Materia","select-materia",true),new Field("CALIFICACION","Calificación","number",false));
    };}
    private String table(String e){return switch(e){case "alumnos"->"ALUMNO";case "profesores"->"PROFESOR";case "materias"->"MATERIA";case "profesor-materia"->"PROFESOR_MATERIA";default->"ALUMNO_MATERIA";};}
    private boolean relation(String e){return e.equals("profesor-materia")||e.equals("alumno-materia");}
    private List<String> keys(String e){return e.equals("profesor-materia")?List.of("PROFESOR_ID","MATERIA_ID"):e.equals("alumno-materia")?List.of("ID"):List.of("ID");}
    private String listSql(String e){return switch(e){
      case "alumnos"->"SELECT ID,NOMBRE,EDAD,PROMEDIO FROM ALUMNO ORDER BY ID";
      case "profesores"->"SELECT ID,NOMBRE,ESPECIALIDAD FROM PROFESOR ORDER BY ID";
      case "materias"->"SELECT ID,NOMBRE,CREDITOS FROM MATERIA ORDER BY ID";
      case "profesor-materia"->"SELECT pm.PROFESOR_ID,pm.MATERIA_ID,p.NOMBRE AS PROFESOR,m.NOMBRE AS MATERIA FROM PROFESOR_MATERIA pm JOIN PROFESOR p ON p.ID=pm.PROFESOR_ID JOIN MATERIA m ON m.ID=pm.MATERIA_ID ORDER BY pm.PROFESOR_ID,pm.MATERIA_ID";
      default->"SELECT am.ID,am.ALUMNO_ID,am.MATERIA_ID,a.NOMBRE AS ALUMNO,m.NOMBRE AS MATERIA,am.CALIFICACION FROM ALUMNO_MATERIA am JOIN ALUMNO a ON a.ID=am.ALUMNO_ID JOIN MATERIA m ON m.ID=am.MATERIA_ID ORDER BY am.ID";
    };}
    private List<Map<String,Object>> query(String sql) throws SQLException { List<Map<String,Object>> out=new ArrayList<>(); try(Connection c=DB.getConnection(); Statement s=c.createStatement(); ResultSet r=s.executeQuery(sql)){ResultSetMetaData md=r.getMetaData(); while(r.next()){Map<String,Object> row=new LinkedHashMap<>();for(int i=1;i<=md.getColumnCount();i++)row.put(md.getColumnLabel(i),r.getObject(i));out.add(row);}} return out; }
    private Map<String,Object> one(String e,String id) throws SQLException {
      String sql; if(e.equals("profesor-materia")){String[] k=id.split(",",-1);if(k.length!=2) return null;sql="SELECT PROFESOR_ID,MATERIA_ID FROM PROFESOR_MATERIA WHERE PROFESOR_ID=? AND MATERIA_ID=?";try(Connection c=DB.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setLong(1,Long.parseLong(k[0]));p.setLong(2,Long.parseLong(k[1]));return row(p);}}
      String col=e.equals("alumno-materia")?"ID":"ID";sql="SELECT * FROM "+table(e)+" WHERE "+col+"=?";try(Connection c=DB.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setLong(1,Long.parseLong(id));return row(p);}
    }
    private Map<String,Object> row(PreparedStatement p)throws SQLException{try(ResultSet r=p.executeQuery()){if(!r.next())return null;ResultSetMetaData md=r.getMetaData();Map<String,Object> m=new HashMap<>();for(int i=1;i<=md.getColumnCount();i++)m.put(md.getColumnLabel(i),r.getObject(i));return m;}}
    private List<Map<String,Object>> options(String sql)throws SQLException{return query(sql);}
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
      String e=entity(req),action=Optional.ofNullable(req.getParameter("action")).orElse("list");if(!TITLES.containsKey(e)){resp.sendError(404);return;}req.setAttribute("entity",e);req.setAttribute("title",TITLES.get(e));req.setAttribute("fields",fields(e));
      try{
        if(action.equals("form")){String id=req.getParameter("id");req.setAttribute("editing",id!=null&&!id.isBlank());req.setAttribute("record",id==null?new HashMap<String,Object>():one(e,id));req.setAttribute("recordId",id);setOptions(req);req.getRequestDispatcher("/WEB-INF/views/form.jsp").forward(req,resp);}
        else {req.setAttribute("rows",query(listSql(e)));req.getRequestDispatcher("/WEB-INF/views/list.jsp").forward(req,resp);}
      }catch(Exception ex){throw new ServletException("No se pudo consultar la base de datos. Verifica MySQL y la configuración de conexión.",ex);}
    }
    private void setOptions(HttpServletRequest req)throws SQLException{
      req.setAttribute("alumnos",options("SELECT ID,NOMBRE FROM ALUMNO ORDER BY NOMBRE"));req.setAttribute("profesores",options("SELECT ID,NOMBRE FROM PROFESOR ORDER BY NOMBRE"));req.setAttribute("materias",options("SELECT ID,NOMBRE FROM MATERIA ORDER BY NOMBRE"));
    }
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
      req.setCharacterEncoding("UTF-8");String e=entity(req),action=req.getParameter("action");if(!TITLES.containsKey(e)){resp.sendError(404);return;}
      try{
        if("delete".equals(action)){delete(e,req);req.getSession().setAttribute("flash","Registro eliminado correctamente.");}
        else {save(e,req);req.getSession().setAttribute("flash","Registro guardado correctamente.");}
        resp.sendRedirect(req.getContextPath()+"/"+e);
      }catch(SQLIntegrityConstraintViolationException ex){req.getSession().setAttribute("error","No se puede eliminar o guardar: el registro está relacionado con otros datos, o la relación ya existe.");resp.sendRedirect(req.getContextPath()+"/"+e);}
      catch(IllegalArgumentException ex){req.getSession().setAttribute("error","Revisa los datos: los campos numéricos deben contener valores válidos.");resp.sendRedirect(req.getContextPath()+"/"+e+"?action=form");}
      catch(Exception ex){throw new ServletException("Error al guardar los datos. Revisa la conexión con MySQL y los valores enviados.",ex);}
    }
    private void save(String e,HttpServletRequest req)throws SQLException{
      List<Field> fs=fields(e);String id=req.getParameter("id");boolean edit=id!=null&&!id.isBlank();
      if(e.equals("profesor-materia")){
        String prof=req.getParameter("PROFESOR_ID"),mat=req.getParameter("MATERIA_ID");if(edit){String[] old=req.getParameter("oldKey").split(",");try(Connection c=DB.getConnection();PreparedStatement p=c.prepareStatement("UPDATE PROFESOR_MATERIA SET PROFESOR_ID=?,MATERIA_ID=? WHERE PROFESOR_ID=? AND MATERIA_ID=?")){p.setLong(1,Long.parseLong(prof));p.setLong(2,Long.parseLong(mat));p.setLong(3,Long.parseLong(old[0]));p.setLong(4,Long.parseLong(old[1]));p.executeUpdate();}}else try(Connection c=DB.getConnection();PreparedStatement p=c.prepareStatement("INSERT INTO PROFESOR_MATERIA(PROFESOR_ID,MATERIA_ID) VALUES(?,?)")){p.setLong(1,Long.parseLong(prof));p.setLong(2,Long.parseLong(mat));p.executeUpdate();}return;
      }
      List<String> names=new ArrayList<>();List<String> vals=new ArrayList<>();for(Field f:fs){String v=req.getParameter(f.name);if(f.required&&(v==null||v.isBlank()))throw new IllegalArgumentException();names.add(f.name);vals.add(v==null||v.isBlank()?null:v.trim());}
      if(e.equals("alumnos")){int edad=Integer.parseInt(vals.get(1));BigDecimal prom=new BigDecimal(vals.get(2));if(edad<1||edad>120||prom.compareTo(BigDecimal.ZERO)<0||prom.compareTo(new BigDecimal("10.00"))>0)throw new IllegalArgumentException();}
      if(e.equals("materias")&&Integer.parseInt(vals.get(1))<1)throw new IllegalArgumentException();
      if(e.equals("alumno-materia")){BigDecimal cal=vals.get(2)==null?null:new BigDecimal(vals.get(2));if(cal!=null&&(cal.compareTo(BigDecimal.ZERO)<0||cal.compareTo(new BigDecimal("10.00"))>0))throw new IllegalArgumentException();}
      if(edit){StringBuilder sql=new StringBuilder("UPDATE "+table(e)+" SET ");for(int i=0;i<names.size();i++){if(i>0)sql.append(',');sql.append(names.get(i)).append("=?");}sql.append(" WHERE ID=?");try(Connection c=DB.getConnection();PreparedStatement p=c.prepareStatement(sql.toString())){bind(p,vals);p.setLong(vals.size()+1,Long.parseLong(id));p.executeUpdate();}}
      else {StringBuilder sql=new StringBuilder("INSERT INTO "+table(e)+" (");sql.append(String.join(",",names)).append(") VALUES (").append(String.join(",",Collections.nCopies(names.size(),"?"))).append(')');try(Connection c=DB.getConnection();PreparedStatement p=c.prepareStatement(sql.toString())){bind(p,vals);p.executeUpdate();}}
    }
    private void bind(PreparedStatement p,List<String> vals)throws SQLException{for(int i=0;i<vals.size();i++){String v=vals.get(i);if(v==null)p.setNull(i+1,Types.NULL);else p.setString(i+1,v);}}
    private void delete(String e,HttpServletRequest req)throws SQLException{
      if(e.equals("profesor-materia")){String[] k=req.getParameter("id").split(",");try(Connection c=DB.getConnection();PreparedStatement p=c.prepareStatement("DELETE FROM PROFESOR_MATERIA WHERE PROFESOR_ID=? AND MATERIA_ID=?")){p.setLong(1,Long.parseLong(k[0]));p.setLong(2,Long.parseLong(k[1]));p.executeUpdate();}}
      else {try(Connection c=DB.getConnection();PreparedStatement p=c.prepareStatement("DELETE FROM "+table(e)+" WHERE ID=?")){p.setLong(1,Long.parseLong(req.getParameter("id")));p.executeUpdate();}}
    }
}
